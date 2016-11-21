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
    private FloatBuffer vertexBuffer;
    private ArrayList<vector3> vertices;
    private vector3 color;

    public MyPencil(vector3 vec, vector3 _color)
    {
        //make new vertex array
        vertices = new ArrayList<vector3>();

        //set color
        color = new vector3(_color);

        //add first vertex
        addVertex(vec);
    }

    public void addVertex(vector3 vec)
    {
        //add vertex
        vertices.add(vec);

        //make vertexArray
        float verticeArray[] = new float[vertices.size() * 3];

        int i = 0;
        for(vector3 _vec : vertices)
        {
            verticeArray[i++] = _vec.x;
            verticeArray[i++] = _vec.y;
            verticeArray[i++] = _vec.z;
        }

        //renewal vertex buffer
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(verticeArray.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuf.asFloatBuffer();
        vertexBuffer.put(verticeArray);
        vertexBuffer.position(0);
    }

    public void draw(GL10 gl)
    {
        gl.glLineWidth(10);

        //set vertex array
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

        //gl.glLineWidth(0.1f);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        {
            gl.glColor4f(color.x, color.y, color.z, 0);
            //draw lines
            gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, vertices.size());
        }

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
}
