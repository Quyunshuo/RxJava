package com.quyunshuo.rxjava;

import android.util.Log;

import java.util.ArrayList;
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
 * @Class: RxJavaConditionalOperator
 * @Remark: RxJava 条件操作符
 */
public class RxJavaConditionalOperator {

    /**
     * all()
     * 判断事件序列是否全部满足某个事件，如果都满足则返回 true，反之则返回 false。
     */
    public void onAll() {
        Disposable subscribe = Observable.just(1, 2, 3, 4)
                .all(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer < 5;
                    }
                })
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        Log.d(TAG, "==========>> aBoolean " + aBoolean);
                    }
                });
    }

    /**
     * takeWhile()
     * 可以设置条件，当某个数据满足条件时就会发送该数据，反之则不发送。
     */
    public void onTakeWhile() {
        Disposable subscribe = Observable.just(1, 2, 3, 4)
                .takeWhile(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer < 3;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "==========>> integer " + integer);
                    }
                });
    }

    /**
     * skipWhile()
     * 可以设置条件，当某个数据满足条件时不发送该数据，反之则发送。
     */
    public void onSkipWhile() {
        Disposable subscribe = Observable.just(1, 2, 3, 4)
                .skipWhile(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer < 3;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "==========>> integer " + integer);
                    }
                });
    }

    /**
     * takeUntil()
     * 可以设置条件，当事件满足此条件时，下一次的事件就不会被发送了
     */
    public void onTakeUntil() {
        Disposable subscribe = Observable.just(1, 2, 3, 4, 5, 6)
                .takeUntil(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer > 3;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "==========>> integer " + integer);
                    }
                });
    }

    /**
     * skipUntil()
     * 当 skipUntil() 中的 Observable 发送事件了，原来的 Observable 才会发送事件给观察者。
     */
    public void onSkipUntil() {
        Observable.intervalRange(1, 5, 0, 1, TimeUnit.SECONDS)
                .skipUntil(Observable.intervalRange(6, 5, 3, 1, TimeUnit.SECONDS))
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "==========>> onSubscribe ");
                    }

                    @Override
                    public void onNext(Long along) {
                        Log.d(TAG, "==========>> onNext " + along);
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
        // ==========>> onNext 4
        // ==========>> onNext 5
        // ==========>> onComplete
        // 从结果可以看出，skipUntil() 里的 Observable 并不会发送事件给观察者
    }

    /**
     * sequenceEqual()
     * 判断两个 Observable 发送的事件是否相同
     */
    public void onSequenceEqual() {
        Disposable subscribe = Observable.sequenceEqual(Observable.just(1, 2, 3),
                Observable.just(1, 2, 3))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        Log.d(TAG, "==========>> onNext " + aBoolean);
                    }
                });
    }

    /**
     * contains()
     * 判断事件序列中是否含有某个元素，如果有则返回 true，如果没有则返回 false。
     */
    public void onContains() {
        Disposable subscribe = Observable.just(1, 2, 3)
                .contains(3)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        Log.d(TAG, "==========>> onNext " + aBoolean);
                    }
                });
    }

    /**
     * isEmpty()
     * 判断事件序列是否为空
     */
    public void onIsEmpty() {
        Disposable subscribe = Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onComplete();
            }
        })
                .isEmpty()
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        Log.d(TAG, "==========>> onNext " + aBoolean);
                    }
                });
    }

    /**
     * amb()
     * amb() 要传入一个 Observable 集合，但是只会发送最先发送事件的 Observable 中的事件，
     * 其余 Observable 将会被丢弃。
     */
    public void onAmb() {
        ArrayList<Observable<Long>> list = new ArrayList<>();

        list.add(Observable.intervalRange(1, 5, 2, 1, TimeUnit.SECONDS));
        list.add(Observable.intervalRange(6, 5, 0, 1, TimeUnit.SECONDS));

        Disposable subscribe = Observable.amb(list)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "==========>> aLong " + aLong);
                    }
                });
        // ==========>> aLong 6
        // ==========>> aLong 7
        // ==========>> aLong 8
        // ==========>> aLong 9
        // ==========>> aLong 10
    }

    /**
     * defaultIfEmpty()
     * 如果被观察者只发送一个 onComplete() 事件，则可以利用这个方法发送一个值。
     */
    public void onDefaultIfEmpty() {
        Disposable subscribe = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onComplete();
            }
        })
                .defaultIfEmpty(666)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "==========>> onNext " + integer);
                    }
                });
    }
}
