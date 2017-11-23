package com.alex.banner.verical;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.alex.widget.banner.tips.TipsBanner;
import com.alex.widget.banner.tips.listener.OnBottomTipsClickListener;
import com.alex.widget.banner.tips.listener.OnTopTipsClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity.class";
    private TipsBanner tipsBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tipsBanner = (TipsBanner) findViewById(R.id.banner);
        tipsBanner.setDelayTime(3000)
                .setScrollTime(4000)
                .setAutoPlay(true)
                .setScroll(false);


        List<String> tipsList = new ArrayList<>();
        tipsList.add("贵州茅台接连出现折价大宗交易");
        tipsList.add("创纪录！恒指刚刚突破三万点！");
        tipsList.add("大众领先合资SUV集体下探");
        tipsList.add("基金辣评 | 大象起舞");
        tipsList.add("沪深百元股阵营扩编 达到23只");
        tipsBanner.setTipsList(tipsList);
        tipsBanner.start();

        tipsBanner.setOnTopTipsClickListener(new OnTopTipsClickListener() {
            @Override
            public void OnTopTipsClick(int position) {
                Log.d(TAG,  "点击" + position);
            }
        });

        tipsBanner.setOnBottomTipsClickListener(new OnBottomTipsClickListener() {
            @Override
            public void OnBottomTipsClick(int position) {
                Log.d(TAG,  "点击" + position);
            }
        });
    }
}
