package jp.kt.rss;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jp.kt.logger.ApplicationLogger;
import jp.kt.rss.BaseEntity.InvalidChar;
import jp.kt.tool.Validator;

import com.sun.syndication.feed.module.DCModule;
import com.sun.syndication.feed.module.DCSubject;
import com.sun.syndication.feed.module.Module;
import com.sun.syndication.feed.synd.SyndCategory;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.SyndFeedOutput;

/**
 * フィードの読み込みとフィードの生成を行うクラス.
 * <hr style="border-style:dashed">
 * <p>
 * 【サンプル1：データを直接入力し、フィード生成】<br>
 * <br>
 * <blockquote>
 *
 * <pre style="font-size:small;">
 * // フィードレベルの情報設定
 * Feed feed = new Feed();
 * feed.setTitle(&quot;コード・ブルー　スタッフ日記&quot;);
 * feed.setLink(&quot;http://blog.fujitv.co.jp/codeblue/index.html&quot;);
 * feed.setDescription(&quot;『コード・ブルー～ドクターヘリ緊急救命』スタッフによる公式ブログ&quot;);
 * feed.setCopyright(&quot;(C) FujiTelevision Network, Inc.All rights reserved.&quot;);
 * // アイテム追加 1つ目
 * Item item1 = new Item();
 * item1.setTitle(&quot;２話～☆☆&quot;);
 * item1.setLink(&quot;http://blog.fujitv.co.jp/codeblue/E20100117001.html&quot;);
 * item1.setDescription(&quot;２話もたくさんの方々に見ていただけたようで・・・・・・・・&quot;);
 * item1.setContent(&quot;２話もたくさんの方々に見ていただけたようで・・・・・・・・&quot;);
 * item1.setAuthor(&quot;KN&quot;);
 * item1.setPublishDate(DateUtil.getDate(&quot;2010/01/20 00:07:00&quot;,
 * 		&quot;yyyy/MM/dd HH:mm:ss&quot;));
 * item1.addCategory(&quot;O.A.&quot;);
 * feed.addItem(item1);
 * // アイテム追加 2つ目
 * Item item2 = new Item();
 * item2.setTitle(&quot;ＣＧ？&quot;);
 * item2.setLink(&quot;http://blog.fujitv.co.jp/codeblue/E20100117001.html&quot;);
 * item2.setDescription(&quot;たくさんの感想ありがとうございます・・・・・・・・&quot;);
 * item2.setContent(&quot;たくさんの感想ありがとうございます・・・・・・・・&quot;);
 * item2.setAuthor(&quot;KN&quot;);
 * item2.setPublishDate(DateUtil.getDate(&quot;2010/01/14 01:03:00&quot;,
 * 		&quot;yyyy/MM/dd HH:mm:ss&quot;));
 * item2.addCategory(&quot;編集&quot;);
 * feed.addItem(item2);
 * // フィードタイプを指定してフィードテキストを生成
 * FeedUtil feedUtil = new FeedUtil(logger);
 * String feedText = feedUtil.create(feed, FeedType.RSS_1_0);
 * </pre>
 *
 * </blockquote>
 *
 * <hr style="border-style:dashed">
 * <p>
 * 【サンプル2：複数のフィードファイルを読み込んで、まとめフィード生成】<br>
 * <br>
 * <blockquote>
 *
 * <pre style="font-size:small;">
 * // フィードレベルの情報設定
 * Feed feed = new Feed();
 * feed.setTitle(&quot;まとめフィード（ファイル読み込み版）&quot;);
 * feed.setLink(&quot;http://wwwz.fujitv.co.jp/zoo/index.html&quot;);
 * feed.setDescription(&quot;いくつかのブログをまとめたフィードです&quot;);
 * feed.setCopyright(&quot;(C) FujiTelevision Network, Inc.All rights reserved.&quot;);
 * // まとめフィード内の最大アイテム数を設定
 * feed.setMaxItemLength(100);
 * // 指定したディレクトリにある全てのフィードファイルを読み込む
 * FileUtil dir = new FileUtil(&quot;/var/sample/feed&quot;);
 * List&lt;String&gt; filenameList = dir.getFilenameList();
 * FeedUtil feedUtil = new FeedUtil(logger);
 * for (String filename : filenameList) {
 * 	// ファイルパスへ移動
 * 	dir.moveSub(filename);
 * 	// ファイル読み込み＆parse
 * 	Feed tempFeed = feedUtil.parse(dir.getFileContent());
 * 	// まとめフィードにアイテムをセット（この時点でFeedクラス内でソートと最大件数を超えた場合のカットは自動的に行われている）
 * 	feed.addItemList(tempFeed.getItemList());
 * 	// ディレクトリに戻る
 * 	dir.moveParent();
 * }
 * // （参考）
 * // descriptionは要約、content:encodedタグは全文という使い方が一般的なので
 * // そのようにするためのサンプルソース
 * for (Item item : feed.getItemList()) {
 * 	// descriptionはタグを除去して、200バイトでカットする
 * 	String newText1 = HtmlUtil.removeHtmlTag(item.getDescription());
 * 	String newText2 = StringUtil.cutString(newText1, 200, &quot;...&quot;);
 * 	item.setDescription(newText2);
 * }
 * // フィードタイプを指定してフィードテキストを生成
 * FeedUtil feedUtil = new FeedUtil(logger);
 * String feedText = feedUtil.create(feed, FeedType.RSS_2_0);
 * </pre>
 *
 * </blockquote>
 *
 * @author yousuke.yazawa_bp
 * @author tatsuya.kumon
 */
