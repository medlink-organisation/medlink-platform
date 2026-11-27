package medlink.project.notificationservice.mapper;

import medlink.project.notificationservice.dto.CreateNotificationRequest;
import medlink.project.notificationservice.dto.NotificationDto;
import medlink.project.notificationservice.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationDto toDto(Notification notification);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "emailRecipient", source = "email")
    @Mapping(target = "read", constant = "false")
    @Mapping(target = "readAt", ignore = true)
    @Mapping(target = "emailSent", constant = "false")
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    Notification toEntity(CreateNotificationRequest request);
}
