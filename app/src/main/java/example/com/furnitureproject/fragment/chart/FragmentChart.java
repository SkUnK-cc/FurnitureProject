package example.com.furnitureproject.fragment.chart;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import example.com.furnitureproject.R;
import example.com.furnitureproject.db.DbHelper;
import example.com.furnitureproject.db.bean.AccountBean;
import example.com.furnitureproject.eventbus.bean.ChartClassifyEvent;
import example.com.furnitureproject.eventbus.bean.EventAddOtherTrans;
import example.com.furnitureproject.eventbus.bean.EventAddSellTrans;
import example.com.furnitureproject.eventbus.bean.EventAddStockTrans;
import example.com.furnitureproject.eventbus.bean.EventChartTypeChange;
import example.com.furnitureproject.fragment.BaseFragment;
import example.com.furnitureproject.fragment.adapter.BaseFragmentPagerAdapter;
import example.com.furnitureproject.utils.AccListUtil;
import example.com.furnitureproject.utils.DensityUtil;
import example.com.furnitureproject.view.ListPopWindow;
import example.com.furnitureproject.view.SliderLayout;

public class FragmentChart extends BaseFragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    FrameLayout mLlTitleReturn;
    RadioGroup group;
    RadioButton mRbExpend;
    RadioButton mRbIncome;
    ViewPager mVpChart;
    TabLayout mTabYearMonth;
    //    @BindView(R.id.tab_period)
//    TabLayout mTabPeriod;
    SliderLayout mSliderLayout;
    TextView mTvClassify;
    private ListPopWindow mListPopWindow;
    private ArrayList<AccountBean> mPopData = new ArrayList<>();
    private ArrayList<String> mTitleList = new ArrayList<>();
    private ArrayList<ChartTypeFragment> mFragmentList = new ArrayList<>();
    private String mAccountType = AccountBean.TYPE_INCOME_SELL;

    private String mDetailType = AccountBean.NAME_ALL;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    if (mListPopWindow != null && mListPopWindow.isShowing()) {
                        mListPopWindow.dismiss();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override

    public int getLayoutId() {
        return R.layout.fragment_chart;
    }

    @Override
    public void initData() {
        initPopData();
        initChartData();
    }

    @Override
    public void init(@NotNull View view) {
        super.init(view);
        mLlTitleReturn = view.findViewById(R.id.ll_title_return);
        group = view.findViewById(R.id.rg_type);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int arg1) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.rb_expend:
                        EventBus.getDefault().post(new EventChartTypeChange(AccountBean.TYPE_PAY_STOCK));
                        break;
                    case R.id.rb_income:
                        EventBus.getDefault().post(new EventChartTypeChange(AccountBean.TYPE_INCOME_SELL));
                        break;
                    default:
                        break;
                }
            }
        });
        mRbExpend = view.findViewById(R.id.rb_expend);
//        mRbExpend.setOnClickListener(this);
        mRbIncome = view.findViewById(R.id.rb_income);
        mRbIncome.setChecked(true);
//        mRbIncome.setOnClickListener(this);
        mVpChart = view.findViewById(R.id.vp_chart);
        mTabYearMonth = view.findViewById(R.id.tab_year_month);
        //    @BindView(R.id.tab_period)
