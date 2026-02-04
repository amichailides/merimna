package io.github.amichailides.merimna.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE) // super defensive (whatever happens in future don't even think to create a setter
    private Long id;

    @NonNull @Setter private String username;
    @NonNull @Setter private String password;
    @NonNull @Setter private String email;
    @NonNull @Setter private String firstName;
    @NonNull @Setter private String lastName;
    @Setter private String mobile;
    @Setter private boolean active = true;

    @Builder.Default
    @Setter(AccessLevel.NONE)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public void addRole(@NonNull Role role) {
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
    }
}
