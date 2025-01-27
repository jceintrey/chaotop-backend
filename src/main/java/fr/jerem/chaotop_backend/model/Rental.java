package fr.jerem.chaotop_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * Represents a rental entity in the database.
 * <p>
 * This class is mapped to the {@code rentals} table in the database, and
 * contains
 * the rental's information including the price, the surface, the creation and update
 * dates, the name and a picture, and also the user who is the owner as foreign keys in the database.
 * </p>
 * 
 * Lombock is used to generate Getters/Setters and the empty constructor needed
 * by JPA.
 * 
 */
@Entity
@Table(name = "rentals")
@Getter
@Setter
@NoArgsConstructor
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "surface")
    private double surface;

    @Column(name = "price", precision = 10, scale = 2, nullable = false, columnDefinition = "NUMERIC")
    private BigDecimal price;

    @Column(name = "picture", length = 255)
    private String picture;

    @Column(name = "description", length = 2000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false) // Définir la clé étrangère vers USERS
    private DataBaseEntityUser owner;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt;
}
