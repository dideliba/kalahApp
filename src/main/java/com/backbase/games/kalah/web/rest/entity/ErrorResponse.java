package com.backbase.games.kalah.web.rest.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Date;

/**
 * The response when an error occurs
 * @author didel
 *
 */
@JsonPropertyOrder({"timestamp", "status", "error", "exception", "message", "path"})
public class ErrorResponse {
    @JsonProperty
    private Date timestamp = new Date();

    @JsonProperty
    private int status;

    @JsonProperty
    private String error;

    @JsonProperty
    private String exception;

    @JsonProperty
    private String message;

    @JsonProperty
    private String path;

    public ErrorResponse(int status, String error, String exception, String message, String path) {
        this.status = status;
        this.error = error;
        this.exception = exception;
        this.message = message;
        this.path = path;
    }
}
