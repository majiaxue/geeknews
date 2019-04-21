package com.example.administrator.kaoqian;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bt;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            upFileData();
        }else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            upFileData();
        }
    }

    private void upFileData() {
        File file=new File("/storage/emulated/legacy/storage/tu1.png");
        Retrofit retrofit = new Retrofit.Builder().baseUrl(MyServer.URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyServer myServer = retrofit.create(MyServer.class);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);

        MultipartBody.Part formData = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

        RequestBody requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), "h1808a");

        Observable<UpLoadBean> call = myServer.getCall(requestBody1, formData);

        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UpLoadBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UpLoadBean upLoadBean) {
                        if (upLoadBean!=null&&upLoadBean.getCode()==200){
                            Log.d("这是标识",upLoadBean.getData().getUrl());
                            RequestOptions options = RequestOptions.circleCropTransform();
                            Glide.with(MainActivity.this).load(upLoadBean.getData().getUrl()).apply(options).into(img);
                            Toast.makeText(MainActivity.this,upLoadBean.getRes(),Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(MainActivity.this,"上传失败",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    private void initView() {
        bt = (Button) findViewById(R.id.bt);
        img = (ImageView) findViewById(R.id.img);

        bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt:
                    upFileData();
                break;
        }
    }
}
