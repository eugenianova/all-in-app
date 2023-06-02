package com.example.android.myguardian.presentation.news;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * EmptyRecyclerView is RecyclerView subclass that provides empty view support for RecyclerView
 * to show or hide an empty view based on whether the adapter provided to the RecyclerView has
 * data or not.
 */

public class EmptyRecyclerView extends RecyclerView {

    private View mEmptyView;

    /** AdapterDataObserver вызывает метод checkIfEmpty() каждый раз, и он наблюдает за событием, которое изменяет содержимое адаптера*/
    final private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }
    };

    public EmptyRecyclerView(Context context) {
        super(context);
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs,
                             int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Проверяет, если оба mEmptyView и adapter не NULL
     * Скрывает или показываем mEmptyView в завсимости от размера  данных(item count) в адаптере.
     */
    private void checkIfEmpty() {
        // Если количество, предоставленное адаптером, равно 0, делаем пустой View видимым и прячем EmptyRecyclerView.
        // Иначе прячем пустой View and делаем EmptyRecyclerView видимым.
        if (mEmptyView != null && getAdapter() != null) {
            final boolean emptyViewVisible =
                    getAdapter().getItemCount() == 0;
            mEmptyView.setVisibility(emptyViewVisible ? VISIBLE : GONE);
            setVisibility(emptyViewVisible ? GONE : VISIBLE);
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }

        checkIfEmpty();
    }

    /**
     * Set an empty view on the EmptyRecyclerView
     * @param emptyView refers to the empty state of the view
     */
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
        checkIfEmpty();
    }
}
