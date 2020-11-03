package handler;

import configuration.DataSource;
import configuration.GlobalContainer;
import entities.Column;
import entities.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * created by zlz on 2020/10/9 11:11
 **/
public class TableInfoHandler {

    private static final Logger logger = LoggerFactory.getLogger(DataSource.class);

    private GlobalContainer globalContainer;
    private String tableNameColumn;

    public TableInfoHandler(GlobalContainer globalContainer) {
        this.globalContainer = globalContainer;
        tableNameColumn = "tables_in_" + globalContainer.getConfigurations().getMysqlInfo().getDatabase();
    }

    /**
     * 获取数据库下的所有表名
     * @return 表数据
     */
    public List<Table> handleTableNames(){
        logger.info("表名处理中。。。");
        Long start = System.currentTimeMillis();
        List<Table> tables = new ArrayList<Table>();
        try {

            //获取数据源
            DataSource dataSource = globalContainer.getConfigurations().getDataSource();
            String dataBase = globalContainer.getConfigurations().getMysqlInfo().getDatabase();
            //获取连接
            Connection connection = dataSource.getConnection();
            if(null == connection){
                return tables;
            }
            //查询表数据
            Statement statement = connection.createStatement();
            String tableSql = "show tables";
            ResultSet rs = statement.executeQuery(tableSql);
            while (rs.next()){
                Table table = new Table();
                table.setTableName(rs.getString(tableNameColumn));
                table.setDataBase(dataBase);
                tables.add(table);
            }
            rs.close();
            globalContainer.setTables(tables);
        }catch (Exception e){
            e.printStackTrace();
        }
        Long end = System.currentTimeMillis();
        logger.info("表名处理完成,耗时：" + (end-start) + "毫秒");
        return tables;
    }

    /**
     * 处理表字段
     * @return 表数据
     */
    public Table handleTableColumns(Table table){
        Long start = System.currentTimeMillis();
        try {
            if(null == table){
                logger.error("表名未初始化,请先处理表名");
                throw new RuntimeException("表名未初始化,请先处理表名");
            }
            logger.info(table.getTableName()+"表,表字段处理中。。。");

            List<Column> columns = new ArrayList<>();
            table.setColumns(columns);

            String columnSql = "select  table_name,column_name,column_comment,data_type  from information_schema.columns where table_schema ='"
                    + table.getDataBase() +"'  and table_name = '"
                    + table.getTableName() +"'";

            Connection connection = globalContainer.getConfigurations().getDataSource().getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(columnSql);
            while (rs.next()){
                String columnName = rs.getString("column_name");
                String dataType = rs.getString("data_type");
                String columnComment = rs.getString("column_comment");

                Column column = new Column();
                column.setName(columnName);
                column.setJdbcType(dataType);
                column.setComment(columnComment);
                column.setHumpName(toHumpName(columnName));
                column.setJavaType(toJavaType(dataType));
                columns.add(column);
            }
            rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        Long end = System.currentTimeMillis();
        logger.info("表字段处理完成,耗时：" + (end-start) + "毫秒");
        return table;
    }

    public <E> List<E> oneRowToObject(Class<E> cls, ResultSet rs){
        List<E> list = new ArrayList<E>();
        try {
            //获取结果集元数据(获取此 ResultSet 对象的列的编号、类型和属性。)
            while (rs.next()){
                E obj = (E) cls.newInstance();
                ResultSetMetaData rd=rs.getMetaData();
                for (int i = 0; i < rd.getColumnCount(); i++) {
                    //获取列名
                    String columnName=rd.getColumnLabel(i+1);
                    //组合方法名
                    String methodName="set"+columnName.substring(0, 1).toUpperCase()+columnName.substring(1);
                    //获取列类型
                    int columnType=rd.getColumnType(i+1);
                    Method method=null;
                    switch(columnType) {
                        case java.sql.Types.VARCHAR:
                        case java.sql.Types.CHAR:
                            method=cls.getMethod(methodName, String.class);
                            if(method!=null) {
                                method.invoke(obj, rs.getString(columnName));
                            }
                            break;
                        case java.sql.Types.INTEGER:
                        case java.sql.Types.SMALLINT:
                            method=cls.getMethod(methodName, int.class);
                            if(method!=null) {
                                method.invoke(obj, rs.getInt(columnName));
                            }
                            break;
                        case java.sql.Types.BIGINT:
                            method=cls.getMethod(methodName, long.class);
                            if(method!=null) {
                                method.invoke(obj, rs.getLong(columnName));
                            }
                            break;
                        case java.sql.Types.DATE:
                        case java.sql.Types.TIMESTAMP:
                            try {
                                method=cls.getMethod(methodName, Date.class);
                                if(method!=null) {
                                    method.invoke(obj, rs.getTimestamp(columnName));
                                }
                            } catch(Exception e) {
                                method=cls.getMethod(methodName, String.class);
                                if(method!=null) {
                                    method.invoke(obj, rs.getString(columnName));
                                }
                            }
                            break;
                        case java.sql.Types.DECIMAL:
                            method=cls.getMethod(methodName, BigDecimal.class);
                            if(method!=null) {
                                method.invoke(obj, rs.getBigDecimal(columnName));
                            }
                            break;
                        case java.sql.Types.DOUBLE:
                        case java.sql.Types.NUMERIC:
                            method=cls.getMethod(methodName, double.class);
                            if(method!=null) {
                                method.invoke(obj, rs.getDouble(columnName));
                            }
                            break;
                        case java.sql.Types.BIT:
                            method=cls.getMethod(methodName, boolean.class);
                            if(method!=null) {
                                method.invoke(obj, rs.getBoolean(columnName));
                            }
                            break;
                        default:
                            break;
                    }
                    list.add(obj);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 数据库类型转化为java类型
     * @param jdbcType
     * @return
     */
    private String toJavaType(String jdbcType){
        //todo: 转化处理
        return jdbcType;
    }

    /**
     * 数据库类型转化为java类型
     * @param name
     * @return
     */
    private String toHumpName(String name){
        //todo: 转化处理
        return name;
    }

}
