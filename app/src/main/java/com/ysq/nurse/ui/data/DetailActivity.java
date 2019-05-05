package com.ysq.nurse.ui.data;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;

import com.openxu.cview.chart.barchart.BarHorizontalChart;
import com.openxu.cview.chart.bean.BarBean;
import com.openxu.utils.DensityUtil;
import com.ysq.nurse.R;
import com.ysq.nurse.base.BaseActivity;
import com.ysq.nurse.util.TitleBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

public class DetailActivity extends BaseActivity {

    @BindView(R.id.btn)
    Button button;

    BarHorizontalChart chart1;
    List<List<BarBean>> dataList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    protected void initView() {
        new TitleBar(this).setTitleText("详细信息").setLeftIcoListening(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        Random random = new Random();
        chart1 = findViewById(R.id.chart1);
//        chart1.setBarSpace(DensityUtil.dip2px(this, 1));  //双柱间距
        chart1.setBarItemSpace(DensityUtil.dip2px(this, 5));  //柱间距
        chart1.setDebug(false);
        chart1.setBarNum(2);
//        chart1.setBarColor(new int[]{Color.parseColor("#5F93E7"), Color.parseColor("#F28D02")});
        chart1.setBarColor(new int[]{Color.parseColor("#5F93E7")});

//X轴
        List<String> strXList = new ArrayList<>();
//柱状图数据
        dataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            //此集合为柱状图上一条数据，集合中包含几个实体就是几个柱子
            List<BarBean> list = new ArrayList<>();
            list.add(new BarBean(random.nextInt(30), "lable1"));
//            list.add(new BarBean(random.nextInt(20), "lable2"));
            dataList.add(list);
            strXList.add((i + 1) + "月");
        }
        chart1.setLoading(false);
        chart1.setData(dataList, strXList);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<BarBean> li = new ArrayList<>();
                BarBean b = new BarBean(random.nextInt(60), "lable1");
                li.add(b);
                dataList.set(4, li);
                chart1.setData(dataList, strXList);
            }
        });
    }
}
