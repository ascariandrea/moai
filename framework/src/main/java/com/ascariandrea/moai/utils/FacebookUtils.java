package com.ascariandrea.moai.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.ascariandrea.moai.activities.MoaiFragmentActivity;
import com.ascariandrea.moai.models.FacebookUser;
import com.ascariandrea.moai.models.ModelCollection;
import com.ascariandrea.moai.persist.PersistentPreferences;
import com.ascariandrea.moai.views.PoweredImageView;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by andreaascari on 28/01/14.
 */
public class FacebookUtils {

    public static final String PARAM_NAME = "name";
    public static final String PARAM_CAPTION = "caption";
    public static final String PARAM_DESC = "description";
    public static final String PARAM_LINK = "link";
    public static final String PARAM_PICTURE = "picture";
    public static final String PARAM_MESSAGE = "message";
    public static final int ONLY_USERS_WITH_APP = 1;
    public static final int ONLY_USERS_WITHOUT_APP = 2;
    public static final int BOTH_USERS = 3;
    public static String GRAPH_ENDPOINT = "http://graph.facebook.com";
    private static int mToastMessageResource = android.R.string.cancel;


    private static String TAG = FacebookUtils.class.getSimpleName();


    public static void getMe(final FacebookUserCallback facebookUserCallback) {
        getMe(null, facebookUserCallback);
    }

    public static void getMe(Bundle params, final FacebookUserCallback facebookUserCallback) {
        if (params == null) {
            params = new Bundle();
            params.putString("fields", "picture.width(800).height(800),about,first_name,last_name,email,address,age_range");
        }

        new Request(
                Session.getActiveSession(),
                "/me",
                params,
                HttpMethod.GET,
                new Request.Callback() {
                    @Override
                    public void onCompleted(Response response) {
                        if (response != null) {
                            if (response.getError() != null)
                                facebookUserCallback.onError(response.getError(), response);
                            else if (response.getGraphObjectAs(GraphUser.class) != null)
                                facebookUserCallback.onSuccess(response.getGraphObjectAs(GraphUser.class));
                            else
                                facebookUserCallback.onError(response.getError(), response);
                        } else
                            facebookUserCallback.onError(response.getError(), null);
                    }

                }).executeAsync();
    }

    public static void getMeProfilePicture(Context context, int size, ImageRequestCallback callback) {
        getProfilePicture(context, "me", size, callback);
    }

    public static void getProfilePicture(Context context, String fbUserId, ImageRequestCallback callback) {
        getProfilePicture(context, fbUserId, 50, callback);
    }

    public static void getProfilePicture(final Context context, final String fbUserId, int size, final ImageRequestCallback callback) {
        if (context != null) {
            PersistentPreferences preferences = ((MoaiFragmentActivity) context).getPrefs();
            if (!preferences.getFbImage(fbUserId, size).equals("{}")) {
                callback.onSuccess(preferences.getFbImage(fbUserId, size));
            }
        }
        getProfileImageReq(context, fbUserId, size, callback);
    }

    public static String getProfilePictureUrl(String fbUserId) {
        if (fbUserId != null)
            return GRAPH_ENDPOINT.concat("/").concat(fbUserId).concat("/").concat("picture?width=800&height=800");
        return "";
    }

