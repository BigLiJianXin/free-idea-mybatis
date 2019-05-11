package cn.big.mybatis.alias;

import cn.big.mybatis.annotation.Annotation;
import cn.big.mybatis.util.JavaUtils;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.AnnotatedElementsSearch;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author yanglin
 */
public class AnnotationAliasResolver extends AliasResolver {

	private static final Function<PsiClass, AliasDesc> FUN = (psiClass) -> {
		Optional<String> txt = JavaUtils.getAnnotationValueText(psiClass, Annotation.ALIAS);
		return txt.map((t) -> {
			AliasDesc ad = new AliasDesc();
			ad.setAlias(txt.get());
			ad.setClazz(psiClass);
			return ad;
		}).orElse(null);
	};

	public AnnotationAliasResolver(Project project) {
		super(project);
	}

	public static AnnotationAliasResolver getInstance(@NotNull Project project) {
		return project.getComponent(AnnotationAliasResolver.class);
	}

	@NotNull
	@Override
	public Set<AliasDesc> getClassAliasDescriptions(@Nullable PsiElement element) {
		Optional<PsiClass> clazz = Annotation.ALIAS.toPsiClass(project);
		return clazz.map((t) -> {
			Collection<PsiClass> res = AnnotatedElementsSearch.searchPsiClasses(t, GlobalSearchScope.allScope(project)).findAll();
			return res.stream().map(FUN).collect(Collectors.toSet());
		}).orElse(new HashSet<>());
	}

}
