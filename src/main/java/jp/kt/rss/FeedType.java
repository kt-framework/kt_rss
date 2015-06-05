package jp.kt.rss;

import java.io.Serializable;

/**
 * フィードの形式を定義するクラス.
 *
 * @author yousuke.yazawa_bp
 * @author tatsuya.kumon
 */
public class FeedType implements Serializable {
	/**
	 * RSS1.0でフィードを出力するための定義.
	 */
	public static final FeedType RSS_1_0 = new FeedType("rss_1.0", false);
	/**
	 * RSS2.0でフィードを出力するための定義.
	 */
	public static final FeedType RSS_2_0 = new FeedType("rss_2.0", true);

	/** 出力フィードタイプ文字列 */
	private String type;

	/** Podcast対応かどうかのフラグ */
	private boolean adjustPodcast;

	/**
	 * コンストラクタ.
	 *
	 * @param type
	 *            出力フィードの出力形式.
	 * @param adjustPodcast
	 *            Podcast対応フラグ.
	 */
	private FeedType(String type, boolean adjustPodcast) {
		this.type = type;
		this.adjustPodcast = adjustPodcast;
	}

	/**
	 * Podcast対応かどうかのフラグを取得.<br>
	 * 同一パッケージのクラスからのみ参照可.
	 *
	 * @return Podcastに対応している場合はtrueを返す<br>
	 *         対応していない場合はfalseを返す.
	 */
	boolean isAdjustPodcast() {
		return adjustPodcast;
	}

	/**
	 * 出力フィードの出力形式を文字列で取得する.<br>
	 * 同一パッケージのクラスからのみ参照可.
	 *
	 * @return 出力フィードの出力形式.
	 */
	String getType() {
		return type;
	}
}
