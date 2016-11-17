package com.example.cuvit.openglesexample;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/**
 * Created by cuvit on 2016-11-08.
 */

public class MyGLSurfaceView extends GLSurfaceView {
    private final MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context) {
        super(context);

        mRenderer = new MyGLRenderer();
        setRenderer(mRenderer);
    }

    public MyGLRenderer getRenderer()
    {
        return mRenderer;
    }
}
