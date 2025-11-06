package main

import (
	"fmt"
	"log"
	"net"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/gofiber/fiber/v2"
	"github.com/gofiber/fiber/v2/middleware/cors"
	"github.com/gofiber/fiber/v2/middleware/logger"
	"github.com/hudl/fargo"
)

const (
	SERVICE_NAME = "fcm-service"
	SERVICE_PORT = 8080
	EUREKA_URL   = "http://localhost:8761/eureka"
)

// getLocalIP tries to find the local, non-loopback IP
func getLocalIP() string {
	addrs, err := net.InterfaceAddrs()
	if err != nil {
		log.Printf("Could not get local IP, defaulting to localhost: %v", err)
		return "localhost"
	}

	for _, addr := range addrs {
		if ipNet, ok := addr.(*net.IPNet); ok && !ipNet.IP.IsLoopback() && ipNet.IP.To4() != nil {
			return ipNet.IP.String()
		}
	}
	return "localhost"
}

func main() {
	ip := getLocalIP()

	app := fiber.New(fiber.Config{
		AppName: SERVICE_NAME,
	})

	app.Use(logger.New())
	app.Use(cors.New())

	app.Get("/health", func(c *fiber.Ctx) error {
		return c.JSON(fiber.Map{
			"status":    "UP",
			"service":   SERVICE_NAME,
			"timestamp": time.Now().Unix(),
			"ip":        ip,
		})
	})

	app.Get("/fcm", func(c *fiber.Ctx) error {
		return c.JSON(fiber.Map{
			"message": "FCM Service is running",
			"ip":      ip,
		})
	})

	eurekaConnection := fargo.NewConn(EUREKA_URL)

	instance := &fargo.Instance{
		HostName:         ip,
		IPAddr:           ip,
		App:              SERVICE_NAME,
		Port:             SERVICE_PORT,
		VipAddress:       SERVICE_NAME,
		SecureVipAddress: SERVICE_NAME,
		Status:           fargo.UP,
		DataCenterInfo: fargo.DataCenterInfo{
			Class: "com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo",
			Name:  "MyOwn",
		},
		LeaseInfo: fargo.LeaseInfo{
			RenewalIntervalInSecs: 30,
			DurationInSecs:        90,
		},
	}

	go func() {
		log.Printf("Registering %s with Eureka at %s (IP: %s)", SERVICE_NAME, EUREKA_URL, ip)
		err := eurekaConnection.RegisterInstance(instance)
		if err != nil {
			log.Printf("Failed to register with Eureka: %v", err)
		} else {
			log.Printf("Successfully registered with Eureka")
		}

		ticker := time.NewTicker(30 * time.Second)
		defer ticker.Stop()

		for range ticker.C {
			if err := eurekaConnection.HeartBeatInstance(instance); err != nil {
				log.Printf("Failed to send heartbeat: %v", err)
			}
		}
	}()

	// graceful shutdown
	c := make(chan os.Signal, 1)
	signal.Notify(c, os.Interrupt, syscall.SIGTERM)
	go func() {
		<-c
		log.Println("Shutting down gracefully...")
		eurekaConnection.DeregisterInstance(instance)
		app.Shutdown()
	}()

	log.Printf("Starting %s on %s:%d", SERVICE_NAME, ip, SERVICE_PORT)
	log.Fatal(app.Listen(fmt.Sprintf(":%d", SERVICE_PORT)))
}
