package cn.big.mybatis.alias;

import cn.big.mybatis.util.MapperUtils;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author yanglin
 */
public class ConfigPackageAliasResolver extends PackageAliasResolver {

	public ConfigPackageAliasResolver(Project project) {
		super(project);
	}

	@NotNull
	@Override
	public Collection<String> getPackages(@Nullable PsiElement element) {
		final List<String> result = new ArrayList<>();
		MapperUtils.processConfiguredPackage(project, (pkg) -> {
			result.add(pkg.getName().getStringValue());
			return true;
		});
		return result;
	}

}
