package ua.in.khol.oleh.touristweathercomparer.views.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ua.in.khol.oleh.touristweathercomparer.databinding.MiniAverageBinding;
import ua.in.khol.oleh.touristweathercomparer.model.settings.Settings;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Average;
import ua.in.khol.oleh.touristweathercomparer.views.ViewBinding;

public class MiniAverageAdapter extends RecyclerView.Adapter<MiniAverageAdapter.MiniAverageHolder> {

    private Settings mSettings;
    private List<Average> mAverages = new ArrayList<>();

    @NonNull
    @Override
    public MiniAverageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        MiniAverageBinding binding = MiniAverageBinding.inflate(inflater, parent, false);

        return new MiniAverageHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MiniAverageHolder holder, int position) {
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

    static class MiniAverageHolder extends RecyclerView.ViewHolder
            implements ViewBinding<MiniAverageBinding> {

        private final MiniAverageBinding mBinding;

        MiniAverageHolder(MiniAverageBinding binding) {
            super(binding.getRoot());

            mBinding = binding;
            initBinding(mBinding);
        }

        public void setAverage(Average average) {
            mBinding.setAverage(average);
            mBinding.executePendingBindings();
        }

        public void setSettings(Settings settings) {
            mBinding.setSettings(settings);
        }

        @Override
        public void initBinding(MiniAverageBinding binding) {
            binding.minipesRecycler
                    .setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext(),
                            LinearLayoutManager.VERTICAL, false));
            binding.minipesRecycler.setAdapter(new MinipeAdapter());
        }
    }
}
