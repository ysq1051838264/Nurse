package com.ysq.nurse.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ldoublem.loadingviewlib.LVChromeLogo;
import com.ysq.nurse.R;
import com.ysq.nurse.base.contract.BaseView;
import com.ysq.nurse.util.network.NetUtil;
import com.ysq.nurse.util.network.NetWorkBroadcastReceiver;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * fragment 基础类
 * @packageName: cn.white.ymc.wanandroidmaster.base
 * @fileName: BaseFragment
 * @date: 2018/7/27  9:39
 * @author: ymc
 * @QQ:745612618
 */

public abstract class BaseFragment extends Fragment implements BaseView, NetWorkBroadcastReceiver.NetEvent{
    protected Activity activity;
    protected MyApplication context;
    private Unbinder bind;
    public static NetWorkBroadcastReceiver.NetEvent eventFragment;
    private int netMobile;

    /**
     * 处理页面加载中、页面加载失败、页面没数据
     */
    private static final int NORMAL_STATE = 0;
    private static final int LOADING_STATE = 1;
    public static final int ERROR_STATE = 2;
    public static final int EMPTY_STATE = 3;

//    private View mErrorView;
//    private View mLoadingView;
//    private View mEmptyView;
//    private LVChromeLogo lvChromeLogo;
//    private ViewGroup mNormalView;
//    private TextView tvMsg;
    /**
     * 当前状态
     */
    private int currentState = NORMAL_STATE;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutResID(), container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        context = MyApplication.getInstance();
        bind = ButterKnife.bind(this, view);
        initUI();
        initData();
    }

    /**
     * 网络改变的监听
     * @param netMobile
     */
    @Override
    public void onNetChange(int netMobile) {
        this.netMobile = netMobile;
        isNetConnect();
    }

    @Override
    public void onDestroy() {
        bind.unbind();
        super.onDestroy();
    }

    /**
     * 获取 布局信息
     * @return
     */
    public abstract int getLayoutResID();

    /**
     * 数据初始化
     */
    protected abstract void initData();

    /**
     * 初始化 ui 布局
     */
    protected void initUI(){
        if (getView() == null) {
            return;
        }
//        mNormalView = getView().findViewById(R.id.normal_view);
//        if (mNormalView == null) {
//            throw new IllegalStateException("The subclass of RootActivity must contain a View named 'mNormalView'.");
//        }
//        if (!(mNormalView.getParent() instanceof ViewGroup)) {
//            throw new IllegalStateException("mNormalView's ParentView should be a ViewGroup.");
//        }
//        ViewGroup parent = (ViewGroup) mNormalView.getParent();
//        View.inflate(activity, R.layout.view_loading, parent);
//        View.inflate(activity, R.layout.view_error, parent);
//        View.inflate(activity, R.layout.view_empty, parent);
//        mLoadingView = parent.findViewById(R.id.loading_group);
//        lvChromeLogo = mLoadingView.findViewById(R.id.lv_load);
//        mErrorView = parent.findViewById(R.id.error_group);
//        mEmptyView = parent.findViewById(R.id.empty_group);
//        tvMsg = parent.findViewById(R.id.tv_err_msg);
//        mErrorView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                reload();
//            }
//        });
//        mErrorView.setVisibility(View.GONE);
//        mEmptyView.setVisibility(View.GONE);
//        mLoadingView.setVisibility(View.GONE);
//        mNormalView.setVisibility(View.VISIBLE);
    }

    /**
     * 判断有无网络
     *
     * @return true 有网, false 没有网络.
     */
    public boolean isNetConnect() {
        if (netMobile == NetUtil.NETWORK_WIFI) {
            return true;
        } else if (netMobile == NetUtil.NETWORK_MOBILE) {
            return true;
        } else if (netMobile == NetUtil.NETWORK_NONE) {
            return false;
        }
        return false;
    }

}
