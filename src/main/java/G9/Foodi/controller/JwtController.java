package G9.Foodi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import G9.Foodi.dto.JwtResponse;
import G9.Foodi.service.JwtService;

@RestController
@RequestMapping("/api/v1/jwt")
public class JwtController {

    @Autowired
    private JwtService jwtService;
    
    // Trong yêu cầu gốc, đầu vào là một đối tượng người dùng nhưng chúng ta chỉ cần email
    @PostMapping
    public JwtResponse generateToken(@RequestBody Email email) {
        return jwtService.generateToken(email.getEmail());
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