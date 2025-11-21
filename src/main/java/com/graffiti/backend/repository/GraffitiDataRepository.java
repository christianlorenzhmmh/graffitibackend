package com.graffiti.backend.repository;

import com.graffiti.backend.entity.GraffitiData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GraffitiDataRepository extends JpaRepository<GraffitiData, Long> {
}
