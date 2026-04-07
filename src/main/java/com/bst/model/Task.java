package com.bst.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "task")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private LocalDate deadline;

    private String status; // You can convert this to ENUM later
    
    private String submissionDescription;
    private String submissionFileName;
    
    @JsonIgnore
    @Column(columnDefinition = "LONGTEXT")
    private String submissionFileData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "internship_id")
    private Internship internship;
    
    @JsonIgnore
    @OneToMany(mappedBy = "task")
    private List<Evaluation> evaluations;
}