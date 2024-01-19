package apptive.london.bridge.domain.call.controller;

import apptive.london.bridge.domain.call.service.UserCallService;
import apptive.london.bridge.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/call")
@Transactional
public class UserCallController {

    private final UserCallService userCallService;

    @PostMapping("/request/{creatorId}")
    public ResponseEntity<SseEmitter> registerCallRequest(@AuthenticationPrincipal User user, @PathVariable("creatorId") Long creatorId) {
        SseEmitter sseEmitter = userCallService.registerCallRequest(user, creatorId);
        return ResponseEntity.ok(sseEmitter);
    }

    @GetMapping("/keep")
    public ResponseEntity<SseEmitter> keepWait(@AuthenticationPrincipal User user) {
        SseEmitter sseEmitter = userCallService.keepWait(user);
        return ResponseEntity.ok(sseEmitter);
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<?> cancelCallRequest(@AuthenticationPrincipal User user) {
        userCallService.cancelCallRequest(user);
        return ResponseEntity.ok(null);
    }
}
