package es.viedma.blowme;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.view.View;

public class BlowView extends View {
	
    private ShapeDrawable ship, goal;
    
    public BlowView(Context context, int width, int height) {
        super(context);

        ship = new ShapeDrawable(new OvalShape());
        ship.getPaint().setColor(0xff74AC23);
        ship.setBounds(width/2 -25,height - 100,width/2 + 25,height - 50);
        
        goal = new ShapeDrawable(new RectShape());
        goal.getPaint().setColor(0xffff0000);
        goal.setBounds(width/2 - 50, 0, width/2 + 50, 25);

    }
    
    public void moveShape(int x, int y){
    	Rect bounds = ship.copyBounds();
    	bounds.left   -= x;
    	bounds.right  -= x;
    	bounds.top    -= y;
    	bounds.bottom -= y;
    	
    	if (bounds.top <= 25){
    		goal.getPaint().setColor(0xff00ff00);
    	}
    	else{
    		ship.setBounds(bounds);
    	}
    }

//    public void refresh(){
//    	mDrawable.draw(canvas)
//    }
    protected void onDraw(Canvas canvas) {
        ship.draw(canvas);
        goal.draw(canvas);
    }
}




