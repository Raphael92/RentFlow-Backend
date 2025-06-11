package kz.brdevelopment.test.repository;

import kz.brdevelopment.test.model.RentRequest;
import kz.brdevelopment.test.model.RequestStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RentRequestRepository extends JpaRepository<RentRequest, Long> {
    List<RentRequest> findByStatusNot(RequestStatus status, Sort sort);
    Optional<RentRequest> findByPublicToken(String publicToken);
    @Modifying
    @Query("UPDATE RentRequest r SET r.status = :status WHERE r.id = :requestId")
    void updateStatusById(@Param("status") RequestStatus status,
                                              @Param("requestId") Long requestId);
}