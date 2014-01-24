package com.android13.shooting;

import java.util.HashMap;

import android.content.Context;
import android.media.SoundPool;

public class PlaySound {

	private static HashMap<String, Integer> hashMap;
	//private static Context context;
	public static SoundPool soundPool;

	public static void init(Context context, int maxStreams, int streamType,
			int srcQuality) {
		soundPool = new SoundPool(maxStreams, streamType, srcQuality);
		hashMap = new HashMap<String, Integer>();
		hashMap.put("ball", soundPool.load(context, R.raw.sound_ballhit, 1));
		hashMap.put("wind", soundPool.load(context, R.raw.sound_wind, 1));
		//PlaySound.context = context;
	}

	public static void play(String sound, int loop) {
		// AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		// float currVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		// float maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		// float volume = currVolume / maxVolume;
		float volume = 1f;
		float curStreamId = soundPool.play(hashMap.get(sound), volume, volume,
				1, loop, 1.0f);
		System.out.println(curStreamId);
	}

}
