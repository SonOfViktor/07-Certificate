package com.epam.esm.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class AuditListener {
    public static final Logger logger = LoggerFactory.getLogger(AuditListener.class);

    private enum AuditOperation {
        INSERT, UPDATE, DELETE
    }

    @PostPersist
    public void onPostPersist(Object entity) {
        audit(AuditOperation.INSERT, entity);
    }

    @PreUpdate
    public void onPreUpdate(Object entity) {
        audit(AuditOperation.UPDATE, entity);
    }

    @PreRemove
    public void onPreRemove(Object entity) {
        audit(AuditOperation.DELETE, entity);
    }

    private void audit(AuditOperation operation, Object entity) {
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        logger.info("Operation {} on {} was made at {}", operation, entity, timestamp);
    }
}
