package example.com.furnitureproject.activity

import android.app.Service
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnDismissListener
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import example.com.furnitureproject.R
import example.com.furnitureproject.fragment.addaccount.AccountFragmentAdapter
import example.com.furnitureproject.fragment.addgoods.FragmentAddGoods
import example.com.furnitureproject.fragment.addgoods.FragmentAddOthers
import example.com.furnitureproject.rxjava.BaseObserver
import example.com.furnitureproject.utils.ToastUtil
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_goods_add.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class GoodsAddActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        val PARAM_TYPE = "param_type"
        val TYPE_COMMODITY = "商品"
        val TYPE_PAY_OTHER = "其他支出"
    }

    private var typeList = mutableListOf<String>()
    private var mPvOptions: OptionsPickerView<String>? = null
    private var fragmentAdapter: AccountFragmentAdapter? = null
    private var fragmentList = mutableListOf<Fragment>()

    private var selectType = ""

    var type2Fragment = mutableMapOf<String, Fragment>()

    private var imm: InputMethodManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_add)

        initView()
        imm = getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager?
    }

    private fun getNameList(): List<String> {
        return type2Fragment.keys.toMutableList()
    }

    private fun getFragments(): List<Fragment> {
        return type2Fragment.values.toList()
    }

    private fun hideDigitBoard() {
        imm?.hideSoftInputFromWindow(accountType?.applicationWindowToken,0)
    }

    private fun initView() {
        selectType = intent.getStringExtra(PARAM_TYPE)
        type2Fragment[TYPE_COMMODITY] = FragmentAddGoods()
        type2Fragment[TYPE_PAY_OTHER] = FragmentAddOthers()

        accountType.setOnClickListener(this)
        accountType.text = selectType
        fragmentAdapter = AccountFragmentAdapter(supportFragmentManager)
        fragmentList.addAll(getFragments())
        fragmentAdapter?.setData(fragmentList)
        viewpager.offscreenPageLimit = 3
        viewpager.adapter = fragmentAdapter
        viewpager.currentItem = getNameList().indexOf(selectType)

        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }
            override fun onPageSelected(position: Int) {
            }
            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        ll_title_return.setOnClickListener(this)
        ll_title_contract.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.accountType -> {
                hideDigitBoard()
                showTypePicker()
            }
            R.id.ll_title_return -> {
                finish()
            }
            R.id.ll_title_contract -> {
                val curFragment = type2Fragment[selectType] as AddDetailTypeFragment
                curFragment.saveData()
                        .subscribe(object: BaseObserver<SaveMessage>() {
                            override fun onNext(t: SaveMessage) {
                                if(t.success){
                                    finish()
                                }else{
                                    ToastUtil.showShort(t.message)
                                }
                            }
                        })
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
                toolbar_title.text = selectType
                viewpager.currentItem = options1
            }
        }).build()
        mPvOptions?.setOnDismissListener(OnDismissListener {})
        mPvOptions?.setPicker(typeList)
        mPvOptions?.show()

    }

    interface AddDetailTypeFragment {
        fun saveData(): Observable<SaveMessage>
    }

    class SaveMessage(val success: Boolean,val message: String)
}
