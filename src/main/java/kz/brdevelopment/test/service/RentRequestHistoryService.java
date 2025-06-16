package kz.brdevelopment.test.service;

import kz.brdevelopment.test.model.RentRequestHistory;
import kz.brdevelopment.test.repository.RentRequestHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RentRequestHistoryService {
    private final RentRequestHistoryRepository requestHistoryRepository;

    public RentRequestHistory addAction(RentRequestHistory rentRequestHistory) {
        return requestHistoryRepository.save(rentRequestHistory);
    }

    public List<RentRequestHistory> getHistoryByRequestId(Long requestId) {
        return requestHistoryRepository.findByRentRequestId(requestId);
    }
}
