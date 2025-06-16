package kz.brdevelopment.test.controller;

import kz.brdevelopment.test.dto.UpdateQueueStatusById;
import kz.brdevelopment.test.dto.UpdateQueueStatusByRequestInfo;
import kz.brdevelopment.test.model.*;
import kz.brdevelopment.test.service.RentQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

   /* @PutMapping("/updateStatus/{requestId}/{userId}")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long requestId,
            @PathVariable Long userId,
            @RequestParam QueueStatus status,
            @RequestParam(name = "byClient", required = false, defaultValue = "false") boolean byClient) {

        UpdateQueueStatusByRequestInfo queueStatusByRequestInfo = UpdateQueueStatusByRequestInfo.builder()
                .requestId(requestId)
                .userId(userId)
                .status(status)
                .byClient(byClient)
                .build();

        service.updateStatusByRequestIdAndLandlordId(queueStatusByRequestInfo);
        return ResponseEntity.ok().build();
    }*/

    @PutMapping("/updateStatus/{id}")
    public ResponseEntity<Void> updateStatusById(
            @PathVariable Long id,
            @RequestParam QueueStatus status,
            @RequestParam(name = "byClient", required = false, defaultValue = "false") boolean byClient) {
        UpdateQueueStatusById queueStatusById = UpdateQueueStatusById.builder()
                .queueId(id)
                .status(status)
                .byClient(byClient)
                .build();
        service.updateStatusById(queueStatusById);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/public/{token}")
    public ResponseEntity<List<RentQueue>> getResponsesByToken(@PathVariable String token) {
        //return service.getByRequestPublicToken(token);
        return ResponseEntity.ok(service.getByRequestPublicToken(token));
    }
}
