package com.kyanro.feedreader;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kyanro.feedreader.models.Feed;
import com.kyanro.feedreader.models.Feed.Entry;
import com.kyanro.feedreader.network.ApiService;
import com.kyanro.feedreader.network.ApiService.StackoverflowService;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.android.AndroidLog;
import retrofit.converter.SimpleXMLConverter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;


public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.feed_listview)
    ListView mFeedListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        final List<Entry> entries = new ArrayList<>();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ApiService.STACKOVERFLOW_HOST)
                .setConverter(new SimpleXMLConverter())
                .setLog(new AndroidLog("myrx"))
                .setLogLevel(LogLevel.FULL)
                .build();

        StackoverflowService service = restAdapter.create(StackoverflowService.class);

        final feedAdapter feedAdapter = new feedAdapter(this, android.R.layout.simple_list_item_2, entries);
        mFeedListView.setAdapter(feedAdapter);

        service.newest()
                .flatMap(feed -> Observable.from(feed.entries))
                .take(5)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(entry -> {
                    entries.add(entry);
                    feedAdapter.notifyDataSetChanged();
                }, e -> Log.d("myrx", "error:" + e.getMessage()));
    }

    public static class feedAdapter extends ArrayAdapter<Entry> {

        LayoutInflater inflater;
        @LayoutRes
        int resource;

        public feedAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Entry> entries) {
            super(context, resource, entries);
            inflater = LayoutInflater.from(context);
            this.resource = resource;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            ViewHolder holder;
            if (view == null) {
                view = inflater.inflate(resource, parent, false);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            Entry entry = getItem(position);
            holder.titleText.setText(entry.title);
            holder.postTimeText.setText(entry.published);

            return view;
        }

        static class ViewHolder {
            @InjectView(android.R.id.text1)
            TextView titleText;
            @InjectView(android.R.id.text2)
            TextView postTimeText;

            public ViewHolder(View view) {
                ButterKnife.inject(this, view);
            }
        }
    }
}
