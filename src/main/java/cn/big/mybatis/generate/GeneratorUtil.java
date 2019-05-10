package cn.big.mybatis.generate;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

/**
 * Created by LiJianXin on 2019/5/10 0010.
 */
public class GeneratorUtil {

	public static final StatementGenerator UPDATE_GENERATOR = new UpdateGenerator("update", "modify", "set");

	public static final StatementGenerator SELECT_GENERATOR = new SelectGenerator("select", "get", "look", "find", "list", "search", "count", "query");

	public static final StatementGenerator DELETE_GENERATOR = new DeleteGenerator("del", "cancel");

	public static final StatementGenerator INSERT_GENERATOR = new InsertGenerator("insert", "add", "new");

	public static final Set<StatementGenerator> ALL = ImmutableSet.of(UPDATE_GENERATOR, SELECT_GENERATOR, DELETE_GENERATOR, INSERT_GENERATOR);

}
