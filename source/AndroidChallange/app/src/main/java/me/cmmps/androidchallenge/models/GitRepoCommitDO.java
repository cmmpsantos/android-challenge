package me.cmmps.androidchallenge.models;

import java.io.Serializable;

/**
 * Created by csantos on 08/08/16.
 * POJO class for holding the commits
 */
public class GitRepoCommitDO implements Serializable {

    private String sha;
    private String authorName;
    private String authorDate;
    private String commiterName;
    private String commiterDate;
    private String message;
    private String htmlURL;

    public GitRepoCommitDO() {}

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorDate() {
        return authorDate;
    }

    public void setAuthorDate(String authorDate) {
        this.authorDate = authorDate;
    }

    public String getCommiterName() {
        return commiterName;
    }

    public void setCommiterName(String commiterName) {
        this.commiterName = commiterName;
    }

    public String getCommiterDate() {
        return commiterDate;
    }

    public void setCommiterDate(String commiterDate) {
        this.commiterDate = commiterDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHtmlURL() {
        return htmlURL;
    }

    public void setHtmlURL(String htmlURL) {
        this.htmlURL = htmlURL;
    }

}
