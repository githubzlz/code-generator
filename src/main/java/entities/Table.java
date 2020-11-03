package entities;

import lombok.Data;

import java.util.List;

/**
 * created by zlz on 2020/10/9 11:30
 **/
@Data
public class Table {

    private String dataBase;
    private String tableName;
    private List<Column> columns;
}
