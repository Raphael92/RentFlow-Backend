package kz.brdevelopment.test.dto;

import kz.brdevelopment.test.model.Landlord;
import kz.brdevelopment.test.model.RentQueue;
import kz.brdevelopment.test.model.RequestStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RentRequestDto {
    private Long id;
    private String clientInfo;
    private Long senderId;
    private List<Landlord> landlordResponses;
    private List<RentQueue> queueResponses;
    private LocalDateTime createdAt;
    private RequestStatus status;
    private String publicToken;
}
