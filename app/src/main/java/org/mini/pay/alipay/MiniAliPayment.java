package org.mini.pay.alipay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;

import org.mini.frame.log.MiniLogger;

import java.net.URLDecoder;


@SuppressLint("HandlerLeak")
public class MiniAliPayment {

    private static final String TAG = MiniAliPayment.class.getSimpleName();
	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_CHECK_FLAG = 2;

    public interface MiniAliPaymentListener {
        void onResult(boolean success, String desc);
    }

    private MiniAliPaymentListener listener;

	//-----------------------------------------------------------------
	//单例
	private volatile static MiniAliPayment aliPay = null;
	//单例
	public static MiniAliPayment shard()
	{
		if(aliPay ==null)
		{
			synchronized(MiniAliPayment.class)
			{
				if(aliPay ==null)
				{
					aliPay = new MiniAliPayment();
				}
			}
		}
		return aliPay;
	}
	
	//构造函数
	private MiniAliPayment()
    {
    }
	
	//-----------------------------------------------------------------
	//Handler
	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what) 
			{
				case SDK_PAY_FLAG: 
				{
					AliPayResult payResult = new AliPayResult((String) msg.obj);
					// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
					String resultInfo = payResult.getResult();
					String resultStatus = payResult.getResultStatus();
	
					// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
					if (TextUtils.equals(resultStatus, "9000"))
					{
					    if (listener != null) {
                            listener.onResult(true, resultInfo);
                        }
					} 
					else 
					{
                        if (listener != null) {
                            listener.onResult(false, "["+resultStatus+"]" + resultInfo);
                        }
						// 判断resultStatus 为非“9000”则代表可能支付失败
						// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
					}
                    listener = null;
                    break;
				}
				case SDK_CHECK_FLAG: 
				{
					
					break;
				}
				default:
					break;
			}
            listener = null;
		};
	};
	
	//支付
	public void pay(final Activity context, final String payStr, MiniAliPaymentListener listener) {
	    this.listener = listener;
		Runnable payRunnable = new Runnable() {
			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(context);
				// 调用支付接口，获取支付结果
                String paramString = payStr;
                try {
                    paramString = URLDecoder.decode(payStr, "utf-8");
                }
                catch (Exception e) {
                    MiniLogger.get(TAG).e(e);
                }
				String result = alipay.pay(paramString, true);
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};
		//异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}	

}
