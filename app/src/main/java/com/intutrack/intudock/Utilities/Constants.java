package com.intutrack.intudock.Utilities;

public class Constants {
    public static final String BRDCST_LOGOUT = "BRDLOGOUT";

    public static final String NAV_MAP = "Map";

    public static final int CAMERA_REQUEST = 201;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 112;
    public static final int REQUEST_TAKE_PHOTO = 101;
    public static final int GET_FROM_GALLERY = 385;
    public static final int INK_REQUEST = 185;
    public static final int INK_RESPONSE = 186;
    public static final int AUTO_START_REQ_CODE = 11221;
    public static final String KEY_BITMAP = null;
    public static boolean isDebug = true;
    public static String VOLLEY_TAG = "VOLLEY RESPONSE";
    public static String VOLLEY_REQUEST = "VOLLEY REQUEST";
    public static String VOLLEY_ERROR = "VOLLEY ERROR";

    public static final String NAV_HOME = "NAV_HOME";
    public static final String NAV_LOGOUT = "NAV_LOGOUT";

    public static final int ACTION_CONSENT = 121102;
    public static final int ACTION_LOCATION = 451256;
    public static final int ACTION_START_TRIP = 625326;
    public static final int ACTION_STOP_TRIP = 745156;
    public static final int ALARAM_REQ_CODE = 12321;

    public static final String EVENT_SCREEN_TIME = "SCREEN_TIME";
    public static final String EVENT_BUTTON_CLICK = "BUTTON_CLICK";
    public static final String EVENT_GPS_CHANGED = "GPS_CHANGE";
    public static final String EVENT_TEST = "TEST";

    public static final String ACTION_LOGOUT = "LOGOUT";

    public static final int SLOT_DURATION = 30;

