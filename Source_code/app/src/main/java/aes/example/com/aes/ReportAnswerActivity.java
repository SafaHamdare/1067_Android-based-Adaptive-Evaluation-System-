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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class ReportAnswerActivity extends ActionBarActivity {

    TextView tvReport;
    ListView lvQuestions;
    String email;
    String test_ID;
    Context context = this;

    ArrayList<String> reportList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_answer);
        //tvReport = (TextView) findViewById(R.id.tvReport);
        lvQuestions = (ListView) findViewById(R.id.lvQuestions);

        Bundle extras = getIntent().getExtras();
        test_ID = extras.getString("test_ID");

        email = getSharedPreferences("AES", Context.MODE_PRIVATE).getString("email" , "");

        new ReportAnswerAsync().execute(new ApiConnector());

    }

    private void displayResult(final JSONArray jsonArray)
    {
        String result = "";

        reportList = new ArrayList<String>();
        try
        {

            for(int i=0;i<jsonArray.length();i++)
            {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                /*
                result = result+"Question No "+(i+1)+" : "
                        +jsonArray.getJSONObject(i).getString("Questions")+"\n"
                        +"Level: " + jsonArray.getJSONObject(i).getString("Level")+"\n"
                        +"Correct_Answer: "+jsonArray.getJSONObject(i).getString("Correct_Answer")+"\n"
                        +"Solution: "+jsonArray.getJSONObject(i).getString("Solution")+"\n \n";
                */
                // Toast.makeText(getApplicationContext(),i+" "+jsonObject.getString("Questions") , Toast.LENGTH_LONG).show();
                reportList.add(jsonObject.getString("Questions"));

            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, reportList);
            lvQuestions.setAdapter(arrayAdapter);

            lvQuestions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        Intent intent = new Intent(ReportAnswerActivity.this , DetailAnswerActivity.class);
                        intent.putExtra("Question" , jsonArray.getJSONObject((int) id).getString("Questions"));
                        intent.putExtra("Answer" , jsonArray.getJSONObject((int) id).getString("Answer"));
                        intent.putExtra("Solution" , jsonArray.getJSONObject((int) id).getString("Solution"));
                        intent.putExtra("Level" , jsonArray.getJSONObject((int) id).getString("Level"));
                        intent.putExtra("QuesNum" , String.valueOf(id+1));

                        startActivity(intent);
                        //Toast.makeText(getApplicationContext(), jsonArray.getJSONObject((int) id).getString("Test_ID"), Toast.LENGTH_LONG).show();
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_report_answer, menu);
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

    private class ReportAnswerAsync extends AsyncTask<ApiConnector, Void, JSONArray>
    {
        ProgressDialog pd;

        @Override
        protected void onPreExecute()
        {
            pd = new ProgressDialog(context);
            pd.setTitle("Fetching result");
            pd.setMessage("Please Wait...");
            pd.setIndeterminate(false);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected JSONArray doInBackground(ApiConnector... params)
        {
            return params[0].getReport(email,test_ID);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray)
        {
            pd.dismiss();
            displayResult(jsonArray);
        }
    }
}
