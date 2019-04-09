package com.mancel.yann.topquiz.model;

import java.util.List;

/**
 * Created by Yann MANCEL on 05/02/2019.
 * Name of the project: TopQuiz
 * Name of the package: com.mancel.yann.topquiz.model
 */
public class Question {

    private String mQuestion;
    private List<String> mChoiceList;
    private int mGoodAnswerIndex;

    //----------------------------------------------------------------------------------------------

    /**
     * Initializes a Question object thanks to 3 parameters
     *
     * @param question a String object that contains the question
     * @param choiceList a List<String> object that contains the list of answers
     * @param goodAnswerIndex a integer that contains the good answer of the question
     *
     * @throws TopQuizException if the value of parameters is not valid or possible
     */
    public Question(String question, List<String> choiceList, int goodAnswerIndex) throws TopQuizException {
        this.mQuestion = question;
        this.mChoiceList = choiceList;
        this.mGoodAnswerIndex = goodAnswerIndex;

        // Verifications of parameters
        if (this.mQuestion.length() == 0) throw new TopQuizException("The question is empty.");
        if (this.mChoiceList.size() == 0) throw new TopQuizException("The size of the choice list is equal to zero.");
        if (this.mGoodAnswerIndex < 0 ||
            this.mGoodAnswerIndex >= this.mChoiceList.size() ) throw new TopQuizException("The good answer index is out of boundaries.");
    }

    //----------------------------------------------------------------------------------------------

    /**
     * Returns the mQuestion field
     *
     * @return the field that is a String object
     */
    public String getQuestion() {
        return this.mQuestion;
    }

    /**
     * Returns the mChoiceList field
     *
     * @return the field that is a List<String> object
     */
    public List<String> getChoiceList() {
        return this.mChoiceList;
    }

    /**
     * Returns the mGoodAnswerIndex field
     *
     * @return the field that is an integer
     */
    public int getGoodAnswerIndex() {
        return this.mGoodAnswerIndex;
    }
}