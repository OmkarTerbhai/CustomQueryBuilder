package com.omkar.app;

import com.omkar.app.utils.CommonAppUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author:- OmkarTerbhai
 */
public class QueryBuilder {
    StringBuilder query = new StringBuilder();

    private QueryBuilder(Builder builder) throws Exception {
        StringBuilder joinClause = new StringBuilder();
        validateClausesStrings( builder.getSelectClause(),  builder.getWhereClause(),  builder.getFromClause(),  builder.getOrderByClause(),
                builder.getInnerJoinClause(), builder.getLeftJoinClause(), builder.getOnClause(), builder.getLimitClause());
        if(builder.getInnerJoinClause().length() <= CommonAppUtils.INNER_JOIN_INITIAL_SIZE &&
                builder.LEFT_JOIN_CLAUSE.length() >= CommonAppUtils.LEFT_JOIN_INITIAL_SIZE) {
            joinClause = builder.getLeftJoinClause();
        }
        else if(builder.getInnerJoinClause().length() >= CommonAppUtils.INNER_JOIN_INITIAL_SIZE &&
                builder.LEFT_JOIN_CLAUSE.length() <= CommonAppUtils.LEFT_JOIN_INITIAL_SIZE) {
            joinClause = builder.getInnerJoinClause();
        }
        else {
            joinClause.append(builder.getInnerJoinClause()).append(builder.getLeftJoinClause());
        }
        query.append(builder.SELECT_CLAUSE)
                .append(builder.FROM_CLAUSE)
                .append(builder.WHERE_CLAUSE)
                .append(joinClause)
                .append(builder.ON_CLAUSE)
                .append(builder.getOrderByClause())
                .append(builder.getLimitClause())
                .append(";");
    }

    public void validateClausesStrings(StringBuilder select, StringBuilder where, StringBuilder from, StringBuilder orderBy,
                                       StringBuilder innerJoinClause, StringBuilder leftJoinClause, StringBuilder onClause,
                                       StringBuilder limitClause) throws Exception {

        if(StringUtils.isBlank(select)) {
            throw new Exception("Query Must have a SELECT statement");
        }
        if(StringUtils.isBlank(from)) {
            throw new Exception("Please specify table to fetch data from");
        }

    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public String getQuery() {
        return this.query.toString();
    }

    static class Builder {
        StringBuilder query = new StringBuilder();
        public  final StringBuilder SELECT_CLAUSE = new StringBuilder();
        public  final StringBuilder WHERE_CLAUSE = new StringBuilder();
        public  final StringBuilder FROM_CLAUSE = new StringBuilder();
        public  final StringBuilder ORDER_BY_CLAUSE = new StringBuilder();
        public  final StringBuilder INNER_JOIN_CLAUSE = new StringBuilder();
        public final StringBuilder LEFT_JOIN_CLAUSE = new StringBuilder();
        public final StringBuilder ON_CLAUSE = new StringBuilder();
        public final StringBuilder LIMIT_CLAUSE = new StringBuilder();

        public Builder() {}

        public Builder select(String... attributes) {
            SELECT_CLAUSE.append(CommonAppUtils.SELECT_STATEMENT);
            if(Objects.nonNull(attributes)) {
                SELECT_CLAUSE.append(attributes[0]);
                for(int i = 1; i < attributes.length; i++) {
                    SELECT_CLAUSE.append(", " + attributes[i]);
                }
            }
            else {
                SELECT_CLAUSE.append("*");
            }
            return this;
        }

        /**
         * @param tableName
         * @return
         * @throws Exception
         */
        public Builder from(String tableName) throws Exception {
            if(StringUtils.isBlank(tableName)) {
                throw new Exception("Table Name is required to fetch data from SQL database");
            }
            FROM_CLAUSE.append(CommonAppUtils.FROM_STATEMENT + tableName);

            return this;
        }

        /**
         * @param condition
         * @return
         */
        public Builder where(String condition) {
            if(StringUtils.isNotBlank(condition)) {
                WHERE_CLAUSE.append(CommonAppUtils.WHERE_STATEMENT + condition);
            }
            return this;
        }

        /**
         * @param colNames
         * @return
         * @throws Exception
         */
        public Builder orderBy(String... colNames) throws Exception {
            if(Objects.nonNull(colNames)) {
                ORDER_BY_CLAUSE.append(CommonAppUtils.ORDER_BY_STATEMENT);
                for(String s : colNames) {
                    if(SELECT_CLAUSE.indexOf(s) == -1) {
                        throw new Exception("Column for order by must  be present in select clause");
                    }
                    else {
                        ORDER_BY_CLAUSE.append(s);
                    }
                }
            }
            return this;
        }

        /**
         * @param joinTable
         * @return
         */
        public Builder innerJoin(String joinTable) {
            if(Objects.nonNull(joinTable)) {
                INNER_JOIN_CLAUSE.append(CommonAppUtils.INNER_JOIN_STATEMENT + joinTable);
            }
            return this;
        }

        public Builder leftJoin(String joinTable) {
            if(Objects.nonNull(joinTable)) {
                LEFT_JOIN_CLAUSE.append(CommonAppUtils.LEFT_JOIN_STATEMENT + joinTable);
            }
            return this;
        }
        /**
         * Method to append 'ON' clause to the query
         * @param colNames
         * @return
         */
        public Builder on(String... colNames) {
            if(Objects.nonNull(colNames)) {
                ON_CLAUSE.append(CommonAppUtils.ON_STATEMENT);
                for (String s : colNames) {
                    ON_CLAUSE.append(FROM_CLAUSE.substring(5, FROM_CLAUSE.length()) + "." + s);
                    String[] joinSplit = null;
                    if (INNER_JOIN_CLAUSE.length() <= CommonAppUtils.INNER_JOIN_INITIAL_SIZE &&
                            LEFT_JOIN_CLAUSE.length() >= CommonAppUtils.LEFT_JOIN_INITIAL_SIZE) {
                        joinSplit = LEFT_JOIN_CLAUSE.toString().split(" ");
                        ON_CLAUSE.append(" = " + joinSplit[joinSplit.length - 1] + "." + s);
                    } else if (INNER_JOIN_CLAUSE.length() >= CommonAppUtils.INNER_JOIN_INITIAL_SIZE &&
                            LEFT_JOIN_CLAUSE.length() <= CommonAppUtils.LEFT_JOIN_INITIAL_SIZE) {
                        joinSplit = INNER_JOIN_CLAUSE.toString().split(" ");
                        ON_CLAUSE.append(" = " + joinSplit[joinSplit.length - 1] + "." + s);
                    }
                }
            }
            return this;
        }

        public Builder limit(int limit) {
            LIMIT_CLAUSE.append(CommonAppUtils.LIMIT_STATEMENT + limit);
            return this;
        }

        public StringBuilder getSelectClause() {
            return this.SELECT_CLAUSE;
        }
        public StringBuilder getWhereClause() {
            return this.WHERE_CLAUSE;
        }
        public StringBuilder getFromClause() {
            return this.FROM_CLAUSE;
        }

        public StringBuilder getInnerJoinClause() {
            return this.INNER_JOIN_CLAUSE;
        }

        public StringBuilder getLeftJoinClause() {
            return this.LEFT_JOIN_CLAUSE;
        }

        public StringBuilder getOnClause() {
            return this.ON_CLAUSE;
        }

        public StringBuilder getOrderByClause() {
            return this.ORDER_BY_CLAUSE;
        }

        public StringBuilder getLimitClause() {
            return this.LIMIT_CLAUSE;
        }

        public QueryBuilder build() throws Exception {
            return new QueryBuilder(this);
        }
    }
}
