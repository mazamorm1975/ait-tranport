package com.ait.transporte.repository;

import com.ait.transporte.model.AssignmentFile;
import com.ait.transporte.model.AssignmentFileType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssignmentFileRepository extends JpaRepository<AssignmentFile, UUID> {
    List<AssignmentFile> findAllByAssignment_Id(UUID assignmentId);

    Optional<AssignmentFile> findByAssignment_IdAndType(UUID assignmentId, AssignmentFileType type);
}
