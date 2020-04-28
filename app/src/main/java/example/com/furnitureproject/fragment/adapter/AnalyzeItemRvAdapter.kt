package example.com.furnitureproject.fragment.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import example.com.furnitureproject.R
import example.com.furnitureproject.fragment.chart.vm.AnalyzeFragmentVM

class AnalyzeItemRvAdapter(layoutId: Int): BaseQuickAdapter<AnalyzeFragmentVM.PieItem, BaseViewHolder>(layoutId) {


    override fun convert(holder: BaseViewHolder?, item: AnalyzeFragmentVM.PieItem?) {
        holder?.setText(R.id.tv_analyze_name,item?.name)
        holder?.getView<ImageView>(R.id.iv_analyze_color)?.setBackgroundColor(item?.color!!)
        holder?.setText(R.id.tv_analyze_describe,item?.note)
        holder?.setText(R.id.tv_analyze_money,item?.profit.toString())
    }
}