package example.com.furnitureproject.fragment.chart;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.query.QueryBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import example.com.furnitureproject.R;
import example.com.furnitureproject.constant.ChartConfig;
import example.com.furnitureproject.db.DbHelper;
import example.com.furnitureproject.db.bean.AccountBean;
import example.com.furnitureproject.db.gen.AccountBeanDao;
import example.com.furnitureproject.eventbus.bean.ChartClassifyEvent;
import example.com.furnitureproject.eventbus.bean.EventChartTypeChange;
import example.com.furnitureproject.fragment.BaseFragment;
import example.com.furnitureproject.utils.AccListUtil;
import example.com.furnitureproject.utils.NumUtil;
import example.com.furnitureproject.utils.TimeUtil;
import example.com.furnitureproject.view.chart.SlideSelectLineChart;
import example.com.furnitureproject.view.chart.bean.Axis;
import example.com.furnitureproject.view.chart.bean.AxisValue;
import example.com.furnitureproject.view.chart.bean.Line;
import example.com.furnitureproject.view.chart.bean.PointValue;
import example.com.furnitureproject.view.chart.support.OnPointSelectListener;

/**
 * 年月日下的分类，几年、几月、几日
 * @author luo
 * @date 2017/9/15
 */
public class ChartDetailFragment extends BaseFragment {

    private static final String TIME_TYPE = "TIME_TYPE";
    private static final String END_TIME = "END_TIME";
    private static final String MAX_VALUE = "MAX_VALUE";
    SlideSelectLineChart mSelectChart;
    RecyclerView mRvChartClassify;
    AppBarLayout mAppBar;
    TextView mTvExpendTotalDes;
    TextView mTvExpendTotal;
    TextView mTvAccountTotal;
    TextView mTvAccountMax;

    private int mTimeType;//周、月、年
    private int mTime;
    /**
     * 天数（对应坐标轴点数）
     */
    private int mDays;
    private List<AccountBean> mAccountList = new ArrayList<>();
    private Date mDateStart, mDateEnd;
    private float mMaxValue = 0f; //list最大值
    private ArrayList<Float> mFloatList;
    private String mAccountType = AccountBean.TYPE_INCOME_SELL;
    private int mSelectPosition = 3; //chart当前选中
    private List<ChartDataBean> mRecycleList = new ArrayList<>(); //recycviews数据源
    private ChartDetailCountAdapter mAdapter;
    private String mDetailType = AccountBean.NAME_ALL;
    private float mDaySumCount; //当天记账总额

    public static ChartDetailFragment newInstance(int timeType, int endTime, float maxValue) {
        ChartDetailFragment fragment = new ChartDetailFragment();
        Bundle args = new Bundle();
        args.putInt(TIME_TYPE, timeType);
        args.putInt(END_TIME, endTime);
        args.putFloat(MAX_VALUE, maxValue);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTimeType = getArguments().getInt(TIME_TYPE);
            mTime = getArguments().getInt(END_TIME);
            mMaxValue = getArguments().getFloat(MAX_VALUE);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_chart_detail;
    }

    @Override
    public void initData() {
        showProgressDialog();
        //初始化分类类型
        FragmentChart fragmentChart = (FragmentChart) (getParentFragment().getParentFragment());
        mDetailType = fragmentChart.getDetailType();
        initChartData();
        setDaySumCount(); //当天记账总额
        setRecycleListData(mSelectPosition);
        dismissProgressDialog();

        //消息发送到fragmentChart，获取分类列表
        //sendEventBus();
    }

    @Override
    public void init(@NotNull View view) {
        super.init(view);
        mSelectChart = view.findViewById(R.id.select_chart);
        mRvChartClassify = view.findViewById(R.id.rv_chart_classify);
        mAppBar = view.findViewById(R.id.app_bar);
        mTvExpendTotalDes = view.findViewById(R.id.tv_expend_total_des);
        mTvExpendTotal = view.findViewById(R.id.tv_expend_total);
        mTvAccountTotal = view.findViewById(R.id.tv_account_total);
        mTvAccountMax = view.findViewById(R.id.tv_account_max);
    }

