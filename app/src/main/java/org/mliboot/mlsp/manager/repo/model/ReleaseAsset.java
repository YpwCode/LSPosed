

package org.mliboot.mlsp.manager.repo.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReleaseAsset {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("contentType")
    @Expose
    private String contentType;
    @SerializedName("downloadUrl")
    @Expose
    private String downloadUrl;
    @SerializedName("downloadCount")
    @Expose
    private int downloadCount = 0;
    @SerializedName("size")
    @Expose
    private int size = 0;

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Nullable
    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
