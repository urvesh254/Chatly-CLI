package com.ukpatel.chatly;

/**
 * @author Urveshkumar Patel
 */
public class Utils {
    public static byte[] getPrimitiveArray(Byte[] arr, int offset) {
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

    public static String getFileSize(long fileSize) {
        String s = String.valueOf(fileSize);
        int len = s.length();
        String ch = "";
        float size = 0;
        if (len > 9) {
            ch = "G";
            size = (float) fileSize / 1000000000;
        } else if (len > 6) {
            ch = "M";
            size = (float) fileSize / 1000000;
        } else if (len > 3) {
            ch = "K";
            size = (float) fileSize / 1000;
        } else {
            size = (float) fileSize;
        }
        return String.format("%.1f %sB", size, ch);
    }
}
