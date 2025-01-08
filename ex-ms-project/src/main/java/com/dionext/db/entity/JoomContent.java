package com.dionext.db.entity;

import jakarta.persistence.*;

import java.io.Serializable;
//import javax.persistence.*;


/**
 * The persistent class for the JCountries database table.
 * 
 */
@Entity
@Table(name="joom_content")
//@NamedQuery(name="JCountry.findAll", query="SELECT j FROM JCountry j")
public class JoomContent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id")
	private String id;

	@Column(name="catid")
	private String catId;

	@Lob
	@Column(name="title")
	private String title;

	@Lob
	@Column(name="alias")
	private String alias;

	@Lob
	@Column(name="created_by_alias")
	private String createdByAlias;
	@Lob
	@Column(name="introtext")
	private String introtext;

	@Lob
	@Column(name="fulltext")
	private String fulltext;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getCreatedByAlias() {
		return createdByAlias;
	}

	public void setCreatedByAlias(String createdByAlias) {
		this.createdByAlias = createdByAlias;
	}

	public String getIntrotext() {
		return introtext;
	}

	public void setIntrotext(String introtext) {
		this.introtext = introtext;
	}

	public String getFulltext() {
		return fulltext;
	}

	public void setFulltext(String fulltext) {
		this.fulltext = fulltext;
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}
}