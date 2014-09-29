package com.ascariandrea.afw.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.ascariandrea.afw.utils.FacebookUtils;

/**
 * Created by andreaascari on 22/01/14.
 */

@JsonRootName("facebookUser")
public class FacebookUser extends BaseModel {

    public static String SINGLE_NAME = "user";
    public static String PLURAL_NAME = "users";

    @JsonProperty("id")
    public String id;

    @JsonProperty("name")
    public String name;

    @JsonProperty("first_name")
    public String firstName;

    @JsonProperty("last_name")
    public String lastName;

    @JsonProperty("installed")
    public boolean installed;

    @JsonProperty("picture")
    public ProfilePicture picture;

    @JsonProperty("statusMessage")
    public String statusMessage;

    public class ProfilePicture extends BaseModel {

        @JsonProperty("data")
        public Data data;

        public class Data extends BaseModel {

            @JsonProperty("url")
            public String url;

        }

    }

    @JsonIgnore
    public String getDisplayName() {
        if (this.firstName != null && this.lastName != null)
            return this.firstName.concat(" ").concat(this.lastName);
        else
            return this.name;
    }

    @JsonIgnore
    public String getProfilePictureUrl() {
        return getProfilePictureUrl(-1);
    }

    @JsonIgnore
    public String getProfilePictureUrl(int size) {
        if (this.picture != null && this.picture.data != null && this.picture.data.url != null && size == -1)
            return this.picture.data.url;

        return FacebookUtils.GRAPH_ENDPOINT + "/" + this.id + "/picture?width=" + size +"&height=" + size ;
    }

}
