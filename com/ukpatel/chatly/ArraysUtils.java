package com.ukpatel.chatly;

public class ArraysUtils {
    public static byte[] getPrimtiveArray(Byte[] arr, int offset) {
        byte[] newArr = new byte[arr.length];
        for (int i = 0; i < offset; i++) {
            newArr[i] = arr[i];
        }
        return newArr;
    }

    public static Byte[] getObjectArray(byte[] arr, int offset) {
        Byte[] newArr = new Byte[arr.length];
        for (int i = 0; i < offset; i++) {
            newArr[i] = arr[i];
        }
        return newArr;
    }
}
