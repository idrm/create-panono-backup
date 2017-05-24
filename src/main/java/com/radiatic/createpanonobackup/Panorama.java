package com.radiatic.createpanonobackup;

import org.joda.time.DateTime;

public class Panorama {

	private final String id;
	private final String title;
	private final DateTime createdAt;
	private final String description;

	public Panorama(String id, String title, DateTime createdAt, String description) {
		this.id = id;
		this.title = title;
		this.createdAt = createdAt;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public DateTime getCreatedAt() {
		return createdAt;
	}

	public String getDescription() {
		return description;
	}
}
