package com.quyunshuo.rxjava;

import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static com.quyunshuo.rxjava.Tags.TAG;

/**
 * @Author: QuYunShuo
 * @Time: 2020/4/18
 * @Class: RxJavaCombinationOperator
 * @Remark: RxJava 组合操作符
 */
public class RxJavaCombinationOperator {

    /**
     * concat()
     * 可以将多个观察者组合在一起，然后按照之前发送顺序发送事件。
     * 需要注意的是，concat() 最多只可以发送4个事件。
     */
    public void onConcat() {
        Observable.concat(Observable.just(1, 2),
                Observable.just(3, 4),
                Observable.just(5, 6),
                Observable.just(7, 8, 9))
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "==========>> onSubscribe: ");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "==========>> onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "==========>> onError: ");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "==========>> onComplete: ");
                    }
                });
    }

    /**
     * concatArray()
     * 与 concat() 作用一样，不过 concatArray() 可以发送多于 4 个被观察者。
     */
    public void onConcatArray() {
        Observable.concatArray(Observable.just(1, 2),
                Observable.just(3, 4),
                Observable.just(5, 6),
                Observable.just(7, 8),
                Observable.just(9, 10))
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "==========>> onSubscribe: ");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "==========>> onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "==========>> onError: ");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "==========>> onComplete: ");
                    }
                });
    }

    /**
     * merge() & mergeArray()
     * merge():这个方法月 concat() 作用基本一样，只是 concat() 是串行发送事件，而 merge() 并行发送事件。
     * mergeArray(): mergeArray() 与 merge() 的作用是一样的，只是它可以发送4个以上的被观察者
     */
    public void onMergeAndMergeArray() {
        // 现在来演示 concat() 和 merge() 的区别
        Observable.merge(
                Observable
                        .interval(1000, TimeUnit.MILLISECONDS)
                        .map(new Function<Long, String>() {
                            @Override
                            public String apply(Long aLong) throws Exception {
                                return "A" + aLong;
                            }
                        }),
                Observable
                        .interval(1000, TimeUnit.MILLISECONDS)
                        .map(new Function<Long, String>() {
                            @Override
                            public String apply(Long aLong) throws Exception {
                                return "B" + aLong;
                            }
                        }))
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "==========>> onSubscribe: ");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "==========>> onNext: " + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "==========>> onError: ");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "==========>> onComplete: ");
                    }
                });
        // 打印结果如下
        // 2020-04-18 18:01:43.078 20063-20063/com.quyunshuo.rxjava D/miyan: ==========>> onSubscribe:
        // 2020-04-18 18:01:44.079 20063-20181/com.quyunshuo.rxjava D/miyan: ==========>> onNext: A0
        // 2020-04-18 18:01:44.080 20063-20182/com.quyunshuo.rxjava D/miyan: ==========>> onNext: B0
        // 2020-04-18 18:01:45.079 20063-20181/com.quyunshuo.rxjava D/miyan: ==========>> onNext: A1
        // 2020-04-18 18:01:45.079 20063-20182/com.quyunshuo.rxjava D/miyan: ==========>> onNext: B1
        // 2020-04-18 18:01:46.079 20063-20181/com.quyunshuo.rxjava D/miyan: ==========>> onNext: A2
        // 2020-04-18 18:01:46.079 20063-20182/com.quyunshuo.rxjava D/miyan: ==========>> onNext: B2
        // 2020-04-18 18:01:47.079 20063-20181/com.quyunshuo.rxjava D/miyan: ==========>> onNext: A3
        // 2020-04-18 18:01:47.079 20063-20182/com.quyunshuo.rxjava D/miyan: ==========>> onNext: B3
        // 2020-04-18 18:01:48.079 20063-20181/com.quyunshuo.rxjava D/miyan: ==========>> onNext: A4
        // 2020-04-18 18:01:48.080 20063-20182/com.quyunshuo.rxjava D/miyan: ==========>> onNext: B4
        // 2020-04-18 18:01:49.079 20063-20181/com.quyunshuo.rxjava D/miyan: ==========>> onNext: A5
        // 2020-04-18 18:01:49.079 20063-20181/com.quyunshuo.rxjava D/miyan: ==========>> onNext: B5
        // ……
        // 从结果可以看出，A 和 B 的事件序列都可以发出
        // 将以上的代码换成 concat() 看看打印结果：
        Observable.concat(
                Observable
                        .interval(1000, TimeUnit.MILLISECONDS)
                        .map(new Function<Long, String>() {
                            @Override
                            public String apply(Long aLong) throws Exception {
                                return "A" + aLong;
                            }
                        }),
                Observable
                        .interval(1000, TimeUnit.MILLISECONDS)
                        .map(new Function<Long, String>() {
                            @Override
                            public String apply(Long aLong) throws Exception {
                                return "B" + aLong;
                            }
                        }))
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "==========>> onSubscribe: ");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "==========>> onNext: " + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "==========>> onError: ");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "==========>> onComplete: ");
                    }
                });
        // 2020-04-18 18:03:29.064 21400-21400/com.quyunshuo.rxjava D/miyan: ==========>> onSubscribe:
        // 2020-04-18 18:03:30.066 21400-21506/com.quyunshuo.rxjava D/miyan: ==========>> onNext: A0
        // 2020-04-18 18:03:31.066 21400-21506/com.quyunshuo.rxjava D/miyan: ==========>> onNext: A1
        // 2020-04-18 18:03:32.066 21400-21506/com.quyunshuo.rxjava D/miyan: ==========>> onNext: A2
        // 2020-04-18 18:03:33.066 21400-21506/com.quyunshuo.rxjava D/miyan: ==========>> onNext: A3
        // 2020-04-18 18:03:34.067 21400-21506/com.quyunshuo.rxjava D/miyan: ==========>> onNext: A4
        // 2020-04-18 18:03:35.066 21400-21506/com.quyunshuo.rxjava D/miyan: ==========>> onNext: A5
        // 2020-04-18 18:03:36.066 21400-21506/com.quyunshuo.rxjava D/miyan: ==========>> onNext: A6
        // 2020-04-18 18:03:37.067 21400-21506/com.quyunshuo.rxjava D/miyan: ==========>> onNext: A7
        // 2020-04-18 18:03:38.066 21400-21506/com.quyunshuo.rxjava D/miyan: ==========>> onNext: A8
        // 2020-04-18 18:03:39.066 21400-21506/com.quyunshuo.rxjava D/miyan: ==========>> onNext: A9
        // 2020-04-18 18:03:40.066 21400-21506/com.quyunshuo.rxjava D/miyan: ==========>> onNext: A10
        // 从结果可以知道，只有等到第一个被观察者发送完事件之后，第二个被观察者才会发送事件
        // mergeArray() 与 merge() 的作用是一样的，只是它可以发送4个以上的被观察者
    }

    /**
     * concatArrayDelayError() & mergeArrayDelayError()
     * 在 concatArray() 和 mergeArray() 两个方法当中，如果其中有一个被观察者发送了一个 Error 事件，
     * 那么就会停止发送事件，如果你想 onError() 事件延迟到所有被观察者都发送完事件后再执行的话，
     * 就可以使用  concatArrayDelayError() 和 mergeArrayDelayError()
     */
    public void concatArrayDelayErrorAndMergeArrayDelayError() {
        // 演示concatArray()
        Observable.concatArray(
                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        e.onNext(1);
                        e.onError(new NumberFormatException());
                    }
                }),
                Observable.just(2, 3, 4))
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "==========>> onSubscribe: ");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "==========>> onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "==========>> onError: " + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "==========>> onComplete: ");
                    }
                });
        // 2020-04-18 18:12:00.364 26336-26336/com.quyunshuo.rxjava D/miyan: ==========>> onSubscribe:
        // 2020-04-18 18:12:00.364 26336-26336/com.quyunshuo.rxjava D/miyan: ==========>> onNext: 1
        // 2020-04-18 18:12:00.364 26336-26336/com.quyunshuo.rxjava D/miyan: ==========>> onError: java.lang.NumberFormatException
        // 从结果可以知道，确实中断了，现在换用 concatArrayDelayError()，代码如下：
        Observable.concatArrayDelayError(
                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        e.onNext(1);
                        e.onError(new NumberFormatException());
                    }
                }), Observable.just(2, 3, 4))
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "==========>> onSubscribe: ");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "==========>> onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "==========>> onError: " + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "==========>> onComplete: ");
                    }
                });
        // 2020-04-18 18:13:56.053 27964-27964/com.quyunshuo.rxjava D/miyan: ==========>> onSubscribe:
        // 2020-04-18 18:13:56.053 27964-27964/com.quyunshuo.rxjava D/miyan: ==========>> onNext: 1
        // 2020-04-18 18:13:56.054 27964-27964/com.quyunshuo.rxjava D/miyan: ==========>> onNext: 2
        // 2020-04-18 18:13:56.054 27964-27964/com.quyunshuo.rxjava D/miyan: ==========>> onNext: 3
        // 2020-04-18 18:13:56.054 27964-27964/com.quyunshuo.rxjava D/miyan: ==========>> onNext: 4
        // 2020-04-18 18:13:56.054 27964-27964/com.quyunshuo.rxjava D/miyan: ==========>> onError: java.lang.NumberFormatException
        // 从结果可以看到，onError 事件是在所有被观察者发送完事件才发送的
        // mergeArrayDelayError() 也是有同样的作用。
    }

    /**
     * zip()
     * 会将多个被观察者合并，根据各个被观察者发送事件的顺序一个个结合起来，
     * 最终发送的事件数量会与源 Observable 中最少事件的数量一样。
     */
    public void onZip() {
        Observable.zip(
                Observable
                        .intervalRange(1, 3, 1, 1, TimeUnit.SECONDS)
                        .map(new Function<Long, String>() {
                            @Override
                            public String apply(Long aLong) throws Exception {
                                String s1 = "A" + aLong;
                                Log.d(TAG, "==========>> apply: A 发送的事件 " + s1);
                                return s1;
                            }
                        }),
                Observable
                        .intervalRange(1, 5, 1, 1, TimeUnit.SECONDS)
                        .map(new Function<Long, String>() {
                            @Override
                            public String apply(Long aLong) throws Exception {
                                String s2 = "B" + aLong;
                                Log.d(TAG, "==========>> apply: B 发送的事件 " + s2);
                                return s2;
                            }
                        }),
                new BiFunction<String, String, String>() {
                    @Override
                    public String apply(String s, String s2) throws Exception {
                        return s + s2;
                    }
                })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "==========>> onSubscribe: ");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "==========>> onNext: " + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "==========>> onError: ");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "==========>> onComplete: ");
                    }
                });
        // 上面代码中有两个 Observable，第一个发送事件的数量为3个，第二个发送事件的数量为5个。
        // 现在来看下打印结果：
        // 2020-04-18 18:31:15.343 32104-32104/com.quyunshuo.rxjava D/miyan: ==========>> onSubscribe:
        // 2020-04-18 18:31:16.344 32104-32184/com.quyunshuo.rxjava D/miyan: ==========>> apply: A 发送的事件 A1
        // 2020-04-18 18:31:16.345 32104-32185/com.quyunshuo.rxjava D/miyan: ==========>> apply: B 发送的事件 B1
        // 2020-04-18 18:31:16.345 32104-32185/com.quyunshuo.rxjava D/miyan: ==========>> onNext: A1B1
        // 2020-04-18 18:31:17.344 32104-32184/com.quyunshuo.rxjava D/miyan: ==========>> apply: A 发送的事件 A2
        // 2020-04-18 18:31:17.345 32104-32185/com.quyunshuo.rxjava D/miyan: ==========>> apply: B 发送的事件 B2
        // 2020-04-18 18:31:17.345 32104-32185/com.quyunshuo.rxjava D/miyan: ==========>> onNext: A2B2
        // 2020-04-18 18:31:18.344 32104-32185/com.quyunshuo.rxjava D/miyan: ==========>> apply: B 发送的事件 B3
        // 2020-04-18 18:31:18.344 32104-32184/com.quyunshuo.rxjava D/miyan: ==========>> apply: A 发送的事件 A3
        // 2020-04-18 18:31:18.345 32104-32185/com.quyunshuo.rxjava D/miyan: ==========>> onNext: A3B3
        // 2020-04-18 18:31:18.345 32104-32185/com.quyunshuo.rxjava D/miyan: ==========>> onComplete:
        // 可以发现最终接收到的事件数量是3，那么为什么第二个 Observable 没有发送第5个事件呢？
        // 因为在这之前第一个 Observable 已经发送了 onComplete 事件，所以第二个 Observable 不会再发送事件。
    }

    /**
     * combineLatest() & combineLatestDelayError()
     * combineLatest() 的作用与 zip() 类似，但是 combineLatest() 发送事件的序列是与发送的时间线有关的，
     * 当 combineLatest() 中所有的 Observable 都发送了事件，只要其中有一个 Observable 发送事件，
     * 这个事件就会和其他 Observable 最近发送的事件结合起来发送，这样可能还是比较抽象，看看以下例子代码。
     */
    public void onCombineLatestAndCombineLatestDelayError() {
        Observable.combineLatest(
                Observable.intervalRange(1, 2, 1, 1, TimeUnit.SECONDS)
                        .map(new Function<Long, String>() {
                            @Override
                            public String apply(Long aLong) throws Exception {
                                String s1 = "A" + aLong;
                                Log.d(TAG, "==========>> apply: A 发送的事件 " + s1);
                                return s1;
                            }
                        }),
                Observable.intervalRange(1, 3, 2, 2, TimeUnit.SECONDS)
                        .map(new Function<Long, String>() {
                            @Override
                            public String apply(Long aLong) throws Exception {
                                String s2 = "B" + aLong;
                                Log.d(TAG, "==========>> apply: B 发送的事件 " + s2);
                                return s2;
                            }
                        }),
                new BiFunction<String, String, String>() {
                    @Override
                    public String apply(String s, String s2) throws Exception {
                        return s + s2;
                    }
                })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "==========>> onSubscribe ");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "==========>> 最终接收到的事件 " + s);
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
        // 2020-04-18 18:36:58.355 1203-1203/com.quyunshuo.rxjava D/miyan: ==========>> onSubscribe
        // 2020-04-18 18:36:59.357 1203-1302/com.quyunshuo.rxjava D/miyan: ==========>> apply: A 发送的事件 A1
        // 2020-04-18 18:37:00.356 1203-1302/com.quyunshuo.rxjava D/miyan: ==========>> apply: A 发送的事件 A2
        // 2020-04-18 18:37:00.357 1203-1303/com.quyunshuo.rxjava D/miyan: ==========>> apply: B 发送的事件 B1
        // 2020-04-18 18:37:00.357 1203-1303/com.quyunshuo.rxjava D/miyan: ==========>> 最终接收到的事件 A2B1
        // 2020-04-18 18:37:02.358 1203-1303/com.quyunshuo.rxjava D/miyan: ==========>> apply: B 发送的事件 B2
        // 2020-04-18 18:37:02.360 1203-1303/com.quyunshuo.rxjava D/miyan: ==========>> 最终接收到的事件 A2B2
        // 2020-04-18 18:37:04.358 1203-1303/com.quyunshuo.rxjava D/miyan: ==========>> apply: B 发送的事件 B3
        // 2020-04-18 18:37:04.360 1203-1303/com.quyunshuo.rxjava D/miyan: ==========>> 最终接收到的事件 A2B3
        // 2020-04-18 18:37:04.362 1203-1303/com.quyunshuo.rxjava D/miyan: ==========>> onComplete
        // 分析上面的代码，Observable A 会每隔1秒就发送一次事件，Observable B 会隔2秒发送一次事件
        // 分析上述结果可以知道，当发送 A1 事件之后，因为 B 并没有发送任何事件，所以根本不会发生结合。
        // 当 B 发送了 B1 事件之后，就会与 A 最近发送的事件 A2 结合成 A2B1，这样只有后面一有被观察者发送事件，
        // 这个事件就会与其他被观察者最近发送的事件结合起来了。
        // 因为 combineLatestDelayError() 就是多了延迟发送 onError() 功能，这里就不再赘述了。
    }

    /**
     * reduce()
     * 与 scan() 操作符的作用也是将发送数据以一定逻辑聚合起来，这两个的区别在于
     * scan() 每处理一次数据就会将事件发送给观察者，而 reduce() 会将所有数据聚合在一起才会发送事件给观察者。
     */
    public void onReduce() {
        Disposable subscribe = Observable.just(0, 1, 2, 3)
                .reduce(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        int res = integer + integer2;
                        Log.d(TAG, "==========>> integer " + integer);
                        Log.d(TAG, "==========>> integer2 " + integer2);
                        Log.d(TAG, "==========>> res " + res);
                        return res;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "==========>> accept " + integer);
                    }
                });
        // 2020-04-18 18:42:43.081 3746-3746/com.quyunshuo.rxjava D/miyan: ==========>> integer 0
        // 2020-04-18 18:42:43.081 3746-3746/com.quyunshuo.rxjava D/miyan: ==========>> integer2 1
        // 2020-04-18 18:42:43.081 3746-3746/com.quyunshuo.rxjava D/miyan: ==========>> res 1
        // 2020-04-18 18:42:43.081 3746-3746/com.quyunshuo.rxjava D/miyan: ==========>> integer 1
        // 2020-04-18 18:42:43.081 3746-3746/com.quyunshuo.rxjava D/miyan: ==========>> integer2 2
        // 2020-04-18 18:42:43.081 3746-3746/com.quyunshuo.rxjava D/miyan: ==========>> res 3
        // 2020-04-18 18:42:43.081 3746-3746/com.quyunshuo.rxjava D/miyan: ==========>> integer 3
        // 2020-04-18 18:42:43.081 3746-3746/com.quyunshuo.rxjava D/miyan: ==========>> integer2 3
        // 2020-04-18 18:42:43.081 3746-3746/com.quyunshuo.rxjava D/miyan: ==========>> res 6
        // 2020-04-18 18:42:43.081 3746-3746/com.quyunshuo.rxjava D/miyan: ==========>> accept 6
        // 从结果可以看到，其实就是前2个数据聚合之后，然后再与后1个数据进行聚合，一直到没有数据为止。
    }

    /**
     * collect()
     * 将数据收集到数据结构当中。
     */
    public void onCollect() {
        Disposable subscribe = Observable.just(1, 2, 3, 4, 5)
                .collect(
                        new Callable<ArrayList<Integer>>() {
                            @Override
                            public ArrayList<Integer> call() throws Exception {
                                return new ArrayList<>();
                            }
                        },
                        new BiConsumer<ArrayList<Integer>, Integer>() {
                            @Override
                            public void accept(ArrayList<Integer> integers, Integer integer) throws Exception {
                                integers.add(integer);
                            }
                        })
                .subscribe(new Consumer<ArrayList<Integer>>() {
                    @Override
                    public void accept(ArrayList<Integer> integers) throws Exception {
                        Log.d(TAG, "==========>> accept: " + integers);
                    }
                });
        // ==========>> accept: [1, 2, 3, 4, 5]
    }

    /**
     * startWith() & startWithArray()
     * 在发送事件之前追加事件,追加的事件会先发出
     * startWith():         追加一个事件
     * startWithArray():    可以追加多个事件
     */
    public void onStartWithAndStartWithArray() {
        Disposable subscribe = Observable.just(5, 6, 7)
                .startWithArray(2, 3, 4)
                .startWith(1)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "==========>> accept " + integer);
                    }
                });
        // ==========>> accept 1
        // ==========>> accept 2
        // ==========>> accept 3
        // ==========>> accept 4
        // ==========>> accept 5
        // ==========>> accept 6
        // ==========>> accept 7
    }

    /**
     * count()
     * 返回被观察者发送事件的数量。
     */
    public void onCount() {
        Disposable subscribe = Observable.just(1, 2, 3)
                .count()
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "==========>> aLong " + aLong);
                    }
                });
        // ==========>> aLong 3
    }
}
