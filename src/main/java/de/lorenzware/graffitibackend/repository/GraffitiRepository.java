package de.lorenzware.graffitibackend.repository;

import de.lorenzware.graffitibackend.entity.GraffitiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GraffitiRepository extends JpaRepository<GraffitiEntity, Long> {
//    Page<Graffiti> findByStatus(String status, Pageable pageable);


    @Query(value = "SELECT * FROM graffiti g WHERE g.latitude BETWEEN :minLat AND :maxLat AND g.longitude BETWEEN :minLon AND :maxLon ORDER BY g.created_at DESC LIMIT :max", nativeQuery = true)
    List<GraffitiEntity> findGraffitiInRectangle(
            @Param("minLat") double minLat,
            @Param("maxLat") double maxLat,
            @Param("minLon") double minLon,
            @Param("maxLon") double maxLon,
            @Param("max") int max
    );

    @Query(value = "SELECT * FROM graffiti g WHERE g.latitude BETWEEN :minLat AND :maxLat AND g.longitude BETWEEN :minLon AND :maxLon"
        + " AND (:minCreatedAt IS NULL OR g.created_at >= :minCreatedAt)"
        + " AND (:maxCreatedAt IS NULL OR g.created_at <= :maxCreatedAt)"
        + " AND (:tagIds IS NULL OR g.tag_id IN (:tagIds))"
        + " ORDER BY g.created_at DESC LIMIT :max", nativeQuery = true)
    List<GraffitiEntity> findGraffitiWithFilters(
            @Param("minLat") double minLat,
            @Param("maxLat") double maxLat,
            @Param("minLon") double minLon,
            @Param("maxLon") double maxLon,
            @Param("minCreatedAt") String minCreatedAt,
            @Param("maxCreatedAt") String maxCreatedAt,
            @Param("tagIds") List<Long> tagIds,
            @Param("max") int max
    );
}
