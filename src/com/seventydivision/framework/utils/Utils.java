package com.seventydivision.framework.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.seventydivision.framework.BuildConfig;
import com.seventydivision.framework.R;
import com.seventydivision.framework.models.BaseModel;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by andreaascari on 05/02/14.
 */
public class Utils {

    public static final java.lang.String TAG = Utils.class.getSimpleName();


    public static class Color {

        public static int getTransparentColor(int color, float factor) {
            int alpha = Math.round(android.graphics.Color.alpha(color) * factor);
            int red = android.graphics.Color.red(color);
            int green = android.graphics.Color.green(color);
            int blue = android.graphics.Color.blue(color);
            return android.graphics.Color.argb(alpha, red, green, blue);
        }

        public static boolean isTooLight(int color) {

            int r = (color >> 16) & 0xFF;
            int g = (color >> 8) & 0xFF;
            int b = (color >> 0) & 0xFF;

            double luma = 0.299 * r + 0.587 * g + 0.114 * b; // per ITU-R BT.709

            return luma > 160;

        }

    }

    @SuppressWarnings("unchecked")
    public static <T extends BaseModel> Class<T> getTypeParameter(Object o) {

        boolean typeFound = false;
        Class klass = o.getClass();
        Class<T> tClass = null;

        int count = 0;
        while (!typeFound) {
            count++;
            if (BuildConfig.DEBUG) Log.d(Utils.class.getSimpleName(), klass.getName());
            try {
                if (BuildConfig.DEBUG)
                    Log.d(TAG, "Trying to get parameterizedtype: " + ((ParameterizedType) klass.getGenericSuperclass()).toString());

                ParameterizedType parameterizedType = (ParameterizedType) klass.getGenericSuperclass();
                Type[] actualTypes = parameterizedType.getActualTypeArguments();
                tClass = (Class<T>) actualTypes[0];
                typeFound = true;
            } catch (ClassCastException e) {
                if (klass.getSuperclass() != null) {
                    if (BuildConfig.DEBUG)
                        Log.d(TAG, "No type parameters for this class, get its superclass: " + klass.getSuperclass().toString());

                    klass = klass.getSuperclass();
                } else {
                    if (BuildConfig.DEBUG)
                        Log.d(TAG, "No super class for this class[" + klass.toString() + "], return");
                    typeFound = true;
                }
            }

            if (count == 10)
                typeFound = true;
        }

        return tClass;

    }

    public static class Views {

        private static boolean mKeyboardOpened;

