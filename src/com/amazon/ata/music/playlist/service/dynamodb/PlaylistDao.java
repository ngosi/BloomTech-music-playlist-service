package com.amazon.ata.music.playlist.service.dynamodb;

import com.amazon.ata.music.playlist.service.dynamodb.models.Playlist;
import com.amazon.ata.music.playlist.service.exceptions.PlaylistNotFoundException;

import com.amazon.ata.music.playlist.service.models.requests.CreatePlaylistRequest;
import com.amazon.ata.music.playlist.service.util.MusicPlaylistServiceUtils;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Accesses data for a playlist using {@link Playlist} to represent the model in DynamoDB.
 */
@Singleton
public class PlaylistDao {
    private final DynamoDBMapper dynamoDbMapper;

    /**
     * Instantiates a PlaylistDao object.
     *
     * @param dynamoDbMapper the {@link DynamoDBMapper} used to interact with the playlists table
     */
    @Inject
    public PlaylistDao(DynamoDBMapper dynamoDbMapper) {
        this.dynamoDbMapper = dynamoDbMapper;
    }

    /**
     * Returns the {@link Playlist} corresponding to the specified id.
     *
     * @param id the Playlist ID
     * @return the stored Playlist, or null if none was found.
     */
    public Playlist getPlaylist(String id) {
        Playlist playlist = this.dynamoDbMapper.load(Playlist.class, id);
        if (playlist == null) {
            throw new PlaylistNotFoundException("Could not find playlist with id " + id);
        }

        return playlist;
    }

    public Playlist savePlaylist(CreatePlaylistRequest createPlaylistRequest) {
        Playlist playlist = new Playlist();
        playlist.setId(MusicPlaylistServiceUtils.generatePlaylistId());
        playlist.setName(createPlaylistRequest.getName());
        playlist.setCustomerId(createPlaylistRequest.getCustomerId());
        playlist.setSongCount(0);
        playlist.setTags((createPlaylistRequest.getTags().size() == 0) ? null : new HashSet<>(createPlaylistRequest.getTags()));
        playlist.setSongList(new ArrayList<>());

        try {
            dynamoDbMapper.save(playlist);
        } catch (Exception e){
            System.out.println(e);
        }

        return playlist;
    }

    public Playlist savePlaylist(Playlist playlist) {
        try {
            dynamoDbMapper.save(playlist);
        } catch (Exception e){
            System.out.println(e);
        }
        return playlist;
    }
}
