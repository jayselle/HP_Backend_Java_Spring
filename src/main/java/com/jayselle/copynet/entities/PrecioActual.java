package com.jayselle.copynet.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "precioactual")
public class PrecioActual {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id_precioactual")
    private Long id_precioactual;

    @Column(name="preciocopia_precioactual")
    private Float preciocopia_precioactual;

    @Column(name="preciocolor_precioactual")
    private Float preciocolor_precioactual;

    @Column(name="precioanillado_precioactual")
    private Float precioanillado_precioactual;

    @Column(name="fechadesde_precioactual")
    private LocalDateTime fechadesde_precioactual;

    @Column(name="fechahasta_precioactual")
    private LocalDateTime fechahasta_precioactual;
}
