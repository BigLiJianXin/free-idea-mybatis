package cn.big.mybatis.contributor;

import cn.big.mybatis.dom.model.IdDomElement;
import cn.big.mybatis.util.DomUtils;
import cn.big.mybatis.util.MapperUtils;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.injected.editor.DocumentWindow;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;


/**
 * @author yanglin
 */
public class SqlParamCompletionContributor extends CompletionContributor {

	@Override
	public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull final CompletionResultSet result) {
		if (parameters.getCompletionType() != CompletionType.BASIC) {
			return;
		}

		PsiElement position = parameters.getPosition();
		PsiFile topLevelFile = InjectedLanguageManager.getInstance(position.getProject()).getTopLevelFile(position);
		if (DomUtils.isMybatisFile(topLevelFile)) {
			if (shouldAddElement(position.getContainingFile(), parameters.getOffset())) {
				process(topLevelFile, result, position);
			}
		}
	}

	private void process(PsiFile xmlFile, CompletionResultSet result, PsiElement position) {
		DocumentWindow documentWindow = InjectedLanguageUtil.getDocumentWindow(position);
		if (null == documentWindow) {
			return;
		}
		int offset = documentWindow.injectedToHost(position.getTextOffset());
		Optional<IdDomElement> idDomElement = MapperUtils.findParentIdDomElement(xmlFile.findElementAt(offset));
		idDomElement.ifPresent(t -> {
			TestParamContributor.addElementForPsiParameter(position.getProject(), result, t);
			result.stopHere();
		});
	}

	private boolean shouldAddElement(PsiFile file, int offset) {
		String text = file.getText();
		for (int i = offset - 1; i > 0; i--) {
			char c = text.charAt(i);
			if (c == '{' && text.charAt(i - 1) == '#') return true;
		}
		return false;
	}
}