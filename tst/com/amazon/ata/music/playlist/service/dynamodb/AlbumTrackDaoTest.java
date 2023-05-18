package com.amazon.ata.music.playlist.service.dynamodb;

import com.amazon.ata.music.playlist.service.dynamodb.AlbumTrackDao;
import com.amazon.ata.music.playlist.service.dynamodb.models.AlbumTrack;
import com.amazon.ata.music.playlist.service.exceptions.AlbumTrackNotFoundException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AlbumTrackDaoTest {
    @Mock
    DynamoDBMapper dynamoDBMapper;

    private AlbumTrackDao albumTrackDao;

    @BeforeEach
    public void setUp() {
        initMocks(this);
        albumTrackDao = new AlbumTrackDao(dynamoDBMapper);
    }

    @Test
    public void getAlbumTrack_validAsin_returnsCorrectAlbumTrack() {
        String expectedAsin = "expectedAsin";
        Integer expectedTrackNumber = 1;
        String expectedName = "expectedName";
        String expectedTitle = "exptectedTitle";

        AlbumTrack albumTrack = new AlbumTrack();
        albumTrack.setAsin(expectedAsin);
        albumTrack.setTrackNumber(expectedTrackNumber);
        albumTrack.setAlbumName(expectedName);
        albumTrack.setSongTitle(expectedTitle);

        when(dynamoDBMapper.load(AlbumTrack.class, expectedAsin)).thenReturn(albumTrack);

        assertEquals(albumTrackDao.getAlbumTrack(expectedAsin, expectedTrackNumber), albumTrack);
    }

    @Test
    public void getAlbumTrack_invalidAsin_throwsAlbumTrackNotFoundException() {
        String invalidAsin = "invalidAsin";
        Integer invalidTrackNumber = 0;

        when(dynamoDBMapper.load(AlbumTrack.class, invalidAsin)).thenReturn(null);

        assertThrows(AlbumTrackNotFoundException.class, () -> albumTrackDao.getAlbumTrack(invalidAsin, invalidTrackNumber));
    }
}
