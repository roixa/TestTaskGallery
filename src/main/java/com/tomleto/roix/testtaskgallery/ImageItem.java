package com.tomleto.roix.testtaskgallery;

/**
 * Created by bsr on 07.08.2016.
 */

//our base item model for manage data
public class ImageItem {
    private int id;
    private int count;
    private String url;
    private String comment;
    private boolean isFavorites;

    public ImageItem(int id,int count,String url,String comment,boolean isFavorites){
        this.url = url;
        this.id = id;
        this.count = count;
        this.comment=comment;
        this.isFavorites=isFavorites;

    }

    public boolean isFavored(){
        return isFavorites;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


}
