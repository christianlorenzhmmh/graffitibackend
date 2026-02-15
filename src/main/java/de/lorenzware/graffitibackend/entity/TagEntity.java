package de.lorenzware.graffitibackend.entity;

import java.util.Set;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "tag")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TagEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(unique = true, nullable = false)
    private String value;

    @OneToMany(mappedBy = "tag")
    private Set<GraffitiEntity> graffitiEntities;

}