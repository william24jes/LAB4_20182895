package com.example.lab4_20182895.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "vuelo")
public class Vuelo {
    @Id
    @Column(name = "idvuelo")
    private int idvuelo;

    @Column(nullable = false)
    private String origen;
    @Column(nullable = false)
    private String destino;
    @Column(nullable = false)
    private String fecha_salida;
    @Column(nullable = false)
    private String fecha_llegada;
    @Column(nullable = false)
    private int duracion;
    @Column(nullable = false)
    private float precio;

    @ManyToOne
    @JoinColumn(name = "aerolinea_idaerolinea")
    private Aerolinea aerolinea;

    @Column(nullable = false)
    private int asientos_disponibles;
    @Column(nullable = false)
    private String descripcion;
}
