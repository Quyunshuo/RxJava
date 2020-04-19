package com.quyunshuo.rxjava;

import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

import static com.quyunshuo.rxjava.Tags.TAG;

/**
 * @Author: QuYunShuo
 * @Time: 2020/4/19
 * @Class: RxJavaBackPressure
 * @Remark: RxJava背压策略
 * onBackPressure1-2()在MainActivity中
 */
public class RxJavaBackPressure {

    /**
     * 同步下存在的问题
     * 同步订阅中，被观察者 & 观察者工作于同一线程
     * 同步订阅关系中没有缓存区
     * 被观察者在发送1个事件后，必须等待观察者接收后，才能继续发下1个事件
     * 所以，实际上并不会出现被观察者发送事件速度 > 观察者接收事件速度的情况
     * 可是，却会出现被观察者发送事件数量 > 观察者接收事件数量的问题
     * 如：观察者只能接受3个事件，但被观察者却发送了4个事件，所以出现了不匹配情况
     */
    public void onBackPressure3() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                // 被观察者发送事件数量  4个
                Log.d(TAG, "==========>> subscribe: 发送了事件1");
                emitter.onNext(1);
                Log.d(TAG, "==========>> subscribe: 发送了事件2");
                emitter.onNext(2);
                Log.d(TAG, "==========>> subscribe: 发送了事件3");
                emitter.onNext(3);
                Log.d(TAG, "==========>> subscribe: 发送了事件4");
                emitter.onNext(4);
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.d(TAG, "==========>> onSubscribe: ");
                        s.request(3);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "==========>> onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.d(TAG, "==========>> onError: " + t);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "==========>> onComplete: ");
                    }
                });
        // ==========>> onSubscribe:
        // ==========>> subscribe: 发送了事件1
        // ==========>> onNext: 1
        // ==========>> subscribe: 发送了事件2
        // ==========>> onNext: 2
        // ==========>> subscribe: 发送了事件3
        // ==========>> onNext: 3
        // ==========>> subscribe: 发送了事件4
        // ==========>> onError: io.reactivex.exceptions.MissingBackpressureException: create: could not emit value due to lack of requests
        // 对于没有缓存区概念的同步订阅关系来说，单纯采用控制观察者的接收事件数量（响应式拉取）实际上就等于 “单相思”，
        // 虽然观察者控制了要接收3个事件，但假设被观察者需要发送4个事件，还是会出现问题。
        // 解决办法看onBackPressure4()
    }

    /**
     * 控制被观察者发送事件的速度
     * 同步
     */
    public void onBackPressure4() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                // 调用emitter.requested()获取当前观察者需要接收的事件数量
                long n = emitter.requested();
                Log.d(TAG, "==========>> requested: 观察者可接收事件" + n);
                // 根据emitter.requested()的值，即当前观察者需要接收的事件数量来发送事件
                for (int i = 1; i <= n; i++) {
                    Log.d(TAG, "==========>> subscribe: 发送了事件" + i);
                    emitter.onNext(i);
                }
            }
        }, BackpressureStrategy.ERROR)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.d(TAG, "==========>> onSubscribe");
                        // 设置观察者每次能接受10个事件
                        s.request(10);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "==========>> 接收到了事件" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.w(TAG, "==========>> onError: ", t);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "==========>> onComplete");
                    }
                });
        // ==========>> onSubscribe
        // ==========>> requested: 观察者可接收事件10
        // ==========>> subscribe: 发送了事件1
        // ==========>> 接收到了事件1
        // ==========>> subscribe: 发送了事件2
        // ==========>> 接收到了事件2
        // ==========>> subscribe: 发送了事件3
        // ==========>> 接收到了事件3
        // ==========>> subscribe: 发送了事件4
        // ==========>> 接收到了事件4
        // ==========>> subscribe: 发送了事件5
        // ==========>> 接收到了事件5
        // ==========>> subscribe: 发送了事件6
        // ==========>> 接收到了事件6
        // ==========>> subscribe: 发送了事件7
        // ==========>> 接收到了事件7
        // ==========>> subscribe: 发送了事件8
        // ==========>> 接收到了事件8
        // ==========>> subscribe: 发送了事件9
        // ==========>> 接收到了事件9
        // ==========>> subscribe: 发送了事件10
        // ==========>> 接收到了事件10
    }

    /**
     * 背压模式类型
     * BackpressureStategy.MISSING  没有采用任务背压
     * BackpressureStategy.ERROR:   若上游发送事件速度超出下游处理能力，且事件缓存池已满，则抛出异常；
     * BackpressureStrategy.BUFFER: 若上游发送事件速度超出下游处理能力，则把事件存储起来等待下游处理；
     * BackpressureStrategy.DROP:   若上游发送事件速度超出下游处理能力，事件缓存池满了之后将发送的事件丢弃；
     * BackpressureStrategy.LATEST: 若上游发送事件速度超出下游处理能力，则只存储最新的128个事件。
     * 在使用背压策略模式的时候，有1种情况是需要注意的:
     * 对于自身手动创建FLowable的情况，可通过传入背压模式参数选择背压策略
     * 可是对于自动创建FLowable，却无法手动传入传入背压模式参数，那么出现流速不匹配的情况下，该如何选择 背压模式呢？
     */
    public void onBackPressure5() {
        // RxJava 2.0内部提供 封装了背压策略模式的方法
        // onBackpressureBuffer()
        // onBackpressureDrop()
        // onBackpressureLatest()
        // 默认采用BackpressureStrategy.ERROR模式

        Flowable.interval(1, TimeUnit.MILLISECONDS)
                // 添加背压策略封装好的方法，此处选择Buffer模式，即缓存区大小无限制
                .onBackpressureBuffer()
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.d(TAG, "==========>> onSubscribe");
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.d(TAG, "==========>> onNext: " + aLong);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.w(TAG, "==========>> onError: ", t);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "==========>> onComplete");
                    }
                });
    }
}