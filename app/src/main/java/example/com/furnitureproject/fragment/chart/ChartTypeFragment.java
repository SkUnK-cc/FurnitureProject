package example.com.furnitureproject.fragment.chart;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import example.com.furnitureproject.R;
import example.com.furnitureproject.db.DbHelper;
import example.com.furnitureproject.db.bean.AccountBean;
import example.com.furnitureproject.eventbus.bean.EventAddOtherTrans;
import example.com.furnitureproject.eventbus.bean.EventAddSellTrans;
import example.com.furnitureproject.eventbus.bean.EventAddStockTrans;
import example.com.furnitureproject.eventbus.bean.EventChartTypeChange;
import example.com.furnitureproject.fragment.BaseFragment;
import example.com.furnitureproject.fragment.adapter.BaseFragmentPagerAdapter;
import example.com.furnitureproject.utils.AccListUtil;
import example.com.furnitureproject.utils.TimeUtil;

/**
 * 年月日
 * 图表
 */
public class ChartTypeFragment extends BaseFragment {
    private static final String TIME_TYPE = "timeType";
    private static final String ACCOUNT_TYPE = "accountType";
    public static final int TYPE_WEEK = 1;
    public static final int TYPE_MONTH = 2;
    public static final int TYPE_YEAR = 3;
    TabLayout mTabDettail;
    ViewPager mVpChart;
    LinearLayout mLinEmpty;

    private int mType;
    private String mAccountType = AccountBean.TYPE_PAY_STOCK;
    private List<AccountBean> mDetailTypeList = new ArrayList<>();

    private ArrayList<String> mTitleList = new ArrayList<>();
    private ArrayList<ChartDetailFragment> mFragmentList = new ArrayList<>();

    public ChartTypeFragment() {
        // Required empty public constructor
    }


