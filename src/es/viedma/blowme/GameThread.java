package es.viedma.blowme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.SurfaceHolder;

public class GameThread implements Runnable {
		
	private SurfaceHolder mSurfaceHolder;
	private Handler mHandler;
//	private ShapeDrawable ship, beer;
	private ShapeDrawable ship, ob1, ob2;
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

        ob1 = new ShapeDrawable(new RectShape());
        ob2 = new ShapeDrawable(new RectShape());
        ob1.getPaint().setColor(0xff555555);
        ob2.getPaint().setColor(0xff555555);
        ob1.setBounds(0,SCREEN_HEIGHT/3,200,SCREEN_HEIGHT/3+25);
        ob2.setBounds(SCREEN_WIDTH - 200,2*SCREEN_HEIGHT/3,SCREEN_WIDTH,2*SCREEN_HEIGHT/3 +25);

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
		ob1.draw(canvas);
		ob2.draw(canvas);
		
	}
		
	private void resetGraphics(Canvas canvas) {        
		ship.setBounds(SCREEN_WIDTH/2 -25,SCREEN_HEIGHT - 100,SCREEN_WIDTH/2 + 25,SCREEN_HEIGHT - 50);
	    updateGraphics(canvas);    
	}
	
	private void checkCollisions(Canvas canvas){
		Rect b_ship = ship.copyBounds();
//		Rect b_ob1 = ob1.copyBounds();
//		Rect b_ob2 = ob2.copyBounds();
//		if (b_ship.top <= b_ob1.bottom && b_ship.left <= b_ob1.right){
//			// Hits ob1
//	    	resetGraphics(canvas);
//		}
//		if (){
//			//Hits ob2
//	    	resetGraphics(canvas);
//		}
	    if (b_ship.top <= 25 && b_ship.left <= SCREEN_WIDTH/2+50 && b_ship.left >= SCREEN_WIDTH/2-50){
	    	// Wins!
    		ship.getPaint().setColor(0xff00ff00);
    		canvas.drawBitmap(peter, SCREEN_WIDTH/2 - 100, SCREEN_HEIGHT/2 - 135, null);
    		mp.start();
    	}		
	}
}
