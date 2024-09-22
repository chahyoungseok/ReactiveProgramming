package com.example.reactive_programing.back_pressure;

import com.example.reactive_programing.util.Logger;
import com.example.reactive_programing.util.TimeUtils;
import reactor.core.publisher.BufferOverflowStrategy;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

/**
 * Unbounded request 일 경우, DownStream 에 BackPressure Buffer Drop Latest 전략을 적용하는 예제
 *  - DownStream 으로 전달 할 데이터가 버퍼에 가득 찰 경우, 버퍼 안에 있는 데이터 중 가장 최근에 버퍼로 들어온 데이터부터 Drop 시키는 전략
 *
 */
public class BackPressureStrategyBufferDropLatestExample {

    public static void main(String[] args) {
        Flux
                .interval(Duration.ofMillis(300L))
                .doOnNext(data -> Logger.info("# emitted by original Flux: {}", data))
                .onBackpressureBuffer(2,
                        dropped -> Logger.info("# Overflow & dropped: {}", dropped),
                        BufferOverflowStrategy.DROP_LATEST)
                .doOnNext(data -> Logger.info("# emitted by Buffer: {}", data))
                .publishOn(Schedulers.parallel(), false, 1)
                .subscribe(data -> {
                    TimeUtils.sleep(1000L);
                    Logger.onNext(data);
                }, Logger::onError);

        TimeUtils.sleep(3000L);
    }
}
