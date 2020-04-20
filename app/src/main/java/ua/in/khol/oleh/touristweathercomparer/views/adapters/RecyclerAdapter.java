package ua.in.khol.oleh.touristweathercomparer.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// RECYCLER BINDING ADAPTER
public class RecyclerAdapter<T>
        extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {

    private final int mHolderLayout;
    private final int mItemsVariableId;
    private final List<T> mItems = new ArrayList<>();
    private OnItemClickListener<T> mItemClickListener;

    public RecyclerAdapter(int holderLayout, int itemsVariableId, List<T> items) {
        mHolderLayout = holderLayout;
        mItemsVariableId = itemsVariableId;
        if (items != null) {
            mItems.addAll(items);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(mHolderLayout, parent, false);

        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.RecyclerHolder holder,
                                 int position) {
        T item = mItems.get(position);
        holder.mBinding.getRoot().setOnClickListener(view -> {
            if (mItemClickListener != null)
                mItemClickListener.onItemClick(position, item);
        });
        holder.mBinding.setVariable(mItemsVariableId, item);
    }


    @Override
    public int getItemCount() {
        if (mItems != null)
            return mItems.size();
        return 0;
    }

    public void setOnItemClickListener(OnItemClickListener<T> itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void addItems(List<T> items) {
        if (items != null) {
            mItems.addAll(items);
            notifyDataSetChanged();
        }
    }

    public void clearItems() {
        mItems.clear();
    }

    // RECYCLER BINDING HOLDER
    static class RecyclerHolder extends RecyclerView.ViewHolder {
        private final ViewDataBinding mBinding;

        RecyclerHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }

    interface OnItemClickListener<T> {
        void onItemClick(int position, T item);
    }

}