//    TabLayout mTabPeriod;
        mSliderLayout = view.findViewById(R.id.slider_layout);
        mTvClassify = view.findViewById(R.id.tv_classify);
        mTvClassify.setOnClickListener(this);
    }

    private void initChartData() {
        mTitleList.clear();
        mFragmentList.clear();
        mTitleList.add("周");
        mTitleList.add("月");
        mTitleList.add("年");

        mFragmentList.add(ChartTypeFragment.newInstance(ChartTypeFragment.TYPE_WEEK,mAccountType));
        mFragmentList.add(ChartTypeFragment.newInstance(ChartTypeFragment.TYPE_MONTH,mAccountType));
        mFragmentList.add(ChartTypeFragment.newInstance(ChartTypeFragment.TYPE_YEAR,mAccountType));
    }

    private void initPopData() {
        mPopData.clear();
        addHeaderToPop();

//        List<AccountBean> list = DbHelper.INSTANCE.getAccountManager().getAccountList();

        Date maxDate = DbHelper.INSTANCE.getMaxDate();
//        maxDate.setTime(maxDate.getTime()+1);
        Date minDate = DbHelper.INSTANCE.getMinDate();
//        minDate.setTime(minDate.getTime()-1);
        if (minDate != null && maxDate != null) {
            List<AccountBean> accountList = DbHelper.INSTANCE.getAccountManager().getAccountList(mAccountType, minDate, maxDate);
            mPopData.addAll(AccListUtil.removeRepeat(accountList));
        }
    }

    private void addHeaderToPop() {
        AccountBean bean = new AccountBean();
        bean.setName("全部");
        bean.setPicRes(R.drawable.classify_baby);
        mPopData.add(bean);
    }

    @Override
    public void initView(View view) {
        mLlTitleReturn.setVisibility(View.GONE);
        initViewPager();
    }

    private void initViewPager() {

        // 注意使用的是：getSupportFragmentManager
        BaseFragmentPagerAdapter adapter = new BaseFragmentPagerAdapter(getChildFragmentManager(), mFragmentList, mTitleList);
        mVpChart.setAdapter(adapter);
        // 设置ViewPager最大缓存的页面个数(cpu消耗少)
        mVpChart.setOffscreenPageLimit(3);
        //vpContent.addOnPageChangeListener(this);
        mVpChart.setCurrentItem(0);
        //adapter.notifyDataSetChanged();
        mTabYearMonth.setTabMode(TabLayout.MODE_FIXED);
        mTabYearMonth.setupWithViewPager(mVpChart);
        //mVpChart.clearOnPageChangeListeners();

        mVpChart.addOnPageChangeListener(new SliderLayout.SliderOnPageChangeListener(mTabYearMonth, mSliderLayout));
//        mVpChart.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                //Logger.e(position + "");mFragmentList.get(position).getTypeListData()
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
    }


    public void onViewClicked() {

//        ListPopupWindow listPopupWindow =new ListPopupWindow(context);
//
//        listPopupWindow.setWidth(400);//设置宽度
//
//        listPopupWindow.setHeight(ListPopupWindow.MATCH_PARENT);//设置高度
//        listPopupWindow.setAnchorView(mTvClassify);
//        listPopupWindow.setAdapter(new ChartPopWindowAdapter(context, mPopData));
//        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Logger.e("click " + position);
//            }
//        });
//        listPopupWindow.show();

        if (mPopData == null || mPopData.size() < 2)
            return;
        if (mListPopWindow != null && mListPopWindow.isShowing())
            return;
        mListPopWindow = new ListPopWindow(getContext(), mPopData.size());
        mListPopWindow.setAnchorView(mTvClassify);
        mListPopWindow.setHorizontalOffset(DensityUtil.dip2px(5));
        mListPopWindow.setAdapter(new ChartPopWindowAdapter(getContext(), mPopData));
        mListPopWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Logger.e("click " + position);
                mTvClassify.setText(mPopData.get(position).getName());
                mListPopWindow.dismiss();

                //发送消息通知chartdetailfragment
                String message;
                if (position == 0)
                    message = AccountBean.NAME_ALL;
                else
                    message = mPopData.get(position).getName();
                if (mDetailType.equals(message))
                    return;
                mDetailType = message;
                EventBus.getDefault().post(new ChartClassifyEvent(message));
            }
        });
        mListPopWindow.show();
    }

    public String getDetailType() {
        return mDetailType;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_classify:
                onViewClicked();
                break;
            default:
                break;
        }
    }

    private void refresh(){

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
