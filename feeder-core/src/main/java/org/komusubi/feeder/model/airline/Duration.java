package org.komusubi.feeder.model.airline;

import java.util.Date;

import javax.inject.Provider;

import org.komusubi.feeder.utils.Providers;

/**
 * 
 * @author jun.ozeki 2013/11/27
 */
public class Duration {

    private Date departure;
    private Date arrival;

    public Duration() {
        this(Providers.DefaultDate, Providers.DefaultDate);
    }

    public Duration(Provider<Date> departure, Provider<Date> arrival) {
        this.departure = departure.get();
        this.arrival = arrival.get();
    }

    public Date getDeparture() {
        return departure;
    }
    
    public Date getArrival() {
        return arrival;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Schedule [arrival=").append(arrival).append(", departure=").append(departure).append("]");
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
        Duration other = (Duration) obj;
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
