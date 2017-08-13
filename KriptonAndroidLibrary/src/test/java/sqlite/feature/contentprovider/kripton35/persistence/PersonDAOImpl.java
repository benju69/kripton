package sqlite.feature.contentprovider.kripton35.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import com.abubusoft.kripton.android.Logger;
import com.abubusoft.kripton.android.sqlite.AbstractDao;
import com.abubusoft.kripton.android.sqlite.OnReadCursorListener;
import com.abubusoft.kripton.android.sqlite.SqlUtils;
import com.abubusoft.kripton.common.CollectionUtils;
import com.abubusoft.kripton.common.DateUtils;
import com.abubusoft.kripton.common.StringUtils;
import com.abubusoft.kripton.exception.KriptonRuntimeException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import sqlite.feature.contentprovider.kripton35.entities.Person;

/**
 * <p>
 * DAO implementation for entity <code>Person</code>, based on interface <code>PersonDAO</code>
 * </p>
 *
 *  @see Person
 *  @see PersonDAO
 *  @see sqlite.feature.contentprovider.kripton35.entities.PersonTable
 */
public class PersonDAOImpl extends AbstractDao implements PersonDAO {
  private static final Set<String> insertBean0ColumnSet = CollectionUtils.asSet(String.class, "birth_city", "birth_day", "value", "name", "surname");

  private static final Set<String> insertName1ColumnSet = CollectionUtils.asSet(String.class, "name");

  private static final Set<String> updateRaw5ColumnSet = CollectionUtils.asSet(String.class, "name");

  private static final Set<String> updateRaw6ColumnSet = CollectionUtils.asSet(String.class, "name");

  private static final Set<String> updateRaw7ColumnSet = CollectionUtils.asSet(String.class, "name");

  private static final Set<String> updateBean8ColumnSet = CollectionUtils.asSet(String.class, "alias_parent_id", "birth_city", "birth_day", "value", "name", "surname");

  private static final Set<String> selectOne9ColumnSet = CollectionUtils.asSet(String.class, "id", "alias_parent_id", "birth_city", "birth_day", "value", "name", "surname");

  private static final Set<String> selectAll10ColumnSet = CollectionUtils.asSet(String.class, "id", "alias_parent_id", "birth_city", "birth_day", "value", "name", "surname");

  private static final Set<String> selectOne11ColumnSet = CollectionUtils.asSet(String.class, "id", "alias_parent_id", "birth_city", "birth_day", "value", "name", "surname");

  private static final Set<String> selectBean12ColumnSet = CollectionUtils.asSet(String.class, "id", "alias_parent_id", "birth_city", "birth_day", "value", "name", "surname");

  public PersonDAOImpl(BindPersonDataSource dataSet) {
    super(dataSet);
  }

  /**
   * <p>SQL insert:</p>
   * <pre>INSERT OR FAIL INTO person (birth_city, birth_day, value, name, surname) VALUES (${bean.birthCity}, ${bean.birthDay}, ${bean.value}, ${bean.name}, ${bean.surname})</pre>
   *
   * <p><code>bean.id</code> is automatically updated because it is the primary key</p>
   *
   * <p><strong>Inserted columns:</strong></p>
   * <dl>
   * 	<dt>birth_city</dt><dd>is mapped to <strong>${bean.birthCity}</strong></dd>
   * 	<dt>birth_day</dt><dd>is mapped to <strong>${bean.birthDay}</strong></dd>
   * 	<dt>value</dt><dd>is mapped to <strong>${bean.value}</strong></dd>
   * 	<dt>name</dt><dd>is mapped to <strong>${bean.name}</strong></dd>
   * 	<dt>surname</dt><dd>is mapped to <strong>${bean.surname}</strong></dd>
   * </dl>
   *
   * @param bean
   * 	is mapped to parameter <strong>bean</strong>
   *
   */
  @Override
  public void insertBean(Person bean) {
    ContentValues contentValues=contentValues();
    contentValues.clear();

    if (bean.birthCity!=null) {
      contentValues.put("birth_city", bean.birthCity);
    } else {
      contentValues.putNull("birth_city");
    }
    if (bean.birthDay!=null) {
      contentValues.put("birth_day", DateUtils.write(bean.birthDay));
    } else {
      contentValues.putNull("birth_day");
    }
    contentValues.put("value", bean.value);
    if (bean.getName()!=null) {
      contentValues.put("name", bean.getName());
    } else {
      contentValues.putNull("name");
    }
    if (bean.getSurname()!=null) {
      contentValues.put("surname", bean.getSurname());
    } else {
      contentValues.putNull("surname");
    }

    // log for insert -- BEGIN 
    StringBuffer _columnNameBuffer=new StringBuffer();
    StringBuffer _columnValueBuffer=new StringBuffer();
    String _columnSeparator="";
    for (String columnName:contentValues.keySet()) {
      _columnNameBuffer.append(_columnSeparator+columnName);
      _columnValueBuffer.append(_columnSeparator+":"+columnName);
      _columnSeparator=", ";
    }
    Logger.info("INSERT OR FAIL INTO person (%s) VALUES (%s)", _columnNameBuffer.toString(), _columnValueBuffer.toString());

    // log for content values -- BEGIN
    Object _contentValue;
    for (String _contentKey:contentValues.keySet()) {
      _contentValue=contentValues.get(_contentKey);
      if (_contentValue==null) {
        Logger.info("==> :%s = <null>", _contentKey);
      } else {
        Logger.info("==> :%s = '%s' (%s)", _contentKey, StringUtils.checkSize(_contentValue), _contentValue.getClass().getCanonicalName());
      }
    }
    // log for content values -- END
    // log for insert -- END 

    // conflict algorithm FAIL
    long result = database().insertWithOnConflict("person", null, contentValues, 3);
    bean.id=result;
  }

  /**
   * <h1>Content provider URI (INSERT operation):</h1>
   * <pre>content://sqlite.feature.contentprovider.kripton35/persons</pre>
   *
   * <h2>JQL INSERT for Content Provider</h2>
   * <pre>INSERT OR FAIL INTO Person (birthCity, birthDay, value, name, surname) VALUES (${bean.birthCity}, ${bean.birthDay}, ${bean.value}, ${bean.name}, ${bean.surname})</pre>
   *
   * <h2>SQL INSERT for Content Provider</h2>
   * <pre>INSERT OR FAIL INTO person (birth_city, birth_day, value, name, surname) VALUES (${bean.birthCity}, ${bean.birthDay}, ${bean.value}, ${bean.name}, ${bean.surname})</pre>
   *
   * <p><strong>Dynamic where statement is ignored, due no param with @BindSqlDynamicWhere was added.</strong></p>
   *
   * <p><strong>In URI, * is replaced with [*] for javadoc rapresentation</strong></p>
   *
   * @param uri "content://sqlite.feature.contentprovider.kripton35/persons"
   * @param contentValues content values
   * @return new row's id
   */
  long insertBean0(Uri uri, ContentValues contentValues) {
    Logger.info("Execute INSERT for URI %s", uri.toString());
    for (String columnName:contentValues.keySet()) {
      if (!insertBean0ColumnSet.contains(columnName)) {
        throw new KriptonRuntimeException(String.format("For URI 'content://sqlite.feature.contentprovider.kripton35/persons', column '%s' does not exists in table '%s' or can not be defined in this INSERT operation", columnName, "person" ));
      }
    }

    // log for insert -- BEGIN 
    StringBuffer _columnNameBuffer=new StringBuffer();
    StringBuffer _columnValueBuffer=new StringBuffer();
    String _columnSeparator="";
    for (String columnName:contentValues.keySet()) {
      _columnNameBuffer.append(_columnSeparator+columnName);
      _columnValueBuffer.append(_columnSeparator+":"+columnName);
      _columnSeparator=", ";
    }
    Logger.info("INSERT OR FAIL INTO person (%s) VALUES (%s)", _columnNameBuffer.toString(), _columnValueBuffer.toString());

    // log for content values -- BEGIN
    Object _contentValue;
    for (String _contentKey:contentValues.keySet()) {
      _contentValue=contentValues.get(_contentKey);
      if (_contentValue==null) {
        Logger.info("==> :%s = <null>", _contentKey);
      } else {
        Logger.info("==> :%s = '%s' (%s)", _contentKey, StringUtils.checkSize(_contentValue), _contentValue.getClass().getCanonicalName());
      }
    }
    // log for content values -- END
    // log for insert -- END 

    // conflict algorithm FAIL
    long result = database().insertWithOnConflict("person", null, contentValues, 3);
    return result;
  }

  /**
   * <h2>SQL insert</h2>
   * <pre>INSERT INTO person (name) VALUES (${tempName})</pre>
   *
   * <h2>Inserted columns:</strong></h2>
   * <dl>
   * 	<dt>name</dt><dd>is binded to query's parameter <strong>${name}</strong> and method's parameter <strong>tempName</strong></dd>
   * </dl>
   *
   * @param tempName
   * 	is binded to column value <strong>name</strong>
   *
   */
  @Override
  public void insertName(String tempName) {
    ContentValues contentValues=contentValues();
    contentValues.clear();

    if (tempName!=null) {
      contentValues.put("name", tempName);
    } else {
      contentValues.putNull("name");
    }

    // log for insert -- BEGIN 
    StringBuffer _columnNameBuffer=new StringBuffer();
    StringBuffer _columnValueBuffer=new StringBuffer();
    String _columnSeparator="";
    for (String columnName:contentValues.keySet()) {
      _columnNameBuffer.append(_columnSeparator+columnName);
      _columnValueBuffer.append(_columnSeparator+":"+columnName);
      _columnSeparator=", ";
    }
    Logger.info("INSERT INTO person (%s) VALUES (%s)", _columnNameBuffer.toString(), _columnValueBuffer.toString());

    // log for content values -- BEGIN
    Object _contentValue;
    for (String _contentKey:contentValues.keySet()) {
      _contentValue=contentValues.get(_contentKey);
      if (_contentValue==null) {
        Logger.info("==> :%s = <null>", _contentKey);
      } else {
        Logger.info("==> :%s = '%s' (%s)", _contentKey, StringUtils.checkSize(_contentValue), _contentValue.getClass().getCanonicalName());
      }
    }
    // log for content values -- END
    // log for insert -- END 

    database().insert("person", null, contentValues);
  }

