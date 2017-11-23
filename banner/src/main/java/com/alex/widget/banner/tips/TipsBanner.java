package com.alex.widget.banner.tips;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alex.widget.banner.tips.listener.OnBottomTipsClickListener;
import com.alex.widget.banner.tips.listener.OnTopTipsClickListener;
import com.alex.widget.banner.tips.view.TipsBannerViewPager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

/**
 * Created by chenlin on 17/11/22.
 */

public class TipsBanner extends FrameLayout implements ViewPager.OnPageChangeListener {
    public String tag = "tips_banner";
    private int delayTime = BannerConfig.TIME;
    private int scrollTime = BannerConfig.DURATION;
    private boolean isAutoPlay = BannerConfig.IS_AUTO_PLAY;
    private boolean isScroll = BannerConfig.IS_SCROLL;
    private int mLayoutResId = R.layout.tips_banner;
    private int mTipsLayoutResId = R.layout.banner_tips;
    private int count = 0;
    private int currentItem;
    private List<String> tipsList;
    private List<View> viewList;
    private Context context;
    private View bannerContainer;
    private TipsBannerViewPager viewPager;
    private BannerPagerAdapter adapter;
    private ViewPager.OnPageChangeListener onPageChangeListener;
    private BannerScroller scroller;
    private OnTopTipsClickListener onTopTipsClickListener;
    private OnBottomTipsClickListener onBottomTipsClickListener;

    private WeakHandler handler = new WeakHandler();

    public TipsBanner(Context context) {
        this(context, null);
    }

