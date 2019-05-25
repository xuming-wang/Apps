package org.air.bigearth.apps.util;

import org.apache.commons.codec.cli.Digest;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5值计算工具类
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-23
 */
public class MD5Class {
    // 计算字符串的MD5
    public static String conVertTextToMD5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            // 32位加密
            return buf.toString();
            // 16位的加密
            // return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }

    //计算文件的MD5，支持4G一下的文件（文件亲测，大文件未亲测）
    public static String conVertFileToMD5(String inputFilePath) {
        int bufferSize = 256 * 1024;

        FileInputStream fileInputStream = null;

        DigestInputStream digestInputStream = null;

        try {

            // 拿到一个MD5转换器（同样，这里可以换成SHA1）

            MessageDigest messageDigest = MessageDigest.getInstance("MD5");

            // 使用DigestInputStream

            fileInputStream = new FileInputStream(inputFilePath);

            digestInputStream = new DigestInputStream(fileInputStream,
                    messageDigest);

            // read的过程中进行MD5处理，直到读完文件

            byte[] buffer = new byte[bufferSize];

            while (digestInputStream.read(buffer) > 0)
                ;

            // 获取最终的MessageDigest

            messageDigest = digestInputStream.getMessageDigest();

            // 拿到结果，也是字节数组，包含16个元素

            byte[] resultByteArray = messageDigest.digest();

            // 同样，把字节数组转换成字符串

            return byteArrayToHex(resultByteArray);

        } catch (Exception e) {

            return null;

        } finally {

            try {

                digestInputStream.close();

            } catch (Exception e) {

            }

            try {

                fileInputStream.close();

            } catch (Exception e) {

            }

        }
    }

    public static String getMD5Three(String path) {
        BigInteger bi = null;
        try {
            byte[] buffer = new byte[8192*5];
            int len = 0;
            MessageDigest md = MessageDigest.getInstance("MD5");
            File f = new File(path);
            FileInputStream fis = new FileInputStream(f);
            while ((len = fis.read(buffer)) != -1) {
                md.update(buffer, 0, len);
            }
            fis.close();
            byte[] b = md.digest();
            bi = new BigInteger(1, b);
        }catch (NoSuchAlgorithmException e) {

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bi.toString(16);
    }

    private static String byteArrayToHex(byte[] byteArray) {

        // 首先初始化一个字符数组，用来存放每个16进制字符

        char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F' };

        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））

        char[] resultCharArray = new char[byteArray.length * 2];

        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去

        int index = 0;

        for (byte b : byteArray) {

            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];

            resultCharArray[index++] = hexDigits[b & 0xf];

        }

        // 字符数组组合成字符串返回

        return new String(resultCharArray);
    }



    public static void main(String[] args) throws IOException {
        String path = "/opt/softwares/ideaIU-2018.2.5.tar.gz";
        String paht = "/run/media/centos/disk/system/cn_windows_7_ultimate_with_sp1_x64_dvd_u_677408.iso";
        // 测试
        //System.out.println(MD5Class.conVertTextToMD5("hello"));
        long start = System.currentTimeMillis();
        //System.out.println(conVertFileToMD5("/opt/softwares/ideaIU-2018.2.5.tar.gz"));
        //System.out.println(DigestUtils.md5Hex(new FileInputStream("/opt/softwares/ideaIU-2018.2.5.tar.gz")));
        System.out.println(getMD5Three(paht));
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        //System.out.println(DigestUtils.md5Hex(new FileInputStream("/opt/softwares/ideaIU-2018.2.5.tar.gz")));
        //System.out.println(System.currentTimeMillis() - end);

    }
}