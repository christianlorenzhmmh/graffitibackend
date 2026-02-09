package de.lorenzware.graffitibackend.entity;

import java.util.Set;
import jakarta.persistence.*;

@Table(name = "tag")
@Entity

public class TagEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(unique = true, nullable = false)
    private String value;

    @OneToMany(mappedBy = "tag")
    private Set<GraffitiEntity> graffitiEntities;

    public void setGraffitiEntities(Set<GraffitiEntity> graffitiEntities) { this.graffitiEntities = graffitiEntities; }
    public Set<GraffitiEntity> getGraffitiEntities() {
        return graffitiEntities;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }












}