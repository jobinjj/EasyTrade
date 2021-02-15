package com.keralagamestudio.easytrade;

import com.keralagamestudio.easytrade.utils.Utils;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        Calendar c = Calendar.getInstance();   // this takes current date
        c.set(Calendar.DAY_OF_MONTH, 1);
        int lastDay = c.getActualMaximum(Calendar.DATE);   // this takes current date
        c.set(Calendar.DAY_OF_MONTH, lastDay);

        System.out.println(Utils.formatDate(c.getTime()));
        System.out.println(lastDay);
        assertEquals(4, 2 + 2);
    }
}