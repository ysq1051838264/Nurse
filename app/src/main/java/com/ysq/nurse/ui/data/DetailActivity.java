package com.ysq.nurse.ui.data;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.openxu.cview.chart.barchart.BarHorizontalChart;
import com.openxu.cview.chart.bean.BarBean;
import com.openxu.utils.DensityUtil;
import com.ysq.nurse.R;
import com.ysq.nurse.base.BaseActivity;
import com.ysq.nurse.util.TitleBar;
import com.ysq.nurse.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

public class DetailActivity extends BaseActivity {
    public static final String KEY_DATA = "key_data";
    String UUID_SERVICE = "6e400001-b5a3-f393-e0a9-e50e24dcca9e";
    String UUID_CHARACTERISTIC_WRITE = "6e400002-b5a3-f393-e0a9-e50e24dcca9e";
    String UUID_CHARACTERISTIC_NOTIFY = "6e400003-b5a3-f393-e0a9-e50e24dcca9e";

    private BleDevice bleDevice;

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
        bleDevice = getIntent().getParcelableExtra(KEY_DATA);
        if (bleDevice == null)
            return;

        initChartView();

        byte[] input = {0x3c, 0x3c, 0x31, 0x31, 0x30, 0x3e, 0x3e};

        BleManager.getInstance().write(
                bleDevice,
                UUID_SERVICE,
                UUID_CHARACTERISTIC_WRITE,
                input,
                new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                        Log.e("ysq监听", "写入数据成功了");
                        BleManager.getInstance().notify(
                                bleDevice,
                                UUID_SERVICE,
                                UUID_CHARACTERISTIC_NOTIFY,
                                new BleNotifyCallback() {
                                    @Override
                                    public void onNotifySuccess() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ToastUtil.showLong(getBaseContext(), "请短按手环1秒，绿灯闪烁之后，开始工作");
                                                Log.e("ysq监听", "监听通知成功");
                                            }
                                        });
                                    }

                                    @Override
                                    public void onNotifyFailure(final BleException exception) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Log.e("ysq监听", "监听失败");
//                                                addText(txt, exception.toString());
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCharacteristicChanged(byte[] data) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Log.e("ysq监听到数据:", HexUtil.formatHexString(data, true));

                                                //监听到数据:: 3c 3c 32 34 30 3e 3e
                                                //（例："<<220>>"代表动作2，刷床单）
                                                if (data.length > 5) {
                                                    notifyData(data[3]);
                                                }
                                            }
                                        });
                                    }
                                });

                    }

                    @Override
                    public void onWriteFailure(final BleException exception) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("ysq监听", "写入数据失败了");
                            }
                        });
                    }
                });
    }

    public void notifyData(int type) {
        switch (type) {
            case 49:
                ToastUtil.show(this, "拍背");
//                strXList.add("拍背");
                break;
            case 50:
                ToastUtil.show(this, "刷床单");
//                strXList.add("刷床单");
                break;
            case 51:
                ToastUtil.show(this, "转床单");
//                strXList.add("转床单");
                break;
            case 52:
                ToastUtil.show(this, "擦胳膊");
//                strXList.add("擦胳膊");
                break;
            case 53:
                ToastUtil.show(this, "梳头");
//                strXList.add("梳头");
                break;
        }
    }

    public void initChartView() {
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
        for (int i = 0; i < 5; i++) {
            //此集合为柱状图上一条数据，集合中包含几个实体就是几个柱子
            List<BarBean> list = new ArrayList<>();
            list.add(new BarBean(random.nextInt(30), "lable1"));
//            list.add(new BarBean(random.nextInt(20), "lable2"));
            dataList.add(list);
//            strXList.add((i + 1) + "月");

            switch (i) {
                case 0:
                    strXList.add("拍背");
                    break;
                case 1:
                    strXList.add("刷床单");
                    break;
                case 2:
                    strXList.add("转床单");
                    break;
                case 3:
                    strXList.add("擦胳膊");
                    break;
                case 4:
                    strXList.add("梳头");
                    break;
            }
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
