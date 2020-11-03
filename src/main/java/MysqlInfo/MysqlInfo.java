package MysqlInfo;


import entities.DriverEnum;

/**
 * created by zlz on 2020/10/9 10:38
 **/
public class MysqlInfo {

    private  String database;
    private  String url;
    private  String username;
    private  String pwd;
    private DriverEnum driver;


    public DriverEnum getDriver() {
        return driver;
    }

    public void setDriver(DriverEnum driver){
        this.driver = driver;
    }

    public void setDataBaseAndUrl(String database){
        this.database = database;
        this.url = "jdbc:mysql://localhost:3306/" + database + "?serverTimezone=Asia/Shanghai";
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
