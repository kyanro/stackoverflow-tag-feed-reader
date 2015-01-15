package com.kyanro.feedreader.models;

import android.support.annotation.NonNull;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by ppp on 2015/01/16.
 */
@Root(strict = false)
public class Feed {
    @ElementList(entry = "entry", inline = true)
    public List<Entry> entries;

    @Root(strict = false)
    public static class Entry{
        @Element
        public String id;

        @Element
        public String title = "test";

        @Element
        public String published;
    }
}
