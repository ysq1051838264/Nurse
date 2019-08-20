package com.ysq.nurse.ui.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bx.marqueeviewlibrary.MarqueeView;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.ysq.nurse.R;
import com.ysq.nurse.adapter.DeviceAdapter;
import com.ysq.nurse.base.BaseFragment;
import com.ysq.nurse.comm.ObserverManager;
import com.ysq.nurse.ui.data.DetailActivity;
import com.ysq.nurse.ui.data.NewDetailActivity;
import com.ysq.nurse.util.TitleBar;
import com.ysq.nurse.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeFragment extends BaseFragment {
    private static final int REQUEST_CODE_OPEN_GPS = 1;
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 2;

    @BindView(R.id.list_device)
    ListView listView_device;
    @BindView(R.id.img_loading)
    ImageView img_loading;
    @BindView(R.id.btn_scan)
    Button btn_scan;
    @BindView(R.id.marqueeview)
    MarqueeView marqueeView;

    private ProgressDialog progressDialog;
    private Animation operatingAnim;
    private DeviceAdapter mDeviceAdapter;

    BleDevice ble;

    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    @Override
    public int getLayoutResID() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initData() {
        new TitleBar(getActivity()).setTitleText("首页", false);

        initAd();

//        BleManager.getInstance().init(context);
//        BleManager.getInstance()
//                .enableLog(true)
//                .setReConnectCount(1, 5000)
//                .setConnectOverTime(20000)
//                .setOperateTimeout(5000);

//        operatingAnim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
//        operatingAnim.setInterpolator(new LinearInterpolator());
//        progressDialog = new ProgressDialog(getContext());
//
//        mDeviceAdapter = new DeviceAdapter(getContext());
//        mDeviceAdapter.setOnDeviceClickListener(new DeviceAdapter.OnDeviceClickListener() {
//            @Override
//            public void onConnect(BleDevice bleDevice) {
//                ble = bleDevice;
//                if (!BleManager.getInstance().isConnected(bleDevice)) {
//                    BleManager.getInstance().cancelScan();
//                    connect(bleDevice);
//                }
//            }
//
//            @Override
//            public void onDisConnect(final BleDevice bleDevice) {
//                if (BleManager.getInstance().isConnected(bleDevice)) {
//                    BleManager.getInstance().disconnect(bleDevice);
//                }
//            }
//
//            @Override
//            public void onDetail(BleDevice bleDevice) {
//                if (BleManager.getInstance().isConnected(bleDevice)) {
//                    Intent intent = new Intent(getContext(), DetailActivity.class);
//                    intent.putExtra(DetailActivity.KEY_DATA, bleDevice);
//                    startActivity(intent);
//                }
//            }
//        });
//        listView_device.setAdapter(mDeviceAdapter);
    }

    private void initAd() {
        List<String> notices = new ArrayList<>();
        notices.add("本月出勤30次,完成服务80次");
        notices.add("你今天有工作，请按时出勤");
        marqueeView.startMarquee(notices);
    }

    @OnClick({R.id.btn, R.id.btn_scan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn:
//                if (ble != null && BleManager.getInstance().isConnected(ble)) {
//                    Intent intent = new Intent(getContext(), DetailActivity.class);
//                    intent.putExtra(DetailActivity.KEY_DATA, ble);
//                    startActivity(intent);
//                } else
//                    ToastUtil.showLong(getContext(), "请先连接手环，在打卡");
                startActivity(new Intent(getContext(), NewDetailActivity.class));
                break;
            case R.id.btn_scan:
//                if (btn_scan.getText().equals(getString(R.string.start_scan))) {
//                    checkPermissions();
//                } else if (btn_scan.getText().equals(getString(R.string.stop_scan))) {
//                    BleManager.getInstance().cancelScan();
//                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        BleManager.getInstance().disconnectAllDevice();
//        BleManager.getInstance().destroy();
    }

    private void startScan() {
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                mDeviceAdapter.clearScanDevice();
                mDeviceAdapter.notifyDataSetChanged();
                img_loading.startAnimation(operatingAnim);
                img_loading.setVisibility(View.VISIBLE);
                btn_scan.setText(getString(R.string.stop_scan));
            }

            @Override
            public void onLeScan(BleDevice bleDevice) {
                super.onLeScan(bleDevice);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                mDeviceAdapter.addDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                img_loading.clearAnimation();
                img_loading.setVisibility(View.INVISIBLE);
                btn_scan.setText(getString(R.string.start_scan));
            }
        });
    }

    private void setScanRule() {
        String[] uuids;
        String str_uuid = "6e400001-b5a3-f393-e0a9-e50e24dcca9e,6e400002-b5a3-f393-e0a9-e50e24dcca9e,00001800-0000-1000-8000-00805f9b34fb," +
                "00001801-0000-1000-8000-00805f9b34fb,0000180a-0000-1000-8000-00805f9b34fb," +
                "00001812-0000-1000-8000-00805f9b34fb";
        if (TextUtils.isEmpty(str_uuid)) {
            uuids = null;
        } else {
            uuids = str_uuid.split(",");
        }
        UUID[] serviceUuids = null;
        if (uuids != null && uuids.length > 0) {
            serviceUuids = new UUID[uuids.length];
            for (int i = 0; i < uuids.length; i++) {
                String name = uuids[i];
                String[] components = name.split("-");
                if (components.length != 5) {
                    serviceUuids[i] = null;
                } else {
                    serviceUuids[i] = UUID.fromString(uuids[i]);
                }
            }
        }

        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
//                .setServiceUuids(serviceUuids)      // 只扫描指定的服务的设备，可选
//                .setDeviceName(true, names)   // 只扫描指定广播名的设备，可选
//                .setDeviceMac(mac)                  // 只扫描指定mac的设备，可选
                .setAutoConnect(true)      // 连接时的autoConnect参数，可选，默认false
                .setScanTimeOut(10000)              // 扫描超时时间，可选，默认10秒
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
    }

    private void connect(final BleDevice bleDevice) {
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                progressDialog.show();
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                img_loading.clearAnimation();
                img_loading.setVisibility(View.INVISIBLE);
                btn_scan.setText(getString(R.string.start_scan));
                progressDialog.dismiss();
                Toast.makeText(getContext(), getString(R.string.connect_fail), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                progressDialog.dismiss();
                mDeviceAdapter.addDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                progressDialog.dismiss();

                mDeviceAdapter.removeDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();

                if (isActiveDisConnected) {
                    Toast.makeText(getContext(), getString(R.string.active_disconnected), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), getString(R.string.disconnected), Toast.LENGTH_LONG).show();
                    ObserverManager.getInstance().notifyObserver(bleDevice);
                }
            }
        });
    }

    private void checkPermissions() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Toast.makeText(getContext(), getString(R.string.please_open_blue), Toast.LENGTH_LONG).show();
            return;
        }

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        List<String> permissionDeniedList = new ArrayList<>();
        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(getContext(), permission);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permission);
            } else {
                permissionDeniedList.add(permission);
            }
        }
        if (!permissionDeniedList.isEmpty()) {
            String[] deniedPermissions = permissionDeniedList.toArray(new String[permissionDeniedList.size()]);
            ActivityCompat.requestPermissions(getActivity(), deniedPermissions, REQUEST_CODE_PERMISSION_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_LOCATION:
//                if (grantResults.length > 0) {
//                    for (int i = 0; i < grantResults.length; i++) {
//                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
//                            onPermissionGranted(permissions[i]);
//                        }
//                    }
//                }
                break;
        }
    }

    private void onPermissionGranted(String permission) {
        switch (permission) {
            case Manifest.permission.ACCESS_FINE_LOCATION:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkGPSIsOpen()) {
                    new AlertDialog.Builder(getContext())
                            .setTitle(R.string.notifyTitle)
                            .setMessage(R.string.gpsNotifyMsg)
                            .setNegativeButton(R.string.cancel,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            getActivity().finish();
                                        }
                                    })
                            .setPositiveButton(R.string.setting,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                            startActivityForResult(intent, REQUEST_CODE_OPEN_GPS);
                                        }
                                    })

                            .setCancelable(false)
                            .show();
                } else {
                    setScanRule();
                    startScan();
                }
                break;
        }
    }

    private boolean checkGPSIsOpen() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null)
            return false;
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE_OPEN_GPS) {
//            if (checkGPSIsOpen()) {
//                setScanRule();
//                startScan();
//            }
//        }
    }


}
