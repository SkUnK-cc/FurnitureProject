package example.com.furnitureproject.fragment

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import example.com.furnitureproject.R
import example.com.furnitureproject.fragment.adapter.CenterAdapter

class FragmentMe: BaseFragmentKotlin() {

    private val ARG_PARAM1 = "param1"
    private var mRvMine: RecyclerView? = null
    private var mToolbar: Toolbar? = null
    private var mCollapsingToolbar: CollapsingToolbarLayout? = null
    private var mAppBarLayout: AppBarLayout? = null
    private var mParam1: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
        }
    }

    override fun findView(view: View) {
        super.findView(view)
        mRvMine = view.findViewById(R.id.rv_mine)
        mToolbar = view.findViewById(R.id.toolbar)
        mCollapsingToolbar = view.findViewById(R.id.collapsing_toolbar)
        mAppBarLayout = view.findViewById(R.id.appbar)
    }

    override fun initView(rootView: View) {
        mAppBarLayout?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (Math.abs(verticalOffset) >= appBarLayout.totalScrollRange) {
                mCollapsingToolbar?.setTitle("我的")
                //Logger.e("折叠状态");
            } else
                mCollapsingToolbar?.setTitle("")
            //Logger.e("展开状态");
        })
        initRecycleView()
    }

    private fun initRecycleView() {
        val linearLayoutManager = LinearLayoutManager(context)
        mRvMine?.setLayoutManager(linearLayoutManager)
        mRvMine?.setAdapter(CenterAdapter(context))
    }

    override fun initData() {
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_me
    }
}