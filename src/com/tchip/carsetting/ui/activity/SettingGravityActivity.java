package com.tchip.carsetting.ui.activity;

import com.tchip.carsetting.Constant;
import com.tchip.carsetting.MyApplication;
import com.tchip.carsetting.R;
import com.tchip.carsetting.util.MyLog;
import com.tchip.carsetting.view.SwitchButton;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingGravityActivity extends Activity {

	private TextView textHint;
	private SharedPreferences sharedPreferences;
	private Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		View decorView = getWindow().getDecorView();
		decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
		setContentView(R.layout.activity_setting_gravity);

		sharedPreferences = getSharedPreferences(Constant.MySP.NAME,
				Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();

		initialLayout();
	}

	private void initialLayout() {
		textHint = (TextView) findViewById(R.id.textHint);
		SwitchButton switchGravity = (SwitchButton) findViewById(R.id.switchGravity);
		switchGravity.setChecked(isGravityOn());
		switchGravity.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				editor.putBoolean("crashOn", isChecked);
				editor.commit();
				MyApplication.isCrashOn = isChecked;

				// 通知CarLauncher
				sendBroadcast(new Intent("com.tchip.SETTING_SYNC").putExtra(
						"content", isChecked ? "crashOn" : "crashOff"));
			}
		});

		RelativeLayout layoutBack = (RelativeLayout) findViewById(R.id.layoutBack);
		layoutBack.setOnClickListener(new MyOnClickListener());

		SeekBar gravitySeekBar = (SeekBar) findViewById(R.id.gravitySeekBar);
		gravitySeekBar.setMax(2);
		gravitySeekBar.setProgress(getGravityLevel());

		gravitySeekBar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						int crashSensitive = seekBar.getProgress();
						MyLog.v("[SettingGravity] Set crash sensitive:"
								+ crashSensitive);
						MyApplication.crashSensitive = crashSensitive;
						editor.putInt("crashSensitive", crashSensitive);
						editor.commit();

						// 通知CarLauncher
						String content = "crashMiddle";
						switch (crashSensitive) {
						case 0:
							content = "crashLow";
							break;

						case 2:
							content = "crashHigh";
							break;

						case 1:
						default:
							content = "crashMiddle";
							break;
						}
						sendBroadcast(new Intent("com.tchip.SETTING_SYNC")
								.putExtra("content", content));
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
					}
				});
	}

	/**
	 * 碰撞侦测是否打开
	 */
	private boolean isGravityOn() {
		boolean isGravityOn = sharedPreferences.getBoolean("crashOn",
				Constant.GravitySensor.DEFAULT_ON);
		return isGravityOn;
	}

	/**
	 * 获取当前设置的碰撞等级
	 */
	private int getGravityLevel() {
		int crashSensitive = sharedPreferences.getInt("crashSensitive",
				Constant.GravitySensor.SENSITIVE_DEFAULT);
		return crashSensitive;
	}

	class MyOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.layoutBack:
				finish();
				break;
			}
		}
	}

}
