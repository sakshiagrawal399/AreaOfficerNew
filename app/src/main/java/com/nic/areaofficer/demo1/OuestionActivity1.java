package com.nic.areaofficer.demo1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.nic.areaofficer.AreaOfficer;
import com.nic.areaofficer.R;
import com.nic.areaofficer.util.ImageProcess;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OuestionActivity1 extends AppCompatActivity {

    EditText one,two,five;
    Button save,update;
    Task task;
    RadioGroup radio_group_one;
    RadioButton three_radio1,three_radio2,three_radio3;
    String group1;
    static Uri imageUri, m_imgUri;
    private static final int CAMERA_REQUEST1 = 1888;
    ImageView image;
    Bitmap bitmap;
    String image1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ouestion1);

        one=(EditText)findViewById(R.id.one);
        two=(EditText)findViewById(R.id.two);
        five=(EditText)findViewById(R.id.five);

        save=(Button)findViewById(R.id.save);
        update=(Button)findViewById(R.id.update);

        three_radio1=(RadioButton)findViewById(R.id.three_radio1);
        three_radio2=(RadioButton)findViewById(R.id.three_radio2);
        three_radio3=(RadioButton)findViewById(R.id.three_radio3);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTask(task);
            }
        });

        radio_group_one=(RadioGroup)findViewById(R.id.radio_group_one);

        radio_group_one.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i) {
                    case R.id.three_radio1:

                        group1="yes";

                        break;
                    case R.id.three_radio2:

                        group1="no";

                        break;
                    case R.id.three_radio3:

                        group1="not aware";

                        break;
                }
            }
        });

        getTasks();




    }

    private static Uri getImageUri() {
        m_imgUri = null;
        File m_file;
        try {
            SimpleDateFormat m_sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String m_curentDateandTime = m_sdf.format(new Date());
            String m_imagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + m_curentDateandTime + ".jpg";
            m_file = new File(m_imagePath);
            m_imgUri = Uri.fromFile(m_file);
        } catch (Exception p_e) {
        }
        return m_imgUri;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            if (requestCode == CAMERA_REQUEST1 && resultCode == Activity.RESULT_OK) {
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Uri imageUri = m_imgUri;
                File file = new File(imageUri.getPath());
                bitmap= BitmapFactory.decodeFile(file.getAbsolutePath(), bmOptions);
                bitmap = getResizedBitmap(bitmap, 500);
                image1= ImageProcess.encode(bitmap);
                image.setImageBitmap(bitmap);
                //file.delete();
            /*QuestionReAdapter questionAdapter = new QuestionReAdapter(QuestionActivity1.this, arrayList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(questionAdapter);*/
            }else {

            }

        }catch (Exception e){

        }

    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void loadTask(Task task) {
        try {
            one.setText(task.getTask());
            two.setText(task.getDesc());
            five.setText(task.getFinishBy());

            if (task.getGroup1().equals("yes")){
                three_radio1.setChecked(true);
            }else if (task.getGroup1().equals("no")){
                three_radio2.setChecked(true);
            }else {
                three_radio3.setChecked(true);
            }
            image.setImageBitmap(ImageProcess.decode(task.getImage()));
        }catch (Exception e){


        }

       // checkBoxFinished.setChecked(task.isFinished());
    }

    private void getTasks() {
        class GetTasks extends AsyncTask<Void, Void, List<Task>> {

            @Override
            protected List<Task> doInBackground(Void... voids) {
                List<Task> taskList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .taskDao()
                        .getAll();
                return taskList;
            }

            @Override
            protected void onPostExecute(List<Task> tasks) {
                super.onPostExecute(tasks);
                //TasksAdapter adapter = new TasksAdapter(MainActivity.this, tasks);
                //recyclerView.setAdapter(adapter);
                //tasks = (Task) getIntent().getSerializableExtra("task");
               // Task task = tasks.get();
                for (int i=0;i<=tasks.size()-1;i++){

                    try {
                        Bundle bundle = getIntent().getExtras();
                        String loc=bundle.getString("position");
                        String location=tasks.get(i).getLocation();
                        if (location.equals(loc)){
                            loadTask(tasks.get(i));
                            task = tasks.get(bundle.getInt("position"));
                            update.setVisibility(View.VISIBLE);
                            save.setVisibility(View.GONE);
                        }else {

                        }


                    }catch (Exception e){


                        save.setVisibility(View.VISIBLE);
                        update.setVisibility(View.GONE);
                    }


                }


            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }



    private void saveTask() {
        final String sTask = one.getText().toString().trim();
        final String sDesc = two.getText().toString().trim();
        final String sFinishBy = five.getText().toString().trim();
        final Bundle bundle = getIntent().getExtras();


        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //creating a task
                Task task = new Task();
                task.setTask(sTask);
                task.setDesc(sDesc);
                task.setFinishBy(sFinishBy);
                task.setGroup1(group1);
                task.setImage(image1);
                task.setLocation((bundle.getString("position")));
                task.setLevels(bundle.getString("levels"));

                task.setImage(AreaOfficer.getPreferenceManager().getPanchayatCode());
                task.setFinished(false);

                //adding to database
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .insert(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
               // startActivity(new Intent(getApplicationContext(), LevelsActivity.class));
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }

        SaveTask st = new SaveTask();
        st.execute();
    }


    private void updateTask(final Task task) {
        final String sTask = one.getText().toString().trim();
        final String sDesc = two.getText().toString().trim();
        final String sFinishBy = five.getText().toString().trim();



        class UpdateTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                task.setTask(sTask);
                task.setDesc(sDesc);
                task.setFinishBy(sFinishBy);
                task.setGroup1(group1);

                task.setImage(image1);
               // task.setFinished(checkBoxFinished.isChecked());
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .update(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
               // startActivity(new Intent(getApplicationContext(), LevelsActivity.class));
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
               // finish();
               // startActivity(new Intent(UpdateTaskActivity.this, MainActivity.class));
            }
        }

        UpdateTask ut = new UpdateTask();
        ut.execute();
    }

}

