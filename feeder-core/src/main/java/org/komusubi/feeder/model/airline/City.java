package org.komusubi.feeder.model.airline;

import java.io.Serializable;

/**
 * 
 * @author jun.ozeki 2013/11/28
 */
public class City implements Serializable {
    private static final long serialVersionUID = 1L;
    private Airports airports;
    private String code;
    
    public City(String code, Airports airports) {
        this.code = code;
        this.airports = airports;
    }
    
    public String getCode() {
        return code;
    }
    
    public Airports getAirports() {
        return airports;
    }
}
