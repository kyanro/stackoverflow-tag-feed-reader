package com.kyanro.feedreader;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
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

        // debug
        final List<Entry> entries = new ArrayList<>();
        //entries.add(new Entry() {{
        //    title = "test1";
        //    published = "post1";
        //}});
        //entries.add(new Entry() {{
        //    title = "test2";
        //    published = "post2";
        //}});
        //
        //mFeedListView.setAdapter(new feedAdapter(this, android.R.layout.simple_list_item_2, entries));

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ApiService.STACKOVERFLOW_HOST)
                .setConverter(new SimpleXMLConverter())
                .build();

        StackoverflowService service = restAdapter.create(StackoverflowService.class);

        //service.newest()
        Observable.just(new Feed())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(feed -> {
                    entries.addAll(feed.entries);
                    mFeedListView.setAdapter(new feedAdapter(this, android.R.layout.simple_list_item_2, entries));
                });
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
