package cn.big.mybatis.action;

import cn.big.mybatis.util.DomUtils;
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil;
import com.intellij.sql.psi.SqlFile;

import org.jetbrains.annotations.NotNull;

/**
 * @author yanglin
 */
public class MybatisTypedHandler extends TypedHandlerDelegate {

	@Override
	public Result checkAutoPopup(char charTyped, @NotNull final Project project, @NotNull final Editor editor, @NotNull PsiFile file) {
		if (charTyped == '.' && DomUtils.isMybatisFile(file)) {
			return Result.STOP;
		}
		return super.checkAutoPopup(charTyped, project, editor, file);
	}

	@Override
	public Result charTyped(char c, @NotNull final Project project, @NotNull final Editor editor, @NotNull PsiFile file) {
		int index = editor.getCaretModel().getOffset() - 2;
		PsiFile topLevelFile = InjectedLanguageUtil.getTopLevelFile(file);
		boolean parameterCase = c == '{' &&
				index >= 0 &&
				editor.getDocument().getText().charAt(index) == '#' &&
				file instanceof SqlFile &&
				DomUtils.isMybatisFile(topLevelFile);
		if (parameterCase) {
			return Result.STOP;
		}
		return super.charTyped(c, project, editor, file);
	}

}