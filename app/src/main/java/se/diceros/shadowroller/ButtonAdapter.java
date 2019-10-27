package se.diceros.shadowroller;

import android.graphics.Color;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.widget.BaseAdapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.util.DisplayMetrics;
/**
 * Created by Kalle on 2016-07-13.
 */
public class ButtonAdapter extends BaseAdapter {

    private Context mContext;

    public ButtonAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return 40;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TextView text;
        String name = "" + (position + 1);

        if (convertView == null) {
            // Make a new text view
            text = new TextView(new ContextThemeWrapper(mContext, R.style.myTextViewStyle), null,0);
            DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
            int screenWidth = metrics.widthPixels;
            text.setLayoutParams(new GridView.LayoutParams(screenWidth/8, screenWidth/8));

        } else {
            text = (TextView) convertView;
        }
        text.setText(name);
        return text;
    }

}
