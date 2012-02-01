package com.outofjungle.lightbulb;

import org.json.JSONException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ToggleButton;

public class LightbulbActivity extends Activity {

	private SeekBar ch1;
	private SeekBar ch2;
	private SeekBar ch3;
	private SeekBar ch4;
	private ToggleButton sw;
	
	private Pachube pachube;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ch1 = (SeekBar) findViewById(R.id.ch1);
		ch2 = (SeekBar) findViewById(R.id.ch2);
		ch3 = (SeekBar) findViewById(R.id.ch3);
		ch4 = (SeekBar) findViewById(R.id.ch4);
		sw = (ToggleButton) findViewById(R.id.sw);

		pachube = new Pachube(getString(R.string.api_uri),
				getString(R.string.api_key));

		new FetchDatastream().execute(pachube);
		
		ch1.setOnSeekBarChangeListener(channel_listener);
		ch2.setOnSeekBarChangeListener(channel_listener);
		ch3.setOnSeekBarChangeListener(channel_listener);
		ch4.setOnSeekBarChangeListener(channel_listener);
		sw.setOnClickListener(toggle_listener);
	}

	private class FetchDatastream extends AsyncTask<Pachube, Void, Pachube> {
		@Override
		protected Pachube doInBackground(Pachube... pachube) {
			pachube[0].fetch();
			return pachube[0];
		}

		@Override
		protected void onPostExecute(Pachube pachube) {
			setTitle(pachube.title());
			ch1.setProgress(pachube.get("ch1"));
			ch2.setProgress(pachube.get("ch2"));
			ch3.setProgress(pachube.get("ch3"));
			ch4.setProgress(pachube.get("ch4"));
			sw.setChecked(pachube.get("switch") != 0);			
		}
	}

	private OnSeekBarChangeListener channel_listener = new OnSeekBarChangeListener() {
		
		String channel;
		
		@Override
		public void onStopTrackingTouch(SeekBar bar) {
			switch (bar.getId()) {

			case R.id.ch1:
				channel = "ch1";
				break;

			case R.id.ch2:
				channel = "ch2";
				break;

			case R.id.ch3:
				channel = "ch3";
				break;
			
			case R.id.ch4:
				channel = "ch4";
				break;

			default:
				break;
			}
			if (channel != "") {
				try {
					pachube.set(channel, bar.getProgress());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar bar) {}
		
		@Override
		public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) {}
	};

	private OnClickListener toggle_listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {

			Integer value;
			if (sw.isChecked()) {
				value = 1;
			} else {
				value = 0;
			}
			try {
				pachube.set("switch", value );
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}	
	};
}