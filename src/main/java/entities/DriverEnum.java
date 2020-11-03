package entities;

/**
 * created by zlz on 2020/10/9 15:36
 **/
public enum DriverEnum {

    MYSQL_8(0, "mysql_8","com.mysql.cj.jdbc.Driver"),
    MYSQL(1, "mysql", "com.mysql.jdbc.Driver");

    private DriverEnum(Integer code, String name, String driver){
        this.code = code;
        this.name = name;
        this.driver = driver;
    }

    private Integer code;

    private String name;

    private String driver;

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDriver() {
        return driver;
    }
}
