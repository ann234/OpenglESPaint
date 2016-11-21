package com.example.cuvit.openglesexample;

import android.opengl.GLSurfaceView;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by cuvit on 2016-11-08.
 */

public class MyGLRenderer implements GLSurfaceView.Renderer {
    private vector3 startVec3, endVec3;

    private MyTriangle tr;
    private MyStraight st;

    private MyStraight tmpSt;
    private ArrayList<Figure> figures;
    private float mAngle;

    private boolean mouseLeftDown;

    public void addFigure(Figure _figure)
    {
        figures.add(_figure);
    }

    public Figure getTailFigure()
    {
        return figures.get(figures.size() - 1);
    }

    public void clearFigure()
    {
        figures.clear();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glFrontFace(GL10.GL_CW);
        //set shading mode
        gl.glShadeModel(gl.GL_SMOOTH);

        gl.glClearColor(0.8f, 0.8f, 0.8f, 1.0f);
        tr = new MyTriangle();
        st = new MyStraight(new vector3(-1, 0, 0), new vector3(1, 0, 0), new vector3(1, 0, 0));
        figures = new ArrayList<Figure>();
        mAngle = 0;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(gl.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        for (Figure figure: figures) {
            figure.draw(gl);
        }

        gl.glTranslatef(0.0f, 0.0f, -6.0f);
        gl.glRotatef(mAngle, 0, 0, 1);
        mAngle += 1.0f;
        //tr.draw(gl);
        //st.draw(gl);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);

        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();

        float ratio;
        if(width <= height)
        {
            ratio = (float)height / (float)width;
            gl.glOrthof(-2, 2, -2 * ratio, 2 * ratio, -1, 10);
        }
        else
        {
            ratio = (float)width / (float)height;
            gl.glOrthof(-2 * ratio, 2 * ratio, -2, 2, -1, 10);
        }
    }

    public void getCoordinateByDown(float _x, float _y)
    {
        startVec3 = new vector3(_x, _y, 0);
        endVec3 = new vector3(_x, _y, 0);

        tmpSt = new MyStraight(startVec3, endVec3, new vector3(0, 1, 0));
    }

    public void getCoordinateByDrag(float _x, float _y)
    {
        endVec3.x = _x;  endVec3.y = _y;
        mouseLeftDown = true;

        tmpSt.vertices[1].x = _x;
        tmpSt.vertices[1].y = _y;
        tmpSt.renewalVBuffer();
    }
}
