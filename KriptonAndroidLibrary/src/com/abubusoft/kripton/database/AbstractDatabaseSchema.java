package com.abubusoft.kripton.database;

import java.util.LinkedHashMap;

import com.abubusoft.kripton.binder.schema.ElementSchema;
import com.abubusoft.kripton.binder.schema.MappingSchema;
import com.abubusoft.kripton.exception.MappingException;

public abstract class AbstractDatabaseSchema<C extends Insert, R extends Query, U extends Update, D extends Delete> {

	interface TableTask {
		String onTable(DatabaseTable item);
	}

	// use LRU cache to limit memory consumption.
	protected LinkedHashMap<String, DatabaseTable> tables = new LinkedHashMap<>();

	public LinkedHashMap<String, DatabaseTable> getTables() {
		return tables;
	}

	protected DatabaseHandler<C,R,U,D> handler;

	protected LinkedHashMap<Class<?>, DatabaseTable> class2Table = new LinkedHashMap<>();

	public void build(DatabaseSchemaOptions options) {
		handler = createHandler(options);
		handler.init();
		buildTables(handler, options);
	}

	protected void buildTables(DatabaseHandler<C,R,U,D> handler, DatabaseSchemaOptions options) {
		MappingSchema[] array = new MappingSchema[options.mappingSchemaSet.size()];
		array = options.mappingSchemaSet.toArray(array);
		DatabaseTable table;
		DatabaseColumn column;
		String key;
		for (MappingSchema item : array) {
			key = options.tablePrefix + options.nameConverter.convertName(item.tableInfo.name);

			table = new DatabaseTable();
			table.name = key;
			table.schema = item;
			table.clazz = item.getType();

			for (ElementSchema element : item.getField2SchemaMapping().values()) {
				column = createColumn(element, options);

				table.columns.add(column);
				table.field2column.put(element.getName(), column);

				// look for pk
				if (column.feature == ColumnType.PRIMARY_KEY) {
					table.primaryKey = column;
				}
			}

			tables.put(key, table);
			class2Table.put(item.getType(), table);

			// create default insert, update and select
			createInsert(item.getType(), InsertOptions.build());
			createQuery(item.getType(), QueryOptions.build().name(Query.QUERY_ALL));

			// only for table with primary key
			if (table.primaryKey != null) {
				String whereById = table.primaryKey.schema.getName() + "=#{" + table.primaryKey.schema.getName() + "}";

				createQuery(item.getType(), QueryOptions.build().name(Query.DEFAULT_BY_ID).where(whereById).paramsClass(table.primaryKey.schema.getFieldType()));
				createUpdate(item.getType(), UpdateOptions.build().name(Update.DEFAULT_BY_ID).where(whereById));
				createDelete(item.getType(), DeleteOptions.build().name(Delete.DEFAULT_BY_ID).where(whereById).paramsClass(table.primaryKey.schema.getFieldType()));
			}
		}
	}

	protected abstract DatabaseColumn createColumn(ElementSchema element, DatabaseSchemaOptions options);

	protected abstract DatabaseHandler<C,R,U,D> createHandler(DatabaseSchemaOptions options);

	public DatabaseTable getTableFromClass(Class<?> clazz) {
		return class2Table.get(clazz);
	}

	public R createQuery(Class<?> clazz, QueryOptions options) {
		DatabaseTable table = this.class2Table.get(clazz);

		if (table == null)
			throw new MappingException("Table for class " + clazz.getName() + " does not exists. Have you included it in db definition?");
		return handler.createQuery(table, options);
	}

	public C createInsert(Class<?> clazz, InsertOptions options) {
		DatabaseTable table = this.class2Table.get(clazz);

		if (table == null)
			throw new MappingException("Table for class " + clazz.getName() + " does not exists. Have you included it in db definition?");
		return handler.createInsert(table, options);
	}

	public U createUpdate(Class<?> clazz, UpdateOptions options) {
		DatabaseTable table = this.class2Table.get(clazz);

		if (table == null)
			throw new MappingException("Table for class " + clazz.getName() + " does not exists. Have you included it in db definition?");
		return handler.createUpdate(table, options);
	}

	public D createDelete(Class<?> clazz, DeleteOptions options) {
		DatabaseTable table = this.class2Table.get(clazz);

		if (table == null)
			throw new MappingException("Table for class " + clazz.getName() + " does not exists. Have you included it in db definition?");
		return handler.createDelete(table, options);
	}

