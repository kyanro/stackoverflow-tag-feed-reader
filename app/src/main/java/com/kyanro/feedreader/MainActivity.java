package com.kyanro.feedreader;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.kyanro.feedreader.models.Feed.Entry;
import com.kyanro.feedreader.network.Stackoverflow;
import com.kyanro.feedreader.network.Stackoverflow.StackoverflowService;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.android.view.OnClickEvent;
import rx.android.view.ViewObservable;
import rx.functions.Func1;
import rx.functions.Func2;


public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.tag_etext)
    EditText mTagEditText;
    @InjectView((R.id.refresh_button))
    View mRefreshButton;
    @InjectView(R.id.feed_listview)
    ListView mFeedListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        final List<Entry> entries = new ArrayList<>();

        StackoverflowService service = Stackoverflow.getStackoverflowService();

        final feedAdapter feedAdapter = new feedAdapter(this, android.R.layout.simple_list_item_2, entries);
        mFeedListView.setAdapter(feedAdapter);


        Observable<String> tagTextChangedStream =
                Observable.<String>create(subscriber -> mTagEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        //Log.d("myrx", "s?:" + s);
                        if (s == null) {
                            subscriber.onNext(null);
                        } else {
                            subscriber.onNext(s.toString());
                        }
                    }
                }))
                        .startWith((String) null);

        Observable<OnClickEvent> refreshClickStream = ViewObservable.clicks(mRefreshButton);

        Observable<String> tagTextChangedCounted = tagTextChangedStream.publish().refCount();


        Observable<OnClickEvent> clickCounted = refreshClickStream.publish().refCount();

        refreshClickStream.join(tagTextChangedStream,
                new Func1<OnClickEvent, Observable<OnClickEvent>>() {
                    @Override
                    public Observable<OnClickEvent> call(OnClickEvent onClickEvent) {
                        Log.d("myrx", "left duration:");
                        return clickCounted;
                    }
                },
                new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        Log.d("myrx", "right duration:" + s);
                        return tagTextChangedCounted;
                    }
                },
                new Func2<OnClickEvent, String, String>() {
                    @Override
                    public String call(OnClickEvent onClickEvent, String s) {
                        Log.d("myrx", "result:" + s);

                        return s;
                    }
                })
                .subscribe(s -> Log.d("myrx", "success:" + s));


        //tagTextChangedStream,
        //new Func1<OnClickEvent, Observable<OnClickEvent>>() {
        //    @Override
        //    public Observable<OnClickEvent> call(OnClickEvent onClickEvent) {
        //        Log.d("myrx", "left selector:");
        //        return Observable.empty();
        //    }
        //},
        //new Func1<String, Observable<String>>() {
        //    @Override
        //    public Observable<String> call(String s) {
        //        Log.d("myrx", "right selector:" + s);
        //        return Observable.just(s);
        //    }
        //},
        //new Func2<OnClickEvent, String, String>() {
        //    @Override
        //    public String call(OnClickEvent onClickEvent, String s) {
        //        Log.d("myrx", "result:" + s);
        //        return s;
        //    }
        //})
        //.subscribe(s -> Log.d("myrx", "success:" + s));

        //tagTextChangedStream.join(refreshClickStream,
        //        new Func1<String, Observable<String>>() {
        //            @Override
        //            public Observable<String> call(String s) {
        //                Log.d("myrx", "left selector:" + s);
        //                return Observable.just(s);
        //            }
        //        },
        //        new Func1<OnClickEvent, Observable<OnClickEvent>>() {
        //            @Override
        //            public Observable<OnClickEvent> call(OnClickEvent onClickEvent) {
        //                Log.d("myrx", "right selector:");
        //                return Observable.just(onClickEvent);
        //            }
        //        },
        //        new Func2<String, OnClickEvent, String>() {
        //            @Override
        //            public String call(String s, OnClickEvent onClickEvent) {
        //                Log.d("myrx", "result stream:" + s);
        //                return s;
        //            }
        //        })
        //        //.zipWith(refreshClickStream, (s, onClickEvent) -> s)
        //        .subscribe(s -> Log.d("myrx", "success:" + s));
        //
        //Observable<Entry> entryStream =  refreshClickStream
        //        //.zipWith(tagTextChangedStream.last(), (onClickEvent, s) -> s)
        //        .flatMap(onClickEvent -> Observable.<String>just(null))
        //        .flatMap(service::getFeedsTag)
        //        .flatMap(feed -> Observable.from(feed.entries));
        //
        //entryStream
        //        .take(5)
        //        .observeOn(AndroidSchedulers.mainThread())
        //        .subscribe(entry -> {
        //            entries.add(entry);
        //            feedAdapter.notifyDataSetChanged();
        //        }, e -> Log.d("myrx", "error:" + e.getMessage()));
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
