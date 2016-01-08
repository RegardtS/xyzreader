package com.example.xyzreader;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by regardtschindler on 2016/01/08.
 */
public class Article implements Parcelable {

    String id;
    String title;
    String published_date;
    String author;
    String thumb_url;
    String photo_url;
    String aspect_ratio;
    String body_text;

    public Article(String id,String title,String published_date, String author,String thumb_url,String photo_url,String aspect_ratio,String body_text) {
        this.id = id;
        this.title = title;
        this.published_date = published_date;
        this.author = author;
        this.thumb_url = thumb_url;
        this.photo_url = photo_url;
        this.aspect_ratio = aspect_ratio;
        this.body_text = body_text;
    }

    protected Article(Parcel in) {
        id = in.readString();
        title = in.readString();
        published_date = in.readString();
        author = in.readString();
        thumb_url = in.readString();
        photo_url = in.readString();
        aspect_ratio = in.readString();
        body_text = in.readString();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPublished_date() {
        return published_date;
    }

    public String getAuthor() {return author;}

    public String getThumb_url() {
        return thumb_url;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public String getAspect_ratio() {
        return aspect_ratio;
    }

    public String getBody_text() {
        return body_text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(published_date);
        dest.writeString(author);
        dest.writeString(thumb_url);
        dest.writeString(photo_url);
        dest.writeString(aspect_ratio);
        dest.writeString(body_text);
    }
}
