package ru.sirius.hl.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;



@Entity
@Table(name = "customer", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 256)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;
}