    public static String MAP_STYLE = "[\n" +
            "  {\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#212121\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"elementType\": \"labels.icon\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"visibility\": \"off\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#757575\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"elementType\": \"labels.text.stroke\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#212121\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"administrative\",\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#757575\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"administrative.country\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#9e9e9e\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"administrative.locality\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#bdbdbd\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"poi\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#757575\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"poi.business\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"visibility\": \"off\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"poi.park\",\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#181818\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"poi.park\",\n" +
            "    \"elementType\": \"labels.text\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"visibility\": \"off\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"poi.park\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#616161\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"poi.park\",\n" +
            "    \"elementType\": \"labels.text.stroke\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#1b1b1b\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"road\",\n" +
            "    \"elementType\": \"geometry.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#2c2c2c\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"road\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#8a8a8a\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"road.arterial\",\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#373737\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"road.highway\",\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#3c3c3c\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"road.highway.controlled_access\",\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#4e4e4e\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"road.local\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#616161\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"transit\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#757575\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"water\",\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#000000\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"water\",\n" +
            "    \"elementType\": \"labels.text.fill\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"color\": \"#3d3d3d\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "]";

   public static String MAP_LIGHT = "[\n" +
           "  {\n" +
           "    \"elementType\": \"geometry\",\n" +
           "    \"stylers\": [\n" +
           "      {\n" +
           "        \"color\": \"#f5f5f5\"\n" +
           "      }\n" +
           "    ]\n" +
           "  },\n" +
           "  {\n" +
           "    \"elementType\": \"labels.icon\",\n" +
           "    \"stylers\": [\n" +
           "      {\n" +
           "        \"visibility\": \"off\"\n" +
           "      }\n" +
           "    ]\n" +
           "  },\n" +
           "  {\n" +
           "    \"elementType\": \"labels.text.fill\",\n" +
           "    \"stylers\": [\n" +
           "      {\n" +
           "        \"color\": \"#616161\"\n" +
           "      }\n" +
           "    ]\n" +
           "  },\n" +
           "  {\n" +
           "    \"elementType\": \"labels.text.stroke\",\n" +
           "    \"stylers\": [\n" +
           "      {\n" +
           "        \"color\": \"#f5f5f5\"\n" +
           "      }\n" +
           "    ]\n" +
           "  },\n" +
           "  {\n" +
           "    \"featureType\": \"administrative.land_parcel\",\n" +
           "    \"elementType\": \"labels.text.fill\",\n" +
           "    \"stylers\": [\n" +
           "      {\n" +
           "        \"color\": \"#bdbdbd\"\n" +
           "      }\n" +
           "    ]\n" +
           "  },\n" +
           "  {\n" +
           "    \"featureType\": \"poi\",\n" +
           "    \"elementType\": \"geometry\",\n" +
           "    \"stylers\": [\n" +
           "      {\n" +
           "        \"color\": \"#eeeeee\"\n" +
           "      }\n" +
           "    ]\n" +
           "  },\n" +
           "  {\n" +
           "    \"featureType\": \"poi\",\n" +
           "    \"elementType\": \"labels.text.fill\",\n" +
           "    \"stylers\": [\n" +
           "      {\n" +
           "        \"color\": \"#757575\"\n" +
           "      }\n" +
           "    ]\n" +
           "  },\n" +
           "  {\n" +
           "    \"featureType\": \"poi.park\",\n" +
           "    \"elementType\": \"geometry\",\n" +
           "    \"stylers\": [\n" +
           "      {\n" +
           "        \"color\": \"#e5e5e5\"\n" +
           "      }\n" +
           "    ]\n" +
           "  },\n" +
           "  {\n" +
           "    \"featureType\": \"poi.park\",\n" +
           "    \"elementType\": \"labels.text.fill\",\n" +
           "    \"stylers\": [\n" +
           "      {\n" +
           "        \"color\": \"#9e9e9e\"\n" +
           "      }\n" +
           "    ]\n" +
           "  },\n" +
           "  {\n" +
           "    \"featureType\": \"road\",\n" +
           "    \"elementType\": \"geometry\",\n" +
           "    \"stylers\": [\n" +
           "      {\n" +
           "        \"color\": \"#ffffff\"\n" +
           "      }\n" +
           "    ]\n" +
           "  },\n" +
           "  {\n" +
           "    \"featureType\": \"road.arterial\",\n" +
           "    \"elementType\": \"labels.text.fill\",\n" +
           "    \"stylers\": [\n" +
           "      {\n" +
           "        \"color\": \"#757575\"\n" +
           "      }\n" +
           "    ]\n" +
           "  },\n" +
           "  {\n" +
           "    \"featureType\": \"road.highway\",\n" +
           "    \"elementType\": \"geometry\",\n" +
           "    \"stylers\": [\n" +
           "      {\n" +
           "        \"color\": \"#dadada\"\n" +
           "      }\n" +
           "    ]\n" +
           "  },\n" +
           "  {\n" +
           "    \"featureType\": \"road.highway\",\n" +
           "    \"elementType\": \"labels.text.fill\",\n" +
           "    \"stylers\": [\n" +
           "      {\n" +
           "        \"color\": \"#616161\"\n" +
           "      }\n" +
           "    ]\n" +
           "  },\n" +
           "  {\n" +
           "    \"featureType\": \"road.local\",\n" +
           "    \"elementType\": \"labels.text.fill\",\n" +
           "    \"stylers\": [\n" +
           "      {\n" +
           "        \"color\": \"#9e9e9e\"\n" +
           "      }\n" +
           "    ]\n" +
           "  },\n" +
           "  {\n" +
           "    \"featureType\": \"transit.line\",\n" +
           "    \"elementType\": \"geometry\",\n" +
           "    \"stylers\": [\n" +
           "      {\n" +
           "        \"color\": \"#e5e5e5\"\n" +
           "      }\n" +
           "    ]\n" +
           "  },\n" +
           "  {\n" +
           "    \"featureType\": \"transit.station\",\n" +
           "    \"elementType\": \"geometry\",\n" +
           "    \"stylers\": [\n" +
           "      {\n" +
           "        \"color\": \"#eeeeee\"\n" +
           "      }\n" +
           "    ]\n" +
           "  },\n" +
           "  {\n" +
           "    \"featureType\": \"water\",\n" +
           "    \"elementType\": \"geometry\",\n" +
           "    \"stylers\": [\n" +
           "      {\n" +
           "        \"color\": \"#c9c9c9\"\n" +
           "      }\n" +
           "    ]\n" +
           "  },\n" +
           "  {\n" +
           "    \"featureType\": \"water\",\n" +
           "    \"elementType\": \"labels.text.fill\",\n" +
           "    \"stylers\": [\n" +
           "      {\n" +
           "        \"color\": \"#9e9e9e\"\n" +
           "      }\n" +
           "    ]\n" +
           "  }\n" +
           "]";
}
