package ua.in.khol.oleh.touristweathercomparer.ui.rx;

import com.google.android.material.textfield.TextInputEditText;

public class RxUtil {

    public static InitialValueObservable<CharSequence> textChanges(TextInputEditText editText) {
        return new EditTextObservable(editText);
    }
}
