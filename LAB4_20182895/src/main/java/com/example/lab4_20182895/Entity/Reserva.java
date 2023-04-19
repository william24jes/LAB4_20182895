package com.example.lab4_20182895.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "reserva")
public class Reserva {

    @Id
    @Column(name = "idreserva")
    private int idreserva;

    @Column(nullable = false)
    private String fecha_reserva;
    @Column(nullable = false)
    private float precio_total;
    @Column(nullable = false)
    private String estado_pago;

    @ManyToOne
    @JoinColumn(name = "user_iduser")
    private User user;

    @ManyToOne
    @JoinColumn(name = "vuelo_idvuelo")
    private Vuelo vuelo;


}
