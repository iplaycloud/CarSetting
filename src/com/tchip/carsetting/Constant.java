package com.tchip.carsetting;

public interface Constant {
	/**
	 * Debug：打印Log
	 */
	public static final boolean isDebug = true;

	/**
	 * 日志Tag
	 */
	public static final String TAG = "ZMS";

	/**
	 * SharedPreferences名称
	 */
	public static final class MySP {
		/** 名称 **/
		public static final String NAME = "CarSetting";

		/** 是否开机自动录像[boolean:true] **/
		public static final String STR_AUTO_RECORD = "autoRecord";

		/** 停车侦测是否打开[boolean:true] **/
		public static final String STR_PARKING_ON = "parkingOn";

		/** 手动设置的亮度[int] **/
		public static final String STR_MANUL_LIGHT_VALUE = "manulLightValue";
	}

	/**
	 * 广播
	 */
	public static final class Broadcast {
		/** ACC上电 **/
		public static final String ACC_ON = "com.tchip.ACC_ON";

		/** ACC下电 **/
		public static final String ACC_OFF = "com.tchip.ACC_OFF";

		/** 进入休眠 **/
		public static final String SLEEP_ON = "com.tchip.SLEEP_ON";

		/** 取消休眠 **/
		public static final String SLEEP_OFF = "com.tchip.SLEEP_OFF";

	}

	public static final class Setting {

		/**
		 * 最大亮度
		 */
		public static final int MAX_BRIGHTNESS = 196; // 255;

		/**
		 * 默认亮度
		 */
		public static final int DEFAULT_BRIGHTNESS = 180;

		/**
		 * Camera自动调节亮度是否打开
		 */
		public static final boolean AUTO_BRIGHT_DEFAULT_ON = false;
	}

	public static final class GravitySensor {
		/**
		 * 碰撞侦测是否默认打开
		 */
		public static final boolean DEFAULT_ON = true;

		/**
		 * 碰撞侦测默认灵敏度Level
		 */
		public static final float VALUE = 9.8f;

		public static final int SENSITIVE_LOW = 0;
		public static final int SENSITIVE_MIDDLE = 1;
		public static final int SENSITIVE_HIGH = 2;
		public static final int SENSITIVE_DEFAULT = SENSITIVE_MIDDLE;

		public static final float VALUE_LOW = VALUE * 1.8f;
		public static final float VALUE_MIDDLE = VALUE * 1.5f;
		public static final float VALUE_HIGH = VALUE * 1;
		public static final float VALUE_DEFAULT = VALUE_MIDDLE;

	}

	public static final class Module {

		/**
		 * 进入MagicActivity的密码
		 */
		public static final String MagicCode = "55555";

		/**
		 * 是否有用户中心
		 */
		public static final boolean hasUserCenter = false;

		/**
		 * 是否有拨号短信模块
		 */
		public static final boolean hasDialer = false;

		/** 停车侦测是否默认打开 **/
		public static final boolean parkDefaultOn = true;

		/** 是否是公版 */
		public static final boolean isPublic = false;

		/** 是否显示APN设置 */
		public static final boolean hasAPNSetting = isPublic;
	}

	/**
	 * FM发射
	 */
	public static final class FMTransmit {
		/**
		 * 系统设置：FM发射开关
		 */
		public static final String SETTING_ENABLE = "fm_transmitter_enable";

		/**
		 * 系统设置：FM发射频率
		 */
		public static final String SETTING_CHANNEL = "fm_transmitter_channel";
	}

	/**
	 * 路径
	 */
	public static final class Path {

		/**
		 * 字体目录
		 */
		public static final String FONT = "fonts/";
	}

	/**
	 * 设置条目点击波纹速度
	 */
	public static final int SETTING_ITEM_RIPPLE_SPEED = 80;
}
