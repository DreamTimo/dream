package com.timo.timolib.utils.math;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Java表单验证工具类
 */
@SuppressWarnings("all")
@SuppressLint("all")
public class RegexUtil {
    private RegexUtil() {
    }

    private static RegexUtil instance;

    public static RegexUtil getInstance() {
        if (instance == null) {
            instance = new RegexUtil();
        }
        return instance;
    }

    public boolean isNull(Object[] objs) {
        if (objs == null || objs.length == 0) return true;
        return false;
    }

    public boolean isNull(Integer integer) {
        if (integer == null || integer == 0) return true;
        return false;
    }

    public boolean isNull(Collection collection) {
        if (collection == null || collection.size() == 0) return true;
        return false;
    }

    public boolean isNull(Map map) {
        if (map == null || map.size() == 0) return true;
        return false;
    }

    public boolean isNull(String str) {
        return str == null || "".equals(str.trim()) || "null".equals(str.toLowerCase());
    }


    public boolean isNull(Long longs) {
        if (longs == null || longs == 0) return true;
        return false;
    }

    public boolean isNotNull(Long longs) {
        return !isNull(longs);
    }

    public boolean isNotNull(String str) {
        return !isNull(str);
    }

    public boolean isNotNull(Collection collection) {
        return !isNull(collection);
    }

    public boolean isNotNull(Map map) {
        return !isNull(map);
    }

    public boolean isNotNull(Integer integer) {
        return !isNull(integer);
    }

    public boolean isNotNull(Object[] objs) {
        return !isNull(objs);
    }

    /**
     * 匹配URL地址
     */
    public boolean isUrl(String str) {
        return match(str, "^http://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$");
    }

    /**
     * 匹配密码，以字母开头，长度在6-12之间，只能包含字符、数字和下划线。
     */
    public boolean isPwd(String str) {
        return match(str, "^[a-zA-Z]\\w{6,12}$");
    }

    /**
     * 验证字符，只能包含中文、英文、数字、下划线等字符。
     */
    public boolean stringCheck(String str) {
        return match(str, "^[a-zA-Z0-9\u4e00-\u9fa5-_]+$");
    }

