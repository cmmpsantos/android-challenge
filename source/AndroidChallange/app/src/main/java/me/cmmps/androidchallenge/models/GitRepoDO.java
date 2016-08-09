package me.cmmps.androidchallenge.models;

/**
 * Created by csantos on 08/08/16.
 * POJO class for holding repo data.
 * For this example / challenge, I won't consider having the list of the Commits declared here as
 * we are working with just one repo.
 */
public class GitRepoDO {

    private long id;
    private String name;
    private String fullName;
    private String ownerLogin;
    private String ownerAvatar;
    private String commitsURL;

    public GitRepoDO() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    public String getOwnerAvatar() {
        return ownerAvatar;
    }

    public void setOwnerAvatar(String ownerAvatar) {
        this.ownerAvatar = ownerAvatar;
    }

    public String getCommitsURL() {
        return commitsURL;
    }

    public void setCommitsURL(String commitsURL) {
        this.commitsURL = commitsURL;
    }

}
