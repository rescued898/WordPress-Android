package org.wordpress.android.ui.themes;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import org.wordpress.android.R;
import org.wordpress.android.WordPress;
import org.wordpress.android.widgets.HeaderGridView;

/**
 * Adapter for the {@link ThemeBrowserFragment}'s listview
 *
 */
public class ThemeBrowserAdapter extends CursorAdapter {
    private final LayoutInflater mInflater;
    private final HeaderGridView mGridView;

    public ThemeBrowserAdapter(Context context, Cursor c, boolean autoRequery, HeaderGridView gridView) {
        super(context, c, autoRequery);
        mInflater = LayoutInflater.from(context);
        mGridView = gridView;
    }

    private static class ThemeViewHolder {
        private final NetworkImageView imageView;
        private final TextView nameView;
        private final TextView priceView;

        ThemeViewHolder(View view) {
            imageView = (NetworkImageView) view.findViewById(R.id.theme_grid_item_image);
            nameView = (TextView) view.findViewById(R.id.theme_grid_item_name);
            priceView = (TextView) view.findViewById(R.id.theme_grid_item_price);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.theme_grid_item, parent, false);

        ThemeViewHolder themeViewHolder = new ThemeViewHolder(view);
        view.setTag(themeViewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ThemeViewHolder themeViewHolder = (ThemeViewHolder) view.getTag();

        final String screenshotURL = cursor.getString(cursor.getColumnIndex("screenshot"));
        final String name = cursor.getString(cursor.getColumnIndex("name"));
        final String price = cursor.getString(cursor.getColumnIndex("price"));

        themeViewHolder.nameView.setText(name);
        themeViewHolder.priceView.setText(price);

        ScreenshotHolder urlHolder = (ScreenshotHolder) themeViewHolder.imageView.getTag();
        if (urlHolder == null) {
            urlHolder = new ScreenshotHolder();
            urlHolder.requestURL = screenshotURL;
            themeViewHolder.imageView.setDefaultImageResId(R.drawable.theme_loading);
            themeViewHolder.imageView.setTag(urlHolder);
        }

        if (!urlHolder.requestURL.equals(screenshotURL)) {
            urlHolder.requestURL = screenshotURL;
        }

        themeViewHolder.imageView.setImageUrl(screenshotURL + "?w=" + mGridView.getWidth(), WordPress.imageLoader);
    }

    static class ScreenshotHolder {
        String requestURL;
    }
}
