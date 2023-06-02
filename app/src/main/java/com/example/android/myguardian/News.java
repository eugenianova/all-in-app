package com.example.android.myguardian;

/**
 * Объект {@link News} содержит информацию относящуюся к конкретной новости.
 */

public class News {

    /** Title of the article */
    private String mTitle;

    /** Section name of the article*/
    private String mSection;

    /** Author name in the article */
    private String mAuthor;

    /** Web publication date of the article */
    private String mDate;

    /** Website URL of the article */
    private String mUrl;

    /** Thumbnail of the article */
    private String mThumbnail;

    /** TrailText of the article with string type Html */
    private String mTrailTextHtml;

    /**
     * Constructs a new {@link News} object.
     *
     * @param title is the title of the article
     * @param section is the section name of the article
     * @param author is author name in article
     * @param date is the web publication date of the article
     * @param url is the website URL to find more details about the article
     * @param thumbnail is the thumbnail of the article
     * @param trailText is trail text of the article with string type Html
     */
    public News(String title, String section, String author, String date, String url, String thumbnail, String trailText) {
        mTitle = title;
        mSection = section;
        mAuthor = author;
        mDate = date;
        mUrl = url;
        mThumbnail = thumbnail;
        mTrailTextHtml = trailText;
    }

    /**
     * Врзвращаем title статьи
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Возвращает раздел статьи.
     */
    public String getSection() {
        return mSection;
    }

    /**
     * Возвращает имя автора статьи
     */
    public String getAuthor() {
        return mAuthor;
    }
    /**
     * Возвращает дату публикации статьи
     */
    public String getDate() {
        return mDate;
    }

    /**
     * Возвращает ссылку URL статьи
     */
    public String getUrl() {
        return mUrl;
    }

    /**
     *Ввозвращает короткое описание статьи
     */
    public String getThumbnail() {
        return mThumbnail;
    }

    /**
     * Returns the TrailText of the article with string type Html
     */
    public String getTrailTextHtml() {
        return mTrailTextHtml;
    }
}
