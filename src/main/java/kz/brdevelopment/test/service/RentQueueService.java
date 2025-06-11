package kz.brdevelopment.test.service;

import kz.brdevelopment.test.model.Landlord;
import kz.brdevelopment.test.model.QueueStatus;
import kz.brdevelopment.test.model.RentQueue;
import kz.brdevelopment.test.model.RentRequest;
import kz.brdevelopment.test.repository.RentQueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Queue;

@Service
@RequiredArgsConstructor
public class RentQueueService {
    private final RentQueueRepository queueRepository;

    public RentQueue addQueue(RentQueue queue) {
        return queueRepository.save(queue);
    }

    public List<RentQueue> getAll() {
        return queueRepository.findAll();
    }

    public List<RentQueue> getAllActive() {
        return queueRepository.findByStatusNot(QueueStatus.CANCELED);
    }

    public List<RentQueue> getByRequestId(Long requestId) {
        return queueRepository.findByRequestId(requestId);
    }

    public List<RentQueue> getByRequestPublicToken(String token) {
        return queueRepository.findByRequestPublicTokenAndStatusNot(token, QueueStatus.CANCELED);
    }

    public void deleteById(Long id) {
        if (!queueRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Отклик не найден");
        }
        queueRepository.deleteById(id);
    }

    public void deleteByRequestIdAndUserId(Long requestId, Long userId) {
        queueRepository.deleteByRequestIdAndLandlordId(requestId, userId);
    }

    @Transactional
    public void updateStatusByRequestIdAndLandlordId(Long requestId, Long userId, QueueStatus status) {
        queueRepository.updateStatusByRequestIdAndLandlordId(status, requestId, userId);
    }

    @Transactional
    public void updateStatusById(Long id, QueueStatus status) {
        queueRepository.updateStatusById(status, id);
    }
}
