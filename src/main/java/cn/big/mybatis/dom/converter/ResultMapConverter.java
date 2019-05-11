package cn.big.mybatis.dom.converter;

import cn.big.mybatis.dom.model.IdDomElement;
import cn.big.mybatis.dom.model.Mapper;
import cn.big.mybatis.dom.model.ResultMap;
import cn.big.mybatis.util.MapperUtils;
import com.google.common.collect.Collections2;
import com.intellij.util.xml.ConvertContext;
import com.intellij.util.xml.DomElement;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author yanglin
 */
public class ResultMapConverter extends IdBasedTagConverter {

	@NotNull
	@Override
	public Collection<? extends IdDomElement> getComparisons(@Nullable Mapper mapper, ConvertContext context) {
		DomElement invocationElement = context.getInvocationElement();
		if (mapper == null) {
			return new ArrayList<>();
		}
		if (isContextElementOfResultMap(mapper, invocationElement)) {
			return doFilterResultMapItself(mapper, (ResultMap) invocationElement.getParent());
		} else {
			return mapper.getResultMaps();
		}
	}

	private boolean isContextElementOfResultMap(Mapper mapper, DomElement invocationElement) {
		return MapperUtils.isMapperWithSameNamespace(MapperUtils.getMapper(invocationElement), mapper)
				&& invocationElement.getParent() instanceof ResultMap;
	}

	private Collection<? extends IdDomElement> doFilterResultMapItself(Mapper mapper, final ResultMap resultMap) {
		return Collections2.filter(mapper.getResultMaps(), input -> {
			if (input == null) {
				return false;
			}
			String id = MapperUtils.getId(input);
			if (StringUtils.isBlank(id)) {
				return false;
			}
			return !id.equals(MapperUtils.getId(resultMap));
		});
	}

}
