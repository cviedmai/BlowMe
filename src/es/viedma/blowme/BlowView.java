package es.viedma.blowme;

import android.content.Context;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class BlowView extends SurfaceView implements SurfaceHolder.Callback{
	
	private GameThread mGameThread;
	
    public BlowView(Context context) {
        super(context);        
        getHolder().addCallback(this);
    }
    
//    public void refresh(){
//    	mDrawable.draw(canvas)
//    }
//    protected void onDraw(Canvas canvas) {
//        ship.draw(canvas);
//        goal.draw(canvas);
//    }
    
    public void setGameThread(GameThread gameThread) {
		mGameThread = gameThread;
	}
@Override
public void surfaceChanged(SurfaceHolder holder, int format, int width,
		int height) {
	// TODO Auto-generated method stub
	
}

@Override
public void surfaceCreated(SurfaceHolder holder) {
	Rect frame = holder.getSurfaceFrame();
//	SizeUtil.setScreenSize(frame.width(), frame.height());			
    mGameThread.start(holder, frame.width(), frame.height());
}

public void surfaceDestroyed(SurfaceHolder holder) {
	// TODO Auto-generated method stub
	
}
}




