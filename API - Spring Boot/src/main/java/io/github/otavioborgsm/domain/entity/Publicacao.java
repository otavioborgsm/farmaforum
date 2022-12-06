package io.github.otavioborgsm.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "publicacao")
public class Publicacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String conteudo;

    @Column
    private LocalDateTime dataCriacao;

    @ManyToOne
    @JoinColumn(name = "usuario")
    private Usuario usuario;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "publicacao")
    private List<Comentario> comentarios;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "publicacao")
    private List<FarmacoPublicacao> farmacoPublicacaoList;
}
