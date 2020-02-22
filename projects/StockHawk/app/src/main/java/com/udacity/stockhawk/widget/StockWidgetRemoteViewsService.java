package com.udacity.stockhawk.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.ui.DetailsActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by narko on 11/03/17.
 */

public class StockWidgetRemoteViewsService extends RemoteViewsService {
    private static final String TAG = StockWidgetRemoteViewsService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StockRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    private class StockRemoteViewsFactory implements RemoteViewsFactory {
        private Context mContext;
        private int mAppWidgetId;
        private Cursor data;
        private final DecimalFormat dollarFormatWithPlus;
        private final DecimalFormat dollarFormat;

        public StockRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
            dollarFormatWithPlus = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
            dollarFormatWithPlus.setPositivePrefix("+$");
        }

        @Override
        public void onCreate() {
            // empty
        }

        @Override
        public void onDataSetChanged() {
            if (data != null) {
                data.close();
            }
            // This method is called by the app hosting the widget (e.g., the launcher)
            // However, our ContentProvider is not exported so it doesn't have access to the
            // data. Therefore we need to clear (and finally restore) the calling identity so
            // that calls use our process and permission
            final long identityToken = Binder.clearCallingIdentity();
            data = getContentResolver().query(
                    Contract.Quote.URI,
                    Contract.Quote.QUOTE_COLUMNS.toArray(new String[]{}),
                    null, null, Contract.Quote.COLUMN_SYMBOL);
            Binder.restoreCallingIdentity(identityToken);
        }

        @Override
        public void onDestroy() {
            if (data != null) {
                data.close();
                data = null;
            }
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (position == AdapterView.INVALID_POSITION ||
                    data == null || !data.moveToPosition(position)) {
                return null;
            }
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.list_item_quote);
            String symbol = data.getString(Contract.Quote.POSITION_SYMBOL);
            float price = data.getFloat(Contract.Quote.POSITION_PRICE);
            float rawAbsoluteChange = data.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);

            views.setTextViewText(R.id.symbol, symbol);
            views.setTextViewText(R.id.price, dollarFormat.format(price));
            views.setTextViewText(R.id.change, dollarFormatWithPlus.format(rawAbsoluteChange));
            if (rawAbsoluteChange > 0) {
                views.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_green);
            } else {
                views.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_red);
            }

            // Next, set a fill-intent, which will be used to fill in the pending intent template
            // that is set on the collection view in StockWidgetProvider.
            Bundle extras = new Bundle();
            extras.putString(DetailsActivity.SYMBOL_KEY, symbol);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            views.setOnClickFillInIntent(R.id.stock_item, fillInIntent);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return new RemoteViews(getPackageName(), R.layout.list_item_quote);
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            if (data != null && data.moveToPosition(position)) {
                return data.getLong(Contract.Quote.POSITION_ID);
            }
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
