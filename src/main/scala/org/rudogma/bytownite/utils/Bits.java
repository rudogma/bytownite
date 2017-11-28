package org.rudogma.bytownite.utils;

public class Bits {

    public static boolean getBoolean(byte[] paramArrayOfByte, int paramInt) {
        return (paramArrayOfByte[paramInt] != 0);
    }

    public static char getChar(byte[] paramArrayOfByte, int paramInt) {
        return (char) (((paramArrayOfByte[(paramInt + 1)] & 0xFF) << 0) + (paramArrayOfByte[(paramInt + 0)] << 8));
    }

    public static short getShort(byte[] paramArrayOfByte, int paramInt) {
        return (short) (((paramArrayOfByte[(paramInt + 1)] & 0xFF) << 0) + (paramArrayOfByte[(paramInt + 0)] << 8));
    }

    public static short getShort(byte[] paramArrayOfByte, int paramInt,
                                 boolean reversed) {
        if (!reversed) {
            return getShort(paramArrayOfByte, paramInt);
        }
        return (short) (((paramArrayOfByte[(paramInt + 0)] & 0xFF) << 0) + (paramArrayOfByte[(paramInt + 1)] << 8));
    }

    public static int getInt(byte[] paramArrayOfByte, int paramInt) {
        return (((paramArrayOfByte[(paramInt + 3)] & 0xFF) << 0)
                + ((paramArrayOfByte[(paramInt + 2)] & 0xFF) << 8)
                + ((paramArrayOfByte[(paramInt + 1)] & 0xFF) << 16) + (paramArrayOfByte[(paramInt + 0)] << 24));
    }

    public static int getInt(byte[] paramArrayOfByte, int paramInt,
                             boolean reversed) {
        if (!reversed) {
            return getInt(paramArrayOfByte, paramInt);
        }
        return (((paramArrayOfByte[(paramInt + 0)] & 0xFF) << 0)
                + ((paramArrayOfByte[(paramInt + 1)] & 0xFF) << 8)
                + ((paramArrayOfByte[(paramInt + 2)] & 0xFF) << 16) + (paramArrayOfByte[(paramInt + 3)] << 24));
    }

    public static float getFloat(byte[] paramArrayOfByte, int paramInt) {
        int i = ((paramArrayOfByte[(paramInt + 3)] & 0xFF) << 0)
                + ((paramArrayOfByte[(paramInt + 2)] & 0xFF) << 8)
                + ((paramArrayOfByte[(paramInt + 1)] & 0xFF) << 16)
                + (paramArrayOfByte[(paramInt + 0)] << 24);
        return Float.intBitsToFloat(i);
    }

    public static long getLong(byte[] bytes, int offset) {

        long value = 0;

        value <<= 8;
        value ^= bytes[offset] & 0xFF;

        value <<= 8;
        value ^= bytes[offset+1] & 0xFF;

        value <<= 8;
        value ^= bytes[offset+2] & 0xFF;

        value <<= 8;
        value ^= bytes[offset+3] & 0xFF;

        value <<= 8;
        value ^= bytes[offset+4] & 0xFF;

        value <<= 8;
        value ^= bytes[offset+5] & 0xFF;

        value <<= 8;
        value ^= bytes[offset+6] & 0xFF;

        value <<= 8;
        value ^= bytes[offset+7] & 0xFF;

        return value;
    }

    public static long getLongReversed(byte[] bytes, int offset) {
        long value = 0;

        value <<= 8;
        value ^= bytes[offset+7] & 0xFF;

        value <<= 8;
        value ^= bytes[offset+6] & 0xFF;

        value <<= 8;
        value ^= bytes[offset+5] & 0xFF;

        value <<= 8;
        value ^= bytes[offset+4] & 0xFF;

        value <<= 8;
        value ^= bytes[offset+3] & 0xFF;

        value <<= 8;
        value ^= bytes[offset+2] & 0xFF;

        value <<= 8;
        value ^= bytes[offset+1] & 0xFF;

        value <<= 8;
        value ^= bytes[offset] & 0xFF;

        return value;
    }

    public static double getDouble(byte[] bytes, int offset) {
        return Double.longBitsToDouble(getLong(bytes, offset));
    }

    public static double getDoubleReversed(byte[] bytes, int offset) {
        return Double.longBitsToDouble(getLongReversed(bytes, offset));
    }

    public static void putBoolean(byte[] paramArrayOfByte, int paramInt,
                                  boolean paramBoolean) {
        paramArrayOfByte[paramInt] = (byte) ((paramBoolean) ? 1 : 0);
    }

