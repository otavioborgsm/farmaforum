package io.github.otavioborgsm.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "farmacoPublicacao")
public class FarmacoPublicacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "publicacao")
    @NotFound(action = NotFoundAction.IGNORE)
    private Publicacao publicacao;

    @ManyToOne
    @JoinColumn(name = "farmaco")
    @NotFound(action = NotFoundAction.IGNORE)
    private Farmaco farmaco;
}
