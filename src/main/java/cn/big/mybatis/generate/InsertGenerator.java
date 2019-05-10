package cn.big.mybatis.generate;

import cn.big.mybatis.dom.model.GroupTwo;
import cn.big.mybatis.dom.model.Mapper;
import com.intellij.psi.PsiMethod;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class InsertGenerator extends StatementGenerator {

	public InsertGenerator(@NotNull String... patterns) {
		super(patterns);
	}

	@NotNull
	@Override
	protected GroupTwo getTarget(@NotNull Mapper mapper, @NotNull PsiMethod method) {
		return mapper.addInsert();
	}

	@NotNull
	@Override
	public String getId() {
		return "InsertGenerator";
	}

	@NotNull
	@Override
	public String getDisplayText() {
		return "Insert Statement";
	}
}
