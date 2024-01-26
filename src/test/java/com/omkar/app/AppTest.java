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
        QueryBuilder q = QueryBuilder.getBuilder().
                select("name", "age")
                .from("Employees")
                .build();

        assertEquals("Basic Query Generated", "SELECT name, age FROM Employees", q.getQuery());
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
