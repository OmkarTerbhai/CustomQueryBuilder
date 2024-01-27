package com.omkar.app;

import com.omkar.app.utils.CommonAppUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author:- OmkarTerbhai
 */
public class QueryBuilder {
    StringBuilder query = new StringBuilder();

    private QueryBuilder(StringBuilder select, StringBuilder where, StringBuilder from, StringBuilder orderBy,
                            StringBuilder innerJoinClause, StringBuilder leftJoinClause, StringBuilder onClause) {
        StringBuilder joinClause = new StringBuilder();
        if(innerJoinClause.length() <= CommonAppUtils.INNER_JOIN_INITIAL_SIZE &&
                leftJoinClause.length() >= CommonAppUtils.LEFT_JOIN_INITIAL_SIZE) {
            joinClause = leftJoinClause;
        }
        else if(innerJoinClause.length() >= CommonAppUtils.INNER_JOIN_INITIAL_SIZE &&
                leftJoinClause.length() <= CommonAppUtils.LEFT_JOIN_INITIAL_SIZE) {
            joinClause = innerJoinClause;
        }
        else {
            joinClause.append(innerJoinClause).append(leftJoinClause);
        }
        query.append(select)
                .append(from)
                .append(where)
                .append(joinClause)
                .append(onClause)
                .append(orderBy)
                .append(";");
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public String getQuery() {
        return this.query.toString();
    }

    static class Builder {
        StringBuilder query = new StringBuilder();
        public  final StringBuilder SELECT_CLAUSE = new StringBuilder("SELECT ");
        public  final StringBuilder WHERE_CLAUSE = new StringBuilder(" WHERE ");
        public  final StringBuilder FROM_CLAUSE = new StringBuilder(" FROM ");
        public  final StringBuilder ORDER_BY_CLAUSE = new StringBuilder(" ORDER BY ");
        public  final StringBuilder INNER_JOIN_CLAUSE = new StringBuilder(" INNER JOIN ");

        public final StringBuilder LEFT_JOIN_CLAUSE = new StringBuilder(" LEFT JOIN ");
        public final StringBuilder ON_CLAUSE = new StringBuilder(" ON ");

        public Builder() {}

        public Builder select(String... attributes) {
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
            FROM_CLAUSE.append(tableName);

            return this;
        }

        /**
         * @param condition
         * @return
         */
        public Builder where(String condition) {
            if(StringUtils.isNotBlank(condition)) {
                WHERE_CLAUSE.append(condition);
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
                INNER_JOIN_CLAUSE.append(joinTable);
            }
            return this;
        }

        public Builder leftJoin(String joinTable) {
            if(Objects.nonNull(joinTable)) {
                LEFT_JOIN_CLAUSE.append(joinTable);
            }
            return this;
        }
        /**
         * Method to append 'ON' clause to the query
         * @param colNames
         * @return
         */
        public Builder on(String... colNames) {
            for(String s : colNames) {
                ON_CLAUSE.append(FROM_CLAUSE.substring(5, FROM_CLAUSE.length()) + "." + s);
                String[] joinSplit = null;
                if(INNER_JOIN_CLAUSE.length() <= CommonAppUtils.INNER_JOIN_INITIAL_SIZE &&
                        LEFT_JOIN_CLAUSE.length() >= CommonAppUtils.LEFT_JOIN_INITIAL_SIZE) {
                    joinSplit = LEFT_JOIN_CLAUSE.toString().split(" ");
                    ON_CLAUSE.append(" = " + joinSplit[joinSplit.length-1] + "." + s);
                }
                else if(INNER_JOIN_CLAUSE.length() >= CommonAppUtils.INNER_JOIN_INITIAL_SIZE &&
                        LEFT_JOIN_CLAUSE.length() <= CommonAppUtils.LEFT_JOIN_INITIAL_SIZE) {
                    joinSplit = INNER_JOIN_CLAUSE.toString().split(" ");
                    ON_CLAUSE.append(" = " + joinSplit[joinSplit.length-1] + "." + s);
                }
            }
            return this;
        }

        public QueryBuilder build() {
            return new QueryBuilder(SELECT_CLAUSE, WHERE_CLAUSE, FROM_CLAUSE, ORDER_BY_CLAUSE,
                    INNER_JOIN_CLAUSE, LEFT_JOIN_CLAUSE, ON_CLAUSE);
        }
    }
}
