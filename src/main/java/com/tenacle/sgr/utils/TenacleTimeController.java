/*
 * Copyright 2014 samuel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tenacle.sgr.utils;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author samuel
 */
public class TenacleTimeController {

    private TenacleTimeController() {}

    public static TenacleTimeController getInstance() {
        return TransactionControllerHolder.INSTANCE;
    }

    private static class TransactionControllerHolder {

        private static final TenacleTimeController INSTANCE = new TenacleTimeController();
    }

    /**
     * Computes the start and end times of a day.
     * @param date
     * @return 
     */
    public static Date[] _24HourDateBound(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);

        Date start = cal.getTime();

        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 23, 59, 59);
        Date end = cal.getTime();

        return new Date[]{start, end};
    }
    
    
    /**
     * Computes the start of a day given a time.
     * 
     * @param date
     * @return 
     */
    public static Date _24HourDateStart(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);
        Date start = cal.getTime();
        return start;
    }
    
    /**
     * Computes the last second of the day.
     * @param date
     * @return 
     */
    public static Date _24HourDateEnd(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);        
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 23, 59, 59);
        Date end = cal.getTime();
        return end;
    }
    
    public static Date getFirstDayOfMonth(Date date){
        date.setDate(1);
        return _24HourDateStart(date);
    }
    
    public static Date getLastDayOfMonth(Date date){
        date.setDate(getLastDay(date));
        return _24HourDateEnd(date);
    }
    
    private static int getLastDay(Date date) {
        int month = date.getMonth();
        switch (month) {
            case 0:
                return 31;
            case 1:
                return (date.getYear() %4 == 0)? 28 : 29 ;
            case 2:
                return 31;
            case 3:
                return 30;
            case 4:
                return 31;
            case 5:
                return 30;
            case 6:
                return 31;
            case 7:
                return 30;
            case 8:
                return 31;
            case 9:
                return 30;
            case 10:
                return 31;
            case 11:
                return 31;
            default:
        }
        return 0;
    }
    
}

