package apptive.london.bridge.domain.user.repository;

import apptive.london.bridge.domain.user.entity.ProfileImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileImgRepository extends JpaRepository<ProfileImg, Long> {

}
