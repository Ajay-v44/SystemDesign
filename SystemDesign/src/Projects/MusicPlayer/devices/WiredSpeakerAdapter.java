package Projects.MusicPlayer.devices;

import Projects.MusicPlayer.external.WiredSpeakerAPI;
import Projects.MusicPlayer.models.Song;

public class WiredSpeakerAdapter implements  IAudioOutputDevice{
    private WiredSpeakerAPI wiredApi;

    public WiredSpeakerAdapter(WiredSpeakerAPI api) {
        this.wiredApi = api;
    }

    @Override
    public void playAudio(Song song) {
        String payload = song.getTitle() + " by " + song.getArtist();
        wiredApi.playSoundViaCable(payload);
    }
}
