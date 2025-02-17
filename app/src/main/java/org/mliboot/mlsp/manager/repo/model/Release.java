

package org.mliboot.mlsp.manager.repo.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Release {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("descriptionHTML")
    @Expose
    private String descriptionHTML;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("publishedAt")
    @Expose
    private String publishedAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("tagName")
    @Expose
    private String tagName;
    @SerializedName("isPrerelease")
    @Expose
    private Boolean isPrerelease;
    @SerializedName("releaseAssets")
    @Expose
    private List<ReleaseAsset> releaseAssets = new ArrayList<>();

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Nullable
    public String getDescriptionHTML() {
        return descriptionHTML;
    }

    public void setDescriptionHTML(String descriptionHTML) {
        this.descriptionHTML = descriptionHTML;
    }

    @Nullable
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Nullable
    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    @Nullable
    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Nullable
    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    @Nullable
    public Boolean getIsPrerelease() {
        return isPrerelease;
    }

    public void setIsPrerelease(Boolean isPrerelease) {
        this.isPrerelease = isPrerelease;
    }

    @Nullable
    public List<ReleaseAsset> getReleaseAssets() {
        return releaseAssets;
    }

    public void setReleaseAssets(List<ReleaseAsset> releaseAssets) {
        this.releaseAssets = releaseAssets;
    }
}