  /**
   * <h1>Content provider URI (INSERT operation):</h1>
   * <pre>content://sqlite.feature.contentprovider.kripton35/persons/[*]</pre>
   *
   * <h2>JQL INSERT for Content Provider</h2>
   * <pre>INSERT INTO Person (name) VALUES (${name})</pre>
   *
   * <h2>SQL INSERT for Content Provider</h2>
   * <pre>INSERT INTO person (name) VALUES (${name})</pre>
   *
   * <h3>Path variables defined:</h3>
   * <ul>
   * <li><strong>${name}</strong> at path segment 1</li>
   * </ul>
   *
   * <p><strong>Dynamic where statement is ignored, due no param with @BindSqlDynamicWhere was added.</strong></p>
   *
   * <p><strong>In URI, * is replaced with [*] for javadoc rapresentation</strong></p>
   *
   * @param uri "content://sqlite.feature.contentprovider.kripton35/persons/[*]"
   * @param contentValues content values
   * @return new row's id
   */
  long insertName1(Uri uri, ContentValues contentValues) {
    Logger.info("Execute INSERT for URI %s", uri.toString());
    for (String columnName:contentValues.keySet()) {
      if (!insertName1ColumnSet.contains(columnName)) {
        throw new KriptonRuntimeException(String.format("For URI 'content://sqlite.feature.contentprovider.kripton35/persons/*', column '%s' does not exists in table '%s' or can not be defined in this INSERT operation", columnName, "person" ));
      }
    }

    // Add parameter name at path segment 1
    contentValues.put("name", uri.getPathSegments().get(1));
    // log for insert -- BEGIN 
    StringBuffer _columnNameBuffer=new StringBuffer();
    StringBuffer _columnValueBuffer=new StringBuffer();
    String _columnSeparator="";
    for (String columnName:contentValues.keySet()) {
      _columnNameBuffer.append(_columnSeparator+columnName);
      _columnValueBuffer.append(_columnSeparator+":"+columnName);
      _columnSeparator=", ";
    }
    Logger.info("INSERT INTO person (%s) VALUES (%s)", _columnNameBuffer.toString(), _columnValueBuffer.toString());

    // log for content values -- BEGIN
    Object _contentValue;
    for (String _contentKey:contentValues.keySet()) {
      _contentValue=contentValues.get(_contentKey);
      if (_contentValue==null) {
        Logger.info("==> :%s = <null>", _contentKey);
      } else {
        Logger.info("==> :%s = '%s' (%s)", _contentKey, StringUtils.checkSize(_contentValue), _contentValue.getClass().getCanonicalName());
      }
    }
    // log for content values -- END
    // log for insert -- END 

    long result = database().insert("person", null, contentValues);
    return result;
  }

  /**
   * <h2>SQL delete</h2>
   * <pre>DELETE FROM person WHERE id = ${id}</pre>
   *
   *
   * <h2>Where parameters:</h2>
   * <dl>
   * 	<dt>${id}</dt><dd>is mapped to method's parameter <strong>id</strong></dd>
   * </dl>
   *
   * @param id
   * 	is used as where parameter <strong>${id}</strong>
   */
  @Override
  public void deleteRaw(long id) {
    ArrayList<String> _sqlWhereParams=getWhereParamsArray();
    _sqlWhereParams.add(String.valueOf(id));

    StringBuilder _sqlBuilder=new StringBuilder();
    // generation CODE_001 -- BEGIN
    // generation CODE_001 -- END

    // manage WHERE arguments -- BEGIN

    // manage WHERE statement
    String _sqlWhereStatement=" id = ?";
    _sqlBuilder.append(_sqlWhereStatement);

    // manage WHERE arguments -- END

    // display log
    Logger.info("DELETE FROM person WHERE id = ?");

    // log for where parameters -- BEGIN
    int _whereParamCounter=0;
    for (String _whereParamItem: _sqlWhereParams) {
      Logger.info("==> param%s: '%s'",(_whereParamCounter++), StringUtils.checkSize(_whereParamItem));
    }
    // log for where parameters -- END
    int result = database().delete("person", _sqlWhereStatement, _sqlWhereParams.toArray(new String[_sqlWhereParams.size()]));;
  }

  /**
   * <h1>Content provider URI (DELETE operation):</h1>
   * <pre>content://sqlite.feature.contentprovider.kripton35/persons/#</pre>
   *
   * <h2>JQL DELETE for Content Provider</h2>
   * <pre>DELETE FROM Person WHERE id = ${id}</pre>
   *
   * <h2>SQL DELETE for Content Provider</h2>
   * <pre>DELETE FROM person WHERE id = ${id}</pre>
   *
   * <h3>Path variables defined:</h3>
   * <ul>
   * <li><strong>${id}</strong> at path segment 1</li>
   * </ul>
   *
   * <p><strong>Dynamic where statement is ignored, due no param with @BindSqlDynamicWhere was added.</strong></p>
   *
   * <p><strong>In URI, * is replaced with [*] for javadoc rapresentation</strong></p>
   *
   * @param uri "content://sqlite.feature.contentprovider.kripton35/persons/#"
   * @param selection dynamic part of <code>where</code> statement <b>NOT USED</b>
   * @param selectionArgs arguments of dynamic part of <code>where</code> statement <b>NOT USED</b>
   * @return number of effected rows
   */
  int deleteRaw2(Uri uri, String selection, String[] selectionArgs) {
    Logger.info("Execute DELETE for URI %s", uri.toString());
    StringBuilder _sqlBuilder=new StringBuilder();
    // generation CODE_001 -- BEGIN
    // generation CODE_001 -- END
    ArrayList<String> _sqlWhereParams=getWhereParamsArray();

    // manage WHERE arguments -- BEGIN

    // manage WHERE statement
    String _sqlWhereStatement=" id = ?";
    _sqlBuilder.append(_sqlWhereStatement);

    // manage WHERE arguments -- END
    // Add parameter id at path segment 1
    _sqlWhereParams.add(uri.getPathSegments().get(1));

    // display log
    Logger.info("DELETE FROM person WHERE id = ?");

    // log for where parameters -- BEGIN
    int _whereParamCounter=0;
    for (String _whereParamItem: _sqlWhereParams) {
      Logger.info("==> param%s: '%s'",(_whereParamCounter++), StringUtils.checkSize(_whereParamItem));
    }
    // log for where parameters -- END

    // execute SQL
    int result = database().delete("person", _sqlWhereStatement, _sqlWhereParams.toArray(new String[_sqlWhereParams.size()]));
    return result;
  }

  /**
   * <h2>SQL delete</h2>
   * <pre>DELETE FROM person WHERE id = ${id} AND #{DYNAMIC_WHERE}</pre>
   *
   *
   * <h2>Where parameters:</h2>
   * <dl>
   * 	<dt>${id}</dt><dd>is mapped to method's parameter <strong>id</strong></dd>
   * </dl>
   *
   * <dl>
   * <dt>where</dt><dd>is part of where conditions resolved at runtime. In above SQL compairs as #{DYNAMIC_WHERE}</dd>
   * </dl>
   *
   * <h2>Method's parameters and associated dynamic parts:</h2>
   * <dl>
   * <dt>where</dt><dd>is part of where conditions resolved at runtime. In above SQL compairs as #{DYNAMIC_WHERE}</dd></dl>
   *
   * @param id
   * 	is used as where parameter <strong>${id}</strong>
   * @param where
   * 	is used as dynamic where conditions
   * @param args
   * 	is used as updated field <strong>args</strong>
   */
  @Override
  public void deleteRaw(long id, String where, String[] args) {
    ArrayList<String> _sqlWhereParams=getWhereParamsArray();
    _sqlWhereParams.add(String.valueOf(id));

    StringBuilder _sqlBuilder=new StringBuilder();
    // generation CODE_001 -- BEGIN
    // initialize dynamic where
    String _sqlDynamicWhere=where;
    // initialize dynamic where args
    String[] _sqlDynamicWhereArgs=args;
    // generation CODE_001 -- END

    // manage WHERE arguments -- BEGIN

    // manage WHERE statement
    String _sqlWhereStatement=" id = ?"+StringUtils.ifNotEmptyAppend(_sqlDynamicWhere," AND ");
    _sqlBuilder.append(_sqlWhereStatement);

    // manage WHERE arguments -- END
    if (StringUtils.hasText(_sqlDynamicWhere) && _sqlDynamicWhereArgs!=null) {
      for (String _arg: _sqlDynamicWhereArgs) {
        _sqlWhereParams.add(_arg);
      }
    }

    // display log
    Logger.info("DELETE FROM person WHERE id = ?%s", StringUtils.ifNotEmptyAppend(_sqlDynamicWhere," AND "));

    // log for where parameters -- BEGIN
    int _whereParamCounter=0;
    for (String _whereParamItem: _sqlWhereParams) {
      Logger.info("==> param%s: '%s'",(_whereParamCounter++), StringUtils.checkSize(_whereParamItem));
    }
    // log for where parameters -- END
    int result = database().delete("person", _sqlWhereStatement, _sqlWhereParams.toArray(new String[_sqlWhereParams.size()]));;
  }

  /**
   * <h1>Content provider URI (DELETE operation):</h1>
   * <pre>content://sqlite.feature.contentprovider.kripton35/persons/test0/#</pre>
   *
   * <h2>JQL DELETE for Content Provider</h2>
   * <pre>DELETE FROM Person WHERE id = ${id} AND #{DYNAMIC_WHERE}</pre>
   *
   * <h2>SQL DELETE for Content Provider</h2>
   * <pre>DELETE FROM person WHERE id = ${id} AND #{DYNAMIC_WHERE}</pre>
   *
   * <h3>Path variables defined:</h3>
   * <ul>
   * <li><strong>${id}</strong> at path segment 2</li>
   * </ul>
   *
   * <p><strong>In URI, * is replaced with [*] for javadoc rapresentation</strong></p>
   *
   * @param uri "content://sqlite.feature.contentprovider.kripton35/persons/test0/#"
   * @param selection dynamic part of <code>where</code> statement 
   * @param selectionArgs arguments of dynamic part of <code>where</code> statement 
   * @return number of effected rows
   */
  int deleteRaw3(Uri uri, String selection, String[] selectionArgs) {
    Logger.info("Execute DELETE for URI %s", uri.toString());
    StringBuilder _sqlBuilder=new StringBuilder();
    // generation CODE_001 -- BEGIN
    // initialize dynamic where
    String _sqlDynamicWhere=selection;
    // initialize dynamic where args
    String[] _sqlDynamicWhereArgs=selectionArgs;
    // generation CODE_001 -- END
    ArrayList<String> _sqlWhereParams=getWhereParamsArray();

    // manage WHERE arguments -- BEGIN

    // manage WHERE statement
    String _sqlWhereStatement=" id = ?"+StringUtils.ifNotEmptyAppend(_sqlDynamicWhere," AND ");
    _sqlBuilder.append(_sqlWhereStatement);

    // manage WHERE arguments -- END
    if (StringUtils.hasText(_sqlDynamicWhere) && _sqlDynamicWhereArgs!=null) {
      for (String _arg: _sqlDynamicWhereArgs) {
        _sqlWhereParams.add(_arg);
      }
    }
    // Add parameter id at path segment 2
    _sqlWhereParams.add(uri.getPathSegments().get(2));
    if (StringUtils.hasText(_sqlDynamicWhere) && _sqlDynamicWhereArgs!=null) {
      for (String _arg: _sqlDynamicWhereArgs) {
        _sqlWhereParams.add(_arg);
      }
    }

    // display log
    Logger.info("DELETE FROM person WHERE id = ?%s", StringUtils.ifNotEmptyAppend(_sqlDynamicWhere," AND "));

    // log for where parameters -- BEGIN
    int _whereParamCounter=0;
    for (String _whereParamItem: _sqlWhereParams) {
      Logger.info("==> param%s: '%s'",(_whereParamCounter++), StringUtils.checkSize(_whereParamItem));
    }
    // log for where parameters -- END

    // execute SQL
    int result = database().delete("person", _sqlWhereStatement, _sqlWhereParams.toArray(new String[_sqlWhereParams.size()]));
    return result;
  }

