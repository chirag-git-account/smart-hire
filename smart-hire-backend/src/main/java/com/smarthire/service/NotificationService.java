package com.smarthire.service;

import com.smarthire.dto.NotificationDto;
import com.smarthire.entity.Notification;
import com.smarthire.entity.User;
import com.smarthire.entity.enums.NotificationType;
import com.smarthire.repository.NotificationRepository;
import com.smarthire.repository.UserRepository;
import com.smarthire.security.UserPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void sendNotification(User recipient, String title, String message, NotificationType type, String linkUrl) {
        if (recipient == null) return;
        Notification notification = Notification.builder()
                .user(recipient)
                .title(title)
                .message(message)
                .type(type)
                .linkUrl(linkUrl)
                .isRead(false)
                .build();
        notificationRepository.save(notification);
    }

    public List<NotificationDto> getUserNotifications(UserPrincipal principal) {
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return notificationRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public long getUnreadCount(UserPrincipal principal) {
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return notificationRepository.countByUserAndIsReadFalse(user);
    }

    @Transactional
    public NotificationDto markAsRead(UserPrincipal principal, Long notificationId) {
        Notification n = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));

        if (!n.getUser().getId().equals(principal.getId())) {
            throw new IllegalArgumentException("Access denied to notification");
        }

        n.setIsRead(true);
        return mapToDto(notificationRepository.save(n));
    }

    @Transactional
    public void markAllAsRead(UserPrincipal principal) {
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Notification> unread = notificationRepository.findByUserOrderByCreatedAtDesc(user);
        for (Notification n : unread) {
            if (!n.getIsRead()) {
                n.setIsRead(true);
            }
        }
        notificationRepository.saveAll(unread);
    }

    private NotificationDto mapToDto(Notification n) {
        return NotificationDto.builder()
                .id(n.getId())
                .title(n.getTitle())
                .message(n.getMessage())
                .type(n.getType() != null ? n.getType().name() : "SYSTEM_ALERT")
                .isRead(n.getIsRead())
                .linkUrl(n.getLinkUrl())
                .createdAt(n.getCreatedAt())
                .build();
    }
}
