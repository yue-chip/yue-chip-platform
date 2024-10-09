package com.yue.chip.utils;

import com.cslc.exception.GeneralException;
import com.yue.chip.exception.BusinessException;
import datech.com.cn.Util;
import datech.com.cn.psbc_csp_api;
import datech.com.cn.psbc_csp_handle;
import org.springframework.util.StringUtils;
import sign.com.cn.Dean_UAM_Handle;
import sign.com.cn.DtTimeStamp;
import sign.com.cn.NetSignResult;
import sign.com.cn.sign_api;
import time.com.cn.Dean_UAM_Api;
import time.com.cn.Dean_UAM_Time_Handle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Scanner;

/**
 * @author: wwz
 * @date: 2022/5/16
 * @version: 1.0.0
 */
public class MGUtil {

    public static String path = "";


//    D6ADB51C107FEA5D265B87F80E56C510

    /**
     * 密钥管理系统获取密钥测试
     */
    public static String GetSM4Key()  {
        return "D6ADB51C107FEA5D265B87F80E56C510";


//        String key = "";
//        psbc_csp_api csp_api = new psbc_csp_api("/opt/java/psbc.ini");
//        for (int i = 0;i<100;i++) {
//            try {
//                psbc_csp_handle pCspHandle = new psbc_csp_handle();
//                int ret = csp_api.PSBC_Connect(pCspHandle);
//                System.out.println("获取密钥："+csp_api.toString());
//                if (ret == 0) {
//                    byte[] sm4KeyByVersion = csp_api.KMPDB_QuerySM4KeyByVersion(pCspHandle, psbc_csp_api.TenantId, "10000012", psbc_csp_api.AppId, 1000);
//
//                    if (sm4KeyByVersion != null) {
//                        key = new String(sm4KeyByVersion);
//                        System.out.println("密钥：" + key);
//                    } else {
//                        System.out.println("获取密钥失败：" + csp_api.PSBC_GetErrCode());
//                    }
//                    csp_api.PSBC_Disconnect(pCspHandle);
//                    if (StringUtils.hasText(key)) {
//                        i = 10;
//                        return key;
//                    }
//                } else {
//                    System.out.println("=========================连接密管服务失败：" + csp_api.PSBC_GetErrCode());
//                }
//                Thread.sleep(500);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        if (!StringUtils.hasText(key)) {
//            BusinessException.throwException("获取密钥失败");
//        }
//        return key;
    }

    /**
     * SM2裸签名/验签（内部运算SM3）
     * @throws IOException
     */
    public static void sm2RawSignVerify() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入签名证书主题：");
        String certSubject = scanner.next();
        System.out.println("请输入原文：");
        String data = scanner.next();
        System.out.println("请输入签名证书存放位置：");
        String certPath = scanner.next();

        byte[] certByte = null;
        FileInputStream inStream;
        try {
            inStream = new FileInputStream(certPath);
            int zipLength = inStream.available();
            certByte = new byte[zipLength];
            inStream.read(certByte);
            inStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        sign_api dean_api=new sign_api(path);
        Dean_UAM_Handle dean_handle=new Dean_UAM_Handle();

        int i = dean_api.PSBC_Connect(dean_handle);
        if (i != 0){
            System.out.println("签名验签服务连接失败：" + dean_api.PSBC_GetErrCode());
        }

        NetSignResult netSignResult = dean_api.raw_signature(dean_handle, data.getBytes("GBK"), certSubject.getBytes("GBK"), 3);
        int errCode = netSignResult.getErrCode(); // 获取错误码
        if (errCode != 0){
            System.out.println("SM2裸签名失败：" + errCode);
        }else {
            byte[] signData = netSignResult.getByteArrayResult("signData");
            System.out.println("签名值：" + Util.Bytes2HexString(signData));
            NetSignResult rawVerify = dean_api.raw_verify(dean_handle, data.getBytes("GBK"), signData, certByte, 3);
            int verifyErrCode = rawVerify.getErrCode();
            if (verifyErrCode != 0){
                System.out.println("SM2签名验签失败：" + verifyErrCode);
            }else {
                System.out.println("SM2签名验签成功");
            }
        }
        dean_api.PSBC_Disconnect(dean_handle);
    }

