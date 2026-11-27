package medlink.project.schedulingservice.dto;

import java.util.List;

public record GenerationCreneauxResponse(
        int createdCount,
        List<CreneauDto> creneaux
) {
}
