package G9.Foodi.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import G9.Foodi.model.Order;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByUserId(String userId);
    List<Order> findByStatus(String status);
} 