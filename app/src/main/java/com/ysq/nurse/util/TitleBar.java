package com.ysq.nurse.util;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ysq.nurse.R;

/**
 * 类描述：
 * 创建人：Bob
 * 创建时间：2015/9/25 11:36
 */
public class TitleBar {
    private View titleView;
    private RelativeLayout titleBar;
    private TextView text;
    private TextView leftIco;
    private TextView rightTv;
    private ImageView rightIco;

    /**
     * 构造方法：用于获取对象
     */
    public TitleBar(Activity context) {
        titleView = context.findViewById(R.id.title_bar);
        text = (TextView) titleView.findViewById(R.id.title_text);
        titleBar = (RelativeLayout) titleView.findViewById(R.id.title_bar);
        leftIco = (TextView) titleView.findViewById(R.id.title_leftIco);
        rightIco = (ImageView) titleView.findViewById(R.id.title_rightIco);
        rightTv = (TextView) titleView.findViewById(R.id.title_rightTv);
    }

    /**
     * 用于设置标题栏文字
     */
    public TitleBar setTitleText(String titleText) {
        if (!TextUtils.isEmpty(titleText)) {
            text.setText(titleText);
        }
        return this;
    }

    public TitleBar setTextListening(View.OnClickListener listener) {
        if (text.getVisibility() == View.VISIBLE) {
            text.setOnClickListener(listener);
        }
        return this;
    }

    /**
     * 用于设置标题栏左边要显示的图片
     */
    public TitleBar setLeftIco(String s) {
        leftIco.setText(s);
        return this;
    }

    /**
     * 用于设置标题栏右边要显示的图片
     */
    public TitleBar setRightIco(int resId) {
        rightIco.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        rightIco.setImageResource(resId);
        return this;
    }

    /**
     * 用于设置标题栏右边要显示的图片
     */
    public TitleBar setRightText(String text) {
        rightTv.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(text))
            rightTv.setText(text);
        return this;
    }

    /**
     * 用于设置标题栏左边图片的单击事件
     */
    public TitleBar setLeftIcoListening(View.OnClickListener listener) {
        if (leftIco.getVisibility() == View.VISIBLE) {
            leftIco.setOnClickListener(listener);
        }
        return this;
    }

    /**
     * 用于设置标题栏右边图片的单击事件
     */
    public TitleBar setRightIcoListening(View.OnClickListener listener) {
        if (rightIco.getVisibility() == View.VISIBLE) {
            rightIco.setOnClickListener(listener);
        }
        return this;
    }

    /**
     * 用于设置标题栏右边文字的单击事件
     */
    public TitleBar setRightTextListening(View.OnClickListener listener) {
        if (rightTv.getVisibility() == View.VISIBLE) {
            rightTv.setOnClickListener(listener);
        }
        return this;
    }
}