    /**
     * 匹配Email地址
     */
    public boolean isEmail(String str) {
        return match(str, "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
    }

    /**
     * 匹配非负整数（正整数+0）
     */
    public boolean isInteger(String str) {
        return match(str, "^[+]?\\d+$");
    }

    /**
     * 判断数值类型，包括整数和浮点数
     */
    public boolean isNumeric(String str) {
        if (isFloat(str) || isInteger(str)) return true;
        return false;
    }

    /**
     * 只能输入数字
     */
    public boolean isDigits(String str) {
        return match(str, "^[0-9]*$");
    }

    /**
     * 匹配正浮点数
     */
    public boolean isFloat(String str) {
        return match(str, "^[-\\+]?\\d+(\\.\\d+)?$");
    }

    /**
     * 联系电话(手机/电话皆可)验证
     */
    public boolean isTel(String text) {
        if (isMobile(text) || isPhone(text)) return true;
        return false;
    }

    /**
     * 电话号码验证
     */
    public boolean isPhone(String text) {
        return match(text, "^(\\d{3,4}-?)?\\d{7,9}$");
    }

    /**
     * 手机号码验证
     */
    public boolean isMobile(String text) {
        if (text.length() != 11) return false;
        return match(text, "^1[0-9]{10}$");
    }

    /**
     * 身份证号码验证
     */
    public boolean isIdCardNo(String text) {
        //return match(text, "^(\\d{6})()?(\\d{4})(\\d{2})(\\d{2})(\\d{3})(\\w)$");
        return match(text, "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$|[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|[xX])$");
    }

    /**
     * 邮政编码验证
     */
    public boolean isZipCode(String text) {
        return match(text, "^[0-9]{6}$");
    }

    /**
     * 姓名验证
     */
    public boolean isRealName(String text) {
        return match(text, "^[\\x{4e00}-\\x{9fa5}A-Za-z_]{2,4}$");
    }

    public boolean isCorrect(String text) {
        return match(text, "^[\\x{4e00}-\\x{9fa5}]+$");
    }

    /**
     * 昵称验证
     */
    public boolean isUserName(String text) {
        return match(text, "^[\\x{4e00}-\\x{9fa5}A-Za-z_]{2,10}$");
    }

    /**
     * 验证邀请码
     */
    public boolean isInviteCode(String text) {
        //return match(text, "^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\\d{8})$");
        return match(text, "^[a-f0-9]{8}$");
    }

    /**
     * 验证密码
     * 字母和数据混排6位以上
     */
    public boolean isPassWord(String text) {
        return match(text, "^[0-9A-Za-z]{6,18}$");
    }

    /**
     * 判断整数num是否等于0
     */
    public boolean isIntEqZero(int num) {
        return num == 0;
    }

    /**
     * 判断整数num是否大于0
     */
    public boolean isIntGtZero(int num) {
        return num > 0;
    }

    /**
     * 判断整数num是否大于或等于0
     */
    public boolean isIntGteZero(int num) {
        return num >= 0;
    }

    /**
     * 判断浮点数num是否等于0
     */
    public boolean isFloatEqZero(float num) {
        return num == 0f;
    }

    /**
     * 判断浮点数num是否大于0
     */
    public boolean isFloatGtZero(float num) {
        return num > 0f;
    }

    /**
     * 判断浮点数num是否大于或等于0
     */
    public boolean isFloatGteZero(float num) {
        return num >= 0f;
    }

    /**
     * 判断是否为合法字符(a-zA-Z0-9-_)
     */
    public boolean isRightfulString(String text) {
        return match(text, "^[A-Za-z0-9_-]+$");
    }

    /**
     * 判断英文字符(a-zA-Z)
     */
    public boolean isEnglish(String text) {
        return match(text, "^[A-Za-z]+$");
    }

    /**
     * 判断中文字符(包括汉字和符号)
     */
    public boolean isChineseChar(String text) {
        return match(text, "^[\u0391-\uFFE5]+$");
    }

    /**
     * 匹配汉字
     */
    public boolean isChinese(String text) {
        return match(text, "^[\u4e00-\u9fa5]+$");
    }

    /**
     * 是否包含中英文特殊字符，除英文"-_"字符外
     */
    public boolean isContainsSpecialChar(String text) {
        if (TextUtils.isEmpty((text))) return false;
        String[] chars = {"[", "`", "~", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "+", "=", "|", "{", "}", "'",
                ":", ";", "'", ",", "[", "]", ".", "<", ">", "/", "?", "~", "！", "@", "#", "￥", "%", "…", "&", "*", "（", "）",
                "—", "+", "|", "{", "}", "【", "】", "‘", "；", "：", "”", "“", "’", "。", "，", "、", "？", "]"};
        for (String ch : chars) {
            if (text.contains(ch)) return true;
        }
        return false;
    }

    /**
     * 过滤中英文特殊字符，除英文"-_"字符外
     */
    public String stringFilter(String text) {
        String regExpr = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regExpr);
        Matcher m = p.matcher(text);
        return m.replaceAll("").trim();
    }

    /**
     * 判断是否是URL
     */
    public boolean isURL(String url) {
        if (!TextUtils.isEmpty(url)) {
            int pos = url.indexOf("http://");
            if (pos == -1) {
                pos = url.indexOf("https://");
                if (pos == -1) {
                    return false;
                }
                return true;
            }
            return true;
        }
        return false;
    }

    /**
     * 过滤html代码
     */
    public String htmlFilter(String inputString) {
        String htmlStr = inputString; // 含html标签的字符串
        String textStr = "";
        Pattern p_script;
        Matcher m_script;
        Pattern p_style;
        Matcher m_style;
        Pattern p_html;
        Matcher m_html;
        Pattern p_ba;
        Matcher m_ba;

        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            // }
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            // }
            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
            String patternStr = "\\s+";

            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签

            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签

            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签

            p_ba = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
            m_ba = p_ba.matcher(htmlStr);
            htmlStr = m_ba.replaceAll(""); // 过滤空格

            textStr = htmlStr;

        } catch (Exception e) {
            System.err.println("Html2Text: " + e.getMessage());
        }
        return textStr;// 返回文本字符串
    }

    /**
     * 正则表达式匹配
     */
    private boolean match(String text, String reg) {
        if (TextUtils.isEmpty(text) || TextUtils.isEmpty(reg))
            return false;
        return Pattern.compile(reg).matcher(text).matches();
    }


