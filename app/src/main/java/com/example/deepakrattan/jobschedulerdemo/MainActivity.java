package com.example.deepakrattan.jobschedulerdemo;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.pm.ComponentInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private RadioGroup rdGrpNetworkType;
    private RadioButton rdBtnDefaultNetwork, rdBtnAnyNetwork, rdBtnWiFiNetwork;
    private Button btnScheduleJob, btnCancelJob;
    private static final int JOB_ID = 0;
    private JobScheduler jobScheduler;
    private Switch switchDeviceIdle, switchDeviceCharging;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //findViewByID
        rdGrpNetworkType = (RadioGroup) findViewById(R.id.rdGrpNetworkType);
        rdBtnDefaultNetwork = (RadioButton) findViewById(R.id.rdBtnDefaultNetwork);
        rdBtnAnyNetwork = (RadioButton) findViewById(R.id.rdBtnAnyNetwork);
        rdBtnWiFiNetwork = (RadioButton) findViewById(R.id.rdBtnWiFiNetwork);
        switchDeviceIdle = (Switch) findViewById(R.id.switchDeviceIdle);
        switchDeviceCharging = (Switch) findViewById(R.id.switchDeviceCharging);
        btnCancelJob = (Button) findViewById(R.id.btnCancelJob);
        btnScheduleJob = (Button) findViewById(R.id.btnScheduleJob);


        //On clicking schedule job button
        //Create the JobScheduler and the JobInfo object
        btnScheduleJob.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

            @Override
            public void onClick(View view) {
                jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
                //get the selected network radio button id
                int selectedNetworkID = rdGrpNetworkType.getCheckedRadioButtonId();
                //Create a selected network option integer variable and
                // set it equal to the default network option (no network required)
                int selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE;
                switch (selectedNetworkID) {
                    case R.id.rdBtnAnyNetwork:
                        selectedNetworkOption = JobInfo.NETWORK_TYPE_ANY;
                        break;
                    case R.id.rdBtnDefaultNetwork:
                        selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE;
                        break;
                    case R.id.rdBtnWiFiNetwork:
                        selectedNetworkOption = JobInfo.NETWORK_TYPE_UNMETERED;
                        break;
                }


                ComponentName componentName = new ComponentName(getPackageName(), NotificationJobService.class.getName());
                JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, componentName)
                        .setRequiredNetworkType(selectedNetworkOption)
                        .setRequiresDeviceIdle(switchDeviceIdle.isChecked())
                        .setRequiresCharging(switchDeviceCharging.isChecked());

                boolean constraintSet = (selectedNetworkOption != JobInfo.NETWORK_TYPE_NONE) || switchDeviceCharging.isChecked() || switchDeviceIdle.isChecked();
                if (constraintSet) {
                    JobInfo jobInfo = builder.build();
                    jobScheduler.schedule(jobInfo);
                    Toast.makeText(MainActivity.this, getString(R.string.job_scheduled), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, "Please set one constraint", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //On clicking cancel Job
        btnCancelJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (jobScheduler != null) {
                    jobScheduler.cancelAll();
                    jobScheduler = null;
                    Toast.makeText(MainActivity.this, "Job cancelled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
