package ua.in.khol.oleh.touristweathercomparer.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// RECYCLER BINDING ADAPTER
public class RecyclerAdapter<T>
        extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {

    private int mHolderLayout, mItemsVariableId;
    private List<T> mItems;
    private OnItemClickListener<T> mItemClickListener;

    public RecyclerAdapter(int holderLayout, int itemsVariableId, List<T> items) {
        mHolderLayout = holderLayout;
        mItemsVariableId = itemsVariableId;
        mItems = items;
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

    public void update(List<T> items) {
        mItems = items;
    }

    // RECYCLER BINDING HOLDER
    class RecyclerHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding mBinding;

        RecyclerHolder(@NonNull View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }

    public interface OnItemClickListener<T> {
        void onItemClick(int position, T item);
    }

}
