package org.komusubi.feeder.model.airline;

import java.io.Serializable;

/**
 * 
 * @author jun.ozeki 2013/11/27
 */
public class Aircraft implements Serializable {
    private static final long serialVersionUID = 1L;
    // TODO aircraft code abbreviation ? (IATA?)
    private String code;
    private String name;

    public Aircraft(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Aircraft [code=").append(code).append(", name=").append(name).append("]");
        return builder.toString();
    }
    
}
    
