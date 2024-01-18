package apptive.london.bridge.domain.call.controller;

import apptive.london.bridge.domain.call.dto.CallToken;
import apptive.london.bridge.domain.call.dto.CreatorCallConfig;
import apptive.london.bridge.domain.call.service.CreatorCallService;
import apptive.london.bridge.domain.user.entity.Creator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/creator/call")
public class CreatorCallController {

    private final CreatorCallService creatorCallService;

    /**
     * 크리에이터가 전화 시작을 위해 세션을 생성하는 메서드
     * @param creator 전화 시작을 요청한 크리에이터
     * @param creatorCallConfig 전화 환경설정을 위한 설정 DTO
     * @return agora voice call을 위한 call token
     */
    @PostMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> startCall(@AuthenticationPrincipal Creator creator, @RequestBody CreatorCallConfig creatorCallConfig) {
        SseEmitter sseEmitter = creatorCallService.startCall(creator, creatorCallConfig);
        return ResponseEntity.ok(sseEmitter);
    }

    @GetMapping("/keep")
    public ResponseEntity<SseEmitter> keepEmitter(@AuthenticationPrincipal Creator creator, @RequestBody CreatorCallConfig creatorCallConfig) {
        SseEmitter sseEmitter = creatorCallService.keepEmitter(creator);
        return ResponseEntity.ok(sseEmitter);
    }

    @PostMapping("/next")
    public ResponseEntity<SseEmitter> nextCall(@AuthenticationPrincipal Creator creator) {
        SseEmitter sseEmitter = creatorCallService.nextCall(creator);
        return ResponseEntity.ok(sseEmitter);
    }

    @DeleteMapping("/stop")
    public ResponseEntity<?> stopCall(@AuthenticationPrincipal Creator creator) {
        creatorCallService.stopCall(creator);
        return ResponseEntity.ok(null);
    }
}
