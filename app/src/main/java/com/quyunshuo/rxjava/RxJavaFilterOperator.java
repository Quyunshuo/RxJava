package com.quyunshuo.rxjava;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

import static com.quyunshuo.rxjava.Tags.TAG;

/**
 * @Author: QuYunShuo
 * @Time: 2020/4/19
 * @Class: RxJavaFilterOperator
 * @Remark: RxJava 过滤操作符
 */
public class RxJavaFilterOperator {

    /**
     * filter()
     * 通过一定逻辑来过滤被观察者发送的事件，如果返回 true 则会发送事件，否则不会发送
     */
    public void onFilter() {
        Disposable subscribe = Observable.just(1, 2, 3, 4)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        boolean isSend = false;
                        if (integer > 2) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "==========>> accept: " + integer);
                    }
                });
    }

    /**
     * ofType()
     * 可以过滤不符合该类型事件
     */
    public void onOfType() {
        Disposable subscribe = Observable.just(1, 2, 3, "A", "B")
                .ofType(String.class)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "==========>> accept: " + s);
                    }
                });
    }

    /**
     * skip()
     * 跳过正序某些事件，count 代表跳过事件的数量
     */
    public void onSkip() {
        Disposable subscribe = Observable.just(1, 2, 3, 4, 5)
                .skip(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "==========>> accept: " + integer);
                    }
                });
        // ==========>> accept: 3
        // ==========>> accept: 4
        // ==========>> accept: 5
    }

    /**
     * skipLast()
     * 用来跳过正序的后面的事件
     */
    public void onSkipLast() {
        Disposable subscribe = Observable.just(1, 2, 3, 4, 5)
                .skipLast(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "==========>> accept: " + integer);
                    }
                });
        // ==========>> accept: 1
        // ==========>> accept: 2
        // ==========>> accept: 3
    }

    /**
     * distinct()
     * 过滤事件序列中的重复事件。
     */
    public void onDistinct() {
        Disposable subscribe = Observable.just(1, 2, 3, 2, 1)
                .distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "==========>> accept: " + integer);
                    }
                });
        // ==========>> accept: 1
        // ==========>> accept: 2
        // ==========>> accept: 3
    }

    /**
     * distinctUntilChanged()
     * 过滤掉连续重复的事件
     */
    public void onDistinctUntilChanged() {
        Disposable subscribe = Observable.just(1, 2, 3, 3, 3, 2, 1)
                .distinctUntilChanged()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "==========>> accept: " + integer);
                    }
                });
        // ==========>> accept: 1
        // ==========>> accept: 2
        // ==========>> accept: 3
        // ==========>> accept: 2
        // ==========>> accept: 1
        // 因为3连续出现了3次  所以只会发送一个，其余两个被过滤掉了
    }

    /**
     * take()
     * 控制观察者接收的事件的数量
     */
    public void onTake() {
        Disposable subscribe = Observable.just(1, 2, 3, 4, 5)
                .take(3)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "==========>> accept: " + integer);
                    }
                });
        //==========>> accept: 1
        //==========>> accept: 2
        //==========>> accept: 3
    }

    /**
     * debounce()
     * 如果两件事件发送的时间间隔小于设定的时间间隔则前一件事件就不会发送给观察者。
     */
    public void onDebounce() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                Thread.sleep(900);
                e.onNext(2);
            }
        })
                .debounce(1, TimeUnit.SECONDS)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "==========>> onSubscribe ");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "==========>> onNext " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "==========>> onError ");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "==========>> onComplete ");
                    }
                });
        // ==========>> onSubscribe
        // ==========>> onNext 2
        // 可以看到事件1并没有发送出去
    }

    /**
     * firstElement() && lastElement()
     * firstElement(): 取事件序列的第一个元素
     * lastElement():  取事件序列的最后一个元素。
     */
    public void onFirstElementAndLastElement() {
        Disposable subscribe1 = Observable.just(1, 2, 3, 4)
                .firstElement()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "==========>> firstElement " + integer);
                    }
                });

        Disposable subscribe = Observable.just(1, 2, 3, 4)
                .lastElement()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "==========>> lastElement " + integer);
                    }
                });
    }

    /**
     * elementAt() & elementAtOrError()
     * elementAt() 可以指定取出事件序列中事件，但是输入的 index 超出事件序列的总数的话就不会出现任何结果。
     * 这种情况下，你想发出异常信息的话就用 elementAtOrError() 。
     */
    public void onElementAtAndElementAtOrError() {
        Disposable subscribe = Observable.just(1, 2, 3, 4)
                .elementAt(0)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "==========>> accept " + integer);
                    }
                });
        // ==========>> accept 1
        // 将 elementAt() 的值改为5，这时是没有打印结果的，因为没有满足条件的元素

        // 替换 elementAt() 为 elementAtOrError()，代码如下:
        Disposable subscribe2 = Observable.just(1, 2, 3, 4)
                .elementAtOrError(5)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "==========>> accept " + integer);
                    }
                });
        // W/System.err: io.reactivex.exceptions.OnErrorNotImplementedException: The exception was not handled due to missing onError handler in the subscribe() method call.
        // Further reading: https://github.com/ReactiveX/RxJava/wiki/Error-Handling | java.util.NoSuchElementException
        // W/System.err:     at io.reactivex.internal.functions.Functions$OnErrorMissingConsumer.accept(Functions.java:704)
        // W/System.err:     at io.reactivex.internal.functions.Functions$OnErrorMissingConsumer.accept(Functions.java:701)
        // W/System.err:     at io.reactivex.internal.observers.ConsumerSingleObserver.onError(ConsumerSingleObserver.java:46)
        // W/System.err:     at io.reactivex.internal.operators.observable.ObservableElementAtSingle$ElementAtObserver.onComplete(ObservableElementAtSingle.java:115)
        // W/System.err:     at io.reactivex.internal.operators.observable.ObservableFromArray$FromArrayDisposable.run(ObservableFromArray.java:111)
        // W/System.err:     at io.reactivex.internal.operators.observable.ObservableFromArray.subscribeActual(ObservableFromArray.java:37)
        // W/System.err:     at io.reactivex.Observable.subscribe(Observable.java:12284)
        // W/System.err:     at io.reactivex.internal.operators.observable.ObservableElementAtSingle.subscribeActual(ObservableElementAtSingle.java:37)
        // W/System.err:     at io.reactivex.Single.subscribe(Single.java:3666)
        // W/System.err:     at io.reactivex.Single.subscribe(Single.java:3652)
        // W/System.err:     at io.reactivex.Single.subscribe(Single.java:3620)
        // W/System.err:     at com.quyunshuo.rxjava.RxJavaFilterOperator.onElementAtAndElementAtOrError(RxJavaFilterOperator.java:243)
        // W/System.err:     at com.quyunshuo.rxjava.MainActivity.initData(MainActivity.java:116)
        // W/System.err:     at com.quyunshuo.rxjava.MainActivity.onCreate(MainActivity.java:32)
        // W/System.err:     at android.app.Activity.performCreate(Activity.java:7174)
        // W/System.err:     at android.app.Instrumentation.callActivityOnCreate(Instrumentation.java:1220)
        // W/System.err:     at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2908)
        // W/System.err:     at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:3030)
        // W/System.err:     at android.app.ActivityThread.-wrap11(Unknown Source:0)
        // W/System.err:     at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1696)
        // W/System.err:     at android.os.Handler.dispatchMessage(Handler.java:105)
        // W/System.err:     at android.os.Looper.loop(Looper.java:164)
        // W/System.err:     at android.app.ActivityThread.main(ActivityThread.java:6938)
        // W/System.err:     at java.lang.reflect.Method.invoke(Native Method)
        // W/System.err:     at com.android.internal.os.Zygote$MethodAndArgsCaller.run(Zygote.java:327)
        // W/System.err:     at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:1374)
        // W/System.err: Caused by: java.util.NoSuchElementException
        // W/System.err: 	... 23 more
        // 这时候会抛出 NoSuchElementException 异常。
    }
}
