package example.com.furnitureproject.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import example.com.furnitureproject.R
import example.com.furnitureproject.activity.AnalyzeSetting
import example.com.furnitureproject.eventbus.bean.EventAddSellTrans
import example.com.furnitureproject.eventbus.bean.EventAnalyzeSettingChange
import example.com.furnitureproject.fragment.adapter.AnalyzeItemRvAdapter
import example.com.furnitureproject.fragment.chart.vm.AnalyzeFragmentVM
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener
import lecho.lib.hellocharts.model.PieChartData
import lecho.lib.hellocharts.model.SliceValue
import lecho.lib.hellocharts.util.ChartUtils
import lecho.lib.hellocharts.view.PieChartView
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class FragmentAnalyze: BaseFragmentKotlin(), View.OnClickListener {

    private var vm: AnalyzeFragmentVM? = null

    private var chart: PieChartView? = null
    private var ivAnalyzeSetting: ImageView? = null
    private var tvTitle: TextView? = null
    private var rvAnalyze: RecyclerView? = null
    private var rvAdapter: AnalyzeItemRvAdapter? = null

    private var data: PieChartData? = null
    private var hasLabels = false

    private var hasLabelsOutside = false
    private var hasCenterCircle = false
    private var hasCenterText1 = false
    private var hasCenterText2 = false
    private var isExploded = false
    private var hasLabelForSelected = false
    override fun findView(rootView: View) {
        super.findView(rootView)
        chart = rootView.findViewById(R.id.chart) as PieChartView
        ivAnalyzeSetting = rootView.findViewById(R.id.iv_analyze_setting)
        tvTitle = rootView.findViewById(R.id.tv_title)
        rvAnalyze = rootView.findViewById(R.id.rv_analyze)
    }


    override fun initView(rootView: View) {
        chart?.onValueTouchListener = ValueTouchListener()
        ivAnalyzeSetting?.setOnClickListener(this)
        rvAdapter = AnalyzeItemRvAdapter(R.layout.item_analyze_profit)
    }
    override fun initData() {
        vm = ViewModelProviders.of(this).get(AnalyzeFragmentVM::class.java)
        vm?.getPieData()?.observe(context!!, Observer {
            refreshData(it!!)
        })
        vm?.type?.observe(context!!, Observer {
            tvTitle?.text = it
        })
        vm?.getRvData()?.observe(context!!, Observer {
            rvAdapter?.setNewData(it)
        })

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_analyze
    }

    fun refreshData(values: ArrayList<SliceValue>){
        generateData(values)
    }

    private fun generateData(values: ArrayList<SliceValue>) {
        val numValues = 6

        data = PieChartData(values)
        data?.setHasLabels(true)
//        data?.setHasLabelsOnlyForSelected(hasLabelForSelected)
        data?.setHasLabelsOutside(hasLabelsOutside)
        data?.setHasCenterCircle(false)

        if (isExploded) {
            data?.setSlicesSpacing(24)
        }

        if (hasCenterText1) {
            data?.setCenterText1("Hello!")

            // Get roboto-italic font.
            val tf = Typeface.createFromAsset(activity!!.assets, "Roboto-Italic.ttf")
            data?.setCenterText1Typeface(tf)

            // Get font size from dimens.xml and convert it to sp(library uses sp values).
            data?.setCenterText1FontSize(ChartUtils.px2sp(resources.displayMetrics.scaledDensity,
                    resources.getDimension(R.dimen.pie_chart_text1_size).toInt()))
        }

        if (hasCenterText2) {
            data?.setCenterText2("Charts (Roboto Italic)")

            val tf = Typeface.createFromAsset(activity!!.assets, "Roboto-Italic.ttf")

            data?.setCenterText2Typeface(tf)
            data?.setCenterText2FontSize(ChartUtils.px2sp(resources.displayMetrics.scaledDensity,
                    resources.getDimension(R.dimen.pie_chart_text2_size).toInt()))
        }

        chart?.setPieChartData(data)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.iv_analyze_setting -> {
                val intent = Intent(context,AnalyzeSetting::class.java)
                startActivity(intent)
            }
        }
    }
    private inner class ValueTouchListener : PieChartOnValueSelectListener {

        override fun onValueSelected(arcIndex: Int, value: SliceValue) {
            Toast.makeText(activity, "Selected: $value", Toast.LENGTH_SHORT).show()
        }

        override fun onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSettingChange(event: EventAnalyzeSettingChange){
        vm?.updateSetting(event)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSettingChange(event: EventAddSellTrans){
        if(vm?.type?.value == AnalyzeSetting.profitAnalyze){
            vm?.getProfitData()
        }
    }
}