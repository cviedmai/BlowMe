package es.viedma.blowme;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.os.Handler;
import android.view.SurfaceHolder;

public class GameThread implements Runnable {
		
	private SurfaceHolder mSurfaceHolder;
	private Handler mHandler;
	private ShapeDrawable ship, goal;
	private Microphone mic;
	private int SCREEN_WIDTH, SCREEN_HEIGHT;

	private PaintDrawable mBackground;
	private Thread mThread;	
//	private State mState = State.INIT;

//	private enum State {
//    	INIT,
//    	RUNNING,
//    	PAUSED,
//    	WIN,
//    	GAME_OVER,
//    }
	    
    public GameThread(Handler handler) {
    	mHandler = handler;
    }
    
    public void start(SurfaceHolder surfaceHolder, int width, int height) {
    	mSurfaceHolder = surfaceHolder;
    	SCREEN_WIDTH = width;
    	SCREEN_HEIGHT = height;
		if (mThread == null) {
//			Level.setupLevel(this, mLevel);
			createBackground();
			initShapes();
			mic = new Microphone();
			mThread = new Thread(this, "GameThread");
			mThread.start();
		} else {
            synchronized(this) {
//            	--mThreadSuspended;
            	notify();
            }
		}
	}
    
	@Override
	public void run() {
//		mState = State.RUNNING;
		Canvas canvas;
		while (true) {
			synchronized (this) {
				canvas = mSurfaceHolder.lockCanvas();
				if (canvas != null) {
					try {
						// updateAnimations();
						// updateSound();
						updateMic();
						updateAccel();
				        updateGraphics(canvas);
					} finally {
						mSurfaceHolder.unlockCanvasAndPost(canvas);
					}
				}
			}
        }
	}
	
	public void initShapes(){
        ship = new ShapeDrawable(new OvalShape());
        ship.getPaint().setColor(0xff74AC23);
        ship.setBounds(SCREEN_WIDTH/2 -25,SCREEN_HEIGHT - 100,SCREEN_WIDTH/2 + 25,SCREEN_HEIGHT - 50);

        goal = new ShapeDrawable(new RectShape());
        goal.getPaint().setColor(0xffff0000);
        goal.setBounds(SCREEN_WIDTH/2 - 50, 0, SCREEN_WIDTH/2 + 50, 25);

	}
	
	public void moveShape(int x, int y){
    	Rect bounds = ship.copyBounds();
    	int new_left = bounds.left + x;
    	int new_right = bounds.right + x;
    	int new_top = bounds.top - y;
    	int new_bottom = bounds.bottom - y;
    	
    	if (new_left >= 0 && new_right <= SCREEN_WIDTH && new_top >= 0 && new_bottom <= SCREEN_HEIGHT){
	    	bounds.left   += x;
	    	bounds.right  += x;
	    	bounds.top    -= y;
	    	bounds.bottom -= y;
    	}
    	
    	if (bounds.top <= 25){
    		goal.getPaint().setColor(0xff00ff00);
    	}
    	else{
    		ship.setBounds(bounds);
    	}
    }

	public void updateMic(){
		int level = mic.getLevel();
		moveShape(0, level);
	}
	
	public void updateAccel(){
		float x = Accelerometer.getX();
		moveShape(Math.round(5*x), 0);
	}

	private void createBackground() {
		// Adjust width/height to game ratio to avoid invisible walls
    	Rect bounds = new Rect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    	if (SCREEN_WIDTH * 3 > SCREEN_HEIGHT * 2) {
    		bounds.left = (SCREEN_WIDTH - SCREEN_HEIGHT * 2 / 3) / 2;
    		bounds.right -= bounds.left;
    	} else if (SCREEN_WIDTH * 3 < SCREEN_HEIGHT * 2) {
    		bounds.top = (SCREEN_HEIGHT - SCREEN_WIDTH * 3 / 2) / 2;
			bounds.bottom -= bounds.top;
    	}

		mBackground = new PaintDrawable(0xff4646ff);
		mBackground.setBounds(bounds);
	}
	
	private void updateGraphics(Canvas canvas) {
		mBackground.draw(canvas);
		ship.draw(canvas);
        goal.draw(canvas);
	}
}
