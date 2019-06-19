package com.ysq.nurse.ui.data;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.openxu.cview.chart.barchart.BarHorizontalChart;
import com.openxu.cview.chart.bean.BarBean;
import com.ysq.nurse.R;
import com.ysq.nurse.adapter.ScoreAdapter;
import com.ysq.nurse.base.BaseActivity;
import com.ysq.nurse.ui.util.SimpleDividerItemDecoration;
import com.ysq.nurse.util.TitleBar;
import com.ysq.nurse.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

public class DetailActivity extends BaseActivity {
    public static final String KEY_DATA = "key_data";
    String UUID_SERVICE = "6e400001-b5a3-f393-e0a9-e50e24dcca9e";
    String UUID_CHARACTERISTIC_WRITE = "6e400002-b5a3-f393-e0a9-e50e24dcca9e";
    String UUID_CHARACTERISTIC_NOTIFY = "6e400003-b5a3-f393-e0a9-e50e24dcca9e";

    public static final int START = 75;   //定义范围开始数字
    public static final int END = 100; //定义范围结束数字

    private BleDevice bleDevice;

    @BindView(R.id.btn)
    Button button;
    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;
    @BindView(R.id.all)
    TextView all;

    BarHorizontalChart chart1;
    List<List<BarBean>> dataList;
    List<String> strXList;
    ScoreAdapter adpter;

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

    private void initRecycler() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this, 20, 1));

        adpter = new ScoreAdapter(this, dataList);
        mRecyclerView.setAdapter(adpter);
    }

    @Override
    protected void initData() {
        bleDevice = getIntent().getParcelableExtra(KEY_DATA);
        if (bleDevice == null)
            return;

        initChartView();

        initRecycler();

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

    public int getScore() {
        Random random = new Random();
        int number = random.nextInt(END - START + 1) + START;
        return number;
    }

    public void notifyData(int type) {
        List<BarBean> list = new ArrayList<>();
        BarBean b = null;
        int index = 0;
        switch (type) {
            case 49:
                ToastUtil.showLong(this, "拍背");
                index = 0;
                b = new BarBean(dataList.get(index).get(0).getNum() + 1, "拍背", getScore());
                break;
            case 50:
                ToastUtil.showLong(this, "刷床单");
                index = 1;
                b = new BarBean(dataList.get(index).get(0).getNum() + 1, "刷床单", getScore());
                break;
            case 51:
                ToastUtil.showLong(this, "转床单");
                index = 2;
                b = new BarBean(dataList.get(index).get(0).getNum() + 1, "转床单", getScore());
                break;
            case 52:
                ToastUtil.showLong(this, "擦胳膊");
                index = 3;
                b = new BarBean(dataList.get(index).get(0).getNum() + 1, "擦胳膊", getScore());
                break;
            case 53:
                ToastUtil.showLong(this, "梳头");
                index = 4;
                b = new BarBean(dataList.get(index).get(0).getNum() + 1, "梳头", getScore());
                break;
            default:
                index = 0;
                b = new BarBean(dataList.get(index).get(0).getNum() + 1, "拍背", getScore());
                break;
        }

        list.add(b);
        dataList.set(index, list);
        chart1.refresh(dataList);

        if (adpter != null)
            adpter.notifyDataSetChanged();

//        culatureScore();
        getAllScore(b);
    }

    int allScore = 0;
    int allNum = 0;

    public void getAllScore(BarBean b) {
        allScore = allScore + b.getScore();
        allNum = allNum + 1;
        if (all!=null && allScore > 0) {
            all.setText(String.format("综合质量评分: %s%s", allScore / allNum, "%"));
        }
    }

    private void culatureScore() {
        int s = 0;

        for (int i = 0; i < dataList.size(); i++) {
            s = s + dataList.get(i).get(0).getScore();
        }

        all.setText(String.format("综合质量评分: %s %", s / 5));
    }

    public void initChartView() {
        //X轴
        strXList = new ArrayList<>();
//        //柱状图数据
        dataList = new ArrayList<>();

        chart1 = findViewById(R.id.chart1);
        chart1.setDebug(false);
        chart1.setLoading(true);
        chart1.setBarNum(1);
        chart1.setBarColor(new int[]{Color.parseColor("#5F93E7")});

        for (int i = 0; i < 5; i++) {
            //此集合为柱状图上一条数据，集合中包含几个实体就是几个柱子
            List<BarBean> list = new ArrayList<>();
            String s = "lable1";
            switch (i) {
                case 0:
                    s = "拍背";
                    break;
                case 1:
                    s = "刷床单";
                    break;
                case 2:
                    s = "转床单";
                    break;
                case 3:
                    s = "擦胳膊";
                    break;
                case 4:
                    s = "梳头";
                    break;
            }
            list.add(new BarBean(0, s, 0));
            dataList.add(list);

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
    }


    @OnClick(R.id.btn)
    public void goMain() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您确定要结束服务么？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancel();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.create().show();
    }

    public void cancel() {
        byte[] input = {0x3c, 0x3c, 0x31, 0x30, 0x30, 0x3e, 0x3e};
        ToastUtil.showShort(this, "本次服务已结束");
        BleManager.getInstance().write(
                bleDevice,
                UUID_SERVICE,
                UUID_CHARACTERISTIC_WRITE,
                input,
                new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                        Log.e("ysq监听", "写入数据成功了");
                        finish();
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


}
