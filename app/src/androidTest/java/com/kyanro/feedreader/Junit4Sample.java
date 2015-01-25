package com.kyanro.feedreader;


import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;


public class Junit4Sample {

    private Context c;

    @Before
    public void before() {
        c = InstrumentationRegistry.getTargetContext();
    }
    
    @Test
    public void myTest() {
        assertThat("test", equalTo("test"));
    }

    @Test
    public void myTest2() {
        assertThat(c.getString(R.string.hello_world) + ":this is failure", "test2", equalTo("test failure"));
    }

    @Test
    public void myTest3() {
        assertTrue("failure bool", false);
    }
}
