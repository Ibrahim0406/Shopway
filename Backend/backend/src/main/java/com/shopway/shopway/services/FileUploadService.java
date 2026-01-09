package com.shopway.shopway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


/*
 * Servis za upload fajlova na S3-kompatibilni storage (Bunny CDN).
 */
@Service
public class FileUploadService {

    @Autowired
    private S3Client s3Client;

    @Value("${FILE_ZONE}")
    private String bucketName;

    /*
     * Uploaduje fajl na S3 storage.
     *
     * Proces:
     * 1. Konvertuje MultipartFile u byte array
     * 2. Kreira PutObjectRequest sa bucket imenom, nazivom fajla i content type-om
     * 3. Šalje fajl na S3 korišćenjem S3Client-a
     *
     * @param file MultipartFile objekat - fajl koji se uploaduje
     * @param fileName naziv pod kojim će fajl biti sačuvan na storage-u
     * @return 201 ako je upload uspešan, 500 ako je došlo do greške
     */
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

