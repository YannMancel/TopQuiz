package com.mancel.yann.topquiz.controller;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.NumberPicker;

import com.mancel.yann.topquiz.R;
import com.mancel.yann.topquiz.model.TopQuizException;

/**
 * Created by Yann MANCEL on 04/04/2019.
 * Name of the project: TopQuiz
 * Name of the package: com.mancel.yann.topquiz.controller
 */
public class NumberDialog extends Dialog {

    // Coming from layout file
    private NumberPicker mNumberPicker;
    private Button mYesButton;
    private Button mNoButton;

    //----------------------------------------------------------------------------------------------

    /**
     * Initializes a NumberDialog object with 2 arguments.
     *
     * @param context the Context object in which the dialog should run
     * @param themeResId a style resource describing the theme to use for the window
     *
     * @throws TopQuizException if the current value of parameters is not valid or possible
     */
    public NumberDialog(Context context, int themeResId, final int minInt, final int maxInt, final int CurrentInt)  throws TopQuizException {
        super(context, themeResId);

        // Associates the layout file to this class
        setContentView(R.layout.dialog_number);

        // Creates of link between the fields and the layout file
        this.mNumberPicker = (NumberPicker) findViewById(R.id.NumberDialog_mNumberPicker);
        this.mYesButton = (Button) findViewById(R.id.NumberDialog_mYesButton);
        this.mNoButton = (Button) findViewById(R.id.NumberDialog_mNoButton);

        // Initializes the characteristics of the NumberPicker filed
        this.mNumberPicker.setMinValue(minInt);
        this.mNumberPicker.setMaxValue(maxInt);
        this.mNumberPicker.setValue(CurrentInt);

        // Verifications of parameters
        if (CurrentInt < minInt || CurrentInt > maxInt) throw new TopQuizException("The current value is out of boundaries.");
    }

    //----------------------------------------------------------------------------------------------

    /**
     * Returns the selected number
     *
     * @return an integer that contains the value of the selected number
     */
    public int getSelectedNumber() {
        return this.mNumberPicker.getValue();
    }

    /**
     * Returns the Yes Button field
     *
     * @return a Button object that corresponds to the Yes Button widget
     */
    public Button getYesButton() {
        return this.mYesButton;
    }

    /**
     * Returns the No Button field
     *
     * @return a Button object that corresponds to the No Button widget
     */
    public Button getNoButton() {
        return this.mNoButton;
    }
}
