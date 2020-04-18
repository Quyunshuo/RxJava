package com.quyunshuo.rxjava;

import android.util.Log;

import com.quyunshuo.rxjava.model.Person;
import com.quyunshuo.rxjava.model.Plan;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;

import static com.quyunshuo.rxjava.Tags.TAG;

/**
 * @Author: QuYunShuo
 * @Time: 2020/4/17
 * @Class: RxJavaTransformOperator
 * @Remark: RxJava转换操作符
 */
public class RxJavaTransformOperator {
    /**
     * map()
     * map 可以将被观察者发送的数据类型转变成其他的类型
     */
    public void onMap() {
        Disposable subscribe = Observable.just(1, 2, 3, 4, 5, 6)
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {

                        return "转换=> " + integer;
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "==========> accept: " + s);
                    }
                });
    }

    private List<Person> getTestData() {
        List<Person> personList = new ArrayList<>();
        List<Plan> planList1 = new ArrayList<>();
        List<Plan> planList2 = new ArrayList<>();
        List<String> actionList = new ArrayList<>();
        actionList.add("喝粥");
        actionList.add("吃肉");

        Plan plan1 = new Plan("8:00", "张三 吃早饭");
        plan1.setActionList(actionList);
        planList1.add(plan1);
        Plan plan2 = new Plan("12:00", "张三 吃午饭");
        plan2.setActionList(actionList);
        planList1.add(plan2);
        Plan plan3 = new Plan("6:00", "张三 吃晚饭");
        plan3.setActionList(actionList);
        planList1.add(plan3);

        Plan plan4 = new Plan("8:00", "李四 吃早饭");
        plan4.setActionList(actionList);
        planList2.add(plan4);
        Plan plan5 = new Plan("12:00", "李四 吃午饭");
        plan5.setActionList(actionList);
        planList2.add(plan5);
        Plan plan6 = new Plan("6:00", "李四 吃晚饭");
        plan6.setActionList(actionList);
        planList2.add(plan6);

        personList.add(new Person("张三", planList1));
        personList.add(new Person("李四", planList2));
        return personList;
    }

    /**
     * flatMap()
     * 这个方法可以将事件序列中的元素进行整合加工，返回一个新的被观察者。
     */
    public void onFlatMap() {
        //现在有一个需求就是要将 Person 集合中的每个元素中的 Plan 的 action 打印出来
        Observable.fromIterable(getTestData())
                .flatMap(new Function<Person, ObservableSource<Plan>>() {
                    @Override
                    public ObservableSource<Plan> apply(Person person) {
                        return Observable.fromIterable(person.getPlanList());
                    }
                })
                .flatMap(new Function<Plan, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Plan plan) {
                        return Observable.fromIterable(plan.getActionList());
                    }
                })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "==========> onSubscribe: ");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "==========> onNext: " + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "==========> onError: " + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "==========> onComplete: ");
                    }
                });
    }

    /**
     * concatMap()
     * 和 flatMap() 基本上是一样的，只不过 concatMap() 转发出来的事件是有序的，而 flatMap() 是无序的。
     */
    public void onConcatMap() {
        // 验证flatMap()是无序的
        Observable.fromIterable(getTestData())
                .flatMap(new Function<Person, ObservableSource<Plan>>() {
                    @Override
                    public ObservableSource<Plan> apply(Person person) {
                        if ("张三".equals(person.getName())) {
                            return Observable.fromIterable(person.getPlanList()).delay(100, TimeUnit.MILLISECONDS);
                        }
                        return Observable.fromIterable(person.getPlanList());
                    }
                })
                .subscribe(new Observer<Plan>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Plan plan) {
                        Log.d(TAG, "==================plan " + plan.getContent());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        // 为了更好的验证 flatMap 是无序的，使用了一个 delay() 方法来延迟，直接看打印结果：
        // 2020-04-17 17:43:36.418 13700-13700/com.quyunshuo.rxjava D/miyan: ==================plan 李四 吃早饭
        // 2020-04-17 17:43:36.418 13700-13700/com.quyunshuo.rxjava D/miyan: ==================plan 李四 吃午饭
        // 2020-04-17 17:43:36.418 13700-13700/com.quyunshuo.rxjava D/miyan: ==================plan 李四 吃晚饭
        // 2020-04-17 17:43:36.518 13700-13774/com.quyunshuo.rxjava D/miyan: ==================plan 张三 吃早饭
        // 2020-04-17 17:43:36.518 13700-13774/com.quyunshuo.rxjava D/miyan: ==================plan 张三 吃午饭
        // 2020-04-17 17:43:36.518 13700-13774/com.quyunshuo.rxjava D/miyan: ==================plan 张三 吃晚饭
        // 可以看到本来 张三 的事件发送顺序是排在 李四 事件之前，但是经过延迟后， 这两个事件序列发送顺序互换了。

        // 现在来验证下 concatMap() 是否是有序的，使用上面同样的代码，只是把 flatMap() 换成 concatMap()
        Observable.fromIterable(getTestData())
                .concatMap(new Function<Person, ObservableSource<Plan>>() {
                    @Override
                    public ObservableSource<Plan> apply(Person person) {
                        if ("张三".equals(person.getName())) {
                            return Observable.fromIterable(person.getPlanList()).delay(100, TimeUnit.MILLISECONDS);
                        }
                        return Observable.fromIterable(person.getPlanList());
                    }
                })
                .subscribe(new Observer<Plan>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Plan plan) {
                        Log.d(TAG, "==================plan " + plan.getContent());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        // 打印结果如下：
        // 2020-04-17 17:46:13.186 14494-14563/com.quyunshuo.rxjava D/miyan: ==================plan 张三 吃早饭
        // 2020-04-17 17:46:13.186 14494-14563/com.quyunshuo.rxjava D/miyan: ==================plan 张三 吃午饭
        // 2020-04-17 17:46:13.189 14494-14563/com.quyunshuo.rxjava D/miyan: ==================plan 张三 吃晚饭
        // 2020-04-17 17:46:13.190 14494-14563/com.quyunshuo.rxjava D/miyan: ==================plan 李四 吃早饭
        // 2020-04-17 17:46:13.190 14494-14563/com.quyunshuo.rxjava D/miyan: ==================plan 李四 吃午饭
        // 2020-04-17 17:46:13.190 14494-14563/com.quyunshuo.rxjava D/miyan: ==================plan 李四 吃晚饭
        // 这就代表 concatMap() 转换后发送的事件序列是有序的了。
    }

    /**
     * buffer()
     * 从需要发送的事件当中获取一定数量的事件，并将这些事件放到缓冲区当中一并发出。
     * public final Observable<List<T>> buffer(int count, int skip)
     * int count: 缓冲区元素的数量
     * int skip: 缓冲区满了之后，发送下一次事件序列的时候要跳过多少元素
     */
    public void onBuffer() {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                // 每次发送事件，指针都会往后移动3个元素再取值，直到指针移动到没有元素的时候就会停止取值。
                .buffer(3, 3)
                .subscribe(new Observer<List<Integer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Integer> integers) {
                        Log.d(TAG, "==========> 缓冲区大小: " + integers.size());
                        for (Integer i : integers) {
                            Log.d(TAG, "==========> 元素: " + i);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * groupBy()
     * 将发送的数据进行分组，每个分组都会返回一个被观察者。
     */
    public void onGroupBy() {
        Observable.just(5, 2, 3, 4, 1, 6, 8, 9, 7, 10)
                .groupBy(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer) throws Exception {
                        return integer % 3;
                    }
                })
                .subscribe(new Observer<GroupedObservable<Integer, Integer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "==========> onSubscribe ");
                    }

                    @Override
                    public void onNext(final GroupedObservable<Integer, Integer> integerIntegerGroupedObservable) {
                        Log.d(TAG, "==========> onNext ");
                        integerIntegerGroupedObservable.subscribe(new Observer<Integer>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.d(TAG, "==========> GroupedObservable onSubscribe ");
                            }

                            @Override
                            public void onNext(Integer integer) {
                                Log.d(TAG, "==========> GroupedObservable onNext  groupName: " + integerIntegerGroupedObservable.getKey() + " value: " + integer);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "==========> GroupedObservable onError ");
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "==========> GroupedObservable onComplete ");
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "==========> onError ");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "==========> onComplete ");
                    }
                });
    }

    /**
     * scan()
     * 将数据以一定的逻辑聚合起来。
     */
    public void onScan() {
        Disposable subscribe = Observable.just(1, 2, 3, 4)
                .scan(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        Log.d(TAG, "==========> apply ");
                        Log.d(TAG, "==========> integer " + integer);
                        Log.d(TAG, "==========> integer2 " + integer2);
                        return integer + integer2;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "==========> accept " + integer);
                    }
                });
    }

    /**
     * window()
     * 发送指定数量的事件时，就将这些事件分为一组。
     * public final Observable<Observable<T>> window(long count)
     * window 中的 count 的参数就是代表指定的数量
     * 例如将 count 指定为2，那么每发2个数据就会将这2个数据分成一组。
     */
    public void onWindow() {
        Observable.just(1, 2, 3, 4, 5)
                .window(2)
                .subscribe(new Observer<Observable<Integer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "==========> onSubscribe ");
                    }

                    @Override
                    public void onNext(Observable<Integer> integerObservable) {
                        integerObservable.subscribe(new Observer<Integer>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.d(TAG, "==========> integerObservable onSubscribe ");
                            }

                            @Override
                            public void onNext(Integer integer) {
                                Log.d(TAG, "==========> integerObservable onNext " + integer);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "==========> integerObservable onError ");
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "==========> integerObservable onComplete ");
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "==========> onError ");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "==========> onComplete ");
                    }
                });
    }
}
