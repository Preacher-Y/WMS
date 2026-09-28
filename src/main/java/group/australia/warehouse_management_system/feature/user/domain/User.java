package group.australia.warehouse_management_system.feature.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private Long id;

    private String name;

    private String password;

    @Builder.Default
    private boolean active = true;

    private Integer version;

    private LocalDateTime createdTimestamp;

    private LocalDateTime updatedTimestamp;

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    public void rename(String name) {
        this.name = name;
    }
}
