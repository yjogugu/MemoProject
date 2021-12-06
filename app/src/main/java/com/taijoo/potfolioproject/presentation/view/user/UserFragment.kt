package com.taijoo.potfolioproject.presentation.view.user

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.taijoo.potfolioproject.R
import com.taijoo.potfolioproject.databinding.UserFragmentBinding
import com.taijoo.potfolioproject.presentation.view.memo.MemoPagingAdapter
import com.taijoo.potfolioproject.presentation.view.memo.MemoViewModel
import com.taijoo.potfolioproject.presentation.view.memo.MemoViewModelFactory
import com.taijoo.potfolioproject.util.InterFace.MemoDeleteClickInterface
import com.taijoo.potfolioproject.util.ItemTouchHelperCallback
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class UserFragment : Fragment(), MemoDeleteClickInterface {

    companion object {
        fun newInstance() = UserFragment()
    }

    private lateinit var viewModel: MemoViewModel

    private lateinit var binding : UserFragmentBinding

    private lateinit var adapter : MemoPagingAdapter

    private lateinit var gm : GridLayoutManager

    @SuppressLint("FragmentLiveDataObserve", "UseRequireInsteadOfGet")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.user_fragment, container, false)

        viewModel = ViewModelProvider(this,MemoViewModelFactory(activity!!.application))[MemoViewModel::class.java]

        binding.apply {

            lifecycleOwner = this@UserFragment
        }

        return binding.root
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gm = GridLayoutManager(context, 1)

        binding.rvTest.setHasFixedSize(true)

        binding.rvTest.layoutManager = gm

        adapter = MemoPagingAdapter(context!!, gm,this )

        binding.rvTest.adapter = adapter


        val helper : ItemTouchHelper?  //아이템이동 터치헬퍼
        helper = ItemTouchHelper(ItemTouchHelperCallback(adapter, viewModel))
        helper.attachToRecyclerView(binding.rvTest)

        viewLifecycleOwner.lifecycleScope.launch {
            showData()
        }

        binding.btDouble.setOnClickListener {
            adapter.setRemoveType(1)
        }
    }


    suspend fun showData(){

        viewModel.getContent().collectLatest {
            adapter.submitData(it)
        }


    }


    override fun onDestroy() {
        super.onDestroy()
    }

    override fun itemViewOnClick(type: Int,position: Int , isCheck : Boolean) {
        viewModel.deleteMemoData(adapter.snapshot().items[position].icon_seq)
//        adapter.snapshot().items.subList()
        adapter.notifyItemRemoved(position)
        adapter.notifyItemRangeChanged(position,adapter.itemCount , false)
//        live
//        viewModel.viewModelScope.launch {
////            adapter.refresh()
////            adapter.notifyDataSetChanged()
//        }

    }

    override fun itemViewOnClick(type: Int) {

    }

}