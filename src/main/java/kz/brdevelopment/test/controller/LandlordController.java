package kz.brdevelopment.test.controller;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import kz.brdevelopment.test.dto.AuthRequest;
import kz.brdevelopment.test.dto.AuthResponse;
import kz.brdevelopment.test.model.Landlord;
import kz.brdevelopment.test.model.RentRequest;
import kz.brdevelopment.test.security.JwtUtil;
import kz.brdevelopment.test.service.LandlordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/landlords")
@RequiredArgsConstructor
public class LandlordController {
    private final LandlordService service;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<Landlord> create(@RequestBody Landlord landlord) {
        return ResponseEntity.ok(service.create(landlord));
    }

    @GetMapping
    public List<Landlord> getAll() {
        return service.getAll();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        System.out.println("test: ");
        service.register(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request, HttpServletResponse response) {

        //String token = service.login(request);
        AuthResponse authResponse = service.login(request);

        Cookie cookie = new Cookie("token", authResponse.getToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // true, если используешь HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 дней

        response.addCookie(cookie);

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/csrf")
    public ResponseEntity<Void> csrf() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}")
    public void updateTokenVersion(@PathVariable Long id) {
        service.updateTokenVersion(id);
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkToken(@CookieValue(value = "token", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        try {
            Claims claims = jwtUtil.verifyAccessToken(token);
            Long userId = Long.valueOf(claims.getSubject());
            // можно дополнительно проверить существование пользователя
            //return ResponseEntity.ok().build();
            return ResponseEntity.ok(Map.of("userId", userId));
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(401).body("Access token expired");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token");
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }
}