  /**
   * <h2>SQL delete:</h2>
   * <pre>DELETE FROM Person WHERE id = ${bean.id}</pre>
   *
   * <h2>Parameters used in where conditions:</h2>
   * <dl>
   * 	<dt>${bean.id}</dt><dd>is mapped to method's parameter <strong>bean.id</strong></dd>
   * </dl>
   *
   * @param bean
   * 	is used as ${bean}
   */
  @Override
  public void deleteBean(Person bean) {
    ArrayList<String> _sqlWhereParams=getWhereParamsArray();
    _sqlWhereParams.add(String.valueOf(bean.id));

    StringBuilder _sqlBuilder=new StringBuilder();
    // generation CODE_001 -- BEGIN
    // generation CODE_001 -- END

    // manage WHERE arguments -- BEGIN

    // manage WHERE statement
    String _sqlWhereStatement=" id = ?";
    _sqlBuilder.append(_sqlWhereStatement);

    // manage WHERE arguments -- END

    // display log
    Logger.info("DELETE FROM person WHERE id = ?");

    // log for where parameters -- BEGIN
    int _whereParamCounter=0;
    for (String _whereParamItem: _sqlWhereParams) {
      Logger.info("==> param%s: '%s'",(_whereParamCounter++), StringUtils.checkSize(_whereParamItem));
    }
    // log for where parameters -- END
    int result = database().delete("person", _sqlWhereStatement, _sqlWhereParams.toArray(new String[_sqlWhereParams.size()]));;
  }

  /**
   * <h1>Content provider URI (DELETE operation):</h1>
   * <pre>content://sqlite.feature.contentprovider.kripton35/persons/test1/#</pre>
   *
   * <h2>JQL DELETE for Content Provider</h2>
   * <pre>DELETE FROM Person WHERE id = ${bean.id}</pre>
   *
   * <h2>SQL DELETE for Content Provider</h2>
   * <pre>DELETE FROM person WHERE id = ${bean.id}</pre>
   *
   * <h3>Path variables defined:</h3>
   * <ul>
   * <li><strong>${bean.id}</strong> at path segment 2</li>
   * </ul>
   *
   * <p><strong>Dynamic where statement is ignored, due no param with @BindSqlDynamicWhere was added.</strong></p>
   *
   * <p><strong>In URI, * is replaced with [*] for javadoc rapresentation</strong></p>
   *
   * @param uri "content://sqlite.feature.contentprovider.kripton35/persons/test1/#"
   * @param selection dynamic part of <code>where</code> statement <b>NOT USED</b>
   * @param selectionArgs arguments of dynamic part of <code>where</code> statement <b>NOT USED</b>
   * @return number of effected rows
   */
  int deleteBean4(Uri uri, String selection, String[] selectionArgs) {
    Logger.info("Execute DELETE for URI %s", uri.toString());
    StringBuilder _sqlBuilder=new StringBuilder();
    // generation CODE_001 -- BEGIN
    // generation CODE_001 -- END
    ArrayList<String> _sqlWhereParams=getWhereParamsArray();

    // manage WHERE arguments -- BEGIN

    // manage WHERE statement
    String _sqlWhereStatement=" id = ?";
    _sqlBuilder.append(_sqlWhereStatement);

    // manage WHERE arguments -- END
    // Add parameter bean.id at path segment 2
    _sqlWhereParams.add(uri.getPathSegments().get(2));

    // display log
    Logger.info("DELETE FROM person WHERE id = ?");

    // log for where parameters -- BEGIN
    int _whereParamCounter=0;
    for (String _whereParamItem: _sqlWhereParams) {
      Logger.info("==> param%s: '%s'",(_whereParamCounter++), StringUtils.checkSize(_whereParamItem));
    }
    // log for where parameters -- END

    // execute SQL
    int result = database().delete("person", _sqlWhereStatement, _sqlWhereParams.toArray(new String[_sqlWhereParams.size()]));
    return result;
  }

  /**
   * <h2>SQL update</h2>
   * <pre>UPDATE person SET name=${name} WHERE id=${id}</pre>
   *
   * <h2>Updated columns:</h2>
   * <ul>
   * </ul>
   *
   * <h2>Where parameters:</h2>
   * <dl>
   * 	<dt>${id}</dt><dd>is mapped to method's parameter <strong>id</strong></dd>
   * </dl>
   *
   * @param name
   * 	is used as updated field <strong>name</strong>
   * @param id
   * 	is used as where parameter <strong>${id}</strong>
   *
   * @return number of updated records
   */
  @Override
  public int updateRaw(String name, long id) {
    ContentValues contentValues=contentValues();
    contentValues.clear();
    if (name!=null) {
      contentValues.put("name", name);
    } else {
      contentValues.putNull("name");
    }

    ArrayList<String> _sqlWhereParams=getWhereParamsArray();
    _sqlWhereParams.add(String.valueOf(id));

    StringBuilder _sqlBuilder=new StringBuilder();
    // generation CODE_001 -- BEGIN
    // generation CODE_001 -- END

    // manage WHERE arguments -- BEGIN

    // manage WHERE statement
    String _sqlWhereStatement=" id=?";
    _sqlBuilder.append(_sqlWhereStatement);

    // manage WHERE arguments -- END

    // display log
    Logger.info("UPDATE person SET name=:name WHERE id=?");

    // log for content values -- BEGIN
    Object _contentValue;
    for (String _contentKey:contentValues.keySet()) {
      _contentValue=contentValues.get(_contentKey);
      if (_contentValue==null) {
        Logger.info("==> :%s = <null>", _contentKey);
      } else {
        Logger.info("==> :%s = '%s' (%s)", _contentKey, StringUtils.checkSize(_contentValue), _contentValue.getClass().getCanonicalName());
      }
    }
    // log for content values -- END

    // log for where parameters -- BEGIN
    int _whereParamCounter=0;
    for (String _whereParamItem: _sqlWhereParams) {
      Logger.info("==> param%s: '%s'",(_whereParamCounter++), StringUtils.checkSize(_whereParamItem));
    }
    // log for where parameters -- END
    int result = database().update("person", contentValues, _sqlWhereStatement, _sqlWhereParams.toArray(new String[_sqlWhereParams.size()]));;
    return result;
  }

  /**
   * <h1>Content provider URI (UPDATE operation):</h1>
   * <pre>content://sqlite.feature.contentprovider.kripton35/persons/#</pre>
   *
   * <h2>JQL UPDATE for Content Provider</h2>
   * <pre>UPDATE Person SET name=${name} WHERE id=${id}</pre>
   *
   * <h2>SQL UPDATE for Content Provider</h2>
   * <pre>UPDATE person SET name=${name} WHERE id=${id}</pre>
   *
   * <h3>Path variables defined:</h3>
   * <ul>
   * <li><strong>${id}</strong> at path segment 1</li>
   * </ul>
   *
   * <p><strong>Dynamic where statement is ignored, due no param with @BindSqlDynamicWhere was added.</strong></p>
   *
   * <p><strong>In URI, * is replaced with [*] for javadoc rapresentation</strong></p>
   *
   * @param uri "content://sqlite.feature.contentprovider.kripton35/persons/#"
   * @param contentValues content values
   * @param selection dynamic part of <code>where</code> statement <b>NOT USED</b>
   * @param selectionArgs arguments of dynamic part of <code>where</code> statement <b>NOT USED</b>
   * @return number of effected rows
   */
  int updateRaw5(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
    Logger.info("Execute UPDATE for URI %s", uri.toString());
    StringBuilder _sqlBuilder=new StringBuilder();
    // generation CODE_001 -- BEGIN
    // generation CODE_001 -- END
    ArrayList<String> _sqlWhereParams=getWhereParamsArray();

    // manage WHERE arguments -- BEGIN

    // manage WHERE statement
    String _sqlWhereStatement=" id=?";
    _sqlBuilder.append(_sqlWhereStatement);

    // manage WHERE arguments -- END
    // Add parameter id at path segment 1
    _sqlWhereParams.add(uri.getPathSegments().get(1));
    for (String columnName:contentValues.keySet()) {
      if (!updateRaw5ColumnSet.contains(columnName)) {
        throw new KriptonRuntimeException(String.format("For URI 'content://sqlite.feature.contentprovider.kripton35/persons/#', column '%s' does not exists in table '%s' or can not be defined in this UPDATE operation", columnName, "person" ));
      }
    }

    // display log
    Logger.info("UPDATE person SET name=:name WHERE id=?");

    // log for content values -- BEGIN
    Object _contentValue;
    for (String _contentKey:contentValues.keySet()) {
      _contentValue=contentValues.get(_contentKey);
      if (_contentValue==null) {
        Logger.info("==> :%s = <null>", _contentKey);
      } else {
        Logger.info("==> :%s = '%s' (%s)", _contentKey, StringUtils.checkSize(_contentValue), _contentValue.getClass().getCanonicalName());
      }
    }
    // log for content values -- END

    // log for where parameters -- BEGIN
    int _whereParamCounter=0;
    for (String _whereParamItem: _sqlWhereParams) {
      Logger.info("==> param%s: '%s'",(_whereParamCounter++), StringUtils.checkSize(_whereParamItem));
    }
    // log for where parameters -- END

    // execute SQL
    int result = database().update("person", contentValues, _sqlWhereStatement, _sqlWhereParams.toArray(new String[_sqlWhereParams.size()]));
    return result;
  }

  /**
   * <h2>SQL update</h2>
   * <pre>UPDATE person SET name=${name} WHERE id=${id} AND #{DYNAMIC_WHERE}</pre>
   *
   * <h2>Updated columns:</h2>
   * <ul>
   * </ul>
   *
   * <h2>Where parameters:</h2>
   * <dl>
   * 	<dt>${id}</dt><dd>is mapped to method's parameter <strong>id</strong></dd>
   * </dl>
   *
   * <dl>
   * <dt>where</dt><dd>is part of where conditions resolved at runtime. In above SQL compairs as #{DYNAMIC_WHERE}</dd>
   * </dl>
   *
   * <h2>Method's parameters and associated dynamic parts:</h2>
   * <dl>
   * <dt>where</dt><dd>is part of where conditions resolved at runtime. In above SQL compairs as #{DYNAMIC_WHERE}</dd></dl>
   *
   * @param name
   * 	is used as updated field <strong>name</strong>
   * @param id
   * 	is used as where parameter <strong>${id}</strong>
   * @param where
   * 	is used as dynamic where conditions
   *
   * @return number of updated records
   */
  @Override
  public int updateRaw(String name, long id, String where) {
    ContentValues contentValues=contentValues();
    contentValues.clear();
    if (name!=null) {
      contentValues.put("name", name);
    } else {
      contentValues.putNull("name");
    }

    ArrayList<String> _sqlWhereParams=getWhereParamsArray();
    _sqlWhereParams.add(String.valueOf(id));

    StringBuilder _sqlBuilder=new StringBuilder();
    // generation CODE_001 -- BEGIN
    // initialize dynamic where
    String _sqlDynamicWhere=where;
    // generation CODE_001 -- END

    // manage WHERE arguments -- BEGIN

    // manage WHERE statement
    String _sqlWhereStatement=" id=?"+StringUtils.ifNotEmptyAppend(_sqlDynamicWhere," AND ");
    _sqlBuilder.append(_sqlWhereStatement);

    // manage WHERE arguments -- END

    // display log
    Logger.info("UPDATE person SET name=:name WHERE id=?%s", StringUtils.ifNotEmptyAppend(_sqlDynamicWhere," AND "));

    // log for content values -- BEGIN
    Object _contentValue;
    for (String _contentKey:contentValues.keySet()) {
      _contentValue=contentValues.get(_contentKey);
      if (_contentValue==null) {
        Logger.info("==> :%s = <null>", _contentKey);
      } else {
        Logger.info("==> :%s = '%s' (%s)", _contentKey, StringUtils.checkSize(_contentValue), _contentValue.getClass().getCanonicalName());
      }
    }
    // log for content values -- END

    // log for where parameters -- BEGIN
    int _whereParamCounter=0;
    for (String _whereParamItem: _sqlWhereParams) {
      Logger.info("==> param%s: '%s'",(_whereParamCounter++), StringUtils.checkSize(_whereParamItem));
    }
    // log for where parameters -- END
    int result = database().update("person", contentValues, _sqlWhereStatement, _sqlWhereParams.toArray(new String[_sqlWhereParams.size()]));;
    return result;
  }

