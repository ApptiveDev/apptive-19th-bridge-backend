package apptive.london.bridge.domain.call.repository;

import apptive.london.bridge.domain.call.entity.Call;
import apptive.london.bridge.domain.user.entity.Creator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CallRepository extends JpaRepository<Call, Long> {
    Optional<Call> findByCreator(Creator creator);

    void deleteByCreator(Creator creator);

    Optional<Call> findByCreatorId(Long creatorId);
}
