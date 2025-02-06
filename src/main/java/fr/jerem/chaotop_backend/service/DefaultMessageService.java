package fr.jerem.chaotop_backend.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import fr.jerem.chaotop_backend.dto.MessageResponse;
import fr.jerem.chaotop_backend.exception.RentalNotFoundException;
import fr.jerem.chaotop_backend.exception.UserNotFoundException;
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
    public MessageResponse createMessage(Long userId, Long rentalId, String message) {
        // get user Entity
        log.debug("Try to create a new message, userId: " + userId + " and rentalId " + rentalId);

        Optional<DataBaseEntityUser> optionalUserEntity = userManagementService.getUserEntityById(userId);
        if (optionalUserEntity.isEmpty())
            throw new UserNotFoundException("User with id " + userId + " not found",
                    "DefaultMessageService.createMessage");
        DataBaseEntityUser userEntity = optionalUserEntity.get();

        // get rental Entity
        Optional<RentalEntity> optionalRentalEntity = rentalService.getRentalEntityById(rentalId);
        if (optionalRentalEntity.isEmpty())
            throw new RentalNotFoundException("Rental with id " + rentalId + " not found");
        RentalEntity rentalEntity = optionalRentalEntity.get();

        // Build MessageEntity
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setCreatedAt(LocalDateTime.now());
        messageEntity.setUpdatedAt(LocalDateTime.now());
        messageEntity.setMessage(message);
        messageEntity.setUser(userEntity);
        messageEntity.setRental(rentalEntity);

        // save to repository
        messageRepository.save(messageEntity);

        // Build DTO
        MessageResponse messageResponse = new MessageResponse("Message sent with success");

        return messageResponse;
    }

}
