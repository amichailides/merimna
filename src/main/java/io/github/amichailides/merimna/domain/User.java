package io.github.amichailides.merimna.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NonNull
    @Column(unique = true, nullable = false)
    private String username;

    // TODO: Use BCryptPasswordEncoder.encode() στο Service πριν save (αποθήκευση hashed password)
    @Setter(AccessLevel.NONE)
    @Column(nullable = false)
    private String password;

    @NonNull
    @Column(unique = true, nullable = false)
    private String email;

    @NonNull
    @Column(nullable = false)
    private String firstName;

    @NonNull
    @Column(nullable = false)
    private String lastName;

    private String mobile;

    @Builder.Default
    private boolean active = true;

    @Builder.Default
    @Setter(AccessLevel.NONE)  // super defensive (whatever happens in future don't even think to create a setter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    // TODO: Προσθηκη @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Role> roles = new HashSet<>();

    public void addRole(@NonNull Role role) {
        this.roles.add(role);
    }

    public void removeRole(@NonNull Role role) {
        this.roles.remove(role);
    }

    public void setEncodedPassword(String encodedPassword) {
        if (encodedPassword == null || encodedPassword.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        this.password = encodedPassword;
    }
}
