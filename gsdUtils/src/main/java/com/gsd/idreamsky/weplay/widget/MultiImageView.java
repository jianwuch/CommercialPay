package com.gsd.idreamsky.weplay.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.gsd.idreamsky.weplay.utils.DensityUtil;
import com.gsd.idreamsky.weplay.utils.ImageLoader;
import com.gsd.idreamsky.weplay.utils.MR;
import java.util.List;

public class MultiImageView extends GridLayout {
    private static final String TAG = MultiImageView.class.getSimpleName();
    private static final int DEFAULT_COLUMN_NUM = 3;
    private Context mContext;
    private OnItemClickListener onItemClicklistener;
    private int mSize;
    private List<String> mImageUrl;

    private int mMaxColumn = 0;//设置的列数
    private int mMaxRow = 0;//最大的行数
    private ShowInType mShowInType;
    private int mNeedShowNum = 0;//列表中无法显示完全的情况下显示在最后一张图的张数

    //帖子列表类型使用
    private ShowInType mPhotoNum;

    public MultiImageView(Context context) {
        this(context, null);
    }

    public MultiImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        mMaxColumn = getColumnCount();
        if (mMaxColumn == 0) {
            mMaxColumn = DEFAULT_COLUMN_NUM;
        }
        setOrientation(HORIZONTAL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        removeAllViews();
        this.mSize = (w - DensityUtil.dp2px(10)) / mMaxColumn;

        if (null != mImageUrl && mImageUrl.size() > 0) {
            showPics();
        }
        Log.d(TAG, "onSizeChanged, width:" + w + "|" + mImageUrl.toString());
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        int size = MeasureSpec.getSize(widthSpec);
        Log.d(TAG, "onMeasure,width size:" + size);
    }

    private void showPics() {
        Log.d(TAG, "add Child and child width is :" + mSize);
        if (mSize == 0) {
            return;
        }
        for (int i = 0; i < mImageUrl.size(); i++) {
            int rowIndex = i / 3;//行
            int columnIndex = i % 3;//列
            Spec rowSpec = GridLayout.spec(i / 3);
            Spec columnSpec = GridLayout.spec(i % 3);
            LayoutParams params = new LayoutParams();
            params.rowSpec = rowSpec;
            params.columnSpec = columnSpec;
            params.width = mSize;
            params.height = mSize;
            if (mMaxColumn != (columnIndex + 1)) {
                //不是最后一列需要右边距
                params.rightMargin = DensityUtil.dp2px(5);
            }

            if (mMaxRow != (rowIndex + 1)) {
                //不是最后一行不需要下边距
                params.bottomMargin = DensityUtil.dp2px(5);
            }

            View imageView = null;
            if (i < mMaxRow * mMaxColumn - 1) {
                //非最后一张
                imageView = new ImageView(mContext);
                ((ImageView) imageView).setScaleType(ImageView.ScaleType.CENTER_CROP);
                ImageLoader.getInstance().loadNormal(mImageUrl.get(i), ((ImageView) imageView));
            } else {
                //最后一张
                //如果是帖子列表样式
                switch (mShowInType) {
                    case TYPE_SHOW_IN_TOPIC_LIST:
                        View layout = LayoutInflater.from(mContext)
                                .inflate(MR.getIdByLayoutName(mContext, "wpk_layout_with_num_img"),
                                        null);
                        ImageView imgView = (ImageView) layout.findViewById(
                                MR.getIdByIdName(mContext, "imgView"));
                        TextView numTv =
                                (TextView) layout.findViewById(MR.getIdByIdName(mContext, "numTv"));
                        ImageLoader.getInstance().loadNormal(mImageUrl.get(i), imgView);
                        numTv.setText(mNeedShowNum + "");
                        imageView = layout;
                        break;
                    default:
                        imageView = new ImageView(mContext);
                        ((ImageView) imageView).setScaleType(ImageView.ScaleType.CENTER_CROP);
                        ImageLoader.getInstance()
                                .loadNormal(mImageUrl.get(i), ((ImageView) imageView));
                        break;
                }
            }

            addView(imageView, params);
            final int position = i;
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClicklistener != null) {
                        onItemClicklistener.onClick(position);
                    }
                }
            });
        }
    }

    /**
     * 实现全图片
     */
    public void addImageList(List<String> imageUrl) {

        this.addImageList(imageUrl, ShowInType.TYPE_SHOW_ONLY_ALL_IMAGE);
    }

    /**
     * 可以显示前三张，最后一张显示图片数量
     */
    public void addImageList(List<String> imageUrl, ShowInType type) {
        Log.d(TAG, "addImageList");
        removeAllViews();
        this.mShowInType = type;
        switch (type) {
            case TYPE_SHOW_ONLY_ALL_IMAGE:
                int column = mMaxColumn;
                int size = imageUrl.size();
                if (size % column == 0) {
                    setRowCount(size / column);
                    this.mMaxRow = size / column;
                } else {
                    setRowCount(size / column + 1);
                    this.mMaxRow = size / column + 1;
                }

                this.mImageUrl = imageUrl;
                break;
            case TYPE_SHOW_IN_TOPIC_LIST:
                //取前三张，最大行数1
                int column1 = mMaxColumn;
                setRowCount(1);
                this.mMaxRow = 1;
                mNeedShowNum = imageUrl.size();
                if (mNeedShowNum > column1) {
                    this.mImageUrl = imageUrl.subList(0, column1);
                } else {
                    this.mImageUrl = imageUrl;
                }
                break;
        }

        showPics();
    }

    public List<String> getImageList() {
        return mImageUrl;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClicklistener) {
        this.onItemClicklistener = onItemClicklistener;
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public enum ShowInType {
        TYPE_SHOW_IN_TOPIC_LIST, TYPE_SHOW_ONLY_ALL_IMAGE
    }
}
