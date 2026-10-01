package com.ait.transporte.repository;

import com.ait.transporte.model.AssignmentFile;
import com.ait.transporte.model.AssignmentFileType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssignmentFileRepository extends JpaRepository<AssignmentFile, UUID> {

    //Se realizan los derived queries
    List<AssignmentFile> findAllByAssignment_Id(UUID assignmentId);

    //Devuelve el archivo asignado: Document o Image
    Optional<AssignmentFile> findByAssignment_IdAndType(UUID assignmentId, AssignmentFileType type);
}
