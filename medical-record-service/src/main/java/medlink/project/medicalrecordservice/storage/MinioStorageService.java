package medlink.project.medicalrecordservice.storage;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.ServerSideEncryption;
import lombok.extern.slf4j.Slf4j;
import medlink.project.medicalrecordservice.config.properties.MinioProperties;
import medlink.project.medicalrecordservice.exception.ObjectStorageException;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URI;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class MinioStorageService implements ObjectStorageService {

    private static final String OBJECT_PREFIX = "medical-documents/";

    private final MinioClient minioClient;
    private final String bucket;
    private final ServerSideEncryption serverSideEncryption;

    public MinioStorageService(MinioProperties properties) {
        validateEndpoint(properties.endpoint(), properties.allowHttp());
        this.minioClient = MinioClient.builder()
                .endpoint(properties.endpoint())
                .credentials(properties.accessKey(), properties.secretKey())
                .build();
        this.bucket = properties.bucket();
        this.serverSideEncryption = createServerSideEncryption(properties.sseKmsKeyId());

        if (serverSideEncryption == null) {
            log.warn("MinIO SSE-KMS is not enabled for the medical document bucket");
        }
    }

    @Override
    public String upload(InputStream inputStream, long size, String contentType) {
        String objectKey = OBJECT_PREFIX + UUID.randomUUID() + ".pdf";
        try {
            ensureBucketExists();
            PutObjectArgs.Builder builder = PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectKey)
                    .contentType(contentType)
                    .stream(inputStream, size, -1L);
            if (serverSideEncryption != null) {
                builder.sse(serverSideEncryption);
            }
            minioClient.putObject(builder.build());
            return objectKey;
        } catch (Exception exception) {
            throw new ObjectStorageException("Le stockage du document médical a échoué.", exception);
        }
    }

    @Override
    public InputStream download(String objectKey) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder().bucket(bucket).object(objectKey).build()
            );
        } catch (Exception exception) {
            throw new ObjectStorageException("Le téléchargement du document médical a échoué.", exception);
        }
    }

    @Override
    public void delete(String objectKey) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket(bucket).object(objectKey).build()
            );
        } catch (Exception exception) {
            throw new ObjectStorageException("La suppression du document médical a échoué.", exception);
        }
    }

    private void ensureBucketExists() throws Exception {
        boolean exists = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(bucket).build()
        );
        if (!exists) {
            throw new IllegalStateException(
                    "Le bucket médical privé n'existe pas; sa création relève de l'infrastructure."
            );
        }
    }

    private void validateEndpoint(String endpoint, boolean allowHttp) {
        URI uri = URI.create(endpoint);
        if (!"https".equalsIgnoreCase(uri.getScheme()) && !allowHttp) {
            throw new IllegalStateException(
                    "MinIO doit être joint en HTTPS; minio.allow-http est réservé au développement."
            );
        }
    }

    private ServerSideEncryption createServerSideEncryption(String kmsKeyId) {
        if (kmsKeyId.isBlank()) {
            return null;
        }
        try {
            return new ServerSideEncryption.KMS(kmsKeyId, Map.of());
        } catch (Exception exception) {
            throw new IllegalStateException("La configuration MinIO SSE-KMS est invalide.", exception);
        }
    }
}
