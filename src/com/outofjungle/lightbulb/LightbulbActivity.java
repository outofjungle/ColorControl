package com.outofjungle.lightbulb;

import org.json.JSONException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class LightbulbActivity extends Activity {

	private SeekBar ch1;
	private SeekBar ch2;
	private SeekBar ch3;
	private SeekBar ch4;
	private ToggleButton sw;
	private TextView progress;
	
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
		progress = (TextView) findViewById(R.id.UpdateText);
		
		loadPachube();
				
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
			ch1.setProgress(pachube.get("1"));
			ch2.setProgress(pachube.get("2"));
			ch3.setProgress(pachube.get("3"));
			ch4.setProgress(pachube.get("4"));
			sw.setChecked(pachube.get("0") != 0);			
		}
	}


	private class SyncDatastream extends AsyncTask<Pachube, Void, Pachube> {
		@Override
		protected Pachube doInBackground(Pachube... pachube) {
			pachube[0].sync();
			return pachube[0];
		}

		@Override
		protected void onPreExecute() {
			progress.setText("Updating...");
		}

		@Override
		protected void onPostExecute(Pachube pachube) {
			progress.setText("");		
		}
	}
	private OnSeekBarChangeListener channel_listener = new OnSeekBarChangeListener() {
		
		String channel;
		
		@Override
		public void onStopTrackingTouch(SeekBar bar) {
			switch (bar.getId()) {

			case R.id.ch1:
				channel = "1";
				break;

			case R.id.ch2:
				channel = "2";
				break;

			case R.id.ch3:
				channel = "3";
				break;
			
			case R.id.ch4:
				channel = "4";
				break;

			default:
				break;
			}
			if (channel != "") {
				try {
					pachube.set(channel, bar.getProgress());
					new SyncDatastream().execute(pachube);
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
				pachube.set("0", value );
				new SyncDatastream().execute(pachube);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}	
	};
	
	private void loadPachube() {
	    final Pachube data = (Pachube) getLastNonConfigurationInstance();
	    
	    if (data == null) {
	        pachube = new Pachube(getString(R.string.api_uri),
	        		getString(R.string.api_key));
	        new FetchDatastream().execute(pachube);
	    } else {
	    		pachube = new Pachube( data );
	    }
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
	    final Pachube data = new Pachube( pachube );
	    return data;
	}
}