package apptive.london.bridge.domain.call.entity;

import apptive.london.bridge.domain.user.entity.Creator;
import apptive.london.bridge.domain.user.entity.User;
import apptive.london.bridge.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;

@Entity
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class Call extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime duration; // 통화시간

    private Integer price;      // 통화권 가격

    private CallType callType;  // 통화 종류

    private String rule;        // 통화 규칙

    @OneToOne
    private Creator creator;    // 통화를 개설한 크리에이터

    @OneToOne
    private User fan;

    public void setFan(User fan) {
        this.fan = fan;
    }
}
