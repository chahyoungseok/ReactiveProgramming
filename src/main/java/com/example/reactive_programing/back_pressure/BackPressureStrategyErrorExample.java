package com.example.reactive_programing.back_pressure;

import com.example.reactive_programing.util.Logger;
import com.example.reactive_programing.util.TimeUtils;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

/**
 * Unbounded request 일 경우, DownStream 에 BackPressure Error 전략을 적용하는 예제
 *  - DownStream 으로 전달 할 데이터가 버퍼에 가득 찰 경우, Exception 을 발생 시키는 전략
 */
public class BackPressureStrategyErrorExample {

    public static void main(String[] args) {
        Flux
                .interval(Duration.ofMillis(1L))
                .onBackpressureError()
                .doOnNext(Logger::doOnNext)
                .publishOn(Schedulers.parallel()) // 스레드 하나를 더 추가
                .subscribe(data -> {
                    TimeUtils.sleep(5L); // Producer 에서 처리하는 속도보다 Subscriber 에서 처리하는 속도가 느림
                    Logger.onNext(data);
                }, error -> Logger.onError(error));

        TimeUtils.sleep(2000L);
    }
}
