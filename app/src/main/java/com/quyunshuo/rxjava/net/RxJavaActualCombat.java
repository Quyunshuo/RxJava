package com.quyunshuo.rxjava.net;

import android.util.Log;

import com.quyunshuo.rxjava.model.TranslationModel;
import com.quyunshuo.rxjava.model.TranslationModel2;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
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
 * @Class: rxJavaActualCombat
 * @Remark: 实战
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
public class RxJavaActualCombat {

    private static final String BASE_URL = "http://fy.iciba.com/";

    /**
     * 设置变量 = 模拟轮询服务器次数
     */
    private int i = 0;

    /**
     * 用于存放最终展示的数据
     */
    String result = "数据源来自 = ";

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

    /**
     * 网络请求嵌套回调
     * 背景: 需要进行嵌套网络请求：即在第1个网络请求成功后，继续再进行一次网络请求
     * 如 先进行 用户注册 的网络请求, 待注册成功后回再继续发送 用户登录 的网络请求
     * 结合 RxJava2中的变换操作符FlatMap（）实现嵌套网络请求
     * 实现功能：发送嵌套网络请求（将英文翻译成中文，翻译两次）
     * 为了让大家都能完成Demo，所以通过 公共的金山词霸API 来模拟 “注册 - 登录”嵌套网络请求
     * 即先翻译 Register（注册），再翻译 Login（登录）
     */
    public void networkRequestNestedCallback() {
        // 创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        // 创建 网络请求接口 的实例
        Api request = retrofit.create(Api.class);

        Observable<TranslationModel> translationModelObservable1 = request.getTranslation();
        final Observable<TranslationModel2> translationModelObservable2 = request.getTranslation2();
        // 首先进行请求1(第一次的翻译)
        Disposable subscribe = translationModelObservable1
                // （初始被观察者）切换到IO线程进行网络请求1
                .subscribeOn(Schedulers.io())
                // （新观察者）切换到主线程 处理网络请求1的结果
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<TranslationModel>() {
                    @Override
                    public void accept(TranslationModel translationModel) throws Exception {
                        // 对第1次网络请求返回的结果进行操作 = 显示翻译结果
                        Log.d(TAG, "==========>> accept: 第一次请求成功" + translationModel.toString());
                    }
                })
                // （新被观察者，同时也是新观察者）切换到IO线程去发起登录请求
                .observeOn(Schedulers.io())
                // 特别注意：因为flatMap是对初始被观察者作变换，所以对于旧被观察者，它是新观察者，所以通过observeOn切换线程
                // 但对于初始观察者，它则是新的被观察者
                .flatMap(new Function<TranslationModel, ObservableSource<TranslationModel2>>() {
                    @Override
                    public ObservableSource<TranslationModel2> apply(TranslationModel translationModel) throws Exception {
                        // 将网络请求1转换成网络请求2，即发送网络请求2
                        return translationModelObservable2;
                    }
                })
                // （初始观察者）切换到主线程 处理网络请求2的结果
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<TranslationModel2>() {
                    @Override
                    public void accept(TranslationModel2 translationModel2) throws Exception {
                        // 对第2次网络请求返回的结果进行操作 = 显示翻译结果
                        Log.d(TAG, "==========>> accept: 第二次请求成功" + translationModel2.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "==========>> accept: 网络错误");
                    }
                });
    }

    /**
     * 合并数据源 & 同时展示
     * 同时向2个数据源获取数据 -> 合并数据 -> 统一展示到客户端
     * Merge（）例子: 实现较为简单的从（网络 + 本地）获取数据 & 统一展示
     * Zip（）例子:   结合Retrofit 与RxJava，实现较为复杂的合并2个网络请求向2个服务器获取数据 & 统一展示
     */
    public void mergingDataSources() {
        /*
         * 设置第1个Observable：通过网络获取数据
         * 此处仅作网络请求的模拟
         **/
        Observable<String> network = Observable.just("网络");

        /*
         * 设置第2个Observable：通过本地文件获取数据
         * 此处仅作本地文件请求的模拟
         **/
        Observable<String> file = Observable.just("本地文件");

        /*
         * 通过merge（）合并事件 & 同时发送事件
         **/
        Observable.merge(network, file)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "==========>> onSubscribe: ");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "==========>> onNext: " + s);
                        result = result + "+" + s;
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "==========>> onError: ");
                    }

                    // 接收合并事件后，统一展示
                    @Override
                    public void onComplete() {
                        Log.d(TAG, "==========>> onComplete: " + result);
                    }
                });


        // 使用zip()
        // 从不同数据源（2个服务器）获取数据，即 合并网络请求的发送
        // 统一显示结果

        // 创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        // 创建 网络请求接口 的实例
        Api request = retrofit.create(Api.class);

        // 采用Observable<...>形式 对 2个网络请求 进行封装
        Observable<TranslationModel> translationModelObservable1 = request.getTranslation().subscribeOn(Schedulers.io());
        Observable<TranslationModel2> translationModel2Observable2 = request.getTranslation2().subscribeOn(Schedulers.io());

        // 注：创建BiFunction对象传入的第3个参数 = 合并后数据的数据类型
        Disposable subscribe = Observable.zip(
                translationModelObservable1,
                translationModel2Observable2,
                new BiFunction<TranslationModel, TranslationModel2, String>() {
                    @Override
                    public String apply(TranslationModel translationModel, TranslationModel2 translationModel2) throws Exception {
                        return translationModel.toString() + " & " + translationModel2.toString();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "==========>> accept: " + s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "accept: " + throwable.getMessage());
                    }
                });
    }
}
