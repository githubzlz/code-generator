import configuration.GlobalContainer;
import entities.Table;
import handler.TableInfoHandler;

import java.util.List;

/**
 * created by zlz on 2020/10/9 10:09
 **/
public class CodeGenerator {

    public static void main(String[] args){
        GlobalContainer globalContainer = new GlobalContainer();
        globalContainer.initializeConfigurations();

        TableInfoHandler tableInfoHandler = new TableInfoHandler(globalContainer);
        List<Table> tables = tableInfoHandler.handleTableNames();

        tableInfoHandler.handleTableColumns(tables.get(0));
        tableInfoHandler.handleTableColumns(tables.get(1));
        tables.forEach(System.out::println);

    }


}
