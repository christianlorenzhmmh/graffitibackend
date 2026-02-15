package de.lorenzware.graffitibackend.repository;

import de.lorenzware.graffitibackend.entity.GraffitiEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface GraffitiRepository extends JpaRepository<GraffitiEntity, Long>, JpaSpecificationExecutor< GraffitiEntity> {
//    Page<Graffiti> findByStatus(String status, Pageable pageable);


//    @Query(value = "SELECT * FROM graffiti g WHERE g.latitude BETWEEN :minLat AND :maxLat AND g.longitude BETWEEN :minLon AND :maxLon ORDER BY g.created_at DESC LIMIT :max", nativeQuery = true)
//    List<GraffitiEntity> findGraffitiInRectangle(
//            @Param("minLat") double minLat,
//            @Param("maxLat") double maxLat,
//            @Param("minLon") double minLon,
//            @Param("maxLon") double maxLon,
//            @Param("max") int max
//    );

//    @Query("SELECT g FROM GraffitiEntity g WHERE g.latitude BETWEEN :minLat AND :maxLat " +
//           "AND g.longitude BETWEEN :minLon AND :maxLon " +
//           "AND (:minCreatedAt IS NULL OR g.createdAt >= :minCreatedAt) " +
//           "AND (:maxCreatedAt IS NULL OR g.createdAt <= :maxCreatedAt) " +
//           "AND (:tagIds IS NULL OR g.tag.id IN :tagIds) " +
//           "ORDER BY g.createdAt DESC")
//    List<GraffitiEntity> findGraffitiWithFilters(
//            @Param("minLat") double minLat,
//            @Param("maxLat") double maxLat,
//            @Param("minLon") double minLon,
//            @Param("maxLon") double maxLon,
//            @Param("minCreatedAt") LocalDateTime minCreatedAt,
//            @Param("maxCreatedAt") LocalDateTime maxCreatedAt,
//            @Param("tagIds") List<Long> tagIds,
//            org.springframework.data.domain.Pageable pageable
//    );

    public static Specification< GraffitiEntity> withFilters(Double minLat, Double maxLat, Double minLon, Double maxLon,
                                                           LocalDateTime minCreated, LocalDateTime maxCreated, List<Long> tagIds) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.between(root.get("latitude"), minLat, maxLat));
            predicates.add(cb.between(root.get("longitude"), minLon, maxLon));

            if (minCreated != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), minCreated));
            }

            if (maxCreated != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), maxCreated));
            }

            if (tagIds != null && !tagIds.isEmpty()) {
                predicates.add(root.get("tag").get("id").in(tagIds));
            }
            query.orderBy(cb.desc(root.get("createdAt")));
            // KEIN LIMIT HIER! Limiting erfolgt im Service mit Pageable
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
