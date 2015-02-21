package aes.example.com.aes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Suraj on 19-01-2015.
 */
public class ConnectionDetector {
    /*private Context context;
    ConnectionDetector(Context context)
    {
        this.context = context;
    }
    */

    public static boolean isConnectedToInternet(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager!=null)
        {
            NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
            if(networkInfos!=null)
            {
                for(int i=0;i<networkInfos.length;i++)
                {
                    if(networkInfos[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }


}
