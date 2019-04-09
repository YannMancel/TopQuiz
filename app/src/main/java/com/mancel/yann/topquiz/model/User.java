package com.mancel.yann.topquiz.model;

/**
 * Created by Yann MANCEL on 05/02/2019.
 * Name of the project: TopQuiz
 * Name of the package: com.mancel.yann.topquiz.model
 */
public class User {

    private String mFirstName;

    //----------------------------------------------------------------------------------------------

    /**
     * Returns the mFirstName field
     * @return the field that is a String object
     */
    public String getFirstName() {
        return this.mFirstName;
    }

    /**
     * Modifies the mFirstName field
     * @param firstName a String object that contains the new value of this member
     */
    public void setFirstName(String firstName) {
        this.mFirstName = firstName;
    }
}