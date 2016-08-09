package me.cmmps.androidchallenge.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import me.cmmps.androidchallenge.AppController;
import me.cmmps.androidchallenge.interfaces.DAOListener;
import me.cmmps.androidchallenge.connectors.GitHubConnector;

/**
 * Created by csantos on 08/08/16.
 * "Data Access Object" for Repository
 */
public class GitRepoDAO implements GitHubConnector.GitHubConnectorListener {

    private GitRepoDO gitRepo;
    private DAOListener daoListener;
    private GitHubConnector gitHubConnector;
    private static String GIT_REPO_URL = "https://api.github.com/repos/android/platform_build";

    public GitRepoDAO() {}

    public GitRepoDAO setDAOListener(DAOListener daoListener) {
        this.daoListener = daoListener;
        return this;
    }

    public void loadRepo() throws Exception {

        if (daoListener==null) {
            // Can't use this method without setting the listener
            Log.e("DAO ERROR", "No DAOListener setup for callbacks");
            throw new Exception("No listener setup for callbacks");
        }

        gitHubConnector = new GitHubConnector();
        gitHubConnector.setGitHubConnectorListener(this);
        gitHubConnector.queryURL(GIT_REPO_URL);

    }

    public GitRepoDO getGitRepo() {
        return gitRepo;
    }

    private void processJsonData(JSONObject jsonObject) {

        gitRepo = new GitRepoDO();

        try {
            // Skipping "has" tests... if fails, catch will trigger "onError": BAD_JSON
            gitRepo.setId(jsonObject.getLong("id"));
            gitRepo.setName(jsonObject.getString("name"));
            gitRepo.setFullName(jsonObject.getString("full_name"));
            JSONObject ownerData = jsonObject.getJSONObject("owner");
            gitRepo.setOwnerLogin(ownerData.getString("login"));
            gitRepo.setOwnerAvatar(ownerData.getString("avatar_url"));
            String commitsURL = jsonObject.getString("commits_url");
            if (commitsURL.indexOf('{') > 0) {
                commitsURL = commitsURL.substring(0, commitsURL.indexOf('{'));
            }
            gitRepo.setCommitsURL(commitsURL);
            daoListener.onLoad(GitHubConnector.RequestResponse.REQ_OK);
        }
        catch (Exception e) {
            daoListener.onError(GitHubConnector.RequestResponse.BAD_JSON);
        }

    }

    @Override
    public void onResponse(GitHubConnector.RequestResponse requestResponse, String jsonResponse) {
        try {
            if (AppController.DEBUG_FLAG) Log.d("GitRepoDAOResponse", jsonResponse);
            JSONObject jsonObject = new JSONObject(jsonResponse);
            processJsonData(jsonObject);
        }
        catch (JSONException e) {
            daoListener.onError(GitHubConnector.RequestResponse.BAD_JSON);
        }
    }

    @Override
    public void onError(GitHubConnector.RequestResponse requestResponse) {
        daoListener.onError(requestResponse);
    }
}
