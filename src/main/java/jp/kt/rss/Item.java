package jp.kt.rss;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sun.syndication.feed.module.DCSubject;
import com.sun.syndication.feed.module.DCSubjectImpl;
import com.sun.syndication.feed.synd.SyndCategory;
import com.sun.syndication.feed.synd.SyndCategoryImpl;

/**
 * フィード内のアイテム情報.
 *
 * @author yousuke.yazawa_bp
 * @author tatsuya.kumon
 */
public class Item extends BaseEntity {
	/** 本文（HTML可） */
	private String content;

	/** 投稿者名 */
	private String author;

	/** 投稿日時 */
	private Date publishDate;

	/** カテゴリ名一覧 */
	private List<SyndCategory> categoryList;

	/** dc:subjectタグの値のリスト */
	private List<DCSubject> dcSubjectList;

	/**
	 * 本文をセット.
	 *
	 * @param content
	 *            本文.<br>
	 *            content:encodedタグに出力される内容.<br>
	 *            HTML可.
	 */
	public void setContent(String content) {
		this.content = removeInvalidChar(content, "content");
	}

	/**
	 * 投稿者名をセット.
	 *
	 * @param author
	 *            投稿者名
	 */
	public void setAuthor(String author) {
		this.author = removeInvalidChar(author, "author");
	}

	/**
	 * 投稿日時をセット.
	 *
	 * @param publishDate
	 *            投稿日時
	 */
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	/**
	 * 本文を取得する.
	 *
	 * @return content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * カテゴリ名一覧を取得する.
	 *
	 * @return カテゴリ名一覧.
	 */
	public String[] getCategories() {
		int size = categoryList == null ? 0 : categoryList.size();
		String[] categories = new String[size];
		for (int i = 0; i < size; i++) {
			categories[i] = categoryList.get(i).getName();
		}
		return categories;
	}

	/**
	 * カテゴリ一覧をSyndCategoryオブジェクトのListで返す.
	 * <p>
	 * FeedUtilクラスで使用するためのメソッドなので不可視.
	 * </p>
	 *
	 * @return categoryList
	 */
	List<SyndCategory> getCategoryList() {
		return categoryList;
	}

	/**
	 * カテゴリ名をセットする.
	 *
	 * @param category
	 *            カテゴリ名.
	 */
	public void addCategory(String category) {
		if (this.categoryList == null) {
			this.categoryList = new ArrayList<SyndCategory>();
		}
		SyndCategory syndCategory = new SyndCategoryImpl();
		syndCategory.setName(category);
		categoryList.add(syndCategory);
	}

	/**
	 * 投稿者名を取得する.
	 *
	 * @return 投稿者名.
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * 公開日時を取得する.
	 *
	 * @return 公開日時
	 */
	public Date getPublishDate() {
		return publishDate;
	}

	/**
	 * dc:subject値をセットする.
	 *
	 * @param subject
	 *            サブジェクト値
	 */
	public void addDcSubject(String subject) {
		if (this.dcSubjectList == null) {
			this.dcSubjectList = new ArrayList<DCSubject>();
		}
		DCSubject sub = new DCSubjectImpl();
		sub.setValue(subject);
		this.dcSubjectList.add(sub);
	}

	/**
	 * dc:subject値一覧をDCSubjectオブジェクトのListで返す.
	 * <p>
	 * FeedUtilクラスで使用するためのメソッドなので不可視.
	 * </p>
	 *
	 * @return サブジェクトリスト
	 */
	List<DCSubject> getDcSubjectList() {
		return this.dcSubjectList;
	}

	/**
	 * dc:subject値一覧を取得する.
	 *
	 * @return dc:subject値一覧.
	 */
	public String[] getDcSubjects() {
		int size = dcSubjectList == null ? 0 : dcSubjectList.size();
		String[] dcSubjects = new String[size];
		for (int i = 0; i < size; i++) {
			dcSubjects[i] = dcSubjectList.get(i).getValue();
		}
		return dcSubjects;
	}
}
