package com.githubquest.mauriciofeijo.githubquest.utils;

import android.content.Context;

import com.githubquest.mauriciofeijo.githubquest.models.Repository;

import java.util.Arrays;
import java.util.List;

public class RepositoryAsyncTask extends JsonTask {
    private RepositoryRequestListener mRepositoryRequestListener;

    private RepositoryAsyncTask(Context context, RepositoryRequestListener repositoryRequestListener, ErrorListener errorListener) {
        super(context, errorListener);
        mRepositoryRequestListener = repositoryRequestListener;
    }

    public static void newInstance(Context context, String user, RepositoryRequestListener repositoryRequestListener, ErrorListener errorListener) {
        new RepositoryAsyncTask(context, repositoryRequestListener, errorListener).execute("https://api.github.com/users/" + user + "/repos");
    }

    @Override
    void onJsonResult(String json) {
        final List<Repository> repositories = Arrays.asList(Utils.getGson().fromJson(json, Repository[].class));

        if (mRepositoryRequestListener != null) mRepositoryRequestListener.onJsonResult(repositories);
    }

    public interface RepositoryRequestListener {
        void onJsonResult(List<Repository> result);
    }
}
