package fr.jerem.chaotop_backend.service;


import fr.jerem.chaotop_backend.dto.MessageResponse;

/**
 * Service interface for managing messages created by users for specific
 * rentals.
 * <p>
 * This service defines the contract for creating messages.
 * 
 * </p>
 * 
 * @see DefaultMessageService for the default implementation.
 */
public interface MessageService {

    /**
     * Creates and stores a message for a rental.
     * 
     * @param userId   the ID of the user sending the message
     * @param rentalId the ID of the rental associated with the message
     * @param message  the content of the message
     * @return a {@link MessageResponse} indicating the operation's success
     */
    public MessageResponse createMessage(Long userId, Long rentalId, String message);

}
