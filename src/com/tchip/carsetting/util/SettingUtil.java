package com.tchip.carsetting.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import com.tchip.carsetting.Constant;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.telephony.TelephonyManager;
import android.util.Log;

public class SettingUtil {

	/**
	 * 调整系统亮度
	 * 
	 * @param brightness
	 */
	public static void setBrightness(Context context, int brightness) {
		if (brightness <= Constant.Setting.MAX_BRIGHTNESS && brightness > -1) {
			boolean setSuccess = Settings.System.putInt(
					context.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS, brightness);
			MyLog.v("[SettingUtil]setBrightness: " + brightness + ", "
					+ setSuccess);

			SharedPreferences sharedPreferences = context.getSharedPreferences(
					Constant.MySP.NAME, Context.MODE_PRIVATE);
			Editor editor = sharedPreferences.edit();

			editor.putInt(Constant.MySP.STR_MANUL_LIGHT_VALUE, brightness);
			editor.commit();
		}
	}

	public static int getBrightness(Context context) {
		try {
			int nowBrightness = Settings.System.getInt(
					context.getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS);
			MyLog.v("[SettingUtil]nowBrightness:" + nowBrightness);
			return nowBrightness;
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
			return Constant.Setting.DEFAULT_BRIGHTNESS;
		}
	}

	public static void setScreenOffTime(Context context, int time) {
		Settings.System.putInt(context.getContentResolver(),
				android.provider.Settings.System.SCREEN_OFF_TIMEOUT, time);
	}

	public static int getScreenOffTime(Context context) {
		try {
			return Settings.System.getInt(context.getContentResolver(),
					Settings.System.SCREEN_OFF_TIMEOUT);
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
			return 155;
		}
	}

	/**
	 * FM发射开关节点
	 * 
	 * 1：开 0：关
	 */
	public static File nodeFmEnable = new File(
			"/sys/devices/platform/mt-i2c.1/i2c-1/1-002c/enable_qn8027");

	/**
	 * FM发射频率节点
	 * 
	 * 频率范围：7600~10800:8750-10800
	 */
	public static File nodeFmChannel = new File(
			"/sys/devices/platform/mt-i2c.1/i2c-1/1-002c/setch_qn8027");

	public static boolean isFmTransmitOn(Context context) {
		boolean isFmTransmitOpen = false;
		String fmEnable = Settings.System.getString(
				context.getContentResolver(),
				Constant.FMTransmit.SETTING_ENABLE);
		if (fmEnable.trim().length() > 0) {
			if ("1".equals(fmEnable)) {
				isFmTransmitOpen = true;
			} else {
				isFmTransmitOpen = false;
			}
		}
		return isFmTransmitOpen;
	}

	/**
	 * 获取设置中存取的频率
	 * 
	 * @return 8750-10800
	 */
	public static int getFmFrequceny(Context context) {
		String fmChannel = Settings.System.getString(
				context.getContentResolver(),
				Constant.FMTransmit.SETTING_CHANNEL);

		return Integer.parseInt(fmChannel);
	}

	/**
	 * 设置FM发射频率:8750-10800
	 * 
	 * @param frequency
	 */
	public static void setFmFrequency(Context context, int frequency) {
		if (frequency >= 8750 || frequency <= 10800) {
			Settings.System.putString(context.getContentResolver(),
					Constant.FMTransmit.SETTING_CHANNEL, "" + frequency);

			SaveFileToNode(nodeFmChannel, String.valueOf(frequency));
			MyLog.v("[SettingUtil]:Set FM Frequency success:" + frequency
					/ 100.0f + "MHz");

		}
	}

