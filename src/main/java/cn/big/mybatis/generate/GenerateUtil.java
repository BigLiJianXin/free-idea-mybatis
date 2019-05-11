package cn.big.mybatis.generate;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

/**
 * Created by LiJianXin on 2019/5/11 0011.
 */
public class GenerateUtil {

	public static final StatementGenerator UPDATE_GENERATOR = new UpdateGenerator("update", "modify", "set");

	public static final StatementGenerator SELECT_GENERATOR = new SelectGenerator("select", "get", "look", "find", "list", "search", "count", "query");

	public static final StatementGenerator DELETE_GENERATOR = new DeleteGenerator("del", "cancel");

	public static final StatementGenerator INSERT_GENERATOR = new InsertGenerator("insert", "add", "new");

	public static final Set<StatementGenerator> ALL = ImmutableSet.of(UPDATE_GENERATOR, SELECT_GENERATOR, DELETE_GENERATOR, INSERT_GENERATOR);

	public static final GenerateModel START_WITH_MODEL = new GenerateModel.StartWithModel();

	public static final GenerateModel END_WITH_MODEL = new GenerateModel.EndWithModel();

	public static final GenerateModel CONTAIN_MODEL = new GenerateModel.ContainModel();


}
