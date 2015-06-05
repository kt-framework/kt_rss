package jp.kt.rss;

import java.util.Comparator;

/**
 * {@link Item}リストの降順ソートComparator.
 *
 * @author tatsuya.kumon
 */
class ItemComparator implements Comparator<Item> {
	/**
	 * 大小比較.
	 *
	 * @param o1
	 *            比較対象の最初のオブジェクト
	 * @param o2
	 *            比較対象の 2 番目のオブジェクト
	 * @return 最初の引数が 2 番目の引数より小さい場合は負の整数、両方が等しい場合は 0、最初の引数が 2 番目の引数より大きい場合は正の整数
	 */
	public int compare(Item o1, Item o2) {
		int result = 0;
		if (o1 == null || o1.getPublishDate() == null) {
			result = 1;
		} else if (o2 == null || o2.getPublishDate() == null) {
			result = -1;
		} else {
			result = o1.getPublishDate().compareTo(o2.getPublishDate());
		}
		// 降順なので-1をかける
		return (result * -1);
	}
}
