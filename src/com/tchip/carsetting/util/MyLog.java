package com.tchip.carsetting.util;

import com.tchip.carsetting.Constant;

import android.util.Log;

public class MyLog {

	public static void e(String log) {
		if (Constant.isDebug)
			Log.e(Constant.TAG, log);
	}

	public static void v(String log) {
		if (Constant.isDebug)
			Log.v(Constant.TAG, log);
	}

	public static void d(String log) {
		if (Constant.isDebug)
			Log.d(Constant.TAG, log);
	}

	public static void i(String log) {
		if (Constant.isDebug)
			Log.i(Constant.TAG, log);
	}

	public static void w(String log) {
		if (Constant.isDebug)
			Log.w(Constant.TAG, log);
	}

}
