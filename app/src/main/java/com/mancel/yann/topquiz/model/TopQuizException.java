package com.mancel.yann.topquiz.model;

/**
 * Created by Yann MANCEL on 05/02/2019.
 * Name of the project: TopQuiz
 * Name of the package: com.mancel.yann.topquiz.model
 */
public class TopQuizException extends RuntimeException {

    /**
     * Initializes a TopQuizException object thanks to 1 parameter
     *
     * @param message a String object that contains the message of this exception
     */
    public TopQuizException(String message) {
        super(message);
    }
}