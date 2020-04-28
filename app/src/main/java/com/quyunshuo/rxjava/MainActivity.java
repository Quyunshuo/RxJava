package com.quyunshuo.rxjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.quyunshuo.rxjava.databinding.ActivityMainBinding;
import com.quyunshuo.rxjava.net.RxJavaActualCombat;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.quyunshuo.rxjava.Tags.TAG;

/**
 * @Author: QuYunShuo
 * @Time: 2020/4/17
 * @Class: MainActivity
 * @Remark:
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        initOperator();
        initBackPressure();
        initActualCombat();
    }

    private void initView() {
        binding.receiveEventTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSubscription != null) {
                    // 每次点击就会从缓冲区接收两个事件
                    mSubscription.request(2);
                }
            }
        });
    }

    /**
     * 操作符相关
     */
    private void initOperator() {
        // 创建操作符
        // RxJavaCreateOperator rxJavaCreateOperator = new RxJavaCreateOperator();
        // rxJavaCreateOperator.onCreate();
        // rxJavaCreateOperator.onJust();
        // rxJavaCreateOperator.onFromArray();
        // rxJavaCreateOperator.onFromCallable();
        // rxJavaCreateOperator.onFromFuture();
        // rxJavaCreateOperator.onFromIterable();
        // rxJavaCreateOperator.onDefer();
        // rxJavaCreateOperator.onTimer();
        // rxJavaCreateOperator.onInterval();
        // rxJavaCreateOperator.onIntervalRange();
        // rxJavaCreateOperator.onRange();
        // rxJavaCreateOperator.onRangeLong();
        // rxJavaCreateOperator.onEmpty();
        // rxJavaCreateOperator.onNever();
        // rxJavaCreateOperator.onError();

        // 转换操作符
        // RxJavaTransformOperator rxJavaTransformOperator = new RxJavaTransformOperator();
        // rxJavaTransformOperator.onMap();
        // rxJavaTransformOperator.onFlatMap();
        // rxJavaTransformOperator.onConcatMap();
        // rxJavaTransformOperator.onBuffer();
        // rxJavaTransformOperator.onGroupBy();
        // rxJavaTransformOperator.onScan();
        // rxJavaTransformOperator.onWindow();

        // 组合操作符
        // RxJavaCombinationOperator rxJavaCombinationOperator = new RxJavaCombinationOperator();
        // rxJavaCombinationOperator.onConcat();
        // rxJavaCombinationOperator.onConcatArray();
        // rxJavaCombinationOperator.onMergeAndMergeArray();
        // rxJavaCombinationOperator.concatArrayDelayErrorAndMergeArrayDelayError();
        // rxJavaCombinationOperator.onZip();
        // rxJavaCombinationOperator.onCombineLatestAndCombineLatestDelayError();
        // rxJavaCombinationOperator.onReduce();
        // rxJavaCombinationOperator.onCollect();
        // rxJavaCombinationOperator.onStartWithAndStartWithArray();
        // rxJavaCombinationOperator.onCount();

        // 功能操作符
        // RxJavaFunctionalOperator rxJavaFunctionalOperator = new RxJavaFunctionalOperator();
        // rxJavaFunctionalOperator.onDelay();
        // rxJavaFunctionalOperator.onDoOnEach();
        // rxJavaFunctionalOperator.onDoOnNext();
        // rxJavaFunctionalOperator.onDoAfterNext();
        // rxJavaFunctionalOperator.onDoOnComplete();
        // rxJavaFunctionalOperator.onDoOnError();
        // rxJavaFunctionalOperator.onDoOnSubscribe();
        // rxJavaFunctionalOperator.onDoOnDispose();
        // rxJavaFunctionalOperator.onDoOnLifecycle();
        // rxJavaFunctionalOperator.onDoOnTerminateAndDoAfterTerminate();
        // rxJavaFunctionalOperator.onDoFinally();
        // rxJavaFunctionalOperator.onOnErrorReturn();
        // rxJavaFunctionalOperator.onOnErrorResumeNext();
        // rxJavaFunctionalOperator.onOnExceptionResumeNext();
        // rxJavaFunctionalOperator.onRetry();
        // rxJavaFunctionalOperator.onRetryUntil();
        // rxJavaFunctionalOperator.onRetryWhen();
        // rxJavaFunctionalOperator.onRepeat();
        // rxJavaFunctionalOperator.onRepeatWhen();
        // rxJavaFunctionalOperator.onSubscribeOn();
        // rxJavaFunctionalOperator.onObserveOn();

        // 过滤操作符
        // RxJavaFilterOperator rxJavaFilterOperator = new RxJavaFilterOperator();
        // rxJavaFilterOperator.onFilter();
        // rxJavaFilterOperator.onOfType();
        // rxJavaFilterOperator.onSkip();
        // rxJavaFilterOperator.onSkipLast();
        // rxJavaFilterOperator.onDistinct();
        // rxJavaFilterOperator.onDistinctUntilChanged();
        // rxJavaFilterOperator.onTake();
        // rxJavaFilterOperator.onDebounce();
        // rxJavaFilterOperator.onFirstElementAndLastElement();
        // rxJavaFilterOperator.onElementAtAndElementAtOrError();

        // 条件操作符
        // RxJavaConditionalOperator rxJavaConditionalOperator = new RxJavaConditionalOperator();
        // rxJavaConditionalOperator.onAll();
        // rxJavaConditionalOperator.onTakeWhile();
        // rxJavaConditionalOperator.onSkipWhile();
        // rxJavaConditionalOperator.onTakeUntil();
        // rxJavaConditionalOperator.onSkipUntil();
        // rxJavaConditionalOperator.onSequenceEqual();
        // rxJavaConditionalOperator.onContains();
        // rxJavaConditionalOperator.onIsEmpty();
        // rxJavaConditionalOperator.onAmb();
        // rxJavaConditionalOperator.onDefaultIfEmpty();
    }

    /**
     * 背压策略相关
     */
    private void initBackPressure() {
        // 背压策略
        // RxJavaBackPressure rxJavaBackPressure = new RxJavaBackPressure();
        // onBackPressure1();
        // onBackPressure2();
        // rxJavaBackPressure.onBackPressure3();
        // rxJavaBackPressure.onBackPressure4();
        // rxJavaBackPressure.onBackPressure5();
    }

    /**
     * 实战相关
     */
    public void initActualCombat() {
        // RxJavaActualCombat rxJavaActualCombat = new RxJavaActualCombat();
        // rxJavaActualCombat.unconditionalNetworkRequestPolling();
        // rxJavaActualCombat.conditionalNetworkRequestPolling();
        // rxJavaActualCombat.networkRequestNestedCallback();
        // rxJavaActualCombat.mergingDataSources();
        // rxJavaActualCombat.onSubscribe();
    }

    /**
     * 异步
     * 背压代码演示1：观察者不接收事件的情况下，被观察者继续发送事件 & 存放到缓存区；再按需取出
     */
    public void onBackPressure1() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "==========>> subscribe: 发送事件 1");
                emitter.onNext(1);
                Log.d(TAG, "==========>> subscribe: 发送事件 2");
                emitter.onNext(2);
                Log.d(TAG, "==========>> subscribe: 发送事件 3");
                emitter.onNext(3);
                Log.d(TAG, "==========>> subscribe: 发送事件 4");
                emitter.onNext(4);
                Log.d(TAG, "==========>> subscribe: 发送事件 5");
                emitter.onNext(5);
                Log.d(TAG, "==========>> subscribe: 发送完成");
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR)
                // 指定线程 异步
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FlowableSubscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.d(TAG, "==========>> onSubscribe: ");
                        // 保存Subscription对象，等待点击按钮时（调用request(2)）观察者再接收事件
                        mSubscription = s;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "==========>> onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.d(TAG, "==========>> onError: ");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "==========>> onComplete: ");
                    }
                });
    }

    /**
     * 异步
     * 背压代码演示2：观察者不接收事件的情况下，被观察者继续发送事件至超出缓存区大小（128）
     */
    public void onBackPressure2() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                // 一共发送129个事件，即超出了缓存区的大小
                for (int i = 0; i < 129; i++) {
                    Log.d(TAG, "==========>> subscribe: 发送了事件" + i);
                    emitter.onNext(i);
                }
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.d(TAG, "==========>> onSubscribe: ");
                        // 保存Subscription对象，等待点击按钮时（调用request(2)）观察者再接收事件
                        mSubscription = s;
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
        // ......
        // ==========>> subscribe: 发送了事件124
        // ==========>> subscribe: 发送了事件125
        // ==========>> subscribe: 发送了事件126
        // ==========>> subscribe: 发送了事件127
        // ==========>> subscribe: 发送了事件128
        // ==========>> onError: io.reactivex.exceptions.MissingBackpressureException: create: could not emit value due to lack of requests
    }
}