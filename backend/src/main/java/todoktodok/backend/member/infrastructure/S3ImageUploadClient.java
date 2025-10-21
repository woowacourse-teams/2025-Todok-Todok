package todoktodok.backend.member.infrastructure;

import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import todoktodok.backend.member.application.ImageType;
import todoktodok.backend.member.infrastructure.exception.AwsApiException;

public class S3ImageUploadClient {

    private final S3Client s3Client;
    private final String bucketName;
    private final Region region;
    private final String keyPrefix;

    public S3ImageUploadClient(
            final S3Client s3Client,
            final String bucketName,
            final Region region,
            final String keyPrefix
    ) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.region = region;
        this.keyPrefix = keyPrefix;
    }

    public ProfileImageResponse uploadImage(final MultipartFile file) {
        return new ProfileImageResponse("");
//        try {
//            final String contentType = file.getContentType();
//            final String extension = ImageType.getExtension(contentType);
//
//            final String key = String.format("%s%s%s", keyPrefix, UUID.randomUUID(), extension);
//            final PutObjectRequest putObj = PutObjectRequest.builder()
//                    .bucket(bucketName)
//                    .key(key)
//                    .contentType(contentType)
//                    .build();
//            s3Client.putObject(putObj, RequestBody.fromBytes(file.getBytes()));
//
//            final String downloadUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region.id(), key);
//
//            return new ProfileImageResponse(downloadUrl);
//        } catch (final S3Exception e) { // S3 API 서버 측 오류
//            throw new AwsApiException(e.getMessage());
//        } catch (final SdkClientException e) { // AWS 입장에서 클라이언트 측(==우리 서버)에서 발생한 오류
//            throw new AwsApiException(e.getMessage());
//        } catch (final Exception e) { // 그 외 오류
//            throw new AwsApiException(e.getMessage());
//        }
    }
}
