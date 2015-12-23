package com.paulytee.olaf.util;

/**
 * Created by Mary on 12/16/2015.
 */
public class LanguageConstants {
	public enum Language {
		US("US"), spa_MEX("spa-MEX"), CHINA("CHINA"), hi_IN("hi-IN");

		private String text;

		Language(String text) {
			this.text = text;
		}

		public String getText() {
			return this.text;
		}

		public static Language fromString(String text) {
			if (text != null) {
				for (Language l : Language.values()) {
					if (text.equalsIgnoreCase(l.text)) {
						return l;
					}
				}
			}
			return null;
		}
	}

}
