package org.komusubi.feeder.storage.table;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the categories database table.
 * 
 */
@Entity
@Table(name="categories")
@NamedQuery(name="Category.findAll", query="SELECT c FROM Category c")
public class Category implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;
	private String name;

	//bi-directional many-to-one association to Site
	@OneToMany(mappedBy="categoryBean")
	private List<Site> sites;

	public Category() {
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Site> getSites() {
		return this.sites;
	}

	public void setSites(List<Site> sites) {
		this.sites = sites;
	}

	public Site addSite(Site site) {
		getSites().add(site);
		site.setCategoryBean(this);

		return site;
	}

	public Site removeSite(Site site) {
		getSites().remove(site);
		site.setCategoryBean(null);

		return site;
	}

}