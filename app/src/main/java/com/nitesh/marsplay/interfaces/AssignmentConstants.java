package com.nitesh.marsplay.interfaces;

public interface AssignmentConstants {

    interface Api {
        String CLOUDINARY_MARSPLAY_FOLDER_IMAGES_ENDPOINT
                = "https://api.cloudinary.com/v1_1/niteshjain/resources/image";

        String AUTHORIZATION = "Authorization";
        String BASIC_AUTHORIZATION_SECRET_KEY = "Basic OTg2NTY4ODQ4ODYxNTQ1OnVUUFhKLXd1V2xBRWI3a1F5MTFsT2NPWnlBWQ==";
    }

    interface EventCenter {
        int NO_INTERNET_CONNECTION = 0;

        int IMAGE_UPLOADED_SUCCESSFULLY = 100;
        int UNABLE_TO_UPLOAD_IMAGE = 101;


        int NO_IMAGE_UPLOADED_YET = 102;
        int GET_ALL_IMAGE_SUCCESSFUL = 103;
        int UNABLE_TO_GET_ALL_IMAGES = 104;

        interface ClickEvents {
            int ON_UPLOADED_IMAGE_CLICK = 500;
        }
    }

    interface RequestCodes {
        int PICK_IMAGE_FROM_GALLERY_OR_CAMERA = 1000;
        int UPLOAD_IMAGE_TO_CLOUDINARY = 1001;
    }

    String RESOURCE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
}
