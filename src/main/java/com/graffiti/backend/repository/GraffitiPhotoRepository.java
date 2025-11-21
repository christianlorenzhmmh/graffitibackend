package com.graffiti.backend.repository;

import com.graffiti.backend.entity.GraffitiPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GraffitiPhotoRepository extends JpaRepository<GraffitiPhoto, Long> {
}
