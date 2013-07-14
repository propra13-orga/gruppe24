package Client;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Hashtable;
import java.util.Vector;

public class SoundLib {

	Hashtable<String, AudioClip> sounds;
	Vector<AudioClip> loopingClips;

	public SoundLib() {
		sounds = new Hashtable<String, AudioClip>();
		loopingClips = new Vector<AudioClip>();
	}

	public void loadSound(String name, String path) {

		if (sounds.containsKey(name)) {
			return;
		}

		try {
			sounds.put(name, (AudioClip) Applet.newAudioClip(new File(path)
					.toURI().toURL()));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public void playSound(String name) {
		AudioClip audio = sounds.get(name);
		audio.play();
	}

	public void loopSound(String name) {
		AudioClip audio = sounds.get(name);
		loopingClips.add(audio);
		audio.loop();
	}

	public void stopLoopingSound() {
		for (AudioClip c : loopingClips) {
			c.stop();
		}
	}

}
