package example.com.furnitureproject.fragment.addaccount

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.text.InputType
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.CustomListener
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.bigkoo.pickerview.view.TimePickerView
import example.com.furnitureproject.R
import example.com.furnitureproject.activity.GoodsAddActivity
import example.com.furnitureproject.db.DbHelper
import example.com.furnitureproject.db.bean.AccountBean
import example.com.furnitureproject.db.bean.DetailTypeBean
import example.com.furnitureproject.db.gen.DetailTypeBeanDao
import example.com.furnitureproject.eventbus.bean.EventAddDetailType
import example.com.furnitureproject.fragment.addaccount.vm.FragmentSellVM
import example.com.furnitureproject.utils.DigitUtil
import example.com.furnitureproject.utils.ToastUtil
import kotlinx.android.synthetic.main.fragment_sell.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*

class FragmentSell: BaseAddTransFragment(), View.OnClickListener {

    private var select_goods: TextView? = null
    private var et_count: EditText? = null
    private var et_price: EditText? = null
    private var tv_time: TextView? = null


    val formatter = SimpleDateFormat("yyyy-MM-dd")

    private var vm : FragmentSellVM? = null

    private var mPvOptions: OptionsPickerView<DetailTypeBean>? = null
    private var mTimePicker: TimePickerView? = null

    private var goodsList: MutableList<DetailTypeBean> = mutableListOf()

    private var time: Long = System.currentTimeMillis()
    private var inputMethodManager: InputMethodManager? = null

    private var isUpdate = false
    var accountBean: AccountBean? = null

    override fun initView(rootView: View) {
        vm = ViewModelProviders.of(this).get(FragmentSellVM::class.java)
        select_goods?.setOnClickListener(this)
        tv_time?.setOnClickListener(this)
        initKeyBoard()
        accountBean = arguments?.getParcelable<AccountBean>(PARAM_ACCOUNT)
        if(accountBean!=null) {
            vm?.initAccountBean(accountBean!!)
            select_goods?.setText(accountBean!!.name)
            et_count?.setText(accountBean!!.count.toString())
            et_price?.setText(accountBean!!.price.toString())
            time = accountBean!!.time
            et_note?.setText(accountBean!!.note)
            val detailBean = DbHelper.getDetailTypeManager().getAbstractDao().queryBuilder()
                    .where(DetailTypeBeanDao.Properties.Name.eq(accountBean!!.name))
                    .where(DetailTypeBeanDao.Properties.Id.eq(accountBean!!.typeId))
                    .list()
            if(detailBean == null || detailBean.isEmpty()){
                ToastUtil.showShort("查询不到类型")
            }
            vm?.selectGoods = detailBean!![0]
        }
        tv_time?.text = formatter.format(time)
    }

    override fun initData() {
    }

