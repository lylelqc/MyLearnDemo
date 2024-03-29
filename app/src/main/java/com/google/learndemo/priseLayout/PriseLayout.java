package com.google.learndemo.priseLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.learndemo.R;
import com.google.learndemo.tools.GlideUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PriseLayout extends ViewGroup {

    private Context context;
    private List<String> allUrls = new ArrayList<>();
    // false表示右边增加，true表示左边增加
    private boolean flag = false;
    // 每个布局的默认宽度，默认20
    private int spWidth = 20;
    private int radius;

    public PriseLayout(Context context) {
        super(context);
        this.context = context;

    }

    public PriseLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public PriseLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        //wrap_content
        int width = 0;
        int height = 0;

        //每一行的，宽，高
        int lineWidth = 0;
        int lineHeight = 0;
        //获取所有的子view
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            //测量子view
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            //得到layoutparams
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            if (lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight()) {
                //换行判断
                height += lineHeight;
                //新的行高
                lineHeight = childHeight;
                width = Math.max(width, lineWidth);
                lineWidth = childWidth;

            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }

            if (i == cCount - 1) {
                width = Math.max(lineWidth, width);
                height += lineHeight;
            }
        }

        setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width-(cCount-1) * spWidth,
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height);
    }


    /**
     * 存储所有的View
     */
    private List<List<View>> mAllViews = new ArrayList<List<View>>();
    /**
     * 每一行的高度
     */
    private List<Integer> mLineHeight = new ArrayList<Integer>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        mAllViews.clear();
        mLineHeight.clear();

        int width = getWidth();
        int lineWidth = 0;
        int lineHeight = 0;
        List<View> lineViews = new ArrayList<View>();
        int cCount = getChildCount();

        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            if (childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width - getPaddingRight() - getPaddingLeft()) {
                mLineHeight.add(lineHeight);
                mAllViews.add(lineViews);
                lineViews = new ArrayList<>();
                lineWidth = 0;
                lineHeight = childHeight + lp.topMargin + lp.bottomMargin;
            }
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin - spWidth;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin
                    + lp.bottomMargin);
            lineViews.add(child);
        }

        // 处理最后一行
        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);

        // 设置子View的位置
        int left = getPaddingLeft();
        int top = getPaddingTop();
        // 行数
        int lineNum = mAllViews.size();

        for (int i = 0; i < lineNum; i++) {
            lineViews = mAllViews.get(i);
            lineHeight = mLineHeight.get(i);

            //反序显示
            if(flag){
                Collections.reverse(lineViews);
            }
            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();
                child.layout(lc, tc, rc, bc);
                left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin - spWidth;
            }
            left = getPaddingLeft();
            top += lineHeight;
        }
    }

    public PriseLayout setRadius(int radius) {
        this.radius = dp2px(radius);
        return this;
    }

    public PriseLayout setSpWidth(int spWidth) {
        this.spWidth = dp2px(spWidth);
        return this;
    }

    public PriseLayout setFlag(boolean flag) {
        this.flag = flag;
        return this;
    }

    public PriseLayout setUrls(List<String> listVals) {
        allUrls.clear();
        allUrls.addAll(listVals);
        return this;
    }

    public PriseLayout setSingleUrls(String urlVal) {
        allUrls.clear();
        allUrls.add(urlVal);
        return this;
    }

    public PriseLayout cancels(String urlVal) {
        allUrls.remove(urlVal);
        addViews();
        return this;
    }

    // 开始绘制头像
    public PriseLayout addViews() {
        //清空，重新绘制
        removeAllViews();
        if(allUrls.size() > 0){
            if(flag){
                for (int i = allUrls.size() - 1; i >= 0; i--) {
                    ImageView imageView = (ImageView) LayoutInflater.from(context).inflate(R.layout.item_prise, this, false);
                    if(radius != 0){
                        imageView.getLayoutParams().width = radius;
                        imageView.getLayoutParams().height = radius;
                    }
                    GlideUtil.loadCircleImage(context, allUrls.get(i), imageView);
                    this.addView(imageView);
                }
            }else{
                for (int i = 0; i < allUrls.size(); i++) {
                    ImageView imageView = (ImageView) LayoutInflater.from(context).inflate(R.layout.item_prise, this, false);
                    if(radius != 0){
                        imageView.getLayoutParams().width = radius;
                        imageView.getLayoutParams().height = radius;
                    }
                    GlideUtil.loadCircleImage(context, allUrls.get(i), imageView);
                    this.addView(imageView);
                }
            }
        }
        return this;
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    public int dp2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
