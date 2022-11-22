package com.bjpn.workbench.bean;

import com.github.pagehelper.PageInfo;

import java.util.Objects;

public class LikeActivity {
    private PageInfo<Activity> pageInfoLike;
    private String likeActivityName;
    private String likeActivityOwner;
    private String likeStartDate;
    private String likeEndDate;

    public PageInfo<Activity> getPageInfoLike() {
        return pageInfoLike;
    }

    public void setPageInfoLike(PageInfo<Activity> pageInfoLike) {
        this.pageInfoLike = pageInfoLike;
    }

    public String getLikeActivityName() {
        return likeActivityName;
    }

    public void setLikeActivityName(String likeActivityName) {
        this.likeActivityName = likeActivityName;
    }

    public String getLikeActivityOwner() {
        return likeActivityOwner;
    }

    public void setLikeActivityOwner(String likeActivityOwner) {
        this.likeActivityOwner = likeActivityOwner;
    }

    public String getLikeStartDate() {
        return likeStartDate;
    }

    public void setLikeStartDate(String likeStartDate) {
        this.likeStartDate = likeStartDate;
    }

    public String getLikeEndDate() {
        return likeEndDate;
    }

    public void setLikeEndDate(String likeEndDate) {
        this.likeEndDate = likeEndDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LikeActivity that = (LikeActivity) o;
        return Objects.equals(pageInfoLike, that.pageInfoLike) &&
                Objects.equals(likeActivityName, that.likeActivityName) &&
                Objects.equals(likeActivityOwner, that.likeActivityOwner) &&
                Objects.equals(likeStartDate, that.likeStartDate) &&
                Objects.equals(likeEndDate, that.likeEndDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageInfoLike, likeActivityName, likeActivityOwner, likeStartDate, likeEndDate);
    }
}
