package com.hyeeyoung.wishboard.config;

public interface ResultCode {
    int FROM_CAMERA = 0;
    int FROM_ALBUM = 1;
    int NOTI_RESULT_CODE = 1003; // @param : 사용자가 설정한 알림 정보를 전달 시 사용
    int FOLDER_RESULT_CODE = 1004; // @param : 사용자가 설정한 폴더 정보를 전달 시 사용
}
