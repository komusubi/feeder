package org.komusubi.feeder.model.airline;

import java.io.Serializable;

import org.komusubi.feeder.model.airline.Airline.Code;

/**
 * 
 * @author jun.ozeki 2013/11/28
 */
public class FlightName implements Serializable {
    private static final long serialVersionUID = 1L;
    private Code code;
    private FlightNumber flightNumber;

    public FlightName(Airline.Code code, FlightNumber flightNumber) {
        this.code = code;
        this.flightNumber = flightNumber;
    }
    
    // FIXME fomatter
    public String getFlightName() {
        return code.getCode() + " " + flightNumber.getNumber();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((flightNumber == null) ? 0 : flightNumber.hashCode());
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
        FlightName other = (FlightName) obj;
        if (code != other.code)
            return false;
        if (flightNumber == null) {
            if (other.flightNumber != null)
                return false;
        } else if (!flightNumber.equals(other.flightNumber))
            return false;
        return true;
    }
    
}