public class FeedUtil {
	private ApplicationLogger logger;

	/**
	 * コンストラクタ.
	 *
	 * @param logger
	 *            Logger
	 */
	public FeedUtil(ApplicationLogger logger) {
		this.logger = logger;
	}

	/**
	 * フィード情報を読み込んで{@link Feed}オブジェクトとして返す.<br>
	 *
	 * @param rssText
	 *            パースするフィード文字列.
	 * @return {@link Feed}フィードの内容が格納されたオブジェクト.<br>
	 * @throws Exception
	 *             処理中に例外発生した場合
	 */
	public Feed parse(String rssText) throws Exception {
		Feed feed = null;
		BufferedReader br = null;
		try {
			StringReader sr = new StringReader(rssText);
			br = new BufferedReader(sr);
			SyndFeedInput sfi = new SyndFeedInput();
			SyndFeed sf = sfi.build(br);
			// ブログ情報を取得
			feed = new Feed();
			feed.setTitle(sf.getTitle());
			feed.setLink(sf.getLink());
			feed.setDescription(sf.getDescription());
			feed.setCopyright(sf.getCopyright());
			// 記事一覧を取得
			List<?> seList = sf.getEntries();
			for (int j = 0; j < seList.size(); j++) {
				SyndEntry se = (SyndEntry) seList.get(j);
				// content:encodedタグの内容を取得
				List<?> contentList = se.getContents();
				String content = null;
				if (contentList != null && contentList.size() > 0) {
					content = ((SyndContent) contentList.get(0)).getValue();
				}
				// Itemオブジェクト作成
				Item item = new Item();
				item.setTitle(se.getTitle());
				if (!Validator.isEmpty(se.getLink())) {
					item.setLink(se.getLink());
				} else {
					item.setLink(se.getUri());
				}
				if (se.getDescription() != null) {
					item.setDescription(se.getDescription().getValue());
				}
				item.setContent(content);
				item.setAuthor(se.getAuthor());
				item.setPublishDate(se.getPublishedDate());
				List<?> categoryList = se.getCategories();
				for (int i = 0; categoryList != null && i < categoryList.size(); i++) {
					SyndCategory category = (SyndCategory) categoryList.get(i);
					item.addCategory(category.getName());
				}
				// dc:subject
				Module module = se.getModule(DCModule.URI);
				if (module != null) {
					DCModule dcModule = (DCModule) module;
					List<?> subjectList = dcModule.getSubjects();
					for (int i = 0; subjectList != null
							&& i < subjectList.size(); i++) {
						DCSubject sub = (DCSubject) subjectList.get(i);
						item.addDcSubject(sub.getValue());
					}
				}
				feed.addItem(item);
			}
		} finally {
			if (br != null) {
				br.close();
			}
		}
		return feed;
	}

