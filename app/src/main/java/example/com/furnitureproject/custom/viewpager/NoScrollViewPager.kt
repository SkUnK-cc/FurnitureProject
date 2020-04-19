package example.com.furnitureproject.custom.viewpager

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class NoScrollViewPager: ViewPager{

    private var scrollEnable = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context,attrs)

    fun setScrollEnable(enable: Boolean){
        scrollEnable = enable
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if(!scrollEnable){
            return false
        }else{
            return super.onTouchEvent(ev)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (scrollEnable){
            return false
        }else{
            return super.onInterceptTouchEvent(ev)
        }    }
}