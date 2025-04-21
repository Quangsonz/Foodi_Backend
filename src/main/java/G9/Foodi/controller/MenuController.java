package G9.Foodi.controller;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import G9.Foodi.model.Menu;
import G9.Foodi.service.MenuService;
import G9.Foodi.service.UserService;
import G9.Foodi.repository.MenuRepository;

@RestController
@RequestMapping("/api/v1/menu")
@CrossOrigin(origins = "http://localhost:5173")
public class MenuController {
    
    @Autowired
    private MenuService menuService;
    
    @Autowired
    private MenuRepository menuRepository;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public ResponseEntity<List<Menu>> getAllMenuItems() {
        List<Menu> menuItems = menuRepository.findAll();
        return ResponseEntity.ok(menuItems);
    }
    
    @PostMapping
    public ResponseEntity<Menu> createMenuItem(@RequestBody Menu menu) {
        return new ResponseEntity<>(menuService.createMenuItem(menu), HttpStatus.CREATED);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMenuItem(@PathVariable String id, Authentication authentication) {
        try {
            System.out.println("Attempting to delete menu item with ID: " + id);
            System.out.println("User email: " + authentication.getName());
            
            // Check if user is admin
            String userEmail = authentication.getName();
            if (!userService.isAdmin(userEmail)) {
                System.out.println("User is not admin");
                Map<String, String> error = new HashMap<>();
                error.put("message", "Admin privileges required");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }
            
            System.out.println("User is admin, proceeding with deletion");
            menuService.deleteMenuItem(id);
            System.out.println("Menu item deleted successfully");
            return ResponseEntity.ok().build();
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error deleting menu item: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error deleting menu item: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Menu> getMenuItemById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(menuService.getMenuItemById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Menu> updateMenuItem(@PathVariable String id, @RequestBody Menu menuDetails) {
        try {
            return ResponseEntity.ok(menuService.updateMenuItem(id, menuDetails));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Menu>> searchMenuItems(@RequestParam String query) {
        List<Menu> menuItems = menuRepository.findByNameContainingIgnoreCase(query);
        return ResponseEntity.ok(menuItems);
    }
}