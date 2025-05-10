package G9.Foodi.security;

import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Bộ lọc xác thực JWT được chạy một lần duy nhất cho mỗi yêu cầu HTTP.
 * Nhiệm vụ: kiểm tra token từ header Authorization, xác thực người dùng,
 * và thiết lập thông tin xác thực trong SecurityContext.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = Logger.getLogger(JwtAuthFilter.class.getName());

    @Autowired
    private JwtUtil jwtUtil; // Tiện ích để xử lý JWT (giải mã, xác thực, lấy username)

    @Autowired
    private UserDetailsServiceImpl userDetailsService; // Custom service để load thông tin người dùng từ DB

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // In ra URI của request để debug
        String requestURI = request.getRequestURI();
        logger.info("Processing request: " + requestURI);

        // Lấy Authorization header từ request
        String authHeader = request.getHeader("Authorization");
        logger.info("Auth header: " + (authHeader != null ? authHeader : "Missing"));

        String token = null;
        String username = null;

        // Kiểm tra header có bắt đầu bằng "Bearer " không (chuẩn định dạng JWT)
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Bỏ "Bearer " để lấy token thật sự
            logger.info("JWT token extracted: " + token);
            try {
                // Trích xuất username từ token
                username = jwtUtil.extractUsername(token);
                logger.info("Username extracted from token: " + username);
            } catch (Exception e) {
                logger.severe("Error extracting username from token: " + e.getMessage());
            }
        } else {
            logger.warning("No valid Bearer token found in Authorization header");
        }

        // Nếu đã lấy được username và người dùng chưa được xác thực trước đó
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                // Tải thông tin người dùng từ database (hoặc cache)
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                logger.info("User details loaded for: " + username + ", Username from UserDetails: " + userDetails.getUsername());

                // Xác thực token (kiểm tra chữ ký, hạn sử dụng, v.v.)
                if (jwtUtil.validateToken(token, userDetails)) {
                    logger.info("Token validation successful for user: " + username);

                    // Tạo đối tượng xác thực
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    // Thiết lập thông tin chi tiết của request (IP, user agent,...)
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Đưa thông tin xác thực vào SecurityContext để Spring Security nhận diện người dùng
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    logger.info("Authentication set in SecurityContext for: " + username);
                } else {
                    logger.warning("Token validation failed for user: " + username);
                }
            } catch (Exception e) {
                logger.severe("Error during authentication: " + e.getMessage());
            }
        }

        // Tiếp tục luồng filter
        filterChain.doFilter(request, response);
    }
}
