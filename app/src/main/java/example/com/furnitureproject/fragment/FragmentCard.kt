package example.com.furnitureproject.fragment

import android.graphics.Typeface
import android.view.View
import android.widget.Toast
import example.com.furnitureproject.R
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener
import lecho.lib.hellocharts.model.PieChartData
import lecho.lib.hellocharts.model.SliceValue
import lecho.lib.hellocharts.util.ChartUtils
import lecho.lib.hellocharts.view.PieChartView
import java.util.*

class FragmentCard: BaseFragmentKotlin() {

    private var chart: PieChartView? = null
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

    }
    override fun initView(rootView: View) {
        chart?.onValueTouchListener = ValueTouchListener()
    }

    override fun initData() {
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_card
    }

    private fun generateData() {
        val numValues = 6

        val values = ArrayList<SliceValue>()
        for (i in 0 until numValues) {
            val sliceValue = SliceValue(Math.random().toFloat() * 30 + 15, ChartUtils.pickColor())
            values.add(sliceValue)
        }

        data = PieChartData(values)
        data?.setHasLabels(hasLabels)
        data?.setHasLabelsOnlyForSelected(hasLabelForSelected)
        data?.setHasLabelsOutside(hasLabelsOutside)
        data?.setHasCenterCircle(hasCenterCircle)

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
    private inner class ValueTouchListener : PieChartOnValueSelectListener {

        override fun onValueSelected(arcIndex: Int, value: SliceValue) {
            Toast.makeText(activity, "Selected: $value", Toast.LENGTH_SHORT).show()
        }

        override fun onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }
}