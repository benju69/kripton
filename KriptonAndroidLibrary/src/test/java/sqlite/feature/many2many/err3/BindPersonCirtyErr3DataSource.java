package sqlite.feature.many2many.err3;

import android.database.sqlite.SQLiteDatabase;
import com.abubusoft.kripton.android.Logger;
import com.abubusoft.kripton.android.sqlite.AbstractDataSource;
import com.abubusoft.kripton.android.sqlite.DataSourceOptions;
import com.abubusoft.kripton.android.sqlite.SQLiteUpdateTask;
import com.abubusoft.kripton.android.sqlite.SQLiteUpdateTaskHelper;
import com.abubusoft.kripton.exception.KriptonRuntimeException;
import java.util.List;
import sqlite.feature.many2many.CityTable;
import sqlite.feature.many2many.PersonTable;

/**
 * <p>
 * Represents implementation of datasource PersonCirtyErr3DataSource.
 * This class expose database interface through Dao attribute.
 * </p>
 *
 * @see PersonCirtyErr3DataSource
 * @see BindPersonCirtyErr3DaoFactory
 * @see PersonErr3Dao
 * @see PersonErr3DaoImpl
 * @see Person
 * @see CityErr3Dao
 * @see CityErr3DaoImpl
 * @see City
 * @see PersonCityErr1Dao
 * @see PersonCityErr1DaoImpl
 * @see PersonCityErr3
 */
public class BindPersonCirtyErr3DataSource extends AbstractDataSource implements BindPersonCirtyErr3DaoFactory, PersonCirtyErr3DataSource {
  /**
   * <p>datasource singleton</p>
   */
  static BindPersonCirtyErr3DataSource instance;

  /**
   * <p>dao instance</p>
   */
  protected PersonErr3DaoImpl personErr3Dao = new PersonErr3DaoImpl(this);

  /**
   * <p>dao instance</p>
   */
  protected CityErr3DaoImpl cityErr3Dao = new CityErr3DaoImpl(this);

  /**
   * <p>dao instance</p>
   */
  protected PersonCityErr1DaoImpl personCityErr1Dao = new PersonCityErr1DaoImpl(this);

  protected BindPersonCirtyErr3DataSource(DataSourceOptions options) {
    super("person.db", 1, options);
  }

  @Override
  public PersonErr3DaoImpl getPersonErr3Dao() {
    return personErr3Dao;
  }

  @Override
  public CityErr3DaoImpl getCityErr3Dao() {
    return cityErr3Dao;
  }

  @Override
  public PersonCityErr1DaoImpl getPersonCityErr1Dao() {
    return personCityErr1Dao;
  }

  /**
   * <p>Executes a transaction. This method <strong>is thread safe</strong> to avoid concurrent problems. Thedrawback is only one transaction at time can be executed. The database will be open in write mode.</p>
   *
   * @param transaction
   * 	transaction to execute
   */
  public void execute(Transaction transaction) {
    SQLiteDatabase connection=openWritableDatabase();
    try {
      connection.beginTransaction();
      if (transaction!=null && transaction.onExecute(this)) {
        connection.setTransactionSuccessful();
      }
    } catch(Throwable e) {
      Logger.error(e.getMessage());
      e.printStackTrace();
      if (transaction!=null) transaction.onError(e);
    } finally {
      try {
        connection.endTransaction();
      } catch (Throwable e) {
        Logger.warn("error closing transaction %s", e.getMessage());
      }
      close();
    }
  }

  /**
   * instance
   */
  public static synchronized BindPersonCirtyErr3DataSource instance() {
    if (instance==null) {
      instance=new BindPersonCirtyErr3DataSource(null);
    }
    return instance;
  }

  /**
   * Retrieve data source instance and open it.
   * @return opened dataSource instance.
   */
  public static BindPersonCirtyErr3DataSource open() {
    BindPersonCirtyErr3DataSource instance=instance();
    instance.openWritableDatabase();
    return instance;
  }

