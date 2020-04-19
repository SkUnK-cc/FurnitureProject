package example.com.furnitureproject.fragment.addaccount

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class AccountFragmentAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    private var list = mutableListOf<Fragment>()

    fun setData(data: List<Fragment>){
        list.clear()
        list.addAll(data)
    }

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }
}