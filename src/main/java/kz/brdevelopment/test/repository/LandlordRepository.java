package kz.brdevelopment.test.repository;

import kz.brdevelopment.test.model.Landlord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LandlordRepository extends JpaRepository<Landlord, Long> {
    Optional<Landlord> findByUsername(String username);
}
