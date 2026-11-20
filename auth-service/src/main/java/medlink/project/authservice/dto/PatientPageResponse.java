package medlink.project.authservice.dto;

import java.util.List;

public record PatientPageResponse(
        List<PatientResponse> content,
        int first,
        int max,
        boolean hasNext
) {
}
