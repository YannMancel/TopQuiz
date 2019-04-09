package com.mancel.yann.topquiz.model;

import java.util.Comparator;
import java.util.Map;

/**
 * Created by Yann MANCEL on 26/03/2019.
 * Name of the project: TopQuiz
 * Name of the package: com.mancel.yann.topquiz.model
 */
public class ScoreMapComparator implements Comparator<String> {

    Map<String, Integer> mMap;

    //----------------------------------------------------------------------------------------------

    /**
     * Initializes a ScoreMapComparator object thanks to 1 parameter
     *
     * @param map a Map<String, Integer> object that contains the key/value pairs
     */
    public ScoreMapComparator(Map<String, Integer> map) {
        this.mMap = map;
    }

    //----------------------------------------------------------------------------------------------

    @Override
    public int compare(String o1, String o2) {
        // Retrieves the value of the associated key
        Integer int1 = this.mMap.get(o1);
        Integer int2 = this.mMap.get(o2);

        // Decreasing order
        return int2.compareTo(int1);
    }
}