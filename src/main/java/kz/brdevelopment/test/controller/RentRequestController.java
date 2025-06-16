package kz.brdevelopment.test.controller;

import kz.brdevelopment.test.dto.RentRequestDto;
import kz.brdevelopment.test.model.QueueStatus;
import kz.brdevelopment.test.model.RentQueue;
import kz.brdevelopment.test.model.RentRequest;
import kz.brdevelopment.test.model.RequestStatus;
import kz.brdevelopment.test.service.RentRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class RentRequestController {
    private final RentRequestService service;

    @PostMapping
    public ResponseEntity<RentRequest> create(@RequestBody RentRequest request) {
        return ResponseEntity.ok(service.createRequest(request));
    }

  /*  @GetMapping
    public List<RentRequest> getAllRequests() {
        return service.getAll();
    }*/

    @GetMapping
    public List<RentRequestDto> getAllSorted(
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String order
    ) {
        //return service.getAllSorted(sortBy, order);
        return service.getAllSortedActive(sortBy, order);
    }


    @PutMapping("/updateStatus/{requestId}")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long requestId,
            @RequestParam RequestStatus status,
            @RequestParam(name = "byClient", required = false, defaultValue = "false") boolean byClient) {
        service.updateStatusById(requestId, status, byClient);
        return ResponseEntity.ok().build();
    }
}
