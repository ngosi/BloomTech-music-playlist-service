package com.amazon.ata.music.playlist.service.activity;

import com.amazon.ata.music.playlist.service.dynamodb.PlaylistDao;
import com.amazon.ata.music.playlist.service.dynamodb.models.Playlist;
import com.amazon.ata.music.playlist.service.exceptions.InvalidAttributeValueException;
import com.amazon.ata.music.playlist.service.exceptions.PlaylistNotFoundException;
import com.amazon.ata.music.playlist.service.models.requests.CreatePlaylistRequest;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PlaylistDaoTest {
    @Mock
    DynamoDBMapper dynamoDBMapper;

    private PlaylistDao playlistDao;

    @BeforeEach
    public void setUp() {
        initMocks(this);
        playlistDao = new PlaylistDao(dynamoDBMapper);
    }

    @Test
    public void getPlaylist_validId_returnsCorrectPlaylist() {
        String expectedId = "expectedId";
        String expectedName = "expectedName";
        String expectedCustomerId = "expectedCustomerId";
        int expectedSongCount = 0;
        List<String> expectedTags = Lists.newArrayList("tag");

        Playlist playlist = new Playlist();
        playlist.setId(expectedId);
        playlist.setName(expectedName);
        playlist.setCustomerId(expectedCustomerId);
        playlist.setSongCount(expectedSongCount);
        playlist.setTags(Sets.newHashSet(expectedTags));

        when(dynamoDBMapper.load(Playlist.class, expectedId)).thenReturn(playlist);

        assertEquals(playlistDao.getPlaylist(expectedId), playlist);
    }

    @Test
    public void getPlaylist_invalidId_throwsPlaylistNotFoundException() {
        String invalidId = "invalidId";

        when(dynamoDBMapper.load(Playlist.class, invalidId)).thenReturn(null);

        assertThrows(PlaylistNotFoundException.class, () -> playlistDao.getPlaylist(invalidId));
    }

    @Test
    public void savePlaylist_validRequest_savesPlaylist() {
        String expectedName = "expectedName";
        String expectedCustomerId = "expectedCustomerId";
        int expectedSongCount = 0;
        List<String> expectedTags = Lists.newArrayList("tag");

        CreatePlaylistRequest createPlaylistRequest = CreatePlaylistRequest.builder()
                .withName(expectedName)
                .withCustomerId(expectedCustomerId)
                .withTags(expectedTags)
                .build();

        Playlist result = playlistDao.savePlaylist(createPlaylistRequest);

        assertTrue(result.getId().matches("[A-Za-z0-9]{5}"));
        assertEquals(expectedName, result.getName());
        assertEquals(expectedCustomerId, result.getCustomerId());
        assertEquals(expectedSongCount, result.getSongCount());
        assertEquals(expectedTags, new ArrayList<>(result.getTags()));
    }

    @Test
    public void savePlaylist_zeroTags_playlistTagsEqualNull() {
        String expectedName = "expectedName";
        String expectedCustomerId = "expectedCustomerId";
        int expectedSongCount = 0;
        List<String> emptyTags = new ArrayList<>();

        CreatePlaylistRequest createPlaylistRequest = CreatePlaylistRequest.builder()
                .withName(expectedName)
                .withCustomerId(expectedCustomerId)
                .withTags(emptyTags)
                .build();

        Playlist result = playlistDao.savePlaylist(createPlaylistRequest);

        assertTrue(result.getId().matches("[A-Za-z0-9]{5}"));
        assertEquals(expectedName, result.getName());
        assertEquals(expectedCustomerId, result.getCustomerId());
        assertEquals(expectedSongCount, result.getSongCount());
        assertNull(result.getTags());
    }

    @Test
    public void savePlaylist_invalidRequest_throwsInvalidAttributeValueException() {
        String invalidName = "\\invalidName";
        String invalidCustomerId = "\\invalidCustomerId";
        List<String> emptyTags = new ArrayList<>();

        CreatePlaylistRequest createPlaylistRequest = CreatePlaylistRequest.builder()
                .withName(invalidName)
                .withCustomerId(invalidCustomerId)
                .withTags(emptyTags)
                .build();

        assertThrows(InvalidAttributeValueException.class, () -> playlistDao.savePlaylist(createPlaylistRequest));
    }
}
