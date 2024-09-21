package com.example.reactive_programing.back_pressure;

import com.example.reactive_programing.util.Logger;
import com.example.reactive_programing.util.TimeUtils;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

/**
 * Subscriber가 처리 가능한만큼의 Request 개수를 조절하는 예제
 */

public class BackPressureExample {
    private static int count = 0;

    // Producer : range Operator 를 사용해서 DownStream 으로 Emit 하고있음
    // Subscriber : BaseSubscriber 를 사용해 요청 데이터의 개수를 조절함
    public static void main(String[] args) {
        Flux.range(1, 5)
                .doOnRequest(Logger::doOnRequest) // Subscriber 에서 요청한 개수를 출력
                .doOnNext(Logger::doOnNext) // UpStream 에서 Emit한 데이터를 출력
                .subscribe(new BaseSubscriber<Integer>() {
                    // 구독시점에 요청개수를 1로 설정한다
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        //request(1);
                        request(2);
                    }

                    // Producer에서 Emit 하면 받는 메서드
                    @Override
                    protected void hookOnNext(Integer value) {
//                        TimeUtils.sleep(2000L);
//                        Logger.onNext(value);
//                        request(1);

                        count++;
                        Logger.onNext(value);
                        if (count % 2 == 0) {
                            TimeUtils.sleep(2000L);
                            request(2);
                        }
                    }
                });
    }
}
