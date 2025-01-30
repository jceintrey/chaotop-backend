package fr.jerem.chaotop_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.jerem.chaotop_backend.model.MessageEntity;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> { 

}
