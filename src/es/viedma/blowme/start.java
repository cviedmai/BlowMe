package es.viedma.blowme;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;

public class start extends Activity {
    BlowView bv;
    Microphone micro;
	private GameThread mGameThread;
    
    private Handler mHandler = new Handler(new Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			// Display messages on top of the game view
//			switch (msg.what) {
//			case GameThread.START:
//				hideText();
//				break;
//			case GameThread.WIN:
//				showText(R.string.well_done, R.string.next_level);
//				break;
//			case GameThread.COMPLETED:
//				showText(R.string.congratulations, R.string.last_game);
//				break;
//			case GameThread.GAME_OVER:
//				showText(R.string.game_over, R.string.restart_game);
//				break;
//			case GameThread.PAUSED:
//				showText(R.string.paused, R.string.resume_game);
//			}
			return true;
		}
	});
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mGameThread = new GameThread(mHandler, this);
        bv = new BlowView(this);
        bv.setGameThread(mGameThread);
        setContentView(bv);        
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Accelerometer.start(sensorManager);

    }

}