package io.github.amichailides.merimna.audit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    Page<AuditLog> findBySubjectEmployeePublicId(UUID subjectEmployeePublicId, Pageable pageable);
}