package com.bitbank.creditcalc;

import org.apache.http.protocol.HTTP;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;

/**
 * User: oleg.danilyuk
 * Date: 08.07.13
 */
public class MD5TestClass {

    @Test
    public void testHash() {
        //System.out.println(toMD5("asdf1ccqsllHDnBJqWZejDIbI"));
        assertEquals("7b786481ecbf74a284473a4d48875e63", toMD5("1p2l3a4n5"+"ccqsllHDnBJqWZejDIbI"));
        assertEquals("facf48fdf0673fedcebb8fb954730575", toMD5("sfsdhfkjsdhf"+"ccqsllHDnBJqWZejDIbI"));
        assertEquals("9f858958b0b89a04fd703b22441f2ae5", toMD5("plan"+"ccqsllHDnBJqWZejDIbI"));
        assertEquals("3025e4ddc7027e2628be0f7f079c45cc", toMD5("1231245624"+"ccqsllHDnBJqWZejDIbI"));
    }

    public static String toMD5(String str) {
        return toHex(toMD5Bytes(str));
    }

    public static byte[] toMD5Bytes(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset(); // just in case
            return md.digest(str.getBytes(HTTP.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            // UTF-8 encoding is unsupported by device (which is VERY unlikely)
            throw new RuntimeException(e);
        }
    }

    public static String toHex(byte[] buf) {
        final int len = buf.length;
        final StringBuilder sb = new StringBuilder(2 * len);
        for (int i = 0; i < len; i++) {
            byte b = buf[i];
            sb.append(Integer.toHexString((b >> 4) & 0x0f));
            sb.append(Integer.toHexString(b & 0x0f));
        }
        return sb.toString();
    }
}
