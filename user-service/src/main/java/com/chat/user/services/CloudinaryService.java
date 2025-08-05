package com.chat.user.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CloudinaryService {

    private Cloudinary cloudinary;

    @Value("${cloudinary.cloud.name}")
    private String cloudName;
    @Value("${cloudinary.api.key}")
    private String apiKey;
    @Value("${cloudinary.api.secret}")
    private String apiSecret;

    @PostConstruct
    public void init() {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    public Map<String, Object> getUploadSignature(String userId) {
        long timestamp = System.currentTimeMillis() / 1000L;
        String folder = "user_images";
        String publicId = folder + "/" + userId;

        Map<String, Object> paramsToSign = ObjectUtils.asMap(
                "timestamp", timestamp,
                "folder", folder,
                "public_id", publicId
        );

        String signature = cloudinary.apiSignRequest(paramsToSign, cloudinary.config.apiSecret);

        return Map.of(
                "signature", signature,
                "timestamp", timestamp,
                "apiKey", cloudinary.config.apiKey,
                "cloudName", cloudinary.config.cloudName,
                "folder", folder,
                "image_id", userId
        );
    }


}
