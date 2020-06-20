/*
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package com.ymy.xxb.migrat.auth.shiro.config;

import com.ymy.xxb.migrat.auth.shiro.filter.SqlFilter;
import com.ymy.xxb.migrat.auth.shiro.tenant.TenantInfo;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.ColDataType;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Mybatis插件拦截器，用于处理多租户问题
 * 
 * 1.如果系统处理登录状态，可以通过用户的状态获取tenantId来路由到指定的数据库，并自动拼接上tenantId来区分租户信息
 * 
 * 2.如果是定时任务、触发事件，由于无法获取状态，需要自行完成租户信息的隔离
 * 	
 *  MyBatis 允许你在已映射语句执行过程中的某一点进行拦截调用。默认情况下，MyBatis 允许使用插件来拦截的方法调用包括：
 *  前面是允许用插件拦截的类名，括号里是允许用插件拦截的方法名
	Executor (update, query, flushStatements, commit, rollback, getTransaction, close, isClosed)
	ParameterHandler (getParameterObject, setParameters)
	ResultSetHandler (handleResultSets, handleOutputParameters)
	StatementHandler (prepare, parameterize, batch, update, query)
 * 
 * @author: wangyi
 *
 */
@Component
@Intercepts(
	{@Signature(method = "prepare", type = StatementHandler.class, args = {Connection.class, Integer.class}),
    @Signature(method = "query", type = StatementHandler.class, args = {java.sql.Statement.class, ResultHandler.class})
	})
public class MybatisTenantInterceptsConfig implements Interceptor {
	
	@Autowired
	private TenantInfo tenantInfo;
	
	@Autowired
	private SqlFilter sqlFilter; 
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		if (tenantInfo.getTenantId() != null) {
            StatementHandler handler = (StatementHandler) invocation.getTarget();
            //由于mappedStatement为protected的，所以要通过反射获取
            MetaObject statementHandler = SystemMetaObject.forObject(handler);
            //mappedStatement中有我们需要的方法id
            MappedStatement mappedStatement = (MappedStatement) statementHandler.getValue("delegate.mappedStatement");
            BoundSql boundSql = handler.getBoundSql();
            String sql = boundSql.getSql();
            String id = mappedStatement.getId();

            if (sqlFilter.needFilter(id, sql)) {
                //获得方法类型
                String newSQL = addWhere(sql);
                if (newSQL != null) {
                    statementHandler.setValue("delegate.boundSql.sql", newSQL);
                }
            }
        }

