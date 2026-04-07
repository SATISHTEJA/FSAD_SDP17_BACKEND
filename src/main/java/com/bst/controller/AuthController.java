package com.bst.controller;

import com.bst.model.Employer;
import com.bst.model.Student;
import com.bst.repo.EmployerRepo;
import com.bst.repo.StudentRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


    @RestController
    @RequestMapping("/api/auth")
    @CrossOrigin(origins = {"http://localhost:3000","http://localhost:5173","http://localhost:5174"})
    public class AuthController {

        @Autowired
        private StudentRepo studentRepo;

        @Autowired
        private EmployerRepo employerRepo;

        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody Map<String, String> request) {

            String email = request.get("email");
            String password = request.get("password");
            String role = request.get("role");

            if ("student".equals(role)) {

                Student student = studentRepo.findByEmail(email).orElse(null);

                if (student == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Map.of("message", "Student not found"));
                }

                if (!student.getPassword().equals(password)) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(Map.of("message", "Invalid password"));
                }

                return ResponseEntity.ok(Map.of(
                        "id", student.getId(),
                        "university", student.getUniversity(),
                        "name", student.getName(),
                        "email", student.getEmail(),
                        "joiningYear", student.getJoiningyear(),
                        "graduatedYear", student.getGraduatedyear(),
                        "role", "STUDENT"
                ));
            }

            else if ("admin".equals(role)) {

                Employer employer = employerRepo.findByEmail(email).orElse(null);

                if (employer == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Map.of("message", "Admin not found"));
                }

                if (!employer.getPassword().equals(password)) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(Map.of("message", "Invalid password"));
                }

                return ResponseEntity.ok(Map.of(
                        "id", employer.getId(),
                        "empname", employer.getEmpname(),
                        "companyname", employer.getCompanyname(),
                        "email", employer.getEmail(),
                        "role", "EMPLOYER"
                ));
            }

            return ResponseEntity.badRequest().body(Map.of("message", "Invalid role"));
        }
        
        
        @PostMapping("/register")
        public ResponseEntity<?> register(@RequestBody Map<String, String> request) {

            String role = request.get("role");

            if ("student".equalsIgnoreCase(role)) {

                Student student = new Student();
                student.setName(request.get("name"));
                student.setEmail(request.get("email"));
                student.setPassword(request.get("password"));
                student.setStream(request.get("stream"));
                student.setBranch(request.get("branch"));
                student.setUniversity(request.get("organization"));
                String joining = request.get("joiningyear");
                String graduated = request.get("graduatedyear");

                if (joining != null && !joining.isEmpty()) {
                    student.setJoiningyear(Integer.parseInt(joining.substring(0, 4)));
                }

                if (graduated != null && !graduated.isEmpty()) {
                    student.setGraduatedyear(Integer.parseInt(graduated.substring(0, 4)));
                }

                studentRepo.save(student);

                return ResponseEntity.ok("Student registered successfully");
            }

            else if ("admin".equalsIgnoreCase(role)) {

                Employer emp = new Employer();
                emp.setEmpname(request.get("empname"));
                emp.setCompanyname(request.get("companyname"));
                emp.setEmail(request.get("email"));
                emp.setPhonenumber(request.get("phonenumber"));
                emp.setPassword(request.get("password"));
                emp.setLocation(request.get("location"));
                emp.setWebsite(request.get("companyWebsite"));

                employerRepo.save(emp);

                return ResponseEntity.ok("Admin registered successfully");
            }

            return ResponseEntity.badRequest().body("Invalid role");
        }
        
}