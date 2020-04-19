package example.com.testlib;

import java.util.regex.Pattern;

public class MyClass {

    public static void main(String[] args) {
        String s = "0";
        System.out.println(isInteger(s));
        System.out.println(isDouble(s));
    }

    //判断整数（int）
    private static boolean isInteger(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    //判断浮点数（double和float）
    private static boolean isDouble(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?\\d*[.]\\d+$"); // 之前这里正则表达式错误，现更正
        return pattern.matcher(str).matches();
    }
}
