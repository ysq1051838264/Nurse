package com.ysq.nurse.ui.my;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ysq.nurse.R;
import com.ysq.nurse.base.BaseActivity;
import com.ysq.nurse.util.ConstantUtil;
import com.ysq.nurse.util.SharedPreferenceUtil;
import com.ysq.nurse.util.TitleBar;

import butterknife.BindView;

/**
 * 用户信息
 */
public class PersonActivity extends BaseActivity {
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.sex)
    TextView sex;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_person;
    }

    @Override
    protected void initView() {
        new TitleBar(this).setTitleText("个人信息").setLeftIcoListening(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        String s1 = (String) SharedPreferenceUtil.get(this, ConstantUtil.USERNAME, "");
        name.setText(s1);

    }

}
