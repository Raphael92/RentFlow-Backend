package kz.brdevelopment.test.repository;

import kz.brdevelopment.test.model.QueueStatus;
import kz.brdevelopment.test.model.RentQueue;
import kz.brdevelopment.test.model.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RentQueueRepository extends JpaRepository<RentQueue, Long> {
    List<RentQueue> findByRequestId(Long requestId);
    List<RentQueue> findByRequestIdAndStatusNot(Long requestId, QueueStatus status);
    List<RentQueue> findByRequestPublicTokenAndStatusNot(String token, QueueStatus status);
    //void updateStatusById(Long id, QueueStatus status);
    List<RentQueue> findByStatusNot(QueueStatus status);
    void deleteByRequestIdAndLandlordId(Long requestId, Long userId);

    @Modifying
    @Query("UPDATE RentQueue r SET r.status = :status WHERE r.request.id = :requestId AND r.landlord.id = :userId")
    void updateStatusByRequestIdAndLandlordId(@Param("status") QueueStatus status,
                                           @Param("requestId") Long requestId,
                                           @Param("userId") Long userId);
    @Modifying
    @Query("UPDATE RentQueue r SET r.status = :status WHERE r.id = :queueId")
    void updateStatusById(@Param("status") QueueStatus status,
                          @Param("queueId") Long requestId);
}
