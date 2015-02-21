package aes.example.com.aes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Suraj on 19-01-2015.
 */
public class AlertDialogInterface {
    public static void showAlertDialog(Context context, String title, String message)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        //alertDialog.setIcon(android.R.drawable.succes.png);
        alertDialog.setButton("OK" , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
