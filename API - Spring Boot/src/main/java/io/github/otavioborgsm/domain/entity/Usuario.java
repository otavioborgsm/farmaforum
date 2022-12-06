package io.github.otavioborgsm.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DiscriminatorFormula;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nome;

    @Column
    private String sobrenome;

    @Column
    @NotEmpty(message = "{campo.login.obrigatorio}")
    private String login;

    @Column
    @NotEmpty(message = "{campo.senha.obrigatorio}")
    private String senha;

    @Column
    private boolean admin;

    @Column
    private LocalDate dataCriacao;

    @Column
    private boolean contaVerificada;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "usuario")
    private Set<Publicacao> publicacoes;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "usuario")
    private List<Comentario> comentarios;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "usuario")
    private Set<TokenVerificacao> tokens;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "usuario")
    private Farmaceutico farmaceutico;
}
