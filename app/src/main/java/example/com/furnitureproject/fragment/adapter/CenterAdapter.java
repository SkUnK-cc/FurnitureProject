package example.com.furnitureproject.fragment.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import example.com.furnitureproject.R;
import example.com.furnitureproject.activity.GoodsListActivity;
import example.com.furnitureproject.activity.OtherPayoutListActivity;
import example.com.furnitureproject.constant.CenterRes;
import example.com.furnitureproject.db.DatabaseDump;
import example.com.furnitureproject.fragment.chart.BaseRecycleAdapter;
import example.com.furnitureproject.utils.DensityUtil;
import example.com.furnitureproject.utils.ToastUtil;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 单选模式
 *
 * @author luo
 * @date 2017/8/14
 */
public class CenterAdapter extends BaseRecycleAdapter {


    private static final int TYPE_END = 1;
    private static final int TYPE_ITEM = 0;
    private static int TYPE = TYPE_ITEM;


    private Context context;


    public CenterAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM)
            return new ItemHoleder(LayoutInflater.from(context).inflate(R.layout.item_center, parent, false));
        else if (viewType == TYPE_END) {
            TextView textView = new TextView(context);
            textView.setText("退出登录");
            textView.setTextColor(context.getResources().getColor(R.color.colorTextRed));
            textView.setTextSize(15);
            textView.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, DensityUtil.dip2px(30), 0, 0);
            textView.setLayoutParams(layoutParams);
            return new EndHoleder(textView);
            //return new EndHoleder(LayoutInflater.from(context).inflate(R.layout.item_card_add, parent, false));
        }

        else
            return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        switch (getItemViewType(position)) {
            case TYPE_END:

                break;
            case TYPE_ITEM:
                ItemHoleder itemHoleder = (ItemHoleder) holder;
                itemHoleder.mTvCenter.setText(CenterRes.NAMES[position]);
                itemHoleder.mIvCenter.setImageResource(CenterRes.ICONS[position]);
                itemHoleder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (position) {
                            case 0 :
                                Intent intentToGoodList = new Intent(context, GoodsListActivity.class);
                                context.startActivity(intentToGoodList);
                                break;
                            case 1 :
                                Intent intentToOtherList = new Intent(context, OtherPayoutListActivity.class);
                                context.startActivity(intentToOtherList);
                                break;
                            case 2 :
                                DatabaseDump.dumpAccountToFile("AccountFilt.xls").subscribe(new Observer<String>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(String s) {
                                        ToastUtil.showShort(s);
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });

                                break;
                            case 3 :
//                                startActivity(RemindManagerActivity.class);
                                break;
                            case 4 :
//                                startActivity(BudgetActivity.class);
                                break;
                            default :
                                break;
                        }
                    }
                });
                break;
            default:
                break;
        }
    }


    @Override
    public int getItemCount() {
        return CenterRes.NAMES.length + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == CenterRes.NAMES.length)
            TYPE = TYPE_END;
        else
            TYPE = TYPE_ITEM;
        return TYPE;
    }



    private void startActivity(Class<?> cls) {

    }


    class ItemHoleder extends RecyclerView.ViewHolder {

        ImageView mIvCenter;
        TextView mTvCenter;
        public ItemHoleder(View itemView) {
            super(itemView);
            mIvCenter = itemView.findViewById(R.id.iv_center);
            mTvCenter = itemView.findViewById(R.id.tv_center);
        }
    }

    class EndHoleder extends RecyclerView.ViewHolder {
        public EndHoleder(View itemView) {
            super(itemView);
        }
    }

}
