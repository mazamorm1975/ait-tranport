package com.ait.transporte.dto;

import com.ait.transporte.model.AssignmentFile;
import com.ait.transporte.model.AssignmentFileType;

public record AssignmentFileDTO(
        AssignmentFileType type,
        String fileName,
        String contentType,
        long size
) {
    public static AssignmentFileDTO fromEntity(AssignmentFile file) {
        return new AssignmentFileDTO(
                file.getType(),
                file.getFileName(),
                file.getContentType(),
                file.getContent().length
        );
    }
}
