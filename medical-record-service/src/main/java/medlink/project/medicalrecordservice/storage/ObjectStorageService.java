package medlink.project.medicalrecordservice.storage;

import java.io.InputStream;

public interface ObjectStorageService {

    String upload(InputStream inputStream, long size, String contentType);

    InputStream download(String objectKey);

    void delete(String objectKey);
}