  /**
   * <h1>Content provider URI (UPDATE operation):</h1>
   * <pre>content://sqlite.feature.contentprovider.kripton35/persons/test1/#</pre>
   *
   * <h2>JQL UPDATE for Content Provider</h2>
   * <pre>UPDATE Person SET name=${name} WHERE id=${id} AND #{DYNAMIC_WHERE}</pre>
   *
   * <h2>SQL UPDATE for Content Provider</h2>
   * <pre>UPDATE person SET name=${name} WHERE id=${id} AND #{DYNAMIC_WHERE}</pre>
   *
   * <h3>Path variables defined:</h3>
   * <ul>
   * <li><strong>${id}</strong> at path segment 2</li>
   * </ul>
   *
   * <p><strong>In URI, * is replaced with [*] for javadoc rapresentation</strong></p>
   *
   * @param uri "content://sqlite.feature.contentprovider.kripton35/persons/test1/#"
   * @param contentValues content values
   * @param selection dynamic part of <code>where</code> statement 
   * @param selectionArgs arguments of dynamic part of <code>where</code> statement 
   * @return number of effected rows
   */
  int updateRaw6(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
    Logger.info("Execute UPDATE for URI %s", uri.toString());
    StringBuilder _sqlBuilder=new StringBuilder();
    // generation CODE_001 -- BEGIN
    // initialize dynamic where
    String _sqlDynamicWhere=selection;
    // generation CODE_001 -- END
    ArrayList<String> _sqlWhereParams=getWhereParamsArray();

    // manage WHERE arguments -- BEGIN

    // manage WHERE statement
    String _sqlWhereStatement=" id=?"+StringUtils.ifNotEmptyAppend(_sqlDynamicWhere," AND ");
    _sqlBuilder.append(_sqlWhereStatement);

    // manage WHERE arguments -- END
    // Add parameter id at path segment 2
    _sqlWhereParams.add(uri.getPathSegments().get(2));
    for (String columnName:contentValues.keySet()) {
      if (!updateRaw6ColumnSet.contains(columnName)) {
        throw new KriptonRuntimeException(String.format("For URI 'content://sqlite.feature.contentprovider.kripton35/persons/test1/#', column '%s' does not exists in table '%s' or can not be defined in this UPDATE operation", columnName, "person" ));
      }
    }

    // display log
    Logger.info("UPDATE person SET name=:name WHERE id=?%s", StringUtils.ifNotEmptyAppend(_sqlDynamicWhere," AND "));

    // log for content values -- BEGIN
    Object _contentValue;
    for (String _contentKey:contentValues.keySet()) {
      _contentValue=contentValues.get(_contentKey);
      if (_contentValue==null) {
        Logger.info("==> :%s = <null>", _contentKey);
      } else {
        Logger.info("==> :%s = '%s' (%s)", _contentKey, StringUtils.checkSize(_contentValue), _contentValue.getClass().getCanonicalName());
      }
    }
    // log for content values -- END

    // log for where parameters -- BEGIN
    int _whereParamCounter=0;
    for (String _whereParamItem: _sqlWhereParams) {
      Logger.info("==> param%s: '%s'",(_whereParamCounter++), StringUtils.checkSize(_whereParamItem));
    }
    // log for where parameters -- END

    // execute SQL
    int result = database().update("person", contentValues, _sqlWhereStatement, _sqlWhereParams.toArray(new String[_sqlWhereParams.size()]));
    return result;
  }

  /**
   * <h2>SQL update</h2>
   * <pre>UPDATE person SET name=${name} WHERE id=${id} AND #{DYNAMIC_WHERE}</pre>
   *
   * <h2>Updated columns:</h2>
   * <ul>
   * </ul>
   *
   * <h2>Where parameters:</h2>
   * <dl>
   * 	<dt>${id}</dt><dd>is mapped to method's parameter <strong>id</strong></dd>
   * </dl>
   *
   * <dl>
   * <dt>where</dt><dd>is part of where conditions resolved at runtime. In above SQL compairs as #{DYNAMIC_WHERE}</dd>
   * </dl>
   *
   * <h2>Method's parameters and associated dynamic parts:</h2>
   * <dl>
   * <dt>where</dt><dd>is part of where conditions resolved at runtime. In above SQL compairs as #{DYNAMIC_WHERE}</dd></dl>
   *
   * @param name
   * 	is used as updated field <strong>name</strong>
   * @param id
   * 	is used as where parameter <strong>${id}</strong>
   * @param where
   * 	is used as dynamic where conditions
   * @param args
   * 	is used as updated field <strong>args</strong>
   *
   * @return number of updated records
   */
  @Override
  public int updateRaw(String name, long id, String where, String[] args) {
    ContentValues contentValues=contentValues();
    contentValues.clear();
    if (name!=null) {
      contentValues.put("name", name);
    } else {
      contentValues.putNull("name");
    }

    ArrayList<String> _sqlWhereParams=getWhereParamsArray();
    _sqlWhereParams.add(String.valueOf(id));

    StringBuilder _sqlBuilder=new StringBuilder();
    // generation CODE_001 -- BEGIN
    // initialize dynamic where
    String _sqlDynamicWhere=where;
    // initialize dynamic where args
    String[] _sqlDynamicWhereArgs=args;
    // generation CODE_001 -- END

    // manage WHERE arguments -- BEGIN

    // manage WHERE statement
    String _sqlWhereStatement=" id=?"+StringUtils.ifNotEmptyAppend(_sqlDynamicWhere," AND ");
    _sqlBuilder.append(_sqlWhereStatement);

    // manage WHERE arguments -- END
    if (StringUtils.hasText(_sqlDynamicWhere) && _sqlDynamicWhereArgs!=null) {
      for (String _arg: _sqlDynamicWhereArgs) {
        _sqlWhereParams.add(_arg);
      }
    }

    // display log
    Logger.info("UPDATE person SET name=:name WHERE id=?%s", StringUtils.ifNotEmptyAppend(_sqlDynamicWhere," AND "));

    // log for content values -- BEGIN
    Object _contentValue;
    for (String _contentKey:contentValues.keySet()) {
      _contentValue=contentValues.get(_contentKey);
      if (_contentValue==null) {
        Logger.info("==> :%s = <null>", _contentKey);
      } else {
        Logger.info("==> :%s = '%s' (%s)", _contentKey, StringUtils.checkSize(_contentValue), _contentValue.getClass().getCanonicalName());
      }
    }
    // log for content values -- END

    // log for where parameters -- BEGIN
    int _whereParamCounter=0;
    for (String _whereParamItem: _sqlWhereParams) {
      Logger.info("==> param%s: '%s'",(_whereParamCounter++), StringUtils.checkSize(_whereParamItem));
    }
    // log for where parameters -- END
    int result = database().update("person", contentValues, _sqlWhereStatement, _sqlWhereParams.toArray(new String[_sqlWhereParams.size()]));;
    return result;
  }

  /**
   * <h1>Content provider URI (UPDATE operation):</h1>
   * <pre>content://sqlite.feature.contentprovider.kripton35/persons/test2/#</pre>
   *
   * <h2>JQL UPDATE for Content Provider</h2>
   * <pre>UPDATE Person SET name=${name} WHERE id=${id} AND #{DYNAMIC_WHERE}</pre>
   *
   * <h2>SQL UPDATE for Content Provider</h2>
   * <pre>UPDATE person SET name=${name} WHERE id=${id} AND #{DYNAMIC_WHERE}</pre>
   *
   * <h3>Path variables defined:</h3>
   * <ul>
   * <li><strong>${id}</strong> at path segment 2</li>
   * </ul>
   *
   * <p><strong>In URI, * is replaced with [*] for javadoc rapresentation</strong></p>
   *
   * @param uri "content://sqlite.feature.contentprovider.kripton35/persons/test2/#"
   * @param contentValues content values
   * @param selection dynamic part of <code>where</code> statement 
   * @param selectionArgs arguments of dynamic part of <code>where</code> statement 
   * @return number of effected rows
   */
  int updateRaw7(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
    Logger.info("Execute UPDATE for URI %s", uri.toString());
    StringBuilder _sqlBuilder=new StringBuilder();
    // generation CODE_001 -- BEGIN
    // initialize dynamic where
    String _sqlDynamicWhere=selection;
    // initialize dynamic where args
    String[] _sqlDynamicWhereArgs=selectionArgs;
    // generation CODE_001 -- END
    ArrayList<String> _sqlWhereParams=getWhereParamsArray();

    // manage WHERE arguments -- BEGIN

    // manage WHERE statement
    String _sqlWhereStatement=" id=?"+StringUtils.ifNotEmptyAppend(_sqlDynamicWhere," AND ");
    _sqlBuilder.append(_sqlWhereStatement);

    // manage WHERE arguments -- END
    if (StringUtils.hasText(_sqlDynamicWhere) && _sqlDynamicWhereArgs!=null) {
      for (String _arg: _sqlDynamicWhereArgs) {
        _sqlWhereParams.add(_arg);
      }
    }
    // Add parameter id at path segment 2
    _sqlWhereParams.add(uri.getPathSegments().get(2));
    if (StringUtils.hasText(_sqlDynamicWhere) && _sqlDynamicWhereArgs!=null) {
      for (String _arg: _sqlDynamicWhereArgs) {
        _sqlWhereParams.add(_arg);
      }
    }
    for (String columnName:contentValues.keySet()) {
      if (!updateRaw7ColumnSet.contains(columnName)) {
        throw new KriptonRuntimeException(String.format("For URI 'content://sqlite.feature.contentprovider.kripton35/persons/test2/#', column '%s' does not exists in table '%s' or can not be defined in this UPDATE operation", columnName, "person" ));
      }
    }

    // display log
    Logger.info("UPDATE person SET name=:name WHERE id=?%s", StringUtils.ifNotEmptyAppend(_sqlDynamicWhere," AND "));

    // log for content values -- BEGIN
    Object _contentValue;
    for (String _contentKey:contentValues.keySet()) {
      _contentValue=contentValues.get(_contentKey);
      if (_contentValue==null) {
        Logger.info("==> :%s = <null>", _contentKey);
      } else {
        Logger.info("==> :%s = '%s' (%s)", _contentKey, StringUtils.checkSize(_contentValue), _contentValue.getClass().getCanonicalName());
      }
    }
    // log for content values -- END

    // log for where parameters -- BEGIN
    int _whereParamCounter=0;
    for (String _whereParamItem: _sqlWhereParams) {
      Logger.info("==> param%s: '%s'",(_whereParamCounter++), StringUtils.checkSize(_whereParamItem));
    }
    // log for where parameters -- END

    // execute SQL
    int result = database().update("person", contentValues, _sqlWhereStatement, _sqlWhereParams.toArray(new String[_sqlWhereParams.size()]));
    return result;
  }

