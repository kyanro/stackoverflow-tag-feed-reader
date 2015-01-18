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
import android.widget.Toast;

import com.kyanro.feedreader.models.Feed.Entry;
import com.kyanro.feedreader.network.Stackoverflow;
import com.kyanro.feedreader.network.Stackoverflow.StackoverflowService;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.view.OnClickEvent;
import rx.android.view.ViewObservable;


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

        // 必要な TextChangeEvent を observable 化
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
                        if (s == null) {
                            subscriber.onNext(null);
                        } else {
                            subscriber.onNext(s.toString());
                        }
                    }
                }));

        Observable<OnClickEvent> refreshClickStream = ViewObservable.clicks(mRefreshButton);

        Observable<Entry> entryUpdateStream = refreshClickStream
                .doOnNext(e -> Log.d("myrx", "refresh clicked"))
                .join(  // 更新クリック時に最新のtagChangedStream の値を後ろへ流すよう合成
                        tagTextChangedStream,
                        onClickEvent -> Observable.empty(),
                        s -> tagTextChangedStream.publish().refCount(),
                        (onClickEvent, s) -> s)
                .doOnNext(e -> Log.d("myrx", "joined"))
                .distinct()
                .flatMap(service::getFeedsTag)
                .doOnNext(e -> Log.d("myrx", "got feed"))
                .flatMap(feed -> Observable.from(feed.entries).take(5));

        entryUpdateStream
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(entry -> {
                    entries.add(entry);
                    feedAdapter.notifyDataSetChanged();
                }, e -> Toast.makeText(this, "error:" + e.getMessage(), Toast.LENGTH_LONG).show()
                        , () -> Log.d("myrx", "complete!"));
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
