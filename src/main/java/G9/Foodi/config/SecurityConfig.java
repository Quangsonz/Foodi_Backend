package G9.Foodi.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import G9.Foodi.security.JwtAuthFilter;

// Đánh dấu đây là class cấu hình (Configuration) cho Spring
@Configuration
// Kích hoạt Spring Security cho ứng dụng
@EnableWebSecurity
public class SecurityConfig {

    // Inject (tiêm) filter xác thực JWT vào cấu hình bảo mật
    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    // Định nghĩa bean SecurityFilterChain để cấu hình các rule bảo mật
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Tắt CSRF vì API sử dụng JWT (stateless)
                .csrf(csrf -> csrf.disable())
                // Bật và cấu hình CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Cấu hình quyền truy cập cho từng endpoint
                .authorizeHttpRequests(authorize -> authorize
                        // Cho phép tất cả truy cập các endpoint xác thực JWT
                        .requestMatchers("/api/v1/jwt/**").permitAll()
                        // Cho phép GET menu cho tất cả
                        .requestMatchers(HttpMethod.GET, "/api/v1/menu/**").permitAll()
                        // Chỉ ADMIN mới được POST, PUT, DELETE menu
                        .requestMatchers(HttpMethod.POST, "/api/v1/menu/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/menu/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/menu/**").hasRole("ADMIN")
                        // Chỉ ADMIN mới được truy cập endpoint quản lý user
                        .requestMatchers("/api/v1/users/admin/**").hasRole("ADMIN")
                        // Các endpoint order, cart yêu cầu đăng nhập
                        .requestMatchers("/api/v1/orders/**").authenticated()
                        .requestMatchers("/api/v1/carts/**").authenticated()
                        // Các request còn lại cho phép truy cập tự do
                        .anyRequest().permitAll()
                )
                // Cấu hình session là stateless (không lưu session trên server)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Thêm filter xác thực JWT vào trước filter xác thực username/password mặc định của Spring
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // Định nghĩa bean PasswordEncoder sử dụng thuật toán BCrypt để mã hóa mật khẩu
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Định nghĩa bean cấu hình CORS cho phép frontend truy cập API
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Thêm các origin thực tế của frontend (cả http và https, cả port nếu có)
        configuration.setAllowedOrigins(Arrays.asList(
            "http://35.224.60.159", 
            "http://35.224.60.159:80",
            "http://35.224.60.159:5173", 
            "http://localhost:5173", 
            "http://localhost:3000" 
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}