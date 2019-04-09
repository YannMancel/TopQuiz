package com.mancel.yann.topquiz.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * Created by Yann MANCEL on 05/03/2019.
 * Name of the project: TopQuiz
 * Name of the package: com.mancel.yann.topquiz.model
 */
public class Ranking {

    private Map<String, Integer> mUserMap;
    private int mMaxCount;

    //----------------------------------------------------------------------------------------------

    /**
     * Initializes a Ranking object with 1 argument
     *
     * @param maxCount an integer that contains the maximal number of item to return
     */
    public Ranking(final int maxCount) {
        // The TreeSet object is an object that sorts its elements by natural ordering (default)
        this.mUserMap = new TreeMap<>();

        // Initializes the maximal number of item in the Map field
        this.mMaxCount = maxCount;
    }

    //----------------------------------------------------------------------------------------------

    /**
     * Adds an item (Key/Value system) to the Map field
     *
     * @param user a String object that contains the key
     * @param score a Integer object that contains the value
     */
    public void addItem(final String user, final Integer score) {
        // Checks if the user, given in argument, is already present
        if (this.mUserMap.containsKey(user)) {
            // Checks if the score, given in argument, is less or equal to the last score of user
            if (score <= this.mUserMap.get(user)) {
                /*
                 If the user is already contained in the Map field and
                 the score is less or equal to the last score,
                 so this method is finished
                 The Map is not modified
                 */
                return;
            }
            else {
                // Adds or modifies the Key/Value system
                this.mUserMap.put(user, score);
            }
        }
        else {
            // Checks if the new score is greater than the others
            int count = 0;

            // Receives a Set<Map.Entry<String, Integer>> object that contains the keys/values of the Map field
            Set<Map.Entry<String, Integer>> setEntry = this.mUserMap.entrySet();

            // Receives the iterator to browse the Map field
            Iterator<Map.Entry<String, Integer>> it = setEntry.iterator();

            // The system key/value for an item
            Map.Entry<String, Integer> entry;

            Integer valueToInteger;
            Integer minValue = Integer.MAX_VALUE;
            String userFromMinValue = "";

            while (it.hasNext()) {
                entry = it.next();

                valueToInteger = entry.getValue();

                // Retrieves the user/score couple if the assessed value if less than the minValue
                if (valueToInteger < minValue) {
                    minValue = valueToInteger;
                    userFromMinValue = entry.getKey();
                }

                // Counts the values which are greater or equal to the value of couple given in argument
                if (valueToInteger >= score) {
                    ++count;
                }

                // There is already [mMaxCount] values greater or equal to the value of couple given in argument
                // So the Map size is fully filled.
                if (count == this.mMaxCount) return;
            }

            /*
             Now the value of count is necessarily less than [mMaxCount] because the second IF
             throw a return statement
             */

            /*
             Adds the Key/Value system
             Warning: Now there are potentially [[mMaxCount + 1] values
             The sixth item must be removed except if the map's size is less than [mMaxCount]
             */
            this.mUserMap.put(user, score);

            /*
             Removes the item which contains the minimal value
             except if the map's size is less than [mMaxCount]
             */
            if (this.mUserMap.size() > this.mMaxCount) {
                if (userFromMinValue != "") {
                    this.mUserMap.remove(userFromMinValue);
                }
            }
        }
    }

    /**
     * Modifies the Map field thanks to the Set<String> object given in argument
     *
     * @param set a Set<String> object that contains the new data
     */
    public void setUserMap(final Set<String> set) {
        // The TreeSet object is an object that sorts its elements by natural ordering (default)
        Map<String, Integer> map = new TreeMap<>();

        // Receives the iterator to browse the Set
        Iterator<String> it = set.iterator();

        String str;
        StringTokenizer strTokenizer;

        while(it.hasNext()) {
            str = it.next();

            // Allows to cut a String in sub-String thanks to the delimiter: [name ----> score]
            strTokenizer = new StringTokenizer(str," ----> ");

            // Each StringTokenizer object contains 2 elements, key (String) and value (Integer)
            map.put(strTokenizer.nextToken(), Integer.parseInt(strTokenizer.nextToken()));
        }

        // Initializes the map with the new data
        this.mUserMap = map;
    }

    /**
     * Gets the Map field
     *
     * @return a Set<String> object that contains the data
     */
    public Set<String> getUserMapToStringSet() {
        // The set will be returned
        Set<String> set = new HashSet<>();

        // Receives a Set<Map.Entry<String, Integer>> object that contains the keys/values of the Map field
        Set<Map.Entry<String, Integer>> setEntry = this.mUserMap.entrySet();

        // Receives the iterator to browse the Map field
        Iterator<Map.Entry<String, Integer>> it = setEntry.iterator();

        // The system key/value for an item
        Map.Entry<String, Integer> entry;

        while(it.hasNext()) {
            entry = it.next();

            // Adds the element to the set: [name ----> score]
            set.add(entry.getKey() + " ----> " + entry.getValue());
        }

        return set;
    }

    /**
     * Returns a List<String> object that is sorted in the natural ordering of the letters
     *
     * @return a List<String> object that is sorted in function of the natural ordering of the letters
     */
    public List<String> alphabeticalRanking() {
        // The list will be returned
        List<String> list = new ArrayList<>();

        // Receives a Set<Map.Entry<String, Integer>> object that contains the keys/values of the Map field
        Set<Map.Entry<String, Integer>> setEntry = this.mUserMap.entrySet();

        // Receives the iterator to browse the Map field
        Iterator<Map.Entry<String, Integer>> it = setEntry.iterator();

        // The system key/value for an item
        Map.Entry<String, Integer> entry;

        while(it.hasNext()) {
            entry = it.next();

            // Adds the element to the list: [name: score]
            list.add(entry.getKey() + ": " + entry.getValue());
        }

        return list;
    }

    /**
     * Returns a List<String> object that is sorted in the decreasing ordering of the scores
     *
     * @return a List<String> object that is sorted in function of the decreasing ordering of the scores
     */
    public List<String> scoreRanking() {
        /*
         The list will be returned
         The list is initialized with a list returned by the alphabeticalRanking method
         */
        List<String> list = new ArrayList<>(this.alphabeticalRanking());

        // Sorts the specified list according to the order induced by the specified comparator.
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                // Allows to cut a String in sub-String thanks to the delimiter: [name: score]
                StringTokenizer string1 = new StringTokenizer(o1,": ");
                StringTokenizer string2 = new StringTokenizer(o2,": ");

                // Allows to go to the second item [score]
                string1.nextToken();
                string2.nextToken();

                // Retrieves the score to an Integer object
                Integer integer1 = Integer.parseInt(string1.nextToken());
                Integer integer2 = Integer.parseInt(string2.nextToken());

                // Decreasing order
                return integer2.compareTo(integer1);
            }
        });

        return list;
    }
}