package kz.brdevelopment.test.service;

import kz.brdevelopment.test.dto.RentRequestDto;
import kz.brdevelopment.test.mapper.RentRequestMapper;
import kz.brdevelopment.test.model.*;
import kz.brdevelopment.test.repository.LandlordRepository;
import kz.brdevelopment.test.repository.RentRequestHistoryRepository;
import kz.brdevelopment.test.repository.RentRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentRequestService {
    private final RentRequestRepository requestRepository;
    private final RentRequestHistoryRepository requestHistoryRepository;
    private final LandlordRepository landlordRepository;

    private final RentRequestMapper rentRequestMapper;

    public RentRequest createRequest(RentRequest request) {
        RentRequest saved = requestRepository.save(request);
        requestHistoryRepository.save(
                RentRequestHistory.builder()
                        .rentRequest(saved)
                        .createdAt(LocalDateTime.now())
                        .historyAction(RequestHistoryAction.REQUEST_CREATED)
                        .action("Создана заявка №" + saved.getId() + ": \"" + saved.getClientInfo() + "\"")
                        .build()
        );
        return saved;
    }

    public List<RentRequest> getAll() {
        return requestRepository.findAll();
    }

    public List<RentRequestDto> getAllSorted(String sortBy, String order) {
        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        //return requestRepository.findAll(Sort.by(direction, sortBy));
        return requestRepository.findAll(Sort.by(direction, sortBy)).stream()
                .map(rentRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<RentRequestDto> getAllSortedActive(String sortBy, String order) {
        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        //return requestRepository.findAll(Sort.by(direction, sortBy));
        return requestRepository.findByStatusNot(RequestStatus.CANCELED, Sort.by(direction, sortBy)).stream()
                .map(rentRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateStatusById(Long requestId, RequestStatus status, boolean isByClient) {
        requestRepository.updateStatusById(status, requestId);

       /*RentRequestHistory requestHistory = new RentRequestHistory();
        requestHistory.setRentRequest(requestRepository.getReferenceById(requestId));
        requestHistory.setAction("Клиент обновил статус");
        requestHistoryRepository.save(requestHistory);*/

        String actor;

        if (isByClient) {
            actor = "Клиент";
        }
        else {
            actor = "Автор";
        }

        String action = " обновил статус отклика заявки на " + status;
        if (status == RequestStatus.CANCELED) {
            action = " удалил свою заявку";
        }

        RequestHistoryAction historyAction = null;
        switch(status) {
            case COMPLETED -> historyAction = RequestHistoryAction.REQUEST_COMPLETED;
            case CANCELED ->  historyAction = RequestHistoryAction.REQUEST_CANCELED;
        }
        requestHistoryRepository.save(
                RentRequestHistory.builder()
                        .rentRequest(requestRepository.getReferenceById(requestId))
                        .createdAt(LocalDateTime.now())
                        .historyAction(historyAction)
                        .action(actor + action)
                        .build()
        );
    }

}
