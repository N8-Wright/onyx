package neat.util;

import java.nio.charset.Charset;
import java.util.Arrays;

public class ByteArray
{
    private byte[] _data;
    private int _offset;

    public ByteArray(byte[] data)
    {
        _data = data;
        _offset = _data.length;
    }

    public ByteArray(int capacity)
    {
        _data = new byte[capacity];
        _offset = 0;
    }

    public static ByteArray of(String data)
    {
        return new ByteArray(data.getBytes(Charset.forName("UTF8")));
    }

    public static ByteArray of(byte[] data)
    {
        return new ByteArray(data);
    }

    public void push(byte[] buffer)
    {
        push(buffer, buffer.length);
    }

    public void push(byte[] buffer, int length)
    {
        int end = _offset + length;
        if (end > _data.length)
        {
            resize(end);
        }

        for (int i = _offset, j = 0; i < end; i++, j++)
        {
            _data[i] = buffer[j];
        }

        _offset += buffer.length;
    }

    public byte get(int index)
    {
        return _data[index];
    }

    public int length()
    {
        return _offset;
    }

    public byte[] slice(int from, int to)
    {
        return Arrays.copyOfRange(_data, from, to);
    }

    private void resize(int capacity)
    {
        _data = Arrays.copyOf(_data, capacity);
    }


    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof ByteArray))
        {
            return false;
        }

        return Arrays.equals(_data, ((ByteArray)other)._data);
    }

    @Override
    public int hashCode()
    {
        return Arrays.hashCode(_data);
    }
}
