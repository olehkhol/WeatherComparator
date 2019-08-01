package ua.in.khol.oleh.touristweathercomparer.presenters;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.loader.content.Loader;

public class PresenterLoader<P extends BasePresenter> extends Loader<P> {

    private PresenterFactory<P> mFactory;
    private P mPresenter;

    public PresenterLoader(@NonNull Context context,
                           PresenterFactory<P> factory) {
        super(context);
        mFactory = factory;
    }

    @Override
    protected void onStartLoading() {
        if (mPresenter != null) {
            deliverResult(mPresenter);
            return;
        }

        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        if (mFactory != null) {
            mPresenter = mFactory.create();
            deliverResult(mPresenter);
            // Set value to "null" to avoid memory leak
            // because of lambda in the view part
            mFactory = null;
        }
    }

    @Override
    protected void onReset() {
        mPresenter = null;
    }

    public P getPresenter() {
        return mPresenter;
    }
}
