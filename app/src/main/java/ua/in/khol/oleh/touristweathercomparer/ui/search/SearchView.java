package ua.in.khol.oleh.touristweathercomparer.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

import ua.in.khol.oleh.touristweathercomparer.MainApplication;
import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.ui.DialogFragmentView;
import ua.in.khol.oleh.touristweathercomparer.ui.ProgressEvent;
import ua.in.khol.oleh.touristweathercomparer.ui.rx.RxUtil;

public class SearchView extends DialogFragmentView<SearchViewModel> {

    public static final String TAG = SearchView.class.getSimpleName();
    private static final int MINIMUM_VALUABLE_TEXT_LENGTH = 3;
    private static final String ITEM_POSITION = "ITEM_POSITION";
    private static final int DEFAULT_ITEM_POSITION = -1;

    private SearchViewModel searchViewModel;
    private ListView lvPredictedNames;
    private CircularProgressIndicator cpiNamesPredicting;
    private ArrayAdapter<String> nameItems;
    private int itemPosition = DEFAULT_ITEM_POSITION;

    public static SearchView newInstance() {
        return new SearchView();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        ((MainApplication) requireActivity().getApplication())
                .getApplicationComponent().inject(this);
        super.onAttach(context);

        searchViewModel = getViewModel();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Keep a position after rotating
        if (savedInstanceState != null)
            itemPosition = savedInstanceState.getInt(ITEM_POSITION, DEFAULT_ITEM_POSITION);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(ITEM_POSITION, itemPosition);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.view_search;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);

        tvTitle.setText(R.string.search);
        tvCaption.setText(R.string.enter_city_name);
        btnAccept.setText(R.string.search);
        btnDismiss.setText(R.string.cancel);

        (lvPredictedNames = view.findViewById(R.id.lv_predicted_names))
                .setOnItemClickListener((parent, view1, position, id) -> {
                    btnAccept.setEnabled(true);
                    itemPosition = position;
                    // Redraw the whole list to apply backgrounds
                    nameItems.notifyDataSetChanged();
                });
        lvPredictedNames
                .setAdapter(nameItems = new ArrayAdapter<>(requireContext(), R.layout.item_name) {

                    @NonNull
                    @Override
                    public View getView(int position,
                                        @Nullable View convertView,
                                        @NonNull ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);

                        MaterialTextView name = view.findViewById(R.id.mtv_name);
                        if (position == itemPosition)
                            name.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        else
                            name.setBackground(null);

                        return view;
                    }
                });
        cpiNamesPredicting = view.findViewById(R.id.cpi_predicting);

        searchViewModel.predictPlaceNames(
                RxUtil.textChanges(view.findViewById(R.id.et_name_input))
                        .filter(charSequence -> {
                            boolean isValid = charSequence.length() >= MINIMUM_VALUABLE_TEXT_LENGTH;

                            setPredicting(isValid);
                            setNameItems(null);

                            return isValid;
                        })
        );
        btnAccept.setOnClickListener(v -> searchViewModel.processPosition(itemPosition));
    }

    @Override
    public void onResume() {
        super.onResume();

        searchViewModel.getNamesEvent().observe(this, names -> {
            setNameItems(names);
            setPredicting(false);
        });
        searchViewModel.getProgressEvent().observe(this, progressEvent -> {
            if (progressEvent == ProgressEvent.PROGRESS) {
                setAccepting(true);
            } else {
                setAccepting(false);
                dismiss();
            }
        });
    }

    @Override
    public void onDestroyView() {
        lvPredictedNames.setOnItemClickListener(null);
        lvPredictedNames.setAdapter(null);

        super.onDestroyView();
    }

    private void setPredicting(boolean isPredicting) {
        lvPredictedNames.setVisibility(isPredicting ? View.GONE : View.VISIBLE);
        cpiNamesPredicting.setVisibility(isPredicting ? View.VISIBLE : View.GONE);
    }

    private void setNameItems(List<String> names) {
        btnAccept.setEnabled(false);
        itemPosition = DEFAULT_ITEM_POSITION;

        nameItems.clear();
        if (names != null)
            nameItems.addAll(names);
        nameItems.notifyDataSetChanged();
    }
}