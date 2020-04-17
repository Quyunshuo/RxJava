package com.quyunshuo.rxjava;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

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
}
