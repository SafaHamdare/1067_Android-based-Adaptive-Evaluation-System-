package aes.example.com.aes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends ActionBarActivity {
    EditText etEmail;
    EditText etPassword;
    Button btnLogin;
    Button btnRegister;
    Context context = this;


    String email;
    String password;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public static Activity login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(!ConnectionDetector.isConnectedToInternet(context))
        {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("No Internet Connection");
            alertDialog.setMessage("Please check your network connectivity and try again");
            //alertDialog.setIcon(android.R.drawable.succes.png);
            alertDialog.setButton("OK" , new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            alertDialog.show();
        }
        else {
            login = this;
            sharedPreferences = getSharedPreferences("AES", Context.MODE_PRIVATE);
            //startActivity(new Intent(context, ReportActivity.class));

            if (sharedPreferences.getString("isLoggedIn", "false").equals("true")) {
                startActivity(new Intent(context, WelcomeActivity.class));
                finish();
            }


            //finish();


            etEmail = (EditText) findViewById(R.id.etEmail);
            etPassword = (EditText) findViewById(R.id.etPassword);
            btnLogin = (Button) findViewById(R.id.btnLogin);
            btnRegister = (Button) findViewById(R.id.btnRegister);

            //
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(context, SignUpActivity.class));
                }
            });

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    email = etEmail.getText().toString();
                    password = etPassword.getText().toString();
                    new LoginAsync().execute(new ApiConnector());

                }
            });
        }

    }

    private void checkResult(String result)
    {
        if(result.equals("Success"))
        {
            editor = sharedPreferences.edit();
            editor.putString("isLoggedIn" , "true");
            editor.putString("email" , email);
            editor.commit();
            startActivity(new Intent(context , WelcomeActivity.class));
            finish();
        }
        else
        {
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class LoginAsync extends AsyncTask<ApiConnector, Void, String>
    {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd= new ProgressDialog(context);
            pd.setMessage("Please Wait");
            pd.setCancelable(false);
            pd.setIndeterminate(false);
            pd.show();
        }

        @Override
        protected String doInBackground(ApiConnector... params) {
            return params[0].checkLogin(email, password);
        }

        @Override
        protected void onPostExecute(String s) {
            pd.dismiss();
            checkResult(s);

        }
    }
}
