package G9.Foodi.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// Đánh dấu class này là một Spring Bean để Spring quản lý
@Component
// Ánh xạ các thuộc tính có prefix là "jwt" từ file cấu hình vào các trường của class này
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    // Biến lưu trữ secret key dùng để ký và xác thực JWT
    private String secret;
    // Biến lưu trữ thời gian hết hạn của JWT (tính bằng milliseconds)
    private long expiration;


    public String getSecret() { return secret; }
    // Setter cho secret (Spring sẽ tự động gọi khi ánh xạ từ file cấu hình)
    public void setSecret(String secret) { this.secret = secret; }

    public long getExpiration() { return expiration; }
    // Setter cho expiration (Spring sẽ tự động gọi khi ánh xạ từ file cấu hình)
    public void setExpiration(long expiration) { this.expiration = expiration; }
}
