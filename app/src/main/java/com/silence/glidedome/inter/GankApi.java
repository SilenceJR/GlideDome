package com.silence.glidedome.inter;

import com.silence.glidedome.model.ReqMsg;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Silence
 *
 * @time 2017/9/23 3:02
 * @des ${TODO}
 */

public interface GankApi {

    @GET("福利/10/{page}")
    Observable<ReqMsg> getGank(@Path("page") int page);
}
