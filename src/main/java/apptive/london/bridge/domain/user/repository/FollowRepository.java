package apptive.london.bridge.domain.user.repository;

import apptive.london.bridge.domain.user.entity.Creator;
import apptive.london.bridge.domain.user.entity.Follow;
import apptive.london.bridge.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Follow findByCreatorAndUserId(Creator creator, Long userId);

}
