package ua.in.khol.oleh.touristweathercomparer.views.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ua.in.khol.oleh.touristweathercomparer.BR;
import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.databinding.TitleItemBinding;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Title;

// LOWER ADAPTER
public class TitleAdapter extends RecyclerView.Adapter<TitleAdapter.LowerHolder> {

    private ObservableList<Title> mTitles = new ObservableArrayList<>();

    @NonNull
    @Override
    public LowerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        TitleItemBinding binding = TitleItemBinding.inflate(inflater, parent, false);

        return new LowerHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LowerHolder holder, int position) {
        Title title = mTitles.get(position);
        holder.mBinding.setTitle(title);
    }

    @Override
    public int getItemCount() {
        return mTitles.size();
    }

    public void addItems(List<Title> titles) {
        mTitles.addAll(titles);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mTitles.clear();
    }

    // Lower HOLDER
    static class LowerHolder extends RecyclerView.ViewHolder {

        private TitleItemBinding mBinding;

        LowerHolder(TitleItemBinding binding) {
            super(binding.getRoot());

            mBinding = binding;
        }
    }
}