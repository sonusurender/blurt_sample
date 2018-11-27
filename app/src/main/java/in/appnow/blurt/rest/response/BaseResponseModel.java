package in.appnow.blurt.rest.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhishek Thanvi on 29/03/18.
 * Copyright © 2018 Abhishek Thanvi. All rights reserved.
 */


public class BaseResponseModel {

    @SerializedName("err_msg")
    private String errorMsg;

    @SerializedName("err_status")
    private boolean errorStatus;

    public BaseResponseModel() {
    }


    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public boolean isErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(boolean errorStatus) {
        this.errorStatus = errorStatus;
    }


}
