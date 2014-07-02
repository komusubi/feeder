package org.komusubi.feeder.storage.table;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the messages database table.
 * 
 */
@Entity
@Table(name="messages")
@NamedQuery(name="Message.findAll", query="SELECT m FROM Message m")
public class Message implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;
	private Timestamp created;

	private String text;

	//bi-directional many-to-one association to Site
	@ManyToOne
	@JoinColumn(name="site_id", referencedColumnName="id")
	private Site site;

	//bi-directional many-to-one association to Tweet
	@OneToMany(mappedBy="message")
	private List<Tweet> tweets;

	public Message() {
	}

	public Timestamp getCreated() {
		return this.created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Site getSite() {
		return this.site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public List<Tweet> getTweets() {
		return this.tweets;
	}

	public void setTweets(List<Tweet> tweets) {
		this.tweets = tweets;
	}

	public Tweet addTweet(Tweet tweet) {
		getTweets().add(tweet);
		tweet.setMessage(this);

		return tweet;
	}

	public Tweet removeTweet(Tweet tweet) {
		getTweets().remove(tweet);
		tweet.setMessage(null);

		return tweet;
	}

}