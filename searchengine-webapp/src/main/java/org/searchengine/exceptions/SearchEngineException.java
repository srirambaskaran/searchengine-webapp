package org.searchengine.exceptions;

public class SearchEngineException extends Exception{
    private static final long serialVersionUID = 1L;

    private String message;

    public SearchEngineException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}
