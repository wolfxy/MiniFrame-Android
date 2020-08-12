package org.mini.pay.wxpay;


import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.mini.frame.activity.MiniUIActivity;
import org.mini.frame.application.MiniApplication;
import org.mini.frame.log.MiniLogger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


public class MiniWXPayment {

	public static String WX_APPID = "";
	
	private IWXAPI wxApi = null;

	private static final String TAG = MiniWXPayment.class.getSimpleName();

	public interface MiniWXPaymentListener {
        void onResult(boolean success, String desc);
    }

    private MiniWXPaymentListener listener;
	//-----------------------------------------------------------------
	//单例
	private volatile static MiniWXPayment wx_share = null;
	//单例
	public static MiniWXPayment shard()
	{
		if(wx_share==null)
		{
			synchronized(MiniWXPayment.class)
			{
				if(wx_share==null)
				{
					wx_share = new MiniWXPayment();
				}
			}
		}
		return wx_share;
	}

    //构造函数
    private MiniWXPayment()
    {
        init();
    }

    public void init()
    {
        wxApi = WXAPIFactory.createWXAPI(MiniApplication.application, null);
        wxApi.registerApp(WX_APPID);
    }

    public void onResult(boolean success, String desc)
    {
        if (this.listener != null) {
            this.listener.onResult(success, desc);
        }
        this.listener = null;
    }

	//支付
	public void pay(MiniUIActivity activity, String payStr, MiniWXPaymentListener listener)
	{
	    try {
            payStr = URLDecoder.decode(payStr, "utf-8");
        }
	    catch (Exception e) {
            MiniLogger.get(TAG).e(e);
        }
	    this.listener = listener;
		if(wxApi != null)
		{
			if (!wxApi.isWXAppInstalled())
			{
                activity.toastMessage("请您先安装微信");
				return ;
			}
			wxApi.registerApp(WX_APPID);
			PayReq request = new PayReq();
			String[] array = payStr.split("&");
			for(int i=0;i<array.length;i++)
			{
				String[] dataArray = array[i].split("=");
				if (dataArray.length == 2)
				{
					String key   = dataArray[0];
					String value = dataArray[1];
					
					if (key.equals("appid"))
					{
						request.appId = value;
					}
					else if (key.equals("noncestr"))
					{
						request.nonceStr = value;
					}
					else if (key.equals("package"))
		            {
		                try {
							request.packageValue = URLDecoder.decode(value, "UTF-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
		            }
		            else if (key.equals("partnerid"))
		            {
		                request.partnerId = value;
		            }
		            else if (key.equals("prepayid"))
		            {
		                request.prepayId = value;
		            }
		            else if (key.equals("timestamp"))
		            {
		                request.timeStamp = value; 
		            }
		            else if (key.equals("sign"))
		            {
		                request.sign = value;
		            }
				}
			}
			wxApi.sendReq(request);
		}
	}
}
