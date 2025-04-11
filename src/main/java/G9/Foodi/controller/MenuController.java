package G9.Foodi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import G9.Foodi.model.Menu;
import G9.Foodi.service.MenuService;
import G9.Foodi.repository.MenuRepository;

@RestController
@RequestMapping("/api/v1/menu")
@CrossOrigin(origins = "http://localhost:5173")
public class MenuController {
    
    @Autowired
    private MenuService menuService;
    
    @Autowired
    private MenuRepository menuRepository;
    
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
    public ResponseEntity<Void> deleteMenuItem(@PathVariable String id) {
        try {
            menuService.deleteMenuItem(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
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