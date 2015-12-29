package se.novafaen.worktap.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.util.Hashtable;

/**
 * Created by Kristoffer Nilsson on 2015-12-27.
 */
public class FontCache {

    private static Hashtable<String, Typeface> fontCache = new Hashtable<String, Typeface>();

    public static Typeface get(Context context, String name) {
        Typeface tf = fontCache.get(name);

        if (tf == null) {
            try {
                tf = Typeface.createFromAsset(context.getAssets(), name);
            } catch (Exception e) {
                Log.e("FontCache", "could not load font " + name);
                return null;
            }
            fontCache.put(name, tf);
        }

        return tf;
    }
}
