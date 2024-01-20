package apptive.london.bridge.domain.user.repository;

import apptive.london.bridge.domain.user.entity.Creator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CreatorRepository extends JpaRepository<Creator, Long> {

    @Query("SELECT c FROM Creator c JOIN FETCH c.channelLinks")
    List<Creator> findAllWithChannelLink();

}
