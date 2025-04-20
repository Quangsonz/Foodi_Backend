package G9.Foodi.service;

import java.util.List;
import G9.Foodi.model.Order;

public interface OrderService {
    List<Order> getAllOrders();
    Order createOrder(Order order);
    Order getOrderById(String id);
    void deleteOrder(String id);
    Order updateOrder(String id, Order orderDetails);
    Order updateOrderStatus(String id, String status);
    List<Order> getOrdersByUserId(String userId);
    double calculateTotalRevenue();
    long countTotalOrders();
    List<Order> getRecentOrders();
} 