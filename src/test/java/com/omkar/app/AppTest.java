package com.omkar.app;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() throws Exception {
        QueryBuilder q = QueryBuilder.getBuilder()
                .where("salary < 40000")
                .select("name", "age")
                .from("MyTable")
                .orderBy("age")
                .leftJoin("Department")
                .on("deptId")
                .limit(3)
                .build();

        assertEquals("Basic Query Generated", "SELECT name, age " +
                "FROM MyTable " +
                "WHERE salary < 40000 " +
                "LEFT JOIN Department " +
                "ON  MyTable.deptId = Department.deptId " +
                "ORDER BY age " +
                "LIMIT 3;", q.getQuery());
    }

    @Test
    public void testQuerySpacing() throws Exception {
        QueryBuilder q = QueryBuilder.getBuilder().
                select("name", "age")
                .from("Employees")
                .build();

        assertNotEquals("Column Spacing Violated", "SELECT name,age FROM Employees", q.getQuery());
    }
}
