package com.quyunshuo.rxjava.net;

import android.util.Log;

import com.quyunshuo.rxjava.model.TranslationModel;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.quyunshuo.rxjava.Tags.TAG;

/**
 * @Author: QuYunShuo
 * @Time: 2020/4/20
 * @Class: RxJavaNetworkRequestPolling
 * @Remark: 网络请求轮询
 * URL模板
 * http://fy.iciba.com/ajax.php
 * URL示例
 * http://fy.iciba.com/ajax.php?a=fy&f=auto&t=auto&w=hello%20world
 * 参数说明：
 * a：固定值 fy
 * f：原文内容类型，日语取 ja，中文取 zh，英语取 en，韩语取 ko，德语取 de，西班牙语取 es，法语取 fr，自动则取 auto
 * t：译文内容类型，日语取 ja，中文取 zh，英语取 en，韩语取 ko，德语取 de，西班牙语取 es，法语取 fr，自动则取 auto
 * w：查询内容
 */
public class RxJavaNetworkRequestPolling {

    public static final String BASE_URL = "http://fy.iciba.com/";

    /**
     * 设置变量 = 模拟轮询服务器次数
     */
    private int i = 0;

    /**
     * 无条件网络请求轮询
     */
    public void unconditionalNetworkRequestPolling() {

        // 创建Retrofit对象
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                // //设置使用Gson解析
                .addConverterFactory(GsonConverterFactory.create())
                // 支持RxJava
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        // 创建 网络请求接口 的实例
        final Api request = retrofit.create(Api.class);

        // 采用interval（）延迟发送 此处主要展示无限次轮询
        Disposable subscribe = Observable.interval(2, 3, TimeUnit.SECONDS)
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "==========>> accept: 第 " + aLong + " 次轮询");
                        Observable<TranslationModel> translation = request.getTranslation();
                        translation
                                // 切换到IO线程进行网络请求
                                .subscribeOn(Schedulers.io())
                                // 切换回到主线程 处理请求结果
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<TranslationModel>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {
                                        Log.d(TAG, "==========>> onSubscribe: ");
                                    }

                                    @Override
                                    public void onNext(TranslationModel translationModel) {
                                        Log.d(TAG, "==========>> onNext: " + translationModel.toString());
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.d(TAG, "==========>> onError: " + e.getMessage());
                                    }

                                    @Override
                                    public void onComplete() {
                                        Log.d(TAG, "==========>> onComplete: ");
                                    }
                                });
                    }
                })
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "==========>> accept: 完成第 " + aLong + " 次轮询");
                    }
                });
    }

    /**
     * 有条件网络请求轮询
     */
    public void conditionalNetworkRequestPolling() {
        // 创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        // 创建 网络请求接口 的实例
        Api request = retrofit.create(Api.class);

        request.getTranslation()
                .repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<Object> objectObservable) throws Exception {
                        return objectObservable.flatMap(new Function<Object, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(Object o) throws Exception {
                                // 加入判断条件：当轮询次数 = 5次后，就停止轮询
                                if (i == 5) {
                                    // 此处选择发送onError事件以结束轮询，因为可触发下游观察者的onError（）方法回调
                                    return Observable.error(new Throwable("==========>> 轮询结束"));
                                }
                                // 若轮询次数＜4次，则发送1Next事件以继续轮询
                                // 此处加入了delay操作符，作用 = 延迟一段时间发送（此处设置 = 2s），以实现轮询间间隔设置
                                return Observable.just(1)
                                        .delay(2, TimeUnit.SECONDS);
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TranslationModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "==========>> onSubscribe: ");
                    }

                    @Override
                    public void onNext(TranslationModel translationModel) {
                        Log.d(TAG, "==========>> onNext: " + translationModel.toString());
                        i++;
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "==========>> onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "==========>> onComplete: ");
                    }
                });
    }
}
