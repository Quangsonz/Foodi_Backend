package G9.Foodi.controller;

// Import các thư viện cần thiết
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

// Đánh dấu đây là một REST controller, trả về dữ liệu JSON thay vì view
@RestController
// Định nghĩa prefix cho tất cả các endpoint trong controller này
@RequestMapping("/api/v1/admin")
// Cho phép CORS từ frontend (React) ở địa chỉ này
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {
    
    // Inject UserRepository để thao tác với dữ liệu người dùng
    @Autowired
    private UserRepository userRepository;
    
    // Inject MenuRepository để thao tác với dữ liệu menu
    @Autowired
    private MenuRepository menuRepository;
    
    // Inject OrderService để thao tác với logic đơn hàng
    @Autowired
    private OrderService orderService;
    
    // Endpoint trả về thống kê tổng quan cho admin
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getAdminStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Lấy tổng số người dùng
        stats.put("users", userRepository.count());
        // Lấy tổng số món ăn trong menu
        stats.put("menuItems", menuRepository.count());
        // Lấy tổng số đơn hàng
        stats.put("orders", orderService.countTotalOrders());
        // Tính tổng doanh thu
        stats.put("revenue", orderService.calculateTotalRevenue());
        
        // Trả về kết quả dưới dạng JSON
        return ResponseEntity.ok(stats);
    }
    
    // Endpoint trả về danh sách các đơn hàng gần đây
    @GetMapping("/orders/recent")
    public ResponseEntity<List<Order>> getRecentOrders() {
        List<Order> recentOrders = orderService.getRecentOrders();
        return ResponseEntity.ok(recentOrders);
    }
    
    // Endpoint trả về thống kê đơn hàng theo từng loại (mock data)
    @GetMapping("/order-stats")
    public ResponseEntity<List<Map<String, Object>>> getOrderStats() {
        // Dữ liệu mẫu cho thống kê đơn hàng (có thể thay bằng truy vấn thực tế)
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