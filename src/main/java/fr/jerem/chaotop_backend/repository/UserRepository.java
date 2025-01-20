package fr.jerem.chaotop_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.jerem.chaotop_backend.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByUsername(String username);
}
