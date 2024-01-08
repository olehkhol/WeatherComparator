package ua.in.khol.oleh.touristweathercomparer.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;

public class DrawableUtils {

    @Nullable
    public static Drawable getDrawableFromAssets(Context context, String fileName) {
        Drawable drawable = null;
        InputStream inputStream = null;

        try {
            inputStream = context.getAssets().open(fileName);
            drawable = Drawable.createFromStream(inputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return drawable;
    }

    public static Drawable resizeDrawable(Drawable drawable, int width, int height) {
        if (drawable != null) {
            drawable.setBounds(0, 0, width, height);
        }

        return drawable;
    }
}
