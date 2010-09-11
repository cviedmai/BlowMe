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
		if (mThread == null) {
//			Level.setupLevel(this, mLevel);
			createBackground(width, height);
			initShapes(width, height);
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
				        updateGraphics(canvas);
					} finally {
						mSurfaceHolder.unlockCanvasAndPost(canvas);
					}
				}
			}
        }
	}
	
	public void initShapes(int width, int height){
        ship = new ShapeDrawable(new OvalShape());
        ship.getPaint().setColor(0xff74AC23);
        ship.setBounds(width/2 -25,height - 100,width/2 + 25,height - 50);

        goal = new ShapeDrawable(new RectShape());
        goal.getPaint().setColor(0xffff0000);
        goal.setBounds(width/2 - 50, 0, width/2 + 50, 25);

	}
	
	public void moveShape(int x, int y){
    	Rect bounds = ship.copyBounds();
    	bounds.left   += x;
    	bounds.right  += x;
    	bounds.top    -= y;
    	bounds.bottom -= y;
    	
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

	private void createBackground(int width, int height) {
		// Adjust width/height to game ratio to avoid invisible walls
    	Rect bounds = new Rect(0, 0, width, height);
    	if (width * 3 > height * 2) {
    		bounds.left = (width - height * 2 / 3) / 2;
    		bounds.right -= bounds.left;
    	} else if (width * 3 < height * 2) {
    		bounds.top = (height - width * 3 / 2) / 2;
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
