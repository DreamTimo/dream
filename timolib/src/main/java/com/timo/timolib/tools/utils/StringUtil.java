package com.timo.timolib.tools.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author luyz
 *         字符串 工具类
 */
public class StringUtil {
    public static final String EMPTY = "";
    public static final int INDEX_NOT_FOUND = -1;
    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    private StringUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * is null or its length is 0 or it is made by space
     * <p>
     * <pre>
     * isBlank(null) = true;
     * isBlank(&quot;&quot;) = true;
     * isBlank(&quot;  &quot;) = true;
     * isBlank(&quot;a&quot;) = false;
     * isBlank(&quot;a &quot;) = false;
     * isBlank(&quot; a&quot;) = false;
     * isBlank(&quot;a b&quot;) = false;
     * </pre>
     *
     * @param str
     * @return if string is null or its size is 0 or it is made by space, return true, else return false.
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    /**
     * 判断字符是否为空
     * <pre>
     * isEmpty(null) = true;
     * isEmpty(&quot;&quot;) = true;
     * isEmpty(&quot;  &quot;) = false;
     * </pre>
     *
     * @param cs
     * @return if string is null or its size is 0, return true, else return false.
     */
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * 判断字符是否不是空
     *
     * @param s
     * @return if string not is null or its size not is 0, return true, else return false.
     */
    public static boolean notEmpty(String s) {
        return s != null && !"".equals(s) && !"null".equals(s);
    }

    /**
     * 获取字符串长度
     * <p>
     * <pre>
     * length(null) = 0;
     * length(\"\") = 0;
     * length(\"abc\") = 3;
     * </pre>
     *
     * @param str
     * @return if str is null or empty, return 0, else return {@link CharSequence#length()}.
     */
    public static int length(CharSequence str) {
        return str == null ? 0 : str.length();
    }

    /**
     * 首字母 大写
     * <p>
     * <pre>
     * capitalizeFirstLetter(null)     =   null;
     * capitalizeFirstLetter("")       =   "";
     * capitalizeFirstLetter("2ab")    =   "2ab"
     * capitalizeFirstLetter("a")      =   "A"
     * capitalizeFirstLetter("ab")     =   "Ab"
     * capitalizeFirstLetter("Abc")    =   "Abc"
     * </pre>
     *
     * @param str
     * @return
     */
    public static String capitalizeFirstLetter(String str) {
        if (isEmpty(str)) {
            return str;
        }

        char c = str.charAt(0);
        return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str : new StringBuilder(str.length())
                .append(Character.toUpperCase(c)).append(str.substring(1)).toString();
    }

