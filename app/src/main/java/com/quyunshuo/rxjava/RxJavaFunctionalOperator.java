package com.quyunshuo.rxjava;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.quyunshuo.rxjava.Tags.TAG;

/**
 * @Author: QuYunShuo
 * @Time: 2020/4/18
 * @Class: RxJavaFunctionalOperator
 * @Remark: RxJava 功能操作符
 */
public class RxJavaFunctionalOperator {

    /**
     * delay()
     * 延迟一段事件发送事件。
     */
    public void onDelay() {
        Disposable subscribe = Observable.just(1, 2, 3)
                .delay(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "==========>> accept: " + integer);
                    }
                });
    }

    /**
     * doOnEach()
     * Observable 每发送一件事件之前都会先回调这个方法。
     */
    public void onDoOnEach() {
        Disposable subscribe = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        })
                .doOnEach(new Consumer<Notification<Integer>>() {
                    @Override
                    public void accept(Notification<Integer> integerNotification) throws Exception {
                        Log.d(TAG, "==========>> accept: " + integerNotification);
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "==========>> accept: " + integer);
                    }
                });
        // ==========>> accept: OnNextNotification[1]
        // ==========>> accept: 1
        // ==========>> accept: OnNextNotification[2]
        // ==========>> accept: 2
        // ==========>> accept: OnNextNotification[3]
        // ==========>> accept: 3
        // ==========>> accept: OnCompleteNotification
        // 从结果就可以看出每发送一个事件之前都会回调 doOnEach 方法，并且可以取出 onNext() 发送的值
    }

    /**
     * doOnNext()
     * Observable 每发送 onNext() 之前都会先回调这个方法。
     */
    public void onDoOnNext() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onComplete();
            }
        })
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "==========>> doOnNext " + integer);
                    }
                })
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
    }

    /**
     * doAfterNext()
     * Observable 每发送 onNext() 之后都会回调这个方法。
     */
    public void onDoAfterNext() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onComplete();
            }
        })
                .doAfterNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "==========>> doAfterNext " + integer);
                    }
                })
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
    }

    /**
     * doOnComplete()
     * Observable 每发送 onComplete() 之前都会回调这个方法
     */
    public void onDoOnComplete() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onComplete();
            }
        })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(TAG, "==========>> doOnComplete ");
                    }
                })
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
    }

    /**
     * doOnError()
     * Observable 每发送 onError() 之前都会回调这个方法
     */
    public void onDoOnError() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onError(new NullPointerException());
            }
        })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "==========>> doOnError " + throwable);
                    }
                })
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
    }

    /**
     * doOnSubscribe()
     * Observable 每发送 onSubscribe() 之前都会回调这个方法。
     */
    public void onDoOnSubscribe() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onComplete();
            }
        })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        Log.d(TAG, "==========>> doOnSubscribe ");
                    }
                })
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
    }

    /**
     * doOnDispose()
     * 当调用 Disposable 的 dispose() 之后回调该方法
     */
    public void onDoOnDispose() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onComplete();
            }
        })
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(TAG, "==========>> doOnDispose ");
                    }
                })
                .subscribe(new Observer<Integer>() {
                    private Disposable d;

                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "==========>> onSubscribe ");
                        this.d = d;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "==========>> onNext " + integer);
                        d.dispose();
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
        // ==========>> onNext 1
        // ==========>> doOnDispose
    }

    /**
     * doOnLifecycle()
     * 在回调 onSubscribe 之前回调该方法的第一个参数的回调方法，可以使用该回调方法决定是否取消订阅。
     */
    public void onDoOnLifecycle() {
        // doOnLifecycle() 第二个参数的回调方法的作用与 doOnDispose() 是一样的，现在用下面的例子来讲解：
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onComplete();
            }
        })
                .doOnLifecycle(
                        new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                Log.d(TAG, "==========>> doOnLifecycle accept");
                            }
                        },
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                Log.d(TAG, "==========>> doOnLifecycle Action");
                            }
                        })
                .doOnDispose(
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                Log.d(TAG, "==========>> doOnDispose Action");
                            }
                        })
                .subscribe(new Observer<Integer>() {
                    private Disposable d;

                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "==========>> onSubscribe ");
                        this.d = d;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "==========>> onNext " + integer);
                        d.dispose();
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
        // ==========>> doOnLifecycle accept
        // ==========>> onSubscribe
        // ==========>> onNext 1
        // ==========>> doOnDispose Action
        // ==========>> doOnLifecycle Action
        // 可以看到当在 onNext() 方法进行取消订阅操作后，doOnDispose() 和 doOnLifecycle() 都会被回调。

        // 如果使用 doOnLifecycle 进行取消订阅，来看看打印结果：
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onComplete();
            }
        })
                .doOnLifecycle(
                        new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                Log.d(TAG, "==========>> doOnLifecycle accept");
                                disposable.dispose();
                            }
                        },
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                Log.d(TAG, "==========>> doOnLifecycle Action");
                            }
                        })
                .doOnDispose(
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                Log.d(TAG, "==========>> doOnDispose Action");
                            }
                        })
                .subscribe(new Observer<Integer>() {
                    private Disposable d;

                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "==========>> onSubscribe ");
                        this.d = d;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "==========>> onNext " + integer);
                        d.dispose();
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
        // ==========>> doOnLifecycle accept
        // ==========>> onSubscribe
        // 可以发现 doOnDispose Action 和 doOnLifecycle Action 都没有被回调
    }

    /**
     * doOnTerminate() & doAfterTerminate()
     * doOnTerminate:    在 onError 或者 onComplete 发送之前回调
     * doAfterTerminate: 在 onError 或者 onComplete 发送之后回调
     */
    public void onDoOnTerminateAndDoAfterTerminate() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                // e.onError(new NullPointerException());
                e.onComplete();
            }
        })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(TAG, "==========>> doOnTerminate ");
                    }
                })
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(TAG, "==========>> doAfterTerminate ");
                    }
                })
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
    }

    /**
     * doFinally()
     * 在所有事件发送完毕之后回调该方法
     * 这里可能你会有个问题，那就是 doFinally() 和 doAfterTerminate() 到底有什么区别？
     * 区别就是在于取消订阅，如果取消订阅之后 doAfterTerminate() 就不会被回调，
     * 而 doFinally() 无论怎么样都会被回调，且都会在事件序列的最后。
     */
    public void onDoFinally() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onComplete();
            }
        })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(TAG, "==========>> doFinally ");
                    }
                })
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(TAG, "==========>> doOnDispose ");
                    }
                })
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(TAG, "==========>> doAfterTerminate ");
                    }
                })
                .subscribe(new Observer<Integer>() {
                    private Disposable d;

                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "==========>> onSubscribe ");
                        this.d = d;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "==========>> onNext " + integer);
