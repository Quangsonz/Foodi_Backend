package G9.Foodi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import G9.Foodi.model.Menu;

@Repository
public interface MenuRepository extends MongoRepository<Menu, String> {
    // Các phương thức truy vấn tùy chỉnh (nếu cần)
}