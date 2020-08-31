package ua.in.khol.oleh.touristweathercomparer.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import ua.in.khol.oleh.touristweathercomparer.R;
import ua.in.khol.oleh.touristweathercomparer.databinding.SearchItemBinding;
import ua.in.khol.oleh.touristweathercomparer.databinding.ViewSearchBinding;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.SearchViewModel;
import ua.in.khol.oleh.touristweathercomparer.viewmodel.ViewModelProviderFactory;

public class SearchView extends DialogFragment
        implements ViewBinding<ViewSearchBinding> {
    private static final String ITEM_POSITION = "ITEM_POSITION";
    @Inject
    ViewModelProviderFactory mViewModelProviderFactory;
    private SearchViewModel mViewModel;
    private ArrayAdapter<String> mAdapter;
    private ListView mListView;
    private TextWatcher mTextWatcher;
    private EditText mEditText;
    private Button mOkButton;
    private int mPosition = -1;

    public SearchView() {
    }

    public static SearchView newInstance() {
        SearchView fragment = new SearchView();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
        // Instantiate a viewmodel from the injected factory
        mViewModel = new ViewModelProvider(this, mViewModelProviderFactory)
                .get(SearchViewModel.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Keep a position after rotating
        if (savedInstanceState != null)
            mPosition = savedInstanceState.getInt(ITEM_POSITION, -1);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        ViewSearchBinding binding = ViewSearchBinding
                .inflate(requireActivity().getLayoutInflater());
        binding.setSearchViewModel(mViewModel);
        initBinding(binding);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(binding.getRoot())
                .setPositiveButton(R.string.ok, (dialog, which) -> mViewModel.onOkButtonClicked(mPosition))
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Keep this in onStart to avoid crash after rotate
        mOkButton = ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(ITEM_POSITION, mPosition);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        mListView.setOnItemClickListener(null);
        mListView.setAdapter(null);
        mEditText.removeTextChangedListener(mTextWatcher);
        mViewModel.getNames().removeObservers(this);

        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Enable OK button if an item was selected
        if (mPosition >= 0)
            enableOkButton();
        else
            disableOkButton();
    }

    @Override
    public void initBinding(ViewSearchBinding binding) {
        mListView = binding.namesListView;
        mEditText = binding.inputEditText;
        mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                if (start != before) { // After lifecycle change they are equals
                    mPosition = -1;
                    mViewModel.onTextChanged(text.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        mEditText.addTextChangedListener(mTextWatcher);
        mListView.setOnItemClickListener((parent, view, position, id) -> {
            SearchView.this.enableOkButton();
            mPosition = position;
            mAdapter.notifyDataSetChanged(); // Redraw the whole list to apply backgrounds
        });
        mAdapter = new ArrayAdapter<String>(requireContext(), R.layout.search_item) {
            @NonNull
            @Override
            public View getView(int position,
                                @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                SearchItemBinding binding = DataBindingUtil.bind(view);

                if (position == mPosition)
                    binding.searchItemText
                            .setBackgroundColor(getResources().getColor(R.color.colorAccent));
                else
                    binding.searchItemText.setBackground(null);

                return view;
            }
        };
        mListView.setAdapter(mAdapter);
        // Subscribe to livedata from viewmodel
        mViewModel.getNames().observe(this, names -> {
            disableOkButton();
            mAdapter.clear();
            mAdapter.addAll(names);
        });
    }

    private void disableOkButton() {
        mOkButton.setEnabled(false);
    }

    private void enableOkButton() {
        mOkButton.setEnabled(true);
    }
}
