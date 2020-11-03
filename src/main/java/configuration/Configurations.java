package configuration;

import MysqlInfo.MysqlInfo;
import entities.DriverEnum;
import lombok.Data;

/**
 * created by zlz on 2020/10/9 14:41
 **/
@Data
public class Configurations {

    private MysqlInfo mysqlInfo;

    private DataSource dataSource;

    Configurations(){
        initializeDataSource("blog", "root", "123456", DriverEnum.MYSQL_8);
    }

    private void initializeDataSource(String database, String username, String password, DriverEnum driver){
        MysqlInfo mysqlInfo = new MysqlInfo();
        mysqlInfo.setDataBaseAndUrl(database);
        mysqlInfo.setUsername(username);
        mysqlInfo.setPwd(password);
        mysqlInfo.setDriver(driver);
        this.mysqlInfo = mysqlInfo;

        this.dataSource = new DataSource(mysqlInfo);
    }
}
