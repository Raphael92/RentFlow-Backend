package kz.brdevelopment.test.controller;

import kz.brdevelopment.test.model.RentRequest;
import kz.brdevelopment.test.model.RentRequestHistory;
import kz.brdevelopment.test.service.RentRequestHistoryService;
import kz.brdevelopment.test.service.RentRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/request_history")
@RequiredArgsConstructor
public class RentRequestHistoryController {
    private final RentRequestHistoryService service;

    @PostMapping
    public ResponseEntity<RentRequestHistory> addAction(@RequestBody RentRequestHistory request) {
        return ResponseEntity.ok(service.addAction(request));
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<List<RentRequestHistory>> getHistoryByRequestId(@PathVariable Long requestId) {
        return ResponseEntity.ok(service.getHistoryByRequestId(requestId));
    }
}
