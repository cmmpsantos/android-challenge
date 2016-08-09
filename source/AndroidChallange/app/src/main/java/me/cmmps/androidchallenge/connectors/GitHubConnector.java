package me.cmmps.androidchallenge.connectors;

import android.os.AsyncTask;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import me.cmmps.androidchallenge.AppController;

/**
 * Created by csantos on 08/08/16.
 * Class to manage the connections to the GitHub API
 */
public class GitHubConnector {

    private final String GITHUB_AUTH_TOKEN = "bc60ca69602a6100f3e2febead13ccfd3cc2ba6c";

    public interface GitHubConnectorListener {
        void onResponse(RequestResponse requestResponse, String jsonResponse);
        void onError(RequestResponse requestResponse);
    }

    public enum RequestResponse {
        REQ_OK, NO_NETWORK, BAD_AUTH, NOT_FOUND, SERVER_ERROR, BAD_JSON
    }

    private GitHubConnectorListener gitHubConnectorListener;

    public GitHubConnector() {}

    public GitHubConnector setGitHubConnectorListener(GitHubConnectorListener gitHubConnectorListener) {
        this.gitHubConnectorListener = gitHubConnectorListener;
        return this;
    }

    public void queryURL(String url) throws Exception {

        if (AppController.DEBUG_FLAG) Log.d("GitHubConnector", "Quering URL " + url);

        if (gitHubConnectorListener==null) {
            // Can't use this method without setting the listener
            Log.e("GitHubConnector", "No listener setup for callbacks");
            throw new Exception("No listener setup for callbacks");
        }

        if (!isNetworkAvailable()) {
            Log.e("GitHubConnector", "Network is down");
            gitHubConnectorListener.onError(RequestResponse.NO_NETWORK);
        }
        else {
            new ProcessHTMLRequest().execute(url);
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) AppController.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private class ProcessHTMLRequest extends AsyncTask<String, Void, Boolean> {

        private RequestResponse lastRequestResponse;
        private String lastJSONResponse;

        @Override
        protected Boolean doInBackground(String... p) {

            if (AppController.DEBUG_FLAG) Log.d("GitHubConnector", "AsyncTask called");

            // Receive the URL to connect in the first argument
            String urlToConnect = p[0];
            URL url = null;

            try {

                url = new URL(urlToConnect);

                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                httpConn.setReadTimeout(10000);
                httpConn.setConnectTimeout(10000);
                httpConn.setRequestMethod("GET");
                // Add authorization header
                httpConn.addRequestProperty("Authorization", "token " + GITHUB_AUTH_TOKEN);
                httpConn.connect();

                int httpResponseCode = httpConn.getResponseCode();

                if (httpResponseCode == HttpURLConnection.HTTP_OK) {

                    String line = "";
                    InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
                    BufferedReader reader = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }

                    lastJSONResponse = sb.toString();
                    lastRequestResponse = RequestResponse.REQ_OK;
                    return true;

                }
                else if (httpResponseCode == HttpURLConnection.HTTP_FORBIDDEN || httpResponseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    lastRequestResponse = RequestResponse.BAD_AUTH;
                }
                else if (httpResponseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    lastRequestResponse = RequestResponse.NOT_FOUND;
                }
                else {
                    // Considers any other error as "server error"
                    lastRequestResponse = RequestResponse.SERVER_ERROR;
                }

                return false;

            }
            catch (Exception e) {

                Log.e("AsyncTaskError", e.getMessage());
                // Considers an exception as a "server error"
                lastRequestResponse = RequestResponse.SERVER_ERROR;
                return false;

            }

        }

        @Override
        protected void onPostExecute(Boolean result) {

            if (AppController.DEBUG_FLAG) Log.d("GitHubConnector", "AsyncTask onPostExecute called");

            if (gitHubConnectorListener!=null) {

                if (result) {
                    gitHubConnectorListener.onResponse(lastRequestResponse, lastJSONResponse);
                } else {
                    gitHubConnectorListener.onError(lastRequestResponse);
                }

            }
            else {
                Log.e("GitHubConnector", "No listener setup for callbacks on AsyncTask!");
            }

        }

    }

}
