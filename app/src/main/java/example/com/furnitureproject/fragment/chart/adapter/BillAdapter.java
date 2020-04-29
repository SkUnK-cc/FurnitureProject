package example.com.furnitureproject.fragment.chart.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import example.com.furnitureproject.R;
import example.com.furnitureproject.db.DbHelper;
import example.com.furnitureproject.db.bean.AccountBean;
import example.com.furnitureproject.utils.TimeUtil;

/**
 *
 * @author luo
 * @date 2017/8/14
 */
public class BillAdapter extends UltimateViewAdapter {

    private List<AccountBean> mAccountList;
    private OnItemClickListener mItemClickListener;

    public BillAdapter(List<AccountBean> mAccountList) {
        this.mAccountList = mAccountList;
    }

    @Override
    public RecyclerView.ViewHolder newFooterHolder(View view) {
        return new UltimateRecyclerviewViewHolder<>(view);
    }

    @Override
    public RecyclerView.ViewHolder newHeaderHolder(View view) {

        return new UltimateRecyclerviewViewHolder<>(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bill_list, parent, false);
        ItemHoleder vh = new ItemHoleder(v);
        return vh;
    }

    @Override
    public int getAdapterItemCount() {
        return mAccountList.size();
    }

    @Override
    public long generateHeaderId(int position) {
        //Logger.e(calendar.get(Calendar.DAY_OF_MONTH) + "--->" + position);
        return getDay(new Date(mAccountList.get(position).getTime())) ;
    }

    private long getDay(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        Logger.e("normal: " + super.getItemViewType(position));
//        if (super.getItemViewType(position) == VIEW_TYPES.HEADER)

        ((ItemHoleder) holder).itemView.setTag(position);
        ItemHoleder itemHoleder = (ItemHoleder) holder;
        float count = mAccountList.get(position).getPrice()*mAccountList.get(position).getCount();
        String type = mAccountList.get(position).getType();
        String note = mAccountList.get(position).getNote();
        String remark = mAccountList.get(position).getNote();
        if (type.equals(AccountBean.TYPE_PAY_OTHER) || type.equals(AccountBean.TYPE_PAY_STOCK)) //支出类型
            count = -count;
        itemHoleder.tvClassifyMoney.setText(count + "");
        itemHoleder.tvClassify.setText(mAccountList.get(position).getName());
        itemHoleder.tvTransDetail.setText(mAccountList.get(position).getCount()+"笔"+"  单价："+mAccountList.get(position).getPrice());
        itemHoleder.ivClassify.setImageResource(mAccountList.get(position).getPicRes());
        if ( TextUtils.isEmpty(note) && TextUtils.isEmpty(remark)) {
            itemHoleder.tvClassifyDescribe.setVisibility(View.GONE);
        } else
            itemHoleder.tvClassifyDescribe.setText(note + "," +remark);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_stick_header, parent, false);
        return new HeaderHoleder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

        //Logger.e("header: " + getItemViewType(position));
        HeaderHoleder headerHoleder = (HeaderHoleder) holder;
        String time = TimeUtil.date2String(new Date(mAccountList.get(position).getTime()), "MM月dd日");
        String week = TimeUtil.getWeekByDate(new Date(mAccountList.get(position).getTime()));
        headerHoleder.tvStickyDay.setText(time);
        headerHoleder.tvStickyWeek.setText(week);
        //Logger.e(generateHeaderId(position)+ "  -- > " + position);
        float sumExpend = 0f;
        float sumIncome = 0f;
        for (int i = position; i < mAccountList.size(); i++) {
            Date date = new Date(mAccountList.get(i).getTime());
            //从当前day往后循环判断，如果day相同则表示为同一天，否则跳出循环
            if (getDay(date) == generateHeaderId(position)) {
                String type = mAccountList.get(i).getType();
                if (type.equals(AccountBean.TYPE_PAY_OTHER)||type.equals(AccountBean.TYPE_PAY_STOCK)) //支出
                    sumExpend += mAccountList.get(i).getPrice()*mAccountList.get(i).getCount();
                if (type.equals(AccountBean.TYPE_INCOME_SELL)) //收入
                    sumIncome += mAccountList.get(i).getPrice()*mAccountList.get(i).getCount();
            } else
                break;
        }
        headerHoleder.tvStickyExpend.setText("支出：" + sumExpend);
        headerHoleder.tvStickyIncome.setText("收入：" + sumIncome);

    }

    @Override
    public void onItemDismiss(int position) {
        super.onItemDismiss(position);
        removeItem(position);
    }

    private void removeItem(int position){
        if(position<0 || position>=mAccountList.size())return;
        DbHelper.INSTANCE.getAccountManager().deleteAccount(mAccountList.get(position));
        mAccountList.remove(position);
        notifyItemRemoved(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * 设置Item点击监听
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    class ItemHoleder extends UltimateRecyclerviewViewHolder implements View.OnClickListener {

        ImageView ivClassify;
        TextView tvClassify;
        TextView tvTransDetail;
        TextView tvClassifyDescribe;
        TextView tvClassifyMoney;
        View itemBill;

        public ItemHoleder(View itemView) {
            super(itemView);
            ivClassify = (ImageView) itemView.findViewById(
                    R.id.iv_classify);
            tvClassify = (TextView) itemView.findViewById(R.id.tv_classify);
            tvTransDetail = itemView.findViewById(R.id.tv_bill_count);
            tvClassifyDescribe = (TextView) itemView.findViewById(R.id.tv_classify_describe);
            tvClassifyMoney = (TextView) itemView.findViewById(R.id.tv_classify_money);
            itemBill = itemView.findViewById(R.id.item_bill);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }


    class HeaderHoleder extends UltimateRecyclerviewViewHolder implements View.OnClickListener {

        TextView tvStickyDay;
        TextView tvStickyWeek;
        TextView tvStickyExpend;
        TextView tvStickyIncome;

        public HeaderHoleder(View itemView) {
            super(itemView);
            tvStickyDay = (TextView) itemView.findViewById(R.id.tv_sticky_day);
            tvStickyWeek = (TextView) itemView.findViewById(R.id.tv_sticky_week);
            tvStickyExpend = (TextView) itemView.findViewById(R.id.tv_sticky_expend);
            tvStickyIncome = (TextView) itemView.findViewById(R.id.tv_sticky_income);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }


}
