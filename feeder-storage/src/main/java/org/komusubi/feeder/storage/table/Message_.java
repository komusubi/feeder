package org.komusubi.feeder.storage.table;

import java.sql.Timestamp;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-07-03T10:17:06.974+0900")
@StaticMetamodel(Message.class)
public class Message_ {
	public static volatile SingularAttribute<Message, Integer> id;
	public static volatile SingularAttribute<Message, Timestamp> created;
	public static volatile SingularAttribute<Message, String> text;
	public static volatile SingularAttribute<Message, Site> site;
	public static volatile ListAttribute<Message, Tweet> tweets;
}