    public TipsBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TipsBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        viewList = new ArrayList<>();
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        viewList.clear();
        handleTypedArray(context, attrs);
        View view = LayoutInflater.from(context).inflate(mLayoutResId, this, true);
        bannerContainer = view.findViewById(R.id.tipsBannerContainer);
        viewPager = (TipsBannerViewPager) view.findViewById(R.id.tipsBannerViewPager);
        initViewPagerScroll();
    }

    private void handleTypedArray(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TipsBanner);
        delayTime = typedArray.getInt(R.styleable.TipsBanner_delay_time, BannerConfig.TIME);
        scrollTime = typedArray.getInt(R.styleable.TipsBanner_scroll_duration, BannerConfig.DURATION);
        isAutoPlay = typedArray.getBoolean(R.styleable.TipsBanner_is_auto_play, BannerConfig.IS_AUTO_PLAY);
        isScroll = typedArray.getBoolean(R.styleable.TipsBanner_is_scroll, BannerConfig.IS_SCROLL);
        mLayoutResId = typedArray.getResourceId(R.styleable.TipsBanner_banner_layout, mLayoutResId);
        mTipsLayoutResId = typedArray.getResourceId(R.styleable.TipsBanner_tips_layout, mTipsLayoutResId);
        typedArray.recycle();
    }

    private void initViewPagerScroll() {
        try {
            Field mField = VerticalViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            scroller = new BannerScroller(viewPager.getContext());
            scroller.setDuration(scrollTime);
            mField.set(viewPager, scroller);
        } catch (Exception e) {
            Log.e(tag, e.getMessage());
        }
    }


    public TipsBanner setAutoPlay(boolean isAutoPlay) {
        this.isAutoPlay = isAutoPlay;
        return this;
    }

    public TipsBanner setDelayTime(int delayTime) {
        this.delayTime = delayTime;
        return this;
    }

    public TipsBanner setScrollTime(int scrollTime) {
        this.scrollTime = scrollTime;
        return this;
    }

    public TipsBanner setBannerAnimation(Class<? extends ViewPager.PageTransformer> transformer) {
        try {
            setPageTransformer(true, transformer.newInstance());
        } catch (Exception e) {
            Log.e(tag, "Please set the PageTransformer class");
        }
        return this;
    }

    /**
     * Set the number of pages that should be retained to either side of the
     * current page in the view hierarchy in an idle state. Pages beyond this
     * limit will be recreated from the adapter when needed.
     *
     * @param limit How many pages will be kept offscreen in an idle state.
     * @return Banner
     */
    public TipsBanner setOffscreenPageLimit(int limit) {
        if (viewPager != null) {
            viewPager.setOffscreenPageLimit(limit);
        }
        return this;
    }

    /**
     * Set a {@link ViewPager.PageTransformer} that will be called for each attached page whenever
     * the scroll position is changed. This allows the application to apply custom property
     * transformations to each page, overriding the default sliding look and feel.
     *
     * @param reverseDrawingOrder true if the supplied PageTransformer requires page views
     *                            to be drawn from last to first instead of first to last.
     * @param transformer         PageTransformer that will modify each page's animation properties
     * @return Banner
     */
    public TipsBanner setPageTransformer(boolean reverseDrawingOrder, ViewPager.PageTransformer transformer) {
        viewPager.setPageTransformer(reverseDrawingOrder, transformer);
        return this;
    }

    public TipsBanner setScroll(boolean isScroll) {
        this.isScroll = isScroll;
        return this;
    }

    public TipsBanner setTipsList(List<String> tipsList) {
        this.tipsList = tipsList;
//        this.count = tipsList.size();
        this.count = (tipsList.size() + 1) / 2;
        return this;
    }

    public void update(List<String> tipsList) {
        this.viewList.clear();
        this.tipsList.clear();
        this.tipsList.addAll(tipsList);
//        this.count = this.viewList.size();
        this.count = (tipsList.size() + 1) / 2;
        start();
    }

    public TipsBanner start() {
        setCircleViewList(tipsList);
        setData();
        return this;
    }


    private void setCircleViewList(List<String> tipsList) {
        if (tipsList == null || tipsList.size() <= 0) {
            bannerContainer.setVisibility(GONE);
            Log.e(tag, "The image data set is empty.");
            return;
        }
        bannerContainer.setVisibility(VISIBLE);
        for (int i = 0; i <= count + 1; i++) {
            View itemView = LayoutInflater.from(context).inflate(mTipsLayoutResId, null);
            TextView tvTopTips = (TextView) itemView.findViewById(R.id.tvTopTips);
            TextView tvBottomTips = (TextView) itemView.findViewById(R.id.tvBottomTips);
            if (i == 0) {
                if (tipsList.size() % 2 == 0) {
                    tvTopTips.setText(tipsList.get(tipsList.size() - 2));
                    tvBottomTips.setText(tipsList.get(tipsList.size() - 1));
                } else {
                    tvTopTips.setText(tipsList.get(tipsList.size() - 1));
                    tvBottomTips.setText("");
                }
            } else if (i == count + 1) {
                tvTopTips.setText(tipsList.get(0));
                if (tipsList.size() > 0)
                    tvBottomTips.setText(tipsList.get(1));
                else
                    tvBottomTips.setText("");
            } else {
                tvTopTips.setText(tipsList.get((i - 1) * 2));
                if (tipsList.size() > (i - 1) * 2 + 1)
                    tvBottomTips.setText(tipsList.get((i - 1) * 2 + 1));
                else
                    tvBottomTips.setText("");
            }
            viewList.add(itemView);
        }
    }

    private void setData() {
        currentItem = 1;
        if (adapter == null) {
            adapter = new BannerPagerAdapter();
            viewPager.setOnPageChangeListener(this);
        }
        viewPager.setAdapter(adapter);
        viewPager.setFocusable(true);
        viewPager.setCurrentItem(1);
        if (isScroll && count > 1) {
            viewPager.setScrollable(true);
        } else {
            viewPager.setScrollable(false);
        }
        if (isAutoPlay)
            startAutoPlay();
    }


    public void startAutoPlay() {
        handler.removeCallbacks(task);
        handler.postDelayed(task, delayTime);
    }

    public void stopAutoPlay() {
        handler.removeCallbacks(task);
    }

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if (count > 1 && isAutoPlay) {
                currentItem = currentItem % (count + 1) + 1;
//                Log.i(tag, "curr:" + currentItem + " count:" + count);
                if (currentItem == 1) {
                    viewPager.setCurrentItem(currentItem, false);
                    handler.post(task);
                } else {
                    viewPager.setCurrentItem(currentItem);
                    handler.postDelayed(task, delayTime);
                }
            }
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.i(tag, ev.getAction() + "--" + isAutoPlay);
        if (isAutoPlay) {
            int action = ev.getAction();
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                    || action == MotionEvent.ACTION_OUTSIDE) {
                startAutoPlay();
            } else if (action == MotionEvent.ACTION_DOWN) {
                stopAutoPlay();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 返回真实的位置
     *
     * @param position
     * @return 下标从0开始
     */
    public int toRealPosition(int position) {
        int realPosition = (position - 1) % count;
        if (realPosition < 0)
            realPosition += count;
        return realPosition;
    }

    class BannerPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            container.addView(viewList.get(position));
            View view = viewList.get(position);
            if (onTopTipsClickListener != null)
                view.findViewById(R.id.tvTopTips).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onTopTipsClickListener.OnTopTipsClick(toRealPosition(position) * 2);
                    }
                });

            if (onBottomTipsClickListener != null)
                view.findViewById(R.id.tvBottomTips).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBottomTipsClickListener.OnBottomTipsClick(toRealPosition(position) * 2 + 1);
                    }
                });
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrollStateChanged(state);
        }
//        Log.i(tag,"currentItem: "+currentItem);
        switch (state) {
            case 0://No operation
                if (currentItem == 0) {
                    viewPager.setCurrentItem(count, false);
                } else if (currentItem == count + 1) {
                    viewPager.setCurrentItem(1, false);
                }
                break;
            case 1://start Sliding
                if (currentItem == count + 1) {
                    viewPager.setCurrentItem(1, false);
                } else if (currentItem == 0) {
                    viewPager.setCurrentItem(count, false);
                }
                break;
            case 2://end Sliding
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrolled(toRealPosition(position), positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        currentItem = position;
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageSelected(toRealPosition(position));
        }
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        onPageChangeListener = onPageChangeListener;
    }

    public void releaseBanner() {
        handler.removeCallbacksAndMessages(null);
    }

    public void setOnTopTipsClickListener(OnTopTipsClickListener onTopTipsClickListener) {
        this.onTopTipsClickListener = onTopTipsClickListener;
    }

    public void setOnBottomTipsClickListener(OnBottomTipsClickListener onBottomTipsClickListener) {
        this.onBottomTipsClickListener = onBottomTipsClickListener;
    }

}
