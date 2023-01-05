package com.shamsaha.app.activity.CallHelper.videocall;

import com.google.gson.annotations.Expose;

public class VideoNotificationBody {
    @Expose
    private String invite_id;
    @Expose
    private String message;
    @Expose
    private String room_id;
    @Expose
    private String type;

    public VideoNotificationBody(String invite_id, String message, String room_id, String type) {
        this.invite_id = invite_id;
        this.message = message;
        this.room_id = room_id;
        this.type = type;
    }

    public String getInvite_id() {
        return invite_id;
    }

    public String getMessage() {
        return message;
    }

    public String getRoom_id() {
        return room_id;
    }

    public String getType() {
        return type;
    }
}