	public static void SaveFileToNode(File file, String value) {
		if (file.exists()) {
			try {
				StringBuffer strbuf = new StringBuffer("");
				strbuf.append(value);
				OutputStream output = null;
				OutputStreamWriter outputWrite = null;
				PrintWriter print = null;

				try {
					output = new FileOutputStream(file);
					outputWrite = new OutputStreamWriter(output);
					print = new PrintWriter(outputWrite);
					print.print(strbuf.toString());
					print.flush();
					output.close();

				} catch (FileNotFoundException e) {
					e.printStackTrace();
					Log.e(Constant.TAG, "SaveFileToNode:output error");
				}
			} catch (IOException e) {
				Log.e(Constant.TAG, "SaveFileToNode:IO Exception");
			}
		} else {
			Log.e(Constant.TAG, "SaveFileToNode:File:" + file + "not exists");
		}
	}

	/**
	 * 点亮屏幕
	 * 
	 * @param context
	 */
	public static void lightScreen(Context context) {
		// 获取电源管理器对象
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);

		// 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
		PowerManager.WakeLock wl = pm.newWakeLock(
				PowerManager.ACQUIRE_CAUSES_WAKEUP
						| PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");

		wl.acquire(); // 点亮屏幕
		wl.release(); // 释放

		// 得到键盘锁管理器对象
		KeyguardManager km = (KeyguardManager) context
				.getSystemService(Context.KEYGUARD_SERVICE);

		// 参数是LogCat里用的Tag
		KeyguardLock kl = km.newKeyguardLock("ZMS");

		kl.disableKeyguard();
	}

	/**
	 * Camera自动调节亮度节点
	 * 
	 * 1：开 0：关;默认打开
	 */
	public static File fileAutoLightSwitch = new File(
			"/sys/devices/platform/mt-i2c.1/i2c-1/1-007f/back_car_status");

	/**
	 * 设置Camera自动调节亮度开关
	 */
	public static void setAutoLight(Context context, boolean isAutoLightOn) {
		if (isAutoLightOn) {
			SaveFileToNode(fileAutoLightSwitch, "1");
		} else {
			SaveFileToNode(fileAutoLightSwitch, "0");
		}
		MyLog.v("[SettingUtil]setAutoLight:" + isAutoLightOn);
	}

	/**
	 * 停车侦测开关节点
	 * 
	 * 2：打开
	 * 
	 * 3：关闭
	 * 
	 * 默认关闭
	 */
	public static File fileParkingMonitor = new File(
			"/sys/devices/platform/mt-i2c.1/i2c-1/1-007f/back_car_status");

