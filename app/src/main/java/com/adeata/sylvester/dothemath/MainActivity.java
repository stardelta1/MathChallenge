package com.adeata.sylvester.dothemath;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private Button playBtn, helpBtn, highBtn;
    private String[] levelNames = {"Easy", "Medium", "Hard"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playBtn = (Button)findViewById(R.id.play_btn);
        helpBtn = (Button)findViewById(R.id.help_btn);
        highBtn = (Button)findViewById(R.id.high_btn);

        playBtn.setOnClickListener(this);
        helpBtn.setOnClickListener(this);
        highBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.play_btn){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose a level")
                   .setSingleChoiceItems(levelNames, 0, new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           dialog.dismiss();
                            //Start gameplay
                           startPlay(which);
                       }
                   });
            AlertDialog dialog = builder.create();
            dialog.show();

        }else if(v.getId() == R.id.help_btn){
            Intent helpIntent = new Intent(this, HowToPlay.class);
            this.startActivity(helpIntent);

        }else if(v.getId() == R.id.high_btn){
            Intent highIntent = new Intent(this, HighScore.class);
            this.startActivity(highIntent);

        }
    }

    private void startPlay(int choosenlevel) {
        Intent playIntent = new Intent(this, PlayGame.class);
        playIntent.putExtra("level", choosenlevel);
        startActivity(playIntent);
    }


}
