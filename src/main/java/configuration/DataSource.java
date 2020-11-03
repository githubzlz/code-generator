package configuration;

import MysqlInfo.MysqlInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * created by zlz on 2020/10/9 10:30
 **/
public class DataSource {

    private static final Logger logger = LoggerFactory.getLogger(DataSource.class);

    private static volatile Connection connection;

    private MysqlInfo mysqlInfo;

    DataSource(MysqlInfo mysqlInfo){
        this.mysqlInfo = mysqlInfo;
    }
    /**
     * 获取数据库连接
     * @return
     */
    public Connection getConnection(){
        logger.info("获取连接中。。。");
        if(connection == null) {
            synchronized (DataSource.class) {
                if (connection == null) {
                    try {
                        logger.info("创建连接中。。。");
                        long start = System.currentTimeMillis();

                        //获取数据库信息配置
                        String url = mysqlInfo.getUrl();
                        String username = mysqlInfo.getUsername();
                        String pwd = mysqlInfo.getPwd();
                        //注册驱动
                        Class.forName(mysqlInfo.getDriver().getDriver());
                        //获取数据库连接
                        connection = DriverManager.getConnection(url, username, pwd);
                        long end = System.currentTimeMillis();
                        logger.info("创建连接成功！耗时：" + (end-start) + "毫秒");
                    } catch (Exception e) {
                        logger.error("创建连接失败！请重试");
                        logger.error("错误信息："+e.getMessage());
                        return connection;
                    }
                }
            }
        }
        logger.info("获取连接成功！");
        return connection;
    }

    /**
     * 释放资源
     * @param connection
     * @param resultSets
     * @return
     */
    public boolean release(Connection connection, ResultSet[] resultSets){
        try {
            connection.close();
            for (ResultSet resultSet : resultSets) {
                resultSet.close();
            }
            DataSource.connection = null;
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
