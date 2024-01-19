package apptive.london.bridge.domain.call.sse;

import apptive.london.bridge.domain.user.entity.Creator;
import apptive.london.bridge.domain.user.entity.User;
import apptive.london.bridge.global.error.exception.CustomException;
import apptive.london.bridge.global.third_party.agora.AgoraTokenBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class CallSseEmitters {
    private final AgoraTokenBuilder agoraTokenBuilder;

    private final Map<Long, SseEmitter> creatorEmitters = new HashMap<>();
    private final Map<Long, Map<Long, SseEmitter>> fansWaitEmitters = new HashMap<>();


    /**
     * 크리에이터가 전화를 개설했을때 알림을 받을 sseEmitter를 반환한다.
     * @param creator
     * @return
     */
    public SseEmitter getCallSseEmitterFromCreator(Creator creator) {
        SseEmitter emitter = new SseEmitter(60L * 1000);

        // Emitter를 map에 저장하기
        creatorEmitters.put(creator.getId(), emitter);
        // 팬이 SSE emitter를 등록할 list를 생성
        fansWaitEmitters.put(creator.getId(), new HashMap<>());

        // SSE Emitter의 완료 이벤트 처리
        emitter.onCompletion(() -> {
            creatorEmitters.remove(creator.getId());
        });

        // SSE Emitter의 타임아웃 이벤트 처리: 단순히 SseEmitter를 완료 상태로 설정합니다.
        emitter.onTimeout(emitter::complete);

        // connect 되었다는 메세지를 보냅니다.
        sendConnectedMessage(emitter);

        return emitter;
    }

    public SseEmitter getCallSseEmitter(Creator creator) {
        SseEmitter emitter = new SseEmitter(60L * 1000 * 60);

        // Emitter를 map에 저장하기
        creatorEmitters.put(creator.getId(), emitter);

        emitter.onCompletion(() -> creatorEmitters.remove(creator.getId()));
        emitter.onTimeout(emitter::complete);

        sendConnectedMessage(emitter);

        return emitter;
    }

    public SseEmitter getCallSseEmitter(User user, Creator creator) {
        SseEmitter emitter = new SseEmitter(60L * 1000 * 60);

        // Emitter를 map에 저장하기
        fansWaitEmitters.get(creator.getId()).put(user.getId(), emitter);

        emitter.onCompletion(() -> creatorEmitters.remove(creator.getId()));
        emitter.onTimeout(emitter::complete);

        sendConnectedMessage(emitter);

        return emitter;
    }

    /**
     * 팬을 전화대기풀에 등록하고 결과를 받을 sseEmitter를 반환한다.
     * @param creatorId 대기중인 전화의 상대인 크리에이터 id
     * @return
     */
    public SseEmitter addToWaitPool(Long creatorId, Long fanId) {
        SseEmitter emitter = new SseEmitter(60L * 1000 * 60);

        if (!fansWaitEmitters.containsKey(creatorId)) {
            throw new CustomException("해당 크리에이터의 전화 대기열이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }

        fansWaitEmitters.get(creatorId).put(fanId, emitter);
        emitter.onCompletion(() -> {
            fansWaitEmitters.get(creatorId).remove(emitter);
        });
        emitter.onTimeout(emitter::complete);

        sendConnectedMessage(emitter);

        return emitter;
    }

    /**
     * Emitter를 생성하고 나서 만료 시간까지 아무런 데이터도 보내지 않으면 재연결 요청시 503 Service Unavailable 에러가 발생할 수 있다.
     * 이를 막기위해 연결되었다는 메세지(의미없음)를 하나 보내는 메서드
     * @param emitter 메세지를 보내야되는 SseEmitter
     */
    private void sendConnectedMessage(SseEmitter emitter) {
        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("connected!"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 해당 크리에이터와 크리에이터에게 전화신청한 팬중 랜덤으로 한명을 선택해 결과 성공을 알린다.
     * 실패한 사람들에게는 실패를 알린다.
     *
     * @param creatorId
     */
    public void notifyResultToCreatorAndFan(Long creatorId, Long userId) {
        try {
            SseEmitter creatorEmitter = creatorEmitters.get(creatorId);
            sendSseEvent(creatorEmitter, "success", true);
            sendSseEvent(creatorEmitter, "channel_name", creatorId);
            sendSseEvent(creatorEmitter, "token", agoraTokenBuilder.getTokenByPublisher(creatorId.toString()));

            SseEmitter selectedFansEmitter = fansWaitEmitters.get(creatorId).get(userId);

            sendSseEvent(selectedFansEmitter, "success", true);
            sendSseEvent(selectedFansEmitter, "channel_name", creatorId);
            sendSseEvent(selectedFansEmitter, "token", agoraTokenBuilder.getTokenBySubscriber(creatorId.toString()));
        } catch (IOException e) {
            throw new CustomException("전화 매칭결과 전송실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void sendSseEvent(SseEmitter emitter, String eventName, Object data) throws IOException {
        emitter.send(SseEmitter.event().name(eventName).data(data));
    }


    public void notifyCallEndToFan(Long id) {
        creatorEmitters.remove(id);
        fansWaitEmitters.get(id).forEach((key, value) -> {
            try {
                sendSseEvent(value, "creator close", "close");
            } catch (IOException e) {
                throw new CustomException("크리에이터 종료 알림을 팬에게 전달하는 과정에서 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        });
        fansWaitEmitters.remove(id);
    }

    public void removeFanEmitter(User user, Long creatorId) {
        fansWaitEmitters.get(creatorId).remove(user.getId());
    }
}