package me.cmmps.androidchallenge.interfaces;

import me.cmmps.androidchallenge.connectors.GitHubConnector;

/**
 * Created by csantos on 08/08/16.
 * Listener to be used in the DAO objects
 */
public interface DAOListener {
    void onLoad(GitHubConnector.RequestResponse requestResponse);
    void onError(GitHubConnector.RequestResponse errorResponse);
}
