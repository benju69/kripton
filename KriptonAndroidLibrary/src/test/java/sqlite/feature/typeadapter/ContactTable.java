package sqlite.feature.typeadapter;

import java.lang.String;

/**
 * <p>
 * Entity <code>Contact</code> is associated to table <code>contact</code>
 * This class represents table associated to entity.
 * </p>
 *  @see Contact
 */
public class ContactTable {
  /**
   * Costant represents typeName of table contact
   */
  public static final String TABLE_NAME = "contact";

  /**
   * <p>
   * DDL to create table contact
   * </p>
   *
   * <pre>CREATE TABLE contact (id INTEGER PRIMARY KEY AUTOINCREMENT, birth_day INTEGER, password BLOB, type INTEGER);</pre>
   */
  public static final String CREATE_TABLE_SQL = "CREATE TABLE contact (id INTEGER PRIMARY KEY AUTOINCREMENT, birth_day INTEGER, password BLOB, type INTEGER);";

  /**
   * <p>
   * DDL to drop table contact
   * </p>
   *
   * <pre>DROP TABLE IF EXISTS contact;</pre>
   */
  public static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS contact;";

  /**
   * Entity's property <code>id</code> is associated to table column <code>id</code>. This costant represents column name.
   *
   *  @see Contact#id
   */
  public static final String COLUMN_ID = "id";

  /**
   * Entity's property <code>birthDay</code> is associated to table column <code>birth_day</code>. This costant represents column name.
   *
   *  @see Contact#birthDay
   */
  public static final String COLUMN_BIRTH_DAY = "birth_day";

  /**
   * Entity's property <code>password</code> is associated to table column <code>password</code>. This costant represents column name.
   *
   *  @see Contact#password
   */
  public static final String COLUMN_PASSWORD = "password";

  /**
   * Entity's property <code>type</code> is associated to table column <code>type</code>. This costant represents column name.
   *
   *  @see Contact#type
   */
  public static final String COLUMN_TYPE = "type";
}