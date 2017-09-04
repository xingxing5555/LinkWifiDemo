package com.zlw.main.linkwifidemo.mifi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.zlw.main.linkwifidemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MifiActivity extends AppCompatActivity {

    @BindView(R.id.tvMsg)
    TextView tvMsg;

    public static void startMe(Context context) {
        context.startActivity(new Intent(context, MifiActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mifi);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.btStart, R.id.btClose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btStart:
                break;
            case R.id.btClose:
                break;
        }
    }
}
