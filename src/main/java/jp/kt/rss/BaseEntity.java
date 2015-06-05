package jp.kt.rss;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * FeedとItemの基底となるエンティティ.
 *
 * @author tatsuya.kumon
 */
abstract class BaseEntity implements Serializable {
	/** タイトル */
	private String title;

	/** リンクURL */
	private String link;

	/** 概要 */
	private String description;

	/** 不正な文字のリスト */
	private List<InvalidChar> invalidCharList;

	/**
	 * タイトルのセット.
	 *
	 * @param title
	 *            セットする title
	 */
	public void setTitle(String title) {
		this.title = removeInvalidChar(title, "title");
	}

	/**
	 * リンクのセット.
	 *
	 * @param link
	 *            セットする link
	 */
	public void setLink(String link) {
		this.link = removeInvalidChar(link, "link");
	}

	/**
	 * 概要のセット.
	 * <p>
	 * content:encodedタグに出力される内容.<br>
	 * HTML可.
	 * </p>
	 *
	 * @param description
	 *            セットする description
	 */
	public void setDescription(String description) {
		this.description = removeInvalidChar(description, "description");
	}

	/**
	 * タイトルを取得する.
	 *
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * リンクURLを取得する.
	 *
	 * @return link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * 概要を取得する.
	 *
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 不正文字発見リストを返す
	 *
	 * @return invalidCharList
	 */
	List<InvalidChar> getInvalidCharList() {
		return invalidCharList;
	}

	/**
	 * XMLとして不正な文字を除外する.
	 *
	 * @param text
	 *            精査する文字列
	 * @param variableName
	 *            変数名.<br>
	 *            ログ出力のために必要.
	 * @return 不正な文字を除外した文字列を返す.
	 */
	String removeInvalidChar(String text, String variableName) {
		if (text == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		char[] cArray = text.toCharArray();
		List<Character> cList = null;
		for (Character c : cArray) {
			if (XMLChar.isValid(c) || Character.isHighSurrogate(c)) {
				// XML文字として妥当ならOK
				// または、不正と判断されても上位サロゲート単位の場合はOK
				sb.append(c);
			} else {
				// 不正文字が存在した
				if (cList == null) {
					cList = new ArrayList<Character>();
				}
				cList.add(c);
			}
		}
		if (cList != null && cList.size() > 0) {
			// 不正文字が存在した場合はInvalidCharオブジェクトをセット
			if (invalidCharList == null) {
				invalidCharList = new ArrayList<InvalidChar>();
			}
			invalidCharList.add(new InvalidChar(variableName, cList));
		}
		return sb.toString();
	}

	/**
	 * 不正文字クラス.
	 *
	 * @author tatsuya.kumon
	 */
	class InvalidChar implements Serializable {
		private static final long serialVersionUID = 1L;
		private String variableName;
		private List<Character> charList;

		private InvalidChar(String variableName, List<Character> charList) {
			this.variableName = variableName;
			this.charList = charList;
		}

		/**
		 * @return variableName
		 */
		String getVariableName() {
			return variableName;
		}

		/**
		 * @return charList
		 */
		List<Character> getCharList() {
			return charList;
		}
	}
}
