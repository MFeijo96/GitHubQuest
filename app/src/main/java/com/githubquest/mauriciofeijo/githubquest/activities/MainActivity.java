package com.githubquest.mauriciofeijo.githubquest.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.githubquest.mauriciofeijo.githubquest.fragments.RepositoriesListFragment;

public class MainActivity extends AppCompatActivity {
    private static final String MAIN_FRAGMENT_TAG =  "MainActivity_MainTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        RepositoriesListFragment scannerFragment = (RepositoriesListFragment) fragmentManager.findFragmentByTag(MAIN_FRAGMENT_TAG);
        if (scannerFragment == null) {
            scannerFragment = RepositoriesListFragment.newInstance();
            fragmentManager.beginTransaction()
                    .replace(android.R.id.content, scannerFragment, MAIN_FRAGMENT_TAG)
                    .commitNow();
        }
    }


}
