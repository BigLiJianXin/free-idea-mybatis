package cn.big.mybatis.generate;

import cn.big.mybatis.dom.model.GroupTwo;
import cn.big.mybatis.dom.model.Mapper;
import cn.big.mybatis.dom.model.Select;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author yanglin
 */
public class SelectGenerator extends StatementGenerator {

	public SelectGenerator(@NotNull String... patterns) {
		super(patterns);
	}

	@NotNull
	@Override
	protected GroupTwo getTarget(@NotNull Mapper mapper, @NotNull PsiMethod method) {
		Select select = mapper.addSelect();
		setupResultType(method, select);
		return select;
	}

	private void setupResultType(PsiMethod method, Select select) {
		Optional<PsiClass> clazz = getSelectResultType(method);
		clazz.ifPresent(t -> select.getResultType().setValue(t));
	}

	@NotNull
	@Override
	public String getId() {
		return "SelectGenerator";
	}

	@NotNull
	@Override
	public String getDisplayText() {
		return "Select Statement";
	}
}
