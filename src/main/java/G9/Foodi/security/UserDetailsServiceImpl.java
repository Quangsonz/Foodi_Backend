package G9.Foodi.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import G9.Foodi.model.User;
import G9.Foodi.repository.UserRepository;

// Đánh dấu class này là một Spring Service, được quản lý bởi Spring Container
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // Tự động inject UserRepository để truy vấn thông tin người dùng từ database
    @Autowired
    private UserRepository userRepository;

    // Triển khai phương thức loadUserByUsername từ UserDetailsService
    // Tải thông tin người dùng dựa trên email để sử dụng trong Spring Security
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Tìm user trong database theo email, trả về Optional<User>
        // Nếu không tìm thấy, ném ngoại lệ UsernameNotFoundException
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Tạo đối tượng UserDetails (User của Spring Security) với email, mật khẩu, và quyền
        // Mật khẩu để trống vì ứng dụng sử dụng JWT để xác thực
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), // Email làm username trong Spring Security
                "", // Không sử dụng mật khẩu (JWT-based authentication)
                getAuthorities(user)); // Lấy danh sách quyền của người dùng
    }

    // Phương thức lấy danh sách quyền (authorities) của người dùng
    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        // Tạo danh sách để chứa các quyền
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        
        // Thêm quyền dựa trên vai trò (role) của người dùng, định dạng "ROLE_<role_name>"
        // Ví dụ: ROLE_ADMIN, ROLE_USER
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        
        // Trả về danh sách quyền
        return authorities;
    }
}