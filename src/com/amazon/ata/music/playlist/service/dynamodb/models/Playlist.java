package com.amazon.ata.music.playlist.service.dynamodb.models;

import com.amazon.ata.music.playlist.service.converters.AlbumTrackLinkedListConverter;

import com.amazon.ata.music.playlist.service.exceptions.InvalidAttributeValueException;
import com.amazon.ata.music.playlist.service.util.MusicPlaylistServiceUtils;
import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a record in the playlists table.
 */
@DynamoDBTable(tableName = "playlists")
public class Playlist {
    private String id;
    private String name;
    private String customerId;
    private int songCount;
    private Set<String> tags;
    private List<AlbumTrack> songList;

    @DynamoDBHashKey(attributeName = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!MusicPlaylistServiceUtils.isValidString(name)) {
            throw new InvalidAttributeValueException("Invalid name provided. Must not contain \", ', or \\.");
        }

        this.name = name;
    }

    @DynamoDBAttribute(attributeName = "customerId")
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        if (!MusicPlaylistServiceUtils.isValidString(customerId)) {
            throw new InvalidAttributeValueException("Invalid customerId provided. Must not contain \", ', or \\.");
        }

        this.customerId = customerId;
    }

    @DynamoDBAttribute(attributeName = "songCount")
    public int getSongCount() {
        return songCount;
    }

    public void setSongCount(int songCount) {
        this.songCount = songCount;
    }

    @DynamoDBAttribute(attributeName = "tags")
    public Set<String> getTags() {
        if (tags == null) {
            return new HashSet<>();
        }
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    // PARTICIPANTS: You do not need to modify the songList getters/setters or annotations
//    @DynamoDBTypeConverted(converter = AlbumTrackLinkedListConverter.class)
    @DynamoDBAttribute(attributeName = "songList")
    public List<AlbumTrack> getSongList() {
        return songList;
    }

    public void setSongList(List<AlbumTrack> songList) {
        this.songList = songList;
    }
}
