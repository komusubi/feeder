package org.komusubi.feeder.sns.twitter.spi;

import javax.inject.Inject;
import javax.inject.Provider;

import org.komusubi.feeder.model.Tag;
import org.komusubi.feeder.sns.twitter.HashTag;

/**
 * 
 * @author jun.ozeki
 */
public class TagProvider implements Provider<Tag> {

    private String value;
    
    /**
     * create new instance.
     * @param value
     */
    @Inject
    public TagProvider(String value) {
        this.value = value;
    }
    
    @Override
    public Tag get() {
        return new HashTag(value);
    }

}
