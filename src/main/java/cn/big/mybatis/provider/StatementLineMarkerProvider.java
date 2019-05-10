package cn.big.mybatis.provider;

import cn.big.mybatis.dom.model.Delete;
import cn.big.mybatis.dom.model.GroupTwo;
import cn.big.mybatis.dom.model.IdDomElement;
import cn.big.mybatis.dom.model.Insert;
import cn.big.mybatis.dom.model.Select;
import cn.big.mybatis.dom.model.Update;
import cn.big.mybatis.util.Icons;
import cn.big.mybatis.util.JavaUtils;
import cn.big.mybatis.util.MapperUtils;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomUtil;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author yanglin
 */
public class StatementLineMarkerProvider extends SimpleLineMarkerProvider<XmlTag, PsiMethod> {

    private static final ImmutableList<Class<? extends GroupTwo>> TARGET_TYPES = ImmutableList.of(
            Select.class,
            Update.class,
            Insert.class,
            Delete.class
    );

    @Override
    public boolean isTheElement(@NotNull PsiElement element) {
        return element instanceof XmlTag
                && MapperUtils.isElementWithinMybatisFile(element)
                && isTargetType(element);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    @Override
    public Optional<PsiMethod> apply(@NotNull XmlTag from) {
        DomElement domElement = DomUtil.getDomElement(from);
        return null == domElement ? Optional.<PsiMethod>absent() : JavaUtils.findMethod(from.getProject(), (IdDomElement) domElement);
    }

    private boolean isTargetType(PsiElement element) {
        DomElement domElement = DomUtil.getDomElement(element);
        for (Class<?> clazz : TARGET_TYPES) {
            if (clazz.isInstance(domElement))
                return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    @Override
    public Navigatable getNavigatable(@NotNull XmlTag from, @NotNull PsiMethod target) {
        return (Navigatable) target.getNavigationElement();
    }

    @NotNull
    @Override
    public String getTooltip(@NotNull XmlTag from, @NotNull PsiMethod target) {
        return "Data access object found - " + target.getContainingClass().getQualifiedName();
    }

    @NotNull
    @Override
    public Icon getIcon() {
        return Icons.STATEMENT_LINE_MARKER_ICON;
    }

}
