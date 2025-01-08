package com.dionext.db.entity;

import jakarta.persistence.*;

import java.io.Serializable;
//import javax.persistence.*;


/**
 * The persistent class for the JCountries database table.
 * 
 */
@Entity
@Table(name="joom_categories")
public class JoomCategories implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "path")
	private String path;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
