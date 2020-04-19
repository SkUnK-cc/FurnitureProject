package example.com.furnitureproject.custom.recyviewpager

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View

class AutoGridLayoutManager: GridLayoutManager {

    private var measureWidget: Int = 0
    private var measureHeight: Int = 0

    constructor(context: Context,spanCount:Int) : super(context,spanCount)
    constructor(context: Context,attributeSet: AttributeSet,defStyleAttr:Int,defStyleRes: Int) : super(context,attributeSet,defStyleAttr,defStyleRes)
    constructor(context: Context,spanCount:Int,orientation: Int,reverseLayout: Boolean) : super(context,spanCount,orientation,reverseLayout)

    // 决定 RecyclerView 的宽高
    override fun onMeasure(recycler: RecyclerView.Recycler?, state: RecyclerView.State?, widthSpec: Int, heightSpec: Int) {
        if(measureHeight<=0){
            var view = recycler?.getViewForPosition(0)
            if(view!=null){
                measureChild(view,widthSpec,heightSpec)
                measureWidget = View.MeasureSpec.getSize(widthSpec)
                measureHeight = view.measuredHeight * spanCount     // 单个View的高度乘以行数
            }
        }
        setMeasuredDimension(measureWidget,measureHeight)
    }
}