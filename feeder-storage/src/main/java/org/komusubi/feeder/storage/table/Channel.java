package org.komusubi.feeder.storage.table;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the channels database table.
 * 
 */
@Entity
@Table(name="channels")
@NamedQuery(name="Channel.findAll", query="SELECT c FROM Channel c")
public class Channel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;
	private String name;

	//bi-directional many-to-one association to Site
	@OneToMany(mappedBy="channelBean")
	private List<Site> sites;

	public Channel() {
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
		site.setChannelBean(this);

		return site;
	}

	public Site removeSite(Site site) {
		getSites().remove(site);
		site.setChannelBean(null);

		return site;
	}

}