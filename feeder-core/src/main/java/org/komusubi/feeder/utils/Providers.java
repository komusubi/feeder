package org.komusubi.feeder.utils;

import java.util.Date;

import javax.inject.Provider;

/**
 * 
 * @author jun.ozeki 2013/11/27
 */
public class Providers {

    public static final Provider<Date> CurrentDate = new Provider<Date>() {

        @Override
        public Date get() {
            return new Date();
        }
        
    };
    
    public static final Provider<Date> DefaultDate = new Provider<Date>() {
        private Date date = new Date(0L);

        @Override
        public Date get() {
            return date;
        }
        
    };

}
