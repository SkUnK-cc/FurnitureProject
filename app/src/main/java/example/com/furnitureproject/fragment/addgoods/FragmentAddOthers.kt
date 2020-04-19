package example.com.furnitureproject.fragment.addgoods

import android.content.Intent
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
import example.com.furnitureproject.account.goods.GoodsProvider
import example.com.furnitureproject.activity.GoodsAddActivity
import example.com.furnitureproject.db.bean.DetailTypeBean
import example.com.furnitureproject.fragment.BaseFragmentKotlin
import example.com.furnitureproject.utils.KeyboardUtil
import kotlinx.android.synthetic.main.fragment_add_others.*
import java.text.SimpleDateFormat
import java.util.*

class FragmentAddOthers: BaseFragmentKotlin(), View.OnClickListener {
    private var mPvOptions: OptionsPickerView<DetailTypeBean>? = null
    private var mTimePicker: TimePickerView? = null
    private var selectGoods: DetailTypeBean? = null
    private var goodsList: MutableList<DetailTypeBean> = mutableListOf()

    private var keyboardUtil: KeyboardUtil? = null


    override fun initView(rootView: View) {
        et_payout_name.setOnClickListener(this)
//        initKeyBoard()
    }

    override fun initData() {
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_add_others
    }

    override fun onClick(v: View?) {
        when(v?.id){

        }
    }

//    private fun initKeyBoard(){
//        keyboardUtil = KeyboardUtil(context,activity,et_prime)
//        keyboardUtil?.hideKeyboard()
//        et_prime.inputType = InputType.TYPE_NULL
//        rl_prime_cost.setOnTouchListener { v, event ->
//            keyboardUtil?.setEditText(et_prime)
//            keyboardUtil?.showKeyboard()
//            false
//        }
//        keyboardUtil?.setOnKeyListener {
//            keyboardUtil?.editText?.setText(it.toString())
//            keyboardUtil?.hideKeyboard()
//            //saveData(it)
//            //activity?.finish()
//        }
//    }

    private fun saveData(it: Float) {

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
                val tvCancel = v?.findViewById(R.id.tv_cancel) as TextView
                val tvAddGoods = v?.findViewById(R.id.tv_add_goods) as TextView
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
}