//                        d.dispose();
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
        // ==========>> onNext 1
        // ==========>> doOnDispose
        // ==========>> doFinally
        // 可以看到如果调用了 dispose() 方法，doAfterTerminate() 不会被回调

        // 现在试试把 dispose() 注释掉看看，看看打印结果：
        // ==========>> onSubscribe
        // ==========>> onNext 1
        // ==========>> onNext 2
        // ==========>> onNext 3
        // ==========>> onComplete
        // ==========>> doAfterTerminate
        // ==========>> doFinally
        // doAfterTerminate() 已经成功回调，doFinally() 还是会在事件序列的最后
    }

    /**
     * onErrorReturn()
     * 当接受到一个 onError() 事件之后回调，返回的值会回调 onNext() 方法，并正常结束该事件序列
     */
    public void onOnErrorReturn() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onError(new NullPointerException());
            }
        })
                .onErrorReturn(new Function<Throwable, Integer>() {
                    @Override
                    public Integer apply(Throwable throwable) throws Exception {
                        Log.d(TAG, "==========>> onErrorReturn " + throwable);
                        return 404;
                    }
                })
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
        // ==========>> onNext 1
        // ==========>> onNext 2
        // ==========>> onNext 3
        // ==========>> onErrorReturn java.lang.NullPointerException
        // ==========>> onNext 404
        // ==========>> onComplete
    }

    /**
     * onErrorResumeNext()
     * 当接收到 onError() 事件时，返回一个新的 Observable，并正常结束事件序列
     */
    public void onOnErrorResumeNext() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onError(new NullPointerException());
            }
        })
                .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends Integer>>() {
                    @Override
                    public ObservableSource<? extends Integer> apply(Throwable throwable) throws Exception {
                        Log.d(TAG, "==========>> onErrorResumeNext " + throwable);
                        return Observable.just(4, 5, 6);
                    }
                })
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
        // ==========>> onNext 1
        // ==========>> onNext 2
        // ==========>> onNext 3
        // ==========>> onErrorResumeNext java.lang.NullPointerException
        // ==========>> onNext 4
        // ==========>> onNext 5
        // ==========>> onNext 6
        // ==========>> onComplete
    }

    /**
     * onExceptionResumeNext()
     * 与 onErrorResumeNext() 作用基本一致，但是这个方法只能捕捉 Exception。
     */
    public void onOnExceptionResumeNext() {
        // 先来试试 onExceptionResumeNext() 是否能捕捉 Error
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onError(new Error("404"));
            }
        })
                .onExceptionResumeNext(new Observable<Integer>() {
                    @Override
                    protected void subscribeActual(Observer<? super Integer> observer) {
                        observer.onNext(333);
                        observer.onComplete();
                    }
                })
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
        // ==========>> onNext 1
        // ==========>> onNext 2
        // ==========>> onNext 3
        // ==========>> onError
        // 从打印结果可以知道，观察者收到 onError() 事件，证明 onErrorResumeNext() 不能捕捉 Error 事件
        // 将被观察者的 e.onError(new Error("404")) 改为 e.onError(new Exception("404"))，现在看看是否能捕捉 Exception 事件:
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onError(new Exception("404"));
            }
        })
                .onExceptionResumeNext(new Observable<Integer>() {
                    @Override
                    protected void subscribeActual(Observer<? super Integer> observer) {
                        observer.onNext(333);
                        observer.onComplete();
                    }
                })
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
        // ==========>> onNext 1
        // ==========>> onNext 2
        // ==========>> onNext 3
        // ==========>> onNext 333
        // 从打印结果可以知道，这个方法成功捕获 Exception 事件
    }

    /**
     * retry()
     * 如果出现错误事件，则会重新发送所有事件序列。times 是代表重新发的次数。
     */
    public void onRetry() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onError(new Exception("404"));
            }
        })
                .retry(2)
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
        // ==========>> onNext 1
        // ==========>> onNext 2
        // ==========>> onNext 3
        // ==========>> onNext 1
        // ==========>> onNext 2
        // ==========>> onNext 3
        // ==========>> onNext 1
        // ==========>> onNext 2
        // ==========>> onNext 3
    }

    /**
     * retryUntil()
     * 出现错误事件之后，可以通过此方法判断是否继续发送事件。
     */
    public void onRetryUntil() {
        final int i = 6;
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onError(new Exception("404"));
            }
        })
                .retryUntil(new BooleanSupplier() {
                    @Override
                    public boolean getAsBoolean() throws Exception {
                        if (i == 6) {
                            return true;
                        }
                        return false;
                    }
                })
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
    }

    /**
     * retryWhen()
     * 当被观察者接收到异常或者错误事件时会回调该方法，这个方法会返回一个新的被观察者。
     * 如果返回的被观察者发送 Error 事件则之前的被观察者不会继续发送事件，如果发送正常事件则之前的被观察者会继续不断重试发送事件。
     */
    public void onRetryWhen() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("chan");
                e.onNext("ze");
                e.onNext("de");
                e.onError(new Exception("303"));
                e.onNext("haha");
            }
        })
                .retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                        return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(Throwable throwable) throws Exception {
                                if (!throwable.toString().equals("java.lang.Exception: 404")) {
                                    return Observable.just("可以忽略的异常");
                                } else {
                                    return Observable.error(new Throwable("终止啦"));
                                }
                            }
                        });
                    }
                })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "==========>> onSubscribe ");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "==========>> onNext " + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "==========>> onError " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "==========>> onComplete ");
                    }
                });
        // ==========>> onSubscribe
        // ==========>> onNext chan
        // ==========>> onNext ze
        // ==========>> onNext de
        // ==========>> onError java.lang.Throwable: 终止啦
        // 将 onError(new Exception("404")) 改为 onError(new Exception("303")) 看看打印结果：
        // ==========>> onNext ze
        // ==========>> onNext de
        // ==========>> onNext chan
        // ==========>> onNext ze
        // ==========>> onNext de
        // ==========>> onNext chan
        // ==========>> onNext ze
        // ==========>> onNext de
        // ==========>> onNext chan
        // ==========>> onNext ze
        // ==========>> onNext de
        // ==========>> onNext chan
        // ==========>> onNext ze
        // ==========>> onNext de
        // ==========>> onNext chan
        // 从结果可以看出，会不断重复发送消息。
    }

    /**
     * repeat()
     * 重复发送被观察者的事件，times 为发送次数。
     */
    public void onRepeat() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onComplete();
            }
        })
                .repeat(2)
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
                        Log.d(TAG, "==========>> onError: ");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "==========>> onComplete ");
                    }
                });
    }

    /**
     * repeatWhen()
     * 这个方法可以会返回一个新的被观察者设定一定逻辑来决定是否重复发送事件
     * 这里分三种情况，如果新的被观察者返回 onComplete 或者 onError 事件，则旧的被观察者不会继续发送事件。
     * 如果被观察者返回其他事件，则会重复发送事件。
     */
    public void onRepeatWhen() {
        // 现在试验发送 onComplete 事件
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onComplete();
            }
        })
                .repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<Object> objectObservable) throws Exception {
                        return Observable.empty();
                        // return Observable.error(new Exception("404"));
                        // return Observable.just(4);
                    }
                })
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
        // ==========>> onComplete

        // 下面直接看看发送 onError 事件和其他事件的打印结果:
        // ==========>> onSubscribe
        // ==========>> onError

        // 发送其他事件的打印结果:
        // ==========>> onSubscribe
        // ==========>> onNext 1
        // ==========>> onNext 2
        // ==========>> onNext 3
        // ==========>> onComplete
    }

    /**
     * subscribeOn()
     * 指定被观察者的线程，要注意的是，如果多次调用此方法，只有第一次有效。
     */
    public void onSubscribeOn() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                Log.d(TAG, "==========>> currentThread name: " + Thread.currentThread().getName());
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<Integer>() {
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
        // ==========>> onSubscribe
        // ==========>> currentThread name: RxNewThreadScheduler-1
        // ==========>> onNext 1
        // ==========>> onNext 2
        // ==========>> onNext 3
        // ==========>> onComplete
    }

    /**
     * observeOn()
     * 指定观察者的线程，每指定一次就会生效一次。
     */
    public void onObserveOn() {
        Observable.just(1, 2, 3)
                .observeOn(Schedulers.newThread())
                .flatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Integer integer) throws Exception {
                        Log.d(TAG, "==========>> flatMap Thread name " + Thread.currentThread().getName());
                        return Observable.just("chan" + integer);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "==========>> onSubscribe");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "==========>> onNext Thread name " + Thread.currentThread().getName());
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
        // ==========>> onSubscribe
        // ==========>> flatMap Thread name RxNewThreadScheduler-1
        // ==========>> onNext Thread name main
        // ==========>> onNext chan1
        // ==========>> onNext Thread name main
        // ==========>> onNext chan2
        // ==========>> onNext Thread name main
        // ==========>> onNext chan3
        // ==========>> onComplete
        // 从打印结果可以知道，observeOn 成功切换了线程。

        // RxJava 中的调度器：
        // Schedulers.computation( )    用于使用计算任务，如事件循环和回调处理
        // Schedulers.immediate( )      当前线程
        // Schedulers.io( )             用于 IO 密集型任务，如果异步阻塞 IO 操作。
        // Schedulers.newThread( )      创建一个新的线程
        // AndroidSchedulers.mainThread()   Android 的 UI 线程，用于操作 UI。
    }
}
