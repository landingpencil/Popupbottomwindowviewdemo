package com.landingpencil.popupbottomwindowview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


/**
 * Created by Landingpencil on 2017/6/28.
 */

public class Popupbottomwindowview extends LinearLayout {

    private AnimatorListener animatorListener;

    //底部内容的View
    private FrameLayout base_view;

    //View内容的
    private FrameLayout content_view;

    //背景的View
    private RelativeLayout popup_bg;

    //xml加载的view
    private View bottomPopupView;

    //外部加载的内容View
    private View contentView;

    //外部加载的底部内容viwe
    private View baseView;

    //手势最小值
    private float miniVelocity = 0;

    private boolean mDrawable = true;

    public Popupbottomwindowview(Context context) {
        this(context, null);
    }

    public Popupbottomwindowview(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Popupbottomwindowview(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //初始化各种数值

        miniVelocity = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        bottomPopupView = LayoutInflater.from(getContext()).inflate(R.layout.layout_bottom_popup, null);
        base_view = (FrameLayout) bottomPopupView.findViewById(R.id.bottom_view);
        content_view = (FrameLayout) bottomPopupView.findViewById(R.id.content_view);
        popup_bg = (RelativeLayout) bottomPopupView.findViewById(R.id.pupup_bg);

        //把整个View都加载在LinearLayout里以显示出来
        addView(bottomPopupView);

        //背景颜色监听
        popup_bg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                disMissPopupView();
            }
        });

        //屏蔽内容区域点击事件
        content_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //屏蔽底部内容区域点击事件
        base_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //内容区域判断是否向下, 手势先下就关闭窗
        content_view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float y1 = 0, y2 = 0;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    y1 = event.getY();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    y2 = event.getY();
                    if ((y2 - y1) > miniVelocity) {
                        disMissPopupView();
                    }
                }
                return false;
            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDrawable && baseView!=null) {
            base_view.addView(baseView);
            mDrawable = false;
        }
    }

    public void showPopupView() {
        if (contentView != null) {
            //开始动画
            startAnimation();

            //开启背景颜色的渐变动画
            popup_bg.setVisibility(View.VISIBLE);
            popup_bg.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.bp_bottom_bg_in));

            //把整个区域全部显示出来
            ((Popupbottomwindowview) this).setLayoutParams(new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

            //加入内容区域
            content_view.addView(contentView, 0);
            content_view.setVisibility(View.VISIBLE);

            //开启内容区域动画
            content_view.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.bp_bottom_view_in));

        }
    }

    //获取View的高度
    public int getViewHeight(View view) {
        int width = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
        return view.getMeasuredHeight();

    }

    public void startAnimation() {
        final ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 40);
        valueAnimator.setDuration(250);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (animatorListener != null) {
                    animatorListener.startValue((Integer) valueAnimator.getAnimatedValue());
                }
            }
        });
        valueAnimator.start();
    }

    public void endAnimition() {
        final ValueAnimator valueAnimator = ValueAnimator.ofInt(40, 0);
        valueAnimator.setDuration(250);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (animatorListener != null) {
                    animatorListener.startValue((Integer) valueAnimator.getAnimatedValue());
                }
            }
        });
        valueAnimator.start();

    }

    public void disMissPopupView() {

        endAnimition();

        //开启内容区域动画
        content_view.setVisibility(GONE);

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.bp_bottom_view_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                content_view.removeAllViews();
                popup_bg.setVisibility(GONE);
                popup_bg.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.bp_bottom_bg_out));

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, getViewHeight(Popupbottomwindowview.this));
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, -1);
                ((Popupbottomwindowview)Popupbottomwindowview.this).setLayoutParams(layoutParams);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        content_view.setAnimation(animation);

    }


    public void setAnimatorListener(AnimatorListener animatorListener) {
        this.animatorListener = animatorListener;
    }

    public void setBaseViewiew(View baseView) {
        this.baseView = baseView;
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

    public void setContentView(int id) {
        this.contentView = LayoutInflater.from(getContext()).inflate(id, null);

    }
}