  /**
   * <h2>SQL update:</h2>
   * <pre>UPDATE Person SET parentId=${person.parentId}, birthCity=${person.birthCity}, birthDay=${person.birthDay}, value=${person.value}, name=${person.name}, surname=${person.surname} WHERE id=${person.id}</pre>
   *
   * <h2>Updated columns:</h2>
   * <dl>
   * 	<dt>alias_parent_id</dt><dd>is mapped to <strong>${person.parentId}</strong></dd>
   * 	<dt>birth_city</dt><dd>is mapped to <strong>${person.birthCity}</strong></dd>
   * 	<dt>birth_day</dt><dd>is mapped to <strong>${person.birthDay}</strong></dd>
   * 	<dt>value</dt><dd>is mapped to <strong>${person.value}</strong></dd>
   * 	<dt>name</dt><dd>is mapped to <strong>${person.name}</strong></dd>
   * 	<dt>surname</dt><dd>is mapped to <strong>${person.surname}</strong></dd>
   * </dl>
   *
   * <h2>Parameters used in where conditions:</h2>
   * <dl>
   * 	<dt>${person.id}</dt><dd>is mapped to method's parameter <strong>person.id</strong></dd>
   * </dl>
   *
   * @param person
   * 	is used as ${person}
   *
   * @return number of updated records
   */
  @Override
  public int updateBean(Person person) {
    ContentValues contentValues=contentValues();
    contentValues.clear();

    contentValues.put("alias_parent_id", person.parentId);
    if (person.birthCity!=null) {
      contentValues.put("birth_city", person.birthCity);
    } else {
      contentValues.putNull("birth_city");
    }
    if (person.birthDay!=null) {
      contentValues.put("birth_day", DateUtils.write(person.birthDay));
    } else {
      contentValues.putNull("birth_day");
    }
    contentValues.put("value", person.value);
    if (person.getName()!=null) {
      contentValues.put("name", person.getName());
    } else {
      contentValues.putNull("name");
    }
    if (person.getSurname()!=null) {
      contentValues.put("surname", person.getSurname());
    } else {
      contentValues.putNull("surname");
    }

    ArrayList<String> _sqlWhereParams=getWhereParamsArray();
    _sqlWhereParams.add(String.valueOf(person.id));

    StringBuilder _sqlBuilder=new StringBuilder();
    // generation CODE_001 -- BEGIN
    // generation CODE_001 -- END

    // manage WHERE arguments -- BEGIN

    // manage WHERE statement
    String _sqlWhereStatement=" id=?";
    _sqlBuilder.append(_sqlWhereStatement);

    // manage WHERE arguments -- END

    // display log
    Logger.info("UPDATE person SET parentId=:parentId, birthCity=:birthCity, birthDay=:birthDay, value=:value, name=:name, surname=:surname WHERE id=?");

    // log for content values -- BEGIN
    Object _contentValue;
    for (String _contentKey:contentValues.keySet()) {
      _contentValue=contentValues.get(_contentKey);
      if (_contentValue==null) {
        Logger.info("==> :%s = <null>", _contentKey);
      } else {
        Logger.info("==> :%s = '%s' (%s)", _contentKey, StringUtils.checkSize(_contentValue), _contentValue.getClass().getCanonicalName());
      }
    }
    // log for content values -- END

    // log for where parameters -- BEGIN
    int _whereParamCounter=0;
    for (String _whereParamItem: _sqlWhereParams) {
      Logger.info("==> param%s: '%s'",(_whereParamCounter++), StringUtils.checkSize(_whereParamItem));
    }
    // log for where parameters -- END
    int result = database().update("person", contentValues, _sqlWhereStatement, _sqlWhereParams.toArray(new String[_sqlWhereParams.size()]));;
    return result;
  }

  /**
   * <h1>Content provider URI (UPDATE operation):</h1>
   * <pre>content://sqlite.feature.contentprovider.kripton35/persons/test3/#</pre>
   *
   * <h2>JQL UPDATE for Content Provider</h2>
   * <pre>UPDATE Person SET parentId=${person.parentId}, birthCity=${person.birthCity}, birthDay=${person.birthDay}, value=${person.value}, name=${person.name}, surname=${person.surname} WHERE id=${person.id}</pre>
   *
   * <h2>SQL UPDATE for Content Provider</h2>
   * <pre>UPDATE person SET parentId=${person.parentId}, birthCity=${person.birthCity}, birthDay=${person.birthDay}, value=${person.value}, name=${person.name}, surname=${person.surname} WHERE id=${person.id}</pre>
   *
   * <h3>Path variables defined:</h3>
   * <ul>
   * <li><strong>${person.id}</strong> at path segment 2</li>
   * </ul>
   *
   * <p><strong>Dynamic where statement is ignored, due no param with @BindSqlDynamicWhere was added.</strong></p>
   *
   * <p><strong>In URI, * is replaced with [*] for javadoc rapresentation</strong></p>
   *
   * @param uri "content://sqlite.feature.contentprovider.kripton35/persons/test3/#"
   * @param contentValues content values
   * @param selection dynamic part of <code>where</code> statement <b>NOT USED</b>
   * @param selectionArgs arguments of dynamic part of <code>where</code> statement <b>NOT USED</b>
   * @return number of effected rows
   */
  int updateBean8(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
    Logger.info("Execute UPDATE for URI %s", uri.toString());
    StringBuilder _sqlBuilder=new StringBuilder();
    // generation CODE_001 -- BEGIN
    // generation CODE_001 -- END
    ArrayList<String> _sqlWhereParams=getWhereParamsArray();

    // manage WHERE arguments -- BEGIN

    // manage WHERE statement
    String _sqlWhereStatement=" id=?";
    _sqlBuilder.append(_sqlWhereStatement);

    // manage WHERE arguments -- END
    // Add parameter person.id at path segment 2
    _sqlWhereParams.add(uri.getPathSegments().get(2));
    for (String columnName:contentValues.keySet()) {
      if (!updateBean8ColumnSet.contains(columnName)) {
        throw new KriptonRuntimeException(String.format("For URI 'content://sqlite.feature.contentprovider.kripton35/persons/test3/#', column '%s' does not exists in table '%s' or can not be defined in this UPDATE operation", columnName, "person" ));
      }
    }

    // display log
    Logger.info("UPDATE person SET parentId=:parentId, birthCity=:birthCity, birthDay=:birthDay, value=:value, name=:name, surname=:surname WHERE id=?");

    // log for content values -- BEGIN
    Object _contentValue;
    for (String _contentKey:contentValues.keySet()) {
      _contentValue=contentValues.get(_contentKey);
      if (_contentValue==null) {
        Logger.info("==> :%s = <null>", _contentKey);
      } else {
        Logger.info("==> :%s = '%s' (%s)", _contentKey, StringUtils.checkSize(_contentValue), _contentValue.getClass().getCanonicalName());
      }
    }
    // log for content values -- END

    // log for where parameters -- BEGIN
    int _whereParamCounter=0;
    for (String _whereParamItem: _sqlWhereParams) {
      Logger.info("==> param%s: '%s'",(_whereParamCounter++), StringUtils.checkSize(_whereParamItem));
    }
    // log for where parameters -- END

    // execute SQL
    int result = database().update("person", contentValues, _sqlWhereStatement, _sqlWhereParams.toArray(new String[_sqlWhereParams.size()]));
    return result;
  }

  /**
   * <h2>Select SQL:</h2>
   *
   * <pre>SELECT id, alias_parent_id, birth_city, birth_day, value, name, surname FROM person WHERE name like ${nameTemp} || '%' GROUP BY id HAVING id=2 ORDER BY id,  #{DYNAMIC_ORDER_BY}</pre>
   *
   * <h2>Projected columns:</h2>
   * <dl>
   * 	<dt>id</dt><dd>is associated to bean's property <strong>id</strong></dd>
   * 	<dt>alias_parent_id</dt><dd>is associated to bean's property <strong>parentId</strong></dd>
   * 	<dt>birth_city</dt><dd>is associated to bean's property <strong>birthCity</strong></dd>
   * 	<dt>birth_day</dt><dd>is associated to bean's property <strong>birthDay</strong></dd>
   * 	<dt>value</dt><dd>is associated to bean's property <strong>value</strong></dd>
   * 	<dt>name</dt><dd>is associated to bean's property <strong>name</strong></dd>
   * 	<dt>surname</dt><dd>is associated to bean's property <strong>surname</strong></dd>
   * </dl>
   *
   * <h2>Method's parameters and associated dynamic parts:</h2>
   * <dl>
   * <dt>orderBy</dt>is part of order statement resolved at runtime. In above SQL compairs as #{DYNAMIC_ORDER_BY}</dd>
   * </dl>
   *
   * <h2>Query's parameters:</h2>
   * <dl>
   * 	<dt>${nameTemp}</dt><dd>is binded to method's parameter <strong>nameValue</strong></dd>
   * </dl>
   *
   * @param nameValue
   * 	is binded to <code>${nameTemp}</code>
   * @param orderBy
   * 	is used as <strong>dynamic ORDER BY statement</strong> and it is formatted by ({@link StringUtils#format})
   * @return collection of bean or empty collection.
   */
  @Override
  public List<Person> selectOne(String nameValue, String orderBy) {
    StringBuilder _sqlBuilder=new StringBuilder();
    _sqlBuilder.append("SELECT id, alias_parent_id, birth_city, birth_day, value, name, surname FROM person");
    // generation CODE_001 -- BEGIN
    // generation CODE_001 -- END
    String _sortOrder=orderBy;
    ArrayList<String> _sqlWhereParams=getWhereParamsArray();

    // manage WHERE arguments -- BEGIN

    // manage WHERE statement
    String _sqlWhereStatement=" WHERE name like ? || '%'";
    _sqlBuilder.append(_sqlWhereStatement);

    // manage WHERE arguments -- END

    // build where condition
    _sqlWhereParams.add((nameValue==null?"":nameValue));

    // manage GROUP BY statement
    String _sqlGroupByStatement=" GROUP BY id";
    _sqlBuilder.append(_sqlGroupByStatement);

    // manage HAVING statement
    String _sqlHavingStatement=" HAVING id=2";
    _sqlBuilder.append(_sqlHavingStatement);
    // generation order - BEGIN
    String _sqlOrderByStatement=" ORDER BY id"+StringUtils.ifNotEmptyAppend(_sortOrder, ", ");
    _sqlBuilder.append(_sqlOrderByStatement);
    // generation order - END

    //StringUtils, SqlUtils will be used in case of dynamic parts of SQL
    String _sql=_sqlBuilder.toString();
    String[] _sqlArgs=_sqlWhereParams.toArray(new String[_sqlWhereParams.size()]);
    Logger.info(_sql);

    // log for where parameters -- BEGIN
    int _whereParamCounter=0;
    for (String _whereParamItem: _sqlWhereParams) {
      Logger.info("==> param%s: '%s'",(_whereParamCounter++), StringUtils.checkSize(_whereParamItem));
    }
    // log for where parameters -- END
    try (Cursor cursor = database().rawQuery(_sql, _sqlArgs)) {
      Logger.info("Rows found: %s",cursor.getCount());

      LinkedList<Person> resultList=new LinkedList<Person>();
      Person resultBean=null;

      if (cursor.moveToFirst()) {

        int index0=cursor.getColumnIndex("id");
        int index1=cursor.getColumnIndex("alias_parent_id");
        int index2=cursor.getColumnIndex("birth_city");
        int index3=cursor.getColumnIndex("birth_day");
        int index4=cursor.getColumnIndex("value");
        int index5=cursor.getColumnIndex("name");
        int index6=cursor.getColumnIndex("surname");

        do
         {
          resultBean=new Person();

          resultBean.id=cursor.getLong(index0);
          if (!cursor.isNull(index1)) { resultBean.parentId=cursor.getLong(index1); }
          if (!cursor.isNull(index2)) { resultBean.birthCity=cursor.getString(index2); }
          if (!cursor.isNull(index3)) { resultBean.birthDay=DateUtils.read(cursor.getString(index3)); }
          if (!cursor.isNull(index4)) { resultBean.value=cursor.getLong(index4); }
          if (!cursor.isNull(index5)) { resultBean.setName(cursor.getString(index5)); }
          if (!cursor.isNull(index6)) { resultBean.setSurname(cursor.getString(index6)); }

          resultList.add(resultBean);
        } while (cursor.moveToNext());
      }

      return resultList;
    }
  }

