package sqlite.test03;

import android.database.Cursor;
import com.abubusoft.kripton.android.Logger;
import com.abubusoft.kripton.android.sqlite.AbstractDao;
import com.abubusoft.kripton.android.sqlite.KriptonContentValues;
import com.abubusoft.kripton.android.sqlite.SQLContext;
import com.abubusoft.kripton.common.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * DAO implementation for entity <code>Bean01</code>, based on interface <code>DaoBean01</code>
 * </p>
 *
 *  @see Bean01
 *  @see DaoBean01
 *  @see Bean01Table
 */
public class DaoBean01Impl extends AbstractDao implements DaoBean01 {
  private static final String LIST_ALL_SQL1 = "SELECT lista, id, message_date, message_text, bean_list, value FROM bean01 WHERE 1=1";

  public DaoBean01Impl(SQLContext context) {
    super(context);
  }

  /**
   * <h2>Select SQL:</h2>
   *
   * <pre>SELECT lista, id, message_date, message_text, bean_list, value FROM bean01 WHERE 1=1</pre>
   *
   * <h2>Projected columns:</h2>
   * <dl>
   * 	<dt>lista</dt><dd>is associated to bean's property <strong>lista</strong></dd>
   * 	<dt>id</dt><dd>is associated to bean's property <strong>id</strong></dd>
   * 	<dt>message_date</dt><dd>is associated to bean's property <strong>messageDate</strong></dd>
   * 	<dt>message_text</dt><dd>is associated to bean's property <strong>messageText</strong></dd>
   * 	<dt>bean_list</dt><dd>is associated to bean's property <strong>beanList</strong></dd>
   * 	<dt>value</dt><dd>is associated to bean's property <strong>value</strong></dd>
   * </dl>
   *
   * @return collection of bean or empty collection.
   */
  @Override
  public List<Bean01> listAll() {
    KriptonContentValues _contentValues=contentValues();
    // query SQL is statically defined
    String _sql=LIST_ALL_SQL1;
    // add where arguments
    String[] _sqlArgs=_contentValues.whereArgsAsArray();
    // log section BEGIN
    if (_context.isLogEnabled()) {
      // manage log
      Logger.info(_sql);

      // log for where parameters -- BEGIN
      int _whereParamCounter=0;
      for (String _whereParamItem: _contentValues.whereArgs()) {
        Logger.info("==> param%s: '%s'",(_whereParamCounter++), StringUtils.checkSize(_whereParamItem));
      }
      // log for where parameters -- END
    }
    // log section END
    try (Cursor _cursor = database().rawQuery(_sql, _sqlArgs)) {
      // log section BEGIN
      if (_context.isLogEnabled()) {
        Logger.info("Rows found: %s",_cursor.getCount());
      }
      // log section END

      ArrayList<Bean01> resultList=new ArrayList<Bean01>(_cursor.getCount());
      Bean01 resultBean=null;

      if (_cursor.moveToFirst()) {

        int index0=_cursor.getColumnIndex("lista");
        int index1=_cursor.getColumnIndex("id");
        int index2=_cursor.getColumnIndex("message_date");
        int index3=_cursor.getColumnIndex("message_text");
        int index4=_cursor.getColumnIndex("bean_list");
        int index5=_cursor.getColumnIndex("value");

        do
         {
          resultBean=new Bean01();

          if (!_cursor.isNull(index0)) { resultBean.setLista(Bean01Table.parseLista(_cursor.getBlob(index0))); }
          resultBean.setId(_cursor.getLong(index1));
          if (!_cursor.isNull(index2)) { resultBean.setMessageDate(_cursor.getLong(index2)); }
          resultBean.setMessageText(_cursor.getString(index3));
          if (!_cursor.isNull(index4)) { resultBean.setBeanList(Bean01Table.parseBeanList(_cursor.getBlob(index4))); }
          if (!_cursor.isNull(index5)) { resultBean.setValue(_cursor.getLong(index5)); }

          resultList.add(resultBean);
        } while (_cursor.moveToNext());
      }

      return resultList;
    }
  }

  public static void clearCompiledStatements() {
  }
}
