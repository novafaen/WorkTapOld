package se.novafaen.worktap;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;

import java.util.TimeZone;

import se.novafaen.worktap.db.TapDatabase;
import se.novafaen.worktap.util.DateHelper;
import se.novafaen.worktap.util.FontCache;
import se.novafaen.worktap.util.TimeFormatter;

/**
 * Default activity.
 *
 * @since 2015-12-18
 * @author Kristoffer Nilsson
 */
public class OverviewActivity extends AppCompatActivity {

    private TapDatabase dbHelper;
    private TapTime tapTime;
    private Handler updateHandler;

    /**
     * Updated GUI with one second
     */
    private Runnable updateTimeRunnable = new Runnable() {
        @Override
        public void run() {
            updateGUI();

            // update once every minute
            updateHandler.postDelayed(this, 5000); // TODO: change to a minute later
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // handler to update gui
        updateHandler = new Handler();

        // set gui fonts
        Typeface typeface = FontCache.get(getApplicationContext(), "fonts/fontawesome-webfont.ttf");
        Button editButton = (Button)findViewById(R.id.overview_summary_button_edit);
        editButton.setTypeface(typeface);
        TextView icon = (TextView)findViewById(R.id.overview_summary_icon);
        icon.setTypeface(typeface);

        // setup storage
        dbHelper = new TapDatabase(this);

        // setup time
        long todayTime = dbHelper.loadTappedSinceTimestamp(
                DateHelper.getStartOfDayToday());
        long weekTime = dbHelper.loadTappedSinceTimestamp(
                DateHelper.getStartOfDaySinceWeekday(DateHelper.Day.MONDAY)) - todayTime;
        long monthTime = dbHelper.loadTappedSinceTimestamp(
                DateHelper.getStartOfDaySinceMonthStart()) - todayTime - weekTime;

        tapTime = new TapTime(todayTime, weekTime, monthTime);

        // finally update gui
        updateGUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_overview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // do nothing, so far
        return super.onOptionsItemSelected(item);
    }

    /**
     * register tap from GUI.
     */
    public void registerTap(View view) {
        Log.d("OverviewActivity", "registering tap");

        Long nowTimeStamp = Instant.now().getMillis();

        Button editButton = (Button)findViewById(R.id.overview_summary_button_edit);

        tapTime.registerTap(nowTimeStamp);

        // set gui in correct state
        if (tapTime.ongoingTap()) {
            // event started a tap, start update timer
            updateHandler.post(updateTimeRunnable);

            editButton.setVisibility(View.VISIBLE);

        } else {
            // event ended a tap, stop update timer
            updateHandler.removeCallbacks(updateTimeRunnable); // stop update timer

            String timeZone = TimeZone.getDefault().getDisplayName();
            //String timeZone = DateTimeZone.getDefault().getName(nowTimeStamp);
            Log.d("OverviewActivity", "registering tap for time zome=" + timeZone);

            if (dbHelper.saveToDatabase(tapTime.getStartTimestamp(), nowTimeStamp, timeZone)) {
                tapTime.resetCurrentTap();
            }

            editButton.setVisibility(View.INVISIBLE);

            updateGUI(); // finally, update gui
        }

    }

    /**
     * update GUI, called explicitly or by timer.
     */
    private void updateGUI() {
        // today
        TextView todayTextView = (TextView)findViewById(R.id.overview_summay_time_today);
        Long timeToday = tapTime.getTappedToday();
        todayTextView.setText(TimeFormatter.formatTimeNatural(timeToday));

        // week
        TextView weekTextView = (TextView)findViewById(R.id.overview_summay_time_week);
        Long timeWeek = tapTime.getTappedWeekTotal();
        weekTextView.setText(TimeFormatter.formatTimeNatural(timeWeek));

        // month
        TextView monthTextView = (TextView)findViewById(R.id.overview_summay_time_month);
        Long timeMonth = tapTime.getTappedMonthTotal();
        monthTextView.setText(TimeFormatter.formatTimeNatural(timeMonth));

        // set tap button color
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        if (tapTime.ongoingTap()) {
            fab.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorRed));
        } else {
            fab.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.colorGreen));
        }
    }
}
