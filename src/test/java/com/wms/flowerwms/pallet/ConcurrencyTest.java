package com.wms.flowerwms.pallet;

import com.wms.flowerwms.pallet.repository.PalletRepository;
import com.wms.flowerwms.pallet.service.PalletCommandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ConcurrencyTest {

    @Autowired PalletCommandService palletCommandService;
    @Autowired PalletRepository palletRepository;

    @Test
    public void inboundConcurrencyTest() throws InterruptedException {
        Long palletId = palletRepository.findAll().get(0).getId();

        int threads = 30;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            pool.submit(() -> {
                try {
                    palletCommandService.inbound(palletId, 1);
                } catch (Exception e) {
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        int used = palletRepository.findById(palletId).orElseThrow().getUsedBoxQty();
        System.out.println("테스트 후 usedBoxQty 개수 : " + used);
        assertThat(used).isBetween(1, threads);
    }
}