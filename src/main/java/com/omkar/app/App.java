package com.omkar.app;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {

        QueryBuilder q = QueryBuilder.getBuilder().
                select("name", "age")
                .from("Employees")
                .where("salary < 40000")
                .build();
        System.out.println(q.getQuery());
    }
}
