# VerticalTipsBanner
现在很多App都有2行的文章小贴士功能，垂直方向轮播展示 文章标题。

## 效果图
<img src="screens/circle.gif" />

## 使用方法

Gradle配置

### 1. 在project的build.gradle添加如下代码

```
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```
### 2. 在Module的build.gradle添加依赖

```
compile "com.github.alexchenopen:VerticalTipsBanner:0.0.1"
```

### 3. 在xml中使用该控件
```
<com.alex.widget.banner.tips.Banner
        android:id="@+id/banner"
        android:layout_width="match_parent"
        android:layout_height="100dp"
        android:background="@android:color/white"
        app:tips_layout="@layout/layout_tips"
        app:delay_time="3000"
        app:is_auto_play="true"
        app:is_scroll="false"
        app:scroll_duration="4000"/>  
```
可以直接在xml布局文件中设置 滑动间隔delay_time、滑动时间scroll_duration、是否自动滑动is_auto_play、是否可以拖动is_scroll

也可以在Activity中通过Java代码控制
```
banner = (Banner) findViewById(R.id.banner);
banner.setDelayTime(3000)
        .setScrollTime(4000)
        .setAutoPlay(true)
        .setScroll(false);
```

xml布局中 app:tips_layout="@layout/layout_tips" 设置tips展示布局，<b>必须把2个TextView的id定义为tvTopTips和tvBottomTips</b> 比如
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical">

    <TextView
        android:id="@+id/tvTopTips"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:padding="10dp"
        android:text="tips"
        android:textColor="#000"
        android:textSize="16sp"/>

    <TextView
        android:id="@+id/tvBottomTips"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:padding="10dp"
        android:text="tips"
        android:textColor="#08c3a6"
        android:textSize="16sp"/>
</LinearLayout>
```

### 4、传入tips列表
```
List<String> tipsList = new ArrayList<>();
        tipsList.add("贵州茅台接连出现折价大宗交易");
        tipsList.add("创纪录！恒指刚刚突破三万点！");
        tipsList.add("大众领先合资SUV集体下探");
        tipsList.add("基金辣评 | 大象起舞");
        tipsList.add("沪深百元股阵营扩编 达到23只");
banner.setTipsList(tipsList);
banner.start();
```

### 5、监控点击事件

```
banner.setOnTopTipsClickListener(new OnTopTipsClickListener() {
            @Override
            public void OnTopTipsClick(int position) {
                Log.d(TAG,  "点击" + position);
            }
        });

banner.setOnBottomTipsClickListener(new OnBottomTipsClickListener() {
            @Override
            public void OnBottomTipsClick(int position) {
                Log.d(TAG,  "点击" + position);
            }
        });
 ```


##  FAQ
QQ:108762795
