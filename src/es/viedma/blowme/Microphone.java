package es.viedma.blowme;

import java.io.IOException;

import android.media.MediaRecorder;

public class Microphone {
	MediaRecorder recorder;
	int level;
	
	public Microphone(){
        // Audio recorder
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile("/dev/null");
       try {
			recorder.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        recorder.start();	
	}
	
	public int getLevel(){
		level = recorder.getMaxAmplitude();
		level = (level/2000)%20;
		return level;
	}
	
}
