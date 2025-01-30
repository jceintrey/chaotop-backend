package fr.jerem.chaotop_backend.service;

import fr.jerem.chaotop_backend.dto.MessageResponse;

public interface MessageService {

  


    public MessageResponse createMessage(Long userId, Long rentalId, String message);

    
} 
