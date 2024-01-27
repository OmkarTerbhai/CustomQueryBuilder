package com.omkar.app;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
        QueryBuilder q = QueryBuilder.getBuilder()
                .where("salary < 40000")
                .select("name", "age")
                .from("Employees")
                .orderBy("age")
                .leftJoin("Department")
                .on("name")
                .build();


        System.out.println(q.getQuery());
    }
}
