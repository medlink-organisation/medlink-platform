package medlink.project.notificationservice.controller;

import medlink.project.notificationservice.dto.MarkedReadCountDto;
import medlink.project.notificationservice.dto.NotificationDto;
import medlink.project.notificationservice.dto.UnreadCountDto;
import medlink.project.notificationservice.service.NotificationService;
import medlink.project.notificationservice.validation.NotificationRoleValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@PreAuthorize("hasAnyRole('PATIENT', 'PRATICIEN', 'ADMIN')")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationRoleValidator roleValidator;

    public NotificationController(
            NotificationService notificationService,
            NotificationRoleValidator roleValidator
    ) {
        this.notificationService = notificationService;
        this.roleValidator = roleValidator;
    }

    @GetMapping
    public Page<NotificationDto> getMine(
            @AuthenticationPrincipal Jwt jwt,
            Authentication authentication,
            @RequestParam(defaultValue = "false") boolean unreadOnly,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return notificationService.getForUser(
                jwt.getSubject(),
                roleValidator.allowedRecipientTypes(authentication),
                unreadOnly,
                pageable
        );
    }

    @GetMapping("/unread-count")
    public UnreadCountDto unreadCount(
            @AuthenticationPrincipal Jwt jwt,
            Authentication authentication
    ) {
        return new UnreadCountDto(notificationService.countUnread(
                jwt.getSubject(), roleValidator.allowedRecipientTypes(authentication)
        ));
    }

    @PatchMapping("/{notificationId}/read")
    public NotificationDto markAsRead(
            @AuthenticationPrincipal Jwt jwt,
            Authentication authentication,
            @PathVariable UUID notificationId
    ) {
        return notificationService.markAsRead(
                notificationId,
                jwt.getSubject(),
                roleValidator.allowedRecipientTypes(authentication)
        );
    }

    @PatchMapping("/read-all")
    public MarkedReadCountDto markAllAsRead(
            @AuthenticationPrincipal Jwt jwt,
            Authentication authentication
    ) {
        return new MarkedReadCountDto(notificationService.markAllAsRead(
                jwt.getSubject(), roleValidator.allowedRecipientTypes(authentication)
        ));
    }
}
