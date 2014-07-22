package com.seventydivision.framework.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import com.seventydivision.framework.R;
import com.seventydivision.framework.activities.MainActivity;
import com.seventydivision.framework.models.FacebookUser;
import com.seventydivision.framework.models.ModelCollection;
import com.seventydivision.framework.persist.PersistentPreferences;
import com.seventydivision.framework.views.PoweredImageView;
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

    public static String GRAPH_ENDPOINT = "http://graph.facebook.com";

    public static final String PARAM_NAME = "name";
    public static final String PARAM_CAPTION = "caption";
    public static final String PARAM_DESC = "description";
    public static final String PARAM_LINK = "link";
    public static final String PARAM_PICTURE = "picture";
    public static final String PARAM_MESSAGE = "message";

    public static final int ONLY_USERS_WITH_APP = 1;
    public static final int ONLY_USERS_WITHOUT_APP = 2;
    public static final int BOTH_USERS = 3;


    private static int mToastMessageResource = android.R.string.cancel;


    private static String TAG = FacebookUtils.class.getSimpleName();



    public static void getMeProfilePicture(Context context, int size, ImageRequestCallback callback) {
        getProfilePicture(context, "me", size, callback);
    }

    public static void getProfilePicture(Context context, String fbUserId, ImageRequestCallback callback) {
        getProfilePicture(context, fbUserId, 50, callback);
    }

    public static void getProfilePicture(final Context context, final String fbUserId, int size, final ImageRequestCallback callback) {
        if (context != null) {
            PersistentPreferences preferences = ((MainActivity) context).getPrefs();
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
                                    PersistentPreferences preferences = ((MainActivity) context).getPrefs();
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

    public static void loadProfilePictureInto(final Context mContext, String fbId, final ImageView targetView) {
        FacebookUtils.getProfilePicture(mContext, fbId, new ImageRequestCallback() {
            @Override
            public void onSuccess(String url) {
                Picasso.with(mContext).load(url).into(targetView);
            }
        });
    }
    public static void loadProfilePictureInto(MainActivity mContext, String fbUserId, int i, final PoweredImageView targetView) {
        FacebookUtils.getProfilePicture(mContext, fbUserId, i, new ImageRequestCallback() {
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
                    toastMessageResource = R.string.com_facebook_choose_friends;
                    Utils.Views.showLongToast(activity, toastMessageResource);

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



    public static class OnPostCallback implements FacebookDialog.Callback {

        private final Activity mActivity;

        public OnPostCallback(Activity activity) {
            super();
            mActivity = activity;
        }

        @Override
        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
            if (data != null && FacebookDialog.getNativeDialogDidComplete(data) && FacebookDialog.getNativeDialogCompletionGesture(data).equals("post")) {
//                mToastMessageResource = 0; //R.string.canceled_fb_post_creation
                mActivity.finish();
            } else {
//                mToastMessageResource = R.string.successfull_fb_post_creation;

            }

            Utils.Views.showLongToast(mActivity, mToastMessageResource);


        }

        @Override
        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
            if (error instanceof FacebookOperationCanceledException) {
                //toastMessageResource = R.string.canceled_fb_post_creation;
            } else {
                //toastMessageResource = R.string.error_fb_post_creation;
            }
            Utils.Views.showLongToast(mActivity, android.R.string.cancel);
        }
    }

    private static boolean isValidFbRes(Response response) {
        return (response != null && response.getGraphObject() != null) == true;
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

}
