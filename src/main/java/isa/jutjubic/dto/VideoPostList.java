package isa.jutjubic.dto;

public class VideoPostList {

    public String title;
    public String thumbnail;

    VideoPostList() {}

    VideoPostList(String title, String thumbnail)
    {
        this.title = title;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
