package com.example.android.myguardian.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.loader.content.Loader;
import android.util.Log;

import com.example.android.myguardian.News;
import com.example.android.myguardian.presentation.news.NewsLoader;
import com.example.android.myguardian.NewsPreferences;
import com.example.android.myguardian.R;

import java.util.List;

/**
 * The EnvironmentFragment is a {@link BaseArticlesFragment} subclass that
 * reuses methods of the parent class by passing the specific type of article to be fetched.
 */
public class EnvironmentFragment extends BaseArticlesFragment {

    private static final String LOG_TAG = EnvironmentFragment.class.getName();

    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        String environmentUrl = NewsPreferences.getPreferredUrl(getContext(), getString(R.string.environment));
        Log.e(LOG_TAG, environmentUrl);

        // Create a new loader for the given URL
        return new NewsLoader(getActivity(), environmentUrl);
    }
}
