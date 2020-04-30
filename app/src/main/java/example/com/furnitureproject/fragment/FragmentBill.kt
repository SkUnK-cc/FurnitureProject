package example.com.furnitureproject.fragment

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView
import com.marshalchen.ultimaterecyclerview.itemTouchHelper.SimpleItemTouchHelperCallback
import com.marshalchen.ultimaterecyclerview.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration
import example.com.furnitureproject.R
import example.com.furnitureproject.db.DbHelper
import example.com.furnitureproject.db.bean.AccountBean
import example.com.furnitureproject.db.gen.AccountBeanDao
import example.com.furnitureproject.eventbus.bean.EventAddOtherTrans
import example.com.furnitureproject.eventbus.bean.EventAddSellTrans
import example.com.furnitureproject.eventbus.bean.EventAddStockTrans
import example.com.furnitureproject.fragment.chart.adapter.BillAdapter
import example.com.furnitureproject.utils.TimeUtil
import example.com.furnitureproject.utils.ToastUtil
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*

class FragmentBill: BaseFragmentKotlin(), View.OnClickListener{
    private val ARG_PARAM1 = "param1"
    private val CONTRACT_NOT = 0
    private val CONTRACT_ING = 1
    private val CONTRACT_FINISH = 2
    private var mIsFirst = true
    var mTvBudgetMonth          : TextView? = null
    var mTvBudgetMonthDescribe  : TextView? = null
    var mTvExpend               : TextView? = null
    var mTvExpendDescribe       : TextView? = null
    var mTvIncome               : TextView? = null
    var mTvIncomeDescribe       : TextView? = null
    var mToolbar                : Toolbar? = null
    var mLlTitleContract        : FrameLayout? = null
    var mTvTitleTime            : TextView? = null
    var mLlTitleLeft            : FrameLayout? = null
    var mLlTitleRight           : FrameLayout? = null
    var mUltimateRecyclerView   : UltimateRecyclerView? = null
    var mAppBar                 : AppBarLayout? = null

    private var mParam1: String? = null
    private var mBillAdapter: BillAdapter? = null
    private val mAccountList = ArrayList<AccountBean>()
    private var mCurrentDate: Date? = null



    fun newInstance(param1: String): FragmentBill {
        val fragment: FragmentBill = FragmentBill()
        val args = Bundle()
        args.putString(ARG_PARAM1, param1)
        fragment.arguments = args
        return fragment
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_bill
    }

