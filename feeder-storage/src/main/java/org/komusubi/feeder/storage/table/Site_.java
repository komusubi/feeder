package org.komusubi.feeder.storage.table;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-07-03T10:17:06.983+0900")
@StaticMetamodel(Site.class)
public class Site_ {
	public static volatile SingularAttribute<Site, Integer> id;
	public static volatile SingularAttribute<Site, String> name;
	public static volatile SingularAttribute<Site, String> url;
	public static volatile SingularAttribute<Site, Category> categoryBean;
	public static volatile SingularAttribute<Site, Channel> channelBean;
	public static volatile SingularAttribute<Site, Feed> feedBean;
	public static volatile ListAttribute<Site, Message> messages;
}
