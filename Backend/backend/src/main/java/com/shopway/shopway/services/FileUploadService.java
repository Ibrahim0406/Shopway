package com.shopway.shopway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class FileUploadService {

    @Autowired
    private S3Client s3Client;

    @Value("${FILE_ZONE}")
    private String bucketName;

    public int uploadFile(MultipartFile file, String fileName) {
        try {
            byte[] bytes = file.getBytes();
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest,
                    RequestBody.fromBytes(bytes));

            return 201; // Uspešno kreirano
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(System.getProperty("https.protocols"));
            return 500;
        }
    }
}

