package com.quyunshuo.rxjava.net;

import com.quyunshuo.rxjava.model.TranslationModel;
import com.quyunshuo.rxjava.model.TranslationModel2;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * @Author: QuYunShuo
 * @Time: 2020/4/20
 * @Class: Api
 * @Remark: Retrofit 请求Api
 */
public interface Api {

    /**
     * 请求翻译
     * 采用Observable<...>接口
     *
     * @return 返回一个被观察者
     */
    @GET("ajax.php?a=fy&f=auto&t=auto&w=hi%20world")
    Observable<TranslationModel> getTranslation();

    /**
     * 模拟两个请求
     *
     * @return 返回一个被观察者
     */
    @GET("ajax.php?a=fy&f=auto&t=auto&w=hi%20world")
    Observable<TranslationModel2> getTranslation2();
}
