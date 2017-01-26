package com.gammarush.engine.sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

//SIMPLE SOUND CLASS THAT GETS AUDIO STREAM FROM RES FOLDER AND PLAYS IT

public class Sound {
	
	//PLAY SOUND FILE ONCE
	public static synchronized void play(final String url) {
		play(url, 0f);
		return;
	}
	
	public static synchronized void play(final String url, float volume) {
		new Thread(new Runnable() {
			public void run() {
				try {
					Clip clip = AudioSystem.getClip();
				    AudioInputStream inputStream = AudioSystem.getAudioInputStream(Sound.class.getResource(url));
				    clip.open(inputStream);
				    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				    gainControl.setValue(volume);
				    clip.start();
				} catch (Exception e) {
				    e.printStackTrace();
				}
			}
		}).start();
	}
	
	//LOOP SOUND FILE CONTINUOUSLY
	public static synchronized void loop(final String url) {
		loop(url, 0f);
		return;
	}
	
	public static synchronized void loop(final String url, float volume) {
		new Thread(new Runnable() {
			public void run() {
				try {
					Clip clip = AudioSystem.getClip();
				    AudioInputStream inputStream = AudioSystem.getAudioInputStream(Sound.class.getResource(url));
				    clip.open(inputStream);
				    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				    gainControl.setValue(volume);
				    clip.loop(Clip.LOOP_CONTINUOUSLY);
				} catch (Exception e) {
				    e.printStackTrace();
				}
			}
		}).start();
	}
	
}
