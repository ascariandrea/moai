package com.seventydivision.framework.utils;

/**
 * Created by andreaascari on 18/03/14.
 */
public class HtmlStringUtils {

    public static final String BOLD_TAG = "<b>";
    public static final String BOLD_TAG_CLOSE = "</b>";

    public static String wrapInTags(String tags, String stringToWrap) {
        String taggedString;
        if (tags.equals(BOLD_TAG)) {
            taggedString = BOLD_TAG.concat(stringToWrap).concat(BOLD_TAG_CLOSE);
        } else {
            taggedString = tags.concat(stringToWrap).concat(tags);
        }

        return taggedString;
    }

    public static String boldString(String string) {
        return BOLD_TAG.concat(string).concat(BOLD_TAG_CLOSE);
    }
}
