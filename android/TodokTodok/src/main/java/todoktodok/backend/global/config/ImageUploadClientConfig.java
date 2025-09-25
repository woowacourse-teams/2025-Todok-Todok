package todoktodok.backend.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import todoktodok.backend.member.infrastructure.S3ImageUploadClient;

@Configuration
public class ImageUploadClientConfig {

    @Bean
    public S3ImageUploadClient s3ImageUploadClient(
            @Value("${cloud.aws.s3.bucket}") final String bucketName,
            @Value("${cloud.aws.s3.key-prefix}") final String keyPrefix,
            @Value("${cloud.aws.s3.region}") final String regionValue

    ) {
        final Region region = Region.of(regionValue);
        final S3Client s3Client = S3Client.builder()
                .region(region)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();

        return new S3ImageUploadClient(s3Client, bucketName, region, keyPrefix);
    }
}
