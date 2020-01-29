package ua.in.khol.oleh.touristweathercomparer.views.adapters;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

public final class SpinnerBindingAdapters {

    private SpinnerBindingAdapters() {
    }

    @BindingAdapter("selectedAttrChanged")
    public static void setListeners(Spinner spinner,
                                    final InverseBindingListener attrChange) {
        if (attrChange != null) {
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    attrChange.onChange();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    @BindingAdapter("selected")
    public static void setSelected(Spinner spinner, int position) {
        spinner.setSelection(position);
    }

    @InverseBindingAdapter(attribute = "selected")
    public static int getSelected(Spinner spinner) {
        return spinner.getSelectedItemPosition();
    }

}
