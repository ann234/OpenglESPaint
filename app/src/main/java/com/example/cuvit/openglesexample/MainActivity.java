package com.example.cuvit.openglesexample;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

enum FIGURE_TYPE
{
    FIGURE_STRAIGHT,
    FIGURE_TRIANGLE,
    FIGURE_PENCIL
}

enum COLOR
{
    COLOR_RED,
    COLOR_GREEN,
    COLOR_BLUE
}

public class MainActivity extends Activity {
    private GLSurfaceView mGLView;
    private MyGLRenderer myGLRenderer;
    private FIGURE_TYPE curDrawType;
    private LinearLayout menuLayout;

    private Button btnLeft;
    private Button btnRight;
    //SeekBar for changing color(R, G, B)
    private SeekBar BarR, BarG, BarB;

    private boolean isImageOn;
    private vector3 color;

    private void initValue()
    {
        color = new vector3(0, 0, 0);
    }

    private void initView()
    {
        mGLView = new GLSurfaceView(this);
        myGLRenderer = new MyGLRenderer();
        mGLView.setRenderer(myGLRenderer);

        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.frameLayout);
        frameLayout.addView(mGLView, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        //get menulayout and
        menuLayout = (LinearLayout)findViewById(R.id.menuLayout);
        menuLayout.bringToFront();
        menuLayout.invalidate();
        //turn off
        isImageOn = true;

        //get menulayout's button
        /*btnLeft = (Button)findViewById(R.id.ButtonLeft);
        btnRight = (Button)findViewById(R.id.ButtonRight);*/

        //RGB Seekbar
        BarR = (SeekBar)findViewById(R.id.BarR);
        BarG = (SeekBar)findViewById(R.id.BarG);
        BarB = (SeekBar)findViewById(R.id.BarB);
        BarR.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                color.x = (float)progress / 100;
                ((TextView)findViewById(R.id.TextRed)).setText("R : " + Float.toString(color.x * 100));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        BarG.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                color.y = (float)progress / 100;
                ((TextView)findViewById(R.id.TextGreen)).setText("G : " + Float.toString(color.y * 100));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        BarB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                color.z = (float)progress / 100;
                ((TextView)findViewById(R.id.TextBlue)).setText("B : " + Float.toString(color.z * 100));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create a GlSurfaceView instnace and set it
        //as the ContentView for this activity
        /*mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);*/

        initValue();
        initView();

        curDrawType = FIGURE_TYPE.FIGURE_PENCIL;

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
                                    menuLayout.setVisibility(View.VISIBLE);
                                else
                                    menuLayout.setVisibility(View.INVISIBLE);
                            }
                            break;
                        case MotionEvent.ACTION_DOWN:
                            mGLView.queueEvent(new Runnable(){
                                public void run()
                                {
                                    //make new figure object
                                    Figure newFigure;
                                    switch(curDrawType)
                                    {
                                        case FIGURE_PENCIL:
                                            //make new Pencil
                                            newFigure = new MyPencil(new vector3(normalizedX, normalizedY, 0), color);

                                            //add pencil to figure list
                                            myGLRenderer.addFigure(newFigure);

                                            Log.i("Axis", "x : " + String.valueOf(normalizedX));
                                            Log.i("Axis", "y : " + String.valueOf(normalizedY));
                                            break;
                                        case FIGURE_STRAIGHT:
                                            //make new straight and init first and last coordinate
                                            newFigure = new MyStraight(new vector3(normalizedX, normalizedY, 0),
                                                    new vector3(normalizedX, normalizedY, 0), color);

                                            //add straight to figure list
                                            myGLRenderer.addFigure(newFigure);

                                            Log.i("Axis", "x : " + String.valueOf(normalizedX));
                                            Log.i("Axis", "y : " + String.valueOf(normalizedY));
                                            break;
                                    }
                                    //mGLView.getRenderer().getCoordinateByDown(normalizedX, normalizedY);
                                }
                            });
                            break;
                        case MotionEvent.ACTION_MOVE:
                            mGLView.queueEvent(new Runnable(){
                                public void run()
                                {
                                    switch(curDrawType)
                                    {
                                        case FIGURE_PENCIL:
                                            //add new vertex
                                            MyPencil pFigure = (MyPencil)myGLRenderer.getTailFigure();

                                            pFigure.addVertex(new vector3(normalizedX, normalizedY, 0));
                                            break;
                                        case FIGURE_STRAIGHT:
                                            //get figure's tail figure = tail figure is current figure
                                            MyStraight sFigure = (MyStraight)myGLRenderer.getTailFigure();

                                            //change straight's last point if move the mouse
                                            sFigure.vertices[1] = new vector3(normalizedX, normalizedY, 0);

                                            //refresh figure's vertex buffer
                                            sFigure.renewalVBuffer();
                                            break;
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
