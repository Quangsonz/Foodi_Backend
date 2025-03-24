package G9.Foodi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import G9.Foodi.dto.UserDto;
import G9.Foodi.model.User;
import G9.Foodi.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;
    
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {
        try {
            return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/admin/{email}")
    public ResponseEntity<Boolean> getAdmin(@PathVariable String email, Authentication authentication) {
        // Kiểm tra xem email được yêu cầu có khớp với người dùng đã xác thực không
        if (!email.equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(false);
        }
        
        boolean isAdmin = userService.isAdmin(email);
        return ResponseEntity.ok(isAdmin);
    }
    
    @PutMapping("/admin/{id}")
    public ResponseEntity<User> makeAdmin(@PathVariable String id) {
        try {
            return ResponseEntity.ok(userService.makeAdmin(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}