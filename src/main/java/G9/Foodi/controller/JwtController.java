package G9.Foodi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import G9.Foodi.dto.JwtResponse;
import G9.Foodi.service.JwtService;

@RestController
@RequestMapping("/api/v1/jwt")
@CrossOrigin(origins = "http://35.224.60.159:80")
public class JwtController {

    @Autowired
    private JwtService jwtService;
    
    // Trong yêu cầu gốc, đầu vào là một đối tượng người dùng nhưng chúng ta chỉ cần email
    @PostMapping
    public JwtResponse generateToken(@RequestBody Email email) {
        System.out.println("JWT request received for email: " + email.getEmail());
        JwtResponse response = jwtService.generateToken(email.getEmail());
        System.out.println("JWT generated successfully, token length: " + (response.getToken() != null ? response.getToken().length() : 0));
        return response;
    }
    
    // Class đơn giản để nhận email từ request body
    public static class Email {
        private String email;
        
        public Email() {}
        
        public Email(String email) {
            this.email = email;
        }
        
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
    }
} 