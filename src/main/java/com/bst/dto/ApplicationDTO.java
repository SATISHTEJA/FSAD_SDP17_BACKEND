package com.bst.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDTO {

    private Long id;
    private String status;
    private String role;
    private String university;
    private String appliedDate;
    private String description;
    private String internshipTitle;
    private String companyName;
    private String location;
    private String duration;
    private String stipend;
    
}