package de.lorenzware.graffitibackend.repository;

import de.lorenzware.graffitibackend.entity.Graffiti;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GraffitiRepository extends JpaRepository<Graffiti, Long> {
    Page<Graffiti> findByStatus(String status, Pageable pageable);
}
