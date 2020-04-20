package ua.in.khol.oleh.touristweathercomparer.views.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ua.in.khol.oleh.touristweathercomparer.databinding.CanapeBinding;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Average;

public class CanapeAdapter extends RecyclerView.Adapter<CanapeAdapter.CanapeHolder> {

    private List<Average.Canape> mCanapes = new ArrayList<>();

    @NonNull
    @Override
    public CanapeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CanapeBinding binding = CanapeBinding.inflate(inflater, parent, false);

        return new CanapeHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CanapeHolder holder, int position) {
        holder.setCanape(mCanapes.get(position));
    }

    @Override
    public int getItemCount() {
        if (mCanapes != null)
            return mCanapes.size();

        return 0;
    }


    void setItems(List<Average.Canape> canapes) {
        mCanapes.clear();
        mCanapes.addAll(canapes);
        notifyDataSetChanged();
    }

    static class CanapeHolder extends RecyclerView.ViewHolder {
        private final CanapeBinding mBinding;

        CanapeHolder(@NonNull CanapeBinding binding) {
            super(binding.getRoot());

            mBinding = binding;
        }

        public void setCanape(Average.Canape canape) {
            mBinding.setCanape(canape);
        }
    }
}
