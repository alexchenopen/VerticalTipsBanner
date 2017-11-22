package com.alex.banner.verical;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alex.widget.banner.tips.Banner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Banner banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        banner = (Banner) findViewById(R.id.banner);
        banner.setDelayTime(3000)
                .setScrollTime(4000);


        List<String> tipsList = new ArrayList<>();
        tipsList.add("贵州茅台接连出现折价大宗交易");
        tipsList.add("创纪录！恒指刚刚突破三万点！");
        tipsList.add("大众领先合资SUV集体下探");
        tipsList.add("基金辣评 | 大象起舞");
        tipsList.add("沪深百元股阵营扩编 达到23只");
        banner.setTipsList(tipsList);
        banner.start();
    }
}
