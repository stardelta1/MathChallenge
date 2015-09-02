package com.adeata.sylvester.dothemath;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class PlayGame extends ActionBarActivity implements View.OnClickListener{
    //gameplay elements
    // First, integers to represent the level, operands, operator and answer
    private int level = 0, answer = 0, operator = 0, operand1 = 0, operand2 = 0;
    //operator constants
    //Next define some constants for the four operators,
    // which will streamline the gameplay processing
    private final int ADD_OPERATOR = 0, SUBTRACT_OPERATOR = 1, MULTIPLY_OPERATOR = 2,
            DIVIDE_OPERATOR = 3;
    //operator text
    private String[] operators = {"+", "-", "x", "/"};
    //min and max for each level and operator
    //The range of operators is going to depend on the level the user chose
    //We will use a random number generator to choose the operands, with a minimum and maximum in each case
    //For example, the minimum operand for addition at medium difficulty is 11,
    // while the maximum operand for subtraction at hard difficulty is 30
    private int[][] levelMin = {
            //E  M   H
            {1, 11, 21},//add
            {1, 5, 10},//subt
            {2, 5, 10},//multi
            {2, 3, 5}};//divide
    private int[][] levelMax = {
            {10, 25, 50},
            {10, 20, 30},
            {5, 10, 15},
            {10, 50, 100}};
    //random number generator
    private Random random;
    //ui elements
    private TextView question, answerTxt, scoreTxt;
    private ImageView response;
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0,
            enterBtn, clearBtn;

    //shared preferences
    private SharedPreferences gamePrefs;
    public static final String GAME_PREFS = "ArithmeticFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_game);

        //initiate shared prefs
        gamePrefs = getSharedPreferences(GAME_PREFS, 0);

        //text and image views
        question = (TextView)findViewById(R.id.question);
        answerTxt = (TextView)findViewById(R.id.answer);
        response = (ImageView)findViewById(R.id.response);
        scoreTxt = (TextView)findViewById(R.id.score);

        //hide tick cross initially
        response.setVisibility(View.INVISIBLE);

        //number, enter and clear buttons
        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        btn3 = (Button)findViewById(R.id.btn3);
        btn4 = (Button)findViewById(R.id.btn4);
        btn5 = (Button)findViewById(R.id.btn5);
        btn6 = (Button)findViewById(R.id.btn6);
        btn7 = (Button)findViewById(R.id.btn7);
        btn8 = (Button)findViewById(R.id.btn8);
        btn9 = (Button)findViewById(R.id.btn9);
        btn0 = (Button)findViewById(R.id.btn0);
        enterBtn = (Button)findViewById(R.id.enter);
        clearBtn = (Button)findViewById(R.id.clear);

        //listen for clicks
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn0.setOnClickListener(this);
        enterBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);

        //Now retrieve the level number we passed from the main Activity
        if(savedInstanceState!=null){
            //saved instance state data
            level=savedInstanceState.getInt("level");
            int exScore = savedInstanceState.getInt("score");
            scoreTxt.setText("Score: "+exScore);
        }
        else{
            //get passed level number
            Bundle extras = getIntent().getExtras();
            if(extras !=null)
            {
                int passedLevel = extras.getInt("level", -1);
                if(passedLevel>=0) level = passedLevel;
            }
        }

        //initialize random
        random = new Random();
        //play
        chooseQuestion();
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.enter){
            //enter button
            //get answer
            String answerContent = answerTxt.getText().toString();
            //check we have an answer
            if(!answerContent.endsWith("?")){
                //get number
                int enteredAnswer = Integer.parseInt(answerContent.substring(2));
                //get score
                int exScore = getScore();
                //check answer
                if(enteredAnswer==answer){
                    //correct
                    scoreTxt.setText("Score: "+(exScore+1));
                    response.setImageResource(R.drawable.tick);
                    response.setVisibility(View.VISIBLE);
                }
                else{
                    //set high score
                    setHighScore();
                    //incorrect
                    scoreTxt.setText("Score: 0");
                    response.setImageResource(R.drawable.cross);
                    response.setVisibility(View.VISIBLE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Incorrect!");
                    builder.setMessage(operand1 + " " + operators[operator] + " " + operand2 + " = " + answer + "\n\n You entered " + enteredAnswer);
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           finish();
                        }
                    });
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                chooseQuestion();
            }
        }
        else if(view.getId()==R.id.clear){
            //clear button
            answerTxt.setText("= ?");
        }
        else {
            //number button
            //Now let's handle the number buttons. In the else block,
            // start by setting the tick/cross image to be hidden,
            // as we don't want feedback displayed while the user is entering an answer
            response.setVisibility(View.INVISIBLE);
            //get number from tag
            int enteredNum = Integer.parseInt(view.getTag().toString());
            //check if the user is entering the first
            // digit of the answer, or a subsequent digit.
            if(answerTxt.getText().toString().endsWith("?"))
                answerTxt.setText("= "+enteredNum);
            else
                answerTxt.append(""+enteredNum);
        }
    }

    //method retrieves score
    private int getScore(){
        String scoreStr = scoreTxt.getText().toString();
        return Integer.parseInt(scoreStr.substring(scoreStr.lastIndexOf(" ")+1));
    }

    //method generates questions
    private void chooseQuestion(){
        //reset answer text
        //This is necessary because the answer Text View is going to
        // display the user-entered answer, so it must be reset each time a new question
        answerTxt.setText("= ?");
        //choose operator
        operator = random.nextInt(operators.length);
        //choose operands
        operand1 = getOperand();
        operand2 = getOperand();

        //checks for operators
        if(operator==SUBTRACT_OPERATOR){
            //no negative answers
            while(operand2>operand1){
                operand1 = getOperand();
                operand2 = getOperand();
            }
        }
        else if(operator==DIVIDE_OPERATOR){
            //whole numbers only
            while((((double)operand1/(double)operand2)%1 > 0)
                    || (operand1==operand2)){
                operand1 = getOperand();
                operand2 = getOperand();
            }
        }

        //calculate answer
        switch(operator){
            case ADD_OPERATOR:
                answer = operand1+operand2;
                break;
            case SUBTRACT_OPERATOR:
                answer = operand1-operand2;
                break;
            case MULTIPLY_OPERATOR:
                answer = operand1*operand2;
                break;
            case DIVIDE_OPERATOR:
                answer = operand1/operand2;
                break;
            default:
                break;
        }

        //show question
        question.setText(operand1+" "+operators[operator]+" "+operand2);
    }

    //method generates operands
    private int getOperand(){
        return random.nextInt(levelMax[operator][level] - levelMin[operator][level] + 1)
                + levelMin[operator][level];
    }

    //set high score
    private void setHighScore(){
        //n this method we will deal with the details of
        // checking whether or not the current score makes the top ten
        int exScore = getScore();
        if(exScore>0){
            //we have a valid score
            SharedPreferences.Editor scoreEdit = gamePrefs.edit();
            //For each high score,
            // we will include the score and the date
            DateFormat dateForm = new SimpleDateFormat("dd MMMM yyyy");
            String dateOutput = dateForm.format(new Date());
            //get existing scores
            String scores = gamePrefs.getString("highScores", "");
            //check for scores
            if(scores.length()>0){
                //we have existing scores
                List<Score> scoreStrings = new ArrayList<Score>();
                //split scores
                String[] exScores = scores.split("\\|");
                //add score object for each
                for(String eSc : exScores){
                    String[] parts = eSc.split(" - ");
                    scoreStrings.add(new Score(parts[0], Integer.parseInt(parts[1])));
                }
                //new score
                Score newScore = new Score(dateOutput, exScore);
                scoreStrings.add(newScore);
                //sort
                Collections.sort(scoreStrings);
                //get top ten
                StringBuilder scoreBuild = new StringBuilder("");
                for(int s=0; s<scoreStrings.size(); s++){
                    if(s>=10) break;
                    if(s>0) scoreBuild.append("|");
                    scoreBuild.append(scoreStrings.get(s).getScoreText());
                }
                //write to prefs
                scoreEdit.putString("highScores", scoreBuild.toString());
                scoreEdit.commit();

            }
            else{
                //no existing scores
                scoreEdit.putString("highScores", ""+dateOutput+" - "+exScore);
                scoreEdit.commit();
            }
        }
    }

    //set high score if activity destroyed
    protected void onDestroy(){
        setHighScore();
        super.onDestroy();
    }

    //save instance state
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        //save score and level
        int exScore = getScore();
        savedInstanceState.putInt("score", exScore);
        savedInstanceState.putInt("level", level);
        //superclass method
        super.onSaveInstanceState(savedInstanceState);
    }
}
