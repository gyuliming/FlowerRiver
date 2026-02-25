package com.wms.flowerwms.global.concurrency;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;

@Component
public class OptimisticLockRetry {

    public void run(Runnable action) {
        int maxRetries = 10;
        int backoffMs = 20;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                action.run();
                return;
            } catch (OptimisticLockingFailureException e) {
                System.out.println("충돌 감지 attempt: " + attempt);
                if (attempt == maxRetries) {
                    System.out.println("재시도 소진 - 실패");
                    throw e;
                }
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