package com.tchip.carsetting.ui.activity;

import com.tchip.carsetting.Constant;
import com.tchip.carsetting.R;
import com.tchip.carsetting.util.OpenUtil;
import com.tchip.carsetting.util.OpenUtil.MODULE_TYPE;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class MagicActivity extends Activity {
	private EditText textPass;
	private Button btnGo, btnBack;
	private RelativeLayout layoutMagic, layoutBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setBackgroundDrawable(null);
		setContentView(R.layout.activity_magic);

		initialLayout();
	}

	private void initialLayout() {
		layoutMagic = (RelativeLayout) findViewById(R.id.layoutMagic);
		layoutBack = (RelativeLayout) findViewById(R.id.layoutBack);
		layoutBack.setOnClickListener(new MyOnClickListener());
		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new MyOnClickListener());

		textPass = (EditText) findViewById(R.id.textPass);
		btnGo = (Button) findViewById(R.id.btnGo);
		btnGo.setOnClickListener(new MyOnClickListener());

		Button btnDeviceTest = (Button) findViewById(R.id.btnDeviceTest);
		btnDeviceTest.setOnClickListener(new MyOnClickListener());

		Button btnEngineerMode = (Button) findViewById(R.id.btnEngineerMode);
		btnEngineerMode.setOnClickListener(new MyOnClickListener());

		Button btnSystemSetting = (Button) findViewById(R.id.btnSystemSetting);
		btnSystemSetting.setOnClickListener(new MyOnClickListener());

		Button btnMtkLogger = (Button) findViewById(R.id.btnMtkLogger);
		btnMtkLogger.setOnClickListener(new MyOnClickListener());

		Button btnCPUInfo = (Button) findViewById(R.id.btnCPUInfo);
		btnCPUInfo.setOnClickListener(new MyOnClickListener());
	}

	class MyOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnGo:
				String strInput = textPass.getText().toString();
				if (Constant.Module.MagicCode.equals(strInput)) {
					layoutMagic.setVisibility(View.VISIBLE);
				} else {
					textPass.setText("");
				}
				break;

			case R.id.btnDeviceTest:
				OpenUtil.openModule(MagicActivity.this, MODULE_TYPE.DEVICE_TEST);
				break;

			case R.id.btnEngineerMode:
				OpenUtil.openModule(MagicActivity.this,
						MODULE_TYPE.ENGINEER_MODE);
				break;

			case R.id.btnSystemSetting:
				OpenUtil.openModule(MagicActivity.this,
						MODULE_TYPE.SETTING_SYSTEM);
				break;

			case R.id.btnMtkLogger:
				OpenUtil.openModule(MagicActivity.this, MODULE_TYPE.MTK_LOGGER);
				break;

			case R.id.btnCPUInfo:
				OpenUtil.openModule(MagicActivity.this, MODULE_TYPE.CPU_INFO);
				break;

			case R.id.layoutBack:
			case R.id.btnBack:
				finish();
				break;

			default:
				break;
			}

		}

	}

}
