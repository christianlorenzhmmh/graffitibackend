package de.lorenzware.graffitibackend.repository;

import de.lorenzware.graffitibackend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    List<UserEntity> findByEmailContainingIgnoreCase(String email);
}
