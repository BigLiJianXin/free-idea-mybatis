package cn.big.mybatis.dom.description;

import cn.big.mybatis.util.DomUtils;
import com.intellij.openapi.module.Module;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.DomFileDescription;
import cn.big.mybatis.dom.model.Configuration;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author yanglin
 */
public class ConfigurationDescription extends DomFileDescription<Configuration> {

	public ConfigurationDescription() {
		super(Configuration.class, "configuration");
	}

	@Override
	public boolean isMyFile(@NotNull XmlFile file, @Nullable Module module) {
		return DomUtils.isMybatisConfigurationFile(file);
	}

}
