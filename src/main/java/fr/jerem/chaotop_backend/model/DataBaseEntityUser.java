package fr.jerem.chaotop_backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a user entity in the database.
 * <p>
 * This class is mapped to the {@code users} table in the database, and contains
 * the user's information including email, name, password, and timestamps for
 * creation and update.
 * </p>
 * 
 * <p>
 * The {@code email} field is marked as unique to ensure no duplicate user
 * emails
 * exist in the system.
 * </p>
 * Lombock is used to generate Getters/Setters and the empty constructor needed
 * by JPA.
 * 
 */
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Getter
@Setter
@NoArgsConstructor
public class DataBaseEntityUser {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
