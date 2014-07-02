package org.komusubi.feeder.storage.table;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the sites database table.
 * 
 */
@Entity
@Table(name="sites")
@NamedQuery(name="Site.findAll", query="SELECT s FROM Site s")
public class Site implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;
	private String name;

	private String url;

	//bi-directional many-to-one association to Category
	@ManyToOne
	@JoinColumn(name="category", referencedColumnName="id")
	private Category categoryBean;

	//bi-directional many-to-one association to Channel
	@ManyToOne
	@JoinColumn(name="channel", referencedColumnName="id")
	private Channel channelBean;

	//bi-directional many-to-one association to Feed
	@ManyToOne
	@JoinColumn(name="feed", referencedColumnName="id")
	private Feed feedBean;

	//bi-directional many-to-one association to Message
	@OneToMany(mappedBy="site")
	private List<Message> messages;

	public Site() {
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Category getCategoryBean() {
		return this.categoryBean;
	}

	public void setCategoryBean(Category categoryBean) {
		this.categoryBean = categoryBean;
	}

	public Channel getChannelBean() {
		return this.channelBean;
	}

	public void setChannelBean(Channel channelBean) {
		this.channelBean = channelBean;
	}

	public Feed getFeedBean() {
		return this.feedBean;
	}

	public void setFeedBean(Feed feedBean) {
		this.feedBean = feedBean;
	}

	public List<Message> getMessages() {
		return this.messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public Message addMessage(Message message) {
		getMessages().add(message);
		message.setSite(this);

		return message;
	}

	public Message removeMessage(Message message) {
		getMessages().remove(message);
		message.setSite(null);

		return message;
	}

}