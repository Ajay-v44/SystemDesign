package Projects.MusicPlayer.devices;

import Projects.MusicPlayer.models.Song;

public interface IAudioOutputDevice {
    void playAudio(Song song);
}
