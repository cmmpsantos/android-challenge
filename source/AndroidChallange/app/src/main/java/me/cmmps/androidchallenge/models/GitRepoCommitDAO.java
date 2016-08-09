package me.cmmps.androidchallenge.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.cmmps.androidchallenge.AppController;
import me.cmmps.androidchallenge.interfaces.DAOListener;
import me.cmmps.androidchallenge.connectors.GitHubConnector;

/**
 * Created by csantos on 08/08/16.
 * "Data Access Object" for commits
 */
public class GitRepoCommitDAO implements GitHubConnector.GitHubConnectorListener {

    private ArrayList<GitRepoCommitDO> gitRepoCommits;
    private DAOListener daoListener;
    private GitHubConnector gitHubConnector;
    private String commitURL;

    public GitRepoCommitDAO(String commitURL) {
        this.commitURL = commitURL;
    }

    public GitRepoCommitDAO setDAOListener(DAOListener daoListener) {
        this.daoListener = daoListener;
        return this;
    }

    public void loadCommits() throws Exception {

        if (daoListener==null) {
            // Can't use this method without setting the listener
            throw new Exception("No listener setup for callbacks");
        }

        gitHubConnector = new GitHubConnector();
        gitHubConnector.setGitHubConnectorListener(this);
        gitHubConnector.queryURL(commitURL);

    }

    public ArrayList<GitRepoCommitDO> getGitRepoCommits() {
        return gitRepoCommits;
    }

    private void processJsonData(JSONArray jsonArray) {

        gitRepoCommits = new ArrayList<>();

        try {
            // Skipping "has" tests... if fails, catch will trigger "onError": BAD_JSON
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                GitRepoCommitDO gitRepoCommit = new GitRepoCommitDO();

                gitRepoCommit.setSha(jsonObject.getString("sha"));

                JSONObject commitData = jsonObject.getJSONObject("commit");

                JSONObject authorData = commitData.getJSONObject("author");
                gitRepoCommit.setAuthorName(authorData.getString("name"));
                gitRepoCommit.setAuthorDate(authorData.getString("date").replace('T', ' ').replace('Z', ' '));

                JSONObject committerData = commitData.getJSONObject("committer");
                gitRepoCommit.setCommiterName(committerData.getString("name"));
                gitRepoCommit.setCommiterDate(committerData.getString("date").replace('T', ' ').replace('Z', ' '));

                gitRepoCommit.setMessage(commitData.getString("message"));
                gitRepoCommit.setHtmlURL(jsonObject.getString("html_url"));

                gitRepoCommits.add(gitRepoCommit);

            }

            daoListener.onLoad(GitHubConnector.RequestResponse.REQ_OK);

        }
        catch (Exception e) {
            daoListener.onError(GitHubConnector.RequestResponse.BAD_JSON);
        }

    }

    @Override
    public void onResponse(GitHubConnector.RequestResponse requestResponse, String jsonResponse) {
        try {
            if (AppController.DEBUG_FLAG) Log.d("GitRepoCommitDAOResp", jsonResponse);
            JSONArray jsonArray = new JSONArray(jsonResponse);
            processJsonData(jsonArray);
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