// 附 ： 常用的正则表达式：
//    匹配特定数字：
//    ^[1-9]d*$　 　 //匹配正整数
//    ^-[1-9]d*$ 　 //匹配负整数
//    ^-?[1-9]d*$　　 //匹配整数
//    ^[1-9]d*|0$　 //匹配非负整数（正整数 + 0）
//    ^-[1-9]d*|0$　　 //匹配非正整数（负整数 + 0）
//    ^[1-9]d*.d*|0.d*[1-9]d*$　　 //匹配正浮点数
//    ^-([1-9]d*.d*|0.d*[1-9]d*)$　 //匹配负浮点数
//    ^-?([1-9]d*.d*|0.d*[1-9]d*|0?.0+|0)$　 //匹配浮点数
//    ^[1-9]d*.d*|0.d*[1-9]d*|0?.0+|0$　　 //匹配非负浮点数（正浮点数 + 0）
//    ^(-([1-9]d*.d*|0.d*[1-9]d*))|0?.0+|0$　　//匹配非正浮点数（负浮点数 + 0）
//    评注：处理大量数据时有用，具体应用时注意修正
//
//    匹配特定字符串：
//    ^[A-Za-z]+$　　//匹配由26个英文字母组成的字符串
//    ^[A-Z]+$　　//匹配由26个英文字母的大写组成的字符串
//    ^[a-z]+$　　//匹配由26个英文字母的小写组成的字符串
//    ^[A-Za-z0-9]+$　　//匹配由数字和26个英文字母组成的字符串
//    ^w+$　　//匹配由数字、26个英文字母或者下划线组成的字符串
//
//    在使用RegularExpressionValidator验证控件时的验证功能及其验证表达式介绍如下:
//
//    只能输入数字：“^[0-9]*$”
//    只能输入n位的数字：“^d{n}$”
//    只能输入至少n位数字：“^d{n,}$”
//    只能输入m-n位的数字：“^d{m,n}$”
//    只能输入零和非零开头的数字：“^(0|[1-9][0-9]*)$”
//    只能输入有两位小数的正实数：“^[0-9]+(.[0-9]{2})?$”
//    只能输入有1-3位小数的正实数：“^[0-9]+(.[0-9]{1,3})?$”
//    只能输入非零的正整数：“^+?[1-9][0-9]*$”
//    只能输入非零的负整数：“^-[1-9][0-9]*$”
//    只能输入长度为3的字符：“^.{3}$”
//    只能输入由26个英文字母组成的字符串：“^[A-Za-z]+$”
//    只能输入由26个大写英文字母组成的字符串：“^[A-Z]+$”
//    只能输入由26个小写英文字母组成的字符串：“^[a-z]+$”
//    只能输入由数字和26个英文字母组成的字符串：“^[A-Za-z0-9]+$”
//    只能输入由数字、26个英文字母或者下划线组成的字符串：“^w+$”
//    验证用户密码:“^[a-zA-Z]\\w{5,17}$”正确格式为：以字母开头，长度在6-18之间，
//
//    只能包含字符、数字和下划线。
//    验证是否含有^%&’,;=?$”等字符：“[^%&’,;=?$x22]+”
//    只能输入汉字：“^[u4e00-u9fa5],{0,}$”
//    验证Email地址：“^w+[-+.]w+)*@w+([-.]w+)*.w+([-.]w+)*$”
//    验证InternetURL：“^http://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$”
//    验证电话号码：“^((d{3,4})|d{3,4}-)?d{7,8}$”
//
//    正确格式为：“XXXX-XXXXXXX”，“XXXX-XXXXXXXX”，“XXX-XXXXXXX”，
//
//    “XXX-XXXXXXXX”，“XXXXXXX”，“XXXXXXXX”。
//    验证身份证号（15位或18位数字）：“^d{15}|d{}18$”
//    验证一年的12个月：“^(0?[1-9]|1[0-2])$”正确格式为：“01”-“09”和“1”“12”
//    验证一个月的31天：“^((0?[1-9])|((1|2)[0-9])|30|31)$” 正确格式为：“01”“09”和“1”“31”。
//
//    匹配中文字符的正则表达式： [u4e00-u9fa5]
//    匹配双字节字符(包括汉字在内)：[^x00-xff]
//    匹配空行的正则表达式：n[s| ]*r
//    匹配HTML标记的正则表达式：/< (.*)>.*|< (.*) />/
//    匹配首尾空格的正则表达式：(^s*)|(s*$)
//    匹配Email地址的正则表达式：w+([-+.]w+)*@w+([-.]w+)*.w+([-.]w+)*
//    匹配网址URL的正则表达式：^http://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$
}