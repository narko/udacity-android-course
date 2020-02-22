package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

/**
 * Created by narko on 12/06/17.
 */
@RunWith(AndroidJUnit4.class)
public class EndpointsAsyncTaskTest {
    private CountDownLatch signal = null;
    String joke;

    @Before
    public void setUp() {
        signal = new CountDownLatch(1);
    }

    @After
    public void tearDown() {
        signal.countDown();
    }

    @Test
    public void testGetJokeFromServer() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getContext();
        EndpointsAsyncTask task = new EndpointsAsyncTask();
        task.setListener(new EndpointsAsyncTask.AsyncTaskListener() {
            @Override
            public void onComplete(String result) {
                joke = result;
                signal.countDown();
            }
        });
        task.execute(appContext);
        signal.await();
        Log.d(EndpointsAsyncTaskTest.class.getSimpleName(), joke);
        Assert.assertNotNull(joke);
    }

}
