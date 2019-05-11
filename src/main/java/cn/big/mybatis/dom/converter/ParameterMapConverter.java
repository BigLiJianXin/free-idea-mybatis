package cn.big.mybatis.dom.converter;

import cn.big.mybatis.dom.model.IdDomElement;
import cn.big.mybatis.dom.model.Mapper;
import com.intellij.util.xml.ConvertContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author yanglin
 */
public class ParameterMapConverter extends IdBasedTagConverter {

	@NotNull
	@Override
	public Collection<? extends IdDomElement> getComparisons(@Nullable Mapper mapper,
															 ConvertContext context) {
		if (mapper == null) {
			return new ArrayList<>();
		}
		return mapper.getParameterMaps();
	}

}
