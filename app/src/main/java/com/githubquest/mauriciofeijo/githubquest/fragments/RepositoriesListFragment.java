package com.githubquest.mauriciofeijo.githubquest.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.githubquest.mauriciofeijo.githubquest.R;
import com.githubquest.mauriciofeijo.githubquest.activities.ScannerActivity;
import com.githubquest.mauriciofeijo.githubquest.adapters.RepositoriesAdapter;
import com.githubquest.mauriciofeijo.githubquest.models.Commit;
import com.githubquest.mauriciofeijo.githubquest.models.Repository;
import com.githubquest.mauriciofeijo.githubquest.utils.CommitAsyncTask;
import com.githubquest.mauriciofeijo.githubquest.utils.JsonTask;
import com.githubquest.mauriciofeijo.githubquest.utils.RepositoryAsyncTask;
import com.githubquest.mauriciofeijo.githubquest.utils.Utils;
import com.githubquest.mauriciofeijo.githubquest.views.CommitsDialog;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.githubquest.mauriciofeijo.githubquest.activities.ScannerActivity.SCANNED_TEXT_KEY;

public class RepositoriesListFragment extends Fragment {
    private static final int SCANNER_REQUEST_CODE = Utils.getNextRequestCode();
    private static final String CURRENT_USER_KEY = "RepositoriesList_CurrentUserKey";
    private static final String SELECTED_REPOSITORY_KEY = "RepositoriesList_SelectedRepositoryKey";
    private String mCurrentUser;
    private RecyclerView mRecyclerView;
    private View mEmptyView;
    private CommitsDialog mCommitDialog;
    private Repository mSelectedRepository;

    public static RepositoriesListFragment newInstance() {
        return new RepositoriesListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.repositories_list_fragment, container, false);

        mEmptyView = view.findViewById(R.id.textView_emptyList_repositories);

        mRecyclerView = view.findViewById(R.id.recyclerView_repositories);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        updateList(null);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(CURRENT_USER_KEY)) {
            mCurrentUser = savedInstanceState.getString(CURRENT_USER_KEY);

            if (savedInstanceState.containsKey(SELECTED_REPOSITORY_KEY)) {
                mSelectedRepository = savedInstanceState.getParcelable(SELECTED_REPOSITORY_KEY);

                loadDialog(mSelectedRepository);
            }
        }

        if (!Utils.isEmpty(mCurrentUser)) {
            loadRepositories();
        } else {
            updateList(null);
        }
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(CURRENT_USER_KEY, mCurrentUser);

        if (mCommitDialog != null) {
            mCommitDialog.dismiss();
            outState.putParcelable(SELECTED_REPOSITORY_KEY, mSelectedRepository);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == SCANNER_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null && data.hasExtra(SCANNED_TEXT_KEY)) {
                mCurrentUser = data.getStringExtra(SCANNED_TEXT_KEY);
                loadRepositories();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    //region Menu

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuItem_openScanner) {
            startActivityForResult(new Intent(getActivity(), ScannerActivity.class), SCANNER_REQUEST_CODE);
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion

    private void loadRepositories() {
        RepositoryAsyncTask.newInstance(getActivity(), mCurrentUser, new RepositoryAsyncTask.RepositoryRequestListener() {
            @Override
            public void onJsonResult(List<Repository> repositories) {
                if (getActivity() != null && mRecyclerView != null) {
                    updateList(repositories);
                }
            }
        }, getErrorListener());
    }

    private void updateList(final List<Repository> repositories) {
        Utils.runOnUiThread(getActivity(), new Runnable() {
            @Override
            public void run() {
                if (Utils.isEmpty(repositories)) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                } else {
                    mEmptyView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    Utils.runOnUiThread(getActivity(), new Runnable() {
                        @Override
                        public void run() {
                            final RepositoriesAdapter adapter = new RepositoriesAdapter(repositories);
                            mRecyclerView.setAdapter(adapter);
                            adapter.setOnRepositorySelectedListener(new RepositoriesAdapter.RepositorySelectedListener() {
                                @Override
                                public void onRepositorySelected(final Repository repository) {
                                    mSelectedRepository = repository;
                                    loadDialog(repository);
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    private void loadDialog(final Repository repository) {
        CommitAsyncTask.newInstance(getActivity(), mCurrentUser, repository.getName(), new CommitAsyncTask.CommitsRequestListener() {
            @Override
            public void onJsonResult(final List<Commit> commits) {
                Utils.runOnUiThread(getActivity(), new Runnable() {
                    @Override
                    public void run() {
                        mCommitDialog = CommitsDialog.newInstance(getActivity(), commits, repository, new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                mCommitDialog = null;
                                mSelectedRepository = null;
                            }
                        });
                    }
                });
            }
        }, getErrorListener());
    }

    private JsonTask.ErrorListener getErrorListener() {
        return new JsonTask.ErrorListener() {
            @Override
            public void onErrorListener(final String error) {
                if (getActivity() != null) {
                    Utils.runOnUiThread(getActivity(), new Runnable() {
                        @Override
                        public void run() {
                            if (!Utils.isEmpty(error)) {
                                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        };
    }
}
