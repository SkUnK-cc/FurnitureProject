package example.com.furnitureproject.utils;

import android.content.Context;
import android.widget.Toast;

import example.com.furnitureproject.FurnitureApplication;

/**
 * @author luo
 * @date 2017/8/25
 */
public class ToastUtil {
    public static void showShort(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    public static void showShort( String message) {
        Toast.makeText(FurnitureApplication.Companion.getInstance(), message, Toast.LENGTH_SHORT).show();
    }
}
