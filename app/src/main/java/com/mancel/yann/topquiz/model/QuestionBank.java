package com.mancel.yann.topquiz.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Yann MANCEL on 05/02/2019.
 * Name of the project: TopQuiz
 * Name of the package: com.mancel.yann.topquiz.model
 */
public class QuestionBank implements Parcelable {

    private List<Question> mQuestionList;
    private int mNextQuestionIndex;

    //----------------------------------------------------------------------------------------------

    /**
     * Initializes a QuestionBank object thanks to 1 parameter
     *
     * @param questionList a List<Question> object that contains a question list
     */
    public QuestionBank(final List<Question> questionList) {
        // Initializes the member with the parameter
        this.mQuestionList = questionList;

        // Randomly permutes the specified list (static method)
        Collections.shuffle(this.mQuestionList);

        // Initializes the index of the question
        this.mNextQuestionIndex = 0;
    }

    /**
     * Initializes a QuestionBank object thanks to 1 parameter
     *
     * @param in a Parcel object that contains the data to initialize the QuestionBank object
     */
    protected QuestionBank(Parcel in) {
        /*
         Saving order:
            - Index of the next question
            - Size of the question list
            - For each question of the list:
                + Question
                + First answer
                + Second answer
                + Third answer
                + Fourth answer
                + Good answer index
         */

        // Retrieves the index of the next question
        this.mNextQuestionIndex = in.readInt(); //dest.writeInt(this.mNextQuestionIndex);

        // Retrieves the size of the question list
        final int listSize = in.readInt(); //dest.writeInt(this.mQuestionList.size());

        // Retrieves the data of the list
        List<Question> questionList = new ArrayList<>();
        Question question;
        String title;
        List<String> answerList;
        String answer;
        int goodIndex = 0;

        for (int i = 0 ; i<listSize ; i++) {
            // Question
            title = in.readString();

            // Answers
            answerList = new ArrayList<>();
            for (int j = 0 ; j<4 ; j++) {
                answer = in.readString();
                answerList.add(answer);
            }

            // Good answer index
            goodIndex = in.readInt();

            // Creates a new Question object
            question = new Question(title, answerList, goodIndex);

            // Adds the question to the question list
            questionList.add(question);
        }
    }

    //----------------------------------------------------------------------------------------------

    public static final Creator<QuestionBank> CREATOR = new Creator<QuestionBank>() {
        @Override
        public QuestionBank createFromParcel(Parcel in) {
            return new QuestionBank(in);
        }

        @Override
        public QuestionBank[] newArray(int size) {
            return new QuestionBank[size];
        }
    };

    //----------------------------------------------------------------------------------------------

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        /*
         Saving order:
            - Index of the next question
            - Size of the question list
            - For each question of the list:
                + Question
                + First answer
                + Second answer
                + Third answer
                + Fourth answer
                + Good answer index
         */

        // Index of the next question
        dest.writeInt(this.mNextQuestionIndex);

        // Size of the question list
        dest.writeInt(this.mQuestionList.size());

        // Loop on the question list
        List<String> list;

        for (Question question : this.mQuestionList) {
            // Question
            dest.writeString(question.getQuestion());

            // Answers
            list = question.getChoiceList();
            dest.writeString(list.get(0));
            dest.writeString(list.get(1));
            dest.writeString(list.get(2));
            dest.writeString(list.get(3));

            // Good answer index
            dest.writeInt(question.getGoodAnswerIndex());
        }
    }

    /**
     * Returns the next question index
     * @return an integer that contains the next question index
     */
    public int getNextQuestionIndex() {
        return this.mNextQuestionIndex;
    }

    /**
     * Modifies the next question index
     *
     * @param nextQuestionIndex an integer that contains the new value
     */
    public void setNextQuestionIndex(final int nextQuestionIndex) {
        this.mNextQuestionIndex = nextQuestionIndex;
    }

    /**
     * Returns a question located to the list
     *
     * @return a Question object
     */
    public Question getQuestion() {
        // The index is out of boundaries
        if (this.mNextQuestionIndex == this.mQuestionList.size()) {
            this.mNextQuestionIndex = 0;
        }

        // Returns the question and the mNextQuestionIndex field is increased of 1
        return this.mQuestionList.get(this.mNextQuestionIndex++);
    }
}