package com.example.reactive_programing.back_pressure;

import com.example.reactive_programing.util.Logger;
import com.example.reactive_programing.util.TimeUtils;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

/**
 * Unbounded request 일 경우, DownStream 에 BackPressure Drop 전략을 적용하는 예제
 *  - DownStream 으로 전달 할 데이터가 버퍼에 가득 찰 경우, 버퍼 밖에서 대기하는 먼저 emit 된 데이터를 Drop 시키는 전략
 *  1. 버퍼가 가득 차게된다.
 *  2. 가득 찻을 때 오는 데이터들은 바로 Drop 이 된다.
 *  3. 버퍼가 비워졌을때 부터 오는 데이터들을 받는다.
 */
public class BackPressureStrategyDropExample {

    public static void main(String[] args) {
        Flux
                .interval(Duration.ofMillis(1L))
                .onBackpressureDrop(dropped -> Logger.info("# dropped: {}", dropped))
                .publishOn(Schedulers.parallel()) // 스레드 하나를 더 추가
                .subscribe(data -> {
                    TimeUtils.sleep(5L); // Producer 에서 처리하는 속도보다 Subscriber 에서 처리하는 속도가 느림
                    Logger.onNext(data);
                }, Logger::onError);

        TimeUtils.sleep(2000L);
    }
}
