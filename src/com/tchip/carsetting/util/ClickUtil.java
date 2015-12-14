package com.tchip.carsetting.util;

public class ClickUtil {

	private static long lastClickTime;

	/**
	 * 
	 * @param clickMinSpan
	 *            两次点击至少间隔时间,单位:ms
	 * @return
	 */
	public static boolean isQuickClick(int clickMinSpan) {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < clickMinSpan) {
			MyLog.v("[ClickUtil]isQuickClick:Click Too Quickly!");
			return true;
		}
		lastClickTime = time;
		return false;
	}
}
