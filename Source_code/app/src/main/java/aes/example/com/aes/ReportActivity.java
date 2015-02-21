package aes.example.com.aes;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class ReportActivity extends ActionBarActivity {

    ListView lvTestId;
    SharedPreferences sharedPreferences;
    String email = "";
    ArrayList<String> items;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        sharedPreferences = getSharedPreferences("AES", Context.MODE_PRIVATE);

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
                }
            });
            alertDialog.show();
        }
        else
        {
            email = sharedPreferences.getString("email", "");
            //Toast.makeText(getApplicationContext(), "Email : " + email, Toast.LENGTH_SHORT).show();
            //email = "safahamdare@yahoo.in";
            lvTestId = (ListView) findViewById(R.id.lvTestId);
            new ReportAsync().execute(new ApiConnector());
        }


    }

    private void displayResult(final JSONArray jsonArray)
    {
        JSONObject jobject = new JSONObject();
        if(jsonArray.length()==0)
        {
            //Toast.makeText(context, "No reports Available.!!", Toast.LENGTH_LONG).show();
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setCancelable(false);
            alertDialog.setTitle("No reports available");
            alertDialog.setMessage("We don't have any reports available. Please give test to view your report.");
            alertDialog.setButton("OK" , new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                    //startActivity(new Intent(context , WelcomeActivity.class));
                    finish();

                }
            });
            alertDialog.show();
        }
        else {
            try {
                items = new ArrayList<String>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    jobject = jsonArray.getJSONObject(i);
                    String Test_ID = jobject.getString("Test_ID");
                    //items.add(Test_ID);
                    items.add("Test ID : " + Test_ID + " \n Total marks : " + jobject.getString("Total_Marks"));
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, items);
                lvTestId.setAdapter(arrayAdapter);

                lvTestId.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            // String response = ((TextView) view).getText().toString();
                            Intent intent = new Intent(ReportActivity.this, ReportAnswerActivity.class);
                            intent.putExtra("test_ID", jsonArray.getJSONObject((int) id).getString("Test_ID"));
                        /*

                        intent.putExtra("Question" , jsonArray.getJSONObject((int) id).getString("Questions"));
                        intent.putExtra("Answer" , jsonArray.getJSONObject((int) id).getString("Answer"));
                        intent.putExtra("Solution" , jsonArray.getJSONObject((int) id).getString("Solution"));
                        intent.putExtra("Level" , jsonArray.getJSONObject((int) id).getString("Level"));
                        intent.putExtra("QuesNum" , String.valueOf(id+1));
                        */

                            startActivity(intent);
                            //Toast.makeText(getApplicationContext(), jsonArray.getJSONObject((int) id).getString("Test_ID"), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_logout)
        {
            SharedPreferences preferences = getSharedPreferences("AES", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("email" , "");
            editor.putString("isLoggedIn" , "false");
            editor.commit();
            startActivity(new Intent(getApplicationContext() , LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private class ReportAsync extends AsyncTask<ApiConnector, Void, JSONArray>
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
        protected JSONArray doInBackground(ApiConnector... params)
        {
            return params[0].getTestId(email);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray)
        {
            pd.dismiss();
            displayResult(jsonArray);
        }
    }
}
