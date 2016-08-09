package me.cmmps.androidchallenge;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import me.cmmps.android.widgets.WebImageView;
import me.cmmps.androidchallenge.connectors.GitHubConnector;
import me.cmmps.androidchallenge.interfaces.RVAdapterListener;
import me.cmmps.androidchallenge.models.GitRepoCommitDO;

public class MainActivity extends AppCompatActivity implements RVAdapterListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context context = this;
        final WebImageView avatarImage = (WebImageView) findViewById(R.id.imageAvatar);
        final TextView textRepoName = (TextView) findViewById(R.id.textRepoName);
        final TextView textRepoFullName = (TextView) findViewById(R.id.textRepoFullName);
        final TextView textRepoLogin = (TextView) findViewById(R.id.textRepoLogin);
        final TextView textListTitle = (TextView) findViewById(R.id.textListTitle);
        final RecyclerView viewCommits = (RecyclerView) findViewById(R.id.viewCommits);

        AppController appController = AppController.getInstance();
        appController.setAppControllerListener(new AppController.AppControllerListener() {
            @Override
            public void onComplete(GitHubConnector.RequestResponse requestResponse) {

                if (requestResponse == GitHubConnector.RequestResponse.REQ_OK) {

                    // Populate objects
                    avatarImage.loadURL(AppController.getGitRepo().getOwnerAvatar());
                    textRepoName.setText(AppController.getGitRepo().getName());
                    textRepoFullName.setText(AppController.getGitRepo().getFullName());
                    textRepoLogin.setText(AppController.getGitRepo().getOwnerLogin());

                    // Set recycler view
                    RecyclerViewAdapter viewAdapter = new RecyclerViewAdapter(AppController.getGitRepoCommits());
                    viewAdapter.setRVAdapterListener(MainActivity.this);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                    viewCommits.setLayoutManager(layoutManager);
                    viewCommits.setAdapter(viewAdapter);

                    textListTitle.setVisibility(View.VISIBLE);

                }
                else {
                    // This is just a demo. Let's then ignore the list of errors, just say an error occurred.
                    Toast.makeText(context, "An error occurred!", Toast.LENGTH_LONG).show();
                }
            }
        });

        appController.loadRepoData();

    }

    @Override
    public void onItemClick(int position) {

        GitRepoCommitDO commitData = AppController.getGitRepoCommits().get(position);
        Intent intentCommit = new Intent(MainActivity.this, CommitActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("commit_data", commitData);
        intentCommit.putExtras(bundle);
        startActivity(intentCommit);

    }
}
