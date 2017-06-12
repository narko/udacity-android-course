package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.udacity.gradle.jokesdisplay.JokeActivity;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private final static String TAG = MainActivityFragment.class.getSimpleName();

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        Button button = (Button) root.findViewById(R.id.jokeButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayJoke();
            }
        });

        return root;
    }

    private void displayJoke() {
        final Context context = getContext();
        EndpointsAsyncTask task = new EndpointsAsyncTask();
        task.setListener(new EndpointsAsyncTask.AsyncTaskListener() {
            @Override
            public void onComplete(String result) {
                Intent intent = new Intent(context, JokeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(JokeActivity.JOKE_KEY, result);
                context.startActivity(intent);
            }
        });
        task.execute(context);
    }
}