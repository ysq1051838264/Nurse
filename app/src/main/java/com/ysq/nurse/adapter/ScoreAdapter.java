package com.ysq.nurse.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_score, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (position == 0) {
            holder.title.setVisibility(View.VISIBLE);
        } else
            holder.title.setVisibility(View.GONE);

        holder.tv.setText(dataList.get(position).get(0).getName());

        holder.number.setText(Float.valueOf(dataList.get(position).get(0).getNum()).intValue() + "");
        holder.score.setText(dataList.get(position).get(0).getScore() + "");

        holder.progress.setMax(100);
        holder.progress.setProgress(dataList.get(position).get(0).getScore());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv;
        TextView number;
        TextView score;
        LinearLayout title;
        NumberProgressBar progress;

        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.tv);
            number = (TextView) view.findViewById(R.id.number);
            score = (TextView) view.findViewById(R.id.score);
            title = (LinearLayout) view.findViewById(R.id.title);

            progress = (NumberProgressBar) view.findViewById(R.id.progress);
        }
    }
}
