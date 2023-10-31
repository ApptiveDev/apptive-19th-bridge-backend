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

    @Query("SELECT f FROM Follow f JOIN FETCH f.creator c LEFT JOIN FETCH c.profileImg WHERE f.user = :user")
    List<Follow> findByUserWithCreatorAndProfileImg(@Param("user") User user);

    @Query("SELECT f FROM Follow f JOIN FETCH f.user u LEFT JOIN FETCH u.profileImg WHERE f.creator = :creator")
    List<Follow> findByCreatorWithUserAndProfileImg(@Param("creator") Creator creator);

    @Query("SELECT f FROM Follow f JOIN FETCH f.user u LEFT JOIN FETCH u.profileImg WHERE f.creator = :creator AND f.blockStatus = false")
    List<Follow> findNonBlockByCreator(@Param("creator") Creator creator);

    @Query("SELECT f FROM Follow f JOIN FETCH f.user u LEFT JOIN FETCH u.profileImg WHERE f.creator = :creator AND f.blockStatus = true")
    List<Follow> findBlockByCreator(@Param("creator") Creator creator);
}
