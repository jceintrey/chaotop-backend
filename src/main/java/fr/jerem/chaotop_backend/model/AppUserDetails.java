package fr.jerem.chaotop_backend.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AppUserDetails implements UserDetails {

    private final DataBaseEntityUser user;

    public AppUserDetails(DataBaseEntityUser user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO get roles in DB
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