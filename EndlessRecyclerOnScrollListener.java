package com.sjs.sjsapp.ui.sharedUI;

import android.support.v7.widget.*;
/**
 * Created by xeno on 2017/2/17.
 * onscrollListener of RecyclerView.
 * Due to consideration of flexibility，google didn't implement onscrollListener，
 * this listener implement onscrollListener for load more of RecyclerView.
 *
 * 原理：在onScrolled()中比对firstVisibleItemPosition与总item数，观察当前滑动位置是否到达最底部。
 *
 * Usage:
 * recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager){};
 */

public abstract class EndlessRecyclerOnScrollListener extends
        RecyclerView.OnScrollListener {

    private int previousTotal = 0;
    private boolean loading = true;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int currentPage = 1;

    private LinearLayoutManager mLinearLayoutManager;

    public EndlessRecyclerOnScrollListener(
            LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading
                && (totalItemCount - visibleItemCount) <= firstVisibleItem) {
            currentPage++;
            onLoadMore(currentPage);
            loading = true;
        }
    }

    public abstract void onLoadMore(int currentPage);
}