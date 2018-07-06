package org.openbooth.util;

/**
 * This class is used to create SQL strings, which can then be used for prepared statements
 */
public class QueryBuilder {

    private QueryBuilder(){}

    private static final String TABLE_NAME_WARNING = "table name is null or empty";

    /**
     * Generates a SELECT * FROM tableName WHERE conditions[0] = ? AND conditions[1] = ? ... SQL query string
     * This can be used as an input to a prepared statement
     * The '?' for the input parameters are already set
     * @param tableName the name of the database table to query
     * @param conditions the array of column names for the WHERE conditions
     * @return the query string
     */
    public static String buildSelectAllColumns(String tableName, String[] conditions){
        if(tableName == null || tableName.isEmpty())
            throw new IllegalArgumentException(TABLE_NAME_WARNING);

        StringBuilder builder = new StringBuilder();

        builder.append("SELECT * FROM ");
        builder.append(tableName);

        return appendConditions(builder, conditions).toString();

    }

    /**
     * Generates a UPDATE tableName SET affectedColumns[0] = ?, affectedColumns[1] = ?...
     * WHERE conditions[0] = ? AND conditions[1] = ? ...  SQL statement string
     * This statement can be used as is input to a prepared statement
     * The '?' for the input parameters are already set
     * @param tableName the name of the database table
     * @param affectedColumns the array of column names which should be affected by the update statement
     * @param conditions the array of column names for the WHERE conditions
     * @return the statement string
     */
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

    /**
     * Generates a INSERT INTO tableName (affectedColumns[0],affectedColumns[1]...) VALUES (?,?..) SQL statement string
     * This statement can be used as is input to a prepared statement
     * The '?' for the input parameters are already set
     * @param tableName the name of the database table
     * @param affectedColumns the array of column names for which for values should be inserted
     * @return the statement string
     */
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

    /**
     * Generates a DELETE FROM tableName WHERE condition[0] = ? AND condition[1] = ?... SQL statement string
     * This statement can be used as is input to a prepared statement
     * The '?' for the input parameters are already set
     * @param tableName the name of the database table
     * @param conditions the array of column names for the WHERE conditions
     * @return the statement string
     */
    public static String buildDelete(String tableName, String[] conditions){
        if(tableName == null || tableName.isEmpty())
            throw new IllegalArgumentException(TABLE_NAME_WARNING);

        StringBuilder builder = new StringBuilder();

        builder.append("DELETE FROM ");
        builder.append(tableName);

        return appendConditions(builder, conditions).toString();
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
