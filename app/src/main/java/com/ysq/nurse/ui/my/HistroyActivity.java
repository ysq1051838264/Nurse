package com.ysq.nurse.ui.my;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;

import com.openxu.cview.chart.barchart.BarVerticalChart;
import com.openxu.cview.chart.bean.BarBean;
import com.openxu.utils.DensityUtil;
import com.ysq.nurse.R;
import com.ysq.nurse.base.BaseActivity;
import com.ysq.nurse.util.TitleBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

/**
 * 历史数据
 */
public class HistroyActivity extends BaseActivity {
    @BindView(R.id.chart3)
    BarVerticalChart chart3;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_history;
    }

    @Override
    protected void initView() {
        new TitleBar(this).setTitleText("历史服务").setLeftIcoListening(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        //X轴
        List<String> strXList = new ArrayList<>();
        //柱状图数据
        List<List<BarBean>> dataList = new ArrayList<>();
        Random random = new Random();

        chart3 = (BarVerticalChart)findViewById(R.id.chart3);
        chart3.setBarSpace(DensityUtil.dip2px(this, 1));  //双柱间距
        chart3.setBarItemSpace(DensityUtil.dip2px(this, 20));  //柱间距
        chart3.setShowEnd(true);      //内容超过时，初始显示是否是最后的数据
        chart3.setDebug(false);
        chart3.setBarNum(5);
        chart3.setBarColor(new int[]{Color.parseColor("#5F93E7"),Color.parseColor("#F28D02"),
                Color.parseColor("#157EFB"),Color.parseColor("#FED032")
                ,Color.parseColor("#3ddd62")});
        strXList.clear();
        dataList.clear();
        for(int i = 0; i<5; i++){
            //此集合为柱状图上一条数据，集合中包含几个实体就是几个柱子
            List<BarBean> list = new ArrayList<>();
            list.add(new BarBean(random.nextInt(30), "拍背",0));
            list.add(new BarBean(random.nextInt(20), "刷床单",0));
            list.add(new BarBean(random.nextInt(35), "转床单",0));
            list.add(new BarBean(random.nextInt(28), "擦胳膊",0));
            list.add(new BarBean(random.nextInt(28), "梳头",0));
            dataList.add(list);
            strXList.add((i+1)+"月1日");
        }

        chart3.setLoading(false);
        chart3.setData(dataList, strXList);
    }

}
