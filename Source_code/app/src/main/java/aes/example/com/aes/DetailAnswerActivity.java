package aes.example.com.aes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class DetailAnswerActivity extends ActionBarActivity {

    TextView tvQuestion;
    TextView tvAnswer;
    TextView tvSolution;
    TextView tvLevel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_answer);

        tvQuestion = (TextView) findViewById(R.id.tvQuestion);
        tvAnswer = (TextView) findViewById(R.id.tvAnswer);
        tvSolution = (TextView) findViewById(R.id.tvSolution);
        tvLevel = (TextView) findViewById(R.id.tvLevel);

        Bundle extras = getIntent().getExtras();
        tvQuestion.setText("Question No "+extras.getString("QuesNum")+" : " +extras.getString("Question"));
        tvAnswer.setText("Answer : " + extras.getString("Answer"));
        tvSolution.setText("Solution : "+extras.getString("Solution"));
        tvLevel.setText("Level : " + extras.getString("Level"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_answer, menu);
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
            Intent intent = new Intent(getApplicationContext() , LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
