package com.quyunshuo.rxjava;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.quyunshuo.rxjava.Tags.TAG;

/**
 * @Author: QuYunShuo
 * @Time: 2020/4/17
 * @Class: RxJavaCreateOperator
 * @Remark: RxJava 创建操作符
 */
public class RxJavaCreateOperator {

    /**
     * create()
     * 创建一个被观察者
     */
    public void onCreate() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "==========>> currentThread name: " + Thread.currentThread().getName());
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "==========>> onSubscribe");

            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "==========>> onNext " + integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "==========>> onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "==========>> onComplete");
            }
        });
    }

    /**
     * just()
     * 创建一个被观察者，并发送事件，发送的事件不可以超过10个以上
     */
    public void onJust() {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "==========>> onSubscribe");
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "==========>> onNext " + integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "==========>> onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "==========>> onComplete");
            }
        });
    }

    /**
     * fromArray()
     * 和 just() 类似，只不过 fromArray 可以传入多于10个的变量，并且可以传入一个数组
     */
    public void onFromArray() {
        Observable.fromArray("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "b", "o", "p", "q", "r", "s", "t")
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "==========>> onSubscribe");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "==========>> onNext " + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "==========>> onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "==========>> onComplete");
                    }
                });

        Integer[] array = {1, 2, 3, 4};
        Observable.fromArray(array).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "==========>> onSubscribe");
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "==========>> onNext " + integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "==========>> onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "==========>> onComplete");
            }
        });
    }

    /**
     * fromIterable()
     * 直接发送一个 List 集合数据给观察者
     */
    public void onFromIterable() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("e");
        Observable
                .fromIterable(list)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "==========>> onSubscribe");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "==========>> onNext " + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "==========>> onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "==========>> onComplete");
                    }
                });
    }

    /**
     * fromCallable()
     * 这里的 Callable 是 java.util.concurrent 中的 Callable，Callable 和 Runnable 的用法基本一致，
     * 只是它会返回一个结果值，这个结果值就是发给观察者的。
     */
    public void onFromCallable() {
        Observable.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 0;
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "==========>> onSubscribe");
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "==========>> onNext " + integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "==========>> onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "==========>> onComplete");
            }
        });
    }

    /**
     * fromFuture()
     * 参数中的 Future 是 java.util.concurrent 中的 Future，Future 的作用是增加了 cancel() 等方法操作 Callable，
     * 它可以通过 get() 方法来获取 Callable 返回的值。
     */
    public void onFromFuture() {
        final FutureTask<String> stringFutureTask = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Log.d(TAG, "CallableDemo is Running");
                return "返回结果";
            }
        });

        //doOnSubscribe() 的作用就是只有订阅时才会发送事件
        Disposable subscribe = Observable.fromFuture(stringFutureTask)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        stringFutureTask.run();
                    }
                }).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "================accept " + s);
                    }
                });
    }

    private Integer i = 100;

    /**
     * defer()
     * 直到被观察者被订阅后才会创建被观察者
     */
    public void onDefer() {
        Observable<Integer> defer = Observable.defer(new Callable<ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> call() throws Exception {
                return Observable.just(i);
            }
        });

        i = 200;

        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "==========>> onSubscribe");
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "==========>> onNext " + integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "==========>> onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "==========>> onComplete");
            }
        };
        defer.subscribe(observer);

        i = 300;

        defer.subscribe(observer);
        //因为 defer() 只有观察者订阅的时候才会创建新的被观察者，所以每订阅一次就会打印一次，并且都是打印 i 最新的值。
    }

    /**
     * timer()
     * 当到指定时间后就会发送一个 0L 的值给观察者。 相当于延时操作
     */
    public void onTimer() {
        Observable.timer(2000, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "==========>> onSubscribe");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.d(TAG, "==========>> onNext " + aLong);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "==========>> onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "==========>> onComplete");
                    }
                });
    }

    /**
     * interval()
     * 每隔一段时间就会发送一个事件，这个事件是从0开始，不断增1的数字。
     * public static Observable<Long> interval(long initialDelay, long period, TimeUnit unit)
     * initialDelay 参数，这个参数的意思就是 onSubscribe 回调之后，再次回调 onNext 的间隔时间
     */
    public void onInterval() {
        Disposable subscribe = Observable.interval(2000, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "==========>> accept: " + aLong);
                    }
                });
    }

    /**
     * intervalRange()
     * 可以指定发送事件的开始值和数量，其他与 interval() 的功能一样。
     * public static Observable<Long> intervalRange(long start, long count, long initialDelay, long period, TimeUnit unit)
     * long start: 开始值
     * long count: 数量
     * long initialDelay: onSubscribe 回调之后，再次回调 onNext 的间隔时间
     * long period: 正常发送间隔
     * TimeUnit unit: 时间单位
     */
    public void onIntervalRange() {
        Observable.intervalRange(2, 5, 2000, 1000, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "==========>> onSubscribe");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.d(TAG, "==========>> onNext " + aLong);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "==========>> onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "==========>> onComplete");
                    }
                });
    }

    /**
     * range()
     * 同时发送一定范围的事件序列。
     * public static Observable<Integer> range(final int start, final int count)
     * final int start: 开始值
     * final int count: 数量
     */
    public void onRange() {
        Disposable subscribe = Observable.range(2, 5)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "================accept " + integer);
                    }
                });
    }

    /**
     * rangeLong()
     * 作用与 range() 一样，只是数据类型为 Long
     */
    public void onRangeLong() {
        Disposable subscribe = Observable.rangeLong(0, 10)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "================accept " + aLong);

                    }
                });
    }

    /**
     * empty()
     * 直接发送 onComplete() 事件
     */
    public void onEmpty() {
        Observable.empty()
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "==========>> onSubscribe");
                    }

                    @Override
                    public void onNext(Object o) {
                        Log.d(TAG, "==========>> onNext " + o.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "==========>> onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "==========>> onComplete");
                    }
                });
    }

    /**
     * never()
     * 不发送任何事件
     */
    public void onNever() {
        Observable.never()
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "==========>> onSubscribe");
                    }

                    @Override
                    public void onNext(Object o) {
                        Log.d(TAG, "==========>> onNext " + o.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "==========>> onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "==========>> onComplete");
                    }
                });
    }

    /**
     * error()
     * 发送 onError() 事件
     */
    public void onError() {
        Observable.error(new Throwable("message"))
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "==========>> onSubscribe");
                    }

                    @Override
                    public void onNext(Object o) {
                        Log.d(TAG, "==========>> onNext " + o.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "==========>> onError" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "==========>> onComplete");
                    }
                });
    }
}
