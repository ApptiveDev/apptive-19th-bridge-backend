package apptive.london.bridge.domain.call.repository;

import apptive.london.bridge.domain.call.entity.Call;
import apptive.london.bridge.domain.call.entity.CallRequest;
import apptive.london.bridge.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CallRequestRepository extends JpaRepository<CallRequest, Long> {
    Optional<CallRequest> findByUser(User user);

    void deleteByUser(User user);

    Optional<CallRequest> findByCall(Call call);


    long countByCall(Call call);

    void deleteByCall(Call call);

    boolean existsByCall(Call call);

    void deleteAllByCall(Call call);

    List<CallRequest> findAllByCall(Call call);
}
