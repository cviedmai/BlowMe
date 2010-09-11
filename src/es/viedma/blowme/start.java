package es.viedma.blowme;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.widget.TextView;

public class start extends Activity {
    /** Called when the activity is first created. */
    TextView tv;
    MediaRecorder recorder;
    BlowView bv;
    
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

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        // default screen size (nexus one): 533x300
        bv = new BlowView(this, metrics.widthPixels, metrics.heightPixels);
        setContentView(bv);

        MyCount counter = new MyCount(30000, 150);
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
        	int level = recorder.getMaxAmplitude();
            tv.setText("Value:" + level + "Left: " + millisUntilFinished / 1000);
            bv.moveShape(0, (level/2000)%20);
            setContentView(bv);
            System.out.println(level%500);
        }
       }
}