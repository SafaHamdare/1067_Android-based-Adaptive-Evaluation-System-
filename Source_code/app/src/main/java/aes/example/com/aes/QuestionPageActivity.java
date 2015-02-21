package aes.example.com.aes;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;
import java.util.logging.LogRecord;


public class QuestionPageActivity extends ActionBarActivity {

    String level = "1";
    String IsCorrect = "InCorrect";
    String email = "";
    TextView tvQuestion;
    TextView tvTime;
    RadioGroup rgOption;
    Button btnNextQues;
    Button btnSubmitTest;
    Context context = this;

    String Correct_Answer = "";

    RadioButton Option_A;
    RadioButton Option_B;
    RadioButton Option_C;
    RadioButton Option_D;
    TextView tvTotal;

    Timer timer;
    TimerTask timerTask;

    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler= new Handler();


    RadioButton selectedOption;
    String selectedAnswer = "";
    String total = "0";

    int questionNum = 1;
    int maxNumQuestion = 30;
    int currentTime = 0;
    int maxAllotedTime = 1800;

    String test_ID = "-1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_question_page);

        tvQuestion = (TextView) findViewById(R.id.tvQuestion);
        rgOption = (RadioGroup) findViewById(R.id.rgOption);
        btnNextQues = (Button) findViewById(R.id.btnNextQues);
        btnSubmitTest = (Button) findViewById(R.id.btnSubmitTest);
        tvTotal = (TextView) findViewById(R.id.tvTotal);
        tvTime = (TextView) findViewById(R.id.tvTIme);

        Option_A = (RadioButton) findViewById(R.id.optionA);
        Option_B = (RadioButton) findViewById(R.id.optionB);
        Option_C = (RadioButton) findViewById(R.id.optionC);
        Option_D = (RadioButton) findViewById(R.id.optionD);

        email = getSharedPreferences("AES" , Context.MODE_PRIVATE).getString("email" , "");
        new FetchQuesAync().execute(new ApiConnector());

        btnNextQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = rgOption.getCheckedRadioButtonId();
                //selectedOption = (RadioButton) findViewById(selectedId);
                switch (selectedId)
                {
                    case R.id.optionA : selectedAnswer = "A";
                        break;

                    case R.id.optionB : selectedAnswer = "B";
                        break;

                    case R.id.optionC : selectedAnswer = "C";
                        break;

                    case R.id.optionD : selectedAnswer = "D";
                        break;
                }
                if(selectedAnswer.equals(Correct_Answer))
                {
                    //Toast.makeText(context, "Correct "+total , Toast.LENGTH_LONG).show();
                    IsCorrect = "Correct";
                }
                else
                {
                    //Toast.makeText(context, "InCorrect "+total, Toast.LENGTH_LONG).show();
                    IsCorrect = "InCorrect";
                }

                rgOption.clearCheck();

                new FetchQuesAync().execute(new ApiConnector());
                questionNum++;

            }
        });

        AlertDialog.Builder alBuilder = new AlertDialog.Builder(context);
        alBuilder.setMessage("Are you sure you want to submit the test?");
        alBuilder.setCancelable(true);
        alBuilder.setTitle("Submit test");
        alBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                int selectedId = rgOption.getCheckedRadioButtonId();
                //selectedOption = (RadioButton) findViewById(selectedId);
                switch (selectedId)
                {
                    case R.id.optionA : selectedAnswer = "A";
                        break;

                    case R.id.optionB : selectedAnswer = "B";
                        break;

                    case R.id.optionC : selectedAnswer = "C";
                        break;

                    case R.id.optionD : selectedAnswer = "D";
                        break;
                }
                if(selectedAnswer.equals(Correct_Answer))
                {
                    //Toast.makeText(context, "Correct "+total , Toast.LENGTH_LONG).show();
                    IsCorrect = "Correct";
                }
                else
                {
                    //Toast.makeText(context, "InCorrect "+total, Toast.LENGTH_LONG).show();
                    IsCorrect = "InCorrect";
                }

                new FetchQuesAync().execute(new ApiConnector());

                startActivity(new Intent(QuestionPageActivity.this , ReportActivity.class));
                finish();

            }
        });
        alBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        final AlertDialog alertDialog = alBuilder.create();


        btnSubmitTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
            }
        });
    }

    private void displayQues(JSONObject jsonObject)
    {
        if(questionNum<=maxNumQuestion) {
            try {
                Log.i("question", jsonObject.getString("Questions"));
                tvQuestion.setText("Q. No " + questionNum + ": " + jsonObject.getString("Questions")+" [Question level: "+jsonObject.getString("level")+"]");
                Option_A.setText(jsonObject.getString("Option_A"));
                Option_B.setText(jsonObject.getString("Option_B"));
                Option_C.setText(jsonObject.getString("Option_C"));
                Option_D.setText(jsonObject.getString("Option_D"));
                Log.i("question", jsonObject.getString("Correct_Answer"));
                Correct_Answer = jsonObject.getString("Correct_Answer");
                test_ID = jsonObject.getString("Test_ID");
                level = jsonObject.getString("level");
                total = jsonObject.getString("total");
                //Toast.makeText(context, jsonObject.getString("level") , Toast.LENGTH_SHORT).show();
                //Toast.makeText(context, "Loop num" + jsonObject.getString("loop") , Toast.LENGTH_SHORT).show();
                tvTotal.setText("Your total marks : " + total);

            } catch (Exception e) {
                Log.i("failed", e.getMessage() + "\n" + e.getLocalizedMessage());
            }
        }
        else
        {
            Toast.makeText(context,"Maximum number of questions reached..", Toast.LENGTH_LONG).show();
            startActivity(new Intent(context, ReportActivity.class));
            finish();
        }

    }

    public void startTimer()
    {
        timer = new Timer();
        initializeTimerTask();
        timer.scheduleAtFixedRate(timerTask , 0000, 1000);

    }

    public void initializeTimerTask()
    {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable(){
                    @Override
                    public void run() {
                        currentTime++;
                        //Toast.makeText(context, String.valueOf(currentTime), Toast.LENGTH_SHORT).show();
                        tvTime.setText(String.valueOf(currentTime/60)+" Minutes "+(currentTime%60)+" Seconds");
                        if(currentTime == maxAllotedTime)
                        {
                            Toast.makeText(context,"Time's UP.. Generation Report..", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(context, ReportActivity.class));
                            finish();
                        }
                    }
                });
            }
        };
    }

    public void stoptimertask(View v) {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //onResume we start our timer so it can start when the app comes from the background
        startTimer();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_question_page, menu);
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

    private class FetchQuesAync extends AsyncTask<ApiConnector , Void, JSONObject>
    {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd= new ProgressDialog(context);
            pd.setMessage("Please Wait.. Fetching Question from server... ");
            pd.setCancelable(false);
            pd.setIndeterminate(false);
            pd.show();
        }

        @Override
        protected JSONObject doInBackground(ApiConnector... params) {
            return params[0].fetchQuestion(level,email,IsCorrect, total , test_ID);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            displayQues(jsonObject);
            pd.dismiss();
        }
    }
}
