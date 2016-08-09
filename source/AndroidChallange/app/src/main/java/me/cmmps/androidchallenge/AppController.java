package me.cmmps.androidchallenge;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;

import me.cmmps.androidchallenge.connectors.GitHubConnector;
import me.cmmps.androidchallenge.interfaces.DAOListener;
import me.cmmps.androidchallenge.models.GitRepoCommitDAO;
import me.cmmps.androidchallenge.models.GitRepoCommitDO;
import me.cmmps.androidchallenge.models.GitRepoDAO;
import me.cmmps.androidchallenge.models.GitRepoDO;

/**
 * Created by csantos on 08/08/16.
 */
public class AppController extends Application {

    // Flag to enable / disable Log.d outputs
    public static boolean DEBUG_FLAG = true;

    public interface AppControllerListener {
        void onComplete(GitHubConnector.RequestResponse requestResponse);
    }

    private static AppController mInstance;

    private AppControllerListener appControllerListener;

    private static GitRepoDO gitRepo;
    private static ArrayList<GitRepoCommitDO> gitRepoCommits;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public AppController setAppControllerListener(AppControllerListener appControllerListener) {
        this.appControllerListener = appControllerListener;
        return this;
    }

    public static GitRepoDO getGitRepo() {
        return gitRepo;
    }

    public static ArrayList<GitRepoCommitDO> getGitRepoCommits() {
        return gitRepoCommits;
    }

    public void loadRepoData() {

        if (gitRepo==null) {

            if (DEBUG_FLAG) Log.d("APPCONTROLLER", "Requesting data from server");

            // Load repo data from GitHub
            final GitRepoDAO gitRepoDAO = new GitRepoDAO();
            gitRepoDAO.setDAOListener(new DAOListener() {
                @Override
                public void onLoad(GitHubConnector.RequestResponse requestResponse) {
                    // If data successfully loaded, load commits
                    gitRepo = gitRepoDAO.getGitRepo();
                    if (gitRepoCommits==null) {
                        loadCommitData();
                    }
                    else {
                        appControllerListener.onComplete(requestResponse);
                    }
                }

                @Override
                public void onError(GitHubConnector.RequestResponse errorResponse) {
                    appControllerListener.onComplete(errorResponse);
                }
            });

            try {
                gitRepoDAO.loadRepo();
            }
            catch (Exception e) {
                // This exception should not be thrown as we are declaring a listener
                Log.e("AppController", e.getMessage());
            }

        }
        else {

            if (gitRepoCommits==null) {
                loadCommitData();
            }
            else {
                appControllerListener.onComplete(GitHubConnector.RequestResponse.REQ_OK);
            }

        }

    }

    private void loadCommitData() {

        final GitRepoCommitDAO gitRepoCommitDAO = new GitRepoCommitDAO(gitRepo.getCommitsURL());
        gitRepoCommitDAO.setDAOListener(new DAOListener() {
            @Override
            public void onLoad(GitHubConnector.RequestResponse requestResponse) {
                gitRepoCommits = gitRepoCommitDAO.getGitRepoCommits();
                appControllerListener.onComplete(requestResponse);
            }

            @Override
            public void onError(GitHubConnector.RequestResponse errorResponse) {
                appControllerListener.onComplete(errorResponse);
            }
        });

        try {
            gitRepoCommitDAO.loadCommits();
        }
        catch (Exception e) {
            // This exception should not be thrown as we are declaring a listener
            Log.e("AppController", e.getMessage());
        }

    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

}
