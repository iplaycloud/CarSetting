package com.tchip.carsetting.ui.activity;

import com.tchip.carsetting.R;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SettingSystemVolumeActivity extends Activity {

	private AudioManager audioManager;
	private int secondCount = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_setting_system_volume);

		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		// 媒体音量SeekBar
		SeekBar volumeMedia = (SeekBar) findViewById(R.id.volumeMedia);
		volumeMedia.setMax(audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		volumeMedia.setProgress(audioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC));
		volumeMedia.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
						seekBar.getProgress(), 0);

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				secondCount = 1;
			}
		});

		// 铃声音量SeekBar

		SeekBar volumeRing = (SeekBar) findViewById(R.id.volumeRing);

		volumeRing.setMax(audioManager
				.getStreamMaxVolume(AudioManager.STREAM_RING));
		volumeRing.setProgress(audioManager
				.getStreamVolume(AudioManager.STREAM_RING));
		volumeRing.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				audioManager.setStreamVolume(AudioManager.STREAM_RING,
						seekBar.getProgress(), 0);

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

			}
		});

		new Thread(new AutoFinishThread()).start();
	}

	/**
	 * 无操作3秒后关闭音量调节界面
	 */
	class AutoFinishThread implements Runnable {

		@Override
		public void run() {
			while (secondCount < 5) {
				try {
					Thread.sleep(1000);
					secondCount++;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (secondCount >= 3) {
					Message messageFinish = new Message();
					messageFinish.what = 1;
					autoFinishHandler.sendMessage(messageFinish);
				}
			}

		}
	}

	final Handler autoFinishHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				finish();
				break;

			default:
				break;
			}
		}
	};

}
