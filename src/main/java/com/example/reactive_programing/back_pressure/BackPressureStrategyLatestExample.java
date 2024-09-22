package com.example.reactive_programing.back_pressure;

import com.example.reactive_programing.util.Logger;
import com.example.reactive_programing.util.TimeUtils;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

/**
 * Unbounded request 일 경우, DownStream 에 BackPressure Latest 전략을 적용하는 예제
 *  - DownStream 으로 전달 할 데이터가 버퍼에 가득 찰 경우, 버퍼 밖에서 대기하는 가장 나중에 emit 된 데이터부터 버퍼에 채우는 전략
 *  - 기본적으로 Drop 전략과 비슷한데, Drop 전략은 버퍼에 온 데이터를 즉시 폐기처리하지만
 *  - Latest 전략은 버퍼에 온 데이터를 즉시 폐기처리하지 않고, 가지고 있다가 버퍼에 또 하나의 데이터가 오면 가장 최근 데이터를 제외하고 모두 폐기
 */
public class BackPressureStrategyLatestExample {

    public static void main(String[] args) {
        Flux
                .interval(Duration.ofMillis(1L))
                .onBackpressureLatest()
                .publishOn(Schedulers.parallel()) // 스레드 하나를 더 추가
                .subscribe(data -> {
                    TimeUtils.sleep(5L); // Producer 에서 처리하는 속도보다 Subscriber 에서 처리하는 속도가 느림
                    Logger.onNext(data);
                }, Logger::onError);

        TimeUtils.sleep(2000L);
    }
}
