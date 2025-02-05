package fr.jerem.chaotop_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.jerem.chaotop_backend.dto.MessageRequest;
import fr.jerem.chaotop_backend.dto.MessageResponse;

import fr.jerem.chaotop_backend.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller class responsible for handling message-related operations.
 * <p>
 * This class provides endpoints for creating and retrieving messages within the
 * application.
 * </p>
 * <p>
 * - {@link MessageService} service that manage messages.
 * </p>
 * 
 */
@Tag(name = "MessageController", description = "Manage messages for rental purpose.")
@RestController
@RequestMapping("/api/messages")
@Slf4j
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * Post a new message.
     * 
     * <p>
     * Use MessageService to add the posted message.
     * </p>
     * 
     * @param {@link MessageRequest} the request DTO.
     * @return {@link MessageResponse} the response DTO.
     */
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Post a message", description = "This endpoint allows a user to send a message regarding a erntal.", responses = {
            @ApiResponse(responseCode = "200", description = "Message sent successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request, an error occured"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),

    })
    @PostMapping("")
    public ResponseEntity<MessageResponse> createMessage(@Valid @RequestBody MessageRequest messageRequest) {
        log.debug("@PostMapping(\"\") --> createMessage");
        log.trace(messageRequest.toString());
        try {
            log.trace("messageRequest.getId(): " + messageRequest.getUserId());
            log.trace("messageRequest.getRentalId() " + messageRequest.getRentalId());
            log.trace(" messageRequest.getMessage() " + messageRequest.getMessage());
            MessageResponse messageResponse = messageService.createMessage(messageRequest.getUserId(),
                    messageRequest.getRentalId(),
                    messageRequest.getMessage());
            return ResponseEntity.ok(messageResponse);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("{}"));
        }
    }
}
