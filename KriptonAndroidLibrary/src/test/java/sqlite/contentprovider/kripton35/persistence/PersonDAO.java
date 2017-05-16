/*******************************************************************************
 * Copyright 2015, 2016 Francesco Benincasa.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package sqlite.contentprovider.kripton35.persistence;

import java.util.List;

import com.abubusoft.kripton.android.annotation.BindDao;
import com.abubusoft.kripton.android.annotation.BindSqlInsert;
import com.abubusoft.kripton.android.annotation.BindSqlSelect;
import com.abubusoft.kripton.android.annotation.BindSqlUpdate;
import com.abubusoft.kripton.annotation.BindContentProviderEntry;
import com.abubusoft.kripton.annotation.BindContentProviderEntry.ResultType;
import com.abubusoft.kripton.annotation.BindContentProviderPath;

import sqlite.contentprovider.kripton35.entities.Person;

/**
 * Created by xcesco on 27/09/2016.
 */

@BindDao(Person.class)
@BindContentProviderPath(path="persons",typeName="person")
public interface PersonDAO {
	
	@BindSqlInsert
	@BindContentProviderEntry
	boolean insert(Person bean);
	
	@BindSqlUpdate
	@BindContentProviderEntry
	boolean update(Person bean);
	
	@BindSqlSelect
	@BindContentProviderEntry
	List<Person> selectAll();
	
	@BindSqlSelect(where="id=${id}")
	@BindContentProviderEntry(path="/#", resultType=ResultType.ONE)
	Person selectOne(long id);
	
	
	
		
}