package com.ems.finance_tracker.model.entity;

import com.ems.finance_tracker.model.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a User within the financial tracking system.
 *
 * Each user has a name, email, an encrypted password hash, and access permissions
 * based on assigned roles.
 *
 * @author Evandro Machado <a href="https://github.com/e0machado">Github</a>
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "passwordHash") // Exclude password hash for security reasons.
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String name;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true, length = 60) // Unique constraint for authentication purposes.
    private String email;

    @NotBlank
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

}
