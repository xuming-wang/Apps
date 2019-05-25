package org.air.bigearth.apps;

import org.air.bigearth.apps.util.DataKeyUtil;
import org.air.bigearth.apps.util.EncryptAES;

import java.io.File;

public class EncryptAESTest {

    public static void main(String[] args){
        File file1 = new File("/home/centos/Desktop/source.wls1035_win32.zip");
        File file2 = new File("/home/centos/Desktop/wls1035_win32.zip");
        File file3 = new File("/home/centos/Desktop/wls1035_win32_enc.zip");
        long start = System.currentTimeMillis();
        EncryptAES.encryptFile(file1,file2,"password");
        System.out.println((System.currentTimeMillis()-start)+"毫秒");//14679毫秒（721M）
        EncryptAES.decryptFile(file2,file3,"password");
        System.out.println((System.currentTimeMillis()-start)+"毫秒");//29179毫秒（721M）
        String str = "这是一段文字，要进行字符串加密测试，然后再进行解密测试。";
        System.out.println("加密前："+str);
        long s = System.currentTimeMillis();
        String encStr = EncryptAES.encryptString(str,"password");
        System.out.println("加密后："+encStr);
        System.out.println("字符串加密："+(System.currentTimeMillis()-s)+"毫秒");
        String decStr = EncryptAES.decryptString(encStr,"password");
        System.out.println("解密后："+decStr);
        System.out.println("字符串解密："+(System.currentTimeMillis()-s)+"毫秒");


    }

}