    public static ChartTypeFragment newInstance(int param1,String accountType) {
        ChartTypeFragment fragment = new ChartTypeFragment();
        Bundle args = new Bundle();
        args.putInt(TIME_TYPE, param1);
        args.putString(ACCOUNT_TYPE,accountType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt(TIME_TYPE);
            mAccountType = getArguments().getString(ACCOUNT_TYPE);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            initChartData();
//            //消息发送到fragmentChart，获取分类列表
//            EventBus.getDefault().post(mDetailTypeList);
//        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_chart_type;
    }

    @Override
    public void initData() {
        initChartData();
    }


    @Override
    public void init(@NotNull View view) {
        super.init(view);
        mTabDettail = view.findViewById(R.id.tab_dettail);
        mVpChart = view.findViewById(R.id.vp_chart);
        mLinEmpty = view.findViewById(R.id.lin_empty);
    }

    @Override
    public void initView(View view) {
        initViewPager(mFragmentList, mTitleList);
        initListener();

    }

    private void initListener() {
        //mTabDettail.getSelectedTabPosition()
        mVpChart.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                mDetailTypeList = AccListUtil.removeRepeat(mFragmentList.get(position).getTypeListData());
//                //消息发送到fragmentChart，获取分类列表
//                EventBus.getDefault().post(mDetailTypeList);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initChartData() {
        mTitleList.clear();
        mFragmentList.clear();


        Date maxDate = DbHelper.INSTANCE.getMaxDate();
        Date minDate = DbHelper.INSTANCE.getMinDate();
        if (minDate == null || maxDate == null) {
            mVpChart.setVisibility(View.GONE);
            mLinEmpty.setVisibility(View.VISIBLE);
            return;
        }else {
            mVpChart.setVisibility(View.VISIBLE);
            mLinEmpty.setVisibility(View.GONE);
        }

        List<AccountBean> accountList = DbHelper.INSTANCE.getAccountManager().getAccountList(mAccountType, minDate, maxDate);
        mDetailTypeList = AccListUtil.removeRepeat(accountList);
        float maxValue = getMaxValue(accountList);

        switch (mType) {
            case TYPE_WEEK:
                if (maxDate != null && minDate != null) {
                    int minWeek = TimeUtil.getWeekOfYear(minDate);
                    int maxWeek = TimeUtil.getWeekOfYear(maxDate);
                    for (int i = minWeek; i <= maxWeek; i++) {
                        mTitleList.add(i + "周");
                        mFragmentList.add(ChartDetailFragment.newInstance(TYPE_WEEK, i, maxValue));
                    }
                    initViewPager(mFragmentList, mTitleList);
                }

                break;
            case TYPE_MONTH:
                if (maxDate != null && minDate != null) {
                    int minMonth = TimeUtil.getMonthOfYear(minDate);
                    int maxMonth = TimeUtil.getMonthOfYear(maxDate);
                    for (int i = minMonth; i <= maxMonth; i++) {
                        mTitleList.add((i + 1) + "月");
                        mFragmentList.add(ChartDetailFragment.newInstance(TYPE_MONTH, i, maxValue));
                    }
                    initViewPager(mFragmentList, mTitleList);
                }

                break;
            case TYPE_YEAR:
                if (maxDate != null && minDate != null) {
                    int minYear = TimeUtil.getYear(minDate);
                    int maxYear = TimeUtil.getYear(maxDate);
                    for (int i = minYear; i <= maxYear; i++) {
                        mTitleList.add((i) + "年");
                        mFragmentList.add(ChartDetailFragment.newInstance(TYPE_YEAR, i, maxValue));
                    }
                    initViewPager(mFragmentList, mTitleList);
                }
                break;
            default:
                break;
        }

    }

    public List<AccountBean> getTypeListData() {
        return mDetailTypeList;
    }


    /**
     * 获取时间段类记账金额最大值 （以天为单位）
     *
     * @return
     */
    private float getMaxValue(List<AccountBean> accountList) {

        ArrayList<Float> listFloat = new ArrayList<>();
        if (accountList == null || accountList.size() == 0) { //天数为0
            return 0f;
        } else {
            float sumDayCount = 0f;
            int day = TimeUtil.getDayOfYear(new Date(accountList.get(0).getTime()));
            for (AccountBean accountModel : accountList) {
                int dayTemp = TimeUtil.getDayOfYear(new Date(accountModel.getTime()));
                if (dayTemp != day) { //如果不是同一天
                    listFloat.add(sumDayCount);
                    day = dayTemp;
                    sumDayCount = 0f;
                }
                //如果是同一天则相加
                sumDayCount += accountModel.getPrice();
            }
            //循环最后一轮数据还没加入list
            listFloat.add(sumDayCount);
            Float maxValue = Collections.max(listFloat);
            //Logger.e(maxValue + ":" + maxValue);
            return maxValue;
        }
    }


    /**
     * @param mFragmentList
     * @param mTitleList
     */
    private void initViewPager(ArrayList<ChartDetailFragment> mFragmentList, ArrayList<String> mTitleList) {
        BaseFragmentPagerAdapter adapter = new BaseFragmentPagerAdapter(getChildFragmentManager(), mFragmentList, mTitleList);
        mVpChart.setAdapter(adapter);
        // 设置ViewPager最大缓存的页面个数(cpu消耗少)
        mVpChart.setOffscreenPageLimit(1);
        mVpChart.setCurrentItem(mTitleList.size() - 1);
        //vpContent.addOnPageChangeListener(this);
        //vpContent.setCurrentItem(0);
        //adapter.notifyDataSetChanged();
        if (mTitleList.size() < 6)
            mTabDettail.setTabMode(TabLayout.MODE_FIXED);
        else
            mTabDettail.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabDettail.setupWithViewPager(mVpChart);
        //TabLayoutIndicator.setIndicatorWithTextWidth(mTabDettail);
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的

    }

    private void refresh() {
        //if(mFragmentList.isEmpty()) {
            initData();
//            mVpChart.getAdapter().notifyDataSetChanged();
        //}
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    void onSellAdd(EventAddSellTrans e){
        refresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    void onStockAdd(EventAddStockTrans e){
        refresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    void onOtherAdd(EventAddOtherTrans e){
        refresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    void onChange(EventChartTypeChange e){
        mAccountType = e.getType();
    }

}
