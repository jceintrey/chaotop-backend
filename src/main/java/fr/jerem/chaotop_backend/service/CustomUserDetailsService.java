package fr.jerem.chaotop_backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import fr.jerem.chaotop_backend.model.DBUser;
import fr.jerem.chaotop_backend.repository.UserRepository;
import lombok.AllArgsConstructor;

/**
 * UserDetailsService class used to load user from Database
 * 
 * <p>
 * The {@code loadUserByUsername} method is implemented to load the user from
 * the Database if exist in a DBUser java object
 * </p>
 * 
 * 
 */
@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    /**
     * Loads a user from the database by their email and returns a Spring Security
     * {@link UserDetails} object.
     * <p>
     * This method is called by Spring Security during the authentication process.
     * It retrieves the user from the repository using their email address and
     * returns a
     * Spring Security {@link User} object containing the user's
     * credentials (email and password). The granted authorities are set to the
     * default role of "USER", Role-Based Access Control (RBAC) will be implemented
     * later with a
     * dedicated roles table.
     * </p>
     * 
     * @param email the email of the user to load
     * @return a {@link UserDetails} object representing the authenticated user
     * @throws UsernameNotFoundException if no user with the given email is found in
     *                                   the database
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        DBUser user = userRepository.findByEmail(email);
        /*
         * TODO implement RBAC later with a dedicated role table
         */
        return new User(user.getEmail(), user.getPassword(), getGrantedAuthorities("USER"));

    }

    /**
     * Loads a list of Spring Security {@link GrantedAuthority} objects from a
     * a String role
     * <p>
     * This method will be refactored later with RBAC implementation
     * </p>
     * 
     * @param String the role to add to the Authority
     * @return a {@Link List} of {@link GrantedAuthority}
     */
    private List<GrantedAuthority> getGrantedAuthorities(String role) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        return authorities;
    }

}
