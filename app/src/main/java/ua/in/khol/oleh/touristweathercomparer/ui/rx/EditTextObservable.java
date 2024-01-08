package ua.in.khol.oleh.touristweathercomparer.ui.rx;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.widget.AppCompatEditText;

import io.reactivex.Observer;

public class EditTextObservable extends InitialValueObservable<CharSequence> {

    private final AppCompatEditText editText;

    public EditTextObservable(AppCompatEditText editText) {
        this.editText = editText;
    }

    @Override
    protected void subscribeListener(Observer<? super CharSequence> observer) {
        Listener listener = new Listener(editText, observer);
        observer.onSubscribe(listener);
        editText.addTextChangedListener(listener);
    }

    @Override
    protected CharSequence getInitialValue() {
        return editText.getText();
    }

    private final static class Listener extends MainThreadDisposable implements TextWatcher {

        private final AppCompatEditText editText;
        private final Observer<? super CharSequence> observer;

        Listener(AppCompatEditText editText, Observer<? super CharSequence> observer) {
            this.editText = editText;
            this.observer = observer;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!isDisposed())
                observer.onNext(s);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

        @Override
        protected void onDispose() {
            editText.removeTextChangedListener(this);
        }
    }
}