    /**
     * encoded in utf-8  url编码
     * <p>
     * <pre>
     * utf8Encode(null)        =   null
     * utf8Encode("")          =   "";
     * utf8Encode("aa")        =   "aa";
     * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
     * </pre>
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException if an error occurs
     */
    public static String utf8Encode(String str) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
            }
        }
        return str;
    }

    /**
     * get innerHtml from href
     * <p>
     * <pre>
     * getHrefInnerHtml(null)                                  = ""
     * getHrefInnerHtml("")                                    = ""
     * getHrefInnerHtml("mp3")                                 = "mp3";
     * getHrefInnerHtml("&lt;a innerHtml&lt;/a&gt;")                    = "&lt;a innerHtml&lt;/a&gt;";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com"&gt;innerHtml&lt;/a&gt;")               = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com" title="baidu"&gt;innerHtml&lt;/a&gt;") = "innerHtml";
     * getHrefInnerHtml("   &lt;a&gt;innerHtml&lt;/a&gt;  ")                           = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                      = "innerHtml";
     * getHrefInnerHtml("jack&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                  = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml1&lt;/a&gt;&lt;a&gt;innerHtml2&lt;/a&gt;")        = "innerHtml2";
     * </pre>
     *
     * @param href
     * @return <ul>
     * <li>if href is null, return ""</li>
     * <li>if not match regx, return source</li>
     * <li>return the last string that match regx</li>
     * </ul>
     */
    public static String getHrefInnerHtml(String href) {
        if (isEmpty(href)) {
            return "";
        }

        String hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
        Pattern hrefPattern = Pattern.compile(hrefReg, Pattern.CASE_INSENSITIVE);
        Matcher hrefMatcher = hrefPattern.matcher(href);
        if (hrefMatcher.matches()) {
            return hrefMatcher.group(1);
        }
        return href;
    }

    /**
     * process special char in html
     * <p>
     * <pre>
     * htmlEscapeCharsToString(null) = null;
     * htmlEscapeCharsToString("") = "";
     * htmlEscapeCharsToString("mp3") = "mp3";
     * htmlEscapeCharsToString("mp3&lt;") = "mp3<";
     * htmlEscapeCharsToString("mp3&gt;") = "mp3\>";
     * htmlEscapeCharsToString("mp3&amp;mp4") = "mp3&mp4";
     * htmlEscapeCharsToString("mp3&quot;mp4") = "mp3\"mp4";
     * htmlEscapeCharsToString("mp3&lt;&gt;&amp;&quot;mp4") = "mp3\<\>&\"mp4";
     * </pre>
     *
     * @param source
     * @return
     */
    public static String htmlEscapeCharsToString(String source) {
        return isEmpty(source) ? source : source.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
                .replaceAll("&amp;", "&").replaceAll("&quot;", "\"");
    }

    /**
     * 从左侧开始截取str返回separator前面的字符串(不包含separator)
     *
     * @param str
     * @param separator
     * @return
     */
    public static String substringBefore(String str, String separator) {
        if (isEmpty(str) || separator == null) {
            return str;
        }
        if (separator.length() == 0) {
            return EMPTY;
        }
        int pos = str.indexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return str;
        }
        return str.substring(0, pos);
    }

    /**
     * 从左侧开始截取str返回separator后面的字符串(不包含separator)
     *
     * @param str
     * @param separator
     * @return
     */
    public static String substringAfter(String str, String separator) {
        if (isEmpty(str)) {
            return str;
        }
        if (separator == null) {
            return EMPTY;
        }
        int pos = str.indexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return EMPTY;
        }
        return str.substring(pos + separator.length());
    }

    /**
     * 从右侧开始截取str返回separator前面的字符串(不包含separator)
     *
     * @param str
     * @param separator
     * @return
     */
    public static String substringBeforeLast(String str, String separator) {
        if (isEmpty(str) || isEmpty(separator)) {
            return str;
        }
        int pos = str.lastIndexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return str;
        }
        return str.substring(0, pos);
    }

    /**
     * 从右侧开始截取str返回separator后面的字符串(不包含separator)
     *
     * @param str
     * @param separator
     * @return
     */
    public static String substringAfterLast(String str, String separator) {
        if (isEmpty(str)) {
            return str;
        }
        if (isEmpty(separator)) {
            return EMPTY;
        }
        int pos = str.lastIndexOf(separator);
        if (pos == INDEX_NOT_FOUND || pos == (str.length() - separator.length())) {
            return EMPTY;
        }
        return str.substring(pos + separator.length());
    }

    /**
     * 返回两个tag字符串在str中间的字符串,如果tag在str中只有一个返回null
     *
     * @param str
     * @param tag
     * @return
     */
    public static String substringBetween(String str, String tag) {
        return substringBetween(str, tag, tag);
    }

    /**
     * 返回open和close字符串在str中间的字符串,如果没有返回null
     *
     * @param str
     * @return
     */
    public static String substringBetween(String str, String open, String close) {
        if (str == null || open == null || close == null) {
            return null;
        }
        int start = str.indexOf(open);
        if (start != INDEX_NOT_FOUND) {
            int end = str.indexOf(close, start + open.length());
            if (end != INDEX_NOT_FOUND) {
                return str.substring(start + open.length(), end);
            }
        }
        return null;
    }

    /**
     * 返回open和close字符串在str中间的字符串的字符串数组,如果没有返回null
     *
     * @param str
     * @return
     */
    public static String[] substringsBetween(String str, String open, String close) {
        if (str == null || isEmpty(open) || isEmpty(close)) {
            return null;
        }
        int strLen = str.length();
        if (strLen == 0) {
            return EMPTY_STRING_ARRAY;
        }
        int closeLen = close.length();
        int openLen = open.length();
        List<String> list = new ArrayList<String>();
        int pos = 0;
        while (pos < (strLen - closeLen)) {
            int start = str.indexOf(open, pos);
            if (start < 0) {
                break;
            }
            start += openLen;
            int end = str.indexOf(close, start);
            if (end < 0) {
                break;
            }
            list.add(str.substring(start, end));
            pos = end + closeLen;
        }
        if (list.isEmpty()) {
            return null;
        }
        return list.toArray(new String[list.size()]);
    }

    /**
     * 字符串转换字符串数组
     *
     * @return
     */
    public static String[] split(String str) {
        if (str == null || str.trim().equals(""))
            return null;
        // ‘,’作为分隔符
        StringTokenizer commaToker = new StringTokenizer(str, ", ");
        // commaToker.countTokens()分隔符的数量
        String[] result = new String[commaToker.countTokens()];
        int m = 0;
        // hasMoreTokens()是否还有分隔符
        while (commaToker.hasMoreTokens()) {
            // 翻译从当前位置到下一个分隔符的字符串
            result[m] = commaToker.nextToken();
            m++;
        }
        return result;
    }

    /**
     * 字符串转整形
     *
     * @author jmh
     */
    public static int strToInt(String str) {
        if (TextUtils.isEmpty(str)) {
            return -1;
        }
        int value;
        try {
            if (str.contains(".")) {
                str = str.substring(0, str.indexOf('.'));
            }
            if (str != null) {
                value = Integer.parseInt(str.trim());
            } else {
                value = -1;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            value = -1;
        }
        return value;
    }

    public static boolean strToBoolean(String str) {
        boolean value = false;

        try {
            value = Boolean.parseBoolean(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            value = false;
        }

        return value;
    }

    /**
     * 字符串转长整形
     *
     * @author jmh
     */
    public static long strToLong(String str) {
        long value;
        try {
            value = Long.valueOf(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            value = -1;
        }
        return value;
    }

    /**
     * 为文本添加下划线
     *
     * @author jmh
     */
    public static SpannableString setTextUnderline(String str) {
        SpannableString spString = new SpannableString(str);
        spString.setSpan(new UnderlineSpan(), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spString;
    }

    /**
     * 输入流转UTF-8编码
     *
     * @return StringBuffer
     * @throws IOException
     */
    public static String toUTF8String(InputStream is) throws IOException {
        if (is == null)
            return null;
        StringBuffer sb = new StringBuffer();
        InputStreamReader isReader = new InputStreamReader(is, "UTF-8");
        BufferedReader br = new BufferedReader(isReader);
        String data = "";
        while ((data = br.readLine()) != null) {
            sb.append(data);
        }
        return sb.toString();
    }

    /**
     * String 转 InputStream
     *
     * @param str
     * @return
     */
    public static InputStream stringToInputStream(String str) {

        if (str == null) {
            return null;
        }
        ByteArrayInputStream result = null;
        try {
            result = new ByteArrayInputStream(str.getBytes());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;

    }

    /**
     * String转UTF-8
     *
     * @param strContent
     * @return
     */
    public static String formatUTF8(String strContent) {
        try {
            return new String(strContent.getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 判断是不是中文
     *
     * @param value
     * @return
     */
    public static boolean isChinese(String value) {
        String chinese = "[\u4e00-\u9fa5]"; // 匹配中文节字符
        if (value.matches(chinese)) {
            return true;
        }
        return false;
    }

    /**
     * 判断是不是英文
     *
     * @param value
     * @return
     */
    public static boolean isEnglish(String value) {
        String english = "[a-zA-Z]";// 匹配英文字符
        if (value.matches(english)) {
            return true;
        }
        return false;
    }

    /**
     * 验证一串字符串是否全为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        String pattern = "[0-9]+";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 判断是否是电话号码
     *
     * @param phone
     * @return
     */
    public static Boolean isPhoneNumbers(String phone) {
        Boolean isvalid = false;
        Pattern p1 = Pattern.compile("^((0?(13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0,6-8]))\\d{8})$");
        if ((p1.matcher(phone).matches())) {
            isvalid = true;
        }
        return isvalid;
    }

    /**
     * 判断是否为邮箱
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static String findChinese(String str) {
        String chinese = "[\u4e00-\u9fa5]";
        Pattern p = Pattern.compile(chinese);
        Matcher m = p.matcher(str);
        while (m.find()) {
            str += m.group().toString();
        }
        return str;
    }

    public static boolean isURL(String url) {
        if (notEmpty(url)) {
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
     * bitmap 图片转成string
     *
     * @param bitmap
     * @return
     */
    public static String convertBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组  
        return Base64.encodeToString(appicon, Base64.DEFAULT);

    }

    /**
     * string转成bitmap
     *
     * @param st
     */
    public static Bitmap convertStringToBitmap(String st) {
        // OutputStream out;  
        Bitmap bitmap = null;
        try {
            // out = new FileOutputStream("/sdcard/aa.jpg");  
            byte[] bitmapArray;
            bitmapArray = Base64.decode(st, Base64.DEFAULT);
            bitmap =
                    BitmapFactory.decodeByteArray(bitmapArray, 0,
                            bitmapArray.length);
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);  
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }
}
