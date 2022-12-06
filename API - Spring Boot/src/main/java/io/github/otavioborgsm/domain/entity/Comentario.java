package io.github.otavioborgsm.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "comentario")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String conteudo;

    @Column
    private Boolean refutado;

    @Column
    private LocalDateTime dataCriacao;

    @ManyToOne
    @JoinColumn(name = "publicacao")
    private Publicacao publicacao;

    @ManyToOne
    @JoinColumn(name = "usuario")
    private Usuario usuario;
}
