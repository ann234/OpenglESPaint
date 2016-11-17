package com.example.cuvit.openglesexample;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by cuvit on 2016-11-08.
 */

public class MyStraight implements  Figure{
    private FloatBuffer vertexBuffer, colorBuffer;
    public vector3 vertices[];
    private vector3 colors;

    private float[] verticeArray, colorArray;

    public MyStraight() {
        vertices = new vector3[2];
        colors = new vector3();
    }

    public MyStraight(vector3 _p1, vector3 _p2, vector3 _colors)
    {
        vertices = new vector3[2];
        vertices[0] = new vector3(_p1.x, _p1.y, _p1.z);
        vertices[1] = new vector3(_p2.x, _p2.y, _p2.z);

        colors = new vector3(_colors.x, _colors.y, _colors.z);

        renewalVBuffer();
    }

    public void renewalVBuffer()
    {
        verticeArray = new float[]{
                vertices[0].x, vertices[0].y, vertices[0].z,
                vertices[1].x, vertices[1].y, vertices[1].z
        };

        colorArray = new float[]{
                colors.x, 0.0f, 0.0f, 1.0f,
                0.0f, colors.y, 0.0f, 1.0f,
                0.0f, 0.0f, colors.z, 1.0f
        };

        ByteBuffer byteBuf = ByteBuffer.allocateDirect(verticeArray.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuf.asFloatBuffer();
        vertexBuffer.put(verticeArray);
        vertexBuffer.position(0);

        byteBuf = ByteBuffer.allocateDirect(colorArray.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        colorBuffer = byteBuf.asFloatBuffer();
        colorBuffer.put(colorArray);
        colorBuffer.position(0);
    }

    public void draw(GL10 gl)
    {
        //if(vertices.length >= 2)
        {
            gl.glLineWidth(5);
            //set vertex array
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
            //set color array
            gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);

            //turn on vertex, color array usage
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            {
                //draw lines
                gl.glDrawArrays(GL10.GL_LINES, 0, verticeArray.length / 2);
            }

            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        }
    }
}