    public static void putChar(byte[] paramArrayOfByte, int paramInt,
                               char paramChar) {
        paramArrayOfByte[(paramInt + 1)] = (byte) (paramChar >>> '\0');
        paramArrayOfByte[(paramInt + 0)] = (byte) (paramChar >>> '\b');
    }

    public static void putShort(byte[] paramArrayOfByte, int paramInt,
                                short paramShort) {
        paramArrayOfByte[(paramInt + 1)] = (byte) (paramShort >>> 0);
        paramArrayOfByte[(paramInt + 0)] = (byte) (paramShort >>> 8);
    }

    public static void putShortReversed(byte[] paramArrayOfByte, int paramInt,
                                        short paramShort) {
        paramArrayOfByte[(paramInt + 1)] = (byte) (paramShort >>> 8);
        paramArrayOfByte[(paramInt + 0)] = (byte) (paramShort >>> 0);
    }

    public static void putInt(byte[] paramArrayOfByte, int offset, int intValue) {
        paramArrayOfByte[(offset + 3)] = (byte) (intValue >>> 0);
        paramArrayOfByte[(offset + 2)] = (byte) (intValue >>> 8);
        paramArrayOfByte[(offset + 1)] = (byte) (intValue >>> 16);
        paramArrayOfByte[(offset + 0)] = (byte) (intValue >>> 24);
    }

    public static void putIntReversed(byte[] paramArrayOfByte, int offset,
                                      int intValue) {
        paramArrayOfByte[(offset + 3)] = (byte) (intValue >>> 24);
        paramArrayOfByte[(offset + 2)] = (byte) (intValue >>> 16);
        paramArrayOfByte[(offset + 1)] = (byte) (intValue >>> 8);
        paramArrayOfByte[(offset + 0)] = (byte) (intValue >>> 0);
    }

    public static void putFloat(byte[] paramArrayOfByte, int paramInt,
                                float paramFloat) {
        int i = Float.floatToIntBits(paramFloat);
        paramArrayOfByte[(paramInt + 3)] = (byte) (i >>> 0);
        paramArrayOfByte[(paramInt + 2)] = (byte) (i >>> 8);
        paramArrayOfByte[(paramInt + 1)] = (byte) (i >>> 16);
        paramArrayOfByte[(paramInt + 0)] = (byte) (i >>> 24);
    }

    public static void putLong(byte[] paramArrayOfByte, int paramInt,
                               long paramLong) {
        paramArrayOfByte[(paramInt + 7)] = (byte)(paramLong >>> 0);
        paramArrayOfByte[(paramInt + 6)] = (byte)(paramLong >>> 8);
        paramArrayOfByte[(paramInt + 5)] = (byte)(paramLong >>> 16);
        paramArrayOfByte[(paramInt + 4)] = (byte)(paramLong >>> 24);
        paramArrayOfByte[(paramInt + 3)] = (byte)(paramLong >>> 32);
        paramArrayOfByte[(paramInt + 2)] = (byte)(paramLong >>> 40);
        paramArrayOfByte[(paramInt + 1)] = (byte)(paramLong >>> 48);
        paramArrayOfByte[(paramInt + 0)] = (byte)(paramLong >>> 56);
    }

    public static void putLongReversed(byte[] paramArrayOfByte, int paramInt,
                                       long paramLong) {
        paramArrayOfByte[(paramInt + 7)] = (byte) (int) (paramLong >>> 56);
        paramArrayOfByte[(paramInt + 6)] = (byte) (int) (paramLong >>> 48);
        paramArrayOfByte[(paramInt + 5)] = (byte) (int) (paramLong >>> 40);
        paramArrayOfByte[(paramInt + 4)] = (byte) (int) (paramLong >>> 32);
        paramArrayOfByte[(paramInt + 3)] = (byte) (int) (paramLong >>> 24);
        paramArrayOfByte[(paramInt + 2)] = (byte) (int) (paramLong >>> 16);
        paramArrayOfByte[(paramInt + 1)] = (byte) (int) (paramLong >>> 8);
        paramArrayOfByte[(paramInt + 0)] = (byte) (int) (paramLong >>> 0);
    }

    public static void putDouble(byte[] bytes, int offset,
                                 double value) {
        putLong(bytes, offset, Double.doubleToLongBits(value));
    }

    public static void putDoubleReversed(byte[] bytes, int offset,
                                         double value) {
        putLongReversed(bytes, offset, Double.doubleToLongBits(value));
    }
}