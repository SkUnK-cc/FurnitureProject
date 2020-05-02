package example.com.furnitureproject.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import example.com.furnitureproject.R;
import example.com.furnitureproject.db.DbHelper;
import example.com.furnitureproject.db.bean.AccountBean;
import example.com.furnitureproject.db.bean.DetailTypeBean;
import example.com.furnitureproject.utils.ToastUtil;

/**
 *
 * @author luo
 * @date 2017/8/14
 */
public class GoodsListAdapter extends UltimateViewAdapter {

    private List<DetailTypeBean> detailTypeBeanList;
    private OnItemClickListener mItemClickListener;

    public GoodsListAdapter(List<DetailTypeBean> mAccountList) {
        this.detailTypeBeanList = mAccountList;
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
                .inflate(R.layout.item_good_list, parent, false);
        ItemHoleder vh = new ItemHoleder(v);
        return vh;
    }

    @Override
    public int getAdapterItemCount() {
        return detailTypeBeanList.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return position;
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
        float count = detailTypeBeanList.get(position).getPrimeCost();
        String type = detailTypeBeanList.get(position).getType();
        String note = detailTypeBeanList.get(position).getNote();
        if (type.equals(AccountBean.TYPE_PAY_OTHER) || type.equals(AccountBean.TYPE_PAY_STOCK)) //支出类型
            count = -count;
        itemHoleder.tvClassifyMoney.setText(count + "");
        itemHoleder.tvClassify.setText(detailTypeBeanList.get(position).getName());
        itemHoleder.tvClassifyDescribe.setText("备注："+note);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_separator,parent,false);
        return new HeaderHoleder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public void onItemDismiss(int position) {
        super.onItemDismiss(position);
        removeItem(position);
    }

    private void removeItem(int position){
        List<AccountBean> list = DbHelper.INSTANCE.getAccountManager().getAccountList(detailTypeBeanList.get(position).getId());
        if(list.isEmpty()) {
            DbHelper.INSTANCE.getDetailTypeManager().deleteDetailItem(detailTypeBeanList.get(position));
            detailTypeBeanList.remove(position);
            notifyItemRemoved(position);
        } else {
            ToastUtil.showShort("该商品已被使用，无法删除");
        }
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

        TextView tvClassify;
        TextView tvTransDetail;
        TextView tvClassifyDescribe;
        TextView tvClassifyMoney;
        View itemBill;

        public ItemHoleder(View itemView) {
            super(itemView);
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

        public HeaderHoleder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {

        }
    }


}
