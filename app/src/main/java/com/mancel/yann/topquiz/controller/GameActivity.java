package com.mancel.yann.topquiz.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mancel.yann.topquiz.R;
import com.mancel.yann.topquiz.model.Question;
import com.mancel.yann.topquiz.model.QuestionBank;
import com.mancel.yann.topquiz.model.TopQuizException;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Yann MANCEL on 05/02/2019.
 * Name of the project: TopQuiz
 * Name of the package: com.mancel.yann.topquiz.controller
 */
public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    // Coming from layout file
    private TextView mQuestionText;
    private Button mAnswerButton1;
    private Button mAnswerButton2;
    private Button mAnswerButton3;
    private Button mAnswerButton4;

    // Other
    private QuestionBank mQuestionBank;
    private int mGoodAnswerFromCurrentQuestion;
    private int mNumberOfQuestions;
    private int mScore;
    private boolean mEnableTouchEvents;

    // Key name
    public static final String BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE";
    public static final String BUNDLE_STATE_SCORE = "BUNDLE_STATE_SCORE";
    public static final String BUNDLE_STATE_NUMBER_OF_QUESTION = "BUNDLE_STATE_NUMBER_OF_QUESTION";
    public static final String BUNDLE_STATE_QUESTION_BANK = "BUNDLE_STATE_QUESTION_BANK";

    //----------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Associates the layout file to this class
        setContentView(R.layout.activity_game);

        // Creates of link between the fields and the layout file
        this.mQuestionText = (TextView) findViewById(R.id.GameActivity_mQuestionText);
        this.mAnswerButton1 = (Button) findViewById(R.id.GameActivity_mAnswerButton1);
        this.mAnswerButton2 = (Button) findViewById(R.id.GameActivity_mAnswerButton2);
        this.mAnswerButton3 = (Button) findViewById(R.id.GameActivity_mAnswerButton3);
        this.mAnswerButton4 = (Button) findViewById(R.id.GameActivity_mAnswerButton4);

        // Associates a tag to each button widget
        this.mAnswerButton1.setTag(0);
        this.mAnswerButton2.setTag(1);
        this.mAnswerButton3.setTag(2);
        this.mAnswerButton4.setTag(3);

        // Use the same listener for the four buttons.
        // The tag value will be used to distinguish the button triggered
        this.mAnswerButton1.setOnClickListener(this);
        this.mAnswerButton2.setOnClickListener(this);
        this.mAnswerButton3.setOnClickListener(this);
        this.mAnswerButton4.setOnClickListener(this);

        // Initializes the question bank
        this.mQuestionBank = this.generateQuestion();

        // Displays the new question
        this.displayQuestion(this.mQuestionBank.getQuestion());

        // Initializes the maximal number of question
        Intent intent = getIntent();

        if (intent != null) {
            // Retrieves the maximal number of questions from MainActivity class
            final int numberOfQuestion = intent.getIntExtra(MainActivity.BUNDLE_EXTRA_NUMBER_OF_QUESTIONS, 4);

            // Initialized the field with the previous integer
            this.mNumberOfQuestions = numberOfQuestion;
        }
        else {
            this.mNumberOfQuestions = 4;
        }

        // Initializes the score to zero
        this.mScore = 0;

        // Enables the touch events
        this.mEnableTouchEvents = true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Saves the score state, the number of question state and the question bank state
        outState.putInt(BUNDLE_STATE_SCORE, this.mScore);
        outState.putInt(BUNDLE_STATE_NUMBER_OF_QUESTION, this.mNumberOfQuestions);
        outState.putParcelable(BUNDLE_STATE_QUESTION_BANK, this.mQuestionBank);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // The system calls onRestoreInstanceState() only if there is a saved state to restore,
        // so you do not need to check whether the Bundle is null.

        // Restores the score
        final int score = savedInstanceState.getInt(BUNDLE_STATE_SCORE, 0);
        this.mScore = score;

        // Restores the number of question state
        final int numberOfQuestions = savedInstanceState.getInt(BUNDLE_STATE_NUMBER_OF_QUESTION, 4);
        this.mNumberOfQuestions = numberOfQuestions;

        // Restores the question bank state
        final QuestionBank questionBank = savedInstanceState.getParcelable(BUNDLE_STATE_QUESTION_BANK);
        this.mQuestionBank = questionBank;

        // Modifies the question index (see getQuestion method)
        final int lastQuestionIndex = this.mQuestionBank.getNextQuestionIndex() - 1;
        this.mQuestionBank.setNextQuestionIndex(lastQuestionIndex);

        // Displays the last question
        this.displayQuestion(this.mQuestionBank.getQuestion());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return this.mEnableTouchEvents && super.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View v) {
        // Disables the touch events
        this.mEnableTouchEvents = false;

        // Retrieves the tag associated to the widget
        final int responseIndex = (int) v.getTag();

        // Checks the answer
        if (responseIndex == this.mGoodAnswerFromCurrentQuestion) {
            // Right answer
            Toast.makeText(this, getString(R.string.good_answer_text), Toast.LENGTH_SHORT).show();
            this.mScore++;
        }
        else {
            // Wrong answer
            Toast.makeText(this, getString(R.string.wrong_answer_text), Toast.LENGTH_SHORT).show();
        }

        // Freezes the system during 2000 ms
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Enables the touch events
                mEnableTouchEvents = true;

                // Checks if it is possible to display another question
                if (--mNumberOfQuestions == 0) {
                    // Ends the GameActivity
                    endGame();
                }
                else {
                    // Displays the new question
                    displayQuestion(mQuestionBank.getQuestion());
                }
            }

        }, 2000); // LENGTH_SHORT is usually 2 second long
    }

    /**
     * Generates the QuestionBank object tanks to Question objects
     *
     * @return a QuestionBank object that contains all questions
     */
    private QuestionBank generateQuestion() {

        try {
            Question question1 = new Question("Who is the creator of Android?",
                                                Arrays.asList("Andy Rubin",
                                                            "Steve Wozniak",
                                                            "Jake Wharton",
                                                            "Paul Smith"),
                                                0);

            Question question2 = new Question("When did the first man land on the moon?",
                                                Arrays.asList("1958",
                                                            "1962",
                                                            "1967",
                                                            "1969"),
                                                3);

            Question question3 = new Question("What is the house number of The Simpsons?",
                                                Arrays.asList("42",
                                                            "101",
                                                            "666",
                                                            "742"),
                                                3);

            return new QuestionBank(Arrays.asList(question1,
                                                question2,
                                                question3));
        }
        catch (TopQuizException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * Displays the new question thanks to the Question object in argument
     * @param question a Question object that contains the question, the answers and the number of the good answer
     */
    private void displayQuestion(final Question question) {
        // Updates the TextView widget
        this.mQuestionText.setText(question.getQuestion());

        // Updates the Button widgets
        final List<String> list = question.getChoiceList();
        this.mAnswerButton1.setText(list.get(0));
        this.mAnswerButton2.setText(list.get(1));
        this.mAnswerButton3.setText(list.get(2));
        this.mAnswerButton4.setText(list.get(3));

        // Retrieves the current question
        this.mGoodAnswerFromCurrentQuestion = question.getGoodAnswerIndex();
    }

    /**
     * Allow to display the user's score and to finish this activity
     */
    private void endGame() {
        // Creates Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Modifies the title
        builder.setTitle(getString(R.string.well_done_text));

        // Modifies the message
        builder.setMessage(getString(R.string.score_text) + " " + mScore);

        // Modifies the Positive button widget
        builder.setPositiveButton(getString(R.string.ok_text), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Creates a default Intent object
                Intent mainActivityIntent = new Intent();

                // Adds the score with its key word into the Intent object
                mainActivityIntent.putExtra(BUNDLE_EXTRA_SCORE, mScore);

                // Good execution
                setResult(RESULT_OK, mainActivityIntent);

                // Destroys the GameActivity
                finish();
            }
        });

        // Creates and shows the AlertDialog widget
        builder.create().show();
    }
}