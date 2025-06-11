package kz.brdevelopment.test.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.brdevelopment.test.model.Landlord;
import kz.brdevelopment.test.repository.LandlordRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final LandlordRepository userRepository;

    private static final List<String> EXCLUDED_PATHS = List.of(
            "/api/landlords/login",
            "/api/landlords/register"
    );

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public JwtAuthenticationFilter(JwtUtil jwtUtil, LandlordRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return EXCLUDED_PATHS.stream().anyMatch(p -> pathMatcher.match(p, path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("Фильтр вызван для метода: " + request.getMethod() + ", путь: " + request.getRequestURI());
        String jwt = null;

        // 1. Сначала ищем JWT в Authorization заголовке
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
        }

        // 2. Если в заголовке нет — ищем в куках
        if (jwt == null && request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if (cookie.getName().equals("token")) {
                    jwt = cookie.getValue();
                    break;
                }
            }
        }

        // 3. Если нашли JWT — проверяем и устанавливаем пользователя
        try {
            if (jwt != null) {
                if (!jwtUtil.validateToken(jwt)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
                try {
                    Long userId = jwtUtil.extractUserId(jwt);
                    System.out.println("userId = " + userId);
                    Optional<Landlord> optionalLandlord = userRepository.findById(userId);

                    if (optionalLandlord.isPresent()) {
                        Landlord landlord = optionalLandlord.get();

                        // Проверяем tokenVersion
                        Integer tokenVersionFromToken = jwtUtil.extractTokenVersion(jwt);

                        if (!tokenVersionFromToken.equals(landlord.getTokenVersion())) {
                            System.out.println("Token version mismatch");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            return; // не продолжаем, если токен устарел
                        }

                        String username = jwtUtil.extractUsername(jwt);
                        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                                username, "", List.of()
                        );

                        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );
                        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(token);
                    }

                } catch (Exception e) {
                    System.out.println("Ошибка при проверке токена: " + e.getMessage());
                    // (опционально) можешь очистить токен из куки, если он невалиден
                }
        }
        }
        catch (ExpiredJwtException e) {
            System.out.println("Token expired");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }
}