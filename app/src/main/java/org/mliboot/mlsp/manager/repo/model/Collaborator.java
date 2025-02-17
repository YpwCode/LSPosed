

package org.mliboot.mlsp.manager.repo.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Collaborator {

    @SerializedName("login")
    @Expose
    private String login;
    @SerializedName("name")
    @Expose
    private String name;

    @Nullable
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