  /**
   * <h1>Content provider URI (SELECT operation):</h1>
   * <pre>content://sqlite.feature.contentprovider.kripton35/persons/[*]/test0</pre>
   *
   * <h2>JQL SELECT for Content Provider</h2>
   * <pre>SELECT id, parentId, birthCity, birthDay, value, name, surname FROM Person WHERE name like ${nameTemp} || '%' GROUP BY id HAVING id=2 ORDER BY id,  #{DYNAMIC_ORDER_BY}</pre>
   *
   * <h2>SQL SELECT for Content Provider</h2>
   * <pre>SELECT id, alias_parent_id, birth_city, birth_day, value, name, surname FROM person WHERE name like ${nameTemp} || '%' GROUP BY id HAVING id=2 ORDER BY id,  #{DYNAMIC_ORDER_BY}</pre>
   *
   * <h3>Path variables defined:</h3>
   * <ul>
   * <li><strong>${nameTemp}</strong> at path segment 1</li>
   * </ul>
   *
   * <p><strong>Dynamic where statement is ignored, due no param with @BindSqlDynamicWhere was added.</strong></p>
   *
   * <p><strong>In URI, * is replaced with [*] for javadoc rapresentation</strong></p>
   *
   * @param uri "content://sqlite.feature.contentprovider.kripton35/persons/[*]/test0"
   * @param selection dynamic part of <code>where</code> statement <b>NOT USED</b>
   * @param selectionArgs arguments of dynamic part of <code>where</code> statement <b>NOT USED</b>
   * @return number of effected rows
   */
  Cursor selectOne9(Uri uri, String[] projection, String selection, String[] selectionArgs,
      String sortOrder) {
    Logger.info("Execute SELECT for URI %s", uri.toString());
    StringBuilder _sqlBuilder=new StringBuilder();
    // generation CODE_001 -- BEGIN
    // generation CODE_001 -- END
    StringBuilder _projectionBuffer=new StringBuilder();
    String _sortOrder=sortOrder;
    _sqlBuilder.append("SELECT %s FROM person    ");
    ArrayList<String> _sqlWhereParams=getWhereParamsArray();

    // manage WHERE arguments -- BEGIN

    // manage WHERE statement
    String _sqlWhereStatement=" WHERE name like ? || '%'";
    _sqlBuilder.append(_sqlWhereStatement);

    // manage WHERE arguments -- END

    // manage GROUP BY statement
    String _sqlGroupByStatement=" GROUP BY id";
    _sqlBuilder.append(_sqlGroupByStatement);

    // manage HAVING statement
    String _sqlHavingStatement=" HAVING id=2";
    _sqlBuilder.append(_sqlHavingStatement);
    // generation order - BEGIN
    String _sqlOrderByStatement=" ORDER BY id"+StringUtils.ifNotEmptyAppend(_sortOrder, ", ");
    _sqlBuilder.append(_sqlOrderByStatement);
    // generation order - END


    // manage projected columns
    String _columnSeparator="";
    if (projection!=null && projection.length>0) {
      for (String columnName:projection) {
        if (!selectOne9ColumnSet.contains(columnName)) {
          throw new KriptonRuntimeException(String.format("For URI 'content://sqlite.feature.contentprovider.kripton35/persons/*/test0', column '%s' does not exists in table '%s' or can not be defined in this SELECT operation", columnName, "person" ));
        }
        _projectionBuffer.append(_columnSeparator + columnName);
        _columnSeparator=", ";
      }
    } else {
      for (String column: selectOne9ColumnSet) {
        _projectionBuffer.append(_columnSeparator + column);
        _columnSeparator=", ";
      }
    }
    // Add parameter nameTemp at path segment 1
    _sqlWhereParams.add(uri.getPathSegments().get(1));

    // manage log
    String _sql=String.format(_sqlBuilder.toString(), _projectionBuffer.toString());
    Logger.info(_sql);

    // log for where parameters -- BEGIN
    int _whereParamCounter=0;
    for (String _whereParamItem: _sqlWhereParams) {
      Logger.info("==> param%s: '%s'",(_whereParamCounter++), StringUtils.checkSize(_whereParamItem));
    }
    // log for where parameters -- END

    // execute query
    Cursor _result = database().rawQuery(_sql, _sqlWhereParams.toArray(new String[_sqlWhereParams.size()]));
    return _result;
  }

  /**
   * <h2>Select SQL:</h2>
   *
   * <pre>SELECT id, alias_parent_id, birth_city, birth_day, value, name, surname FROM person WHERE #{DYNAMIC_WHERE} ORDER BY name asc,  #{DYNAMIC_ORDER_BY}</pre>
   *
   * <h2>Projected columns:</h2>
   * <dl>
   * 	<dt>id</dt><dd>is associated to bean's property <strong>id</strong></dd>
   * 	<dt>alias_parent_id</dt><dd>is associated to bean's property <strong>parentId</strong></dd>
   * 	<dt>birth_city</dt><dd>is associated to bean's property <strong>birthCity</strong></dd>
   * 	<dt>birth_day</dt><dd>is associated to bean's property <strong>birthDay</strong></dd>
   * 	<dt>value</dt><dd>is associated to bean's property <strong>value</strong></dd>
   * 	<dt>name</dt><dd>is associated to bean's property <strong>name</strong></dd>
   * 	<dt>surname</dt><dd>is associated to bean's property <strong>surname</strong></dd>
   * </dl>
   *
   * <h2>Method's parameters and associated dynamic parts:</h2>
   * <dl>
   * <dt>where</dt><dd>is part of where conditions resolved at runtime. In above SQL compairs as #{DYNAMIC_WHERE}</dd><dt>order</dt>is part of order statement resolved at runtime. In above SQL compairs as #{DYNAMIC_ORDER_BY}</dd>
   * </dl>
   *
   * @param where
   * 	is used as <strong>dynamic WHERE statement</strong> and it is formatted by ({@link StringUtils#format})
   * @param args
   * 	is binded to <code>${args}</code>
   * @param order
   * 	is used as <strong>dynamic ORDER BY statement</strong> and it is formatted by ({@link StringUtils#format})
   * @return collection of bean or empty collection.
   */
  @Override
  public List<Person> selectAll(String where, String[] args, String order) {
    StringBuilder _sqlBuilder=new StringBuilder();
    _sqlBuilder.append("SELECT id, alias_parent_id, birth_city, birth_day, value, name, surname FROM person");
    // generation CODE_001 -- BEGIN
    // initialize dynamic where
    String _sqlDynamicWhere=where;
    // initialize dynamic where args
    String[] _sqlDynamicWhereArgs=args;
    // generation CODE_001 -- END
    String _sortOrder=order;
    ArrayList<String> _sqlWhereParams=getWhereParamsArray();

    // manage WHERE arguments -- BEGIN

    // manage WHERE statement
    String _sqlWhereStatement=StringUtils.ifNotEmptyAppend(_sqlDynamicWhere, " WHERE ");
    _sqlBuilder.append(_sqlWhereStatement);

    // manage WHERE arguments -- END
    if (StringUtils.hasText(_sqlDynamicWhere) && _sqlDynamicWhereArgs!=null) {
      for (String _arg: _sqlDynamicWhereArgs) {
        _sqlWhereParams.add(_arg);
      }
    }

    // build where condition
    // generation order - BEGIN
    String _sqlOrderByStatement=" ORDER BY name asc"+StringUtils.ifNotEmptyAppend(_sortOrder, ", ");
    _sqlBuilder.append(_sqlOrderByStatement);
    // generation order - END

    //StringUtils, SqlUtils will be used in case of dynamic parts of SQL
    String _sql=_sqlBuilder.toString();
    String[] _sqlArgs=_sqlWhereParams.toArray(new String[_sqlWhereParams.size()]);
    Logger.info(_sql);

    // log for where parameters -- BEGIN
    int _whereParamCounter=0;
    for (String _whereParamItem: _sqlWhereParams) {
      Logger.info("==> param%s: '%s'",(_whereParamCounter++), StringUtils.checkSize(_whereParamItem));
    }
    // log for where parameters -- END
    try (Cursor cursor = database().rawQuery(_sql, _sqlArgs)) {
      Logger.info("Rows found: %s",cursor.getCount());

      LinkedList<Person> resultList=new LinkedList<Person>();
      Person resultBean=null;

      if (cursor.moveToFirst()) {

        int index0=cursor.getColumnIndex("id");
        int index1=cursor.getColumnIndex("alias_parent_id");
        int index2=cursor.getColumnIndex("birth_city");
        int index3=cursor.getColumnIndex("birth_day");
        int index4=cursor.getColumnIndex("value");
        int index5=cursor.getColumnIndex("name");
        int index6=cursor.getColumnIndex("surname");

        do
         {
          resultBean=new Person();

          resultBean.id=cursor.getLong(index0);
          if (!cursor.isNull(index1)) { resultBean.parentId=cursor.getLong(index1); }
          if (!cursor.isNull(index2)) { resultBean.birthCity=cursor.getString(index2); }
          if (!cursor.isNull(index3)) { resultBean.birthDay=DateUtils.read(cursor.getString(index3)); }
          if (!cursor.isNull(index4)) { resultBean.value=cursor.getLong(index4); }
          if (!cursor.isNull(index5)) { resultBean.setName(cursor.getString(index5)); }
          if (!cursor.isNull(index6)) { resultBean.setSurname(cursor.getString(index6)); }

          resultList.add(resultBean);
        } while (cursor.moveToNext());
      }

      return resultList;
    }
  }

