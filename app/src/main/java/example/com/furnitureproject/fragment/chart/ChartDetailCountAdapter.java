package example.com.furnitureproject.fragment.chart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import example.com.furnitureproject.R;
import example.com.furnitureproject.utils.NumUtil;

/**
 * @author luo
 * @date 2017/8/14
 */
public class ChartDetailCountAdapter extends BaseRecycleAdapter {

    private static final int TYPE_HEAD = 0;
    private static final int TYPE_ITEM = 1;
    private static int TYPE = TYPE_ITEM;
    private final List<ChartDataBean> mChartList;

    private Context context;

    public ChartDetailCountAdapter(Context context, List<ChartDataBean> list) {
        this.context = context;
        this.mChartList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEAD) {
            return new HeaderHolder(LayoutInflater.from(context).inflate(R.layout.item_chart_count_header, parent, false));
        } else
            return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.item_chart_count, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        switch (getItemViewType(position)) {
            case TYPE_HEAD:

                break;
            case TYPE_ITEM:
                int pos = position - 1; //减去header布局
                final ItemHolder itemHoleder = (ItemHolder) holder;
                itemHoleder.mIvClassify.setImageResource(mChartList.get(pos).getImgRes());
                itemHoleder.mTvClassifyName.setText(mChartList.get(pos).getName());
                itemHoleder.mTvClassifyCount.setText(mChartList.get(pos).getCount() + "笔"+"  单价:"+mChartList.get(pos).getTotal());
                String percent = NumUtil.getTwoPointFloat(mChartList.get(pos).getPrecent() * 100);
                itemHoleder.mTvClassifyPercent.setText(percent + "%");
                itemHoleder.mTvClassifyTotal.setText(mChartList.get(pos).getTotal()*mChartList.get(pos).getCount() + "");
                //startProgressBarAnimation(itemHoleder, (int)(Math.random() * 100));
                float progress = NumUtil.getPointFloat(mChartList.get(pos).getPrecent(), 2);
                itemHoleder.mPbClassify.setProgress((int) ( progress * 100));
                break;
            default:
                break;
        }
    }

    /**
     * pregressbar动画 (卡顿)
     *
     * @param itemHoleder
     * @param progress
     */
    private void startProgressBarAnimation(final ItemHolder itemHoleder, int progress) {
        ValueAnimator animator = ValueAnimator.ofInt(0, progress);
        animator.setDuration(1000l);
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                itemHoleder.mPbClassify.setProgress(value);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mChartList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            TYPE = TYPE_HEAD;
        else
            TYPE = TYPE_ITEM;
        return TYPE;
    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        TextView mTvHeader;

        public HeaderHolder(View itemView) {
            super(itemView);
            mTvHeader = itemView.findViewById(R.id.tv_header);
        }

    }

    class ItemHolder extends RecyclerView.ViewHolder {
        ImageView mIvClassify;
        TextView mTvClassifyName;
        TextView mTvClassifyCount;
        TextView mTvClassifyPercent;
        TextView mTvClassifyTotal;
        ProgressBar mPbClassify;

        public ItemHolder(View itemView) {
            super(itemView);
            mIvClassify = itemView.findViewById(R.id.iv_classify);
            mTvClassifyName = itemView.findViewById(R.id.tv_classify_name);
            mTvClassifyCount = itemView.findViewById(R.id.tv_classify_count);
            mTvClassifyPercent = itemView.findViewById(R.id.tv_classify_percent);
            mTvClassifyTotal = itemView.findViewById(R.id.tv_classify_total);
            mPbClassify = itemView.findViewById(R.id.pb_classify);
        }

    }

}
