package ua.in.khol.oleh.touristweathercomparer.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.databinding.AverageBinding;
import ua.in.khol.oleh.touristweathercomparer.model.settings.Settings;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Average;
import ua.in.khol.oleh.touristweathercomparer.views.ViewBinding;

public class AverageAdapter extends RecyclerView.Adapter<AverageAdapter.AverageHolder> {

    private Settings mSettings;
    private List<Average> mAverages = new ArrayList<>();

    @NonNull
    @Override
    public AverageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        AverageBinding binding = AverageBinding.inflate(inflater, parent, false);

        AverageBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.average, parent, false);

        return new AverageHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AverageHolder holder, int position) {
        holder.setSettings(mSettings);
        holder.setAverage(mAverages.get(position));
    }

    @Override
    public int getItemCount() {
        if (mAverages != null)
            return mAverages.size();

        return 0;
    }

    void setSettings(Settings settings) {
        mSettings = settings;
    }

    void setItems(List<Average> averages) {
        mAverages.clear();
        mAverages.addAll(averages);
        notifyDataSetChanged();
    }

    static class AverageHolder extends RecyclerView.ViewHolder
            implements ViewBinding<AverageBinding> {

        private final AverageBinding mBinding;

        AverageHolder(AverageBinding binding) {
            super(binding.getRoot());

            mBinding = binding;
            initBinding(mBinding);
        }

        public void setAverage(Average average) {
            mBinding.setAverage(average);
        }

        public void setSettings(Settings settings) {
            mBinding.setSettings(settings);
        }

        @Override
        public void initBinding(AverageBinding binding) {
            Context context = binding.getRoot().getContext();
            int orientation = RecyclerView.VERTICAL;
            binding.canapesRecycler
                    .setLayoutManager(new LinearLayoutManager(context, orientation, false));
            binding.canapesRecycler.setAdapter(new CanapeAdapter());
        }
    }
}
