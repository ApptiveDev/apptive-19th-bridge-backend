package apptive.london.bridge.domain.call.service;

import apptive.london.bridge.domain.call.entity.Call;
import apptive.london.bridge.domain.call.entity.CallRequest;
import apptive.london.bridge.domain.call.repository.CallRepository;
import apptive.london.bridge.domain.call.repository.CallRequestRepository;
import apptive.london.bridge.domain.user.entity.User;
import apptive.london.bridge.domain.call.sse.CallSseEmitters;
import apptive.london.bridge.global.error.exception.CustomException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCallService {

    private final CallRepository callRepository;
    private final CallRequestRepository callRequestRepository;
    private final CallSseEmitters callSseEmitters;
    private final EntityManager entityManager;

    public SseEmitter registerCallRequest(User user, Long creatorId) {
        if (callRequestRepository.findByUser(user).isPresent()) {
            throw new CustomException("이미 다른 크리에이터에게 통화 신청을 한 상태입니다.", HttpStatus.CONFLICT);
        }
        
        Call call = callRepository.findByCreatorId(creatorId).orElseThrow(() -> new CustomException("해당 크리에이터는 지금 통화가능상태가 아닙니다.", HttpStatus.BAD_REQUEST));

        SseEmitter sseEmitter = callSseEmitters.addToWaitPool(creatorId, user.getId());

        if (call.getFan() == null) {
            call.setFan(user);
            callRepository.save(call);

            callSseEmitters.notifyResultToCreatorAndFan(creatorId, user.getId());
        } else {
            CallRequest callRequest = CallRequest.builder()
                    .call(call)
                    .user(user)
                    .build();

            callRequestRepository.save(callRequest);
        }

        return sseEmitter;
    }

    public void cancelCallRequest(User user) {
        CallRequest callRequest = callRequestRepository.findByUser(user).orElseThrow(() -> new CustomException("전화 신청하지 않은 사용자입니다", HttpStatus.NOT_FOUND));

        callRequestRepository.deleteByUser(user);
        callSseEmitters.removeFanEmitter(user, callRequest.getCall().getCreator().getId());
    }
}
