package cn.big.mybatis.alias;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.xml.XmlAttributeValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author yanglin
 */
public class AliasClassReference extends PsiReferenceBase<XmlAttributeValue> {

	private Function<AliasDesc, String> function = (input) -> {
		Objects.requireNonNull(input);
		return input.getAlias();
	};


	public AliasClassReference(@NotNull XmlAttributeValue element) {
		super(element, true);
	}

	@Nullable
	@Override
	public PsiElement resolve() {
		XmlAttributeValue attributeValue = getElement();
		if (attributeValue == null || attributeValue.getValue() == null) {
			return null;
		}
		return AliasFacade.getInstance(attributeValue.getProject()).findPsiClass(attributeValue, attributeValue.getValue()).orNull();
	}

	@NotNull
	@Override
	public Object[] getVariants() {
		AliasFacade aliasFacade = AliasFacade.getInstance(getElement().getProject());
		return aliasFacade.getAliasDescs(getElement()).stream().map(function).toArray(String[]::new);
	}

}
