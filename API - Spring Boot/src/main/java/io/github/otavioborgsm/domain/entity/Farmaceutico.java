package io.github.otavioborgsm.domain.entity;
import io.github.otavioborgsm.domain.enums.Estado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "farmaceutico")
public class Farmaceutico{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String crf;

    @Enumerated(EnumType.STRING)
    @Column
    private Estado estado;

    @OneToOne
    private Usuario usuario;
}

