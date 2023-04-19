package com.example.lab4_20182895.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "aerolinea")
public class Aerolinea {
    @Id
    @Column(name = "idaerolinea")
    private int idaerolinea;

    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String codigo;
}