  /**
   * <h1>Content provider URI (SELECT operation):</h1>
   * <pre>content://sqlite.feature.contentprovider.kripton35/persons</pre>
   *
   * <h2>JQL SELECT for Content Provider</h2>
   * <pre>SELECT id, parentId, birthCity, birthDay, value, name, surname FROM Person WHERE #{DYNAMIC_WHERE} ORDER BY name asc,  #{DYNAMIC_ORDER_BY}</pre>
   *
   * <h2>SQL SELECT for Content Provider</h2>
   * <pre>SELECT id, alias_parent_id, birth_city, birth_day, value, name, surname FROM person WHERE #{DYNAMIC_WHERE} ORDER BY name asc,  #{DYNAMIC_ORDER_BY}</pre>
   *
   * <p><strong>In URI, * is replaced with [*] for javadoc rapresentation</strong></p>
   *
   * @param uri "content://sqlite.feature.contentprovider.kripton35/persons"
   * @param selection dynamic part of <code>where</code> statement 
   * @param selectionArgs arguments of dynamic part of <code>where</code> statement 
   * @return number of effected rows
   */
  Cursor selectAll10(Uri uri, String[] projection, String selection, String[] selectionArgs,
      String sortOrder) {
    Logger.info("Execute SELECT for URI %s", uri.toString());
    StringBuilder _sqlBuilder=new StringBuilder();
    // generation CODE_001 -- BEGIN
    // initialize dynamic where
    String _sqlDynamicWhere=selection;
    // initialize dynamic where args
    String[] _sqlDynamicWhereArgs=selectionArgs;
    // generation CODE_001 -- END
    StringBuilder _projectionBuffer=new StringBuilder();
    String _sortOrder=sortOrder;
    _sqlBuilder.append("SELECT %s FROM person  ");
    ArrayList<String> _sqlWhereParams=getWhereParamsArray();

    // manage WHERE arguments -- BEGIN

    // manage WHERE statement
    String _sqlWhereStatement=StringUtils.ifNotEmptyAppend(_sqlDynamicWhere, " WHERE ");
    _sqlBuilder.append(_sqlWhereStatement);

    // manage WHERE arguments -- END
    if (StringUtils.hasText(_sqlDynamicWhere) && _sqlDynamicWhereArgs!=null) {
      for (String _arg: _sqlDynamicWhereArgs) {
        _sqlWhereParams.add(_arg);
      }
    }
    // generation order - BEGIN
    String _sqlOrderByStatement=" ORDER BY name asc"+StringUtils.ifNotEmptyAppend(_sortOrder, ", ");
    _sqlBuilder.append(_sqlOrderByStatement);
    // generation order - END


    // manage projected columns
    String _columnSeparator="";
    if (projection!=null && projection.length>0) {
      for (String columnName:projection) {
        if (!selectAll10ColumnSet.contains(columnName)) {
          throw new KriptonRuntimeException(String.format("For URI 'content://sqlite.feature.contentprovider.kripton35/persons', column '%s' does not exists in table '%s' or can not be defined in this SELECT operation", columnName, "person" ));
        }
        _projectionBuffer.append(_columnSeparator + columnName);
        _columnSeparator=", ";
      }
    } else {
      for (String column: selectAll10ColumnSet) {
        _projectionBuffer.append(_columnSeparator + column);
        _columnSeparator=", ";
      }
    }

    // manage log
    String _sql=String.format(_sqlBuilder.toString(), _projectionBuffer.toString());
    Logger.info(_sql);

    // log for where parameters -- BEGIN
    int _whereParamCounter=0;
    for (String _whereParamItem: _sqlWhereParams) {
      Logger.info("==> param%s: '%s'",(_whereParamCounter++), StringUtils.checkSize(_whereParamItem));
    }
    // log for where parameters -- END

    // execute query
    Cursor _result = database().rawQuery(_sql, _sqlWhereParams.toArray(new String[_sqlWhereParams.size()]));
    return _result;
  }

  /**
   * <h2>Select SQL:</h2>
   *
   * <pre>SELECT id, alias_parent_id, birth_city, birth_day, value, name, surname FROM person WHERE name like ${data.name} || '%' ORDER BY #{DYNAMIC_ORDER_BY}</pre>
   *
   * <h2>Projected columns:</h2>
   * <dl>
   * 	<dt>id</dt><dd>is associated to bean's property <strong>id</strong></dd>
   * 	<dt>alias_parent_id</dt><dd>is associated to bean's property <strong>parentId</strong></dd>
   * 	<dt>birth_city</dt><dd>is associated to bean's property <strong>birthCity</strong></dd>
   * 	<dt>birth_day</dt><dd>is associated to bean's property <strong>birthDay</strong></dd>
   * 	<dt>value</dt><dd>is associated to bean's property <strong>value</strong></dd>
   * 	<dt>name</dt><dd>is associated to bean's property <strong>name</strong></dd>
   * 	<dt>surname</dt><dd>is associated to bean's property <strong>surname</strong></dd>
   * </dl>
   *
   * <h2>Method's parameters and associated dynamic parts:</h2>
   * <dl>
   * <dt>orderBy</dt>is part of order statement resolved at runtime. In above SQL compairs as #{DYNAMIC_ORDER_BY}</dd>
   * </dl>
   *
   * <h2>Query's parameters:</h2>
   * <dl>
   * 	<dt>${data.name}</dt><dd>is binded to method's parameter <strong>bean.name</strong></dd>
   * </dl>
   *
   * @param bean
   * 	is used as ${data}
   * @param orderBy
   * 	is used as <strong>dynamic ORDER BY statement</strong> and it is formatted by ({@link StringUtils#format})
   * @return collection of bean or empty collection.
   */
  @Override
  public List<Person> selectOne(Person bean, String orderBy) {
    StringBuilder _sqlBuilder=new StringBuilder();
    _sqlBuilder.append("SELECT id, alias_parent_id, birth_city, birth_day, value, name, surname FROM person");
    // generation CODE_001 -- BEGIN
    // generation CODE_001 -- END
    String _sortOrder=orderBy;
    ArrayList<String> _sqlWhereParams=getWhereParamsArray();

    // manage WHERE arguments -- BEGIN

    // manage WHERE statement
    String _sqlWhereStatement=" WHERE name like ? || '%'";
    _sqlBuilder.append(_sqlWhereStatement);

    // manage WHERE arguments -- END

    // build where condition
    _sqlWhereParams.add((bean.getName()==null?"":bean.getName()));
    // generation order - BEGIN
    String _sqlOrderByStatement=StringUtils.ifNotEmptyAppend(_sortOrder," ORDER BY ");
    _sqlBuilder.append(_sqlOrderByStatement);
    // generation order - END

    //StringUtils, SqlUtils will be used in case of dynamic parts of SQL
    String _sql=_sqlBuilder.toString();
    String[] _sqlArgs=_sqlWhereParams.toArray(new String[_sqlWhereParams.size()]);
    Logger.info(_sql);

    // log for where parameters -- BEGIN
    int _whereParamCounter=0;
    for (String _whereParamItem: _sqlWhereParams) {
      Logger.info("==> param%s: '%s'",(_whereParamCounter++), StringUtils.checkSize(_whereParamItem));
    }
    // log for where parameters -- END
    try (Cursor cursor = database().rawQuery(_sql, _sqlArgs)) {
      Logger.info("Rows found: %s",cursor.getCount());

      LinkedList<Person> resultList=new LinkedList<Person>();
      Person resultBean=null;

      if (cursor.moveToFirst()) {

        int index0=cursor.getColumnIndex("id");
        int index1=cursor.getColumnIndex("alias_parent_id");
        int index2=cursor.getColumnIndex("birth_city");
        int index3=cursor.getColumnIndex("birth_day");
        int index4=cursor.getColumnIndex("value");
        int index5=cursor.getColumnIndex("name");
        int index6=cursor.getColumnIndex("surname");

        do
         {
          resultBean=new Person();

          resultBean.id=cursor.getLong(index0);
          if (!cursor.isNull(index1)) { resultBean.parentId=cursor.getLong(index1); }
          if (!cursor.isNull(index2)) { resultBean.birthCity=cursor.getString(index2); }
          if (!cursor.isNull(index3)) { resultBean.birthDay=DateUtils.read(cursor.getString(index3)); }
          if (!cursor.isNull(index4)) { resultBean.value=cursor.getLong(index4); }
          if (!cursor.isNull(index5)) { resultBean.setName(cursor.getString(index5)); }
          if (!cursor.isNull(index6)) { resultBean.setSurname(cursor.getString(index6)); }

          resultList.add(resultBean);
        } while (cursor.moveToNext());
      }

      return resultList;
    }
  }

  /**
   * <h1>Content provider URI (SELECT operation):</h1>
   * <pre>content://sqlite.feature.contentprovider.kripton35/persons/[*]/test1</pre>
   *
   * <h2>JQL SELECT for Content Provider</h2>
   * <pre>SELECT id, parentId, birthCity, birthDay, value, name, surname FROM Person WHERE name like ${data.name} || '%' ORDER BY #{DYNAMIC_ORDER_BY}</pre>
   *
   * <h2>SQL SELECT for Content Provider</h2>
   * <pre>SELECT id, alias_parent_id, birth_city, birth_day, value, name, surname FROM person WHERE name like ${data.name} || '%' ORDER BY #{DYNAMIC_ORDER_BY}</pre>
   *
   * <h3>Path variables defined:</h3>
   * <ul>
   * <li><strong>${data.name}</strong> at path segment 1</li>
   * </ul>
   *
   * <p><strong>Dynamic where statement is ignored, due no param with @BindSqlDynamicWhere was added.</strong></p>
   *
   * <p><strong>In URI, * is replaced with [*] for javadoc rapresentation</strong></p>
   *
   * @param uri "content://sqlite.feature.contentprovider.kripton35/persons/[*]/test1"
   * @param selection dynamic part of <code>where</code> statement <b>NOT USED</b>
   * @param selectionArgs arguments of dynamic part of <code>where</code> statement <b>NOT USED</b>
   * @return number of effected rows
   */
  Cursor selectOne11(Uri uri, String[] projection, String selection, String[] selectionArgs,
      String sortOrder) {
    Logger.info("Execute SELECT for URI %s", uri.toString());
    StringBuilder _sqlBuilder=new StringBuilder();
    // generation CODE_001 -- BEGIN
    // generation CODE_001 -- END
    StringBuilder _projectionBuffer=new StringBuilder();
    String _sortOrder=sortOrder;
    _sqlBuilder.append("SELECT %s FROM person  ");
    ArrayList<String> _sqlWhereParams=getWhereParamsArray();

    // manage WHERE arguments -- BEGIN

    // manage WHERE statement
    String _sqlWhereStatement=" WHERE name like ? || '%'";
    _sqlBuilder.append(_sqlWhereStatement);

    // manage WHERE arguments -- END
    // generation order - BEGIN
    String _sqlOrderByStatement=StringUtils.ifNotEmptyAppend(_sortOrder," ORDER BY ");
    _sqlBuilder.append(_sqlOrderByStatement);
    // generation order - END


    // manage projected columns
    String _columnSeparator="";
    if (projection!=null && projection.length>0) {
      for (String columnName:projection) {
        if (!selectOne11ColumnSet.contains(columnName)) {
          throw new KriptonRuntimeException(String.format("For URI 'content://sqlite.feature.contentprovider.kripton35/persons/*/test1', column '%s' does not exists in table '%s' or can not be defined in this SELECT operation", columnName, "person" ));
        }
        _projectionBuffer.append(_columnSeparator + columnName);
        _columnSeparator=", ";
      }
    } else {
      for (String column: selectOne11ColumnSet) {
        _projectionBuffer.append(_columnSeparator + column);
        _columnSeparator=", ";
      }
    }
    // Add parameter data.name at path segment 1
    _sqlWhereParams.add(uri.getPathSegments().get(1));

    // manage log
    String _sql=String.format(_sqlBuilder.toString(), _projectionBuffer.toString());
    Logger.info(_sql);

    // log for where parameters -- BEGIN
    int _whereParamCounter=0;
    for (String _whereParamItem: _sqlWhereParams) {
      Logger.info("==> param%s: '%s'",(_whereParamCounter++), StringUtils.checkSize(_whereParamItem));
    }
    // log for where parameters -- END

    // execute query
    Cursor _result = database().rawQuery(_sql, _sqlWhereParams.toArray(new String[_sqlWhereParams.size()]));
    return _result;
  }

