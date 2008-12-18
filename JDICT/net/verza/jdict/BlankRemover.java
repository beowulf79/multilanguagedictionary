package net.verza.jdict;

public class BlankRemover {

	// ////////////////////////////////////////////////////////////////////////////////
	/*
	 * remove leading whitespace
	 */
	public static String ltrim(String source) {
		return source.replaceAll("^\\s+", "");
	}

	// ////////////////////////////////////////////////////////////////////////////////
	/*
	 * remove trailing whitespace
	 */
	public static String rtrim(String source) {
		return source.replaceAll("\\s+$", "");
	}

	// ////////////////////////////////////////////////////////////////////////////////
	/*
	 * replace multiple whitespaces between words with single blank
	 */
	public static String itrim(String source) {
		return source.replaceAll("\\b\\s{2,}\\b", " ");
	}

	// ////////////////////////////////////////////////////////////////////////////////
	/* remove leading&trailing whitespace */
	public static String lrtrim(String source) {
		return ltrim(rtrim(source));
	}

	// ////////////////////////////////////////////////////////////////////////////////
	/*
	 * remove all superfluous whitespaces in source string
	 */
	public static String trim(String source) {
		return itrim(ltrim(rtrim(source)));
	}

}