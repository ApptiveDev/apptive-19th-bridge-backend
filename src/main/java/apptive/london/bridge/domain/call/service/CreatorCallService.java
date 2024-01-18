package apptive.london.bridge.domain.call.service;

import apptive.london.bridge.domain.call.dto.CreatorCallConfig;
import apptive.london.bridge.domain.call.entity.Call;
import apptive.london.bridge.domain.call.entity.CallRequest;
import apptive.london.bridge.domain.call.repository.CallRepository;
import apptive.london.bridge.domain.call.repository.CallRequestRepository;
import apptive.london.bridge.domain.user.entity.Creator;
import apptive.london.bridge.global.error.exception.CustomException;
import apptive.london.bridge.domain.call.sse.CallSseEmitters;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class CreatorCallService {

    private final CallRepository callRepository;
    private final CallRequestRepository callRequestRepository;
    private final CallSseEmitters callSseEmitters;

    /**
     * 크리에이터 전화 시작시 설정과 토큰 발급
     * @param creator
     * @param creatorCallConfig
     * @return
     */
    public SseEmitter startCall(Creator creator, CreatorCallConfig creatorCallConfig) {

        // 이전 Call가 존재한다면 삭제
        Optional<Call> beforeCall = callRepository.findByCreator(creator);

        if (beforeCall.isPresent()) {
            // 기존 Call 엔티티 수정
            Call call = beforeCall.get();
            call.setDuration(creatorCallConfig.getDuration());
            call.setPrice(creatorCallConfig.getPrice());
            call.setCallType(creatorCallConfig.getCallType());
            call.setRule(creatorCallConfig.getRule());
            callRepository.save(call);
        } else {
            // 새로운 Call 엔티티 생성
            Call call = Call.builder()
                    .creator(creator)
                    .duration(creatorCallConfig.getDuration())
                    .price(creatorCallConfig.getPrice())
                    .callType(creatorCallConfig.getCallType())
                    .rule(creatorCallConfig.getRule())
                    .build();
            callRepository.save(call);
        }

        // SSE emitter 등록 및 반환
        return callSseEmitters.getCallSseEmitterFromCreator(creator);
    }

    public SseEmitter nextCall(Creator creator) {
        Call call = callRepository.findByCreator(creator).orElseThrow(() -> new CustomException("전화 설정을 하지 않았습니다.", HttpStatus.NOT_FOUND));

        SseEmitter emitter = callSseEmitters.getCallSseEmitter(creator);

        if (callRequestRepository.existsByCall(call)) {
            // 랜덤으로 전화신청 추첨
            List<CallRequest> callRequestList = callRequestRepository.findAllByCall(call);
            CallRequest randomCallRequest = callRequestList.get(new Random().nextInt(callRequestList.size()));

            call.setFan(randomCallRequest.getUser());
            callRepository.save(call);

            // 크리에이터와 팬에게 결과 알리기
            callSseEmitters.notifyResultToCreatorAndFan(creator.getId(), randomCallRequest.getUser().getId());

            // 당첨된 팬의 전화신청 엔티티 삭제
            callRequestRepository.delete(randomCallRequest);
        }

        return emitter;
    }

    public void stopCall(Creator creator) {
        Call call = callRepository.findByCreator(creator).orElseThrow(() -> new CustomException("전화중이 아닌 크리에이터입니다.", HttpStatus.BAD_REQUEST));

        callRequestRepository.deleteAllByCall(call);
        callRepository.delete(call);
        callSseEmitters.notifyCallEndToFan(creator.getId());
    }

    public SseEmitter keepEmitter(Creator creator) {
        return callSseEmitters.getCallSseEmitter(creator);
    }
}
