package com.omkar.app;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author:- OmkarTerbhai
 */
public class QueryBuilder {
    StringBuilder query;
//    public static final String SELECT_CLAUSE = "SELECT";
//    public static final String WHERE_CLAUSE = "WHERE";
//    public static final String FROM_CLAUSE = "FROM";
//    public static final String ORDER_BY_CLAUSE = "ORDER BY";

    private QueryBuilder(Builder b) {
        this.query = b.query;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public String getQuery() {
        return this.query.toString();
    }

    static class Builder {
        StringBuilder query = new StringBuilder();
        public static final String SELECT_CLAUSE = "SELECT ";
        public static final String WHERE_CLAUSE = " WHERE ";
        public static final String FROM_CLAUSE = " FROM ";
        public static final String ORDER_BY_CLAUSE = " ORDER BY ";

        public static StringBuilder select_attributes = new StringBuilder();

        public Builder() {}

        public Builder select(String ...attributes) {
            if(Objects.nonNull(attributes)) {
                select_attributes.append(attributes[0]);
                for(int i = 1; i < attributes.length; i++) {
                    select_attributes.append(", " + attributes[i]);
                }
                this.query.append(SELECT_CLAUSE);
                this.query.append(select_attributes);
            }
            else {
                select_attributes.append("*");
            }
            return this;
        }

        public Builder from(String tableName) throws Exception {
            if(StringUtils.isBlank(tableName)) {
                throw new Exception("Table Name is required to fetch data from SQL database");
            }
            this.query.append(FROM_CLAUSE + tableName);

            return this;
        }

        public Builder where(String condition) {
            if(StringUtils.isNotBlank(condition)) {
                this.query.append(WHERE_CLAUSE + condition);
            }
            return this;
        }

        public QueryBuilder build() {
            return new QueryBuilder(this);
        }
    }
}