    /**
     * SM2Detach签名/验签测试
     * @throws UnsupportedEncodingException
     */
    public static void detachSignVerify() throws UnsupportedEncodingException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入签名证书主题：");
        String certSubject = scanner.next();
        System.out.println("请输入原文：");
        String data = scanner.next();

        sign_api dean_api=new sign_api(path);
        Dean_UAM_Handle dean_handle=new Dean_UAM_Handle();

        int i = dean_api.PSBC_Connect(dean_handle);
        if (i != 0){
            System.out.println("签名验签服务连接失败：" + dean_api.PSBC_GetErrCode());
        }

        NetSignResult netSignResult = dean_api.detached_sign(dean_handle, data.getBytes("GBK"), certSubject.getBytes("GBK"), 3);
        int errCode = netSignResult.getErrCode(); // 获取错误码
        if (errCode != 0){
            System.out.println("SM2Detach签名失败：" + errCode);
        }else {
            byte[] signData = netSignResult.getByteArrayResult("signData");
            System.out.println("签名值：" + Util.Bytes2HexString(signData));
            NetSignResult detachedVerify = dean_api.detached_verify(dean_handle, data.getBytes("GBK"), signData, 3);
            int verifyErrCode = detachedVerify.getErrCode();
            if (verifyErrCode != 0){
                System.out.println("SM2Detach签名验签失败：" + verifyErrCode);
            }else {
                System.out.println("SM2Detach签名验签成功");
            }
        }
        dean_api.PSBC_Disconnect(dean_handle);
    }

    /**
     * SM2Attach签名/验签测试
     * @throws UnsupportedEncodingException
     */
    public static void attachSignVerify() throws UnsupportedEncodingException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入签名证书主题：");
        String certSubject = scanner.next();
        System.out.println("请输入原文：");
        String data = scanner.next();

        sign_api dean_api=new sign_api(path);
        Dean_UAM_Handle dean_handle=new Dean_UAM_Handle();

        int i = dean_api.PSBC_Connect(dean_handle);
        if (i != 0){
            System.out.println("签名验签服务连接失败：" + dean_api.PSBC_GetErrCode());
        }

        NetSignResult netSignResult = dean_api.attached_sign(dean_handle, data.getBytes("GBK"), certSubject.getBytes("GBK"), 3);
        int errCode = netSignResult.getErrCode(); // 获取错误码
        if (errCode != 0){
            System.out.println("SM2Attach签名失败：" + errCode);
        }else {
            byte[] signData = netSignResult.getByteArrayResult("signData");
            System.out.println("签名值：" + Util.Bytes2HexString(signData));
            NetSignResult attachedVerify = dean_api.attached_verify(dean_handle, signData, 1);
            int verifyErrCode = attachedVerify.getErrCode();
            if (verifyErrCode != 0){
                System.out.println("SM2Attach签名验签失败：" + verifyErrCode);
            }else {
                System.out.println("SM2Attach签名验签成功");
            }
        }
        dean_api.PSBC_Disconnect(dean_handle);
    }

    /**
     * 数字信封加解密测试
     * @throws IOException
     */
    public static void evpEncodeDecode() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入签名证书主题：");
        String certSubject = scanner.next();
        System.out.println("请输入原文：");
        String data = scanner.next();
        System.out.println("请输入签名证书存放位置：");
        String certPath = scanner.next();

        byte[] certByte = null;
        FileInputStream inStream;
        try {
            inStream = new FileInputStream(certPath);
            int zipLength = inStream.available();
            certByte = new byte[zipLength];
            inStream.read(certByte);
            inStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        sign_api dean_api=new sign_api(path);
        Dean_UAM_Handle dean_handle=new Dean_UAM_Handle();

        int i = dean_api.PSBC_Connect(dean_handle);
        if (i != 0){
            System.out.println("签名验签服务连接失败：" + dean_api.PSBC_GetErrCode());
        }

        NetSignResult evpEncode = dean_api.evp_encode(dean_handle, data.getBytes("GBK"), certByte);
        int errCode = evpEncode.getErrCode(); // 获取错误码
        if (errCode != 0){
            System.out.println("数字信封加密失败：" + errCode);
        }else {
            byte[] result = evpEncode.getByteArrayResult(evpEncode.EVP_TEXT);
            System.out.println("数字信封密文：" + Util.Bytes2HexString(result));
            NetSignResult evpDecode = dean_api.evp_decode(dean_handle, certSubject.getBytes("GBK"), result);
            int evpErrCode = evpDecode.getErrCode();
            if (evpErrCode != 0){
                System.out.println("数字信封解密失败：" + evpErrCode);
            }else {
                System.out.println("SM2签名验签成功");
                String plainData = evpDecode.getStringResult("plainData");
                Base64.Decoder decoder = Base64.getDecoder();
                System.out.println("数字信封解密后明文：" + new String(decoder.decode(plainData),"UTF-8"));
            }
        }
        dean_api.PSBC_Disconnect(dean_handle);
    }

    public static void time() throws IOException, GeneralException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入时间戳签名证书主题：");
        String certSubject = scanner.next();
        System.out.println("请输入原文：");
        String data = scanner.next();
        System.out.println("请输入时间戳签名证书存放位置：");
        String certPath = scanner.next();

        byte[] certByte = null;
        FileInputStream inStream;
        try {
            inStream = new FileInputStream(certPath);
            int zipLength = inStream.available();
            certByte = new byte[zipLength];
            inStream.read(certByte);
            inStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Dean_UAM_Api time_api = new Dean_UAM_Api(path);
        Dean_UAM_Time_Handle time_handle = new Dean_UAM_Time_Handle();

        int i = time_api.PSBC_Connect(time_handle);
        if (i != 0){
            System.out.println("时间戳服务连接失败：" + time_api.PSBC_GetErrCode());
        }

        byte[] tsRequest = time_api.tsRequest(time_handle, certSubject.getBytes("GBK"), data.getBytes(), 1, "".getBytes(), 1);
        System.out.println("时间戳请求值：" + Util.Bytes2HexString(tsRequest));

        byte[] tsResponse = time_api.tsResponse(time_handle, certSubject.getBytes("GBK"), tsRequest, DtTimeStamp.SGD_SM3_SM2);
        System.out.println("时间戳响应值：" + Util.Bytes2HexString(tsResponse));

        boolean tsVerify = time_api.tsVerify(time_handle, tsResponse, DtTimeStamp.SGD_SM3, DtTimeStamp.SGD_SM3_SM2, certByte);
        System.out.println("时间戳校验：" + tsVerify);

        // 每个方法 使用 time_api.PSBC_GetErrCode() 获取错误码

    }

    public static void main(String[] args) throws IOException, GeneralException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入ini配置文件存放位置：");
        path = scanner.next();
        boolean flag = true;
        while (flag){
            System.out.println("请选择：");
            System.out.println("==========密钥管理系统获取密钥测试==========");
            System.out.println("1.获取密钥管理系统密钥");
            System.out.println("==========签名/验签服务测试==========");
            System.out.println("2.SM2裸签名/验签（内部运算SM3）      3.SM2Detach签名/验签");
            System.out.println("4.SM2Attach签名/验签               5.数字信封加密/解密");
//  此接口请查看接口手册调用方式           System.out.println("6.SM2签名/验签（外传SM3摘要值）");
            System.out.println("==========时间戳服务测试==========");
            System.out.println("7.时间戳请求/响应/校验");
            System.out.println("0.退出测试");
            int i = scanner.nextInt();
            switch (i){
                case 1:
                    GetSM4Key();
                    break;
                case 2:
                    sm2RawSignVerify();
                    break;
                case 3:
                    detachSignVerify();
                    break;
                case 4:
                    attachSignVerify();
                    break;
                case 5:
                    evpEncodeDecode();
                    break;
                case 6:
                    time();
                    break;
                case 0:
                    flag = false;
                    break;
            }
        }


    }
}