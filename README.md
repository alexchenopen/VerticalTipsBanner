# VerticalBanner
现在很多App都有2行的文章小贴士功能，垂直功能，无限轮播。

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
        app:delay_time="3000"
        app:is_auto_play="true"
        app:is_scroll="false"
        app:scroll_duration="4000"/>  
```
