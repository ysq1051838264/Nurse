package com.ysq.nurse.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.openxu.cview.chart.bean.BarBean;
import com.ysq.nurse.R;

import java.util.List;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.MyViewHolder> {
    private Context context;
    List<List<BarBean>> dataList;

    public ScoreAdapter(Context context, List<List<BarBean>> mList) {
        this.context = context;
        this.dataList = mList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_score, parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv.setText(dataList.get(position).get(0).getName());
        holder.progress.setMax(100);
        holder.progress.setProgress(dataList.get(position).get(0).getScore());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv;
        NumberProgressBar progress;

        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.tv);
            progress = (NumberProgressBar) view.findViewById(R.id.progress);
        }
    }
}
