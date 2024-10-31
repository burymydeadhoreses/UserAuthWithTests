package io.github.burymydeadhoreses.userauthwithtests.repositories;

import io.github.burymydeadhoreses.userauthwithtests.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {
}
