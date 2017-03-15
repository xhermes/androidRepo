package com.sjs.sjsapp.utils;

import android.text.TextUtils;

import com.sjs.sjsapp.log.Logger;

/**
 * Created by xeno on 2016/3/14.
 */
public class StringUtils {
    public static boolean checkPhone(String phone) {
        return phone.length() >= 11;
    }

    public static String hidePhone(String pNumber) {
        if (!TextUtils.isEmpty(pNumber) && pNumber.length() > 6) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < pNumber.length(); i++) {
                char c = pNumber.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }

            return sb.toString();
        }
        return null;
    }

    public static String hideName(String name) {
        if(name.length() == 1) {
            return "*";
        } else if(name.length() == 2) {
            return name.substring(0,1) + "*";
        } else {
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < name.length() - 2; i++) {
                sb.append("*");
            }
            return name.substring(0,1) + sb.toString() + name.substring(name.length() - 1, name.length());
        }
    }

    public static String hideId(String id) {
        if(id.length() >= 18) {
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < id.length() - 8; i++) {
                sb.append("*");
            }

            return id.substring(0, 4) + sb.toString() + id.substring(id.length() - 4, id.length());
        } else {
            Logger.LogError("身份证少于18位");
            return id;
        }
    }

    public static String hideCardNo(String cardNo) {
        if(cardNo.length() >= 8) {
            StringBuilder sb = new StringBuilder();
            for(int i = 5; i < cardNo.length() - 3; i++) {
                sb.append("*");
            }

            return cardNo.substring(0, 6) + sb.toString() + cardNo.substring(cardNo.length() - 3, cardNo.length());
        } else {
            return "****";
        }
    }

    /**
     * 将每三个数字加上逗号处理（通常使用金额方面的编辑）
     *
     * @param str 无逗号的数字
     */

    private static String addComma(String str) {
// 将传进数字反转
        String reverseStr = new StringBuilder(str).reverse().toString();
        String strTemp = "";
        for (int i = 0; i < reverseStr.length(); i++) {
            if (i * 3 + 3 > reverseStr.length()) {
                strTemp += reverseStr.substring(i * 3, reverseStr.length());
                break;
            }
            strTemp += reverseStr.substring(i * 3, i * 3 + 3) + ",";
        }
// 将[789,456,] 中最后一个[,]去除
        if (strTemp.endsWith(",")) {
            strTemp = strTemp.substring(0, strTemp.length() - 1);
        }
// 将数字重新反转
        String resultStr = new StringBuilder(strTemp).reverse().toString();
        return resultStr;
    }
}
