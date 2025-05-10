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
@RequestMapping("/api/v1/menu") // Tất cả API của menu bắt đầu với /api/v1/menu
@CrossOrigin(origins = "http://35.224.60.159:80") // Cho phép frontend truy cập API (tránh lỗi CORS)
public class MenuController {

    @Autowired
    private MenuService menuService; // Inject MenuService để xử lý logic nghiệp vụ
    
    @Autowired
    private MenuRepository menuRepository; // Dùng để truy vấn dữ liệu trực tiếp (JPA)
    
    @Autowired
    private UserService userService; // Dùng để kiểm tra quyền admin khi xóa món ăn

    // Lấy toàn bộ danh sách menu (GET /api/v1/menu)
    @GetMapping
    public ResponseEntity<List<Menu>> getAllMenuItems() {
        List<Menu> menuItems = menuRepository.findAll(); // Lấy toàn bộ từ DB
        return ResponseEntity.ok(menuItems); // Trả về danh sách món ăn
    }

    // Tạo một món ăn mới (POST /api/v1/menu)
    @PostMapping
    public ResponseEntity<Menu> createMenuItem(@RequestBody Menu menu) {
        return new ResponseEntity<>(menuService.createMenuItem(menu), HttpStatus.CREATED); // Trả về 201 CREATED
    }

    // Xóa món ăn (chỉ admin được quyền xóa) (DELETE /api/v1/menu/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMenuItem(@PathVariable String id, Authentication authentication) {
        try {
            // In log để debug
            System.out.println("Attempting to delete menu item with ID: " + id);
            System.out.println("User email: " + authentication.getName());

            // Kiểm tra người dùng có phải admin không
            String userEmail = authentication.getName();
            if (!userService.isAdmin(userEmail)) {
                System.out.println("User is not admin");
                Map<String, String> error = new HashMap<>();
                error.put("message", "Admin privileges required");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error); // Trả về 403 nếu không phải admin
            }

            // Nếu là admin, thực hiện xóa
            System.out.println("User is admin, proceeding with deletion");
            menuService.deleteMenuItem(id);
            System.out.println("Menu item deleted successfully");
            return ResponseEntity.ok().build(); // Trả về 200 OK nếu xóa thành công

        } catch (IllegalArgumentException e) {
            // Nếu ID không hợp lệ
            System.out.println("Error deleting menu item: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error); // Trả về 404 nếu không tìm thấy
        } catch (Exception e) {
            // Lỗi không xác định
            System.out.println("Unexpected error: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error deleting menu item: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error); // Trả về 500 nếu có lỗi hệ thống
        }
    }

    // Lấy một món ăn theo ID (GET /api/v1/menu/{id})
    @GetMapping("/{id}")
    public ResponseEntity<Menu> getMenuItemById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(menuService.getMenuItemById(id)); // Trả về món ăn nếu tồn tại
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build(); // Trả về 404 nếu không tìm thấy
        }
    }

    // Cập nhật thông tin món ăn (PUT /api/v1/menu/{id})
    @PutMapping("/{id}")
    public ResponseEntity<Menu> updateMenuItem(@PathVariable String id, @RequestBody Menu menuDetails) {
        try {
            return ResponseEntity.ok(menuService.updateMenuItem(id, menuDetails)); // Trả về món ăn đã cập nhật
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build(); // Trả về 404 nếu không tìm thấy ID
        }
    }

    // Tìm kiếm món ăn theo tên (GET /api/v1/menu/search?query=...)
    @GetMapping("/search")
    public ResponseEntity<List<Menu>> searchMenuItems(@RequestParam String query) {
        List<Menu> menuItems = menuRepository.findByNameContainingIgnoreCase(query); // Tìm tên có chứa từ khóa (không phân biệt hoa thường)
        return ResponseEntity.ok(menuItems); // Trả về danh sách kết quả
    }
}
