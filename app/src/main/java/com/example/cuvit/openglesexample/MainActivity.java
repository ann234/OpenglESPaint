package com.example.cuvit.openglesexample;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

enum FIGURE_TYPE
{
    FIGURE_STRAIGHT,
    FIGURE_TRIANGLE,
    FIGURE_PENCIL
}

public class MainActivity extends Activity {
    private GLSurfaceView mGLView;
    private MyGLRenderer myGLRenderer;
    private FIGURE_TYPE curDrawType;

    private boolean isImageOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create a GlSurfaceView instnace and set it
        //as the ContentView for this activity
        /*mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);*/

        mGLView = new GLSurfaceView(this);
        myGLRenderer = new MyGLRenderer();
        mGLView.setRenderer(myGLRenderer);

        isImageOn = false;

        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.frameLayout);
        frameLayout.addView(mGLView, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        findViewById(R.id.imageView01).bringToFront();
        findViewById(R.id.imageView01).invalidate();

        curDrawType = FIGURE_TYPE.FIGURE_STRAIGHT;

        mGLView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event != null)
                {
                    float width = (float)v.getWidth();
                    float height = (float)v.getHeight();
                    float ratio;
                    final float normalizedX, normalizedY;
                    if(width <= height)
                    {
                        ratio = (float)height / (float)width;
                        normalizedX = event.getX() / (float)v.getWidth() * 4 - 2;
                        normalizedY = -(event.getY() / (float)v.getHeight() * 4 - 2) * ratio;
                    }
                    else
                    {
                        ratio = (float)width / (float)height;
                        normalizedX = (event.getX() / (float)v.getWidth() * ratio * 4 - 2) * ratio;
                        normalizedY = -(event.getY() / (float)v.getHeight() * 4 - 2);
                    }

                    int pointerCount = event.getPointerCount();
                    switch(event.getActionMasked())
                    {
                        case MotionEvent.ACTION_POINTER_DOWN:
                            if(pointerCount >= 3)
                            {
                                isImageOn = !isImageOn;
                                if(isImageOn)
                                    findViewById(R.id.imageView01).setVisibility(View.VISIBLE);
                                else
                                    findViewById(R.id.imageView01).setVisibility(View.INVISIBLE);
                            }
                            break;
                        case MotionEvent.ACTION_DOWN:
                            mGLView.queueEvent(new Runnable(){
                                public void run()
                                {
                                    if(curDrawType == FIGURE_TYPE.FIGURE_STRAIGHT)
                                    {
                                        //make new straight and init first and last coordinate
                                        Figure newFigure = new MyStraight(new vector3(normalizedX, normalizedY, 0),
                                                new vector3(normalizedX, normalizedY, 0), new vector3(0, 0, 0));

                                        //add straight to figure list
                                        myGLRenderer.addFigure(newFigure);

                                        Log.i("Axis", "x : " + String.valueOf(normalizedX));
                                        Log.i("Axis", "y : " + String.valueOf(normalizedY));
                                    }
                                    //mGLView.getRenderer().getCoordinateByDown(normalizedX, normalizedY);
                                }
                            });
                            break;
                        case MotionEvent.ACTION_MOVE:
                            mGLView.queueEvent(new Runnable(){
                                public void run()
                                {
                                    if(curDrawType == FIGURE_TYPE.FIGURE_STRAIGHT) {
                                        //get figure's tail figure = tail figure is current figure
                                        MyStraight figure = (MyStraight)myGLRenderer.getTailFigure();

                                        //change straight's last point if move the mouse
                                        figure.vertices[1] = new vector3(normalizedX, normalizedY, 0);

                                        //refresh figure's vertex buffer
                                        figure.renewalVBuffer();

                                        //get renderer and send coordinate x, y
                                        //mGLView.getRenderer().getCoordinateByDrag(normalizedX, normalizedY);

                                        //Log.i("Axis", "x : " + String.valueOf(normalizedX));
                                        //Log.i("Axis", "y : " + String.valueOf(normalizedY));
                                    }
                                }
                            });
                            break;
                    }
                    return true;
                }
                else
                {
                    return false;
                }
            }
        });


    }

    protected void onPause()
    {
        super.onPause();
    }

    protected void onResume()
    {
        super.onResume();
        //mGLView.onResume();
    }
}
