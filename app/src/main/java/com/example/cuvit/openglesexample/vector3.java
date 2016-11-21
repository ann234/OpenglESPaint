package com.example.cuvit.openglesexample;

/**
 * Created by cuvit on 2016-11-08.
 */

public class vector3 {
    public float x, y, z;

    public vector3()
    {
        x = 0;  y = 0;  z = 0;
    }

    public vector3(float _x, float _y, float _z)
    {
        x = _x; y = _y; z = _z;
    }

    public vector3(vector3 _vec)
    {
        x = _vec.x; y = _vec.y; z = _vec.z;
    }
}
