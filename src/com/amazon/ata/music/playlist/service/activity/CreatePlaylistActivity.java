package com.amazon.ata.music.playlist.service.activity;

//import com.amazon.ata.aws.dynamodb.DynamoDbClientProvider;
import com.amazon.ata.music.playlist.service.converters.ModelConverter;
import com.amazon.ata.music.playlist.service.dynamodb.models.Playlist;
import com.amazon.ata.music.playlist.service.exceptions.InvalidAttributeValueException;
import com.amazon.ata.music.playlist.service.models.requests.CreatePlaylistRequest;
import com.amazon.ata.music.playlist.service.models.results.CreatePlaylistResult;
import com.amazon.ata.music.playlist.service.models.PlaylistModel;
import com.amazon.ata.music.playlist.service.dynamodb.PlaylistDao;

//import com.amazonaws.regions.Regions;
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazon.ata.music.playlist.service.util.MusicPlaylistServiceUtils;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of the CreatePlaylistActivity for the MusicPlaylistService's CreatePlaylist API.
 *
 * This API allows the customer to create a new playlist with no songs.
 */
@Singleton
public class CreatePlaylistActivity implements RequestHandler<CreatePlaylistRequest, CreatePlaylistResult> {
    private final Logger log = LogManager.getLogger();
    private final PlaylistDao playlistDao;

//    public CreatePlaylistActivity() {
//        this.playlistDao = new PlaylistDao(new DynamoDBMapper(DynamoDbClientProvider.getDynamoDBClient(Regions.US_WEST_2)));
//    }

    /**
     * Instantiates a new CreatePlaylistActivity object.
     *
     * @param playlistDao PlaylistDao to access the playlists table.
     */
    @Inject
    public CreatePlaylistActivity(PlaylistDao playlistDao) {
        this.playlistDao = playlistDao;
    }

    /**
     * This method handles the incoming request by persisting a new playlist
     * with the provided playlist name and customer ID from the request.
     * <p>
     * It then returns the newly created playlist.
     * <p>
     * If the provided playlist name or customer ID has invalid characters, throws an
     * InvalidAttributeValueException
     *
     * @param createPlaylistRequest request object containing the playlist name and customer ID
     *                              associated with it
     * @return createPlaylistResult result object containing the API defined {@link PlaylistModel}
     */
    @Override
    public CreatePlaylistResult handleRequest(final CreatePlaylistRequest createPlaylistRequest, Context context)  {
        log.info("Received CreatePlaylistRequest {}", createPlaylistRequest);

        String name = createPlaylistRequest.getName();
        String customerId = createPlaylistRequest.getCustomerId();

        if (!MusicPlaylistServiceUtils.isValidString(name) || !MusicPlaylistServiceUtils.isValidString(customerId)) {
            throw new InvalidAttributeValueException("Request includes invalid characters.");
        }

        String id = MusicPlaylistServiceUtils.generatePlaylistId();
        List<String> tags = createPlaylistRequest.getTags();

        Playlist playlist = new Playlist();
        playlist.setId(id);
        playlist.setName(name);
        playlist.setCustomerId(customerId);
        playlist.setSongCount(0);
        playlist.setSongList(new LinkedList<>());
        playlist.setTags((tags == null) ? new HashSet<>() : new HashSet<>(tags));

        playlistDao.savePlaylist(playlist);

        return CreatePlaylistResult.builder()
                .withPlaylist(ModelConverter.toPlaylistModel(playlist))
                .build();
    }
}
