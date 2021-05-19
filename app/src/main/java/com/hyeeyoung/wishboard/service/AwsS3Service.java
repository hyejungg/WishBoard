package com.hyeeyoung.wishboard.service;
import android.content.Context;
import android.util.Log;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.File;

public class AwsS3Service {
    private static String BUCKET_NAME = "BUCKET_NAME"; // @param : 생성한 버킷명
    private static String ACCESS_KEY = "ACCESS_KEY"; // @param : IAM에서 생성한 액세스 키
    private static String SECRET_KEY = "SECRET_KEY"; // @param : IAM에서 생성한 시크릿 키

    private AmazonS3 s3_client;
    private AWSCredentials aws_credentials;
    private TransferUtility transferUtility;
    private String TAG = "AwsS3Service";

    // @param : aws S3 client 생성 및 초기화
    public AwsS3Service(Context context) {
        aws_credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        s3_client = new AmazonS3Client(aws_credentials, Region.getRegion(Regions.AP_NORTHEAST_2));
        transferUtility = TransferUtility.builder().s3Client(s3_client).context(context).build();
        TransferNetworkLossHandler.getInstance(context);
    }

    /**
     * @param file : 업로드 할 이미지 파일
     * @param time_key : 저장될 이미지 파일명(식별자), 타임스탬프로 파일명 지정
     */
    public void uploadFile(File file, String time_key) {
        if (s3_client != null) {
            long time_stamp = System.currentTimeMillis();

            TransferObserver uploadObserver = transferUtility.upload(BUCKET_NAME, "wishboard/"+ time_key, file); // @param "wishboard/" : 버킷 내 폴더명
            uploadObserver.setTransferListener(new TransferListener() {
                @Override
                public void onStateChanged(int id, TransferState state) {
                    Log.d(TAG, "onStateChanged: " + id + ", " + state.toString());

                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                    float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                    int percentDone = (int)percentDonef;
                    Log.d(TAG, "ID:" + id + " bytesCurrent: " + bytesCurrent + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
                }

                @Override
                public void onError(int id, Exception ex) {
                    Log.e(TAG, ex.getMessage());
                }
            });
        }
    }
}
