package com.backbase.games.kalah.web.rest.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * The response of a create game request
 * @author didel
 *
 */
@JsonPropertyOrder({"id", "uri"})
public class CreateResponse {
	@JsonProperty
	private String id;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@JsonProperty
	private String uri;
	
	public CreateResponse(int id, String uri) {
		this.id = String.valueOf(id);
		this.uri = uri;
	}
	
}