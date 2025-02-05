package fr.jerem.chaotop_backend.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import fr.jerem.chaotop_backend.dto.MessageResponse;
import fr.jerem.chaotop_backend.model.DataBaseEntityUser;
import fr.jerem.chaotop_backend.model.MessageEntity;
import fr.jerem.chaotop_backend.model.RentalEntity;
import fr.jerem.chaotop_backend.repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Default implementation of {@link MessageService}.
 * <p>
 * This service handles the creation of messages by validating the existence of
 * both the user and the rental before persisting the message to the database.
 * </p>
 */
@Service
@Slf4j
public class DefaultMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final RentalService rentalService;
    private final UserManagementService userManagementService;

    public DefaultMessageService(
            MessageRepository messageRepository,
            RentalService rentalService,
            UserManagementService userManagementService) {
        this.messageRepository = messageRepository;
        this.rentalService = rentalService;
        this.userManagementService = userManagementService;
    }

    @Override
    public MessageResponse createMessage(Long userId, Long rentalId, String message)
            throws UsernameNotFoundException, IllegalArgumentException {
        // get user if exist
        log.debug("Try to create a new message");
        Optional<DataBaseEntityUser> optionalEntityUser = userManagementService.getUserEntityById(userId);
        DataBaseEntityUser user = optionalEntityUser
                .orElseThrow(() -> new UsernameNotFoundException("User with userId " + userId + " not found"));

        // get rental if exist

        Optional<RentalEntity> optionalRentalEntity = rentalService.getRentalEntityById(rentalId);
        RentalEntity rental = optionalRentalEntity
                .orElseThrow(() -> new IllegalArgumentException("Rental with rentalId " + rentalId + " not found"));

        // Build MessageEntity
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setCreatedAt(LocalDateTime.now());
        messageEntity.setUpdatedAt(LocalDateTime.now());
        messageEntity.setMessage(message);
        messageEntity.setUser(user);
        messageEntity.setRental(rental);

        // save to repository
        messageRepository.save(messageEntity);

        return new MessageResponse("Message sent with success");
    }

}
