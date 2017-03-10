package com.sjs.sjsapp.ui.sharedUI;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sjs.sjsapp.R;
import com.sjs.sjsapp.test.MyViewHolder;

import java.util.List;

/**
 * Created by xeno on 2017/2/20.
 *
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder> {

    public RecyclerViewAdapter(List<String> dataList) {
        this.dataList = dataList;
    }

    public List<String> getDataList() {
        return dataList;
    }

    public void setDataList(List<String> dataList) {
        this.dataList = dataList;
    }

    public void addData(List<String> dataList) {
        this.dataList.addAll(dataList);
    }

    private List<String> dataList;


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_finance_card_view,parent, false);
        MyViewHolder holder= new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.getTextView().setText( dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
