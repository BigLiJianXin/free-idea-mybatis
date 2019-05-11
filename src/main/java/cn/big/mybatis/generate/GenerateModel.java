package cn.big.mybatis.generate;

import java.util.Collection;

/**
 * @author yanglin
 */
public abstract class GenerateModel {

	public static GenerateModel getInstance(String identifier) {
		try {
			return getInstance(Integer.valueOf(identifier));
		} catch (Exception e) {
			return GenerateUtil.START_WITH_MODEL;
		}
	}

	public static GenerateModel getInstance(int identifier) {
		switch (identifier) {
			case 0:
				return GenerateUtil.START_WITH_MODEL;
			case 1:
				return GenerateUtil.END_WITH_MODEL;
			case 2:
				return GenerateUtil.CONTAIN_MODEL;
			default:
				throw new AssertionError();
		}
	}

	public boolean matchesAny(String[] patterns, String target) {
		for (String pattern : patterns) {
			if (apply(pattern, target)) {
				return true;
			}
		}
		return false;
	}

	public boolean matchesAny(Collection<String> patterns, String target) {
		return matchesAny(patterns.toArray(new String[0]), target);
	}

	protected abstract boolean apply(String pattern, String target);

	public abstract int getIdentifier();

	static class StartWithModel extends GenerateModel {

		@Override
		protected boolean apply(String pattern, String target) {
			return target.startsWith(pattern);
		}

		@Override
		public int getIdentifier() {
			return 0;
		}
	}

	static class EndWithModel extends GenerateModel {

		@Override
		protected boolean apply(String pattern, String target) {
			return target.endsWith(pattern);
		}

		@Override
		public int getIdentifier() {
			return 1;
		}
	}

	static class ContainModel extends GenerateModel {

		@Override
		protected boolean apply(String pattern, String target) {
			return target.contains(pattern);
		}

		@Override
		public int getIdentifier() {
			return 2;
		}
	}
}