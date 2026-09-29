package com.ait.transporte.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "assignment_file", uniqueConstraints = @UniqueConstraint(
        columnNames = {"assignment_id", "file_type"}))
public class AssignmentFile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private OrderAssignment assignment;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_type", nullable = false)
    private AssignmentFileType type;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Lob
    @Column(name = "content", nullable = false)
    private byte[] content;
}
