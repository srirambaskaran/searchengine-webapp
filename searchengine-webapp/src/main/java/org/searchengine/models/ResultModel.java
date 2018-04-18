package org.searchengine.models;

import java.util.ArrayList;
import java.util.List;

public class ResultModel {
    private boolean status;
    private String message;
    private int numResult;
    private List<Result> responses;
    
    public ResultModel() {
        this.numResult = 0;
        this.responses = new ArrayList<>();
        this.status = false;
        this.message = "not run";
    }

    public int getNumResult() {
        return numResult;
    }

    public List<Result> getResponses() {
        return responses;
    }

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setNumResult(int numResult) {
        this.numResult = numResult;
    }
    
    public void addResult(Result result) {
        this.responses.add(result);
    }
    
}
