package example.com.furnitureproject.fragment

import android.support.design.widget.TabLayout
import android.view.KeyEvent
import android.view.View
import android.widget.AdapterView
import example.com.furnitureproject.R
import example.com.furnitureproject.constant.Extra
import example.com.furnitureproject.db.DbHelper
import example.com.furnitureproject.db.bean.AccountBean
import example.com.furnitureproject.eventbus.bean.ChartClassifyEvent
import example.com.furnitureproject.fragment.adapter.BaseFragmentPagerAdapter
import example.com.furnitureproject.fragment.chart.ChartPopWindowAdapter
import example.com.furnitureproject.fragment.chart.ChartTypeFragment
import example.com.furnitureproject.utils.AccListUtil
import example.com.furnitureproject.utils.DensityUtil
import example.com.furnitureproject.view.ListPopWindow
import example.com.furnitureproject.view.SliderLayout
import kotlinx.android.synthetic.main.fragment_chart.*
import kotlinx.android.synthetic.main.layout_toolbar_expend_income.*
import org.greenrobot.eventbus.EventBus
import java.util.*

class FragmentChartRR: BaseFragment() {
    private val ARG_PARAM1 = "param1"
//    @BindView(R.id.ll_title_return)
//    internal var ll_title_return: FrameLayout? = null
//    @BindView(R.id.rb_expend)
//    internal var rb_expend: RadioButton? = null
//    @BindView(R.id.rb_income)
//    internal var rb_income: RadioButton? = null
//    @BindView(R.id.vp_chart)
//    internal var vp_chart: ViewPager? = null
//    @BindView(R.id.tab_year_month)
//    internal var tab_year_month: TabLayout? = null
//    //    @BindView(R.id.tab_period)
//    //    TabLayout mTabPeriod;
//    @BindView(R.id.slider_layout)
//    internal var slider_layout: SliderLayout? = null
//    @BindView(R.id.tv_classify)
//    internal var tv_classify: TextView? = null
    private var mListPopWindow: ListPopWindow? = null
    private val mPopData = ArrayList<AccountBean>()
    private val mTitleList = ArrayList<String>()
    private val mFragmentList = ArrayList<ChartTypeFragment>()
    private val mAccountType = AccountBean.TYPE_ALL

    private var mDetailType = "DETAIL_TYPE_DEFAULT"

    override fun onResume() {
        super.onResume()
        view?.setFocusableInTouchMode(true)
        view?.requestFocus()
        view?.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                if (mListPopWindow != null && mListPopWindow!!.isShowing()) {
                    mListPopWindow!!.dismiss()
                    return@OnKeyListener true
                }
            }
            false
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_chart
    }

    override fun initData() {
        initPopData()
        initChartData()
    }

    private fun initChartData() {
        mTitleList.clear()
        mFragmentList.clear()
        mTitleList.add("周")
        mTitleList.add("月")
        mTitleList.add("年")

        mFragmentList.add(ChartTypeFragment.newInstance(ChartTypeFragment.TYPE_WEEK))
        mFragmentList.add(ChartTypeFragment.newInstance(ChartTypeFragment.TYPE_MONTH))
        mFragmentList.add(ChartTypeFragment.newInstance(ChartTypeFragment.TYPE_YEAR))
    }

    private fun initPopData() {
        mPopData.clear()
        addHeaderToPop()

        val maxDate = DbHelper.getMaxDate()
        val minDate = DbHelper.getMinDate()
        if (minDate != null && maxDate != null) {
            val accountList = DbHelper.getAccountManager().getAccountList(mAccountType, minDate, maxDate)
            mPopData.addAll(AccListUtil.removeRepeat(accountList))
        }
    }

    private fun addHeaderToPop() {
        val bean = AccountBean()
        bean.type = AccountBean.TYPE_ALL
        bean.setPicRes(R.drawable.classify_baby)
        mPopData.add(bean)
    }

    override fun initView(view: View) {
        ll_title_return?.visibility = View.GONE
        initViewPager()
    }

    override fun init(view: View){
        initDialog()
    }

    private fun initViewPager() {

        // 注意使用的是：getSupportFragmentManager
        val adapter = BaseFragmentPagerAdapter(childFragmentManager, mFragmentList, mTitleList)
        vp_chart!!.adapter = adapter
        // 设置ViewPager最大缓存的页面个数(cpu消耗少)
        vp_chart!!.offscreenPageLimit = 1
        //vpContent.addOnPageChangeListener(this);
        vp_chart!!.currentItem = 0
        //adapter.notifyDataSetChanged();
        tab_year_month!!.tabMode = TabLayout.MODE_FIXED
        tab_year_month!!.setupWithViewPager(vp_chart)
        //vp_chart.clearOnPageChangeListeners();

        vp_chart!!.addOnPageChangeListener(SliderLayout.SliderOnPageChangeListener(tab_year_month, slider_layout))
    }


    fun onViewClicked() {

        if (mPopData == null || mPopData.size < 2)
            return
        if (mListPopWindow != null && mListPopWindow!!.isShowing())
            return
        mListPopWindow = ListPopWindow(context!!, mPopData.size)
        mListPopWindow!!.setAnchorView(tv_classify)
        mListPopWindow!!.setHorizontalOffset(DensityUtil.dip2px(5f))
        mListPopWindow!!.setAdapter(ChartPopWindowAdapter(context, mPopData))
        mListPopWindow!!.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            //Logger.e("click " + position);
            tv_classify!!.setText(mPopData[position].type)
            mListPopWindow!!.dismiss()

            //发送消息通知chartdetailfragment
            val message: String
            if (position == 0)
                message = Extra.DETAIL_TYPE_DEFAULT
            else
                message = mPopData[position].type
            if (mDetailType == message)
                return@OnItemClickListener
            mDetailType = message
            EventBus.getDefault().post(ChartClassifyEvent(message))
        })
        mListPopWindow!!.show()
    }

    fun getDetailType(): String {
        return mDetailType
    }

//    @Subscribe(threadMode = ThreadMode.POSTING)
//    public void onEvent(List<AccountModel> chartList) {
//        //Logger.e("收到eventbus");
//    }
}