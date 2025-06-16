package kz.brdevelopment.test.repository;

import kz.brdevelopment.test.model.Landlord;
import kz.brdevelopment.test.model.RentRequestHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RentRequestHistoryRepository extends JpaRepository<RentRequestHistory, Long> {
    List<RentRequestHistory> findByRentRequestId(Long requestId);
}
