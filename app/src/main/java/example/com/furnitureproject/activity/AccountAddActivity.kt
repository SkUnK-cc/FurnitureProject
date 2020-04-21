package example.com.furnitureproject.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.View
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnDismissListener
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import example.com.furnitureproject.R
import example.com.furnitureproject.eventbus.bean.EventAddOtherTrans
import example.com.furnitureproject.eventbus.bean.EventAddSellTrans
import example.com.furnitureproject.eventbus.bean.EventAddStockTrans
import example.com.furnitureproject.fragment.addaccount.*
import kotlinx.android.synthetic.main.activity_account_add.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class AccountAddActivity : BaseActivity(), View.OnClickListener {

    private var typeList = mutableListOf<String>()
    private var mPvOptions: OptionsPickerView<String>? = null
    private var fragmentAdapter: AccountFragmentAdapter? = null
    private var fragmentList = mutableListOf<Fragment>()

    private var selectType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_add)

        initView()
    }

    val TYPE_INCOME_SELL = "商品收入"
    val TYPE_PAY_STOCK = "进货"
    val TYPE_PAY_OTHER = "其他支出"

    var type2Fragment = mutableMapOf<String,BaseAddTransFragment>()

    private fun getNameList(): List<String> {
        return type2Fragment.keys.toMutableList()
    }

    private fun getFragments(): List<Fragment> {
        return type2Fragment.values.toList()
    }

    private fun initView() {
        type2Fragment[TYPE_INCOME_SELL] = FragmentSell()
        type2Fragment[TYPE_PAY_STOCK] = FragmentStock()
        type2Fragment[TYPE_PAY_OTHER] = FragmentOther()

        ll_title_contract.setOnClickListener(this)
        ll_title_return.setOnClickListener(this)

        accountType.setOnClickListener(this)
        accountType.setText(getNameList()[0])
        fragmentAdapter = AccountFragmentAdapter(supportFragmentManager)
        fragmentList.addAll(getFragments())
        fragmentAdapter?.setData(fragmentList)
        viewpager.offscreenPageLimit = 3
        viewpager.adapter = fragmentAdapter
        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }
            override fun onPageSelected(position: Int) {
            }
            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.accountType -> {
                showTypePicker()
            }
            R.id.ll_title_contract -> {
                ll_title_contract.isClickable = false
                (fragmentAdapter?.getItem(viewpager.currentItem) as BaseAddTransFragment).saveTrans()
            }
            R.id.ll_title_return -> {
                finish()
            }
        }
    }

    private fun showTypePicker(){
        typeList.clear()
        typeList.addAll(getNameList())
        mPvOptions = OptionsPickerBuilder(this, object: OnOptionsSelectListener{
            override fun onOptionsSelect(options1: Int, options2: Int, options3: Int, v: View?) {
                selectType = typeList[options1]
                accountType.text = selectType
                viewpager.currentItem = options1
            }
        }).build()
        mPvOptions?.setOnDismissListener(OnDismissListener {})
        mPvOptions?.setPicker(typeList)
        mPvOptions?.show()

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSellAdd(event: EventAddSellTrans){
        finish()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onStockAdd(event: EventAddStockTrans){
        finish()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onOtherAdd(event: EventAddOtherTrans){
        finish()
    }

}