  /**
   * Retrieve data source instance and open it in read only mode.
   * @return opened dataSource instance.
   */
  public static BindPersonCirtyErr3DataSource openReadOnly() {
    BindPersonCirtyErr3DataSource instance=instance();
    instance.openReadOnlyDatabase();
    return instance;
  }

  /**
   * onCreate
   */
  @Override
  public void onCreate(SQLiteDatabase database) {
    // generate tables
    Logger.info("Create database '%s' version %s",this.name, this.getVersion());
    Logger.info("DDL: %s",CityTable.CREATE_TABLE_SQL);
    database.execSQL(CityTable.CREATE_TABLE_SQL);
    Logger.info("DDL: %s",PersonTable.CREATE_TABLE_SQL);
    database.execSQL(PersonTable.CREATE_TABLE_SQL);
    Logger.info("DDL: %s",PersonCityErr3Table.CREATE_TABLE_SQL);
    database.execSQL(PersonCityErr3Table.CREATE_TABLE_SQL);
    // if we have a populate task (previous and current are same), try to execute it
    if (options.updateTasks != null) {
      SQLiteUpdateTask task = findPopulateTaskList(database.getVersion());
      if (task != null) {
        Logger.info("Begin update database from version %s to %s", task.previousVersion, task.currentVersion);
        task.execute(database);
        Logger.info("End update database from version %s to %s", task.previousVersion, task.currentVersion);
      }
    }
    if (options.databaseLifecycleHandler != null) {
      options.databaseLifecycleHandler.onCreate(database);
    }
  }

  /**
   * onUpgrade
   */
  @Override
  public void onUpgrade(SQLiteDatabase database, int previousVersion, int currentVersion) {
    Logger.info("Update database '%s' from version %s to version %s",this.name, previousVersion, currentVersion);
    // if we have a list of update task, try to execute them
    if (options.updateTasks != null) {
      List<SQLiteUpdateTask> tasks = buildTaskList(previousVersion, currentVersion);
      for (SQLiteUpdateTask task : tasks) {
        Logger.info("Begin update database from version %s to %s", task.previousVersion, task.currentVersion);
        task.execute(database);
        Logger.info("End update database from version %s to %s", task.previousVersion, task.currentVersion);
      }
    } else {
      // drop all tables
      SQLiteUpdateTaskHelper.dropTablesAndIndices(database);

      // generate tables
      Logger.info("DDL: %s",CityTable.CREATE_TABLE_SQL);
      database.execSQL(CityTable.CREATE_TABLE_SQL);
      Logger.info("DDL: %s",PersonTable.CREATE_TABLE_SQL);
      database.execSQL(PersonTable.CREATE_TABLE_SQL);
      Logger.info("DDL: %s",PersonCityErr3Table.CREATE_TABLE_SQL);
      database.execSQL(PersonCityErr3Table.CREATE_TABLE_SQL);
    }
    if (options.databaseLifecycleHandler != null) {
      options.databaseLifecycleHandler.onUpdate(database, previousVersion, currentVersion, true);
    }
  }

  /**
   * onConfigure
   */
  @Override
  public void onConfigure(SQLiteDatabase database) {
    // configure database
    database.setForeignKeyConstraintsEnabled(true);
    if (options.databaseLifecycleHandler != null) {
      options.databaseLifecycleHandler.onConfigure(database);
    }
  }

  /**
   * Build instance.
   * @return dataSource instance.
   */
  public static synchronized BindPersonCirtyErr3DataSource build(DataSourceOptions options) {
    if (instance==null) {
      instance=new BindPersonCirtyErr3DataSource(options);
    }
    instance.openWritableDatabase();
    return instance;
  }

  /**
   * interface to define transactions
   */
  public interface Transaction extends AbstractTransaction<BindPersonCirtyErr3DaoFactory> {
  }

  /**
   * Simple class implements interface to define transactions
   */
  public abstract static class SimpleTransaction implements Transaction {
    @Override
    public void onError(Throwable e) {
      throw(new KriptonRuntimeException(e));
    }
  }
}