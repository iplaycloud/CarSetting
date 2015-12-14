package com.tchip.carsetting.ui.activity;

import com.tchip.carsetting.Constant;
import com.tchip.carsetting.R;
import com.tchip.carsetting.model.Typefaces;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextClock;
import android.widget.RelativeLayout;

public class SettingSystemUsbActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_setting_system_usb);

		// 返回
		RelativeLayout layoutToSettingFromBright = (RelativeLayout) findViewById(R.id.layoutToSettingFromBright);
		layoutToSettingFromBright.setOnClickListener(new MyOnClickListener());

		// 时钟
		TextClock textClock = (TextClock) findViewById(R.id.textClock);
		textClock.setTypeface(Typefaces.get(this, Constant.Path.FONT
				+ "Font-Helvetica-Neue-LT-Pro.otf"));

		RelativeLayout layoutStepOne = (RelativeLayout) findViewById(R.id.layoutStepOne);
		layoutStepOne.setOnClickListener(new MyOnClickListener());

		RelativeLayout layoutStepTwo = (RelativeLayout) findViewById(R.id.layoutStepTwo);
		layoutStepTwo.setOnClickListener(new MyOnClickListener());
	}

	class MyOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.layoutToSettingFromBright:
				finish();
				break;

			case R.id.layoutStepOne:
				try {
					startActivity(new Intent(
							"android.settings.STORAGE_USB_SETTINGS"));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case R.id.layoutStepTwo:
				// Intent intent = new Intent();
				// ComponentName comp = new
				// ComponentName("com.android.systemui",
				// "com.android.systemui.usb.UsbStorageActivity");
				// intent.setComponent(comp);
				// startActivity(intent);

				sendBroadcast(new Intent(
						"tchip.intent.action.ACTION_SYSTEMUI_USB"));
				break;

			default:
				break;
			}
		}

	}

}
