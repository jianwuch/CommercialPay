package com.gsd.idreamsky.weplay.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ReplacementSpan;
import android.util.AttributeSet;
import android.widget.TextView;
import com.gsd.idreamsky.weplay.utils.ColorUtil;
import com.gsd.idreamsky.weplay.utils.DensityUtil;
import java.util.List;

public class LabelTextView extends TextView {

    public static final int HOLLOW = 2;
    public static final int SOLID = 1;

    private Context mContext;

    public LabelTextView(Context context) {
        super(context);
    }

    public LabelTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LabelTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(Context context) {
        this.mContext = context;
    }

    /**
     * 直接传图片，插入
     */
    public void setLabel(int labelImgId, String text) {
        Bitmap b = BitmapFactory.decodeResource(getResources(), labelImgId);
        ImageSpan imgSpan = new ImageSpan(this.mContext, b);
        SpannableString spanString = new SpannableString("label");
        spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        this.setText(spanString);
        this.append(text);
    }

    public void setTopicLabel(List<ThreadIcon> threadIcons, String text) {
        // TODO: 2017/7/5 还未判断img的情况
        if (threadIcons == null || threadIcons.isEmpty()) {
            if (!TextUtils.isEmpty(text)) {
                int[] colors = ColorUtil.color2rgba(Color.parseColor("#1795FF"));
                String RAW_COLOR_C6 = "#"
                        + Integer.toHexString(colors[0])
                        + Integer.toHexString(colors[1])
                        + Integer.toHexString(colors[2]);
                text = text.replaceAll("<font>", "<font color=\"" + RAW_COLOR_C6 + "\">");
            }
            setText(Html.fromHtml(text));
        } else {
            setTopicLabel(20, threadIcons, text);
        }
    }

    /**
     * 传文字和颜色和样式，自己画
     *
     * @param labelmarginRight 标签与文字的间距
     * @param text 文本
     */
    public void setTopicLabel(int labelmarginRight, List<ThreadIcon> threadIcons, String text) {
        String labels = "";
        for (int i = 0; i < threadIcons.size(); i++) {
            labels += threadIcons.get(i).name + " ";
        }
        if (labels.endsWith(" ")) {
            labels = labels.substring(0, labels.length() - 1);
        }
        if (TextUtils.isEmpty(labels)) {
            return;
        }
        SpannableString spanString = new SpannableString(labels);
        for (int i = 0; i < threadIcons.size(); i++) {
            int start = labels.indexOf(threadIcons.get(i).name);
            int end = start + threadIcons.get(i).name.length();
            //设置标签字体大小-相对值
            spanString.setSpan(new RelativeSizeSpan(0.72f), start, end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //设置标签字体大小-绝对值
            //            spanString.setSpan(new AbsoluteSizeSpan(13,true), 0, label.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanString.setSpan(new RadiusBackgroundSpan(threadIcons.get(i).bgColor,
                            threadIcons.get(i).fontColor, 5, threadIcons.get(i).type, labelmarginRight),
                    start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        this.setText(spanString);
        text = text.replace("<font>", "<font color=\"#1795ff\">");
        this.append(Html.fromHtml(text));
    }

    /**
     * 传文字和颜色和样式，自己画
     *
     * @param labelmarginRight 标签与文字的间距
     * @param label 标签文字
     * @param color 标签颜色
     * @param labelType 标签的类型 HOLLOW空心，SOLID实心
     * @param text 文本
     */
    public void setLabel(int labelmarginRight, @NonNull String label[], @NonNull int color[],
            @NonNull int labelType[], String text) {
        if (label.length != color.length || color.length != labelType.length) return;
        String labels = "";
        for (int i = 0; i < label.length; i++) {
            labels += label[i] + " ";
        }
        if (labels.endsWith(" ")) {
            labels = labels.substring(0, labels.length() - 1);
        }
        SpannableString spanString = new SpannableString(labels);
        for (int i = 0; i < label.length; i++) {
            int start = labels.indexOf(label[i]);
            int end = start + label[i].length();
            //设置标签字体大小-相对值
            spanString.setSpan(new RelativeSizeSpan(0.72f), start, end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //设置标签字体大小-绝对值
            //            spanString.setSpan(new AbsoluteSizeSpan(13,true), 0, label.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanString.setSpan(
                    new RadiusBackgroundSpan(color[i], Color.parseColor("#ffffff"), 5, labelType[i],
                            labelmarginRight), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        this.setText(spanString);
        this.append(text);
    }

    /**
     * 重写背景，自带无法画圆角，并且标签和其他字有间隔
     *
     * @author abel.zhang
     */
    private class RadiusBackgroundSpan extends ReplacementSpan {
        private int mSize;
        private int mBgColor;
        private int mFontColor;
        private int mRadius;
        private int mLabelType;
        private int mLabelmarginRight;

        /**
         * @param bgColor 背景颜色
         * @param radius 圆角半径
         * @param radius 标签类型
         */
        public RadiusBackgroundSpan(int bgColor, int fontColor, int radius, int labelType,
                int labelmarginRight) {
            mBgColor = bgColor;
            mFontColor = fontColor;
            mRadius = radius;
            mLabelType = labelType;
            mLabelmarginRight = labelmarginRight;
        }

        @Override
        public int getSize(Paint paint, CharSequence text, int start, int end,
                Paint.FontMetricsInt fm) {
            //mSize就是文字的宽度加上左右两个圆角的半径得到span宽度
            mSize = (int) (paint.measureText(text, start, end) + 2 * mRadius);
            //mLabelmarginRight设置于又边的间隔
            return mSize + mLabelmarginRight;
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top,
                int y, int bottom, Paint paint) {
            paint.setColor(mBgColor);//设置画笔颜色
            paint.setAntiAlias(true);// 设置画笔的锯齿效果
            x += 3;//右移一点
            RectF oval =
                    new RectF(x, y + paint.ascent() - 9, x + mSize + 8, y + paint.descent() - 2);
            //实心空心转换
            if (mLabelType == LabelTextView.SOLID) {
                //设置文字背景矩形，x为span其实左上角相对整个TextView的x值，y为span左上角相对整个View的y值。paint.ascent()获得文字上边缘，paint.descent()获得文字下边缘
                canvas.drawRoundRect(oval, mRadius, mRadius, paint);//绘制圆角矩形，第二个参数是x半径，第三个参数是y半径
                paint.setColor(mFontColor);//恢复画笔的文字颜色
                //后画文字，否则会被背景盖住
                canvas.drawText(text, start, end, x + mRadius + 4, y - 4, paint);
            } else if (mLabelType == LabelTextView.HOLLOW) {
                //画空心要先画文字，防止背景画笔影响字体
                canvas.drawText(text, start, end, x + mRadius + 4, y - 4, paint);
                paint.setStyle(Paint.Style.STROKE);
                int width = DensityUtil.dp2px(1);
                paint.setStrokeWidth(width);
                canvas.drawRoundRect(oval, mRadius, mRadius, paint);//绘制圆角矩形，第二个参数是x半径，第三个参数是y半径
            }
        }
    }
}
