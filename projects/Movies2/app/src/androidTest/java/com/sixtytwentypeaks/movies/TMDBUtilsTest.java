package com.sixtytwentypeaks.movies;

import android.support.test.runner.AndroidJUnit4;

import com.sixtytwentypeaks.movies.utils.TMDBUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by narko on 25/01/17.
 */
@RunWith(AndroidJUnit4.class)
public class TMDBUtilsTest {
    @Test
    public void popularMoviesUrl_isCorrect() {
        // TODO This code launches the following exception: java.lang.RuntimeException: Method parse in android.net.Uri not mocked.
        // See http://g.co/androidstudio/not-mocked for details.
        //System.out.println(NetworkUtils.getPopularMoviesUrl());
    }

    @Test
    public void topRatedMoviesUrl_isCorrect() {

    }

    @Test
    public void buildMovieTrailersUrl_isCorrect() {
        System.out.println(TMDBUtils.buildMovieTrailersUrl("1"));
    }
}