	/**
	 * Retrieve an insert already defined.
	 * 
	 * @param clazz
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <I extends Insert> I getInsert(Class<?> clazz, String name) {
		DatabaseTable table = this.class2Table.get(clazz);

		if (table == null)
			throw new MappingException("Table for class " + clazz.getName() + " does not exists. Have you included it in db definition?");
		return (I) table.inserts.get(name);
	}

	@SuppressWarnings("unchecked")
	public <I extends Insert> I getInsert(Class<?> clazz) {
		DatabaseTable table = this.class2Table.get(clazz);

		if (table == null)
			throw new MappingException("Table for class " + clazz.getName() + " does not exists. Have you included it in db definition?");

		if (table.inserts.size() != 1) {
			throw new MappingException("Table for class " + clazz.getName() + " does not have one insert. Check insert definitions.");
		}
		return (I) table.lastInsert;
	}

	/**
	 * Retrieve a query already defined.
	 * 
	 * @param clazz
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <Q extends Query> Q  getQuery(Class<?> clazz, String name) {
		DatabaseTable table = this.class2Table.get(clazz);

		if (table == null)
			throw new MappingException("Table for class " + clazz.getName() + " does not exists. Have you included it in db definition?");
		return (Q) table.queries.get(name);
	}

	@SuppressWarnings("unchecked")
	public <Q extends Query> Q getQuery(Class<?> clazz) {
		DatabaseTable table = this.class2Table.get(clazz);

		if (table == null)
			throw new MappingException("Table for class " + clazz.getName() + " does not exists. Have you included it in db definition?");

		if (table.queries.size() != 1) {
			throw new MappingException("Table for class " + clazz.getName() + " does not have one query. Check query definitions.");
		}
		return (Q) table.lastQuery;
	}

	/**
	 * Retrieve a query already defined.
	 * 
	 * @param clazz
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public D getDelete(Class<?> clazz, String name) {
		DatabaseTable table = this.class2Table.get(clazz);

		if (table == null)
			throw new MappingException("Table for class " + clazz.getName() + " does not exists. Have you included it in db definition?");
		return (D) table.deletes.get(name);
	}

	@SuppressWarnings("unchecked")
	public D getDelete(Class<?> clazz) {
		DatabaseTable table = this.class2Table.get(clazz);

		if (table == null)
			throw new MappingException("Table for class " + clazz.getName() + " does not exists. Have you included it in db definition?");

		if (table.deletes.size() != 1) {
			throw new MappingException("Table for class " + clazz.getName() + " does not have one delete. Check delete definitions.");
		}
		return (D) table.lastDelete;
	}

	@SuppressWarnings("unchecked")
	public U getUpdate(Class<?> clazz, String name) {
		DatabaseTable table = this.class2Table.get(clazz);

		if (table == null)
			throw new MappingException("Table for class " + clazz.getName() + " does not exists. Have you included it in db definition?");
		return (U) table.updates.get(name);
	}

	@SuppressWarnings("unchecked")
	public U getUpdate(Class<?> clazz) {
		DatabaseTable table = this.class2Table.get(clazz);

		if (table == null)
			throw new MappingException("Table for class " + clazz.getName() + " does not exists. Have you included it in db definition?");

		if (table.updates.size() != 1) {
			throw new MappingException("Table for class " + clazz.getName() + " does not have one update. Check update definitions.");
		}
		return (U) table.lastUpdate;
	}

	public String[] createTablesSQL() {
		TableTask iteratorCreateTableSQL = new TableTask() {
			@Override
			public String onTable(DatabaseTable item) {
				return handler.createTableSQL(item);
			}
		};

		return forEachTable(iteratorCreateTableSQL);
	}

	public String[] dropTablesSQL() {
		TableTask iteratorDropTableSQL = new TableTask() {
			@Override
			public String onTable(DatabaseTable item) {
				return handler.dropTableSQL(item);
			}
		};

		return forEachTable(iteratorDropTableSQL);
	}

	protected String[] forEachTable(TableTask task) {
		String[] result = new String[tables.size()];
		int i = 0;

		for (DatabaseTable item : tables.values()) {
			result[i] = task.onTable(item);
			i++;
		}

		return result;
	}

}