    private void setDaySumCount() {
        List<AccountBean> accountList = getAccountModels(mSelectPosition, AccountBean.NAME_ALL);
        mDaySumCount = AccListUtil.sum(accountList);
    }


    @Override
    public void initView(View view) {

        initTitleText();

        initLineChart();
        initRecycleView();
        initListener();

    }

    private void initTitleText() {
        switch (mTimeType) {
            case ChartTypeFragment.TYPE_WEEK:
                mTvExpendTotalDes.setText("最近1周支出总额");
                break;
            case ChartTypeFragment.TYPE_MONTH:
                mTvExpendTotalDes.setText("最近1月支出总额");
                break;
            case ChartTypeFragment.TYPE_YEAR:
                mTvExpendTotalDes.setText("最近1年支出总额");
                break;
            default:
                break;
            //Arrays.sort(mAccountList.toArray())
        }

        if (mAccountList != null && mAccountList.size() > 0) {
            mTvExpendTotal.setText(AccListUtil.sum(mAccountList) + "");
            mTvAccountTotal.setText(mAccountList.size() + "");
            mTvAccountMax.setText(AccListUtil.max(mAccountList) + "");
        }
    }

    private void initListener() {
        mSelectChart.setOnPointSelectListener(new OnPointSelectListener() {
            @Override
            public void onPointSelect(int position, String xLabel, String value) {
                mSelectPosition = position;
                setDaySumCount(); //当天记账总额
                setRecycleListData(position);
                mAdapter.notifyDataSetChanged();
                //sendEventBus();
                //setTitleText(position);
                //Logger.e("point: " + position  +  "    value: " + value);
            }
        });

        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    //Logger.e("折叠状态");
                } else if (verticalOffset == 0) {
                    //Logger.e("展开状态");
                }

            }
        });
    }

    private void initRecycleView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRvChartClassify.setLayoutManager(linearLayoutManager);
        mAdapter = new ChartDetailCountAdapter(getContext(), mRecycleList);
        mRvChartClassify.setAdapter(mAdapter);
    }

    private void initChartData() {
        switch (mTimeType) {
            case ChartTypeFragment.TYPE_WEEK:
                mDays = 7;
                mDateStart = TimeUtil.getFirstDayOfWeek(TimeUtil.getDateByWeek(mTime));
                mDateEnd = TimeUtil.getEndDayOfWeek(TimeUtil.getDateByWeek(mTime));
                mDateEnd.setTime(mDateEnd.getTime()+24*60*60*1000);
                mAccountList = DbHelper.INSTANCE.getAccountManager().getAccountList(mAccountType, mDetailType, mDateStart, mDateEnd);
                mFloatList = getValues(mAccountList);
                //mMaxValue = Collections.max(mFloatList);
                //Logger.e("max:" + mAccountList.size());
                break;
            case ChartTypeFragment.TYPE_MONTH:
                mDays = 31;

                mDateStart = TimeUtil.getFirstDayOfMonth(mTime);
                mDateEnd = TimeUtil.getEndDayOfMonth(mTime);
                mAccountList = DbHelper.INSTANCE.getAccountManager().getAccountList(mAccountType, mDetailType, mDateStart, mDateEnd);
                mFloatList = getValues(mAccountList);
                //mMaxValue = Collections.max(mFloatList);
                break;
            case ChartTypeFragment.TYPE_YEAR:
                mDays = 12;
                mDateStart = TimeUtil.getFirstDayOfYear(mTime);
                mDateEnd = TimeUtil.getEndDayOfYear(mTime);
                mAccountList = DbHelper.INSTANCE.getAccountManager().getAccountList(mAccountType, mDetailType, mDateStart, mDateEnd);
                mFloatList = getValues(mAccountList);
                break;
            default:
                break;
        }
    }

    private ArrayList<Float> getValues(List<AccountBean> accountList) {
        ArrayList<Float> list = new ArrayList<>();
        int currentDay;
        for (int i = 1; i <= mDays; i++) {
            if (mTimeType == ChartTypeFragment.TYPE_YEAR) { //年
                currentDay = i - 1;
            } else { //月周
                Date currentDate = TimeUtil.getDistanceDate(mDateStart, i - 1);
                currentDay = TimeUtil.getDayOfYear(currentDate);
            }

            if (accountList == null || accountList.size() == 0) { //天数为0
                list.add(0f);
            } else { //list天数小于等于days
                float sumDayCount = 0f;
                for (AccountBean accountModel : accountList) {
                    int day;
                    if (mTimeType == ChartTypeFragment.TYPE_YEAR)
                        day = TimeUtil.getMonthOfYear(new Date(accountModel.getTime()));
                    else
                        day = TimeUtil.getDayOfYear(new Date(accountModel.getTime()));
                    if (currentDay == day) { //周、月模式同一天的数据累计相加，年为同月数据相加
                        sumDayCount += accountModel.getPrice()*accountModel.getCount();
                    }
                }
                mMaxValue = Math.max(mMaxValue,sumDayCount);

                list.add(sumDayCount);
            }
        }
        return list;

    }

    private void initLineChart() {
        Axis axisX = new Axis(getAxisValuesX());
        axisX.setAxisColor(Color.parseColor("#a9a6b8")).setTextColor(Color.parseColor("#a9a6b8")).setHasLines(false).setShowText(true);
        Axis axisY = new Axis(getAxisValuesY());
        axisY.setAxisColor(Color.TRANSPARENT).setTextColor(Color.DKGRAY).setHasLines(true).setShowText(false).setScaleLength(5);
        mSelectChart.setAxisX(axisX);
        mSelectChart.setAxisY(axisY);

        mSelectChart.setSlideLine(ChartConfig.getSlideingLine());
        mSelectChart.setChartData(getFoldLine());
        //新加属性 // TODO: 2017/9/21 判断是否越界
        mSelectChart.setSelectedPoint(mSelectPosition);

        mSelectChart.show();

        //mSelectChart.
    }

    private void setRecycleListData(int position) {
        mRecycleList.clear();

        List<AccountBean> accountList = getAccountModels(position, mDetailType);

        if (accountList != null && accountList.size() > 0) {


//            String type = accountList.get(0).getName();
//            int imgRes = accountList.get(0).getPicRes();
//            float sumAccountClassify = 0f; //记账总额
//            int addCount = 0; //相加次数(记账笔数)
//            for (AccountBean accountModel : accountList) {
//
//                if (!accountModel.getName().equals(type)) {
//                    ChartDataBean chartBean = getChartDataBean(type, imgRes, sumAccountClassify, addCount);
//                    //Logger.e(NumUtil.getPointFloat(sumAccountClassify/sumAccount, 4) + "");
//                    mRecycleList.add(chartBean);
//
//                    sumAccountClassify = 0f;
//                    addCount = 0;
//                    type = accountModel.getName();
//                    imgRes = accountModel.getPicRes();
//                }
//                sumAccountClassify += accountModel.getCount();
//                addCount++;
//            }
//            ChartDataBean chartBean = getChartDataBean(type, imgRes, sumAccountClassify, addCount);
//            mRecycleList.add(chartBean);

            for (AccountBean accountBean: accountList) {
                ChartDataBean chartBean = getChartDataBean(accountBean.getName(), accountBean.getPicRes(), accountBean.getPrice(), (int) accountBean.getCount());
                mRecycleList.add(chartBean);
            }

        }
        //对list按照金额反转排序（金额从高到低）
        Collections.sort(mRecycleList);
        //Collections.reverse(mRecycleList);
    }

    @NonNull
    private ChartDataBean getChartDataBean(String type, int imgRes, float sumAccountClassify, int addCount) {
        ChartDataBean chartBean = new ChartDataBean();
        chartBean.setTotal(sumAccountClassify); //单价
        chartBean.setCount(addCount);
        chartBean.setName(type);
        chartBean.setImgRes(imgRes);
//        float percent = sumAccountClassify*addCount / mDaySumCount;
//        DecimalFormat decimalFormat=new DecimalFormat(".00");
//        String pri=decimalFormat.format(percent);
//        chartBean.setPrecent(NumUtil.getPointFloat(Float.valueOf(pri), 4));
        chartBean.setPrecent(NumUtil.getPointFloat(sumAccountClassify*addCount / mDaySumCount, 4));
        return chartBean;
    }

    /**
     * 根据当前选中的日期，获取RV 数据
     * @param position
     * @param detailType
     * @return
     */
    private List<AccountBean> getAccountModels(int position, String detailType) {
        Date selectedDate;
        List<AccountBean> list;
        if (mTimeType == ChartTypeFragment.TYPE_YEAR) {
            selectedDate = TimeUtil.getMonthAgo(mDateStart, position);
            list = getAccountList(mAccountType, detailType,
                    TimeUtil.getFirstDayOfMonth(selectedDate), TimeUtil.getEndDayOfMonth(selectedDate));
        } else {
            selectedDate = TimeUtil.getDistanceDate(mDateStart, position);
            list = getAccountList(mAccountType, detailType,
                    TimeUtil.getDayStartTime(selectedDate), TimeUtil.getDayEndTime(selectedDate));
        }
        return list;

    }

    public List<AccountBean> getAccountList(String accountType, String detailType, Date startTime, Date endTime) {
        QueryBuilder<AccountBean> builder = DbHelper.INSTANCE.getAccountManager().queryBuilder()
                .where(AccountBeanDao.Properties.Time.between(startTime.getTime(), endTime.getTime()),
                        AccountBeanDao.Properties.Type.eq(accountType));
        if (!detailType.equals(AccountBean.NAME_ALL))
            builder.where(AccountBeanDao.Properties.Name.eq(detailType));
        builder.orderAsc(AccountBeanDao.Properties.Name);
        //List<AccountModel> accountList  .list();
        return builder.list();
    }


    //x轴
    private List<AxisValue> getAxisValuesX() {
        List<AxisValue> axisValues = new ArrayList<>();

        for (int i = 1; i <= mDays; i++) {
            Date currentDate = TimeUtil.getDistanceDate(mDateStart, i - 1);
            AxisValue value = new AxisValue();
            if (mTimeType == ChartTypeFragment.TYPE_MONTH) {
                if (i % 5 == 0) {
                    value.setLabel(TimeUtil.date2String(currentDate, "MM-dd"));
                    value.setShowLabel(true);
                } else
                    value.setLabel("");
            } else if (mTimeType == ChartTypeFragment.TYPE_YEAR) {
                value.setLabel(i + "月");
                value.setShowLabel(true);
            } else {
                value.setLabel(TimeUtil.date2String(currentDate, "MM-dd"));
                value.setShowLabel(true);
            }

            axisValues.add(value);
        }
        return axisValues;
    }

    //Y轴
    private List<AxisValue> getAxisValuesY() {
        List<AxisValue> axisValues = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            AxisValue value = new AxisValue();
            value.setLabel(String.valueOf(i * (mMaxValue / 10)));
            axisValues.add(value);
        }
        return axisValues;
    }

    //点坐标数据
    private Line getFoldLine() {

        List<PointValue> pointValues = new ArrayList<>();
        for (int i = 1; i <= mDays; i++) {
            PointValue pointValue = new PointValue();
            pointValue.setX((i - 1) / (mDays - 1f));
            //int var = 5 + i + (int) (Math.random() * 200);
            pointValue.setLabel(String.valueOf(mFloatList.get(i - 1)));
            if (mMaxValue != 0) {
                pointValue.setY(mFloatList.get(i - 1) / mMaxValue);
                //Logger.e(mFloatList.get(i - 1) + ":" );
            } else
                pointValue.setY(0f);
            pointValues.add(pointValue);
        }
        return ChartConfig.getLine(pointValues);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //Logger.e("" + isVisibleToUser);
    }


    public List<AccountBean> getTypeListData() {
        return mAccountList;
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEvent(ChartClassifyEvent classifyEvent) {
        //Logger.e("收到eventbus：" + classifyEvent.getMessage());

        mDetailType = classifyEvent.getMessage();
        setRecycleListData(mSelectPosition);
        mAdapter.notifyDataSetChanged();

        initChartData();
        initLineChart();
        initTitleText();
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEvent(EventChartTypeChange message) {
        //Logger.e("收到eventbus：" + classifyEvent.getMessage());
        mAccountType = message.getType();
        setRecycleListData(mSelectPosition);
        mAdapter.notifyDataSetChanged();

        initChartData();
        initLineChart();
        initTitleText();
    }

}
