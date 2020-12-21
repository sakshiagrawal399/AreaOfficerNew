package com.nic.areaofficer.demo2;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.nic.areaofficer.R;
import com.nic.areaofficer.question.QuestionActivity;
import com.nic.areaofficer.util.ImageProcess;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OuestionActivity2 extends AppCompatActivity {

    EditText one,two,five;
    Button save,update;
    Task2 Task2;
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
        setContentView(R.layout.activity_ouestion2);

        one=(EditText)findViewById(R.id.one);
        two=(EditText)findViewById(R.id.two);
        five=(EditText)findViewById(R.id.five);

        save=(Button)findViewById(R.id.save);
        update=(Button)findViewById(R.id.update);

        image=findViewById(R.id.image);

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
                updateTask(Task2);
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent m_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imageUri = getImageUri();
                m_intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(m_intent, CAMERA_REQUEST1);
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



        /*Task2 Task2 = taskList.get(getAdapterPosition());

        Intent intent = new Intent(mCtx, UpdateTaskActivity.class);
        intent.putExtra("Task2", Task2);

        mCtx.startActivity(intent);*/



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

    private void loadTask(Task2 Task2) {
        try {
            one.setText(Task2.getTask());
            two.setText(Task2.getDesc());
            five.setText(Task2.getFinishBy());
            image.setImageBitmap(ImageProcess.decode(Task2.getImage()));
            if (Task2.getGroup1().equals("yes")){
                three_radio1.setChecked(true);
            }else if (Task2.getGroup1().equals("no")){
                three_radio2.setChecked(true);
            }else {
                three_radio3.setChecked(true);
            }

        }catch (Exception e){

        }

       // checkBoxFinished.setChecked(Task2.isFinished());
    }

    private void getTasks() {
        class GetTasks extends AsyncTask<Void, Void, List<Task2>> {

            @Override
            protected List<Task2> doInBackground(Void... voids) {
                List<Task2> taskList = DatabaseClient2
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .taskDao2()
                        .getAll();
                return taskList;
            }

            @Override
            protected void onPostExecute(List<Task2> tasks) {
                super.onPostExecute(tasks);
                //TasksAdapter adapter = new TasksAdapter(MainActivity.this, tasks);
                //recyclerView.setAdapter(adapter);
                //tasks = (Task2) getIntent().getSerializableExtra("Task2");
               // Task2 Task2 = tasks.get();

                try {
                    Bundle bundle = getIntent().getExtras();
                    loadTask(tasks.get(bundle.getInt("position")));
                    Task2 = tasks.get(bundle.getInt("position"));
                    update.setVisibility(View.VISIBLE);
                    save.setVisibility(View.GONE);
                }catch (Exception e){


                    save.setVisibility(View.VISIBLE);
                    update.setVisibility(View.GONE);
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



        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //creating a Task2
                Task2 Task2 = new Task2();
                Task2.setTask(sTask);
                Task2.setDesc(sDesc);
                Task2.setFinishBy(sFinishBy);
                Task2.setGroup1(group1);

                Task2.setImage(image1);

                Task2.setFinished(false);

                //adding to database
                DatabaseClient2.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao2()
                        .insert(Task2);
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


    private void updateTask(final Task2 Task2) {
        final String sTask = one.getText().toString().trim();
        final String sDesc = two.getText().toString().trim();
        final String sFinishBy = five.getText().toString().trim();



        class UpdateTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                Task2.setTask(sTask);
                Task2.setDesc(sDesc);
                Task2.setFinishBy(sFinishBy);
                Task2.setGroup1(group1);
                Task2.setImage(image1);
               // Task2.setFinished(checkBoxFinished.isChecked());
                DatabaseClient2.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao2()
                        .update(Task2);
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

