package com.silence.glidedome;

import android.util.Log;

import com.silence.glidedome.inter.GankApi;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Silence
 *
 * @time 2017/9/23 3:05
 * @des ${TODO}
 */

public class RetrofitFactory {

    private static final String BASE_URL = "http://gank.io/api/data/";

    private static final long TIMEOUT = 30;

    // Retrofit是基于OkHttpClient的，可以创建一个OkHttpClient进行一些配置
//    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
    //包含header、body数据



    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            // 添加通用的Header
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request.Builder builder = chain.request().newBuilder();
//                    builder.addHeader("token", "123");
                    return chain.proceed(builder.build());
                }
            })
            /*
            这里可以添加一个HttpLoggingInterceptor，因为Retrofit封装好了从Http请求到解析，
            出了bug很难找出来问题，添加HttpLoggingInterceptor拦截器方便调试接口
             */
//            .addInterceptor(loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.d("OkHttp3  :   ", message);

                }
            }).setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build();

    private static GankApi retrofitService = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            // 添加Gson转换器
            .addConverterFactory(GsonConverterFactory.create())
            // 添加Retrofit到RxJava的转换器
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient)
            .build()
            .create(GankApi.class);

    public static GankApi getInstance() {
        return retrofitService;
    }

//    private static Gson buildGson() {
    //        return new GsonBuilder()
    //                .serializeNulls()
    //                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
    //                // 此处可以添加Gson 自定义TypeAdapter
    //                .registerTypeAdapter(ReqMsg.class, new UserInfoTypeAdapter())
    //                .create();
    //    }

}