        public static void showKeyboard(final Context context, final View view) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, 0);
            mKeyboardOpened = true;
            //imm.showSoftInputFromInputMethod(view.getWindowToken(), InputMethodManager.SHOW_IMPLICIT);

        }

        public static void hideKeyBoard(final Context context, final View view) {
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    mKeyboardOpened = false;
                }
            }, 300);

        }

        public static void setListViewHeightBasedOnChildren(ListView listView) {
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) {
                return;
            }
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
            int totalHeight = 0;
            View view = null;
            for (int i = 0; i < listAdapter.getCount(); i++) {
                view = listAdapter.getView(i, view, listView);
                if (i == 0) {
                    view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
                }
                view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += view.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);
            listView.requestLayout();
        }

        public static void setExpandableListViewHeightBasedOnContent(ExpandableListView expandableListView) {
            ExpandableListAdapter expandableListAdapter = expandableListView.getExpandableListAdapter();

            if (expandableListAdapter == null)
                return;

            int desiredWidth = View.MeasureSpec.makeMeasureSpec(expandableListView.getWidth(), View.MeasureSpec.AT_MOST);
            int totalHeight = 0;
            View view = null;
            View groupView = null;


            for (int i = 0; i < expandableListAdapter.getGroupCount(); i++) {

                groupView = expandableListAdapter.getGroupView(i, true, groupView, expandableListView);

                view = expandableListAdapter.getChildView(i, 0, false, view, expandableListView);

                if (i == 0) {
                    groupView.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
                    view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
                }


                groupView.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                Log.d(TAG, "totalHeight: " + totalHeight);
                Log.d(TAG, "groupView [measuredHeight" + groupView.getMeasuredHeight() + "]");
                Log.d(TAG, "view [measuredHeight " + view.getMeasuredHeight() + "]");
                Log.d(TAG, "divider [height " + expandableListView.getDividerHeight() + "] [count " + (expandableListAdapter.getChildrenCount(i) - 1) + "]");
                totalHeight += groupView.getMeasuredHeight() + view.getMeasuredHeight() * expandableListAdapter.getChildrenCount(i) + (expandableListView.getDividerHeight() * (expandableListAdapter.getChildrenCount(i) - 1));
            }
            ViewGroup.LayoutParams params = expandableListView.getLayoutParams();
            params.height = totalHeight / 2;
            Log.d(TAG, "height: " + totalHeight);
            expandableListView.setLayoutParams(params);
            expandableListView.requestLayout();
        }


        @SuppressWarnings("deprecation")
        public static void setBackground(View v, Drawable dw) {
            if (Build.VERSION.SDK_INT >= 16) {
                v.setBackground(dw);
            } else {
                v.setBackgroundDrawable(dw);
            }
        }


        public static void showLongToast(Context context, int resource) {
            Toast.makeText(context, context.getString(resource), Toast.LENGTH_LONG).show();
        }

        public static void showLongToast(Context context, java.lang.String message) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }

        public static void drawCircle(ImageView v, Canvas canvas) {
            Drawable drawable = v.getDrawable();

            if (drawable == null) {
                return;
            }

            if (v.getWidth() == 0 || v.getHeight() == 0) {
                return;
            }

            if (drawable instanceof BitmapDrawable) {

                Bitmap b = ((BitmapDrawable) drawable).getBitmap();

                Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

                int w = v.getWidth(), h = v.getHeight();

                Bitmap roundBitmap = Utils.Views.getCroppedBitmap(bitmap, w);
                canvas.drawBitmap(roundBitmap, 0, 0, null);
            }

        }

        private static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
            Bitmap sBmp;

            if (bmp.getWidth() != radius || bmp.getHeight() != radius) {
                float smallest = Math.min(bmp.getWidth(), bmp.getHeight());
                float factor = smallest / radius;
                sBmp = Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() / factor), (int) (bmp.getHeight() / factor), false);
            } else {
                sBmp = bmp;
            }

            Bitmap output = Bitmap.createBitmap(radius, radius,
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xffa19774;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, radius, radius);

            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            paint.setDither(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(android.graphics.Color.parseColor("#BAB399"));
            canvas.drawCircle(
                    radius / 2 + 0.7f,
                    radius / 2 + 0.7f,
                    radius / 2 + 0.1f,
                    paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(sBmp, rect, rect, paint);

            return output;
        }


        public static int getDpMeasure(Context context, int i) {
            return (int) ((context.getResources().getDisplayMetrics().density * i) + 0.5f);
        }
    }


    public static java.lang.String getLoremPixel(int w, int h, boolean g) {
        java.lang.String loremPixelEndpoint = "http://lorempixel.com/";
        if (g) loremPixelEndpoint += "g/";
        return loremPixelEndpoint + w + "/" + h + "/?" + Calendar.getInstance().getTimeInMillis() + Math.random();
    }

    /**
     * Created by andreaascari on 06/05/14.
     */
    public static class String {

        public static java.lang.String capitalize(java.lang.String string) {
            return string.substring(0, 1).toUpperCase() + string.substring(1);
        }

        public static java.lang.String buildUrl(java.lang.String host, java.lang.String[] path) {
            java.lang.String[] parts = new java.lang.String[path.length + 1];
            if (host.endsWith("/")) {
                host = host.substring(0, host.length() - 1);
            }
            parts[0] = host;
            System.arraycopy(path, 0, parts, 1, path.length);
            return join(parts, "/");
        }

        protected static java.lang.String join(java.lang.String[] s, java.lang.String glue) {
            int k = s.length;
            if (k == 0) return "";
            StringBuilder out = new StringBuilder();
            out.append(s[0]);
            for (int x = 1; x < k; ++x)
                out.append(glue).append(s[x]);
            return out.toString();
        }

    }

    public static class API {


        public static boolean isGreatEqualsThan(int targetSdk) {
            return Build.VERSION.SDK_INT >= targetSdk;
        }
    }

    public static class Assets {

        private static final java.lang.String S3_ENDPOINT = "http://gift-app-uploads.s3.amazonaws.com";

        public static java.lang.String defaultGiftImageUrl() {
            return S3_ENDPOINT + "/default-gift-image@2x.png";
        }
    }

    public static class Dialogs {
        private static Dialog mLastDialog;


        public static AlertDialog showError(final Activity activity, java.lang.String message) {
            clearScreen();

            if (message == null)
                message = activity.getString(R.string.alert_error_message);

            return show(
                    activity,
                    activity.getString(R.string.an_error_has_occured),
                    message,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.finish();
                        }
                    },
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.finish();
                        }
                    }
            );
        }


        public static AlertDialog show(Context context, java.lang.String title, java.lang.String message, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {

            clearScreen();

            return (AlertDialog) (mLastDialog = new AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(context.getString(R.string.done), positiveListener)
                    .setNegativeButton(context.getString(R.string.cancel), negativeListener)
                    .show());
        }

        public static AlertDialog showOk(Context context, java.lang.String title, java.lang.String message, java.lang.String buttonText, DialogInterface.OnClickListener neutralListener) {
            clearScreen();

            if (buttonText == null)
                buttonText = context.getString(R.string.done);

            return (AlertDialog) (mLastDialog = new AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(message)
                    .setNeutralButton(buttonText, neutralListener)
                    .show());
        }

        public static void showCircleProgress(Context context) {
            clearScreen();
            mLastDialog = new ProgressDialog(context);
            mLastDialog.show();
            mLastDialog.setContentView(R.layout.transparent_progress_dialog);
        }


        public static void clearScreen() {
            if (mLastDialog != null && mLastDialog.isShowing())
                mLastDialog.dismiss();
        }


    }

}
