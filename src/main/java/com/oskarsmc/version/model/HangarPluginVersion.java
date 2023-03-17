package com.oskarsmc.version.model;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Objects;

/**
 * {@link HangarPluginVersion} represents a version for a Hangar plugin.
 */
public final class HangarPluginVersion {
    private final String name;
    @SerializedName("createdAt")
    private final Date created;
    private final String description;

    public HangarPluginVersion(String name, Date created, String description) {
        this.name = name;
        this.created = created;
        this.description = description;
    }

    /**
     * Get name of the version
     *
     * @return name of the version
     */
    public String name() {
        return name;
    }

    /**
     * Get date of the version's publication
     *
     * @return date of the version's publication
     */
    public Date created() {
        return created;
    }


    /**
     * Get changelog/description of version.
     * Likely in Markdown format - take note
     *
     * @return changelog of version
     */
    public String description() {
        return description;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (HangarPluginVersion) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.created, that.created) &&
                Objects.equals(this.description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, created, description);
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "HangarPluginVersion[" +
                "name=" + name + ", " +
                "created=" + created + ", " +
                "description=" + description + ']';
    }


}
