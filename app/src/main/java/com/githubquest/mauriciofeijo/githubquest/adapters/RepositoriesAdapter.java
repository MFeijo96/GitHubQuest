package com.githubquest.mauriciofeijo.githubquest.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.githubquest.mauriciofeijo.githubquest.R;
import com.githubquest.mauriciofeijo.githubquest.models.Repository;
import com.githubquest.mauriciofeijo.githubquest.utils.Utils;

import java.util.List;

public class RepositoriesAdapter extends RecyclerView.Adapter<RepositoriesAdapter.RepositoryHolder> {
    private final List<Repository> mRepositories;
    private RepositorySelectedListener mOnRepositorySelectedListener;

    public RepositoriesAdapter(List<Repository> repositories) {
        mRepositories = repositories;
    }

    public void setOnRepositorySelectedListener(RepositorySelectedListener onRepositorySelectedListener) {
        mOnRepositorySelectedListener = onRepositorySelectedListener;
    }

    @NonNull
    @Override
    public RepositoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RepositoryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.repository_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RepositoryHolder holder, final int position) {
        final Repository repository = mRepositories.get(position);
        holder.updateView(repository);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnRepositorySelectedListener != null) mOnRepositorySelectedListener.onRepositorySelected(repository);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRepositories != null ? mRepositories.size() : 0;
    }

    public class RepositoryHolder extends RecyclerView.ViewHolder {
        private TextView mTitleTextView, mDescriptionTextView;

        private RepositoryHolder(View itemView) {
            super(itemView);
            mTitleTextView = itemView.findViewById(R.id.textView_title_repositoryView);
            mDescriptionTextView = itemView.findViewById(R.id.textView_description_repositoryView);
        }

        public void updateView(Repository repository) {
            mTitleTextView.setText(repository.getName());
            if (Utils.isEmpty(repository.getDescription())) {
                mDescriptionTextView.setVisibility(View.GONE);
            } else {
                mDescriptionTextView.setVisibility(View.VISIBLE);
                mDescriptionTextView.setText(repository.getDescription());
            }
        }
    }

    public interface RepositorySelectedListener {
        void onRepositorySelected(Repository repository);
    }
}