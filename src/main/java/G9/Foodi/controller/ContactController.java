package G9.Foodi.controller;

// Import các lớp cần thiết
import G9.Foodi.dto.ContactDto;        
import G9.Foodi.model.Contact;         
import G9.Foodi.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Đánh dấu đây là một RestController — controller trả về dữ liệu (JSON) thay vì view
@RestController
@RequestMapping("/api/v1/contact")      // Định nghĩa URL gốc cho controller này
@CrossOrigin("http://35.224.60.159:80")   // Cho phép truy cập API từ frontend chạy ở port 5173 (ví dụ: Vite React)
public class ContactController {

    // Tự động inject ContactService (Dependency Injection)
    @Autowired
    private ContactService contactService;

    // API tạo một liên hệ mới (POST /api/v1/contact)
    @PostMapping
    public ResponseEntity<Contact> createContact(@RequestBody ContactDto contactDto) {
        // Gọi service để tạo liên hệ từ DTO
        Contact savedContact = contactService.createContact(contactDto);
        // Trả về kết quả (dạng JSON) với HTTP status 200 OK
        return ResponseEntity.ok(savedContact);
    }

    // API lấy danh sách tất cả liên hệ (GET /api/v1/contact)
    @GetMapping
    public ResponseEntity<List<Contact>> getAllContacts() {
        // Gọi service để lấy danh sách liên hệ
        List<Contact> contacts = contactService.getAllContacts();
        // Trả về danh sách các liên hệ cùng HTTP status 200 OK
        return ResponseEntity.ok(contacts);
    }

    // API xóa một liên hệ theo ID (DELETE /api/v1/contact/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable String id) {
        // Gọi service để xóa liên hệ theo ID
        contactService.deleteContact(id);
        // Trả về phản hồi rỗng với HTTP status 200 OK
        return ResponseEntity.ok().build();
    }
}