        return invocation.proceed();
	}
	
	/**
     * 添加租户id条件newMs
     *
     * @param sql
     * @return
     * @throws JSQLParserException
     */
    private String addWhere(String sql) throws Exception {
        Statement stmt = CCJSqlParserUtil.parse(sql);
        if (stmt instanceof Insert) {
            //获得Update对象
            Insert insert = (Insert) stmt;
            insert.getColumns().add(new Column(tenantInfo.getTenantColumn()));

            if (insert.getItemsList() instanceof MultiExpressionList){
                for (ExpressionList expressionList : ((MultiExpressionList) insert.getItemsList()).getExprList()) {
                    addTenantValue(expressionList);
                }
            }else {
                addTenantValue(((ExpressionList) insert.getItemsList()));
            }
            return insert.toString();
        }

        if (stmt instanceof Delete) {
            //获得Delete对象
            Delete deleteStatement = (Delete) stmt;
            Expression where = deleteStatement.getWhere();
            if (where instanceof BinaryExpression) {
                EqualsTo equalsTo = new EqualsTo();
                equalsTo.setLeftExpression(new Column(tenantInfo.getTenantColumn()));
                equalsTo.setRightExpression(new StringValue(tenantInfo.getTenantId()));
                AndExpression andExpression = new AndExpression(equalsTo, where);
                deleteStatement.setWhere(andExpression);
            }
            return deleteStatement.toString();
        }

        if (stmt instanceof Update) {
            //获得Update对象
            Update updateStatement = (Update) stmt;
            //获得where条件表达式
            Expression where = updateStatement.getWhere();
            /* if (where instanceof BinaryExpression) {
                // 针对是否含where条件做不同处理
                if (updateStatement.getWhere() != null) {
                    updateStatement.setWhere(addAndExpression(stmt, getTenantIdColumn(), updateStatement.getWhere()));
                } else {
                    EqualsTo equalsTo = new EqualsTo();
                    equalsTo.setLeftExpression(new Column(getTenantIdColumn()));
                    equalsTo.setRightExpression(new StringValue(getTenantInfo().getTenantId()));
                    updateStatement.setWhere(equalsTo);
                }
            } */
            TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
            List<String> tableList = tablesNamesFinder.getTableList(stmt);
            // 针对数据库连接测试没有表名的情况处理【select 'x';select 1什么的】
            if (tableList.size() == 0) {
                return updateStatement.toString();
            }
            for (String table : tableList) {
                if (updateStatement.getWhere() != null) {
                    updateStatement.setWhere(addAndExpression(stmt, table, updateStatement.getWhere()));
                } else {
                    throw new Exception("update语句不能没有where条件:" + sql + Arrays.toString(Thread.currentThread().getStackTrace()));
                }
            }
            return updateStatement.toString();
        }

        if (stmt instanceof Select) {
            Select select = (Select) stmt;
            PlainSelect ps = (PlainSelect) select.getSelectBody();
            TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
            List<String> tableList = tablesNamesFinder.getTableList(select);
            if (tableList.size() == 0) {
                return select.toString();
            }
            for (String table : tableList) {
                if (ps.getWhere() != null) {
                    AndExpression where = addAndExpression(stmt, table, ps.getWhere());
                    // form 和 join 中加载的表
                    if (where != null) {
                        ps.setWhere(where);
                    } else {
                        //子查询中的表
                        findSubSelect(stmt, ps.getWhere());
                    }
                } else {
                    ps.setWhere(addEqualsTo(stmt, table));
                }
            }
            return select.toString();
        }

        if (stmt instanceof CreateTable) {
            CreateTable createTable = (CreateTable) stmt;

            ColumnDefinition columnDefinition = new ColumnDefinition();
            columnDefinition.setColumnName(tenantInfo.getTenantId());
            ColDataType colDataType = new ColDataType();
            colDataType.setDataType(tenantInfo.getDataType());
            columnDefinition.setColDataType(colDataType);
            createTable.getColumnDefinitions().add(columnDefinition);
            return createTable.toString();
        }

        return null;
    }
    
    /**
     * 插入数据 添加租户id
     * @param expressionList
     * @throws Exception
     */
    private void addTenantValue(ExpressionList expressionList) throws Exception {
        expressionList.getExpressions().add(new StringValue(tenantInfo.getTenantId()));
    }

    /**
     * 多条件情况下，使用AndExpression给where条件加上tenantid条件
     *
     * @param table
     * @param where
     * @return
     * @throws Exception
     */
    public AndExpression addAndExpression(Statement stmt, String table, Expression where) throws Exception {
        EqualsTo equalsTo = addEqualsTo(stmt, table);
        if (equalsTo != null) {
            return new AndExpression(equalsTo, where);
        } else {
            return null;
        }
    }

    /**
     * 创建一个 EqualsTo相同判断 条件
     *
     * @param stmt  查询对象
     * @param table 表名
     * @return “A=B” 单个where条件表达式
     * @throws Exception
     */
    public EqualsTo addEqualsTo(Statement stmt, String table) throws Exception {
        EqualsTo equalsTo = new EqualsTo();
        String aliasName;
        aliasName = getTableAlias(stmt, table);
        if (aliasName != null) {
            equalsTo.setLeftExpression(new Column(aliasName + '.' + tenantInfo.getTenantColumn()));
            equalsTo.setRightExpression(new StringValue(tenantInfo.getTenantId()));
            return equalsTo;
        } else {
            return null;
        }
    }


    /**
     *  获取sql送指定表的别名你，没有别名则返回原表名 如果表名不存在返回null
     * 【仅查询from和join 不含 IN 子查询中的表 】
     *
     * @param stmt
     * @param tableName
     * @return
     */
    public String getTableAlias(Statement stmt, String tableName) {
        String alias = null;

        // 插入不做处理
        if (stmt instanceof Insert) {
            return tableName;
        }

        if (stmt instanceof Delete) {
            //获得Delete对象
            Delete deleteStatement = (Delete) stmt;

            if ((deleteStatement.getTable()).getName().equalsIgnoreCase(tableName)) {
                alias = deleteStatement.getTable().getAlias() != null ? deleteStatement.getTable().getAlias().getName() : tableName;
            }
        }

        if (stmt instanceof Update) {
            //获得Update对象
            Update updateStatement = (Update) stmt;

            if ((updateStatement.getTables().get(0)).getName().equalsIgnoreCase(tableName)) {
                alias = updateStatement.getTables().get(0).getAlias() != null ? updateStatement.getTables().get(0).getAlias().getName() : tableName;
            }
        }

        if (stmt instanceof Select) {
            Select select = (Select) stmt;

            PlainSelect ps = (PlainSelect) select.getSelectBody();

            // 判断主表的别名
            if (((Table) ps.getFromItem()).getName().equalsIgnoreCase(tableName)) {
                alias = ps.getFromItem().getAlias() != null ? ps.getFromItem().getAlias().getName() : tableName;
            }
        }
        return alias;
    }

    /**
     * 针对子查询中的表别名查询
     *
     * @param subSelect
     * @param tableName
     * @return
     */
    public String getTableAlias(SubSelect subSelect, String tableName) {
        PlainSelect ps = (PlainSelect) subSelect.getSelectBody();
        // 判断主表的别名
        String alias = null;
        if (((Table) ps.getFromItem()).getName().equalsIgnoreCase(tableName)) {
            if (ps.getFromItem().getAlias() != null) {
                alias = ps.getFromItem().getAlias().getName();
            } else {
                alias = tableName;
            }
        }
        return alias;
    }

    /**
     * 递归处理 子查询中的tenantid-where
     *
     * @param stmt  sql查询对象
     * @param where 当前sql的where条件 where为AndExpression或OrExpression的实例，解析其中的rightExpression，然后检查leftExpression是否为空，
     *              不为空则是AndExpression或OrExpression，再次解析其中的rightExpression
     *              注意tenantid-where是加在子查询上的
     */
    void findSubSelect(Statement stmt, Expression where) throws Exception {

        // and 表达式
        if (where instanceof AndExpression) {
            AndExpression andExpression = (AndExpression) where;
            if (andExpression.getRightExpression() instanceof SubSelect) {
                SubSelect subSelect = (SubSelect) andExpression.getRightExpression();
                doSelect(stmt, subSelect);
            }
            if (andExpression.getLeftExpression() != null) {
                findSubSelect(stmt, andExpression.getLeftExpression());
            }
        } else if (where instanceof OrExpression) {
            //  or表达式
            OrExpression orExpression = (OrExpression) where;
            if (orExpression.getRightExpression() instanceof SubSelect) {
                SubSelect subSelect = (SubSelect) orExpression.getRightExpression();
                doSelect(stmt, subSelect);
            }
            if (orExpression.getLeftExpression() != null) {
                findSubSelect(stmt, orExpression.getLeftExpression());
            }
        }
    }

    /**
     * 处理select 和 subSelect
     *
     * @param stmt   查询对象
     * @param select
     * @return
     * @throws Exception
     */
    Expression doSelect(Statement stmt, Expression select) throws Exception {
        PlainSelect ps = null;
        boolean hasSubSelect = false;

        if (select instanceof SubSelect) {
            ps = (PlainSelect) ((SubSelect) select).getSelectBody();
        }
        if (select instanceof Select) {
            ps = (PlainSelect) ((Select) select).getSelectBody();
        }

        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
        List<String> tableList = tablesNamesFinder.getTableList(select);
        if (tableList.size() == 0) {
            return select;
        }
        for (String table : tableList) {
            // sql 包含 where 条件的情况 使用 addAndExpression 连接 已有的条件和新条件
            if (ps.getWhere() == null) {
                AndExpression where = addAndExpression(stmt, table, ps.getWhere());
                // form 和 join 中加载的表
                if (where != null) {
                    ps.setWhere(where);
                } else {
                    // 如果在Statement中不存在这个表名，则存在于子查询中
                    hasSubSelect = true;
                }
            } else {
                // sql 不含 where条件 新建一个EqualsTo设置为where条件
                EqualsTo equalsTo = addEqualsTo(stmt, table);
                ps.setWhere(equalsTo);
            }
        }

        if (hasSubSelect) {
            //子查询中的表
            findSubSelect(stmt, ps.getWhere());
        }
        return select;
    }
    
    /**
     * 生成代理对象
     *
     * @param target
     * @return
     */
	@Override
	public Object plugin(Object target) {
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties properties) {
		
	}

}
