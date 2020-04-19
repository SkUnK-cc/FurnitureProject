package example.com.furnitureproject.view.navigation

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class BottomAdapter(val mContext: Context, fm: FragmentManager): FragmentPagerAdapter(fm) {

    private var list = mutableListOf<FragmentItem>()

    override fun getItem(position: Int): Fragment {
        var fmItem = list[position]
        if(fmItem.fragment==null){
            fmItem.fragment = Fragment.instantiate(mContext,fmItem.clz.name,null)
        }
        return fmItem.fragment
    }

    override fun getCount(): Int {
        return list.size
    }

    fun setData(list:  ArrayList<FragmentItem>){
        this.list.clear()
        this.list.addAll(list)
    }
}