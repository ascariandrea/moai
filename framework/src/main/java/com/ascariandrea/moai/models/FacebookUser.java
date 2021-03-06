package com.ascariandrea.moai.models;

import com.ascariandrea.moai.utils.FacebookUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Created by andreaascari on 22/01/14.
 */

@JsonRootName("facebookUser")
public class FacebookUser extends Model {

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

    @JsonProperty("birthday")
    public String birthday;

    @JsonProperty("picture")
    public ProfilePicture picture;

    @JsonProperty("statusMessage")
    public String statusMessage;

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

        return FacebookUtils.GRAPH_ENDPOINT + "/" + this.id + "/picture?width=" + size + "&height=" + size;
    }

    public class ProfilePicture extends Model {

        @JsonProperty("data")
        public Data data;

        public class Data extends Model {

            @JsonProperty("url")
            public String url;

        }

    }

}
