package com.taijoo.potfolioproject.presentation.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.robinhood.ticker.TickerUtils
import com.taijoo.potfolioproject.MyApplication
import com.taijoo.potfolioproject.R
import com.taijoo.potfolioproject.data.repository.room.entity.Memo

import com.taijoo.potfolioproject.databinding.ActivityMainBinding
import com.taijoo.potfolioproject.presentation.view.friend.FriendListActivity
import com.taijoo.potfolioproject.presentation.view.memo.MemoAdapter
import com.taijoo.potfolioproject.presentation.view.memo.MemoViewModel
import com.taijoo.potfolioproject.presentation.view.memo.MemoViewModelFactory
import com.taijoo.potfolioproject.presentation.view.memo.memoAdd.MemoAddActivity
import com.taijoo.potfolioproject.presentation.view.opensource.OpenSourceActivity
import com.taijoo.potfolioproject.presentation.view.setting.SettingActivity
import com.taijoo.potfolioproject.util.dialog.MemoDeleteMessageBox
import com.taijoo.potfolioproject.util.InterFace.MemoDeleteClickInterface
import com.taijoo.potfolioproject.util.ItemTouchHelperCallback
import com.taijoo.potfolioproject.util.SPreference
import com.taijoo.potfolioproject.presentation.view.calendar.CalendarDialog
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() ,MemoDeleteClickInterface{

    lateinit var binding : ActivityMainBinding

    lateinit var viewModel : MemoViewModel

    lateinit var adapter : MemoAdapter

    private var span_count: Int = 4

    var sPreference : SPreference? = null

    lateinit var gm : GridLayoutManager

    var start = 50//????????? ?????? ??????

    private lateinit var context : Context

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        //????????? ??????
        binding = DataBindingUtil.setContentView(this , R.layout.activity_main)

        //ViewModel ??????
        viewModel = ViewModelProvider(this@MainActivity,MemoViewModelFactory(application))[MemoViewModel::class.java]

        //MyApplication ??????
        MyApplication.instance.mainActivity = this

        //????????? ????????? ???????????? variable ??? ?????? , lifecycleOwner ??????
        binding.apply {
            context = this@MainActivity
            mainActivity = this@MainActivity
            memoviewmodel = viewModel
            lifecycleOwner = this@MainActivity
        }

        init()
    }

    fun init(){
        sPreference = SPreference(context, "user", 0)

        //?????? ?????? ??????
        if(sPreference!!.getIntValue("span_count") == -11){
            span_count = 4
        }
        else{
            span_count = sPreference!!.getIntValue("span_count")
        }

        //?????? ?????????????????? ??????
        if(binding.rvMemo.adapter == null){

            gm = GridLayoutManager(context, span_count)
            binding.rvMemo.setHasFixedSize(true)
            binding.rvMemo.layoutManager = gm

            adapter = MemoAdapter(context,gm,this)

            binding.rvMemo.adapter = adapter

            val helper : ItemTouchHelper?  //??????????????? ????????????
            helper = ItemTouchHelper(ItemTouchHelperCallback(adapter, viewModel))
            helper.attachToRecyclerView(binding.rvMemo)

        }


        //???????????? ??????????????? ??????
        binding.tivCheckCount.setCharacterLists(TickerUtils.provideNumberList())

        //User Livedata ????????? ??????
        viewModel.userData.observe(this) {
            if (it.network_state) {
                binding.itemDrawer.llFriend.visibility = View.VISIBLE
//                val intent = Intent(applicationContext, MyService::class.java)
//                startService(intent)
            } else {
                binding.itemDrawer.llFriend.visibility = View.GONE
            }
        }


        //Memo Livedata ????????? ??????
        viewModel.getMemoData(0, start).observe(this) { memoData ->

            if (viewModel.getDataType() == 0) {//default
                adapter.setData(memoData as ArrayList<Memo>)

            } else if (viewModel.getDataType() == 1) {//?????? ??????
                if (adapter.itemCount > 0) {
                    if (memoData[0] != adapter.item[0]) {
                        adapter.setInsert(memoData[0])
                        binding.rvMemo.scrollToPosition(0)
                        viewModel.setDataType(-1)
                    }
                } else {
                    adapter.setInsert(memoData[0])
                    viewModel.setDataType(-1)
                }


            } else if (viewModel.getDataType() == 2) {//?????? ??????

                adapter.removeItem(viewModel.delete_item)
                start -= viewModel.delete_item.size
                viewModel._check_count.value = 0
                viewModel.getDelete_string(viewModel._check_count.value!!)
                viewModel.delete_item = ArrayList()

            } else if (viewModel.getDataType() == 3) {//????????????
                adapter.updateData(memoData[viewModel.adapter_position], viewModel.adapter_position)

            }
        }


        //??????????????? ??????
        FloatingActionSetting()

        //?????????????????? ????????? ????????? ??????
        RecyclerViewScrollState()

    }

    private fun FloatingActionSetting(){
        binding.fabMenu.setMenuButtonShowAnimation(AnimationUtils.loadAnimation(this,R.anim.show_from_bottom))
        binding.fabMenu.setMenuButtonHideAnimation(AnimationUtils.loadAnimation(this,R.anim.hide_to_bottom))
    }

    //?????????????????? ????????? ????????? ??????
    fun RecyclerViewScrollState(){
        binding.rvMemo.setOnScrollChangeListener { view, i, i2, i3, i4 ->

            if(i4 >0){//???????????? ?????? ?????????
                if(!binding.fabMenu.isShown){
                    binding.fabMenu.showMenu(true)
                }

            }
            else if (i4 <0){//???????????? ????????? ?????????
                if(binding.fabMenu.isShown){
                    binding.fabMenu.hideMenu(true)
                }

            }

            if(!binding.rvMemo.canScrollVertically(-1)){
                //?????????
            }
            else if(!binding.rvMemo.canScrollVertically(1)){
                //?????????
                lifecycleScope.launch {
                    adapter.setPaging(viewModel.getContentMainThread(start) as MutableList<Memo> )
                }
                start += 50

            }
        }
    }

    //?????????????????? ??????
    fun onMenuClick(){
        binding.dlMain.openDrawer(binding.itemDrawer.root)
        binding.fabMenu.close(false)

    }


    fun onNavigationViewItemClick(){
        val intent = Intent(context, SettingActivity::class.java)
        context.startActivity(intent)
    }


    //?????? ?????? ??????
    fun onEditClick(){
        val intent = Intent(context, MemoAddActivity::class.java)
        context.startActivity(intent)

        binding.fabMenu.close(false)
    }

    //????????? ???????????? ????????? ??????
    fun onDrawerClick(type : Int){
        when(type){
            //???????????? ????????? ??????
            0->{
                val intent = Intent(this, OpenSourceActivity::class.java)
                startActivity(intent)

                binding.fabMenu.close(false)
            }
            //???????????????
            1->{
                val intent = Intent(this , FriendListActivity::class.java)
                startActivity(intent)
                binding.fabMenu.close(false)
            }
        }

    }

    //?????? ????????????
    fun onDeleteOkClick(type : Int){
        if(type == 0){//xml ????????????
            if(viewModel.check_count.value == 0){//????????? ?????? 0?????????
                adapter.setRemoveType(0)
                viewModel.delete_item = ArrayList()
                binding.tvOk.visibility = View.GONE
                binding.ivCalendar.visibility = View.VISIBLE
                binding.tivCheckCount.visibility = View.GONE
            }
            else{//??????????????? 1????????? ????????? ??????????????? ?????????
                MemoDeleteMessageBox(this,viewModel.delete_item).show()
            }
        }
        else{//????????????????????? ????????????
            viewModel.setDataType(2)
            binding.tvOk.visibility = View.GONE
            binding.ivCalendar.visibility = View.VISIBLE
            binding.tivCheckCount.visibility = View.GONE
        }


        binding.fabMenu.close(false)
    }


    //?????? ?????? ??????
    fun onMemoViewCountClick(){
        SpanCountshowPopup(sPreference!!)

        binding.fabMenu.close(false)
    }

    //????????? ??????
    fun onCalendarClick(){
        CalendarDialog(1).show(supportFragmentManager,"CalendarDialog")
    }

    //?????? ?????? ??????
    fun onMemoDeleteClick(){
        adapter.setRemoveType(1)
        binding.tvOk.visibility = View.VISIBLE
        binding.ivCalendar.visibility = View.GONE
        binding.tivCheckCount.visibility = View.VISIBLE
        binding.fabMenu.close(false)
    }

    override fun itemViewOnClick(type: Int) {
        if(type == 1){
            binding.fabMenu.close(false)
        }
    }

    //?????? ????????? ?????? ?????????
    override fun itemViewOnClick(type : Int , position: Int , isCheck : Boolean) {
        viewModel.getCheckCount(isCheck , position)
    }


    override fun onBackPressed() {
        when {
            //?????????????????? ???????????????
            binding.dlMain.isDrawerOpen(GravityCompat.START) -> {
                binding.dlMain.closeDrawer(GravityCompat.START)
            }
            //??????????????? ???????????????
            binding.fabMenu.isOpened -> {
                binding.fabMenu.close(false)
            }
            //????????? ????????????????????????
            adapter.remove_type == 1 -> {
                adapter.setRemoveType(0)
                viewModel.delete_item = ArrayList()
                binding.ivCalendar.visibility = View.VISIBLE
                binding.tvOk.visibility = View.GONE
                binding.tivCheckCount.visibility = View.GONE
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    override fun onStop() {
        for(i in 0 until adapter.itemCount){
            viewModel.updateMemoData(i, adapter.item[i].icon_seq)
        }
        viewModel.setDataType(-1)
        super.onStop()
    }

    //?????????????????? Span ?????? ?????? ?????????
    private fun SpanCountshowPopup(sPreference : SPreference) {
        val popup = PopupMenu(context, binding.fabMemoCount)

        var spanCount : Int
        val now_span_count : Int

        //????????? ??????
        if(sPreference.getIntValue("span_count") == -11){
            spanCount = 4
        }
        else{
            spanCount = sPreference.getIntValue("span_count")
        }

        //?????? ??????
        if(sPreference.getIntValue("now_span_count") == -11){
            now_span_count = 4
        }
        else{
            now_span_count = sPreference.getIntValue("now_span_count")
        }

        popup.inflate(R.menu.scalingmenu)

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.span_count_1 -> {
                    sPreference.putValue("span_count",1)
                    spanCount = sPreference.getIntValue("span_count")

                    if(now_span_count != spanCount){
                        Toggle(spanCount)
                    }
                    sPreference.putValue("now_span_count",1)

                }
                R.id.span_count_2 -> {
                    sPreference.putValue("span_count",2)
                    spanCount = sPreference.getIntValue("span_count")

                    if(now_span_count != spanCount){
                        Toggle(spanCount)
                    }
                    sPreference.putValue("now_span_count",2)
                }
                R.id.span_count_3 -> {
                    sPreference.putValue("span_count",3)
                    spanCount = sPreference.getIntValue("span_count")
                    if(now_span_count != spanCount){
                        Toggle(spanCount)
                    }
                    sPreference.putValue("now_span_count",3)
                }
                R.id.span_count_4 -> {
                    sPreference.putValue("span_count",4)
                    spanCount = sPreference.getIntValue("span_count")
                    if(now_span_count != spanCount){
                        Toggle(spanCount)
                    }
                    sPreference.putValue("now_span_count",4)
                }
                R.id.span_count_5 -> {
                    sPreference.putValue("span_count",5)
                    spanCount = sPreference.getIntValue("span_count")
                    if(now_span_count != spanCount){
                        Toggle(spanCount)
                    }
                    sPreference.putValue("now_span_count",5)
                }

            }

            true
        })

        popup.show()
    }

    //?????? ????????????
    fun Toggle(count: Int){
        val spanCount = if (4 == count) 4 else count
        binding.rvMemo.post {

            (binding.rvMemo.layoutManager as GridLayoutManager).spanCount = spanCount

            adapter.notifyItemRangeChanged(0, adapter.itemCount)


        }

    }


}