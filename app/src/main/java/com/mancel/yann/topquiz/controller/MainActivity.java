package com.mancel.yann.topquiz.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mancel.yann.topquiz.R;
import com.mancel.yann.topquiz.model.Ranking;
import com.mancel.yann.topquiz.model.User;

import java.util.Set;

/*
 Future updates:
 TODO : Add a menu bar in the MainActivity class to gather the ranking and the setting options.
 TODO : Add the possibility to change the questions theme in the MainActivity class.
 TODO : Add the questions in the generateQuestion method of the GameActivity class.
 TODO : Add a user indicator in the GameActivity class to indicate le question number in its label.
 */

/**
 * Created by Yann MANCEL on 05/02/2019.
 * Name of the project: TopQuiz
 * Name of the package: com.mancel.yann.topquiz.controller
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Coming from layout file
    private TextView mGreetingText;
    private EditText mNameInput;
    private Button mPlayButton;
    private Button mRankingButton;
    private Button mSettingButton;

    // Other
    private User mUser;
    private SharedPreferences mPreferences;
    private Ranking mRanking;
    private int mNumberQuestionToGameActivity;

    // Key name
    public static final int GAME_ACTIVITY_REQUEST_CODE = 100;
    public static final int RANKING_ACTIVITY_REQUEST_CODE = 200;
    public static final String PREF_KEY_FIRST_NAME = "PREF_KEY_FIRST_NAME";
    public static final String PREF_KEY_SCORE = "PREF_KEY_SCORE";
    public static final String PREF_KEY_SET = "PREF_KEY_SET";
    public static final String BUNDLE_EXTRA_NUMBER_OF_QUESTIONS = "BUNDLE_EXTRA_NUMBER_OF_QUESTIONS";
    public static final String BUNDLE_STATE_NUMBER_OF_QUESTIONS = "BUNDLE_STATE_NUMBER_OF_QUESTIONS";
    public static final String BUNDLE_STATE_FIRST_NAME = "BUNDLE_STATE_FIRST_NAME";

    //----------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Associates the layout file to this class
        setContentView(R.layout.activity_main);

        // Creates of link between the fields and the layout file
        this.mGreetingText = (TextView) findViewById(R.id.MainActivity_mGreetingText);
        this.mNameInput = (EditText) findViewById(R.id.MainActivity_mNameInput);
        this.mPlayButton = (Button) findViewById(R.id.MainActivity_mPlayButton);
        this.mRankingButton = (Button) findViewById(R.id.MainActivity_mRankingButton);
        this.mSettingButton = (Button) findViewById(R.id.MainActivity_mSettingButton);

        // Associates a tag to each button widget
        this.mPlayButton.setTag(0);
        this.mRankingButton.setTag(1);
        this.mSettingButton.setTag(2);

        // Use the same listener for the four buttons.
        // The tag value will be used to distinguish the button triggered
        this.mPlayButton.setOnClickListener(this);
        this.mRankingButton.setOnClickListener(this);
        this.mSettingButton.setOnClickListener(this);

        // The button is not enabled until the user gives its name
        this.mPlayButton.setEnabled(false);

        // The button is not visible the first time
        this.mRankingButton.setVisibility(View.INVISIBLE);

        // Initializes the User field thanks to the pseudo-constructor
        this.mUser = new User();

        // Storage file in private mode
        this.mPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        // Initializes the Ranking object with a top 5
        this.mRanking = new Ranking(5);

        // Initializes the maximal number of question for the GameActivity class to 5
        this.mNumberQuestionToGameActivity = 5;

        // Sets up the listeners of fields
        this.setUpListeners();

        // Verification if data exist and updates if needed
        this.RetrieveDataFromSaveFile();

        // It is to remove all data: Reset of data
        //this.removeAllSharedPreferences(this.mPreferences);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (GAME_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            // Fetches the score from the Intent object
            final int score = data.getIntExtra(GameActivity.BUNDLE_EXTRA_SCORE, 0);

            // Saves the last score
            this.saveResultWithSharedPreferences(this.mPreferences, PREF_KEY_SCORE, score);

            // Updates the TextView widget
            final String htmlSummary = getString(R.string.hey_text)        + ", <b>" + this.mUser.getFirstName() + "</b>! " +
                                       getString(R.string.last_score_text) + " <b>"  + score                     + "</b>!" +
                                       ", "                                + getString(R.string.better_this_time_text);

            // Checks the code version of the SDK
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                this.mGreetingText.setText(Html.fromHtml(htmlSummary, Html.FROM_HTML_MODE_LEGACY));
            }
            else {
                this.mGreetingText.setText(Html.fromHtml(htmlSummary));
            }

            // Adds the potential new best score
            this.mRanking.addItem(this.mUser.getFirstName(), score);

            // Saves the ranking
            final Set<String> set = this.mRanking.getUserMapToStringSet();
            this.saveResultWithSharedPreferences(this.mPreferences, PREF_KEY_SET, set);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Saves the user name state and the maximal number of questions state
        outState.putString(BUNDLE_STATE_FIRST_NAME, this.mUser.getFirstName());
        outState.putInt(BUNDLE_STATE_NUMBER_OF_QUESTIONS, this.mNumberQuestionToGameActivity);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // The system calls onRestoreInstanceState() only if there is a saved state to restore,
        // so you do not need to check whether the Bundle is null.

        // Restores the user name
        final String firstName = savedInstanceState.getString(BUNDLE_STATE_FIRST_NAME, null);
        this.mUser.setFirstName(firstName);

        // Restores the maximal number of questions
        final int numberOfQuestion = savedInstanceState.getInt(BUNDLE_STATE_NUMBER_OF_QUESTIONS, 5);
        this.mNumberQuestionToGameActivity = numberOfQuestion;
    }

    @Override
    public void onClick(View v) {
        // Retrieves the tag associated to the widget
        final int responseIndex = (int) v.getTag();

        // The tag allows to choice the selected button
        switch (responseIndex) {
            // mPlayButton field
            case 0: {
                // Makes the name into the User field
                final String firstName = this.mNameInput.getText().toString();
                this.mUser.setFirstName(firstName);

                // Saves the user name
                saveResultWithSharedPreferences(this.mPreferences, PREF_KEY_FIRST_NAME, firstName);

                // Calls the GameActivity class with its request code
                Intent gameActivityIntent = new Intent(MainActivity.this, GameActivity.class);

                // Adds the maximal number of questions presented in GameActivity class
                gameActivityIntent.putExtra(BUNDLE_EXTRA_NUMBER_OF_QUESTIONS, this.mNumberQuestionToGameActivity);

                // Starts the GameActivity class with its request code
                startActivityForResult(gameActivityIntent, GAME_ACTIVITY_REQUEST_CODE);

                break;
            }
            // mRankingButton field
            case 1: {
                // Calls the RankingActivity class with its request code
                final Intent rankingActivityIntent = new Intent(MainActivity.this, RankingActivity.class);
                startActivityForResult(rankingActivityIntent, RANKING_ACTIVITY_REQUEST_CODE);

                break;
            }
            // mSettingButton field
            case 2: {
                // Creates NumberDialog object to change the maximal number of questions
                final NumberDialog numberDialog = new NumberDialog(this, R.style.Theme_AppCompat_DayNight, 1, 50, this.mNumberQuestionToGameActivity);

                // Adds a listener to the Yes Button widget
                numberDialog.getYesButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Retrieves the new value from the NumberDialog object
                        mNumberQuestionToGameActivity = numberDialog.getSelectedNumber();

                        // Closes the NumberDialog object
                        numberDialog.dismiss();
                    }
                });

                // Adds a listener to the No Button widget
                numberDialog.getNoButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Closes the NumberDialog object
                        numberDialog.dismiss();
                    }
                });

                // Creates and shows the NumberDialog widget
                numberDialog.show();

                break;
            }
            default: { break; }
        }
    }

    /**
     * Locally saves the integer value thanks to the String key in a XML file initialized into preference
     *
     * @param preferences a SharedPreferences object that contains the file name and the access mode
     * @param key a String object that contains the key
     * @param value an integer that contains the value
     */
    private void saveResultWithSharedPreferences(@NonNull final SharedPreferences preferences, final String key, final int value) {
        // Creates an Editor object to modify the stored data
        SharedPreferences.Editor editor = preferences.edit();

        // Stores an integer into the Editor object (key/value system)
        editor.putInt(key, value);

        // Throws the backup -> you can also use editor.commit(); (to write the data to disk synchronously)
        editor.apply();
    }

    /**
     * Locally saves the String value thanks to the String key in a XML file initialized into preference
     *
     * @param preferences a SharedPreferences object that contains the file name and the access mode
     * @param key a String object that contains the key
     * @param value a String object that contains the value
     */
    private void saveResultWithSharedPreferences(@NonNull final SharedPreferences preferences, final String key, final String value) {
        // Creates an Editor object to modify the stored data
        SharedPreferences.Editor editor = preferences.edit();

        // Stores a String object into the Editor object (key/value system)
        editor.putString(key, value);

        // Throws the backup -> you can also use editor.commit(); (to write the data to disk synchronously)
        editor.apply();
    }

    /**
     * Locally saves the Set<String> value thanks to the String key in a XML file initialized into preference
     *
     * @param preferences a SharedPreferences object that contains the file name and the access mode
     * @param key a String object that contains the key
     * @param value a Set<String> object that contains the values
     */
    private void saveResultWithSharedPreferences(@NonNull final SharedPreferences preferences, final String key, final Set<String> value) {
        // Creates an Editor object to modify the stored data
        SharedPreferences.Editor editor = preferences.edit();

        // Stores a Set<String> object into the Editor object (key/value system)
        editor.putStringSet(key, value);

        // Throws the backup -> you can also use editor.commit(); (to write the data to disk synchronously)
        editor.apply();
    }

    /**
     * Removes all data contained into a XML file initialized thanks to preference
     *
     * @param preferences a SharedPreferences object that contains the file name and the access mode
     */
    private void removeAllSharedPreferences(@NonNull final SharedPreferences preferences) {
        // Creates an Editor object to modify the stored data
        SharedPreferences.Editor editor = preferences.edit();

        // Removes all data contained into a XML file initialized thanks to preference
        editor.remove(PREF_KEY_FIRST_NAME);
        editor.remove(PREF_KEY_SCORE);
        editor.remove(PREF_KEY_SET);

        // Throws the backup -> you can also use editor.commit(); (to write the data to disk synchronously)
        editor.apply();
    }

    /**
     * Fetches the local data from the XML file
     * <ul>
     * <li>a String object that contains the user name</li>
     * <li>an integer that contains the last score</li>
     * <li>a Set<String> object that contains the user name/score couples</li>
     * </ul>
     */
    private void RetrieveDataFromSaveFile() {
        // Fetches the local data from the XML file
        final String firstName = this.mPreferences.getString(PREF_KEY_FIRST_NAME, null);
        final int score = this.mPreferences.getInt(PREF_KEY_SCORE, 0);
        final Set<String> set = this.mPreferences.getStringSet(PREF_KEY_SET, null);

        if (firstName != null) {
            // Updates the TextView widget
            final String htmlSummary = getString(R.string.welcome_back_text) + ", <b>" + firstName + "</b>! " +
                                       getString(R.string.last_score_text)   + " <b>"  + score     + "</b>!" +
                                       ", "                                  + getString(R.string.better_this_time_text);

            // Checks the code version of the SDK
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                this.mGreetingText.setText(Html.fromHtml(htmlSummary, Html.FROM_HTML_MODE_LEGACY));
            }
            else {
                this.mGreetingText.setText(Html.fromHtml(htmlSummary));
            }

            // Updates the EditText widget
            this.mNameInput.setText("");
            this.mNameInput.append(firstName);

            // Retrieves the Set<String> object
            this.mRanking.setUserMap(set);

            // The button is now visible
            this.mRankingButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Sets up the listeners of fields
     */
    private void setUpListeners() {
        // Listener to the EditText object
        this.mNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // the button is enabled if the String length is different to zero
                final boolean enabled = (s.toString().length() != 0);
                mPlayButton.setEnabled(enabled);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}