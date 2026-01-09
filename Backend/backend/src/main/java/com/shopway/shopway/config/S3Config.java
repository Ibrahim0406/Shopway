package com.shopway.shopway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.services.s3.S3Configuration;

import javax.net.ssl.SSLContext;
import java.net.URI;


/*
 * Konfiguraciona klasa za AWS S3 Client (koristi se za Bunny CDN storage).
 * Kreira S3Client bean koji se koristi za upload fajlova na S3-kompatibilni storage.
 */
@Configuration
public class S3Config {

    @Value("${FILE_UPLOAD_KEY}")
    private String accessKey;

    @Value("${FILE_SECRET_KEY}")
    private String secretKey;

    @Value("${FILE_HOST_URL}")
    private String endpoint;


    /*
     * Kreira i konfiguriše S3Client bean za komunikaciju sa S3-kompatibilnim storage-om.
     *
     * Konfiguracija:
     * - endpointOverride:CustomURI za Bunny CDN (nije standardni AWS S3 endpoint)
     * - credentials: Access key i secret key za autentifikaciju
     * - region: AWS region (postavljen na "eu-west-1", ali se ne koristi za Bunny CDN)
     * - pathStyleAccessEnabled: Omogućava path-style pristup (bucket-name/key umesto bucket-name.domain/key)
     * - httpClient: Apache HTTP client za TLS podršku
     *
     * @return konfigurisani S3Client objekat
     */
    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .region(Region.of("eu-west-1"))
                // Ovo je ispravan način za SDK v2:
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .httpClient(ApacheHttpClient.builder().build())
                .build();
    }
}
