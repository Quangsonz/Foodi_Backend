package G9.Foodi.controller;

import G9.Foodi.model.Order;
import G9.Foodi.service.OrderService;
import G9.Foodi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin(origins = "http://35.224.60.159:80") // Cho phép frontend (port 5173) gọi API từ domain khác
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService; // Dịch vụ xử lý nghiệp vụ liên quan đến đơn hàng
    
    @Autowired
    private UserService userService; // Dịch vụ kiểm tra quyền của người dùng

    // API lấy tất cả đơn hàng của người dùng (admin lấy được tất cả, user chỉ lấy của mình)
    @GetMapping
    public ResponseEntity<?> getOrders(Authentication authentication) {
        try {
            logger.info("Getting orders for user: {}", authentication.getName());
            String userEmail = authentication.getName();
            List<Order> orders;

            // Nếu là admin thì lấy tất cả đơn hàng
            if (userService.isAdmin(userEmail)) {
                logger.info("Admin user detected, fetching all orders");
                orders = orderService.getAllOrders();
            } else {
                // Nếu là người dùng thường thì chỉ lấy đơn hàng của chính họ
                logger.info("Regular user detected, fetching user's orders");
                orders = orderService.getOrdersByUserId(userEmail);
            }

            logger.info("Successfully fetched {} orders", orders.size());
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            logger.error("Error fetching orders: ", e);
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error fetching orders: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // API tạo đơn hàng mới
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Order order, Authentication authentication) {
        try {
            String userId = authentication.getName(); // Lấy email người dùng từ token
            order.setUserId(userId); // Gán userId cho đơn hàng
            order.setStatus("pending"); // Trạng thái mặc định khi tạo là "pending"
            
            Order createdOrder = orderService.createOrder(order);
            logger.info("Created order with ID: {} for user: {}", createdOrder.getId(), userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
        } catch (Exception e) {
            logger.error("Error creating order: ", e);
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error creating order: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // API lấy chi tiết đơn hàng theo ID (admin và chủ đơn hàng mới được xem)
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable String id, Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            Order order = orderService.getOrderById(id);

            // Chỉ admin hoặc chủ sở hữu đơn hàng mới được truy cập
            if (!userService.isAdmin(userEmail) && !order.getUserId().equals(userEmail)) {
                logger.warn("User {} attempted to access order {} without permission", userEmail, id);
                Map<String, String> error = new HashMap<>();
                error.put("message", "Access denied");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }

            return ResponseEntity.ok(order);
        } catch (IllegalArgumentException e) {
            logger.warn("Order not found: {}", id);
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            logger.error("Error fetching order: ", e);
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error fetching order: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // API cập nhật trạng thái đơn hàng (chỉ admin được phép)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable String id,
            @RequestBody Map<String, String> statusUpdate,
            Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            // Kiểm tra quyền admin
            if (!userService.isAdmin(userEmail)) {
                logger.warn("Non-admin user {} attempted to update order status", userEmail);
                Map<String, String> error = new HashMap<>();
                error.put("message", "Admin privileges required");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }

            // Lấy giá trị status từ request
            String status = statusUpdate.get("status");
            if (status == null || status.trim().isEmpty()) {
                logger.warn("Invalid status update attempt for order {}", id);
                Map<String, String> error = new HashMap<>();
                error.put("message", "Status is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            // Gọi service để cập nhật
            Order updatedOrder = orderService.updateOrderStatus(id, status);
            logger.info("Updated order {} status to {}", id, status);
            return ResponseEntity.ok(updatedOrder);
        } catch (IllegalArgumentException e) {
            logger.warn("Order not found: {}", id);
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            logger.error("Error updating order status: ", e);
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error updating order status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // API xoá đơn hàng (chỉ admin được phép)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable String id, Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            // Chỉ admin mới được xoá đơn hàng
            if (!userService.isAdmin(userEmail)) {
                logger.warn("Non-admin user {} attempted to delete order {}", userEmail, id);
                Map<String, String> error = new HashMap<>();
                error.put("message", "Admin privileges required");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }

            orderService.deleteOrder(id);
            logger.info("Deleted order: {}", id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            logger.warn("Order not found: {}", id);
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            logger.error("Error deleting order: ", e);
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error deleting order: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
