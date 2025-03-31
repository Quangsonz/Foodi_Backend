package G9.Foodi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import G9.Foodi.repository.MenuRepository;
import G9.Foodi.repository.UserRepository;
import G9.Foodi.service.OrderService;
import G9.Foodi.model.Order;

@RestController
@RequestMapping("/api/v1/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MenuRepository menuRepository;
    
    @Autowired
    private OrderService orderService;
    
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getAdminStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Get counts
        stats.put("users", userRepository.count());
        stats.put("menuItems", menuRepository.count());
        stats.put("orders", orderService.countTotalOrders());
        stats.put("revenue", orderService.calculateTotalRevenue());
        
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/orders/recent")
    public ResponseEntity<List<Order>> getRecentOrders() {
        List<Order> recentOrders = orderService.getRecentOrders();
        return ResponseEntity.ok(recentOrders);
    }
    
    @GetMapping("/order-stats")
    public ResponseEntity<List<Map<String, Object>>> getOrderStats() {
        // Mock order stats for now
        List<Map<String, Object>> orderStats = List.of(
            Map.of(
                "category", "Food",
                "quantity", 45,
                "total", 1350.00
            ),
            Map.of(
                "category", "Drinks",
                "quantity", 25,
                "total", 500.00
            )
        );
        
        return ResponseEntity.ok(orderStats);
    }
} 