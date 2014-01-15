package org.komusubi.feeder.model.airline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.komusubi.feeder.model.airline.Airport.Attribute;

/**
 * 
 * @author jun.ozeki 2013/11/27
 */
public class Airports extends ArrayList<Airport> {
    private static final long serialVersionUID = 1L;
    private Map<String, Airport> map;

    public Airports() {
    }

    public Airports(Collection<Airport> c) {
        super(c);
    }

    public Airports findByAttribute(Attribute attr) {
        Airports result = new Airports();
        for (Airport a: this) {
            if (attr.equals(a.getAttribute()))
                result.add(a);
        }
        return result;
    }

    @Deprecated 
    // multi attribute can not handle. ex) CTS, FUK... 
    public Map<String, Airport> getMap() {
        if (map != null)
            return map;
        Map<String, Airport> map = new HashMap<String, Airport>();
        for (Airport a: this) {
            map.put(a.getCode(), a);
        }
        return map;
    }
    
    @Deprecated
    public Airport getAirport(String code) {
        return getMap().get(code);
    }

}
