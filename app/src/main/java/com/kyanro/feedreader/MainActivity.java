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

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {

    List<Feed> mFeeds = new ArrayList<>();

    @InjectView(R.id.feed_listview)
    ListView mFeedListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        // debug
        mFeeds.add(new Feed(){{ title="test1"; post_time="post1"; }});
        mFeeds.add(new Feed(){{ title="test2"; post_time="post2"; }});

        mFeedListView.setAdapter(new feedAdapter(this, android.R.layout.simple_list_item_2, mFeeds));
    }

    public static class feedAdapter extends ArrayAdapter<Feed> {

        LayoutInflater inflater;
        @LayoutRes
        int resource;
        public feedAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Feed> feeds) {
            super(context, resource, feeds);
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

            Feed feed = getItem(position);
            holder.titleText.setText(feed.title);
            holder.postTimeText.setText(feed.post_time);

            return view;
        }

        static class ViewHolder{
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
