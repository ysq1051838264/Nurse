package com.ysq.nurse.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ysq.nurse.R;
import com.ysq.nurse.base.contract.BaseView;


/**
 * 基础activity 包含请求成功失败 界面
 *
 * @packageName: cn.white.ymc.wanandroidmaster.base.contract
 * @fileName: BaseResultActivity
 * @date: 2018/8/23  14:25
 * @author: ymc
 * @QQ:745612618
 */

public abstract class BaseResultActivity extends BaseActivity implements BaseView {

    private static final int NORMAL_STATE = 0;
    private static final int LOADING_STATE = 1;
    public static final int ERROR_STATE = 2;
    public static final int EMPTY_STATE = 3;

//    private View mErrorView;
//    private View mLoadingView;
//    private View mEmptyView;
//    private ViewGroup mNormalView;
//    private int currentState = NORMAL_STATE;
//    private TextView tvErrMsg;

    @Override
    protected void initView() {
        if(activity == null){
            throw new IllegalStateException("Activity cannot be empty");
        }
//        mNormalView = findViewById(R.id.normal_view);
//        if(mNormalView  == null){
//            throw new IllegalStateException("There must be no mNormalView in the activity");
//        }
//        if(!(mNormalView.getParent() instanceof ViewGroup)){
//            throw new IllegalStateException("The parent layout of mNormalView must belong to the viewgroup");
//        }
//        ViewGroup parent = (ViewGroup) mNormalView.getParent();
//        View.inflate(activity, R.layout.view_loading, parent);
//        View.inflate(activity, R.layout.view_error, parent);
//        View.inflate(activity, R.layout.view_empty, parent);
//        mLoadingView = parent.findViewById(R.id.loading_group);
//        mErrorView = parent.findViewById(R.id.error_group);
//        mEmptyView = parent.findViewById(R.id.empty_group);
//        tvErrMsg = parent.findViewById(R.id.tv_err_msg);
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

}
