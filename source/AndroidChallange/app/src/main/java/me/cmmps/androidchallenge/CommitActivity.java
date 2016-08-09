package me.cmmps.androidchallenge;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.content.Intent;
import android.net.Uri;

import me.cmmps.androidchallenge.models.GitRepoCommitDO;

public class CommitActivity extends Activity {

    private GitRepoCommitDO commitData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commit);

        if (savedInstanceState!=null) {
            // Recover data
            commitData = (GitRepoCommitDO) savedInstanceState.getSerializable("commit_data");
        }
        else {
            // Retrieve data from intent
            Bundle bundle = getIntent().getExtras();
            commitData = (GitRepoCommitDO) bundle.getSerializable("commit_data");
        }

        ((TextView) findViewById(R.id.textCommitSha)).setText(commitData.getSha());
        ((TextView) findViewById(R.id.textCommitAuthorName)).setText(commitData.getAuthorName());
        ((TextView) findViewById(R.id.textCommitAuthorDate)).setText(commitData.getAuthorDate());
        ((TextView) findViewById(R.id.textCommitCommitterName)).setText(commitData.getCommiterName());
        ((TextView) findViewById(R.id.textCommitCommitterDate)).setText(commitData.getCommiterDate());
        ((TextView) findViewById(R.id.textCommitMessage)).setText(commitData.getMessage());

        Button buttonWebSite = (Button) findViewById(R.id.buttonWebSite);
        buttonWebSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Intent.ACTION_VIEW,Uri.parse(commitData.getHtmlURL()));
                startActivity(intent);
            }
        });

        Button buttonGoBack = (Button) findViewById(R.id.buttonGoBack);
        buttonGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("commit_data", commitData);
        super.onSaveInstanceState(outState);
    }
}
