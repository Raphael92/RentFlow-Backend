package kz.brdevelopment.test.dto;

import kz.brdevelopment.test.model.QueueStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UpdateQueueStatusById {
    Long queueId;
    QueueStatus status;
    boolean byClient = false;
}
