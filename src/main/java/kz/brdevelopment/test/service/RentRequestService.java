package kz.brdevelopment.test.service;

import kz.brdevelopment.test.dto.RentRequestDto;
import kz.brdevelopment.test.mapper.RentRequestMapper;
import kz.brdevelopment.test.model.Landlord;
import kz.brdevelopment.test.model.QueueStatus;
import kz.brdevelopment.test.model.RentRequest;
import kz.brdevelopment.test.model.RequestStatus;
import kz.brdevelopment.test.repository.RentRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentRequestService {
    private final RentRequestRepository requestRepository;

    private final RentRequestMapper rentRequestMapper;

    public RentRequest createRequest(RentRequest request) {
        return requestRepository.save(request);
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
    public void updateStatusById(Long requestId, RequestStatus status) {
        requestRepository.updateStatusById(status, requestId);
    }

}
