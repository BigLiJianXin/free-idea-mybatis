package cn.big.mybatis.setting;

import cn.big.mybatis.generate.GenerateModel;
import cn.big.mybatis.generate.GenerateUtil;
import cn.big.mybatis.generate.StatementGenerator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * @author yanglin
 */
@State(name = "MybatisSettings",
		storages = @Storage(file = "$APP_CONFIG$/mybatis.xml"))
public class MybatisSetting implements PersistentStateComponent<Element> {

	private GenerateModel statementGenerateModel;

	private Gson gson = new Gson();

	private Type gsonTypeToken = new TypeToken<Set<String>>() {
	}.getType();

	public MybatisSetting() {
		statementGenerateModel = GenerateUtil.START_WITH_MODEL;
	}

	public static MybatisSetting getInstance() {
		return ServiceManager.getService(MybatisSetting.class);
	}

	@Nullable
	@Override
	public Element getState() {
		Element element = new Element("MybatisSettings");
		element.setAttribute(GenerateUtil.INSERT_GENERATOR.getId(), gson.toJson(GenerateUtil.INSERT_GENERATOR.getPatterns()));
		element.setAttribute(GenerateUtil.DELETE_GENERATOR.getId(), gson.toJson(GenerateUtil.DELETE_GENERATOR.getPatterns()));
		element.setAttribute(GenerateUtil.UPDATE_GENERATOR.getId(), gson.toJson(GenerateUtil.UPDATE_GENERATOR.getPatterns()));
		element.setAttribute(GenerateUtil.SELECT_GENERATOR.getId(), gson.toJson(GenerateUtil.SELECT_GENERATOR.getPatterns()));
		element.setAttribute("statementGenerateModel", String.valueOf(statementGenerateModel.getIdentifier()));
		return element;
	}

	@Override
	public void loadState(@NotNull Element state) {
		loadState(state, GenerateUtil.INSERT_GENERATOR);
		loadState(state, GenerateUtil.DELETE_GENERATOR);
		loadState(state, GenerateUtil.UPDATE_GENERATOR);
		loadState(state, GenerateUtil.SELECT_GENERATOR);
		statementGenerateModel = GenerateModel.getInstance(state.getAttributeValue("statementGenerateModel"));
	}

	private void loadState(Element state, StatementGenerator generator) {
		String attribute = state.getAttributeValue(generator.getId());
		if (null != attribute) {
			generator.setPatterns(gson.fromJson(attribute, gsonTypeToken));
		}
	}

	public GenerateModel getStatementGenerateModel() {
		return statementGenerateModel;
	}

	public void setStatementGenerateModel(GenerateModel statementGenerateModel) {
		this.statementGenerateModel = statementGenerateModel;
	}
}
