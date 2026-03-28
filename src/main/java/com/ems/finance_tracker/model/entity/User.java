package com.ems.finance_tracker.model.entity;

import com.ems.finance_tracker.model.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a User within the financial tracking system.
 *
 * <p>Each user has a name, email, an encrypted password hash, and access permissions
 * based on assigned roles.</p>
 *
 * @author Evandro Machado
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(exclude = "passwordHash") // Exclude password hash for security reasons.
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 50)
    @Column(nullable = false, length = 50)
    private String name;

    @Email
    @NotBlank
    @Size(min = 6, max = 60)
    @Column(nullable = false, unique = true, length = 60) // Unique constraint for authentication purposes.
    private String email;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String passwordHash;

    /**
     * Set of user roles.
     * Uses {@link Set} to prevent duplicate roles.
     * Mapping is handled via join table 'user_roles' with LAZY loading.
     * Values are persisted as {@link Enum} of type {@link Role}.
     */
    @Builder.Default
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
