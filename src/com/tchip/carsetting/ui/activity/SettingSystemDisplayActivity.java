package com.tchip.carsetting.ui.activity;

import com.tchip.carsetting.Constant;
import com.tchip.carsetting.R;
import com.tchip.carsetting.model.Typefaces;
import com.tchip.carsetting.util.MyLog;
import com.tchip.carsetting.util.SettingUtil;
import com.tchip.carsetting.view.MaterialSwitch;
import com.tchip.carsetting.view.SwitchButton;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextClock;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

public class SettingSystemDisplayActivity extends Activity {

	private MaterialSwitch nightSwitch;
	private Context context;
	private RadioGroup screenOffGroup;
	private RadioButton screenOff30Second, screenOff1min, screenOff2min,
			screenOff10min, screenOffNone;

	private SharedPreferences sharedPreferences;
	private Editor editor;

	private SwitchButton switchAutolight;

	private RelativeLayout layoutSeekBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_setting_system_display);

		context = getApplicationContext();

		sharedPreferences = getSharedPreferences(Constant.MySP.NAME,
				Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();

		// 返回
		RelativeLayout layoutToSettingFromBright = (RelativeLayout) findViewById(R.id.layoutToSettingFromBright);
		layoutToSettingFromBright.setOnClickListener(new MyOnClickListener());

		// 时钟
		TextClock textClock = (TextClock) findViewById(R.id.textClock);
		textClock.setTypeface(Typefaces.get(this, Constant.Path.FONT
				+ "Font-Helvetica-Neue-LT-Pro.otf"));

		// 亮度SeekBar
		SeekBar brightSeekBar = (SeekBar) findViewById(R.id.brightSeekBar);
		brightSeekBar.setMax(Constant.Setting.MAX_BRIGHTNESS);
		brightSeekBar.setProgress(SettingUtil.getBrightness(context));
		brightSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				SettingUtil.setBrightness(context, seekBar.getProgress());
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				SettingUtil.setBrightness(context, progress);

			}
		});

		// 屏幕关闭RadioGroup
		iniRadioGroup();
		boolean isAutoLightSwitchOn = isAutoLightSwitchOn();
		layoutSeekBar = (RelativeLayout) findViewById(R.id.layoutSeekBar);
		hideOrShowSeekBarLayout(isAutoLightSwitchOn);

		// 亮度自动调节开关
		switchAutolight = (SwitchButton) findViewById(R.id.switchAutolight);
		switchAutolight.setChecked(isAutoLightSwitchOn);
		switchAutolight
				.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// Settings.System.putString(getContentResolver(),
						// Constant.FMTransmit.SETTING_ENABLE,
						// isChecked ? "1" : "0");
						editor.putBoolean("autoScreenLight", isChecked);
						editor.commit();
						hideOrShowSeekBarLayout(isChecked);

						SettingUtil.setAutoLight(context, isChecked);

						// 关闭自动亮度调节，重设亮度值
						if (!isChecked) {
							int manulLightValue = sharedPreferences.getInt(
									"manulLightValue",
									Constant.Setting.DEFAULT_BRIGHTNESS);
							MyLog.v("[SettingSystemDisplay]manulLightValue:"
									+ manulLightValue);
							SettingUtil.setBrightness(getApplicationContext(),
									manulLightValue - 1);

							SettingUtil.setBrightness(getApplicationContext(),
									manulLightValue + 1);

							SettingUtil.setBrightness(getApplicationContext(),
									manulLightValue);
						}
					}
				});
	}

	private void hideOrShowSeekBarLayout(boolean isAutoLightSwitchOn) {
		if (!isAutoLightSwitchOn) {
			layoutSeekBar.setVisibility(View.VISIBLE);
		} else {
			layoutSeekBar.setVisibility(View.GONE);
		}
	}

	private boolean isAutoLightSwitchOn() {
		return sharedPreferences.getBoolean("autoScreenLight",
				Constant.Setting.AUTO_BRIGHT_DEFAULT_ON);
	}

	class MyOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.layoutToSettingFromBright:
				finish();
				break;

			default:
				break;
			}
		}

	}

	private void iniRadioGroup() {
		screenOffGroup = (RadioGroup) findViewById(R.id.screenOffGroup);
		screenOffGroup
				.setOnCheckedChangeListener(new MyRadioOnCheckedListener());
		screenOff30Second = (RadioButton) findViewById(R.id.screenOff30Second);
		screenOff1min = (RadioButton) findViewById(R.id.screenOff1min);
		screenOff2min = (RadioButton) findViewById(R.id.screenOff2min);
		screenOff10min = (RadioButton) findViewById(R.id.screenOff10min);
		screenOffNone = (RadioButton) findViewById(R.id.screenOffNone);

		int nowScreenOffTime = SettingUtil.getScreenOffTime(context);
		switch (nowScreenOffTime) {
		case 30000:
			screenOff30Second.setChecked(true);
			break;

		case 60000:
			screenOff1min.setChecked(true);
			break;

		case 120000:
			screenOff2min.setChecked(true);
			break;

		case 600000:
			screenOff10min.setChecked(true);
			break;

		default:
			screenOffNone.setChecked(true);
			break;
		}
	}

	class MyRadioOnCheckedListener implements OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.screenOff30Second:
				SettingUtil.setScreenOffTime(context, 30000);
				break;
			case R.id.screenOff1min:
				SettingUtil.setScreenOffTime(context, 60000);
				break;
			case R.id.screenOff2min:
				SettingUtil.setScreenOffTime(context, 120000);
				break;
			case R.id.screenOff10min:
				SettingUtil.setScreenOffTime(context, 600000);
				break;
			case R.id.screenOffNone:
				SettingUtil.setScreenOffTime(context, Integer.MAX_VALUE);
				break;

			default:
				break;
			}
		}
	}

}
