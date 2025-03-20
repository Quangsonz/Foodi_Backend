package G9.Foodi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import G9.Foodi.model.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    // Các phương thức truy vấn tùy chỉnh (nếu cần)
} 