package com.sjs.sjsapp.ui.sharedUI;

/**
 * Created by xeno on 2017/2/20.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ex3ndr on 16.05.15.
 */
public abstract class AnimatorViewHolder extends RecyclerView.ViewHolder {
    public AnimatorViewHolder(View itemView) {
        super(itemView);
    }

    public abstract boolean performAnimation();
}