	public static void setParkingMonitor(Context context, boolean isParkingOn) {
		if (isParkingOn) {
			SaveFileToNode(fileParkingMonitor, "2");
		} else {
			SaveFileToNode(fileParkingMonitor, "3");
		}

		SharedPreferences sharedPreferences = context.getSharedPreferences(
				Constant.MySP.NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();

		editor.putBoolean(Constant.MySP.STR_PARKING_ON, isParkingOn);
		editor.commit();
		MyLog.v("[SettingUtil]setParkingMonitor:" + isParkingOn);
	}

	/**
	 * ACC状态节点
	 */
	public static File fileAccStatus = new File(
			"/sys/devices/platform/mt-i2c.1/i2c-1/1-007f/acc_car_status");

	/**
	 * 获取ACC状态
	 * 
	 * @return 0:ACC下电
	 * 
	 *         1:ACC上电
	 */
	public static int getAccStatus() {
		return getFileInt(fileAccStatus);
	}

	public static int getFileInt(File file) {

		if (file.exists()) {
			try {
				InputStream is = new FileInputStream(file);
				InputStreamReader fr = new InputStreamReader(is);
				int ch = 0;
				if ((ch = fr.read()) != -1)
					return Integer.parseInt(String.valueOf((char) ch));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	/**
	 * 获取背光亮度值
	 */
	public static int getLCDValue() {
		/** 背光值节点 **/
		File fileLCDValue = new File("/sys/class/leds/lcd-backlight/brightness");

		String strValue = "";
		if (fileLCDValue.exists()) {
			try {
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(fileLCDValue), "utf-8");
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTXT = null;
				while ((lineTXT = bufferedReader.readLine()) != null) {
					strValue += lineTXT.toString();
				}
				read.close();

				return Integer.parseInt(strValue);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				MyLog.e("[SettingUtil]getLCDValue: FileNotFoundException");
			} catch (IOException e) {
				e.printStackTrace();
				MyLog.e("[SettingUtil]getLCDValue: IOException");
			}
		}
		return -5;
	}

	/**
	 * 电子狗电源开关节点
	 * 
	 * 1-打开
	 * 
	 * 0-关闭
	 */
	public static File fileEDogPower = new File(
			"/sys/devices/platform/mt-i2c.1/i2c-1/1-007f/edog_car_status");

	/**
	 * 设置电子狗电源开关
	 * 
	 * @param isEDogOn
	 */
	public static void setEDogEnable(boolean isEDogOn) {

		MyLog.v("[SettingUtil]setEDogEnable:" + isEDogOn);
		if (isEDogOn) {
			SaveFileToNode(fileEDogPower, "1");
		} else {
			SaveFileToNode(fileEDogPower, "0");
		}
	}

	/**
	 * 获取Mac地址
	 * 
	 * @param context
	 * @return
	 */
	public String getLocalMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

	/**
	 * 获取设备IMEI
	 * 
	 * @param context
	 * @return
	 */
	public String getImei(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}

	/**
	 * 获取本机IP地址
	 * 
	 * @return
	 */
	public String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("WifiPreference IpAddress", ex.toString());
		}
		return null;
	}

	public static float getGravityVauleBySensitive(int sensitive) {

		if (sensitive == Constant.GravitySensor.SENSITIVE_LOW) {
			return Constant.GravitySensor.VALUE_LOW;
		} else if (sensitive == Constant.GravitySensor.SENSITIVE_MIDDLE) {
			return Constant.GravitySensor.VALUE_MIDDLE;
		} else {
			return Constant.GravitySensor.VALUE_HIGH;
		}
	}

	/**
	 * 
	 * @param context
	 * @param type
	 *            AudioManager.STREAM_MUSIC;STREAM_RING
	 * @param step
	 *            增加音量
	 */
	public static void plusVolume(Context context, int type, int step) {
		AudioManager audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);

		int nowVolume = audioManager.getStreamVolume(type);
		int toVolume = nowVolume + step;
		if (toVolume <= 15)
			audioManager.setStreamVolume(type, toVolume, 0);
		else
			audioManager.setStreamVolume(type, 15, 0);
	}

	/**
	 * 
	 * @param context
	 * @param type
	 *            AudioManager.STREAM_MUSIC;STREAM_RING
	 * @param step
	 */
	public static void minusVolume(Context context, int type, int step) {
		AudioManager audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		int nowVolume = audioManager.getStreamVolume(type);
		int toVolume = nowVolume - step;
		if (toVolume > 0)
			audioManager.setStreamVolume(type, toVolume, 0);
		else
			audioManager.setStreamVolume(type, 0, 0);
	}

	/**
	 * 设置最大音量
	 * 
	 * @param context
	 *            AudioManager.STREAM_MUSIC;STREAM_RING
	 * @param type
	 */
	public static void setMaxVolume(Context context, int type) {
		AudioManager audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		audioManager.setStreamVolume(type, 15, 0);
	}

	/**
	 * 设置最小音量
	 * 
	 * @param context
	 *            AudioManager.STREAM_MUSIC;STREAM_RING
	 * @param type
	 */
	public static void setMinVolume(Context context, int type) {
		AudioManager audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		audioManager.setStreamVolume(type, 0, 0);
	}

	/**
	 * 静音
	 * 
	 * @param context
	 */
	public static void setMute(Context context) {
		AudioManager audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		audioManager.setRingerMode(audioManager.RINGER_MODE_SILENT);
	}

	/**
	 * 关闭静音
	 */
	public static void setUnmute(Context context, int type) {
		AudioManager audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		audioManager.setStreamVolume(type, 8, 0);
	}
}
