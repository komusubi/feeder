package org.komusubi.feeder.storage.table;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-07-02T16:54:52.079+0900")
@StaticMetamodel(Channel.class)
public class Channel_ {
	public static volatile SingularAttribute<Channel, String> name;
	public static volatile ListAttribute<Channel, Site> sites;
	public static volatile SingularAttribute<Channel, Integer> id;
}
