package com.saeinwoojoo.android.wifianalyzer;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.StringRes;
import android.widget.Toast;

@Deprecated
public class ToastUtil {

    private Toast mToast;

    /**
     * Show a standard toast that just contains a text view with the text from a resource.
     *
     * @param context  The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param resId    The resource id of the string resource to use.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link Toast.LENGTH_SHORT} or
     *                 {@link Toast.LENGTH_LONG}
     * @param gravity   @see android.view.Gravity
     *
     * @throws Resources.NotFoundException if the resource can't be found.
     */
    public static Toast showText(Context context, @StringRes int resId, int duration, int gravity)
            throws Resources.NotFoundException {
        Toast toast = Toast.makeText(context, resId, duration);
        toast.setGravity(gravity, 0, 0);
        toast.show();
        return toast;
    }

    public static Toast showText(Context context, @StringRes int resId, int duration)
            throws Resources.NotFoundException {
        Toast toast = Toast.makeText(context, resId, duration);
        toast.show();
        return toast;
    }

    /**
     * Show a standard toast that just contains a text view.
     *
     * @param context  The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param text     The text to show.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link Toast.LENGTH_SHORT} or
     *                 {@link Toast.LENGTH_LONG}
     * @param gravity   @see android.view.Gravity
     */
    public static Toast showText(Context context, CharSequence text, int duration, int gravity) {
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(gravity, 0, 0);
        toast.show();
        return toast;
    }

    public static Toast showText(Context context, CharSequence text, int duration) {
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        return toast;
    }
}