    override fun initData() {
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
        }
        initTitleData()
    }

    private fun initTitleData() {
        val calendar = Calendar.getInstance()
        val format = SimpleDateFormat("MM月dd日")
        mTvTitleTime!!.text = format.format(calendar.time)
        mCurrentDate = calendar.time
        //mDbManager.queryRaw("where AGE>?","10");
        //        String first = TimeUtil.date2String(TimeUtil.getFirstDayOfMonth(mCurrentDate), "yyyy-MM-dd日   HH:mm");
        //        String end = TimeUtil.date2String(TimeUtil.getLastDayOfMonth(mCurrentDate), "yyyy-MM-dd日   HH:mm");
        //        Logger.e("本月第一天：" + first);
        //        Logger.e("本月最末天：" + end);
        mAccountList.addAll(getAccountList(0, mCurrentDate)!!)
    }

    override fun findView(view: View) {
        super.findView(view)
        mTvBudgetMonth = view.findViewById(R.id.tv_budget_month)
        mTvBudgetMonthDescribe = view.findViewById(R.id.tv_budget_month_describe)
        mTvExpend = view.findViewById(R.id.tv_expend)
        mTvExpendDescribe = view.findViewById(R.id.tv_expend_describe)
        mTvIncome = view.findViewById(R.id.tv_income)
        mTvIncomeDescribe = view.findViewById(R.id.tv_income_describe)
        mToolbar = view.findViewById(R.id.toolbar)
        mLlTitleContract = view.findViewById(R.id.ll_title_contract)
        mTvTitleTime = view.findViewById(R.id.tv_title_time)
        mLlTitleLeft = view.findViewById(R.id.ll_title_left)
        mLlTitleRight = view.findViewById(R.id.ll_title_right)
        mUltimateRecyclerView = view.findViewById(R.id.ultimate_recycler_view)
        mAppBar = view.findViewById(R.id.app_bar)
        mTvBudgetMonth?.setOnClickListener(this)
        mTvBudgetMonthDescribe?.setOnClickListener(this)
        mTvExpend?.setOnClickListener(this)
        mTvExpendDescribe?.setOnClickListener(this)
        mTvIncome?.setOnClickListener(this)
        mTvIncomeDescribe?.setOnClickListener(this)
        mToolbar?.setOnClickListener(this)
        mLlTitleContract?.setOnClickListener(this)
        mTvTitleTime?.setOnClickListener(this)
        mLlTitleLeft?.setOnClickListener(this)
        mLlTitleRight?.setOnClickListener(this)
        mUltimateRecyclerView?.setOnClickListener(this)
        mAppBar?.setOnClickListener(this)
    }

    override fun initView(view: View) {
        initToolbar()
        setTitleView()
        initRecycleView()
    }


    private fun setTitleView() {
        mTvExpendDescribe!!.text = (mCurrentDate!!.month + 1).toString() + "月支出"
        mTvIncomeDescribe!!.text = (mCurrentDate!!.month + 1).toString() + "月收入"
        mTvBudgetMonthDescribe!!.text = (mCurrentDate!!.month + 1).toString() + "月预算"
        if (mAccountList.size > 0) {
            var sumExpend = 0f
            var sumIncome = 0f
            for (accountModel in mAccountList) {
                val type = accountModel.type
                if (type.equals(AccountBean.TYPE_PAY_OTHER) || type.equals(AccountBean.TYPE_PAY_STOCK))
                //支出
                    sumExpend += accountModel.price
                if (type.equals(AccountBean.TYPE_INCOME_SELL))
                //收入
                    sumIncome += accountModel.price
            }
            mTvExpend!!.text = sumExpend.toString()
            mTvIncome!!.text = sumIncome.toString()
            //mTvBudgetMonth.setText();
        } else {
            mTvExpend!!.text = "——"
            mTvIncome!!.text = "——"
        }
    }

    private fun initRecycleView() {
        val linearLayoutManager = LinearLayoutManager(context)
        mUltimateRecyclerView!!.layoutManager = linearLayoutManager
        mBillAdapter = BillAdapter(mAccountList)
        mBillAdapter!!.setOnItemClickListener { view, position ->
            //                val intent = Intent(context, CalendarActivity::class.java)
//                intent.putExtra(Extra.ACCOUNT_DATE, mCurrentDate!!.time)
//                activity.startActivity(intent)
            //ToastUtil.showShort(getActivity(), position + "");
        }

        //悬浮头部布局需要加入
        val headersDecor = StickyRecyclerHeadersDecoration(mBillAdapter)
        mUltimateRecyclerView!!.addItemDecoration(headersDecor)
        //设置下拉刷新
        mUltimateRecyclerView!!.setDefaultSwipeToRefreshColorScheme(resources.getColor(R.color.colorPrimary))
        mUltimateRecyclerView!!.setDefaultOnRefreshListener {
            Handler().postDelayed({
                changeList(0)
                setTitleView()
                ToastUtil.showShort("数据已更新")
                //linearLayoutManager.scrollToPosition(0);
            }, 1000)
        }
        mUltimateRecyclerView!!.setAdapter(mBillAdapter)
        mUltimateRecyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //如果滑动到第一条且完全可见则展开appbarLayout
                    val visiblePosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
                    if (visiblePosition == 0) {
                        mAppBar!!.setExpanded(true)
                    }
                }
            }
        })
        val callback = object: SimpleItemTouchHelperCallback(mBillAdapter){
            override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
                val drag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                val swipeFlags = ItemTouchHelper.LEFT
//                return super.getMovementFlags(recyclerView, viewHolder)
                return ItemTouchHelper.Callback.makeMovementFlags(drag,swipeFlags)
            }

            override fun isItemViewSwipeEnabled(): Boolean {
                return true
            }

            override fun isLongPressDragEnabled(): Boolean {
                return false
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(mUltimateRecyclerView?.mRecyclerView)
        mUltimateRecyclerView!!.setEmptyView(R.layout.rv_empty_bill, UltimateRecyclerView.EMPTY_CLEAR_ALL)
        if (mAccountList.size == 0)
            mUltimateRecyclerView!!.showEmptyView()

        mIsFirst = false
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(mToolbar)
    }

    /**
     * 分页查询
     *
     * @param offSet      设置开始页
     * @param currentDate
     */
    private fun getAccountList(offSet: Int, currentDate: Date?): List<AccountBean>? {
        return DbHelper.getAccountManager().getAbstractDao().queryBuilder()
                .where(AccountBeanDao.Properties.Time.between(TimeUtil.getFirstDayOfMonth(currentDate).time, TimeUtil.getEndDayOfMonth(currentDate).time))
                .orderAsc(AccountBeanDao.Properties.Time)
                .offset(offSet * 20)
                .limit(20)
                .list()
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.ll_title_contract -> {
//                if (getContractState() == CONTRACT_NOT)
//                    startActivity(Intent(context, NewContractActivity::class.java))
//                if (getContractState() == CONTRACT_ING)
//                    startActivity(Intent(context, CalendarActivity::class.java))
//                if (getContractState() == CONTRACT_FINISH)
                //startActivity(new Intent(context, CalendarContractActivity.class));
            }
            R.id.tv_title_time -> {
            }
            R.id.ll_title_left -> {
                changeList(-1)
                setTitleView()
            }
            R.id.ll_title_right -> {
                changeList(1)
                setTitleView()
            }
            R.id.ll_expend_detail -> {
//                val intent = Intent(context, BillDetailActivity::class.java)
//                intent.putExtra(Extra.ACCOUNT_DATE, mCurrentDate!!.time)
//                intent.putExtra(Extra.ACCOUNT_TYPE, 1)
//                startActivity(intent)
            }
            R.id.ll_income_detail -> {
//                val intentIn = Intent(context, BillDetailActivity::class.java)
//                intentIn.putExtra(Extra.ACCOUNT_DATE, mCurrentDate!!.time)
//                intentIn.putExtra(Extra.ACCOUNT_TYPE, 2)
//                startActivity(intentIn)
            }
//            R.id.tv_budget_month -> startActivity(Intent(context, BudgetActivity::class.java))
//            R.id.tv_budget_month_describe -> startActivity(Intent(context, BudgetActivity::class.java))
        }//Logger.e(mAccountList.size() + "");
    }

    /**
     * 前后一个月数据
     *
     * @param monthDistance example ：-1为前一月， 1为后一月
     */
    private fun changeList(monthDistance: Int) {
        //当前月
        if (monthDistance > 0 && TimeUtil.date2String(Date(), "yy年MM月")
                        .equals(TimeUtil.date2String(mCurrentDate, "yy年MM月")))
            return
        if (monthDistance != 0) {
            mCurrentDate = TimeUtil.getMonthAgo(mCurrentDate, monthDistance)
            mTvTitleTime!!.setText(TimeUtil.date2String(mCurrentDate, "yy年MM月"))
        }
        val accList = getAccountList(0, mCurrentDate)
        if (accList != null) {
            mAccountList.clear()
            mAccountList.addAll(accList)
            //mBillAdapter.onBindHeaderViewHolder();
            mBillAdapter!!.notifyDataSetChanged()
            if (accList.size == 0) {
                if (mUltimateRecyclerView != null)
                    mUltimateRecyclerView!!.showEmptyView()
            } else {
                if (mUltimateRecyclerView != null)
                    mUltimateRecyclerView!!.hideEmptyView()
            }
        }
    }

    /**
     * TODO
     * 获取契约状态
     *
     * @return 0：尚未签订契约  1：契约期间  2：本期契约成功
     */
    private fun getContractState(): Int {
        return CONTRACT_ING
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSellEvent(event: EventAddSellTrans) {
        changeList(0)
        setTitleView()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onStockEvent(event: EventAddStockTrans) {
        changeList(0)
        setTitleView()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onOtherEvent(event: EventAddOtherTrans) {
        changeList(0)
        setTitleView()
    }
}