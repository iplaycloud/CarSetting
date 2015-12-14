package com.tchip.carsetting.ui.activity;

import com.tchip.carsetting.Constant;
import com.tchip.carsetting.R;
import com.tchip.carsetting.Constant.Module;
import com.tchip.carsetting.Constant.MySP;
import com.tchip.carsetting.Constant.Path;
import com.tchip.carsetting.R.id;
import com.tchip.carsetting.R.layout;
import com.tchip.carsetting.model.Typefaces;
import com.tchip.carsetting.util.MyLog;
import com.tchip.carsetting.util.OpenUtil;
import com.tchip.carsetting.util.SettingUtil;
import com.tchip.carsetting.util.OpenUtil.MODULE_TYPE;
import com.tchip.carsetting.view.SwitchButton;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnLongClickListener;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class MainActivity extends Activity {

	private SharedPreferences preferences;
	private Editor editor;
	private WifiManager wifiManager;

	private SwitchButton switchWifi, switchParking;

	/** WiFi状态监听器 **/
	private IntentFilter wifiIntentFilter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);

		preferences = getSharedPreferences(Constant.MySP.NAME,
				Context.MODE_PRIVATE);
		editor = preferences.edit();

		// 返回
		RelativeLayout btnToViceFromSetting = (RelativeLayout) findViewById(R.id.btnToViceFromSetting);
		btnToViceFromSetting.setOnClickListener(new MyOnClickListener());

		// 时钟
		TextClock textClock = (TextClock) findViewById(R.id.textClock);
		textClock.setTypeface(Typefaces.get(this, Constant.Path.FONT
				+ "Font-Helvetica-Neue-LT-Pro.otf"));
		textClock.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				Intent intentMagic = new Intent(MainActivity.this,
						MagicActivity.class);
				startActivity(intentMagic);
				return false;
			}
		});

		// 用户中心
		RelativeLayout layoutUserCenter = (RelativeLayout) findViewById(R.id.layoutUserCenter);
		layoutUserCenter.setOnClickListener(new MyOnClickListener());
		View lineUserCenter = findViewById(R.id.lineUserCenter);
		if (!Constant.Module.hasUserCenter) {
			layoutUserCenter.setVisibility(View.GONE);
			lineUserCenter.setVisibility(View.GONE);
		}

		// 亮度设置
		RelativeLayout layoutRippleDisplay = (RelativeLayout) findViewById(R.id.layoutRippleDisplay);
		layoutRippleDisplay.setOnClickListener(new MyOnClickListener());

		// Wi-Fi
		RelativeLayout layoutRippleWifi = (RelativeLayout) findViewById(R.id.layoutRippleWifi);
		layoutRippleWifi.setOnClickListener(new MyOnClickListener());
		switchWifi = (SwitchButton) findViewById(R.id.switchWifi);
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		switchWifi.setChecked(wifiManager.isWifiEnabled());
		switchWifi.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				wifiManager.setWifiEnabled(isChecked);
			}
		});

		wifiIntentFilter = new IntentFilter();
		wifiIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		// wifiIntentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
		wifiIntentFilter.setPriority(Integer.MAX_VALUE);
		// 注册wifi消息处理器
		registerReceiver(wifiIntentReceiver, wifiIntentFilter);

		// 热点共享
		RelativeLayout layoutWifiAp = (RelativeLayout) findViewById(R.id.layoutWifiAp);
		layoutWifiAp.setOnClickListener(new MyOnClickListener());

		// 流量使用情况
		RelativeLayout layoutRippleTraffic = (RelativeLayout) findViewById(R.id.layoutRippleTraffic);
		layoutRippleTraffic.setOnClickListener(new MyOnClickListener());

		// 碰撞侦测
		RelativeLayout layoutGravity = (RelativeLayout) findViewById(R.id.layoutGravity);
		layoutGravity.setOnClickListener(new MyOnClickListener());

		// 停车侦测开关
		switchParking = (SwitchButton) findViewById(R.id.switchParking);
		switchParking.setChecked(isParkingMonitorOn());
		switchParking.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				SettingUtil.setParkingMonitor(MainActivity.this, isChecked);
			}
		});
		RelativeLayout layoutRippleParking = (RelativeLayout) findViewById(R.id.layoutRippleParking);
		layoutRippleParking.setOnClickListener(new MyOnClickListener());

		// 位置信息
		RelativeLayout layoutRippleLocation = (RelativeLayout) findViewById(R.id.layoutRippleLocation);
		layoutRippleLocation.setOnClickListener(new MyOnClickListener());

		// 存储
		RelativeLayout layoutRippleStorage = (RelativeLayout) findViewById(R.id.layoutRippleStorage);
		layoutRippleStorage.setOnClickListener(new MyOnClickListener());

		// USB连接设置
		RelativeLayout layoutRippleUsb = (RelativeLayout) findViewById(R.id.layoutRippleUsb);
		layoutRippleUsb.setOnClickListener(new MyOnClickListener());

		// 日期
		RelativeLayout layoutRippleDate = (RelativeLayout) findViewById(R.id.layoutRippleDate);
		layoutRippleDate.setOnClickListener(new MyOnClickListener());

		// 声音
		RelativeLayout layoutRippleSound = (RelativeLayout) findViewById(R.id.layoutRippleSound);
		layoutRippleSound.setOnClickListener(new MyOnClickListener());

		// FM(GONE)
		RelativeLayout layoutRippleFm = (RelativeLayout) findViewById(R.id.layoutRippleFm);
		layoutRippleFm.setOnClickListener(new MyOnClickListener());

		// 恢复出厂设置(GONE)
		RelativeLayout layoutRippleReset = (RelativeLayout) findViewById(R.id.layoutRippleReset);
		layoutRippleReset.setOnClickListener(new MyOnClickListener());

		// 关于设备
		RelativeLayout layoutRippleAbout = (RelativeLayout) findViewById(R.id.layoutRippleAbout);
		layoutRippleAbout.setOnClickListener(new MyOnClickListener());

		// 应用(GONE)
		RelativeLayout layoutRippleApp = (RelativeLayout) findViewById(R.id.layoutRippleApp);
		layoutRippleApp.setOnClickListener(new MyOnClickListener());

		// 拷贝地图(GONE)
		RelativeLayout layoutCopyMap = (RelativeLayout) findViewById(R.id.layoutCopyMap);
		layoutCopyMap.setOnClickListener(new MyOnClickListener());
	}

	/**
	 * 停车侦测是否打开
	 * 
	 * @return
	 */
	private boolean isParkingMonitorOn() {
		return preferences.getBoolean(Constant.MySP.STR_PARKING_ON,
				Constant.Module.parkDefaultOn);
	}

	class MyOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.layoutRippleParking:
				switchParking.setChecked(!isParkingMonitorOn());
				break;

			case R.id.layoutCopyMap:
				break;

			case R.id.layoutRippleDisplay:
				OpenUtil.openModule(MainActivity.this,
						MODULE_TYPE.SETTING_DISPLAY);
				break;

			case R.id.layoutRippleWifi:
				OpenUtil.openModule(MainActivity.this, MODULE_TYPE.WIFI);
				break;

			case R.id.layoutWifiAp:
				OpenUtil.openModule(MainActivity.this, MODULE_TYPE.WIFI_AP);
				break;

			case R.id.layoutRippleTraffic:
				OpenUtil.openModule(MainActivity.this,
						MODULE_TYPE.SETTING_DATA_USAGE);
				break;

			case R.id.layoutGravity:
				Intent intentGravity = new Intent(MainActivity.this,
						SettingGravityActivity.class);
				startActivity(intentGravity);
				break;

			case R.id.layoutRippleLocation:
				OpenUtil.openModule(MainActivity.this,
						MODULE_TYPE.SETTING_LOCATION);
				break;

			case R.id.layoutRippleStorage:
				OpenUtil.openModule(MainActivity.this,
						MODULE_TYPE.SETTING_STORAGE);
				break;

			case R.id.layoutRippleUsb:
				// startActivity(new Intent(
				// "android.settings.STORAGE_USB_SETTINGS"));
				Intent intentUsb = new Intent(MainActivity.this,
						SettingSystemUsbActivity.class);
				startActivity(intentUsb);
				break;

			case R.id.layoutRippleDate:
				OpenUtil.openModule(MainActivity.this, MODULE_TYPE.SETTING_DATE);
				break;

			case R.id.layoutRippleSound:
				OpenUtil.openModule(MainActivity.this,
						MODULE_TYPE.SETTING_VOLUME);
				break;

			case R.id.layoutRippleFm:
				OpenUtil.openModule(MainActivity.this, MODULE_TYPE.SETTING_FM);
				break;

			case R.id.layoutRippleReset:
				OpenUtil.openModule(MainActivity.this,
						MODULE_TYPE.SETTING_RESET);
				break;

			case R.id.layoutRippleAbout:
				OpenUtil.openModule(MainActivity.this,
						MODULE_TYPE.SETTING_ABOUT);
				break;

			case R.id.layoutRippleApp:
				OpenUtil.openModule(MainActivity.this, MODULE_TYPE.SETTING_APP);
				break;

			default:
				break;
			}
		}
	}

	/**
	 * WiFi状态Receiver
	 */
	private BroadcastReceiver wifiIntentReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			int wifi_state = intent.getIntExtra("wifi_state", 0);

			switch (wifi_state) {
			case WifiManager.WIFI_STATE_ENABLED:
			case WifiManager.WIFI_STATE_ENABLING:
			case WifiManager.WIFI_STATE_DISABLING:
			case WifiManager.WIFI_STATE_DISABLED:
			case WifiManager.WIFI_STATE_UNKNOWN:
				switchWifi.setChecked(wifiManager.isWifiEnabled());
				break;
			}
		}
	};

	@Override
	public void onResume() {
		MyLog.v("[SettingFragment]onResume");
		super.onResume();
	}

	@Override
	public void onPause() {
		MyLog.v("[SettingFragment]onPause");
		super.onPause();
	}

	@Override
	public void onDestroy() {
		MyLog.v("[SettingFragment]onPause");

		// 取消注册wifi消息处理器
		if (wifiIntentReceiver != null) {
			unregisterReceiver(wifiIntentReceiver);
		}

		super.onDestroy();
	}

}
