package com.amazon.ata.music.playlist.service.converters;

import com.amazon.ata.music.playlist.service.dynamodb.models.AlbumTrack;
import com.amazon.ata.music.playlist.service.dynamodb.models.Playlist;
import com.amazon.ata.music.playlist.service.models.PlaylistModel;
import com.amazon.ata.music.playlist.service.models.SongModel;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ModelConverterTest {
    @Test
    public void toPlaylistModel_validPlaylist_returnsCorrectPlaylistModel() {
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

        PlaylistModel output = ModelConverter.toPlaylistModel(playlist);

        assertEquals(output.getId(), expectedId);
        assertEquals(output.getName(), expectedName);
        assertEquals(output.getCustomerId(), expectedCustomerId);
        assertEquals(output.getSongCount(), expectedSongCount);
        assertEquals(output.getTags(), expectedTags);
    }

    @Test
    public void toSongModel_validAlbumTrack_returnsCorrectAlbumTrackModel() {
        String expectedAsin = "expectedAsin";
        Integer expectedTrackNumber = 1;
        String expectedName = "expectedName";
        String expectedTitle = "exptectedTitle";

        AlbumTrack albumTrack = new AlbumTrack();
        albumTrack.setAsin(expectedAsin);
        albumTrack.setTrackNumber(expectedTrackNumber);
        albumTrack.setAlbumName(expectedName);
        albumTrack.setSongTitle(expectedTitle);

        SongModel output = ModelConverter.toSongModel(albumTrack);

        assertEquals(output.getAsin(), albumTrack.getAsin());
        assertEquals(output.getTrackNumber(), albumTrack.getTrackNumber());
        assertEquals(output.getAlbum(), albumTrack.getAlbumName());
        assertEquals(output.getTitle(), albumTrack.getSongTitle());
    }
}
