package com.mancel.yann.topquiz.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.mancel.yann.topquiz.R;
import com.mancel.yann.topquiz.model.Ranking;

import java.util.List;
import java.util.Set;

/**
 * Created by Yann MANCEL on 05/02/2019.
 * Name of the project: TopQuiz
 * Name of the package: com.mancel.yann.topquiz.controller
 */
public class RankingActivity extends AppCompatActivity {

    // Coming from layout file
    private ListView mListView;
    private Button mByScoreButton;
    private Button mByAlphabeticalButton;

    // Other
    private Ranking mRanking;
    private SharedPreferences mPreferences;

    //----------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Associates the layout file to this class
        setContentView(R.layout.activity_ranking);

        // Creates of link between the fields and the layout file
        this.mListView = (ListView) findViewById(R.id.RankingActivity_mListView);
        this.mByScoreButton = (Button) findViewById(R.id.RankingActivity_mByScoreButton);
        this.mByAlphabeticalButton = (Button) findViewById(R.id.RankingActivity_mByAlphabeticalButton);

        // Initializes the Ranking object with a top 5
        this.mRanking = new Ranking(5);

        // Storage file in private mode
        this.mPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        // Verification if data exist and updates if needed
        this.RetrieveDataFromSaveFile();

        // Sets up the listeners of fields
        this.setUpListeners();

        // Updates the view
        final List<String> list = this.mRanking.scoreRanking();
        this.displayChoice(list);
    }

    /**
     * Fetches the local data from the XML file
     * <ul>
     * <li>a Set<String> object that contains the user name/score couples</li>
     * </ul>
     */
    private void RetrieveDataFromSaveFile() {
        // Fetches the local data from the XML file
        final Set<String> set = this.mPreferences.getStringSet(MainActivity.PREF_KEY_SET, null);

        if (set != null) {
            // Retrieves the Set<String> object
            this.mRanking.setUserMap(set);
        }
    }

    /**
     * Updates the ListView widget thanks to the list given in argument
     *
     * @param list a List<String> object that contains what shall be to display
     */
    public void displayChoice(List<String> list) {
        // Creates an ArrayAdapter object that contains the String list given in argument
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(RankingActivity.this, android.R.layout.simple_list_item_1, list);

        // Creates an ItemAdapter object that contains the String list given in argument
        ItemAdapter adapter = new ItemAdapter(RankingActivity.this, list);

        // Updates the ListView widget thanks to the ArrayAdapter object
        this.mListView.setAdapter(adapter);
    }

    /**
     * Sets up the listeners of fields
     */
    private void setUpListeners() {
        // Listener to the Button objects
        this.mByScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Updates the ListView widget: Decreasing order by score
                displayChoice(mRanking.scoreRanking());
            }
        });

        this.mByAlphabeticalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Updates the ListView widget: Natural order by alphabetical
                displayChoice(mRanking.alphabeticalRanking());
            }
        });
    }
}