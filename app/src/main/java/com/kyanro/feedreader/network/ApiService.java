package com.kyanro.feedreader.network;

import com.kyanro.feedreader.models.Feed;

import retrofit.http.GET;
import rx.Observable;

/**
 * Created by ppp on 2015/01/16.
 */
public class ApiService {
    public final static String STACKOVERFLOW_HOST = "http://stackoverflow.com/";

    public interface StackoverflowService{
        @GET("/feeds/tag?tagnames=rx-java&sort=newest")
        public Observable<Feed> newest();
    }
}
