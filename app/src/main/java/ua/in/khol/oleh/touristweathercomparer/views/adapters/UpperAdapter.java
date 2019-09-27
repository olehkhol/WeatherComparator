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
import ua.in.khol.oleh.touristweathercomparer.databinding.ProviderItemBinding;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Forecast;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.observables.Provider;

// UPPER ADAPTER
public class UpperAdapter extends RecyclerView.Adapter<UpperAdapter.UpperHolder> {

    private ObservableList<Provider> mProviders = new ObservableArrayList<>();
    private ForecastListChangedCallback mForecastListChangedCallback
            = new ForecastListChangedCallback();
    private OnBannerClickListener mBannerClickListener;

    @NonNull
    @Override
    public UpperHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ProviderItemBinding binding = ProviderItemBinding.inflate(inflater, parent, false);

        return new UpperHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UpperHolder holder, int position) {
        Provider provider = mProviders.get(position);
        holder.mBinding.banner.setOnClickListener(view -> {
            if (mBannerClickListener != null)
                mBannerClickListener.onBannerClick(mProviders.get(position).getUrl());
        });
        holder.mBinding.setProvider(provider);
        holder.mBinding.innerRecycler
                .setLayoutManager(new LinearLayoutManager(holder.mBinding.getRoot().getContext()));
        holder.mBinding.innerRecycler
                .setAdapter(new RecyclerAdapter<>(R.layout.forecast_item, BR.forecast,
                        provider.getForecasts()));

        provider.getForecasts().addOnListChangedCallback(mForecastListChangedCallback);

    }

    @Override
    public int getItemCount() {
        return mProviders.size();
    }

    public void setOnBannerClickListener(OnBannerClickListener bannerClickListener) {
        mBannerClickListener = bannerClickListener;
    }

    public void addItems(List<Provider> providers) {
        mProviders.addAll(providers);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mProviders.clear();
    }

    // UPPER HOLDER
    static class UpperHolder extends RecyclerView.ViewHolder {

        private ProviderItemBinding mBinding;

        UpperHolder(ProviderItemBinding binding) {
            super(binding.getRoot());

            mBinding = binding;
        }
    }

    public interface OnBannerClickListener {
        void onBannerClick(String url);
    }

    private class ForecastListChangedCallback
            extends ObservableList.OnListChangedCallback<ObservableList<Forecast>> {
        @Override
        public void onChanged(ObservableList sender) {
        }

        @Override
        public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {
        }

        @Override
        public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
            notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition,
                                     int itemCount) {
        }

        @Override
        public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {
        }
    }
}