  /**
   * <h2>Select SQL:</h2>
   *
   * <pre>SELECT id, alias_parent_id, birth_city, birth_day, value, name, surname FROM person</pre>
   *
   * <h2>Projected columns:</h2>
   * <dl>
   * 	<dt>id</dt><dd>is associated to bean's property <strong>id</strong></dd>
   * 	<dt>alias_parent_id</dt><dd>is associated to bean's property <strong>parentId</strong></dd>
   * 	<dt>birth_city</dt><dd>is associated to bean's property <strong>birthCity</strong></dd>
   * 	<dt>birth_day</dt><dd>is associated to bean's property <strong>birthDay</strong></dd>
   * 	<dt>value</dt><dd>is associated to bean's property <strong>value</strong></dd>
   * 	<dt>name</dt><dd>is associated to bean's property <strong>name</strong></dd>
   * 	<dt>surname</dt><dd>is associated to bean's property <strong>surname</strong></dd>
   * </dl>
   *
   * @return collection of bean or empty collection.
   */
  @Override
  public List<Person> selectBean() {
    StringBuilder _sqlBuilder=new StringBuilder();
    _sqlBuilder.append("SELECT id, alias_parent_id, birth_city, birth_day, value, name, surname FROM person");
    // generation CODE_001 -- BEGIN
    // generation CODE_001 -- END
    ArrayList<String> _sqlWhereParams=getWhereParamsArray();
    String _sqlWhereStatement="";

    // build where condition
    //StringUtils, SqlUtils will be used in case of dynamic parts of SQL
    String _sql=_sqlBuilder.toString();
    String[] _sqlArgs=_sqlWhereParams.toArray(new String[_sqlWhereParams.size()]);
    Logger.info(_sql);

    // log for where parameters -- BEGIN
    int _whereParamCounter=0;
    for (String _whereParamItem: _sqlWhereParams) {
      Logger.info("==> param%s: '%s'",(_whereParamCounter++), StringUtils.checkSize(_whereParamItem));
    }
    // log for where parameters -- END
    try (Cursor cursor = database().rawQuery(_sql, _sqlArgs)) {
      Logger.info("Rows found: %s",cursor.getCount());

      LinkedList<Person> resultList=new LinkedList<Person>();
      Person resultBean=null;

      if (cursor.moveToFirst()) {

        int index0=cursor.getColumnIndex("id");
        int index1=cursor.getColumnIndex("alias_parent_id");
        int index2=cursor.getColumnIndex("birth_city");
        int index3=cursor.getColumnIndex("birth_day");
        int index4=cursor.getColumnIndex("value");
        int index5=cursor.getColumnIndex("name");
        int index6=cursor.getColumnIndex("surname");

        do
         {
          resultBean=new Person();

          resultBean.id=cursor.getLong(index0);
          if (!cursor.isNull(index1)) { resultBean.parentId=cursor.getLong(index1); }
          if (!cursor.isNull(index2)) { resultBean.birthCity=cursor.getString(index2); }
          if (!cursor.isNull(index3)) { resultBean.birthDay=DateUtils.read(cursor.getString(index3)); }
          if (!cursor.isNull(index4)) { resultBean.value=cursor.getLong(index4); }
          if (!cursor.isNull(index5)) { resultBean.setName(cursor.getString(index5)); }
          if (!cursor.isNull(index6)) { resultBean.setSurname(cursor.getString(index6)); }

          resultList.add(resultBean);
        } while (cursor.moveToNext());
      }

      return resultList;
    }
  }

  /**
   * <h1>Content provider URI (SELECT operation):</h1>
   * <pre>content://sqlite.feature.contentprovider.kripton35/persons/test3</pre>
   *
   * <h2>JQL SELECT for Content Provider</h2>
   * <pre>SELECT id, parentId, birthCity, birthDay, value, name, surname FROM Person</pre>
   *
   * <h2>SQL SELECT for Content Provider</h2>
   * <pre>SELECT id, alias_parent_id, birth_city, birth_day, value, name, surname FROM person</pre>
   *
   * <p><strong>Dynamic where statement is ignored, due no param with @BindSqlDynamicWhere was added.</strong></p>
   *
   * <p><strong>In URI, * is replaced with [*] for javadoc rapresentation</strong></p>
   *
   * @param uri "content://sqlite.feature.contentprovider.kripton35/persons/test3"
   * @param selection dynamic part of <code>where</code> statement <b>NOT USED</b>
   * @param selectionArgs arguments of dynamic part of <code>where</code> statement <b>NOT USED</b>
   * @return number of effected rows
   */
  Cursor selectBean12(Uri uri, String[] projection, String selection, String[] selectionArgs,
      String sortOrder) {
    Logger.info("Execute SELECT for URI %s", uri.toString());
    StringBuilder _sqlBuilder=new StringBuilder();
    // generation CODE_001 -- BEGIN
    // generation CODE_001 -- END
    StringBuilder _projectionBuffer=new StringBuilder();
    _sqlBuilder.append("SELECT %s FROM person");
    ArrayList<String> _sqlWhereParams=getWhereParamsArray();
    String _sqlWhereStatement="";

    // manage projected columns
    String _columnSeparator="";
    if (projection!=null && projection.length>0) {
      for (String columnName:projection) {
        if (!selectBean12ColumnSet.contains(columnName)) {
          throw new KriptonRuntimeException(String.format("For URI 'content://sqlite.feature.contentprovider.kripton35/persons/test3', column '%s' does not exists in table '%s' or can not be defined in this SELECT operation", columnName, "person" ));
        }
        _projectionBuffer.append(_columnSeparator + columnName);
        _columnSeparator=", ";
      }
    } else {
      for (String column: selectBean12ColumnSet) {
        _projectionBuffer.append(_columnSeparator + column);
        _columnSeparator=", ";
      }
    }

    // manage log
    String _sql=String.format(_sqlBuilder.toString(), _projectionBuffer.toString());
    Logger.info(_sql);

    // log for where parameters -- BEGIN
    int _whereParamCounter=0;
    for (String _whereParamItem: _sqlWhereParams) {
      Logger.info("==> param%s: '%s'",(_whereParamCounter++), StringUtils.checkSize(_whereParamItem));
    }
    // log for where parameters -- END

    // execute query
    Cursor _result = database().rawQuery(_sql, _sqlWhereParams.toArray(new String[_sqlWhereParams.size()]));
    return _result;
  }

  /**
   * <h2>Select SQL:</h2>
   *
   * <pre>SELECT id, alias_parent_id, birth_city, birth_day, value, name, surname FROM person WHERE #{DYNAMIC_WHERE} ORDER BY name</pre>
   *
   * <h2>Projected columns:</h2>
   * <dl>
   * 	<dt>id</dt><dd>is associated to bean's property <strong>id</strong></dd>
   * 	<dt>alias_parent_id</dt><dd>is associated to bean's property <strong>parentId</strong></dd>
   * 	<dt>birth_city</dt><dd>is associated to bean's property <strong>birthCity</strong></dd>
   * 	<dt>birth_day</dt><dd>is associated to bean's property <strong>birthDay</strong></dd>
   * 	<dt>value</dt><dd>is associated to bean's property <strong>value</strong></dd>
   * 	<dt>name</dt><dd>is associated to bean's property <strong>name</strong></dd>
   * 	<dt>surname</dt><dd>is associated to bean's property <strong>surname</strong></dd>
   * </dl>
   *
   * <h2>Method's parameters and associated dynamic parts:</h2>
   * <dl>
   * <dt>where</dt><dd>is part of where conditions resolved at runtime. In above SQL compairs as #{DYNAMIC_WHERE}</dd>
   * </dl>
   *
   * @param cursorListener
   * 	is the cursor listener
   * @param where
   * 	is used as <strong>dynamic WHERE statement</strong> and it is formatted by ({@link StringUtils#format})
   */
  @Override
  public void selectCursorListener(OnReadCursorListener cursorListener, String where) {
    StringBuilder _sqlBuilder=new StringBuilder();
    _sqlBuilder.append("SELECT id, alias_parent_id, birth_city, birth_day, value, name, surname FROM person");
    // generation CODE_001 -- BEGIN
    // initialize dynamic where
    String _sqlDynamicWhere=where;
    // generation CODE_001 -- END
    String _sortOrder=null;
    ArrayList<String> _sqlWhereParams=getWhereParamsArray();

    // manage WHERE arguments -- BEGIN

    // manage WHERE statement
    String _sqlWhereStatement=StringUtils.ifNotEmptyAppend(_sqlDynamicWhere, " WHERE ");
    _sqlBuilder.append(_sqlWhereStatement);

    // manage WHERE arguments -- END

    // build where condition
    // generation order - BEGIN
    String _sqlOrderByStatement=" ORDER BY name";
    _sqlBuilder.append(_sqlOrderByStatement);
    // generation order - END

    //StringUtils, SqlUtils will be used in case of dynamic parts of SQL
    String _sql=_sqlBuilder.toString();
    String[] _sqlArgs=_sqlWhereParams.toArray(new String[_sqlWhereParams.size()]);
    Logger.info(_sql);

    // log for where parameters -- BEGIN
    int _whereParamCounter=0;
    for (String _whereParamItem: _sqlWhereParams) {
      Logger.info("==> param%s: '%s'",(_whereParamCounter++), StringUtils.checkSize(_whereParamItem));
    }
    // log for where parameters -- END
    try (Cursor cursor = database().rawQuery(_sql, _sqlArgs)) {
      Logger.info("Rows found: %s",cursor.getCount());

      if (cursor.moveToFirst()) {

        do
         {
          cursorListener.onRead(cursor);
        } while (cursor.moveToNext());
      }
    }
  }
}
