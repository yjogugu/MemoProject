package com.taijoo.potfolioproject.util;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.taijoo.potfolioproject.presentation.view.memo.MemoAdapter;
import com.taijoo.potfolioproject.presentation.view.memo.MemoPagingAdapter;
import com.taijoo.potfolioproject.presentation.view.memo.MemoViewModel;
import com.taijoo.potfolioproject.util.InterFace.ItemTouchHelperListener;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private ItemTouchHelperListener listener;
    int dragFrom = -1;
    int dragTo = -1;

//    MemoAdapter adapter;
    MemoViewModel viewModel;

    public ItemTouchHelperCallback(ItemTouchHelperListener listener  , MemoViewModel viewModel){
        this.listener = listener;
        this.viewModel = viewModel;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return super.isLongPressDragEnabled();
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View view = viewHolder.itemView;
        if(isCurrentlyActive) {//뷰를 잡았을때
            view.setAlpha(0.4f);
        }

    }

    //뷰를 놨을때
    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        View view = viewHolder.itemView;

        dragFrom = dragTo = -1;

        view.setAlpha(1f);

//        adapter.notifyDataSetChanged();

//        for(int i = 0;  i<adapter.getItemCount(); i++){
//            viewModel.updateMemoData(i,adapter.getItem().get(i).getIcon_seq());
//        }

    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int drag_flags = ItemTouchHelper.UP|ItemTouchHelper.DOWN|ItemTouchHelper.START|ItemTouchHelper.END;

        return makeMovementFlags(drag_flags,0);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        if(dragFrom == -1) {
            dragFrom =  viewHolder.getBindingAdapterPosition();
        }
        dragTo = target.getBindingAdapterPosition();
        return listener.onItemMove(viewHolder.getBindingAdapterPosition(),target.getBindingAdapterPosition(),viewHolder);

    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

}