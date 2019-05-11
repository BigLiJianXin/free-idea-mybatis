package cn.big.mybatis.inspection;

import cn.big.mybatis.annotation.Annotation;
import cn.big.mybatis.dom.model.Select;
import cn.big.mybatis.generate.StatementGenerator;
import cn.big.mybatis.locator.MapperLocator;
import cn.big.mybatis.service.JavaService;
import cn.big.mybatis.util.JavaUtils;
import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiMethod;
import com.intellij.util.xml.DomElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author yanglin
 */
public class MapperMethodInspection extends MapperInspection {
	@Nullable
	@Override
	public ProblemDescriptor[] checkMethod(
			@NotNull final PsiMethod method,
			@NotNull final InspectionManager manager,
			final boolean isOnTheFly) {
		if (!MapperLocator.getInstance(method.getProject()).process(method) ||
				JavaUtils.isAnyAnnotationPresent(method, Annotation.STATEMENT_SYMMETRIES)) {
			return EMPTY_ARRAY;
		}

		final List<ProblemDescriptor> problems = createProblemDescriptors(method, manager, isOnTheFly);
		return problems.toArray(new ProblemDescriptor[0]);
	}

	private List<ProblemDescriptor> createProblemDescriptors(
			final PsiMethod method,
			final InspectionManager manager,
			final boolean isOnTheFly) {
		List<ProblemDescriptor> problems = new ArrayList<>(2);
		Optional<ProblemDescriptor> optionalProblem = checkStatementExists(method, manager, isOnTheFly);
		Consumer<ProblemDescriptor> consumer = t -> problems.add(t);
		optionalProblem.ifPresent(consumer);
		optionalProblem = checkResultType(method, manager, isOnTheFly);
		optionalProblem.ifPresent(consumer);
		return problems;
	}

	private Optional<ProblemDescriptor> checkResultType(
			final PsiMethod method,
			final InspectionManager manager,
			final boolean isOnTheFly) {
		final Optional<DomElement> optionalDomElement =
				JavaService.getInstance(method.getProject())
						.findStatement(method);

		if (!optionalDomElement.isPresent()) {
			return Optional.empty();
		}

		final DomElement domElement = optionalDomElement.get();

		if (domElement instanceof Select) {
			final Select selectStatement = (Select) domElement;

			if (selectStatement.getResultMap().getValue() != null) {
				return Optional.empty();
			}

			final Optional<PsiClass> methodResultType = StatementGenerator.getSelectResultType(method);
			final PsiClass selectResultType = selectStatement.getResultType().getValue();
			final PsiIdentifier methodName = method.getNameIdentifier();

			if (methodName != null) {
				if (methodResultType.isPresent() && (
						selectResultType == null ||
								!methodResultType.get().equals(selectResultType) &&
										!selectResultType.isInheritor(methodResultType.get(), true))) {
					return Optional.of(
							manager.createProblemDescriptor(
									methodName,
									"Result type doesn't match for Select id=\"#ref\"",
									new ResultTypeQuickFix(selectStatement, methodResultType.get()),
									ProblemHighlightType.GENERIC_ERROR,
									isOnTheFly));
				}

				if (!methodResultType.isPresent() && selectResultType != null) {
					return Optional.of(
							manager.createProblemDescriptor(
									methodName,
									"Result type doesn't match for Select id=\"#ref\"",
									(LocalQuickFix) null,
									ProblemHighlightType.GENERIC_ERROR,
									isOnTheFly));
				}
			}
		}

		return Optional.empty();
	}

	private Optional<ProblemDescriptor> checkStatementExists(
			final PsiMethod method,
			final InspectionManager manager,
			final boolean isOnTheFly) {
		final PsiIdentifier methodName = method.getNameIdentifier();

		if (!JavaService.getInstance(method.getProject()).findStatement(method).isPresent() && null != methodName) {
			return Optional.of(manager.createProblemDescriptor(
					methodName,
					"Statement with id=\"#ref\" not defined in mapper XML",
					new StatementNotExistsQuickFix(method),
					ProblemHighlightType.GENERIC_ERROR,
					isOnTheFly));
		}

		return Optional.empty();
	}
}