	/**
	 * フィードタイプを設定してフィードを生成する.
	 *
	 * @param feed
	 *            出力フィード情報.
	 * @param feedType
	 *            {@link FeedType}クラスの定数で指定する.
	 * @return フィード文字列
	 * @throws FeedException
	 *             フィード文字列が生成できなかった場合
	 */
	public String create(Feed feed, FeedType feedType) throws FeedException {
		// 不正文字ログ出力
		outputInvalidCharLog(feed);
		/*
		 * フィードレベルのデータ生成
		 */
		// SyndFeedにオブジェクトに情報セット
		SyndFeed sf = new SyndFeedImpl();
		// 言語、文字コード
		sf.setLanguage(feed.getLanguage());
		sf.setEncoding(feed.getEncode());
		// フィードの形式
		sf.setFeedType(feedType.getType());
		// コピーライト
		sf.setCopyright(feed.getCopyright());
		// タイトル、リンクURL
		sf.setTitle(feed.getTitle());
		sf.setLink(feed.getLink());
		// ブログ説明文
		sf.setDescription(feed.getDescription());

		/*
		 * アイテムレベルのデータ生成
		 */
		List<SyndEntry> seList = new ArrayList<SyndEntry>();
		// TODO 後日実装を行うため現状コメントアウトを行う. 20100118 by yousuke.yazawa_bp
		// boolean isPodcast = false;
		Iterator<Item> it = feed.getItemList().iterator();
		while (it.hasNext()) {
			Item item = it.next();
			// 不正文字ログ出力
			outputInvalidCharLog(item);
			// SyndEntryオブジェクトに情報セット
			SyndEntry se = new SyndEntryImpl();
			// タイトル、リンクURL、投稿日時、投稿者
			se.setTitle(item.getTitle());
			se.setLink(item.getLink());
			if (feedType.equals(FeedType.RSS_1_0)) {
				// RSS1.0の場合は、rdf:liタグを出力するために必要
				// RSS2.0で実行してしまうと、<guid isPermaLink="false">になってしまうので実行しない
				se.setUri(item.getLink());
			}
			se.setPublishedDate(item.getPublishDate());
			se.setAuthor(item.getAuthor());
			// 本文（description）
			SyndContentImpl description = new SyndContentImpl();
			description.setType("text/html");
			description.setValue(item.getDescription());
			se.setDescription(description);
			// 本文（content:encoded）
			List<SyndContent> contentList = new ArrayList<SyndContent>();
			SyndContent content = new SyndContentImpl();
			content.setType("encoded");
			content.setValue(item.getContent());
			contentList.add(content);
			se.setContents(contentList);
			// カテゴリ
			List<SyndCategory> categoryList = item.getCategoryList();
			if (categoryList != null) {
				se.setCategories(categoryList);
			}
			// dc:subject
			if (item.getDcSubjectList() != null) {
				Module module = se.getModule(DCModule.URI);
				if (module != null) {
					DCModule dcModule = (DCModule) module;
					dcModule.setSubjects(item.getDcSubjectList());
				}
			}

			// TODO 後日実装を行うため現状コメントアウトを行う. 20100118 by yousuke.yazawa_bp
			/*
			 * Podcast対応RSSタイプ、且つPodcastコンテンツがある場合、enclosureタグの出力
			 */
			// if (rssType.isAdjustPodcast()) {
			// int count = Podcast.setTagsInItem(entry, article
			// .getDescriptionAll(), blog.getBlogId());
			// if (!isPodcast && count > 0) {
			// // Podcast対応RSSであるフラグをセット
			// isPodcast = true;
			// }
			// }

			// リストに追加
			seList.add(se);
		}

		// TODO 後日実装を行うため現状コメントアウトを行う. 20100118 by yousuke.yazawa_bp
		/*
		 * Podcast対応フィードの場合、itunesタグの出力
		 */
		// if (isPodcast) {
		// Podcast.setTagsInChannel(syndFeed, blog);
		// }

		// フィードデータにアイテムリストをセット
		sf.setEntries(seList);

		/*
		 * フィード出力
		 */
		SyndFeedOutput output = new SyndFeedOutput();
		return output.outputString(sf);
	}

	/**
	 * 不正文字が含まれていた場合にログ出力する.
	 *
	 * @param entity
	 *            {@link Feed} もしくは {@link Item} オブジェクト
	 */
	private void outputInvalidCharLog(BaseEntity entity) {
		List<InvalidChar> invalidCharList = entity.getInvalidCharList();
		if (invalidCharList == null) {
			return;
		}
		for (InvalidChar invalidChar : invalidCharList) {
			StringBuilder msg = new StringBuilder();
			msg.append(entity.getLink());
			msg.append(" の ");
			msg.append(entity.getClass().getSimpleName());
			msg.append("#");
			msg.append(invalidChar.getVariableName());
			msg.append(" に不正な文字がありましたので除外しました:");
			for (char c : invalidChar.getCharList()) {
				msg.append("[\\u");
				String cText = Integer.toHexString(c);
				for (int i = 0; i < 4 - cText.length(); i++) {
					// 4文字になるように頭0埋め
					msg.append("0");
				}
				msg.append(cText);
				msg.append("]");
			}
			logger.warnLog("A026", msg.toString());
		}
	}
}
