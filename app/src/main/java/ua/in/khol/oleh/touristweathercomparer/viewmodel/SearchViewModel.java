package ua.in.khol.oleh.touristweathercomparer.viewmodel;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ua.in.khol.oleh.touristweathercomparer.model.Repository;

public class SearchViewModel extends BaseViewModel {

    private final MutableLiveData<List<String>> mNames = new MutableLiveData<>();
    private final List<String> mIds = new ArrayList<>();
    private int mPosition = -1;

    public SearchViewModel(Repository repository) {
        super(repository);
    }

    @Override
    public void refresh() {

    }

    @Override
    protected void onCleared() {
        if (mPosition >= 0)
            getRepository().processPlaceById(mIds.get(mPosition));
        super.onCleared();
    }

    public void onTextChanged(String text) {
        getCompositeDisposable().clear(); // need to dispose of all previous queries
        getCompositeDisposable().add(getRepository().predictPlacesList(text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pairs -> {
                    mIds.clear();
                    List<String> names = new ArrayList<>();
                    for (Pair<String, String> pair : pairs) {
                        mIds.add(pair.first);
                        names.add(pair.second);
                    }
                    mNames.setValue(names);
                }));

    }

    public void onOkButtonClicked(int position) {
        mPosition = position;
    }

    public MutableLiveData<List<String>> getNames() {
        return mNames;
    }
}
