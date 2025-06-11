package kz.brdevelopment.test.service;

import kz.brdevelopment.test.dto.AuthRequest;
import kz.brdevelopment.test.dto.AuthResponse;
import kz.brdevelopment.test.model.Landlord;
import kz.brdevelopment.test.repository.LandlordRepository;
import kz.brdevelopment.test.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LandlordService {
    private final LandlordRepository landlordRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /*public Landlord createLandlord(Landlord landlord) {
        return landlordRepository.save(landlord);
    }*/

    public List<Landlord> getAll() {
        return landlordRepository.findAll();
    }

    public Landlord create(Landlord item) {
        return landlordRepository.save(item);
    }

    public void register(AuthRequest request) {
        Landlord user = new Landlord();
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        landlordRepository.save(user);
    }

    public AuthResponse login(AuthRequest request) {
        Landlord user = landlordRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Неверный пароль");
        }
        String token = jwtUtil.generateToken(user.getId(), user.getTokenVersion());
        return new AuthResponse(token, user.getId());
        //return jwtUtil.generateToken(user.getId(), user.getTokenVersion());
    }

    public Landlord findById(Long id) {
        return landlordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }

    public void deleteById(Long id) {
        landlordRepository.deleteById(id);
    }

    public void updateTokenVersion(Long id) {
        Landlord user = landlordRepository.findById(id).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        user.setTokenVersion(user.getTokenVersion() + 1);
        landlordRepository.save(user);
    }
}
