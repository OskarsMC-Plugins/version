package com.oskarsmc.version.web;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.oskarsmc.version.model.HangarPluginVersion;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

/**
 * The {@link HangarClient} object is used to make requests for information from Hangar, for example the latest version for a plugin.
 */
@SuppressWarnings("unused")
public class HangarClient {
    /**
     * PaperMC's Official Hangar instance
     */
    public static final URI HANGAR_PAPER = URI.create("https://hangar.papermc.io/");
    /**
     * PaperMC's Development Hangar instance
     */
    public static final URI HANGAR_PAPER_DEV = URI.create("https://hangar.papermc.dev/");

    private final HttpClient client = HttpClient.newBuilder().build();
    private final URI baseUri;
    private final Gson gson = new Gson();

    private HangarClient(URI baseUri) {
        this.baseUri = baseUri;
    }

    /**
     * Create a {@link HangarClient} instance initialised with an optional instance of Hangar.
     *
     * @param baseUri uri for an optional instance of Hangar
     * @return A {@link HangarClient} object
     */
    @Contract("_ -> new")
    public static @NotNull HangarClient of(URI baseUri) {
        return new HangarClient(baseUri);
    }

    /**
     * Create a {@link HangarClient} instance initialised with the PaperMC Official instance of Hangar.
     *
     * @return A {@link HangarClient} object
     */
    @Contract(" -> new")
    public static @NotNull HangarClient of() {
        return of(HANGAR_PAPER);
    }

    /**
     * Get the latest version for a Hangar Plugin
     *
     * @param author          Author of the hangar project
     * @param slug            Name of the hangar project
     * @param channel         Channel of the version - optional
     * @param platform        Platform of the version - optional
     * @param platformVersion Platform version of the platform - optional
     * @return If applicable, the latest {@link HangarPluginVersion}
     */
    public @Nullable HangarPluginVersion getLatestVersion(String author, String slug, @Nullable String channel, @Nullable String platform, @Nullable String platformVersion) {
        Objects.requireNonNull(author);
        Objects.requireNonNull(slug);

        URI uri = baseUri.resolve("/api/v1/projects/" + author + "/" + slug + "/");
        StringBuilder query = new StringBuilder("versions?limit=1&offset=0");
        if (channel != null) {
            query.append("&channel=").append(channel);
        }

        if (platform != null) {
            query.append("&platform=").append(platform);
        }

        if (platformVersion != null) {
            query.append("&platformVersion=").append(platformVersion);
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri.resolve(query.toString()))
                .header("User-Agent", getClass().getPackageName() + '/' + getClass().getPackage().getImplementationVersion())
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonElement tree = gson.toJsonTree(JsonParser.parseString(response.body()));
            JsonArray resultArray = tree.getAsJsonObject().get("result").getAsJsonArray();
            if (resultArray.size() < 1) {
                return null;
            }

            return gson.fromJson(resultArray.get(0).getAsJsonObject(), HangarPluginVersion.class);
        } catch (IOException | InterruptedException | JsonParseException e) {
            if (System.getProperty("com.oskarsmc.version.logExceptions") != null) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Get the latest version for a Hangar Plugin
     *
     * @param author   Author of the hangar project
     * @param slug     Name of the hangar project
     * @param channel  Channel of the version - optional
     * @param platform Platform of the version - optional
     * @return If applicable, the latest {@link HangarPluginVersion}
     */
    public @Nullable HangarPluginVersion getLatestVersion(String author, String slug, @Nullable String channel, @Nullable String platform) {
        return getLatestVersion(author, slug, channel, platform, null);
    }
}
