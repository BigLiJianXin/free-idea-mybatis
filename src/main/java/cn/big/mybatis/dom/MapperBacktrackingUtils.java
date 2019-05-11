package cn.big.mybatis.dom;

import cn.big.mybatis.dom.model.Association;
import cn.big.mybatis.dom.model.Collection;
import cn.big.mybatis.dom.model.ParameterMap;
import cn.big.mybatis.dom.model.ResultMap;
import com.intellij.psi.PsiClass;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * @author yanglin
 */
public final class MapperBacktrackingUtils {

	private MapperBacktrackingUtils() {
		throw new UnsupportedOperationException();
	}

	public static Optional<PsiClass> getPropertyClazz(XmlAttributeValue attributeValue) {
		DomElement domElement = DomUtil.getDomElement(attributeValue);
		if (null == domElement) {
			return Optional.empty();
		}

		Collection collection = DomUtil.getParentOfType(domElement, Collection.class, true);
		if (null != collection && !isWithinSameTag(collection, attributeValue)) {
			return Optional.ofNullable(collection.getOfType().getValue());
		}

		Association association = DomUtil.getParentOfType(domElement, Association.class, true);
		if (null != association && !isWithinSameTag(association, attributeValue)) {
			return Optional.ofNullable(association.getJavaType().getValue());
		}

		ParameterMap parameterMap = DomUtil.getParentOfType(domElement, ParameterMap.class, true);
		if (null != parameterMap && !isWithinSameTag(parameterMap, attributeValue)) {
			return Optional.ofNullable(parameterMap.getType().getValue());
		}

		ResultMap resultMap = DomUtil.getParentOfType(domElement, ResultMap.class, true);
		if (null != resultMap && !isWithinSameTag(resultMap, attributeValue)) {
			return Optional.ofNullable(resultMap.getType().getValue());
		}
		return Optional.empty();
	}

	public static boolean isWithinSameTag(@NotNull DomElement domElement, @Nullable XmlElement xmlElement) {
		XmlTag xmlTag = PsiTreeUtil.getParentOfType(xmlElement, XmlTag.class);
		return null != xmlElement && domElement.getXmlTag().equals(xmlTag);
	}

}
