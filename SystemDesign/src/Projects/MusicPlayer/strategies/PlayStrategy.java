package Projects.MusicPlayer.strategies;

import Projects.MusicPlayer.models.Playlist;
import Projects.MusicPlayer.models.Song;

public interface PlayStrategy {
    void setPlaylist(Playlist playlist);

    Song next();

    boolean hasNext();

    Song previous();

    boolean hasPrevious();

    default void addToNext(Song song) {
    }
}
