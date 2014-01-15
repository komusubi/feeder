package org.komusubi.feeder.model.airline;

import java.io.Serializable;

/**
 * 
 * @author jun.ozeki 2013/11/27
 */
public class Route implements Serializable {
    private static final long serialVersionUID = 1L;
    private Airport departure;
    private Airport arrival;

    public Route(Airport departure, Airport arrival) {
        this.departure = departure;
        this.arrival = arrival;
    }

    public Airport getDeparture() {
        return departure;
    }

    public Airport getArrival() {
        return arrival;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Route [departure=").append(departure).append(", arrival=").append(arrival).append("]");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((arrival == null) ? 0 : arrival.hashCode());
        result = prime * result + ((departure == null) ? 0 : departure.hashCode());
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
        Route other = (Route) obj;
        if (arrival == null) {
            if (other.arrival != null)
                return false;
        } else if (!arrival.equals(other.arrival))
            return false;
        if (departure == null) {
            if (other.departure != null)
                return false;
        } else if (!departure.equals(other.departure))
            return false;
        return true;
    }
    
}
