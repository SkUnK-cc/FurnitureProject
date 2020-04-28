package example.com.furnitureproject.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.CustomListener
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.bigkoo.pickerview.view.TimePickerView
import example.com.furnitureproject.R
import example.com.furnitureproject.activity.viewmodel.AnalyzeSettingVM
import example.com.furnitureproject.eventbus.bean.EventAnalyzeSettingChange
import example.com.furnitureproject.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_analyze_setting.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat
import java.util.*

class AnalyzeSetting : BaseActivity(), View.OnClickListener {

    companion object {
        const val profitAnalyze = "利润分析"
        const val primeCostAnalyze = "成本分析"
        const val otherCostAnalyze = "其他支出分析"
    }

    private var mPvOptions: OptionsPickerView<String>? = null
    private var vm: AnalyzeSettingVM? = null

    private var mStartTimePicker: TimePickerView? = null
    private var startTime: Long = 0L
    private var mEndTimePicker: TimePickerView? = null
    private var endTime: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analyze_setting)
        initListener()
        initData()
    }

    private fun initData() {
        vm = ViewModelProviders.of(this).get(AnalyzeSettingVM::class.java)

        vm?.selectType?.observe(this, Observer<String> { t ->
            select_type?.text = t
        })
    }

    private fun initListener() {
        select_type.setOnClickListener(this)
        tv_start_time.setOnClickListener(this)
        tv_end_time.setOnClickListener(this)
        ll_title_return.setOnClickListener(this)
        ll_title_contract.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.select_type -> {
                showTypePicker()
            }
            R.id.tv_start_time -> {
                showStartTimePicker()
            }
            R.id.tv_end_time -> {
                showEndTimePicker()
            }
            R.id.ll_title_return -> {
                finish()
            }
            R.id.ll_title_contract -> {
                saveSetting()
            }
        }
    }

    private fun isTypePickerShow(): Boolean {
        if(mPvOptions==null)return false
        return mPvOptions!!.isShowing
    }

    private fun showTypePicker(){
        if(isTypePickerShow()){
            mPvOptions?.dismiss()
        }
        mPvOptions = OptionsPickerBuilder(this, object: OnOptionsSelectListener {
            override fun onOptionsSelect(options1: Int, options2: Int, options3: Int, v: View?) {
                if(options1>= vm?.typeList!!.size)return
                vm?.selectType?.value = vm?.typeList!![options1]
            }
        }).setLayoutRes(R.layout.pickerview_goods_options,object: CustomListener {
            override fun customLayout(v: View?) {
                val tvSubmit = v?.findViewById(R.id.tv_finish) as TextView
                val tvCancel = v.findViewById(R.id.tv_cancel) as TextView
                val tvAddGoods = v.findViewById(R.id.tv_add_goods) as TextView
                tvSubmit.setOnClickListener {
                    mPvOptions?.returnData()
                    mPvOptions?.dismiss()
                }

                tvCancel.setOnClickListener { mPvOptions?.dismiss() }

            }
        }).build()
        mPvOptions?.setPicker(vm?.typeList)
        mPvOptions?.show()
    }

    private fun showStartTimePicker() {
        mStartTimePicker = TimePickerBuilder(this,object: OnTimeSelectListener {
            override fun onTimeSelect(date: Date?, v: View?) {
                val formatter = SimpleDateFormat("yyyy-MM-dd")
                val dateString = formatter.format(date)
                tv_start_time?.text = dateString
                startTime = date?.time!!
            }
        }).setType(booleanArrayOf(true, true, true, true, true, true))// 默认全部显示
                .build()
        mStartTimePicker?.show()
    }


    private fun showEndTimePicker() {
        mStartTimePicker = TimePickerBuilder(this,object: OnTimeSelectListener {
            override fun onTimeSelect(date: Date?, v: View?) {
                val formatter = SimpleDateFormat("yyyy-MM-dd")
                val dateString = formatter.format(date)
                tv_end_time?.text = dateString
                endTime = date?.time!!
            }
        }).setType(booleanArrayOf(true, true, true, true, true, true))// 默认全部显示
                .build()
        mStartTimePicker?.show()
    }

    private fun saveSetting() {
        if(!checkValid())return
        EventBus.getDefault().post(EventAnalyzeSettingChange(vm?.selectType?.value!!,startTime,endTime))
    }

    private fun checkValid(): Boolean {
        if(startTime==0L){
            ToastUtil.showShort("请选择起始时间")
            return false
        }
        if(endTime==0L){
            ToastUtil.showShort("请选择结束时间")
            return false
        }
        if(startTime>endTime){
            ToastUtil.showShort("起始时间不得大于结束时间")
            return false
        }
        return true
    }

}
