package com.chat.messages.messages.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

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

    /**
     * Generate signed parameters for multiple file uploads
     */
    public List<Map<String, String>> getUploadSignatures(String userId, String chatId, List<Map<String, Object>> files) {
        long timestamp = System.currentTimeMillis() / 1000L;
        String folder = "chat_media";

        List<Map<String, String>> result = new ArrayList<>();

        for (Map<String, Object> file : files) {
            String fileName = (String) file.get("name");
            String publicId = folder + "/" + chatId + "/" + userId + "/" + fileName;

            Map<String, Object> paramsToSign = ObjectUtils.asMap(
                    "timestamp", timestamp,
                    "folder", folder,
                    "public_id", publicId
            );

            String signature = cloudinary.apiSignRequest(paramsToSign, cloudinary.config.apiSecret);

            Map<String, String> entry = new HashMap<>();
            entry.put("publicId", publicId);
            entry.put("signature", signature);
            entry.put("folder", folder);
            entry.put("timestamp", String.valueOf(timestamp));
            entry.put("apiKey", cloudinary.config.apiKey);
            entry.put("cloudName", cloudinary.config.cloudName);

            result.add(entry);
        }

        return result;
    }
}