    private static void getProfileImageReq(final Context context, final String fbUserId, final int size, final ImageRequestCallback callback) {
        String path = fbUserId + "/picture";
        Bundle params = new Bundle();
        params.putBoolean("redirect", false);
        params.putInt("width", size);
        params.putInt("height", size);

        Request request = new Request(
                Session.getActiveSession(),
                path,
                params,
                HttpMethod.GET,
                new Request.Callback() {
                    @Override
                    public void onCompleted(Response response) {
                        if (response != null && response.getGraphObject() != null) {
                            try {
                                JSONObject responseData = response.getGraphObject().getInnerJSONObject().getJSONObject("data");
                                String fbAvatar = responseData.getString("url");
                                Log.d(TAG, fbAvatar);
                                if (context != null) {
                                    PersistentPreferences preferences = ((MoaiFragmentActivity) context).getPrefs();
                                    if (preferences.getFbImage(fbUserId, size).equals("{}")) {
                                        preferences.saveFbImage(fbUserId, size, fbAvatar);
                                    }
                                }
                                callback.onSuccess(fbAvatar);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            callback.onSuccess(null);
                        }
                    }
                }
        );
        request.setVersion("v1.0");
        request.executeAsync();
    }

    public static void loadProfilePictureInto(final Context context, String fbId, final PoweredImageView targetView) {
        FacebookUtils.getProfilePicture(context, fbId, new ImageRequestCallback() {
            @Override
            public void onSuccess(String url) {
                Picasso.with(context).load(url).into((com.squareup.picasso.Target) targetView);
            }
        });
    }

    public static void loadProfilePictureInto(Context context, String fbUserId, int i, final PoweredImageView targetView) {
        FacebookUtils.getProfilePicture(context, fbUserId, i, new ImageRequestCallback() {
            @Override
            public void onSuccess(String url) {
                targetView.setImageUrl(url);
            }
        });
    }

    public static void getUserFriends(final PersistentPreferences preferences, final int usersType, final FriendsRequestCallback callback) {
        String path = "me/friends";
        Bundle params = new Bundle();

        if (preferences != null && preferences.getFbFriends() != null && !preferences.getFbFriends().equals("{}"))
            callback.onSuccess(new ModelCollection<FacebookUser>("data").fromJSONList(preferences.getFbFriends(), FacebookUser.class));

        params.putString("fields", "first_name,last_name,name,installed,picture.type(large)");

        new Request(
                Session.getActiveSession(),
                path,
                params,
                HttpMethod.GET,
                new Request.Callback() {
                    @Override
                    public void onCompleted(Response response) {
                        if (isValidFbRes(response)) {
                            JSONObject friendsList = response.getGraphObject().getInnerJSONObject();
                            preferences.saveFbFriends(friendsList.toString());
                            List<FacebookUser> tmpFacebookUsers = new ModelCollection<FacebookUser>("data").fromJSONList(friendsList.toString(), FacebookUser.class);
                            List<FacebookUser> finalFacebookUsers = new ArrayList<FacebookUser>();
                            if (usersType == ONLY_USERS_WITH_APP) {
                                for (FacebookUser fbUser : tmpFacebookUsers) {
                                    if (fbUser.installed) {
                                        finalFacebookUsers.add(fbUser);
                                    }
                                }
                            } else if (usersType == ONLY_USERS_WITHOUT_APP) {
                                for (FacebookUser fbUser : tmpFacebookUsers) {
                                    if (!fbUser.installed)
                                        finalFacebookUsers.add(fbUser);
                                }
                            } else {
                                Collections.sort(tmpFacebookUsers, new Comparator<FacebookUser>() {
                                    @Override
                                    public int compare(FacebookUser lhs, FacebookUser rhs) {
                                        if (lhs.installed && !rhs.installed) {
                                            return -1;
                                        }
                                        if (!lhs.installed && rhs.installed) {
                                            return +1;
                                        }
                                        return 0;
                                    }
                                });
                                finalFacebookUsers = tmpFacebookUsers;
                            }

                            callback.onSuccess(finalFacebookUsers);
                        } else {
                            Log.d(TAG, response.toString());
                            callback.onFailure(response);
                        }
                    }
                }
        ).executeAsync();
    }


    public static void postOnUserWall(final Activity activity, UiLifecycleHelper uiHelper, List<String> fbUsers, Bundle params) {
        if (FacebookDialog.canPresentShareDialog(activity.getApplicationContext(), FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
            if (fbUsers == null) fbUsers = new ArrayList<String>();

            FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(activity)
                    .setLink(params.getString(PARAM_LINK))
                    .setName(params.getString(PARAM_NAME))
                    .setCaption(params.getString(PARAM_CAPTION))
                    .setDescription(params.getString(PARAM_DESC))
                    .setPicture(params.getString(PARAM_PICTURE))
                    .setFriends(fbUsers)
                    .build();
            uiHelper.trackPendingDialogCall(shareDialog.present());
        } else {
            WebDialog.FeedDialogBuilder feedDialog = new WebDialog.FeedDialogBuilder(activity, Session.getActiveSession(), params).setOnCompleteListener(new WebDialog.OnCompleteListener() {
                @Override
                public void onComplete(Bundle values, FacebookException error) {
                    int toastMessageResource;
                    if (error == null) {
                        final String postId = values.getString("post_id");
                        if (postId != null) {
                            //toastMessageResource = R.string.successfull_fb_post_creation;
                        } else {
                            //  toastMessageResource = R.string.canceled_fb_post_creation;
                        }
                    } else if (error instanceof FacebookOperationCanceledException) {
                        //toastMessageResource = R.string.canceled_fb_post_creation;
                    } else {
                        //toastMessageResource = com.R.string.error_fb_post_creation;
                    }
                    Utils.Toast.showLongToast(activity, "Choose Friends");

                }
            });

            feedDialog.build().show();
        }
    }


    public static List<FacebookUser> filterFacebookUsers(List<FacebookUser> tmpFacebookUsers, int usersType) {
        List<FacebookUser> finalFacebookUsers = new ArrayList<FacebookUser>();
        if (usersType == ONLY_USERS_WITH_APP) {
            for (FacebookUser fbUser : tmpFacebookUsers) {
                if (fbUser.installed) {
                    finalFacebookUsers.add(fbUser);
                }
            }
        } else if (usersType == ONLY_USERS_WITHOUT_APP) {
            for (FacebookUser fbUser : tmpFacebookUsers) {
                if (!fbUser.installed)
                    finalFacebookUsers.add(fbUser);
            }
        } else {
            Collections.sort(tmpFacebookUsers, new Comparator<FacebookUser>() {
                @Override
                public int compare(FacebookUser lhs, FacebookUser rhs) {
                    if (lhs.installed && !rhs.installed) {
                        return -1;
                    }
                    if (!lhs.installed && rhs.installed) {
                        return +1;
                    }
                    return 0;
                }
            });
            finalFacebookUsers = tmpFacebookUsers;
        }

        return finalFacebookUsers;
    }

    private static boolean isValidFbRes(Response response) {
        return (response != null && response.getGraphObject() != null);
    }

    public interface RequestCallback {

        public void onSuccess(JSONObject obj);
    }


    public interface ImageRequestCallback {
        public void onSuccess(String url);
    }

    public interface FriendsRequestCallback {
        public void onSuccess(List<FacebookUser> friends);

        void onFailure(Response response);
    }

    public interface FacebookUserCallback {
        public void onSuccess(GraphUser fbUser);

        public void onError(FacebookRequestError error, Response response);
    }

    public static abstract class OnPostCallback implements FacebookDialog.Callback {

        @Override
        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
            if (data != null) {
                Log.d(TAG, data + "");
                if (FacebookDialog.getNativeDialogDidComplete(data)) {
                    if (FacebookDialog.getNativeDialogCompletionGesture(data).equals("post")) {
                        onSuccess(pendingCall, data);
                    } else if (FacebookDialog.getNativeDialogCompletionGesture(data).equals("cancel")) {
                        onCancel(pendingCall, data);
                    }
                }
            }

        }

        public abstract void onCancel(FacebookDialog.PendingCall pendingCall, Bundle data);

        public abstract void onSuccess(FacebookDialog.PendingCall pendingCall, Bundle data);

        @Override
        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
            if (error instanceof FacebookOperationCanceledException) {
                //toastMessageResource = R.string.canceled_fb_post_creation;
            } else {
                //toastMessageResource = R.string.error_fb_post_creation;
            }
        }
    }

}
