package org.komusubi.feeder.storage.table;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-07-02T16:55:14.499+0900")
@StaticMetamodel(Feed.class)
public class Feed_ {
	public static volatile SingularAttribute<Feed, String> name;
	public static volatile ListAttribute<Feed, Site> sites;
	public static volatile SingularAttribute<Feed, Integer> id;
}