    override fun findView(view: View) {
        super.findView(view)
        inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        select_goods = view.findViewById(R.id.select_goods)
        et_count = view.findViewById(R.id.et_count)
        et_price = view.findViewById(R.id.et_price)
        tv_time = view.findViewById(R.id.tv_time)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_sell
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.select_goods -> {
                hideDigitBoard()
                showTypePicker()
            }
            R.id.tv_time -> {
                hideDigitBoard()
                showTimePicker()
            }
        }
    }

    private fun hideDigitBoard() {
//        keyboardUtil?.hideKeyboard()
        inputMethodManager?.hideSoftInputFromWindow(select_goods?.applicationWindowToken,0)
    }

    private fun initKeyBoard(){
        /**
        keyboardUtil = KeyboardUtil(context,activity,et_price)
        keyboardUtil?.hideKeyboard()
        et_price?.inputType = InputType.TYPE_NULL
        rl_price?.setOnTouchListener { v, event ->
            keyboardUtil?.editText = et_price
            keyboardUtil?.showKeyboard()
            false
        }
//        et_price?.setOnTouchListener { v, event ->
//            keyboardUtil?.editText = et_price
//            keyboardUtil?.showKeyboard()
//            false
//        }
        et_price?.isEnabled = false
        */
        et_price?.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        /**
        keyboardUtil?.setOnKeyListener {
            keyboardUtil?.editText?.setText(it.toString())
            keyboardUtil?.hideKeyboard()
            //saveData(it)
            //activity?.finish()
        }
        et_count?.inputType = InputType.TYPE_NULL
        rl_count?.setOnTouchListener{ v, event ->
            keyboardUtil?.editText = et_count
            keyboardUtil?.showKeyboard()
            false
        }
//        et_count?.setOnTouchListener{ v, event ->
//            keyboardUtil?.editText = et_count
//            keyboardUtil?.showKeyboard()
//            false
//        }
        et_count?.isEnabled = false
        */
        et_count?.inputType = InputType.TYPE_CLASS_NUMBER
    }

    private fun showTimePicker() {
        mTimePicker = TimePickerBuilder(context,object: OnTimeSelectListener{
            override fun onTimeSelect(date: Date?, v: View?) {
                val dateString = formatter.format(date)
                tv_time?.text = dateString
                time = date?.time!!
            }
        }).setType(booleanArrayOf(true, true, true, true, true, true))// 默认全部显示
                .build()
        val calendar = Calendar.getInstance()
        calendar.time = Date(time)
        mTimePicker?.setDate(calendar)
        mTimePicker?.show()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onGoodsAdd(event: EventAddDetailType){
        refreshTypePickerIfNeed()
    }

    private fun refreshTypePickerIfNeed(){
        if(!isTypePickerShow())return
        mPvOptions?.dismiss()
        goodsList.clear()
        goodsList.addAll(DbHelper.getDetailTypeManager().getDetailTypeList(DetailTypeBean.TYPE_COMMODITY))
        mPvOptions?.setPicker(goodsList)
        mPvOptions?.show()
    }

    private fun isTypePickerShow(): Boolean {
        if(mPvOptions==null)return false
        return mPvOptions!!.isShowing
    }


    private fun showTypePicker(){
        if(accountBean != null){
            ToastUtil.showShort("编辑页面不支持修改类型")
            return
        }
        if(isTypePickerShow()){
            mPvOptions?.dismiss()
        }
        goodsList.clear()
        goodsList.addAll(DbHelper.getDetailTypeManager().getDetailTypeList(DetailTypeBean.TYPE_COMMODITY))
        mPvOptions = OptionsPickerBuilder(context, object: OnOptionsSelectListener {
            override fun onOptionsSelect(options1: Int, options2: Int, options3: Int, v: View?) {
                if(options1>=goodsList.size)return
                vm?.selectGoods = goodsList[options1]
                select_goods?.text = vm?.selectGoods?.name
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

                tvAddGoods.setOnClickListener {
                    val intent = Intent(context,GoodsAddActivity::class.java)
                    intent.putExtra(GoodsAddActivity.PARAM_TYPE,GoodsAddActivity.TYPE_COMMODITY)
                    startActivity(intent)
                }

                tvCancel.setOnClickListener { mPvOptions?.dismiss() }
            }
        }).build()
        mPvOptions?.setPicker(goodsList)
        mPvOptions?.show()
    }

    private fun isValidate(): Boolean {
        if(vm?.selectGoods==null){
            ToastUtil.showShort("商品名称不能为空")
            return false
        }
        val count = et_count?.text.toString()
        if(count.isNotEmpty() && !DigitUtil.isInteger(count)){
            ToastUtil.showShort(tv_count.text.toString()+"格式错误")
            return false
        }
        val price = et_price?.text.toString()
        if(price.isNotEmpty() && !DigitUtil.isInteger(price) && !DigitUtil.isDouble(price)){
            ToastUtil.showShort(tv_price.text.toString()+"格式错误")
            return false
        }
        if(time == null){
            return false
        }
        if(count.toFloat()> vm?.selectGoods?.stockCount!!){
            ToastUtil.showShort("商品库存不足")
            return false
        }
        return true
    }

    override fun saveTrans() {
        if(!isValidate())return
        vm?.updateStock(et_count?.text.toString())
        vm?.saveTrans(et_count?.text.toString(),et_price?.text.toString(),et_note.text.toString(),time!!)
    }

    private fun isUpdateValidate(): Boolean {
        if(vm?.selectGoods==null){
            ToastUtil.showShort("商品名称不能为空")
            return false
        }
        val count = (et_count?.text.toString().toInt() - accountBean!!.count).toString()
        if(count.isNotEmpty() && !DigitUtil.isInteger(count)){
            ToastUtil.showShort(tv_count.text.toString()+"格式错误")
            return false
        }
        val price = et_price?.text.toString()
        if(price.isNotEmpty() && !DigitUtil.isInteger(price) && !DigitUtil.isDouble(price)){
            ToastUtil.showShort(tv_price.text.toString()+"格式错误")
            return false
        }
        if(time == null){
            return false
        }
        if(count.toFloat()> vm?.selectGoods?.stockCount!!){
            ToastUtil.showShort("商品库存不足")
            return false
        }
        return true
    }

    override fun updateTrans() {
        if(!isUpdateValidate())return
        val count = et_count?.text.toString().toInt() - accountBean!!.count.toInt()
        vm?.updateStock(count.toString())
        vm?.updateTrans(et_count?.text.toString(),et_price?.text.toString(),et_note.text.toString(),time!!)
    }
}