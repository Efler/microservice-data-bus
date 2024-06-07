/*
 * This file is generated by jOOQ.
 */
package generated.jooqcodegen.tables;


import generated.jooqcodegen.Keys;
import generated.jooqcodegen.Public;
import generated.jooqcodegen.tables.records.FilterRulesRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import javax.annotation.processing.Generated;
import java.util.Collection;


/**
 * This class is generated by jOOQ.
 */
@Generated(
        value = {
                "https://www.jooq.org",
                "jOOQ version:3.19.1"
        },
        comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes", "this-escape"})
public class FilterRules extends TableImpl<FilterRulesRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.filter_rules</code>
     */
    public static final FilterRules FILTER_RULES = new FilterRules();

    /**
     * The class holding records for this type
     */
    @Override
    @NotNull
    public Class<FilterRulesRecord> getRecordType() {
        return FilterRulesRecord.class;
    }

    /**
     * The column <code>public.filter_rules.id</code>.
     */
    public final TableField<FilterRulesRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.filter_rules.filter_id</code>.
     */
    public final TableField<FilterRulesRecord, Long> FILTER_ID = createField(DSL.name("filter_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.filter_rules.rule_id</code>.
     */
    public final TableField<FilterRulesRecord, Long> RULE_ID = createField(DSL.name("rule_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.filter_rules.field_name</code>.
     */
    public final TableField<FilterRulesRecord, String> FIELD_NAME = createField(DSL.name("field_name"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>public.filter_rules.filter_function_name</code>.
     */
    public final TableField<FilterRulesRecord, String> FILTER_FUNCTION_NAME = createField(DSL.name("filter_function_name"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>public.filter_rules.filter_value</code>.
     */
    public final TableField<FilterRulesRecord, String> FILTER_VALUE = createField(DSL.name("filter_value"), SQLDataType.CLOB.nullable(false), this, "");

    private FilterRules(Name alias, Table<FilterRulesRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private FilterRules(Name alias, Table<FilterRulesRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>public.filter_rules</code> table reference
     */
    public FilterRules(String alias) {
        this(DSL.name(alias), FILTER_RULES);
    }

    /**
     * Create an aliased <code>public.filter_rules</code> table reference
     */
    public FilterRules(Name alias) {
        this(alias, FILTER_RULES);
    }

    /**
     * Create a <code>public.filter_rules</code> table reference
     */
    public FilterRules() {
        this(DSL.name("filter_rules"), null);
    }

    @Override
    @Nullable
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    @NotNull
    public Identity<FilterRulesRecord, Integer> getIdentity() {
        return (Identity<FilterRulesRecord, Integer>) super.getIdentity();
    }

    @Override
    @NotNull
    public UniqueKey<FilterRulesRecord> getPrimaryKey() {
        return Keys.FILTER_RULES_PKEY;
    }

    @Override
    @NotNull
    public FilterRules as(String alias) {
        return new FilterRules(DSL.name(alias), this);
    }

    @Override
    @NotNull
    public FilterRules as(Name alias) {
        return new FilterRules(alias, this);
    }

    @Override
    @NotNull
    public FilterRules as(Table<?> alias) {
        return new FilterRules(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public FilterRules rename(String name) {
        return new FilterRules(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public FilterRules rename(Name name) {
        return new FilterRules(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public FilterRules rename(Table<?> name) {
        return new FilterRules(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    public FilterRules where(Condition condition) {
        return new FilterRules(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    public FilterRules where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    public FilterRules where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    public FilterRules where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    @PlainSQL
    public FilterRules where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    @PlainSQL
    public FilterRules where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    @PlainSQL
    public FilterRules where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    @PlainSQL
    public FilterRules where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    public FilterRules whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    public FilterRules whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
