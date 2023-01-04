package com.elm.healthcareapp.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.service.controls.templates.ThumbnailTemplate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elm.healthcareapp.R;
import com.elm.healthcareapp.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class LungFragment extends Fragment {
    TextView daig,title;
    AppCompatButton upload;
    ImageView mriImage;
    int imageSize = 224;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_lung, container, false);
        daig = view.findViewById(R.id.diagn_txt);
        upload = view.findViewById(R.id.uploadImage);
        mriImage = view.findViewById(R.id.mriImage);
        title = view.findViewById(R.id.title_txt);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getContext().checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
                        Intent camer = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(camer,1);
                    }else{
                        requestPermissions(new String[]{Manifest.permission.CAMERA},100);
                    }
                }

            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode ==1 && resultCode ==RESULT_OK){
            Bitmap image =(Bitmap) data.getExtras().get("data");
            int dimension = Math.min(image.getWidth(),image.getHeight());
            image= ThumbnailUtils.extractThumbnail(image,dimension,dimension);
            mriImage.setImageBitmap(image);
            title.setVisibility(View.VISIBLE);
            daig.setVisibility(View.VISIBLE);

            image = Bitmap.createScaledBitmap(image,imageSize,imageSize,false);
            classifyImage(image);
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    private void classifyImage(Bitmap image) {
        try{
            Model model = Model.newInstance(getContext());
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1,224,224,3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4*imageSize * imageSize*3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValue = new int[imageSize * imageSize];
            image.getPixels(intValue,0,image.getWidth(),0,0,image.getWidth(),image.getHeight());

            int pixel =0;
            for (int i =0; i<imageSize ; i++){
                for(int j =0 ; j<imageSize ;j++){
                    int val = intValue[pixel++];
                    byteBuffer.putFloat(((val>>16)& 0xFF)*(1.f/255.f));
                    byteBuffer.putFloat(((val>>8)& 0xFF)*(1.f/255.f));
                    byteBuffer.putFloat( (val & 0xFF) * ( 1.f/255.f));



                }
            }
            inputFeature0.loadBuffer(byteBuffer);
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidence = outputFeature0.getFloatArray();

            int maxPos =0;
            float maxConfidence =0;
            for (int i = 0 ; i <confidence.length ; i++){
                if (confidence[i]>maxConfidence){
                    maxConfidence = confidence[i];
                    maxPos = i;
                }
            }
            String[]classes={"Normal","PNEUMONIA VIRUS","PNEUMONIA BACTERIAL"};
            daig.setText(classes[maxPos]);

            model.close();
        }catch (IOException e){
            Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
        }

    }
}