package example.com.furnitureproject.fragment.addaccount

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class AccountFragmentAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    private var list = mutableListOf<Fragment>()
    private var names = mutableListOf<String>()

    fun setData(data: List<Fragment>,nameList: List<String>){
        list.clear()
        list.addAll(data)
        names.clear()
        names.addAll(nameList)
    }

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return names[position]
    }
}