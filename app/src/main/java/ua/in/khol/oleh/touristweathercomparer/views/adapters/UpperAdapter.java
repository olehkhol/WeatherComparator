package ua.in.khol.oleh.touristweathercomparer.views.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ua.in.khol.oleh.touristweathercomparer.BR;
import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.databinding.ProviderItemBinding;
import ua.in.khol.oleh.touristweathercomparer.views.observables.Provider;

// UPPER ADAPTER
public class UpperAdapter extends RecyclerView.Adapter<UpperAdapter.UpperHolder> {

    private List<Provider> mProviders = new ArrayList<>();
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
    }

    @Override
    public int getItemCount() {
        if (mProviders != null)
            return mProviders.size();
        return 0;
    }

    public void setOnBannerClickListener(OnBannerClickListener bannerClickListener) {
        mBannerClickListener = bannerClickListener;
    }


    public void addItems(List<Provider> providers) {
        if (providers != null)
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
}