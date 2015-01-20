package com.kyanro.feedreader.network;

import android.content.Context;
import android.support.annotation.NonNull;

import com.kyanro.feedreader.models.Feed;

import retrofit.RestAdapter.Builder;
import retrofit.RestAdapter.LogLevel;
import retrofit.android.AndroidLog;
import retrofit.converter.SimpleXMLConverter;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

public class Stackoverflow {
    public final static String STACKOVERFLOW_HOST = "http://stackoverflow.com/";

    public static StackoverflowService stackoverflowService;

    private Stackoverflow() {
    }

    public static synchronized StackoverflowService getStackoverflowService(){
        if (stackoverflowService == null) {
            stackoverflowService = new Builder()
                    .setEndpoint(STACKOVERFLOW_HOST)
                    .setConverter(new SimpleXMLConverter())
                    .setLogLevel(LogLevel.FULL)
                    .setLog(new AndroidLog("myapi"))
                    .build()
                    .create(StackoverflowService.class);
        }
        return stackoverflowService;
    }

    public interface StackoverflowService{
        @POST("/feeds/tag?sort=newest")
        public Observable<Feed> getFeedsTag(@Query("tagnames") String tagnames);
    }


}
