package com.social.socialnetwork.Repository;


public class DBConnection {
    public String DB_URL;
    public String DB_USER;
    public String DB_PASSWD;

    public DBConnection(String dbUrl, String dbUser, String dbPasswd) {
        DB_URL = dbUrl;
        DB_USER = dbUser;
        DB_PASSWD = dbPasswd;
    }
    public DBConnection(){
        DB_URL = "jdbc:postgresql://localhost:5432/SocialNetwork";
        DB_USER = "postgres";
        DB_PASSWD = "A5#t3*R1m29%";
    }

}
