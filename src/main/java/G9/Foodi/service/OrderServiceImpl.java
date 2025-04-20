package G9.Foodi.service;

import java.util.List;
import java.util.Date;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import G9.Foodi.model.Order;
import G9.Foodi.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @Override
    public Order createOrder(Order order) {
        order.setCreatedAt(new Date());
        order.setStatus("pending");
        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + id));
    }

    @Override
    public void deleteOrder(String id) {
        if (!orderRepository.existsById(id)) {
            throw new IllegalArgumentException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    @Override
    public Order updateOrder(String id, Order orderDetails) {
        Order order = getOrderById(id);
        
        order.setStatus(orderDetails.getStatus());
        order.setAddress(orderDetails.getAddress());
        order.setPhone(orderDetails.getPhone());
        
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrderStatus(String id, String status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @Override
    public double calculateTotalRevenue() {
        List<Order> completedOrders = orderRepository.findByStatus("completed");
        return completedOrders.stream()
                .mapToDouble(Order::getTotal)
                .sum();
    }

    @Override
    public long countTotalOrders() {
        return orderRepository.count();
    }

    @Override
    public List<Order> getRecentOrders() {
        return orderRepository.findAll(
            PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"))
        ).getContent();
    }
} 