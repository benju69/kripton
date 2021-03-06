package sqlite.feature.jql.persistence;

import java.util.List;

import com.abubusoft.kripton.android.annotation.BindSqlInsert;
import com.abubusoft.kripton.android.annotation.BindSqlSelect;

public interface DaoBean<E> {

	@BindSqlSelect
	public List<E> selectAll();

	@BindSqlInsert
	public E insertBean(E bean);

}
