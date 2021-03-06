package example.com.furnitureproject.fragment.addgoods

import android.app.Service
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
import example.com.furnitureproject.account.goods.GoodsProvider
import example.com.furnitureproject.activity.GoodsAddActivity
import example.com.furnitureproject.db.DbHelper
import example.com.furnitureproject.db.bean.DetailTypeBean
import example.com.furnitureproject.eventbus.bean.EventAddDetailType
import example.com.furnitureproject.fragment.BaseFragmentKotlin
import example.com.furnitureproject.utils.DigitUtil
import example.com.furnitureproject.utils.ToastUtil
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_add_goods.*
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat
import java.util.*

class FragmentAddGoods: BaseFragmentKotlin(), View.OnClickListener , GoodsAddActivity.AddDetailTypeFragment{

    private var select_goods: TextView? = null
    private var et_prime: EditText? = null

    private var mPvOptions: OptionsPickerView<DetailTypeBean>? = null
    private var mTimePicker: TimePickerView? = null
    private var selectGoods: DetailTypeBean? = null
    private var goodsList: MutableList<DetailTypeBean> = mutableListOf()

//    private var keyboardUtil: KeyboardUtil? = null

    private var imm: InputMethodManager? = null


    override fun initView(rootView: View) {
        select_goods?.setOnClickListener(this)
        initKeyBoard()
        imm = context!!.getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager?
    }

    override fun initData() {
    }

    override fun findView(view: View) {
        super.findView(view)
        select_goods = view.findViewById(R.id.select_goods)
        et_prime = view.findViewById(R.id.et_prime)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_add_goods
    }

    override fun onClick(v: View?) {
        when(v?.id){

        }
    }

    private fun initKeyBoard(){
//        keyboardUtil = KeyboardUtil(context,activity,et_prime)
//        keyboardUtil?.hideKeyboard()
//        et_prime?.inputType = InputType.TYPE_NULL
        et_prime?.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        /**
        rl_prime_cost?.setOnTouchListener { v, event ->
            imm?.hideSoftInputFromWindow(v.applicationWindowToken,0)
            keyboardUtil?.setEditText(et_prime)
            keyboardUtil?.showKeyboard()
            false
        }
        keyboardUtil?.setOnKeyListener {
            keyboardUtil?.editText?.setText(it.toString())
            keyboardUtil?.hideKeyboard()
            //saveData(it)
            //activity?.finish()
        }
        */
    }

    private fun showTimePicker() {
        mTimePicker = TimePickerBuilder(context,object: OnTimeSelectListener {
            override fun onTimeSelect(date: Date?, v: View?) {
                val formatter = SimpleDateFormat("yyyy-MM-dd")
                val dateString = formatter.format(date)
//                tv_time.text = dateString
            }
        }).setType(booleanArrayOf(true, true, true, false, false, false))// 默认全部显示
                .build()
        mTimePicker?.show()
    }

    fun onGoodsAdd(){

    }


    private fun showTypePicker(){
        if(mPvOptions!=null && mPvOptions?.isShowing!!){
            mPvOptions?.dismiss()
        }
        goodsList.clear()
        goodsList.addAll(GoodsProvider.getAllGoods())
        mPvOptions = OptionsPickerBuilder(context, object: OnOptionsSelectListener {
            override fun onOptionsSelect(options1: Int, options2: Int, options3: Int, v: View?) {
                if(options1>=goodsList.size)return
                selectGoods = goodsList[options1]
//                tv_goods.text = selectGoods?.name
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
                    val intent = Intent(context, GoodsAddActivity::class.java)
                    startActivity(intent)
                }

                tvCancel.setOnClickListener { mPvOptions?.dismiss() }
            }
        }).build()
        mPvOptions?.setPicker(goodsList)
        mPvOptions?.show()
    }

    override fun saveData(): Observable<GoodsAddActivity.SaveMessage> {
        return Observable.create (ObservableOnSubscribe<GoodsAddActivity.SaveMessage> { emitter ->
            val list = DbHelper.getDetailTypeManager().getDetailTypeList(DetailTypeBean.TYPE_COMMODITY)
            for (item in list) {
                if (item.name.toString().equals(select_goods?.text)) {
                    emitter.onNext(GoodsAddActivity.SaveMessage(false,"商品已经存在"))
                    emitter.onComplete()
                    return@ObservableOnSubscribe
//                    emitter.onError(Exception("商品已经存在"))
                }
            }
            if (select_goods?.text.toString().equals("")) {
                emitter.onNext(GoodsAddActivity.SaveMessage(false,"商品名称不能为空"))
                emitter.onComplete()
                return@ObservableOnSubscribe
//                emitter.onError(Exception("商品名称不能为空！"))
            }
            val detailTypeBean = DetailTypeBean()
            detailTypeBean.name = select_goods?.text.toString()
            detailTypeBean.time = Date()
            //Logger.e("${et_prime.text.toString()}")
            val primeCost = et_prime?.text.toString()
            if(primeCost.isNotEmpty() && !DigitUtil.isInteger(primeCost) && !DigitUtil.isDouble(primeCost)){
                ToastUtil.showShort(tv_prime.text.toString()+"格式错误")
                return@ObservableOnSubscribe
            }
            if (et_prime?.text.toString() == "") {
                detailTypeBean.primeCost = 0F
            } else {
                detailTypeBean.primeCost = et_prime?.text.toString().toFloat()
            }
            detailTypeBean.type = DetailTypeBean.TYPE_COMMODITY
            detailTypeBean.note = et_note?.text.toString()
            DbHelper.getDetailTypeManager().insert(detailTypeBean)
            EventBus.getDefault().post(EventAddDetailType(detailTypeBean))
            emitter.onNext(GoodsAddActivity.SaveMessage(true,"保存成功"))
            emitter.onComplete()
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}