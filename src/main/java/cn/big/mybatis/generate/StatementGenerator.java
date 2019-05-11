package cn.big.mybatis.generate;

import cn.big.mybatis.dom.model.GroupTwo;
import cn.big.mybatis.dom.model.Mapper;
import cn.big.mybatis.service.EditorService;
import cn.big.mybatis.service.JavaService;
import cn.big.mybatis.setting.MybatisSetting;
import cn.big.mybatis.ui.ListSelectionListener;
import cn.big.mybatis.ui.UiComponentFacade;
import cn.big.mybatis.util.CollectionUtils;
import cn.big.mybatis.util.JavaUtils;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiPrimitiveType;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.CommonProcessors.CollectProcessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;


/**
 * @author yanglin
 */
public abstract class StatementGenerator {


	private static final Function<Mapper, String> FUN = (mapper) -> {
		if (mapper == null) return "";
		VirtualFile vf = mapper.getXmlTag().getContainingFile().getVirtualFile();
		if (null == vf) return "";
		return vf.getCanonicalPath();
	};

	public static Optional<PsiClass> getSelectResultType(@Nullable PsiMethod method) {
		if (null == method) {
			return Optional.empty();
		}
		PsiType returnType = method.getReturnType();
		if (returnType instanceof PsiPrimitiveType && !PsiType.VOID.equals(returnType)) {
			return JavaUtils.findClazz(method.getProject(), ((PsiPrimitiveType) returnType).getBoxedTypeName());
		} else if (returnType instanceof PsiClassReferenceType) {
			PsiClassReferenceType type = (PsiClassReferenceType) returnType;
			if (type.hasParameters()) {
				PsiType[] parameters = type.getParameters();
				if (parameters.length == 1) {
					type = (PsiClassReferenceType) parameters[0];
				}
			}
			return java.util.Optional.ofNullable(type.resolve());
		}
		return Optional.empty();
	}


	public static void applyGenerate(@Nullable final PsiMethod method) {
		if (null == method) return;
		final Project project = method.getProject();
		final StatementGenerator[] generators = getGenerators(method);
		if (1 == generators.length) {
			generators[0].execute(method);
		} else {
			JBPopupFactory.getInstance().createListPopup(
					new BaseListPopupStep<StatementGenerator>("[ Statement type for method: " + method.getName() + "]", generators) {
						@Override
						public PopupStep onChosen(StatementGenerator selectedValue, boolean finalChoice) {
							return this.doFinalStep(() -> WriteCommandAction.runWriteCommandAction(project, () -> selectedValue.execute(method)));
						}
					}
			).showInFocusCenter();
		}
	}

	@NotNull
	public static StatementGenerator[] getGenerators(@NotNull PsiMethod method) {
		GenerateModel model = MybatisSetting.getInstance().getStatementGenerateModel();
		String target = method.getName();
		List<StatementGenerator> result = Lists.newArrayList();
		for (StatementGenerator generator : GenerateUtil.ALL) {
			if (model.matchesAny(generator.getPatterns(), target)) {
				result.add(generator);
			}
		}
		return CollectionUtils.isNotEmpty(result) ? result.toArray(new StatementGenerator[0]) : GenerateUtil.ALL.toArray(new StatementGenerator[0]);
	}

	private Set<String> patterns;

	public StatementGenerator(@NotNull String... patterns) {
		this.patterns = Sets.newHashSet(patterns);
	}

	public void execute(@NotNull final PsiMethod method) {
		PsiClass psiClass = method.getContainingClass();
		if (null == psiClass) return;
		CollectProcessor<Mapper> processor = new CollectProcessor<>();
		JavaService.getInstance(method.getProject()).process(psiClass, processor);
		final List<Mapper> mappers = new ArrayList<>(processor.getResults());
		if (1 == mappers.size()) {
			setupTag(method, Iterables.getOnlyElement(mappers, null));
		} else if (mappers.size() > 1) {
			String[] paths = mappers.stream().map(FUN).toArray(String[]::new);
			UiComponentFacade.getInstance(method.getProject()).showListPopup("Choose target mapper xml to generate", new ListSelectionListener() {
				@Override
				public void selected(int index) {
					setupTag(method, mappers.get(index));
				}

				@Override
				public boolean isWriteAction() {
					return true;
				}
			}, paths);
		}
	}

	private void setupTag(PsiMethod method, Mapper mapper) {
		GroupTwo target = getTarget(mapper, method);
		target.getId().setStringValue(method.getName());
		target.setValue(" ");
		XmlTag tag = target.getXmlTag();
		int offset = tag.getTextOffset() + tag.getTextLength() - tag.getName().length() + 1;
		EditorService editorService = EditorService.getInstance(method.getProject());
		editorService.format(tag.getContainingFile(), tag);
		editorService.scrollTo(tag, offset);
	}

	@Override
	public String toString() {
		return this.getDisplayText();
	}

	@NotNull
	protected abstract GroupTwo getTarget(@NotNull Mapper mapper, @NotNull PsiMethod method);

	@NotNull
	public abstract String getId();

	@NotNull
	public abstract String getDisplayText();

	public Set<String> getPatterns() {
		return patterns;
	}

	public void setPatterns(Set<String> patterns) {
		this.patterns = patterns;
	}

}
