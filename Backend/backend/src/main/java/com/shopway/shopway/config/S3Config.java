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

@Configuration
public class S3Config {

    @Value("${FILE_UPLOAD_KEY}")
    private String accessKey;

    @Value("${FILE_SECRET_KEY}")
    private String secretKey;

    @Value("${FILE_HOST_URL}")
    private String endpoint;

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
