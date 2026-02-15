package de.lorenzware.graffitibackend.repository;

import de.lorenzware.graffitibackend.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
    Optional<TagEntity> findByValue(String value);
    List<TagEntity> findByValueContainingIgnoreCase(String value);
}
