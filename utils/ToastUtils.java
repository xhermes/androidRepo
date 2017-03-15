package com.sjs.sjsapp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sjs.sjsapp.R;

/**
 * Created by xeno on 2016/2/22.
 */
public class ToastUtils {

    private static Toast toast;

    /**
     * 带黄色叹号错误标志的toast
     */
    public static void toastError(Context context, String msg) {
        if(toast != null) {
            toast.cancel();
        }

        toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);

        View v = LayoutInflater.from(context).inflate(R.layout.toast_with_warning, null);
        View errorView = v.findViewById(R.id.toast_iv_error);
        TextView messageView = (TextView)v.findViewById(R.id.toast_tv_message);

        errorView.setVisibility(View.VISIBLE);
        messageView.setText(msg);

        toast.setView(v);
        toast.show();
    }

    public static void toast(Context context, String msg) {
        if(toast != null) {
            toast.cancel();
        }

        toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);

        View v = LayoutInflater.from(context).inflate(R.layout.toast_with_warning, null);
        TextView messageView = (TextView)v.findViewById(R.id.toast_tv_message);
        messageView.setText(msg);

        toast.setView(v);
        toast.show();
    }


}
