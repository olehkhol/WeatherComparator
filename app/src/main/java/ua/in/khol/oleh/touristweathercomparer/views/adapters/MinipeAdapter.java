package ua.in.khol.oleh.touristweathercomparer.views.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ua.in.khol.oleh.touristweathercomparer.databinding.MinipeBinding;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Average;

public class MinipeAdapter extends RecyclerView.Adapter<MinipeAdapter.MinipeHolder> {

    private List<Average.Canape> mMinipes = new ArrayList<>();

    @NonNull
    @Override
    public MinipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        MinipeBinding binding = MinipeBinding.inflate(inflater, parent, false);

        return new MinipeHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MinipeHolder holder, int position) {
        holder.setCanape(mMinipes.get(position));
    }

    @Override
    public int getItemCount() {
        if (mMinipes != null)
            return mMinipes.size();

        return 0;
    }


    void setItems(List<Average.Canape> canapes) {
        mMinipes.clear();
        mMinipes.addAll(canapes);
        notifyDataSetChanged();
    }

    static class MinipeHolder extends RecyclerView.ViewHolder {
        private final MinipeBinding mBinding;

        MinipeHolder(@NonNull MinipeBinding binding) {
            super(binding.getRoot());

            mBinding = binding;
        }

        public void setCanape(Average.Canape minipe) {
            mBinding.setMinipe(minipe);
            mBinding.executePendingBindings();
        }
    }
}
