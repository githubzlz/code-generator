package configuration;

import entities.Table;

import java.sql.SQLException;
import java.util.List;

/**
 * 全局容器，保存配置信息，模板信息等全局内容
 * created by zlz on 2020/10/9 15:02
 **/
public class GlobalContainer {

    /**
     * 配置信息
     */
    private Configurations configurations;

    /**
     * 表信息
     */
    private List<Table> tables;

    public GlobalContainer(){}

    public void initializeConfigurations(){
        this.configurations = new Configurations();
    }

    public Configurations getConfigurations() {
        return configurations;
    }

    public void setConfigurations(Configurations configurations) {
        this.configurations = configurations;
    }

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables){
        this.tables = tables;
    }

    public void closeConnection(){
        try {
            configurations.getDataSource().getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
