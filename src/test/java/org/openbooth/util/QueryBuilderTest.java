package org.openbooth.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QueryBuilderTest {


    @Test
    public void testValidSelectAllWithConditions(){
        String expectedQuery = "SELECT * FROM test WHERE testcolumn1 = ? AND testcolumn2 = ?;";
        String[] conditions = {"testcolumn1", "testcolumn2"};
        assertEquals(expectedQuery, QueryBuilder.buildSelectAll("test", conditions));
    }

    @Test
    public void testValidSelectAllWithoutConditions(){
        String expectedQuery = "SELECT * FROM test;";
        assertEquals(expectedQuery, QueryBuilder.buildSelectAll("test", new String[]{}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSelectAllWithEmptyTableName(){
        QueryBuilder.buildSelectAll("", new String[]{});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSelectAllWithNullTableName(){
        QueryBuilder.buildSelectAll(null, new String[]{});
    }



    @Test
    public void testValidUpdateWithConditions(){
        String expectedQuery = "UPDATE test SET testcolumn1 = ?, testcolumn2 = ? WHERE testcolumn3 = ? AND testcolumn4 = ?;";
        String[] affectedColumns = {"testcolumn1", "testcolumn2"};
        String[] conditions =  {"testcolumn3", "testcolumn4"};
        assertEquals(expectedQuery, QueryBuilder.buildUpdate("test", affectedColumns, conditions));
    }

    @Test
    public void testValidUpdateWithoutConditions(){
        String expectedQuery = "UPDATE test SET testcolumn1 = ?, testcolumn2 = ?;";
        String[] affectedColumns = {"testcolumn1", "testcolumn2"};
        assertEquals(expectedQuery, QueryBuilder.buildUpdate("test",affectedColumns,new String[]{}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyAffectedColumns(){
        QueryBuilder.buildUpdate("test", new String[]{}, new String[]{});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullAffectedColumns(){
        QueryBuilder.buildUpdate("test", null, new String[]{});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithEmptyTableName(){
        QueryBuilder.buildUpdate("", new String []{"testcolumn"}, new String[]{});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithNullTableName(){
        QueryBuilder.buildUpdate(null, new String []{"testcolumn"}, new String[]{});
    }




    @Test
    public void testValidInsert(){
        String expectedQuery = "INSERT INTO test (testcolumn1, testcolumn2) VALUES (?,?);";
        String[] affectedColumns = {"testcolumn1", "testcolumn2"};
        assertEquals(expectedQuery, QueryBuilder.buildInsert("test", affectedColumns));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertWithEmptyAffectedColumns(){
        QueryBuilder.buildInsert("test", new String[]{});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertWithNullAffectedColumns(){
        QueryBuilder.buildInsert("test", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertWithEmptyTableName(){
        QueryBuilder.buildInsert("", new String[]{});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertWithNullTableName(){
        QueryBuilder.buildInsert(null, new String[]{});
    }

}
