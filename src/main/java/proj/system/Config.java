package proj.system;

import proj.utils.datareader.PropertyReader;

public class Config {

    public static final String key = PropertyReader.getProperty("key");
    public static final String token = PropertyReader.getProperty("token");
    public static final String server = PropertyReader.getProperty("server");
    public static String boardId;
    public static String listid;
    public static String listid2;
    public static String cardid;
    public static String cardid2;
    public static String checklistid;
    public static String checklistitemid;
    public static String checklistitemid2;
}
