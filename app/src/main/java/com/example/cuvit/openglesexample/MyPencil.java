package com.example.cuvit.openglesexample;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by cuvit on 2016-11-08.
 */

public class MyPencil implements  Figure{
    private FloatBuffer vertexBuffer, colorBuffer;
    private ArrayList<Float> vertices;
    private float colors[] = {
            0.0f, 0.0f, 0.0f, 1.0f
    };

    public MyPencil()
    {
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(colors.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        colorBuffer = byteBuf.asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);

        vertices = new ArrayList<Float>();
    }

    public void addVertex(float x, float y, float z)
    {
        vertices.add(x);    vertices.add(y);    vertices.add(z);
        //vertexBuffer.clear(); //error

        ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.size() * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuf.asFloatBuffer();

        float verticesArray[] = new float[vertices.size()];

        int i = 0;
        for(Float f : vertices) {
            verticesArray[i++] = (f != null ? f : Float.NaN);
        }
        vertexBuffer.put(verticesArray);
        vertexBuffer.position(0);
    }

    public void draw(GL10 gl)
    {
        if(vertices.size() >= 2)
        {
            //set vertex array
            gl.glVertexPointer(vertices.size(), GL10.GL_FLOAT, 0, vertexBuffer);
            //set color array
            gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);

            //turn on vertex, color array usage
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
            //gl.glLineWidth(0.1f);
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            {
                //draw lines
                gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, vertices.size() - 1);
            }

            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        }
    }
}
