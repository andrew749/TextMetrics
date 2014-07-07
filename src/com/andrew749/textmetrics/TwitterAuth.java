package com.andrew749.textmetrics;

/**
 * Created by andrew on 25/04/14.
 */
public class TwitterAuth {
    public static final String CONSUMER_KEY = "setxnUD8dhi9Z4fJ5MeUC6DIG";
    public static final String CONSUMER_SECRET = "FIKYOzlQacRqawoNcG5Dgz5uUm9x2d5VRYSBYzliLugs3rSKTU";
    public static final String REQUEST_URL = "http://api.twitter.com/oauth/request_token";
    public static final String ACCESS_URL = "http://api.twitter.com/oauth/access_token";
    public static final String AUTHORIZE_URL = "http://api.twitter.com/oauth/authorize";
    public static final String CALLBACK_SCHEME = "x-latify-oauth-twitter";
    public static final String CALLBACK_URL = CALLBACK_SCHEME + "://callback";
    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            //get tweets sent and tweets mentioned in on background thread


        }
    });
    //log the user into twitter and authenticate so that retrieval can take place
    private String OPENID;
    private boolean isAccountConnected = false;

    public TwitterAuth() {
    }
}

