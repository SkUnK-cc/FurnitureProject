package example.com.furnitureproject.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView
import com.marshalchen.ultimaterecyclerview.itemTouchHelper.SimpleItemTouchHelperCallback
import com.marshalchen.ultimaterecyclerview.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration
import example.com.furnitureproject.R
import example.com.furnitureproject.activity.adapter.OtherListAdapter
import example.com.furnitureproject.db.DbHelper
import example.com.furnitureproject.db.bean.DetailTypeBean
import example.com.furnitureproject.eventbus.bean.EventAddDetailType
import kotlinx.android.synthetic.main.activity_goods_list.*
import kotlinx.android.synthetic.main.layout_list_toolbar.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class OtherPayoutListActivity : BaseActivity(), View.OnClickListener {
    private var adapter: OtherListAdapter? = null

    private var list: MutableList<DetailTypeBean> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goods_list)
        initView()
    }

    private fun initView() {
        toolbar_title.text = "其他支出类型"
        ll_title_return.setOnClickListener(this)
        iv_add.setOnClickListener(this)
        initRv()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ll_title_return -> finish()
            R.id.iv_add -> {
                val intentToAddOther = Intent(this, GoodsAddActivity::class.java)
                intentToAddOther.putExtra(GoodsAddActivity.PARAM_TYPE, GoodsAddActivity.TYPE_PAY_OTHER)
                startActivity(intentToAddOther)
            }
        }
    }

    private fun initRv(){
        adapter = OtherListAdapter(list)
        val stickyRecyclerHeadersDecoration = StickyRecyclerHeadersDecoration(adapter)
        ultimate_recycler_view.addItemDecoration(stickyRecyclerHeadersDecoration)
        ultimate_recycler_view.layoutManager = LinearLayoutManager(this)
        ultimate_recycler_view.setAdapter(adapter)
        adapter!!.setOnItemClickListener { view, position ->

        }
        val callback = object: SimpleItemTouchHelperCallback(adapter){
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
        itemTouchHelper.attachToRecyclerView(ultimate_recycler_view?.mRecyclerView)
        ultimate_recycler_view!!.setEmptyView(R.layout.rv_empty_bill, UltimateRecyclerView.EMPTY_CLEAR_ALL)
        if ( list.isEmpty())
            ultimate_recycler_view!!.showEmptyView()
        refresh()
    }

    private fun refresh(){
        list.clear()
        list.addAll(DbHelper.getDetailTypeManager().getDetailTypeList(DetailTypeBean.TYPE_PAY_OTHER))
        adapter?.notifyDataSetChanged()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onChange(e: EventAddDetailType) {
        refresh()
    }
}
