package kz.brdevelopment.test.service;

import kz.brdevelopment.test.dto.UpdateQueueStatusById;
import kz.brdevelopment.test.dto.UpdateQueueStatusByRequestInfo;
import kz.brdevelopment.test.model.*;
import kz.brdevelopment.test.repository.LandlordRepository;
import kz.brdevelopment.test.repository.RentQueueRepository;
import kz.brdevelopment.test.repository.RentRequestHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentQueueService {
    private final RentQueueRepository queueRepository;
    private final RentRequestHistoryRepository requestHistoryRepository;
    private final LandlordRepository landlordRepository;

    public RentQueue addQueue(RentQueue queue) {
        RentQueue saved = queueRepository.save(queue);
        //Landlord user = saved.getLandlord();
        Landlord user = landlordRepository.findById(saved.getLandlord().getId())
                .orElseThrow(() -> new RuntimeException("Landlord not found"));
        requestHistoryRepository.save(
                RentRequestHistory.builder()
                        .rentRequest(queue.getRequest())
                        .createdAt(LocalDateTime.now())
                        .historyAction(RequestHistoryAction.QUEUE_CREATED)
                        .action("Пользователь " + user.getName() +
                                " (@" + user.getUsername() + " ID: " + user.getId() + ") " +
                                "оставил отклик: \"" + queue.getComment() + "\"")
                        .build()
        );
        return saved;
    }

    public List<RentQueue> getAll() {
        return queueRepository.findAll();
    }

    public List<RentQueue> getAllActive() {
        return queueRepository.findByStatusNot(QueueStatus.CANCELED);
    }

    public List<RentQueue> getByRequestId(Long requestId) {
        return queueRepository.findByRequestIdAndStatusNot(requestId, QueueStatus.CANCELED);
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

    /*@Transactional
    public void updateStatusByRequestIdAndLandlordId(UpdateQueueStatusByRequestInfo queueStatusByRequestInfo) {
        QueueStatus status = queueStatusByRequestInfo.getStatus();
        Long requestId = queueStatusByRequestInfo.getRequestId();
        Long userId = queueStatusByRequestInfo.getUserId();
        boolean isByClient = queueStatusByRequestInfo.isByClient();
        queueRepository.updateStatusByRequestIdAndLandlordId(status, requestId, userId);


        RentQueue rentQueue = queueRepository.findByRequestIdAndLandlordIdAndStatusNot(requestId, userId, QueueStatus.CANCELED);
        addUpdatedStatusToHistory(rentQueue, status, isByClient);
    }*/

    private void addUpdatedStatusToHistory(RentQueue rentQueue, QueueStatus status, boolean isByClient) {
        RequestHistoryAction historyAction = null;
        String statusRusText = "";
        switch(status) {
            case PENDING -> {
                historyAction = RequestHistoryAction.QUEUE_PENDED;
                statusRusText = "'В ожидании'";
            }
            case LIKED -> {
                historyAction = RequestHistoryAction.QUEUE_LIKED;
                statusRusText = "'Нравится'";
            }
            case DISLIKED -> {
                historyAction = RequestHistoryAction.QUEUE_DISLIKED;
                statusRusText = "'Не нравится'";
            }
            case SELECTED -> {
                historyAction = RequestHistoryAction.QUEUE_SELECTED;
                statusRusText = "'Выбран'";
            }
            case CANCELED ->  {
                historyAction = RequestHistoryAction.QUEUE_CANCELED;
                statusRusText = "'Удален'";
            }
        }
        String actor;
        String action = " обновил статус отклика \"" + rentQueue.getComment() + "\" на " + statusRusText;
        Landlord user = landlordRepository.findById(rentQueue.getLandlord().getId())
                .orElseThrow(() -> new RuntimeException("Landlord not found"));
        if (status == QueueStatus.CANCELED) {
            action = " удалил свой отклик \"" + rentQueue.getComment() + "\"";
        }
        if (isByClient) {
            actor = "Клиент";
            action = " обновил статус отклика \"" + rentQueue.getComment() + "\" пользователя " +
                    user.getName() + " (@" + user.getUsername() + " ID: " + user.getId() + ") на " + statusRusText;
        }
        else {

            actor = "Пользователь " + user.getName() +
                    " (@" + user.getUsername() + " ID: " + user.getId() + ")";
        }

        requestHistoryRepository.save(
                RentRequestHistory.builder()
                        .rentRequest(rentQueue.getRequest())
                        .createdAt(LocalDateTime.now())
                        .historyAction(historyAction)
                        .action(actor + action)
                        .build()
        );
    }

    @Transactional
    public void updateStatusById(UpdateQueueStatusById queueStatusById/*Long id, QueueStatus status*/) {
        QueueStatus status = queueStatusById.getStatus();
        Long id = queueStatusById.getQueueId();
        boolean isByClient = queueStatusById.isByClient();
        queueRepository.updateStatusById(status, id);

        RentQueue rentQueue = queueRepository.getReferenceById(id);
        addUpdatedStatusToHistory(rentQueue, status, isByClient);
       /* requestHistoryRepository.save(
                RentRequestHistory.builder()
                        .rentRequest(queueRepository.getReferenceById(id).getRequest())
                        .createdAt(LocalDateTime.now())
                        .action("Клиент обновил статус отклика на " + status)
                        .build()
        );*/
    }
}
