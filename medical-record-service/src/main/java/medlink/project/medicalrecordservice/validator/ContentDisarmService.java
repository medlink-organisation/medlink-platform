package medlink.project.medicalrecordservice.validator;

import java.io.InputStream;


public interface ContentDisarmService {

    InputStream sanitizePdf(InputStream inputStream);
}
