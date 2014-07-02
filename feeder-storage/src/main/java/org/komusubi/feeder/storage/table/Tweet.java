package org.komusubi.feeder.storage.table;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the tweets database table.
 * 
 */
@Entity
@Table(name="tweets")
@NamedQuery(name="Tweet.findAll", query="SELECT t FROM Tweet t")
public class Tweet implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String url;

	//bi-directional many-to-one association to Message
	@ManyToOne
	@JoinColumn(name="message_id", referencedColumnName="id")
	private Message message;

	public Tweet() {
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Message getMessage() {
		return this.message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

}