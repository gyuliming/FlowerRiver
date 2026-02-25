package com.wms.flowerwms.global.concurrency;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;

@Component
public class OptimisticLockRetry {

    public void run(Runnable action) {
        int maxRetries = 5;
        int backoffMs = 20;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                action.run();
                return;
            } catch (OptimisticLockingFailureException e) {
                if (attempt == maxRetries) throw e;
                sleep(backoffMs * attempt);
            }
        }
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}