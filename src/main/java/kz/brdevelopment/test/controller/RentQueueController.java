package kz.brdevelopment.test.controller;

import kz.brdevelopment.test.model.Landlord;
import kz.brdevelopment.test.model.QueueStatus;
import kz.brdevelopment.test.model.RentQueue;
import kz.brdevelopment.test.model.RentRequest;
import kz.brdevelopment.test.service.RentQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/queue")
@RequiredArgsConstructor
public class RentQueueController {
    private final RentQueueService service;

    @PostMapping
    public ResponseEntity<RentQueue> create(@RequestBody RentQueue queue) {
        return ResponseEntity.ok(service.addQueue(queue));
    }

    @GetMapping
    public ResponseEntity<List<RentQueue>> getAllActive() {
        //return service.getAllActive();
        return ResponseEntity.ok(service.getAllActive());
    }

    @GetMapping("/getByRequestId/{requestId}")
    public ResponseEntity<List<RentQueue>>  getByRequestId(@PathVariable Long requestId) {
        //return service.getByRequestId(requestId);
        return ResponseEntity.ok(service.getByRequestId(requestId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{requestId}/{userId}")
    public ResponseEntity<Void> deleteByRequestIdAndUserId(@PathVariable Long requestId, @PathVariable Long userId) {
        service.deleteByRequestIdAndUserId(requestId, userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/updateStatus/{requestId}/{userId}")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long requestId,
            @PathVariable Long userId,
            @RequestParam QueueStatus status) {
        service.updateStatusByRequestIdAndLandlordId(requestId, userId, status);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/updateStatus/{id}")
    public ResponseEntity<Void> updateStatusById(
            @PathVariable Long id,
            @RequestParam QueueStatus status) {
        service.updateStatusById(id, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/public/{token}")
    public ResponseEntity<List<RentQueue>> getResponsesByToken(@PathVariable String token) {
        //return service.getByRequestPublicToken(token);
        return ResponseEntity.ok(service.getByRequestPublicToken(token));
    }
}
