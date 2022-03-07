package com.itheima.reggie.common;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;

/**
 * 短信发送工具类
 */
public class SMSUtils {


    /**
     * 签名
     */
    private static final String signName = "coderzhao";
    /**
     * 模板
     */
    private static final String templateCode = "SMS_159782306";
    /**
     * 访问id
     */
    private static final String accessKeyId = "LTAI5tDi85dsuXQzWDsVcuZh";
    /**
     * 密钥
     */
    private static final String secret = "CuyudHAsJ2WVx9Yo8RNjvq7fi6Hcdq";

    /**
     * 发送短信获取短信验证码
     *
     * @param phone 手机号
     * @param code  验证码
     */
    public static void sendMessage(String phone, String code) {
        sendMessage(signName, templateCode, phone, code);
    }


    /**
     * 发送短信
     *
     * @param signName     签名
     * @param templateCode 模板
     * @param phoneNumbers 手机号
     * @param param        参数
     */
    public static void sendMessage(String signName, String templateCode, String phoneNumbers, String param) {
        DefaultProfile profile = DefaultProfile
                .getProfile("cn-hangzhou",
                        accessKeyId,
                        secret);
        IAcsClient client = new DefaultAcsClient(profile);

        SendSmsRequest request = new SendSmsRequest();
        request.setSysRegionId("cn-hangzhou");
        request.setPhoneNumbers(phoneNumbers);
        request.setSignName(signName);
        request.setTemplateCode(templateCode);
        request.setTemplateParam("{\"code\":\"" + param + "\"}");
        try {
            SendSmsResponse response = client.getAcsResponse(request);
            System.out.println("短信发送成功");
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        sendMessage("coderzhao", "SMS_159782306", "15715816936", "888888");
    }

}