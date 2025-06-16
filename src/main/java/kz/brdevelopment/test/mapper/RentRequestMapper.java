package kz.brdevelopment.test.mapper;

import kz.brdevelopment.test.dto.RentRequestDto;
import kz.brdevelopment.test.model.Landlord;
import kz.brdevelopment.test.model.QueueStatus;
import kz.brdevelopment.test.model.RentQueue;
import kz.brdevelopment.test.model.RentRequest;
import kz.brdevelopment.test.repository.RentQueueRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RentRequestMapper {

    private final RentQueueRepository rentQueueRepository;

    public RentRequestMapper(RentQueueRepository rentQueueRepository) {
        this.rentQueueRepository = rentQueueRepository;
    }

    public RentRequestDto toDto(RentRequest rentRequest) {
        RentRequestDto dto = new RentRequestDto();
        dto.setId(rentRequest.getId());
        dto.setClientInfo(rentRequest.getClientInfo());
        dto.setSenderId(rentRequest.getSender().getId());
        dto.setCreatedAt(rentRequest.getCreatedAt());
        dto.setStatus(rentRequest.getStatus());
        dto.setPublicToken(rentRequest.getPublicToken());

        List<RentQueue> responses = rentQueueRepository.findByRequestIdAndStatusNot(rentRequest.getId(), QueueStatus.CANCELED);
        List<Landlord> responseUsers = responses.stream()
                .map(RentQueue::getLandlord)
                .collect(Collectors.toList());
        //dto.setResponses(responses);

        dto.setLandlordResponses(responseUsers);
        dto.setQueueResponses(responses);
        return dto;
    }
}
