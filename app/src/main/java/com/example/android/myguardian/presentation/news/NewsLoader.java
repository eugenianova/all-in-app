package com.example.android.myguardian.presentation.news;

import androidx.loader.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.myguardian.News;
import com.example.android.myguardian.utils.QueryUtils;

import java.util.List;

/**
 * Загружает список новостей, ичпользуя AsyncTask, чтобы исполнить сетевой запрос к данной URL.
 */
public class NewsLoader extends AsyncTaskLoader<List<News>> {

    /** Tag for log messages */
    private static final String LOG_TAG = NewsLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link NewsLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        // Trigger the loadInBackground() method to execute.
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<News> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Исполняем сетевой запрос, парсим ответ, извлекаем список новостей.
        List<News> newsData = QueryUtils.fetchNewsData(mUrl);
        return newsData;
    }
}
