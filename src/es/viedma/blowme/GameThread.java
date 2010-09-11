package es.viedma.blowme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.SurfaceHolder;

public class GameThread implements Runnable {
		
	private SurfaceHolder mSurfaceHolder;
	private Handler mHandler;
//	private ShapeDrawable ship, beer;
	private ShapeDrawable ship;
	private Bitmap beer, peter;
	private Microphone mic;
	private int SCREEN_WIDTH, SCREEN_HEIGHT;
	private Context mContext;
	private MediaPlayer mp;

	private PaintDrawable mBackground;
	private Thread mThread;	
	    
    public GameThread(Handler handler, Context context) {
    	mHandler = handler;
    	mContext = context;
    	mp = MediaPlayer.create(context, R.raw.yeehaaa);
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
				        checkCollisions(canvas);
					} finally {
						mSurfaceHolder.unlockCanvasAndPost(canvas);
					}
				}
			}
        }
	}
	
	public void initShapes(){
        ship = new ShapeDrawable(new OvalShape());
        ship.getPaint().setColor(0xffff0000);
        ship.setBounds(SCREEN_WIDTH/2 -25,SCREEN_HEIGHT - 100,SCREEN_WIDTH/2 + 25,SCREEN_HEIGHT - 50);

        peter =  BitmapFactory.decodeResource(mContext.getResources(), R.drawable.peter);
        beer  =  BitmapFactory.decodeResource(mContext.getResources(), R.drawable.beer);
        
//        beer = res.getDrawable(R.drawable.beer);
//        beer = new ShapeDrawable(new RectShape());
//        beer.getPaint().setColor(0xff740000);
//        beer.setBounds(SCREEN_WIDTH/2 - 25, 0, SCREEN_WIDTH/2 + 25, 50);

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
		ship.setBounds(bounds);
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

		mBackground = new PaintDrawable(0xff000000);
		mBackground.setBounds(bounds);
	}
	
	private void updateGraphics(Canvas canvas) {
		mBackground.draw(canvas);
		canvas.drawBitmap(beer, SCREEN_WIDTH/2 - 38, 10, null);
		ship.draw(canvas);
		
	}
		
	private void resetGraphics(Canvas canvas) {        
		ship.setBounds(SCREEN_WIDTH/2 -25,SCREEN_HEIGHT - 100,SCREEN_WIDTH/2 + 25,SCREEN_HEIGHT - 50);
		canvas.drawBitmap(peter, SCREEN_WIDTH/2 - 100, SCREEN_HEIGHT/2 - 135, null);
	    //updateGraphics(canvas);    
	}
	
	private void checkCollisions(Canvas canvas){
		Rect bounds = ship.copyBounds();
	    if (bounds.top <= 25 && bounds.left <= SCREEN_WIDTH/2+50 && bounds.left >= SCREEN_WIDTH/2-50){
    		ship.getPaint().setColor(0xff00ff00);
    		canvas.drawBitmap(peter, SCREEN_WIDTH/2 - 100, SCREEN_HEIGHT/2 - 135, null);
    		mp.start();
	    	//resetGraphics(canvas);
    	}		
	}
}
