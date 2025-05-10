package G9.Foodi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import G9.Foodi.dto.UserDto;
import G9.Foodi.model.User;
import G9.Foodi.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    // API lấy danh sách tất cả người dùng
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // API tạo người dùng mới từ dữ liệu nhận vào dạng UserDto
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {
        try {
            return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Nếu tài khoản đã tồn tại hoặc không hợp lệ, trả về mã lỗi 409
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    // API xóa người dùng theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            // Trả về lỗi 404 nếu không tìm thấy người dùng cần xóa
            return ResponseEntity.notFound().build();
        }
    }

    // API kiểm tra người dùng có quyền admin hay không (xác thực phải trùng email)
    @GetMapping("/admin/{email}")
    public ResponseEntity<Boolean> getAdmin(@PathVariable String email, Authentication authentication) {
        // Nếu người dùng được yêu cầu không trùng với người dùng đã xác thực, trả về lỗi 403
        if (!email.equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(false);
        }

        // Gọi service để kiểm tra vai trò admin
        boolean isAdmin = userService.isAdmin(email);
        return ResponseEntity.ok(isAdmin);
    }

    // API gán quyền admin cho người dùng theo ID
    @PutMapping("/admin/{id}")
    public ResponseEntity<User> makeAdmin(@PathVariable String id) {
        try {
            return ResponseEntity.ok(userService.makeAdmin(id));
        } catch (IllegalArgumentException e) {
            // Nếu không tìm thấy người dùng, trả về lỗi 404
            return ResponseEntity.notFound().build();
        }
    }

    // API cập nhật thông tin người dùng từ UserDto
    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody UserDto userDto) {
        try {
            return ResponseEntity.ok(userService.updateUser(userDto));
        } catch (IllegalArgumentException e) {
            // Nếu không tìm thấy người dùng cần cập nhật, trả về lỗi 404
            return ResponseEntity.notFound().build();
        }
    }
}
