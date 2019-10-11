package com.githubquest.mauriciofeijo.githubquest.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.widget.TextView;

import com.githubquest.mauriciofeijo.githubquest.R;
import com.githubquest.mauriciofeijo.githubquest.adapters.CommitsAdapter;
import com.githubquest.mauriciofeijo.githubquest.models.Commit;
import com.githubquest.mauriciofeijo.githubquest.models.Repository;

import java.util.List;

public class CommitsDialog {
    private Dialog mDialog;

    public static CommitsDialog newInstance(Context context, List<Commit> commits, Repository repository, DialogInterface.OnDismissListener onDismissListener){
        return new CommitsDialog(context, commits, repository, onDismissListener);
    }

    public CommitsDialog(Context context, List<Commit> commits, Repository repository, DialogInterface.OnDismissListener onDismissListener) {
        final Dialog dialog = new Dialog(context);
        dialog.setOnDismissListener(onDismissListener);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.commits_list_dialog);

        TextView text = dialog.findViewById(R.id.textView_title_commitsView);
        text.setText(repository.getName());

        final RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView_commits);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new CommitsAdapter(context, commits));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        dialog.show();

        mDialog = dialog;
    }

    public void dismiss() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
