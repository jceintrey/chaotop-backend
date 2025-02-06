package fr.jerem.chaotop_backend.service;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import fr.jerem.chaotop_backend.model.AppUserDetails;
import fr.jerem.chaotop_backend.model.DataBaseEntityUser;
import fr.jerem.chaotop_backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * UserDetailsService class used to load user from Database.
 * This class implements Spring Security {@link UserDetailsService} interface.
 * 
 * <p>
 * The {@code loadUserByUsername} method is implemented to load the user from
 * the repository if exist in a {@link DataBaseEntityUser} java object
 * </p>
 * 
 * 
 */

@Service
@AllArgsConstructor
@Slf4j
@Primary
public class DatabaseUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    /**
     * Loads a user from the database by their email and returns a Spring Security
     * {@link UserDetails} object.
     * <p>
     * This method is called by Spring Security during the authentication process.
     * </p>
     * 
     * @param email the email of the user to load
     * @return a {@link UserDetails} object representing the authenticated user
     * @throws UsernameNotFoundException if no user with the given email is found in
     *                                   the repository
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        DataBaseEntityUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + email + "not found"));

        return new AppUserDetails(user);

    }

}
