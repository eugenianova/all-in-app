package com.example.android.myguardian.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.myguardian.presentation.news.EmptyRecyclerView;
import com.example.android.myguardian.News;
import com.example.android.myguardian.presentation.news.NewsLoader;
import com.example.android.myguardian.NewsPreferences;
import com.example.android.myguardian.R;
import com.example.android.myguardian.adapter.NewsAdapter;
import com.example.android.myguardian.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 BaseArticlesFragment — подкласс {@link Fragment}, кот. реализует интерфейс LoaderManager.LoaderCallbacks,
 чтобы Fragment был клиентом, взаимодействующим с LoaderManager.
 Это базовый класс, который отвечает за отображение набора статей независимо от их типа.
 */
public class BaseArticlesFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<News>>{

    private static final String LOG_TAG = BaseArticlesFragment.class.getName();

    /** Константная переменная для news loader ID. */
    private static final int NEWS_LOADER_ID = 1;

    /** Адаптер для списка новостей*/
    private NewsAdapter mAdapter;

    /** TextView который отображается когда recycler view пустой */
    private TextView mEmptyStateTextView;

    /* Индикатор загрузки, который отображается до того как первая загрузка завершена*/
    private View mLoadingIndicator;

    /** The {@link SwipeRefreshLayout} that detects swipe gestures and triggers callbacks in the app.*/
    private SwipeRefreshLayout mSwipeRefreshLayout;
    // Компонент SwipeRefreshLayout позволяет отказаться от сторонних библиотек для реализации
    // шаблона "Pull to Refresh", когда пользователь сдвигает экран, чтобы обновить данные.

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Найти обращение к {@link RecyclerView} в layout
        // Replaced RecyclerView with EmptyRecyclerView
        EmptyRecyclerView mRecyclerView = rootView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);

        // Задать layoutManager в {@link RecyclerView}
        mRecyclerView.setLayoutManager(layoutManager);

        // Найти SwipeRefreshLayout
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh);
        // Задать цветовую схему для SwipeRefreshLayout
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.swipe_color_1),
                getResources().getColor(R.color.swipe_color_2),
                getResources().getColor(R.color.swipe_color_3),
                getResources().getColor(R.color.swipe_color_4));

        // Set up OnRefreshListener that is invoked when the user performs a swipe-to-refresh gesture.
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");
                // restart the loader
                initiateRefresh();
                Toast.makeText(getActivity(), getString(R.string.updated_just_now),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Находим в лэйауте loading indicator
        mLoadingIndicator = rootView.findViewById(R.id.loading_indicator);

        // Находим пустой view в лейауте и устанавливаем его на новый recycler view
        mEmptyStateTextView = rootView.findViewById(R.id.empty_view);
        mRecyclerView.setEmptyView(mEmptyStateTextView);

        // Создаём новый адаптрер который берёт пустой список новостей как входные данные
        mAdapter = new NewsAdapter(getActivity(), new ArrayList<News>());

        // Устанавливаем адаптер на {@link recyclerView}
        mRecyclerView.setAdapter(mAdapter);

        // Проверяем интернет соединение и инициализируем loader
        initializeLoader(isConnected());

        return rootView;
    }

    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        Uri.Builder uriBuilder = NewsPreferences.getPreferredUri(getContext());

        Log.e(LOG_TAG,uriBuilder.toString());

        // Создаём новый loader для данного URL
        return new NewsLoader(getActivity(), uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> newsData) {
        // Скрыть индикатор загрузки, так как данные загрузились
        mLoadingIndicator.setVisibility(View.GONE);

        // Устанавливаем пустое состояние текста лоя отображения "No news found."
        mEmptyStateTextView.setText(R.string.no_news);

        // Очистить адаптер от предыдущих данных
        mAdapter.clearAll();

        //Если есть допустимый список новостей {@link}, добавьте их в набор данных адаптера.
        // Это вызовет обновление recyclerView.
        if (newsData != null && !newsData.isEmpty()) {
            mAdapter.addAll(newsData);
        }

        // Спрятать анимацию иконки свайпа, когда загрузчик закончил обновлять информацию
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<News>> loader) {
        // Загрузчик переустановлен, можем очищать уже существующие данные.
        mAdapter.clearAll();
    }

    /**
     * Когда пользователь возвращается на предыдущий экран, нажимая кнопку в SettingsActivity,
     * перезагружаем Loader, чтобы отобразить текущее значение preference.
     */
    @Override
    public void onResume() {
        super.onResume();
        restartLoader(isConnected());
    }

    /** Проверяем подключение к сети */
    private boolean isConnected() {
        // Получить обращение к ConnectivityManager чтобы проверить подключение к сети
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Получить подробности о текущей активной сети передачи данных по умолчанию
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Если интернет-соединение есть, инициализируем загрузчик как обычно.В противном случае,
     * прячем индикатор загрузки и устанавливаем пустое состояние TextView для отображения "нет инета"

     * @param isConnected internet connection is available or not
     */
    private void initializeLoader(boolean isConnected) {
        if (isConnected) {
            // Получаем обращение к LoaderManager,чтобы взаимодейтсвовать с загрузчиками.
            LoaderManager loaderManager = getLoaderManager();
            //Инициализиуем загрузчик NEWS_LOADER_ID
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            // Иначе выводим ошибку
            // First, hide loading indicator so error message will be visible
            mLoadingIndicator.setVisibility(View.GONE);
            // Update empty state with no connection error message and image
            mEmptyStateTextView.setText(R.string.no_internet_connection);
            mEmptyStateTextView.setCompoundDrawablesWithIntrinsicBounds(Constants.DEFAULT_NUMBER,
                    R.drawable.ic_network_check,Constants.DEFAULT_NUMBER,Constants.DEFAULT_NUMBER);
        }
    }

    /**
     * Перезагружем loader если есть интернет соединение.
     * @param isConnected internet connection is available or not
     */
    private void restartLoader(boolean isConnected) {
        if (isConnected) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            // Restart the loader with the NEWS_LOADER_ID
            loaderManager.restartLoader(NEWS_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            mLoadingIndicator.setVisibility(View.GONE);
            // Update empty state with no connection error message and image
            mEmptyStateTextView.setText(R.string.no_internet_connection);
            mEmptyStateTextView.setCompoundDrawablesWithIntrinsicBounds(Constants.DEFAULT_NUMBER,
                    R.drawable.ic_network_check,Constants.DEFAULT_NUMBER,Constants.DEFAULT_NUMBER);

            // Hide SwipeRefreshLayout
            mSwipeRefreshLayout.setVisibility(View.GONE);
        }
    }

    /**
     * Когда пользователь свайпает для обновления, restart the loader.
     */
    private void initiateRefresh() {
        restartLoader(isConnected());
    }
}
