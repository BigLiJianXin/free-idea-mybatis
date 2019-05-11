package cn.big.mybatis.alias;

import com.google.common.collect.ImmutableSet;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import cn.big.mybatis.util.JavaUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * @author yanglin
 */
public class InnerAliasResolver extends AliasResolver {

	private final Set<AliasDesc> innerAliasDescs = ImmutableSet.of(
			JavaUtils.findClazz(project, "java.lang.String").map(t -> AliasDesc.create(t, "string")).get(),
			JavaUtils.findClazz(project, "java.lang.Byte").map(t -> AliasDesc.create(t, "byte")).get(),
			JavaUtils.findClazz(project, "java.lang.Long").map(t -> AliasDesc.create(t, "long")).get(),
			JavaUtils.findClazz(project, "java.lang.Short").map(t -> AliasDesc.create(t, "short")).get(),
			JavaUtils.findClazz(project, "java.lang.Integer").map(t -> AliasDesc.create(t, "int")).get(),
			JavaUtils.findClazz(project, "java.lang.Integer").map(t -> AliasDesc.create(t, "integer")).get(),
			JavaUtils.findClazz(project, "java.lang.Double").map(t -> AliasDesc.create(t, "double")).get(),
			JavaUtils.findClazz(project, "java.lang.Float").map(t -> AliasDesc.create(t, "float")).get(),
			JavaUtils.findClazz(project, "java.lang.Boolean").map(t -> AliasDesc.create(t, "boolean")).get(),
			JavaUtils.findClazz(project, "java.util.Date").map(t -> AliasDesc.create(t, "date")).get(),
			JavaUtils.findClazz(project, "java.math.BigDecimal").map(t -> AliasDesc.create(t, "decimal")).get(),
			JavaUtils.findClazz(project, "java.lang.Object").map(t -> AliasDesc.create(t, "object")).get(),
			JavaUtils.findClazz(project, "java.util.Map").map(t -> AliasDesc.create(t, "map")).get(),
			JavaUtils.findClazz(project, "java.util.HashMap").map(t -> AliasDesc.create(t, "hashmap")).get(),
			JavaUtils.findClazz(project, "java.util.List").map(t -> AliasDesc.create(t, "list")).get(),
			JavaUtils.findClazz(project, "java.util.ArrayList").map(t -> AliasDesc.create(t, "arraylist")).get(),
			JavaUtils.findClazz(project, "java.util.Collection").map(t -> AliasDesc.create(t, "collection")).get(),
			JavaUtils.findClazz(project, "java.util.Iterator").map(t -> AliasDesc.create(t, "iterator")).get()
	);

	public InnerAliasResolver(Project project) {
		super(project);
	}

	@NotNull
	@Override
	public Set<AliasDesc> getClassAliasDescriptions(@Nullable PsiElement element) {
		return innerAliasDescs;
	}

}
