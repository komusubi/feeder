package org.komusubi.feeder.model.airline;

import java.io.Serializable;

/**
 * 
 * @author jun.ozeki 2013/11/28
 */
public interface Segment extends Serializable {
    /**
     * node type.
     * @author jun.ozeki 2013/11/27
     */
    public enum Type {
        SCHEDULED, // scheduled leg
        ESTIMATED, // before departure leg
        ACTUAL,    // after departure leg (include irregular node)
        VIRTUAL;   // virtual node ex) "ARUNK"...
    }

    public enum Status {
        
    }
    Route getRoute();
    FlightName getFlightName();
    Type getType();
    boolean isIrregular();
}
