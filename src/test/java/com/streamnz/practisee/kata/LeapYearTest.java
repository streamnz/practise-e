package com.streamnz.practisee.kata;

import java.time.LocalDate;

/**
 * @Author cheng hao
 * @Date 08/10/2025 20:57
 */
public class LeapYearTest {

    boolean isLeapYear(int year) {
        // edge case
        int currYear = LocalDate.now().getYear();
        if(year <= 0 || year > currYear) {
            throw new IllegalArgumentException("Year must be between 1 and " + currYear);
        }
        if (year % 4 == 0) {
            if (year % 100 == 0) {
                return year % 400 == 0;
            }
            return true;
        }
        return false;
    }



}
