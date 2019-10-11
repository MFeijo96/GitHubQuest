package com.githubquest.mauriciofeijo.githubquest.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.githubquest.mauriciofeijo.githubquest.R;
import com.githubquest.mauriciofeijo.githubquest.models.Commit;
import com.githubquest.mauriciofeijo.githubquest.utils.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CommitsAdapter extends RecyclerView.Adapter<CommitsAdapter.CommitHolder> {
    private final List<Commit> mCommits;
    private Context mContext;

    public CommitsAdapter(Context context, List<Commit> commits) {
        mCommits = commits;
        mContext = context;
    }

    @NonNull
    @Override
    public CommitHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommitHolder(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.commit_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommitHolder holder, final int position) {
        final Commit commit = mCommits.get(position);
        holder.updateView(commit);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(commit.getUrl()));
                mContext.startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCommits != null ? mCommits.size() : 0;
    }

    public class CommitHolder extends RecyclerView.ViewHolder {
        private TextView mAuthorTextView, mMessageTextView, mDateTextView;
        private Context mContext;

        private CommitHolder(Context context, View itemView) {
            super(itemView);

            mContext = context;

            mAuthorTextView = itemView.findViewById(R.id.textView_author_commitView);
            mMessageTextView = itemView.findViewById(R.id.textView_message_commitView);
            mDateTextView = itemView.findViewById(R.id.textView_date_commitView);
        }

        public void updateView(Commit commit) {
            mAuthorTextView.setText(commit.getAuthorName());
            mMessageTextView.setText(commit.getMessage());

            final Date date = commit.getDate();
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            final int hours = calendar.get(Calendar.HOUR_OF_DAY);
            final int minutes = calendar.get(Calendar.MINUTE);

            StringBuilder dateText = new StringBuilder(Utils.getDateFormat().format(date));
            dateText.append("\n");
            dateText.append(mContext.getString(R.string.at));
            dateText.append(" ");
            dateText.append(String.format("%02d", hours));
            dateText.append(":");
            dateText.append(String.format("%02d", minutes));

            mDateTextView.setText(dateText);
        }
    }
}
