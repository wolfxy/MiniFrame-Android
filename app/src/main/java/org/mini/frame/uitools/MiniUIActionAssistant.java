package org.mini.frame.uitools;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Wuquancheng on 2018/8/17.
 */

public class MiniUIActionAssistant {

    public static void setTargetAction(final View view, final Object target, final String action) {
        view.setClickable(true);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Method method;
                    if(action.endsWith(":")) {
                        String _action = action.substring(0, action.length()-1);
                        method = target.getClass().getMethod(_action, view.getClass());
                        method.invoke(target, view);
                    }
                    else {
                        method = target.getClass().getMethod(action);
                        method.invoke(target);
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void startActivity(Context context, Class target) {
        context.startActivity(new Intent(context,target));
    }
}
