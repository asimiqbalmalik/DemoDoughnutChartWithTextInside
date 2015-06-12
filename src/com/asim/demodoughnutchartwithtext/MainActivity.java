package com.asim.demodoughnutchartwithtext;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.MultipleCategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.support.v7.app.ActionBarActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends ActionBarActivity {

	private SeekBar mBarDoughnutControl;
	private int mProgressValue;
	private GraphicalView mDonutChartView;
	MultipleCategorySeries mcdataset;
	DefaultRenderer donutRenderer;

	LinearLayout doughnutchartlinearLayout;
	private TextView mTvDonutCenterPercentage;
	private CheckBox mChkPlayDonoughnut;

	// public Context context;
	public Handler handler = null;
	public static Runnable runnable = null;

	Random r = new Random();
	int Low = 10;
	int High = 100;
	int playValue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mBarDoughnutControl = (SeekBar) findViewById(R.id.control_bar);
		mTvDonutCenterPercentage = (TextView) findViewById(R.id.tvdonutcenterlayout);
		doughnutchartlinearLayout = (LinearLayout) findViewById(R.id.doughnutchart);
		mChkPlayDonoughnut = (CheckBox) findViewById(R.id.chkplayDoughnut);
		mBarDoughnutControl
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
						updateDonutChart(mProgressValue);
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						// TODO Auto-generated method stub
						mProgressValue = progress;

					}
				});
		mChkPlayDonoughnut
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							mBarDoughnutControl.setEnabled(false);
							playDonughnut();
						} else if (!isChecked) {
							mBarDoughnutControl.setEnabled(true);
							if(handler!=null){
								handler.removeCallbacks(runnable);
							}
						}
					}
				});

	}

	private void playDonughnut() {

		handler = new Handler();
		runnable = new Runnable() {
			public void run() {
				// Toast.makeText(getApplicationContext(),
				// "Service is still running", Toast.LENGTH_LONG).show();
				// Log.d("3g_deativate","Service is still running");

				playValue = r.nextInt(High - Low) + Low;
				updateDonutChart(playValue);
				handler.postDelayed(runnable, 2000);
			}
		};

		handler.postDelayed(runnable, 3000);
	}

	private void openDonutChart() {

		mDonutChartView = ChartFactory.getDoughnutChartView(this, mcdataset,
				donutRenderer);

		mDonutChartView.repaint();
		doughnutchartlinearLayout.removeAllViews();
		doughnutchartlinearLayout.addView(mDonutChartView);

	}

	public void updateDonutChart(double uiparams) {
		int[] colors = new int[2];
		int donutBackgroundColor;
		double total = 100.0;
		double reamining = total - uiparams;
		double x[] = new double[2];
		x[0] = uiparams;
		x[1] = reamining;
		mTvDonutCenterPercentage.setText(String.valueOf(uiparams).replaceFirst(
				"\\.0+$", "")
				+ "%");

		if (x[0] == 100) {

			colors[0] = Color.parseColor("#89ff7d");
			colors[1] = Color.parseColor("#3a8dce");
			donutBackgroundColor = Color.parseColor("#2BB044");
		} else if (x[0] == 0) {

			colors[0] = Color.parseColor("#FF7D7D");
			colors[1] = Color.parseColor("#FF7D7D");
			donutBackgroundColor = Color.parseColor("#B02B2B");

		} else {
			colors[0] = Color.parseColor("#7dc6ff");
			colors[1] = Color.parseColor("#3a8dce");
			donutBackgroundColor = Color.parseColor("#2B76B0");
		}

		mcdataset = getDonutMultiipleCategorySeries(x);
		donutRenderer = getDonutRenderer(colors, donutBackgroundColor);
		openDonutChart();

	}

	public DefaultRenderer getDonutRenderer(int[] colors, int b) {

		DefaultRenderer renderer = new DefaultRenderer();

		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		renderer.setMargins(new int[] { 20, 30, 15, 0 });

		renderer.setApplyBackgroundColor(true);

		renderer.setBackgroundColor(b);

		Log.d("colors", "" + colors[0]);
		for (int color : colors) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(color);
			renderer.addSeriesRenderer(r);
		}

		renderer.setLabelsColor(Color.GRAY);

		renderer.setZoomEnabled(false);
		renderer.setZoomButtonsVisible(false);
		renderer.setShowLabels(false);
		renderer.setPanEnabled(false);
		renderer.setShowLegend(false);
		renderer.setApplyBackgroundColor(false);
		renderer.setMargins(new int[] { 0, 0, 0, 0 });
		renderer.setInScroll(true);
		return renderer;

	}

	private MultipleCategorySeries getDonutMultiipleCategorySeries(double[] a) {

		MultipleCategorySeries series = new MultipleCategorySeries("");
		List<double[]> values = new ArrayList<double[]>();

		values.add(a);
		values.add(new double[] { 0, 0 });

		List<String[]> titles = new ArrayList<String[]>();
		titles.clear();
		titles.add(new String[] { "P1", "p" });
		titles.add(new String[] { "P2", "p" });

		series.clear();
		int k = 0;
		for (double[] value : values) {
			series.add(titles.get(k), value);
			k++;
		}

		return series;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
