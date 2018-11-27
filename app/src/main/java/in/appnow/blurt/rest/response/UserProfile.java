package in.appnow.blurt.rest.response;

import com.google.gson.annotations.SerializedName;

import in.appnow.blurt.rest.request.UserAvailabilityRequest;

public class UserProfile extends UserAvailabilityRequest {

    @SerializedName("name")
    private String name;

    @SerializedName("uid")
    private String userId;

    @SerializedName("status")
    private String userStatus;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public static final String USER_FOUND = "UF";
    public static final String NEW_USER = "NU";
}