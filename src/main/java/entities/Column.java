package entities;

import lombok.Data;

/**
 * 表字段信息
 * created by zlz on 2020/10/9 17:27
 **/
@Data
public class Column {

    /**
     * 字段名
     */
    private String name;

    /**
     * 转化为驼峰命名
     */
    private String humpName;

    /**
     * 字段注释
     */
    private String comment;

    /**
     * 字段类型
     */
    private String jdbcType;

    /**
     * java类型
     */
    private String javaType;

}
