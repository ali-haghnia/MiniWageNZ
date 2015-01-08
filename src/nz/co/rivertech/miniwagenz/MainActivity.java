package nz.co.rivertech.miniwagenz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity implements OnSeekBarChangeListener {
	
	public String TAG = "Main";
	private Button btnCalc;
	private TextView tvResult;
	private EditText etHour;
	private EditText etMin;
	private SeekBar skbHours;
	private SeekBar skbWage;
	private EditText etWage;
	Boolean blnNoChange = false;
	Boolean blnNoWageChange = false;
	Boolean bln = false;
	Integer intMaxHours;
	Integer intMinHours = 10;
	Integer intDefHours = 40;
	Double dblMaxWage = 30.00;
	Double dblMinWage = 06.00;
	Double dblDefWage = 13.75;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		skbHours = (SeekBar) findViewById(R.id.skbHours);
		skbWage = (SeekBar) findViewById(R.id.skbWage);
		etWage = (EditText) findViewById(R.id.etWage);
		etHour = (EditText) findViewById(R.id.etHours);
		etMin = (EditText) findViewById(R.id.etMin);
		btnCalc = (Button) findViewById(R.id.btnCalc);
		tvResult = (TextView) findViewById(R.id.tvResult);

		loadPref();
		setDefValues();
		
		skbHours.setOnSeekBarChangeListener(this);
		skbWage.setOnSeekBarChangeListener(this);
		etHour.setText(intDefHours.toString());
		etMin.setText("00");
		etHour.requestFocus();
		
		etHour.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus)
				{
					blnNoChange = true;			
					if(etHour.getText().toString().equals("")) etHour.setText("00");
					Integer intHour = Integer.valueOf(etHour.getText().toString());
					String strHour = String.format("%02d", intHour);
					etHour.setText(strHour);
					Integer intMin = Integer.valueOf(etMin.getText().toString());
			    	skbHours.setProgress(((intHour - intMinHours) * 4) + (intMin / 15));
				}
			}
		});
		
		etMin.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) return;
				blnNoChange = true;
				if(etMin.getText().toString().equals("")) etMin.setText("00");
				Integer intMin = Integer.valueOf(etMin.getText().toString());
				String strMin = String.format("%02d", intMin);
				etMin.setText(strMin);
				Integer intHour = Integer.valueOf(etHour.getText().toString());
		    	skbHours.setProgress(((intHour - intMinHours) * 4) + (intMin / 15));
			}
		});
		
	etMin.addTextChangedListener(new TextWatcher() {
			String strKeep = "";
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
				strKeep = s.toString();
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (bln)
				{
					bln = false;
					return;
				}				
				Integer intMin = (s.length() == 0)? 0 : Integer.valueOf(s.toString());
//				Log.d("After",String.valueOf(s.length()));
				if (intMin > 59) s.replace(0, s.length(), strKeep);
			}
		});
	
	etWage.setOnFocusChangeListener(new OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) return;
			blnNoWageChange = true;
			double dblWage = Double.valueOf(etWage.getText().toString());
			etWage.setText(String.format("%.02f", dblWage));
			if (dblWage < dblMinWage) skbWage.setProgress(0);
			else if (dblWage > dblMaxWage) skbWage.setProgress((int) (dblMaxWage - dblMinWage) * 4);
			else skbWage.setProgress((int) (dblWage - dblMinWage) * 4);
		}
	});
	
	btnCalc.setOnClickListener(new OnClickListener() {
		public void onClick(View v) {
			tvResult.requestFocus();
			Double dblHour = Double.valueOf(etHour.getText().toString());
			Double dblMin = Double.valueOf(etMin.getText().toString());
			Double dblTime = dblHour + (dblMin / 60);
			Double dblWage = Double.valueOf(etWage.getText().toString());
			Double dblGrossSal = dblTime * dblWage;
			tvResult.setText(dblTime.toString() +"\n" + dblWage.toString() + "\nGross Salary: " + String.format("%.02f", dblGrossSal));
			//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(tvResult.getWindowToken(), 0);
		}
	});
	}

	private void setDefValues() {
		skbHours.setMax((intMaxHours - intMinHours) * 4);
		skbHours.setProgress((intDefHours - intMinHours) * 4);
		skbWage.setMax((int) ((dblMaxWage - dblMinWage) * 4));
		skbWage.setProgress((int) (dblDefWage - dblMinWage) * 4);
	}

	private void loadPref() {
		SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		intMaxHours = Integer.parseInt(mySharedPreferences.getString("max_hours", "60"));
		Log.d(TAG, "loadPref " + intMaxHours.toString());
		
//		intMaxHours= getPreferences(MODE_PRIVATE).getInt(getString(R.string.max_hours_pref), 50);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_settings) {
			if (Build.VERSION.SDK_INT < 11) {
			    startActivityForResult(new Intent(this, PrefActivity.class),0);
			} else {
			    startActivityForResult(new Intent(this, OtherPrefActivity.class),0);
			}
		}
//		if (item.getItemId() == R.id.about) {
//			Intent in = new Intent(HomeActivity.this, AboutActivity.class);
//			startActivity(in);
//			overridePendingTransition(R.anim.push_left_in, R.anim.push_up_out);
//
//		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		loadPref();
		setDefValues();
	}

	 public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) {
		 switch (bar.getId()){
		 case R.id.skbHours:
			if (!blnNoChange) {
				bln = true;
				int intMin = progress % 4;
				int intHour = Integer.valueOf((progress / 4) + intMinHours);
				String strMin = (intMin == 0)? "00" : String.valueOf(intMin * 15);
				String strHour = String.format("%02d", intHour);
				etHour.setText(strHour);
				etMin.setText(strMin);
			} else blnNoChange = false;
			break;
		 case R.id.skbWage:
			 if (!blnNoWageChange) {
				 
			 etWage.setText(String.format("%.02f", progress * 0.25 + dblMinWage));
			 break;
			 } else blnNoWageChange = false;
		 }
	 }

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
}