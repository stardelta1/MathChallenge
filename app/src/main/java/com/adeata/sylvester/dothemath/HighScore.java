package com.adeata.sylvester.dothemath;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class HighScore extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_score);

        TextView scoreView = (TextView)findViewById(R.id.high_scores_list);
        //retrieve the Shared Preferences
        SharedPreferences scorePrefs = getSharedPreferences(PlayGame.GAME_PREFS, 0);
        //get scores
        String[] savedScores = scorePrefs.getString("highScores", "").split("\\|");
        //build string
        StringBuilder scoreBuild = new StringBuilder("");
        for(String score : savedScores){
            scoreBuild.append(score+"\n");
        }
        //display scores
        scoreView.setText(scoreBuild.toString());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_high_score, menu);
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
}
