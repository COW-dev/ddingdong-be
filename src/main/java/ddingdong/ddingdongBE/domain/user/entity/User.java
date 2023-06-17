package ddingdong.ddingdongBE.domain.user.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    @Embedded
    private Password password;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public User(String userId, Password password, String name, Role role) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.role = role;
    }

}
