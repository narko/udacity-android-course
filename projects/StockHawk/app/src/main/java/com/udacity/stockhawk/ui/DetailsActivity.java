package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by narko on 02/03/17.
 */

public class DetailsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = DetailsActivity.class.getSimpleName();
    public static final String SYMBOL_KEY = "symbol";
    private static final int STOCK_HISTORY_LOADER = 2;

    @BindView(R.id.chart)
    LineChart mChart;
    @BindView(R.id.price)
    TextView mPrice;
    @BindView(R.id.per_change)
    TextView mPerChange;
    @BindView(R.id.abs_change)
    TextView mAbsChange;
    @BindView(R.id.symbol)
    TextView mSymbol;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (!intent.hasExtra(SYMBOL_KEY)) {
            return;
        }
        String symbol = intent.getStringExtra(SYMBOL_KEY);
        mSymbol.setText(symbol);

        Bundle bundle = new Bundle();
        bundle.putString(SYMBOL_KEY, symbol);
        getSupportLoaderManager().initLoader(STOCK_HISTORY_LOADER, bundle, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (args == null) {
            Timber.e("Empty stock symbol...");
            return null;
        } else {
            Uri uri = Contract.Quote.makeUriForStock(args.getString(SYMBOL_KEY));
            return new CursorLoader(this,
                    uri,
                    Contract.Quote.QUOTE_COLUMNS.toArray(new String[]{}),
                    null, null, null);
        }
    }

    private String formatDate(String millis) {
        Date date = new Date(Long.parseLong(millis));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() != 0) {
            data.moveToFirst();
            String history = data.getString(Contract.Quote.POSITION_HISTORY);
            Timber.i(history);
            String[] historyRows = history.split("\n");
            String[] cols = null;
            List<Entry> entries = new ArrayList<Entry>();
            final List<String> valuesX = new ArrayList<String>();
            float x = 0f;
            for (int i = historyRows.length - 1; i >= 0; i--) {
                cols = historyRows[i].split(",");
                Timber.i(formatDate(cols[0]) + ", " + cols[1]);
                valuesX.add(formatDate(cols[0]));
                entries.add(new Entry(x, Float.parseFloat(cols[1])));
                x++;
            }
            LineDataSet dataSet = new LineDataSet(entries, getString(R.string.label_close));
            dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            LineData lineData = new LineData(dataSet);
            mChart.setData(lineData);
            XAxis xAxis = mChart.getXAxis();
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return valuesX.get((int) value);
                }
            });

            mChart.invalidate();


            final DecimalFormat dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
            final DecimalFormat dollarFormatWithPlus = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
            dollarFormatWithPlus.setPositivePrefix("+$");
            final DecimalFormat percentageFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
            percentageFormat.setMaximumFractionDigits(2);
            percentageFormat.setMinimumFractionDigits(2);
            percentageFormat.setPositivePrefix("+");

            float rawAbsoluteChange = data.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);
            float percentageChange = data.getFloat(Contract.Quote.POSITION_PERCENTAGE_CHANGE);
            String change = dollarFormatWithPlus.format(rawAbsoluteChange);
            String percentage = percentageFormat.format(percentageChange / 100);

            mPrice.setText(dollarFormat.format(data.getFloat(Contract.Quote.POSITION_PRICE)));
            mPerChange.setText(percentage);
            mAbsChange.setText(change);
        }
     }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (mChart != null) {
            //TODO change data in chart
            mChart.invalidate();
        }
    }
}
