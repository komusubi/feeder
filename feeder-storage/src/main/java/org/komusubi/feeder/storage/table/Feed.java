package org.komusubi.feeder.storage.table;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the feeds database table.
 * 
 */
@Entity
@Table(name="feeds")
@NamedQuery(name="Feed.findAll", query="SELECT f FROM Feed f")
public class Feed implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;
	private String name;

	//bi-directional many-to-one association to Site
	@OneToMany(mappedBy="feedBean")
	private List<Site> sites;

	public Feed() {
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
		site.setFeedBean(this);

		return site;
	}

	public Site removeSite(Site site) {
		getSites().remove(site);
		site.setFeedBean(null);

		return site;
	}

}