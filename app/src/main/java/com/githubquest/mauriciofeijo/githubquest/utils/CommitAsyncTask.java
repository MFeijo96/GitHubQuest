package com.githubquest.mauriciofeijo.githubquest.utils;

import android.content.Context;

import com.githubquest.mauriciofeijo.githubquest.models.Commit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommitAsyncTask extends JsonTask {
    private CommitsRequestListener mJsonRequestListener;

    private CommitAsyncTask(Context context, CommitsRequestListener jsonRequestListener, ErrorListener errorListener) {
        super(context, errorListener);
        mJsonRequestListener = jsonRequestListener;
    }

    public static void newInstance(Context context, String user, String repo, CommitsRequestListener jsonRequestListener, ErrorListener errorListener) {
        new CommitAsyncTask(context, jsonRequestListener, errorListener).execute("https://api.github.com/repos/" + user + "/" + repo + "/commits");
    }

    @Override
    void onJsonResult(String json) {
        final InnerContent[] nativeContent = Utils.getGson().fromJson(json, InnerContent[].class);

        if (nativeContent != null && nativeContent.length > 0) {
            ArrayList<Commit> commits = new ArrayList<>(5);

            for (int i = 0; i < nativeContent.length && i < 5; i++) {
                InnerContent c = nativeContent[i];
                commits.add(new Commit(c.getCommit().getCommitter().getName(), c.getCommit().getMessage(), c.getHtml_url(), c.getCommit().getCommitter().getDate()));
            }

            if (mJsonRequestListener != null) mJsonRequestListener.onJsonResult(commits);
        }
    }

    public interface CommitsRequestListener {
        void onJsonResult(List<Commit> result);
    }

    //region Aux
    private class InnerContent {
        private String mHtml_url;
        private InnerCommit mCommit;

        public String getHtml_url() {
            return mHtml_url;
        }

        public void setHtml_url(String html_url) {
            mHtml_url = html_url;
        }

        public InnerCommit getCommit() {
            return mCommit;
        }

        public void setCommit(InnerCommit commit) {
            mCommit = commit;
        }
    }

    private class InnerCommit {
        private String mMessage;
        private InnerCommitter mCommitter;

        public String getMessage() {
            return mMessage;
        }

        public void setMessage(String message) {
            mMessage = message;
        }

        public InnerCommitter getCommitter() {
            return mCommitter;
        }

        public void setCommitter(InnerCommitter committer) {
            mCommitter = committer;
        }
    }

    private class InnerCommitter {
        private String mName;
        private Date mDate;

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }

        public Date getDate() {
            return mDate;
        }

        public void setDate(Date date) {
            mDate = date;
        }
    }
    //endregion
}

