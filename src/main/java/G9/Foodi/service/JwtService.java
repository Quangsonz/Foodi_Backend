package G9.Foodi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import G9.Foodi.dto.JwtResponse;
import G9.Foodi.security.JwtUtil;

@Service
public class JwtService {

    @Autowired
    private JwtUtil jwtUtil;

    public JwtResponse generateToken(String username) {
        String token = jwtUtil.generateToken(username);
        return new JwtResponse(token);
    }
}
