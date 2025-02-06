package fr.jerem.chaotop_backend.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Implementation of {@link UserDetails} representing user-specific information.
 * <p>
 * This class stores and provides access to user-related details.
 * It is used by Spring Security for authentication and authorization,
 * as well as by {@code UserManagementService} for user management operations
 * such as account creation, retrieval, and validation.
 * </p>
 */
public class AppUserDetails implements UserDetails {

    private final UserEntity user;

    public AppUserDetails(UserEntity user) {
        this.user = user;
    }

    /**
     * Retrieves the authorities (roles) granted to the user.
     * <p>
     * Currently, this method returns a default role {@code "ROLE_USER"}.
     * In the future, it should be updated to fetch roles dynamically from the
     * database,
     * ideally using a dedicated roles table.
     * </p>
     * 
     * @return a collection of {@link GrantedAuthority} representing the user's
     *         roles
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO: Retrieve roles from the database
        // Future improvement: Use a dedicated role table
        return List.of(() -> "ROLE_USER");
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    public Long getId() {
        return Long.valueOf(user.getId().longValue());
    }

    public String getName() {
        return user.getName();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public LocalDateTime getCreatedAt() {
        return user.getCreatedAt();
    }

    public LocalDateTime getUpdatedAt() {
        return user.getUpdatedAt();
    }

    @Override
    public String toString() {
        return "Email: " + getEmail() +
                ", Name: " + getName() +
                ", CreatedAt: " + getCreatedAt() +
                ", UpdatedAt: " + getUpdatedAt();
    }
}