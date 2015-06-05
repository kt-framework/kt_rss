package jp.kt.rss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * フィード全体の情報.
 * <p>
 * アイテムを追加すると、自動的に投稿日時の降順に並べ替えられます.<br>
 * また、アイテムの最大件数を設定している場合は、同時に超えた分をカットします.
 * </p>
 *
 * @author yousuke.yazawa_bp
 * @author tatsuya.kumon
 */
public class Feed extends BaseEntity {
	/** 出力フィードの言語 */
	private static final String LANGUAGE = "ja";

	/** 出力文字コード */
	private static final String ENCODE = "UTF-8";

	/** 投稿日時の降順ソートのためのComparator */
	private static final Comparator<Item> COMPARATOR = new ItemComparator();

	/** コピーライト */
	private String copyright;

	/** アイテムリスト */
	private List<Item> itemList;

	/** アイテムの最大数 */
	private Integer maxItemLength;

	/**
	 * コンストラクタ.
	 */
	public Feed() {
		this.itemList = new ArrayList<Item>();
	}

	/**
	 * アイテムの最大数を設定する.
	 *
	 * @param maxItemLength
	 *            アイテムの最大数.<br>
	 *            0以下の場合は無制限となります.
	 */
	public void setMaxItemLength(int maxItemLength) {
		this.maxItemLength = maxItemLength;
	}

	/**
	 * 出力フィードの言語を取得する.
	 *
	 * @return 出力フィードの言語.
	 */
	public String getLanguage() {
		return LANGUAGE;
	}

	/**
	 * 出力文字コードを取得する.
	 *
	 * @return 文字コード.
	 */
	public String getEncode() {
		return ENCODE;
	}

	/**
	 * アイテム一覧を取得する.
	 *
	 * @return アイテム一覧.
	 */
	public List<Item> getItemList() {
		// アイテムリストの整理
		adjust();
		return itemList;
	}

	/**
	 * アイテムを1件追加する.
	 *
	 * @param item
	 *            アイテム.
	 */
	public void addItem(Item item) {
		this.itemList.add(item);
		// アイテムリストの整理
		adjust();
	}

	/**
	 * アイテムを複数件追加する.
	 *
	 * @param itemList
	 *            アイテムリスト.
	 */
	public void addItemList(List<Item> itemList) {
		this.itemList.addAll(itemList);
		// アイテムリストの整理
		adjust();
	}

	/**
	 * コピーライトを取得する.
	 *
	 * @return コピーライト.
	 */
	public String getCopyright() {
		return copyright;
	}

	/**
	 * コピーライトをセットする.
	 *
	 * @param copyright
	 *            コピーライト.
	 */
	public void setCopyright(String copyright) {
		this.copyright = removeInvalidChar(copyright, "copyright");
	}

	/**
	 * アイテムリストの整理.
	 * <p>
	 * 投稿日時の降順でソートする.<br>
	 * フィードの最大数が決められている場合は超える数をカットする
	 * </p>
	 */
	private void adjust() {
		// ソート
		Collections.sort(itemList, COMPARATOR);
		// フィード最大数を超えるアイテムを除去
		if (this.maxItemLength != null) {
			// 最大数を超えている分をカットする
			while (itemList.size() > maxItemLength) {
				this.itemList.remove(itemList.size() - 1);
			}
		}
	}
}
