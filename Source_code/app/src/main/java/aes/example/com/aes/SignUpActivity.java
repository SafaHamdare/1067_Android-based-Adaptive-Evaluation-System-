package aes.example.com.aes;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.Toast;


public class SignUpActivity extends ActionBarActivity {

    EditText etName, etEmail, etPassword, etRPassword;
    Button btnRegister;
    Context context = this;
    String name;
    String email;
    String password;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etRPassword = (EditText) findViewById(R.id.etRPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        sharedPreferences = getSharedPreferences("AES" , Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();



        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = etName.getText().toString();
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();

                if(name.length()<5)
                {
                    Toast.makeText(context, "Full name must contain at least 5 characters", Toast.LENGTH_LONG).show();
                }
                else if(password.length()<5)
                {
                    Toast.makeText(context, "Password must contain at least 5 characters", Toast.LENGTH_LONG).show();
                }
                else if(password.equals(etRPassword.getText().toString())==false)
                {
                    Toast.makeText(context, "Password doesn't match", Toast.LENGTH_LONG).show();
                }
                else
                {

                    new RegisterAsync().execute(new ApiConnector());
                }

            }
        });


    }

    private void checkResult(String result)
    {
        if(result.equals("Success"))
        {
            editor.putString("isLoggedIn" , "true");
            editor.putString("email" , email);
            editor.commit();
            startActivity(new Intent(context , WelcomeActivity.class));
            LoginActivity.login.finish();
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
        //getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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

    private class RegisterAsync extends AsyncTask<ApiConnector, Void, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd= new ProgressDialog(context);
            pd.setMessage("Please Wait");
            pd.setCancelable(false);
            pd.setIndeterminate(false);
            pd.show();
        }

        @Override
        protected String doInBackground(ApiConnector... params) {
            return params[0].registerUser(name, email , password);
        }

        @Override
        protected void onPostExecute(String result) {


            pd.dismiss();
            checkResult(result);

            //super.onPostExecute(aVoid);
        }
    }

}
