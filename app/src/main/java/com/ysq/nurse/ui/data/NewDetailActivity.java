package com.ysq.nurse.ui.data;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.openxu.cview.chart.barchart.BarHorizontalChart;
import com.openxu.cview.chart.bean.BarBean;
import com.ysq.nurse.R;
import com.ysq.nurse.adapter.ScoreAdapter;
import com.ysq.nurse.ui.util.SimpleDividerItemDecoration;
import com.ysq.nurse.util.TitleBar;
import com.ysq.nurse.util.ToastUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class NewDetailActivity extends Activity implements RadioGroup.OnCheckedChangeListener {
    private static final int REQUEST_SELECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int UART_PROFILE_READY = 10;
    public static final String TAG = "nRFUART";
    private static final int UART_PROFILE_CONNECTED = 20;
    private static final int UART_PROFILE_DISCONNECTED = 21;
    private static final int STATE_OFF = 10;

    TextView mRemoteRssiVal;
    RadioGroup mRg;
    private int mState = UART_PROFILE_DISCONNECTED;
    private UartService mService = null;
    private BluetoothDevice mDevice = null;
    private BluetoothAdapter mBtAdapter = null;

    private Button btnConnectDisconnect;
    private LinearLayout showLly;
    private Button start;
    private Button btn;
    private LinearLayout noShowLly;
    private TextView all;
    private RecyclerView mRecyclerView;

    public static final int START = 75;   //定义范围开始数字
    public static final int END = 100; //定义范围结束数字
    BarHorizontalChart chart1;
    List<List<BarBean>> dataList;
    List<String> strXList;
    ScoreAdapter adpter;

    private boolean uartConnected = false;

    //socket server vars
    private ServerSocket serverSocket;
    Thread serverThread = null;
    private static final int SERVERPORT = 6000;
    private static final int BUFSIZE = 20;
    private boolean socketConnected = false;
    private OutputStream socketOutput = null;

    public void initView() {
        btnConnectDisconnect = findViewById(R.id.btn_select);
        noShowLly = findViewById(R.id.noDataLly);
        start = findViewById(R.id.start);
        showLly = findViewById(R.id.showLly);
        mRecyclerView = findViewById(R.id.recycle);
        all = findViewById(R.id.all);
        btn = findViewById(R.id.btn);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_detail);

        new TitleBar(this).setTitleText("详细信息").setLeftIcoListening(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter == null) {
            ToastUtil.show(this, "蓝牙没有打开");
            finish();
            return;
        }

        initView();
        initChartView();
        initRecycler();

        service_init();

        //socket server
        this.serverThread = new Thread(new ServerThread());
        this.serverThread.start();

        // Handle Disconnect & Connect button
        btnConnectDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBtAdapter.isEnabled()) {
                    Log.i(TAG, "onClick - BT not enabled yet");
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                } else {
                    if (btnConnectDisconnect.getText().equals("Connect")) {
                        //Connect button pressed, open DeviceListActivity class, with popup windows that scan for devices
                        Intent newIntent = new Intent(NewDetailActivity.this, DeviceListActivity.class);
                        startActivityForResult(newIntent, REQUEST_SELECT_DEVICE);
                    } else {
                        //Disconnect button pressed
                        if (mDevice != null) {
                            mService.disconnect();
                        }
                    }
                }
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initBleData();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
            case 1:
                ToastUtil.showLong(this, "拍背");
                index = 0;
                b = new BarBean(dataList.get(index).get(0).getNum() + 1, "拍背", getScore());
                break;
            case 2:
                ToastUtil.showLong(this, "刷床单");
                index = 1;
                b = new BarBean(dataList.get(index).get(0).getNum() + 1, "刷床单", getScore());
                break;
            case 3:
                ToastUtil.showLong(this, "转床单");
                index = 2;
                b = new BarBean(dataList.get(index).get(0).getNum() + 1, "转床单", getScore());
                break;
            case 4:
                ToastUtil.showLong(this, "擦胳膊");
                index = 3;
                b = new BarBean(dataList.get(index).get(0).getNum() + 1, "擦胳膊", getScore());
                break;
            case 5:
                ToastUtil.showLong(this, "擦背");
                index = 4;
                b = new BarBean(dataList.get(index).get(0).getNum() + 1, "擦背", getScore());
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
        if (all != null && allScore > 0) {
            all.setText(String.format("综合质量评分: %s%s", allScore / allNum, "%"));
        }
    }

    private void initRecycler() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this, 20, 1));

        adpter = new ScoreAdapter(this, dataList);
        mRecyclerView.setAdapter(adpter);
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
                    s = "擦背";
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
                    strXList.add("擦背");
                    break;
            }
        }
        chart1.setLoading(false);
        chart1.setData(dataList, strXList);
    }

    public void initBleData() {
        String message = "<<110>>";
        byte[] value;
        try {
            //send data to service
            value = message.getBytes("UTF-8");
            mService.writeRXCharacteristic(value);
            //Update the log with time stamp
            String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
            Log.e("打印————", "[" + currentDateTimeString + "] TX: " + message);

            showLly.setVisibility(View.VISIBLE);
            noShowLly.setVisibility(View.GONE);
            start.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ServerThread implements Runnable {
        public void run() {
            Socket clientSocket = null;
            Thread clientThread = null;
            try {
                serverSocket = new ServerSocket(SERVERPORT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Socket socket = serverSocket.accept();

                    if ((clientThread != null) && clientThread.isAlive()) {
                        clientThread.interrupt();
                        try {
                            clientThread.join(500);
                        } catch (InterruptedException e) {//don't care
                        }
                    }

                    if (clientSocket != null) {
                        try {
                            clientSocket.close();
                        } catch (IOException e) {
                            // ignore
                        }
                    }

                    clientSocket = socket;

                    SocketToBleThread commThread = new SocketToBleThread(socket);
                    clientThread = new Thread(commThread);
                    clientThread.start();
                } catch (IOException e) { //local close at exit
                    break;
                }
            }
            //cleanup at Stop
            if ((clientThread != null) && clientThread.isAlive()) {
                clientThread.interrupt();
                try {
                    clientThread.join(500);
                } catch (InterruptedException e) {//don't care
                }
            }

            if (clientSocket != null) {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    class SocketToBleThread implements Runnable {
        private InputStream input;
        private byte[] buffer = new byte[BUFSIZE];

        public SocketToBleThread(Socket clientSocket) {
            try {
                this.input = clientSocket.getInputStream();
                socketOutput = clientSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            socketConnected = true;
            runOnUiThread(new Runnable() {
                public void run() {
                    Log.e("打印————", "TCP connected");
                }
            });

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    int num_bytes = input.read(buffer);
                    if (num_bytes < 0) { //remote disconnect
                        break;
                    }
                    if ((num_bytes > 0) && (uartConnected)) {
                        /*
                        runOnUiThread(new Runnable() {
                            public void run() {
                                listAdapter.add("TCP->BLE");
                                messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
                            }
                        });
                        */
                        mService.writeRXCharacteristic(buffer);
                    }
                } catch (IOException e) { //remote disconnect
                    break;
                }
            }
            socketConnected = false;
            runOnUiThread(new Runnable() {
                public void run() {
                    Log.e("打印————", "TCP disconnected");
                }
            });
        }
    }

    //UART service connected/disconnected
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
            mService = ((UartService.LocalBinder) rawBinder).getService();
            Log.d(TAG, "onServiceConnected mService= " + mService);
            if (!mService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
        }

        public void onServiceDisconnected(ComponentName classname) {
            ////     mService.disconnect(mDevice);
            mService = null;
        }
    };

    private final BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            final Intent mIntent = intent;
            //*********************//
            if (action.equals(UartService.ACTION_GATT_CONNECTED)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());

                        btnConnectDisconnect.setText("Disconnect");
                        uartConnected = true;

//                        initBleData();
                        Log.d("ysq---", "[" + currentDateTimeString + "] Connected to: " + mDevice.getName());
                        start.setVisibility(View.VISIBLE);

                        mState = UART_PROFILE_CONNECTED;
                    }
                });
            }

            //*********************//
            if (action.equals(UartService.ACTION_GATT_DISCONNECTED)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                        Log.d(TAG, "UART_DISCONNECT_MSG");
                        btnConnectDisconnect.setText("Connect");

                        uartConnected = false;
                        Log.e("ysq断开", "[" + currentDateTimeString + "] Disconnected to: " + mDevice.getName());

                        mState = UART_PROFILE_DISCONNECTED;
                        mService.close();
                        //setUiState();
                    }
                });
            }

            //*********************//
            if (action.equals(UartService.ACTION_GATT_SERVICES_DISCOVERED)) {
                mService.enableTXNotification();
            }

            //*********************//
            if (action.equals(UartService.ACTION_DATA_AVAILABLE)) {
                final byte[] txValue = intent.getByteArrayExtra(UartService.EXTRA_DATA);

                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            String text = new String(txValue, "UTF-8");

                            if (text.length() > 4) {
                                String s = text.substring(3, 4);
                                notifyData(Integer.valueOf(s));
                            }

                            String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());

                            //[上午2:19:42] RX: <<250>>
                            Log.e("ysq 收到的数据 ", "[" + currentDateTimeString + "] RX: " + text);
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    }
                });

                if (socketConnected) {
                    try {
                        socketOutput.write(txValue);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                     /*
                     runOnUiThread(new Runnable() {
                         public void run() {
                             listAdapter.add("BLE->TCP");
                             messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
                         }
                     });
                     */
                }
            }
            //*********************//
            if (action.equals(UartService.DEVICE_DOES_NOT_SUPPORT_UART)) {
                showMessage("Device doesn't support UART. Disconnecting");
                mService.disconnect();
            }
        }
    };

    private void service_init() {
        Intent bindIntent = new Intent(this, UartService.class);
        bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(UARTStatusChangeReceiver, makeGattUpdateIntentFilter());
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UartService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(UartService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(UartService.DEVICE_DOES_NOT_SUPPORT_UART);
        return intentFilter;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");

        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(UARTStatusChangeReceiver);
        } catch (Exception ignore) {
            Log.e(TAG, ignore.toString());
        }
        unbindService(mServiceConnection);
        mService.stopSelf();
        mService = null;
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
        serverThread.interrupt();
        try {
            serverThread.join(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if (!mBtAdapter.isEnabled()) {
            Log.i(TAG, "onResume - BT not enabled yet");
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SELECT_DEVICE:
                //When the DeviceListActivity return, with the selected device address
                if (resultCode == Activity.RESULT_OK && data != null) {
                    String deviceAddress = data.getStringExtra(BluetoothDevice.EXTRA_DEVICE);
                    mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(deviceAddress);

                    Log.d(TAG, "... onActivityResultdevice.address==" + mDevice + "mserviceValue" + mService);
                    mService.connect(deviceAddress);
                }
                break;

            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this, "Bluetooth has turned on ", Toast.LENGTH_SHORT).show();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, "Problem in BT Turning ON ", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                Log.e(TAG, "wrong request code");
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }

    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (mState == UART_PROFILE_CONNECTED) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            showMessage("nRFUART's running in background.\n             Disconnect to exit");
        } else {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("提示")
                    .setMessage("确认退出么？")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("否", null)
                    .show();
        }
    }

}
