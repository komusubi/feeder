package org.komusubi.feeder.model.airline;

import java.util.regex.Pattern;

/**
 *
 * @author jun.ozeki 2013/11/27
 */
public class FlightNumber {
    private static final Pattern pattern = Pattern.compile("[\\d]{3,}");
    private String number;

    public FlightNumber(String number) {
        if (!validate(number))
            throw new IllegalArgumentException("wrong format: flight number: " + number);
        this.number = number;
    }

    public String getNumber() {
        return number;
    }
    
    public static boolean validate(String number) {
        return pattern.matcher(number).find();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("FlightNumber [number=").append(number).append("]");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((number == null) ? 0 : number.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FlightNumber other = (FlightNumber) obj;
        if (number == null) {
            if (other.number != null)
                return false;
        } else if (!number.equals(other.number))
            return false;
        return true;
    }
   
}
