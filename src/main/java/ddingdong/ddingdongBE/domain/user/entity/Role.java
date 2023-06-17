package ddingdong.ddingdongBE.domain.user.entity;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
    USER("ROLE_USER"),
    CLUB("ROLE_CLUB,ROLE_USER"),
    ADMIN("ROLE_ADMIN,ROLE_CLUB,ROLE_USER");

    private final String roles;

    public List<String> getRoles() {
        return Arrays.asList(roles.split(","));
    }
}
