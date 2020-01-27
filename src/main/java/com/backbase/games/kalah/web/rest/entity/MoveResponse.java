package com.backbase.games.kalah.web.rest.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Map;

/**
 * The response of a move request
 * @author didel
 *
 */
@JsonPropertyOrder({"id", "url", "status"})
public class MoveResponse {
	@JsonProperty
	private String id;
	@JsonProperty
	private String url;
	@JsonProperty
	private Map<String, String> status;

	public Map<String, String> getStatus() {
		return status;
	}

	public void setStatus(Map<String, String> status) {
		this.status = status;
	}

	public MoveResponse(int id, String uri, Map<String,String> status ) {
		this.id = String.valueOf(id);
		this.url = uri;
		this.status = status;
    }
	
	public MoveResponse() {
		
	}

}
