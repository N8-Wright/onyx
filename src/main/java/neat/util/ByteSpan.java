package neat.util;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public class ByteSpan
{
    public final int Start;
    public final int End;
    public final byte[] Data;

    public ByteSpan(int start, int end, byte[] data)
    {
        Start = start;
        End = end;
        Data = data;
    }

    public int getInt()
    {
        if (Start - End != 4)
        {
            throw new BufferUnderflowException();
        }

        var buffer = ByteBuffer.wrap(Data);
        return buffer.getInt(Start);
    }

    @Override
    public boolean equals(Object other)
    {
        if (other instanceof ByteSpan)
        {
            if (Start == ((ByteSpan)other).Start && End == ((ByteSpan)other).End)
            {
                for (int i = Start; i < End; i++)
                {
                    if (Data[i] != ((ByteSpan)other).Data[i])
                    {
                        return false;
                    }
                }

                return true;
            }
            else
            {
                return false;
            }
        }
        else if (other instanceof ByteArray)
        {
            if (End - Start != ((ByteArray)other).length())
            {
                return false;
            }

            for (int i = Start, j = 0; i < End; i++, j++)
            {
                if (Data[i] != ((ByteArray)other).get(j))
                {
                    return false;
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public int hashCode()
    {
        int result = 1;
        for (int i = Start; i < End; i++)
        {
            var element = Data[i];
            result = 31 * result + element;
        }

        return result;
    }
}
