package org.komusubi.feeder.storage.table;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-07-02T17:39:37.857+0900")
@StaticMetamodel(Category.class)
public class Category_ {
	public static volatile SingularAttribute<Category, String> name;
	public static volatile ListAttribute<Category, Site> sites;
	public static volatile SingularAttribute<Category, Integer> id;
}
