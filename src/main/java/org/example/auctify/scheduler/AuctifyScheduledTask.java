package org.example.auctify.scheduler;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.auctify.service.auction.GoodsService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuctifyScheduledTask {

    private final GoodsService goodsService;


    // 매분 0초에 실행 (예: 12:00:00, 12:01:00, ...)
    @Scheduled(cron = "0 * * * * *")
    public void runEveryMinute() {
        System.out.println("작업 실행됨: " + System.currentTimeMillis());
        goodsService.processExpiredAuctions(); // 경매 종료 처리 호출

    }
}
