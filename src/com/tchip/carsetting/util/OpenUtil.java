package com.tchip.carsetting.util;

import com.tchip.carsetting.ui.activity.SettingSystemDisplayActivity;
import com.tchip.carsetting.ui.activity.SettingSystemVolumeActivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;

public class OpenUtil {

	public enum MODULE_TYPE {
		/** 设备测试 **/
		DEVICE_TEST,

		/** 工程模式 **/
		ENGINEER_MODE,

		/** 关于 **/
		SETTING_ABOUT,

		/** 应用 **/
		SETTING_APP,

		/** 流量使用情况 **/
		SETTING_DATA_USAGE,

		/** 日期和时间 **/
		SETTING_DATE,

		/** 显示设置 **/
		SETTING_DISPLAY,

		/** FM发射设置 **/
		SETTING_FM,

		/** 位置 **/
		SETTING_LOCATION,

		/** 音量设置 **/
		SETTING_VOLUME,

		/** 备份和重置 **/
		SETTING_RESET,

		/** 存储设置 **/
		SETTING_STORAGE,

		/** 系统设置 **/
		SETTING_SYSTEM,

		/** Wi-Fi **/
		WIFI,

		/** Wi-Fi热点 **/
		WIFI_AP
	}

	public static void openModule(Activity activity, MODULE_TYPE moduleTye) {
		switch (moduleTye) {

		case DEVICE_TEST:
			try {
				Intent intentDeviceTest = new Intent(Intent.ACTION_VIEW);
				intentDeviceTest.setClassName("com.DeviceTest",
						"com.DeviceTest.DeviceTest");
				intentDeviceTest.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				activity.startActivity(intentDeviceTest);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case ENGINEER_MODE:
			try {
				Intent intentEngineerMode = new Intent(Intent.ACTION_VIEW);
				intentEngineerMode.setClassName("com.mediatek.engineermode",
						"com.mediatek.engineermode.EngineerMode");
				intentEngineerMode.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				activity.startActivity(intentEngineerMode);
			} catch (Exception e) {

			}
			break;

		case SETTING_ABOUT:
			try {
				activity.startActivity(new Intent(
						android.provider.Settings.ACTION_DEVICE_INFO_SETTINGS));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case SETTING_APP:
			try {
				activity.startActivity(new Intent(
						android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case SETTING_DATA_USAGE:
			try {
				activity.startActivity(new Intent(
						"android.settings.DATA_USAGE_SETTINGS"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case SETTING_DATE:
			try {
				activity.startActivity(new Intent(
						android.provider.Settings.ACTION_DATE_SETTINGS));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case SETTING_DISPLAY:
			Intent intentDisplay = new Intent(activity,
					SettingSystemDisplayActivity.class);
			activity.startActivity(intentDisplay);
			break;

		case SETTING_FM:
			try {
				activity.startActivity(new Intent(
						"android.settings.FM_SETTINGS"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case SETTING_LOCATION:
			try {
				activity.startActivity(new Intent(
						android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case SETTING_VOLUME:
			// startActivity(new Intent(
			// android.provider.Settings.ACTION_SOUND_SETTINGS));
			Intent intentVolume = new Intent(activity,
					SettingSystemVolumeActivity.class);
			activity.startActivity(intentVolume);
			break;

		case SETTING_RESET:
			try {
				activity.startActivity(new Intent(
						"android.settings.BACKUP_AND_RESET_SETTINGS"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case SETTING_STORAGE:
			try {
				activity.startActivity(new Intent(
						android.provider.Settings.ACTION_MEMORY_CARD_SETTINGS));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case SETTING_SYSTEM:
			try {
				ComponentName componentSetting = new ComponentName(
						"com.android.settings", "com.android.settings.Settings");
				Intent intentSetting = new Intent();
				intentSetting.setComponent(componentSetting);
				activity.startActivity(intentSetting);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case WIFI:
			if (!ClickUtil.isQuickClick(800)) {
				try {
					activity.startActivity(new Intent(
							android.provider.Settings.ACTION_WIFI_SETTINGS));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;

		case WIFI_AP:
			try {
				activity.startActivity(new Intent(
						"android.settings.TETHER_WIFI_SETTINGS"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		default:
			break;
		}
	}

}
