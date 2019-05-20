package com.ysq.nurse.ui.my;

import android.view.View;
import android.widget.Button;

import com.openxu.cview.chart.bean.PieBean;
import com.openxu.cview.chart.piechart.PieChartLayout;
import com.openxu.utils.DensityUtil;
import com.ysq.nurse.R;
import com.ysq.nurse.base.BaseActivity;
import com.ysq.nurse.util.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 本月考勤
 */
public class CheckActivity extends BaseActivity {
    @BindView(R.id.pieChart3)
    PieChartLayout pieChart3;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_check;
    }

    @Override
    protected void initView() {
        new TitleBar(this).setTitleText("本月考勤").setLeftIcoListening(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        initChart();
    }

    private void initChart() {
        List<PieBean> datalist = new ArrayList<>();
        datalist.add(new PieBean(30, "正常"));
        datalist.add(new PieBean(1, "缺勤"));
        datalist.add(new PieBean(11, "早退"));
        datalist.add(new PieBean(8, "迟到"));

        //圆环宽度，如果值>0,则为空心圆环，内环为白色，可以在内环中绘制字
        pieChart3.setRingWidth(DensityUtil.dip2px(this, 0));
        pieChart3.setTagModul(PieChartLayout.TAG_MODUL.MODUL_LABLE);
        pieChart3.setDebug(false);
        pieChart3.setLoading(false);
        pieChart3.setChartData(PieBean.class, "Numner", "Name", datalist, null);
    }

}
