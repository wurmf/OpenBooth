package org.openbooth.util;

/**
 * This class is used to create SQL strings, which can then be used for prepared statements
 */
public class QueryBuilder {

    private QueryBuilder(){}

    private static final String TABLE_NAME_WARNING = "table name is null or empty";

    public static String buildSelectAllColumns(String tableName, String[] conditions){
        if(tableName == null || tableName.isEmpty())
            throw new IllegalArgumentException(TABLE_NAME_WARNING);

        StringBuilder builder = new StringBuilder();

        builder.append("SELECT * FROM ");
        builder.append(tableName);

        return appendConditions(builder, conditions).toString();

    }

    public static String buildUpdate(String tableName, String[] affectedColumns, String[] conditions){
        if(tableName == null || tableName.isEmpty())
            throw new IllegalArgumentException(TABLE_NAME_WARNING);
        if(affectedColumns == null || affectedColumns.length == 0)
            throw new IllegalArgumentException("affectedColumns is null or length zero");

        StringBuilder builder = new StringBuilder();

        builder.append("UPDATE ");
        builder.append(tableName);
        builder.append(" SET ");

        for(int i = 0; i < affectedColumns.length; i++){

            if(i > 0) builder.append(", ");

            builder.append(affectedColumns[i]);
            builder.append(" = ?");
        }

        return appendConditions(builder, conditions).toString();
    }

    public static String buildInsert(String tableName, String[] affectedColumns){
        if(tableName == null || tableName.isEmpty())
            throw new IllegalArgumentException(TABLE_NAME_WARNING);
        if(affectedColumns == null || affectedColumns.length == 0)
            throw new IllegalArgumentException("affected columns is null or length zero");

        StringBuilder builder = new StringBuilder();

        builder.append("INSERT INTO ");
        builder.append(tableName);
        builder.append(" (");

        for(int i = 0; i < affectedColumns.length; i++ ){
            if(i > 0) builder.append(", ");
            builder.append(affectedColumns[i]);
        }

        builder.append(") VALUES (");

        for(int i = 0; i < affectedColumns.length; i++){
            if(i > 0) builder.append(",");
            builder.append("?");
        }

        builder.append(");");
        return builder.toString();
    }

    private static StringBuilder appendConditions(StringBuilder builder, String[] conditions){
        if (conditions == null || conditions.length == 0)
            return builder.append(";");

        builder.append(" WHERE ");

        for(int i = 0; i < conditions.length; i++){

            if(i > 0) builder.append(" AND ");

            builder.append(conditions[i]);
            builder.append(" = ?");
        }

        builder.append(";");
        return builder;
    }

}
