package example.com.furnitureproject.fragment.chart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import example.com.furnitureproject.R;
import example.com.furnitureproject.db.bean.AccountBean;

/**
 * @author luo
 * @date 2017/9/18
 */
public class ChartPopWindowAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<AccountBean> mPopList;

    public ChartPopWindowAdapter(Context context, ArrayList<AccountBean> popWindowBeanList) {
        this.mContext = context;
        this.mPopList = popWindowBeanList;
    }

    @Override
    public int getCount() {
        return mPopList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView ==null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_chart_popup, null);
            holder = new ViewHolder();
            holder.mTvClassify = (TextView) convertView.findViewById(R.id.tv_classify);
            holder.mIvClassify = (ImageView) convertView.findViewById(R.id.iv_classify);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTvClassify.setText(mPopList.get(position).getName());
        holder.mIvClassify.setImageResource(mPopList.get(position).getPicRes());
        return convertView;
    }

    class ViewHolder {
        TextView mTvClassify;
        ImageView mIvClassify;
    }
}
