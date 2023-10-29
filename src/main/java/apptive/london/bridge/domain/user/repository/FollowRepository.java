package apptive.london.bridge.domain.user.repository;

import apptive.london.bridge.domain.user.entity.Follow;
import apptive.london.bridge.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    List<Follow> findAllByUser(User user);
}
