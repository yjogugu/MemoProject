package com.taijoo.potfolioproject.presentation.view.memo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.*
import com.taijoo.potfolioproject.MyApplication
import com.taijoo.potfolioproject.R
import com.taijoo.potfolioproject.data.repository.room.entity.Memo
import com.taijoo.potfolioproject.databinding.MemoFragmentBinding
import com.taijoo.potfolioproject.util.*
import com.taijoo.potfolioproject.util.dialog.MemoDialog
import com.taijoo.potfolioproject.util.InterFace.MemoDeleteClickInterface
import kotlinx.coroutines.launch


class MemoFragment : Fragment(),LifecycleObserver , MemoDeleteClickInterface {


    companion object {
        fun newInstance() = MemoFragment()
    }

    lateinit var binding : MemoFragmentBinding
    lateinit var viewModel: MemoViewModel
    lateinit var adapter : MemoAdapter
//    lateinit var viewModelFactory: MemoViewModelFactory

    private var span_count: Int = 4

    var sPreference : SPreference? = null

    lateinit var gm : GridLayoutManager

    var start = 10//페이징 시작 갯수


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()//뷰 셋팅
    }


    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.memo_fragment, container, false)

//        viewModelFactory = MemoViewModelFactory(activity!!.application )

        viewModel = ViewModelProvider(this@MemoFragment , MemoViewModelFactory(activity!!.application))[MemoViewModel::class.java]

        MyApplication.instance.memoFragment = this

        binding.apply {
            lifecycleOwner = this@MemoFragment
            fragment = this@MemoFragment
        }

        return binding.root
    }


    @SuppressLint("FragmentLiveDataObserve", "UseRequireInsteadOfGet")
    fun init(){

        sPreference = SPreference(context, "user", 0)

        //최초 배율 설정
        if(sPreference!!.getIntValue("span_count") == -11){
            span_count = 4
        }
        else{
            span_count = sPreference!!.getIntValue("span_count")
        }


        RecyclerViewScrollState()//리사이클러뷰 최상,하단 판단
        ClickListener()//클릭이벤트 모음

        //리사이클러뷰 등록
        if(binding.rvMemo.adapter == null){
            gm = GridLayoutManager(context, span_count)

            binding.rvMemo.setHasFixedSize(true)
            binding.rvMemo.layoutManager = gm

            adapter = MemoAdapter(context!!, gm, this )

            binding.rvMemo.adapter = adapter

            val helper : ItemTouchHelper?  //아이템이동 터치헬퍼
            helper = ItemTouchHelper(ItemTouchHelperCallback(adapter, viewModel))
            helper.attachToRecyclerView(binding.rvMemo)
        }


        //LiveData
        viewModel.getMemoData(0, start).observe(this, { memoData ->

            if (viewModel.getDataType() == 0) {//default
                adapter.setData(memoData as ArrayList<Memo>)

            }
            else if (viewModel.getDataType() == 1) {//메모 생성

                adapter.setInsert(memoData[0])
                binding.rvMemo.scrollToPosition(0)

            }
            else if (viewModel.getDataType() == 2) {//메모 삭제

            }
        })

    }


    //클릭 이벤트 처리
    @SuppressLint("UseRequireInsteadOfGet")
    fun ClickListener(){
        //메뉴 다이얼로그 열기
//        binding.ibMemoAddStart.setOnClickListener {
//           MemoDialog(context!!, this).MemoPopup(sPreference!!)
//        }


        //삭제 완료 버튼 클릭
//        binding.tvOk.setOnClickListener {
//            binding.tvOk.visibility = View.INVISIBLE
//            binding.ibMemoAddStart.visibility = View.VISIBLE
////            adapter.setRemoveType(0)
//
//        }
    }

    //메뉴 다이얼로그 열기
    @SuppressLint("UseRequireInsteadOfGet")
    fun onMemoDialogClick(){
        MemoDialog(context!!, this).MemoPopup(sPreference!!)
    }

    //삭제 완료 버튼 클릭
    fun onMemoDeleteOkClick(){
        binding.tvOk.visibility = View.INVISIBLE
        binding.ibMemoAddStart.visibility = View.VISIBLE
    }

    //리사이클러뷰 최상단 최하단 판단
    fun RecyclerViewScrollState(){
        binding.rvMemo.setOnScrollChangeListener { view, i, i2, i3, i4 ->
            if(!binding.rvMemo.canScrollVertically(-1)){
                //최상단
            }
            else if(!binding.rvMemo.canScrollVertically(1)){
                //최하단
                    viewLifecycleOwner.lifecycleScope.launch {
//                        adapter.setPaging(viewModel.getContentMainThread(start) as MutableList<Memo> , start)
                    }
                start += 10

            }
        }
    }


    //어뎁터 클릭
    @SuppressLint("UseRequireInsteadOfGet")
    override fun itemViewOnClick(type: Int,position : Int , isCheck : Boolean) {
        binding.tvOk.visibility = View.VISIBLE
        binding.ibMemoAddStart.visibility = View.INVISIBLE
//        MemoDeleteMessageBox(context!!, this , position).show()
    }

    override fun onDestroyView() {
//        start = 10
        for(i in 0 until adapter.itemCount){
            viewModel.updateMemoData(i, adapter.item[i].icon_seq)
        }
        super.onDestroyView()

    }

    override fun itemViewOnClick(type: Int) {

    }


}