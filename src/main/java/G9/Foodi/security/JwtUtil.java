package G9.Foodi.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import G9.Foodi.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final JwtConfig jwtConfig;

    // Constructor Injection (không cần @Autowired nếu chỉ có một constructor)
    public JwtUtil(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    // Lấy username (subject) từ token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Lấy ngày hết hạn từ token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Hàm dùng chung để lấy thông tin bất kỳ từ Claims
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token); // Parse token ra Claims
        return claimsResolver.apply(claims); // Trích xuất thông tin từ Claims
    }

    // Parse token và lấy toàn bộ Claims
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey()) // Thiết lập khóa bí mật
                .build()
                .parseClaimsJws(token) // Parse JWT
                .getBody();
    }

    // Kiểm tra token đã hết hạn chưa
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Kiểm tra token hợp lệ: đúng username và chưa hết hạn
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Tạo JWT mới từ username
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>(); // Có thể thêm role, quyền,...
        return createToken(claims, username);
    }

    // Tạo token thực sự từ claims và subject
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims) // Thêm thông tin tùy chỉnh
                .setSubject(subject) // username
                .setIssuedAt(new Date(System.currentTimeMillis())) // thời gian phát hành
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration())) // hết hạn
                .signWith(getSignKey(), SignatureAlgorithm.HS256) // ký với thuật toán và key
                .compact(); // tạo chuỗi token
    }

    // Lấy secret key từ file cấu hình và chuyển thành Key
    private Key getSignKey() {
        return Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
    }
}
