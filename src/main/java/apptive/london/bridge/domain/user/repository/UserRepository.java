package apptive.london.bridge.domain.user.repository;

import apptive.london.bridge.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("select u from User u left join fetch u.profileImg where u.id = :userId")
    Optional<User> findWithProfileImgById(@Param("userId") Long userId);

}
