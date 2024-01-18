package apptive.london.bridge.domain.call.entity;

import apptive.london.bridge.domain.user.entity.User;
import apptive.london.bridge.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor
@SuperBuilder
@Getter
public class CallRequest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Call call;

    @OneToOne
    private User user;
}
