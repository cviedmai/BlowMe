package es.viedma.blowme;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

public class start extends Activity {
    /** Called when the activity is first created. */
    TextView tv;
    MediaRecorder recorder;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
        
        tv = new TextView(this);
        this.setContentView(tv);
        tv.setText("Hello, Android");
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

        MyCount counter = new MyCount(30000, 200);
        counter.start();
        
    }
    public class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
          super(millisInFuture, countDownInterval);
        }
        public void onFinish() {
          tv.setText("Done!");
        }
        public void onTick(long millisUntilFinished) {
           tv.setText("Value:" + recorder.getMaxAmplitude() + "Left: " + millisUntilFinished / 1000);
        }
       }
}