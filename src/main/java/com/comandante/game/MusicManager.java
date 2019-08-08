package com.comandante.game;

import com.google.common.io.Resources;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import java.io.*;
import java.net.URL;

public class MusicManager {


    private final Sequencer sequencer;

    public MusicManager(Sequencer sequencer) {
        this.sequencer = sequencer;
    }

    public void loadMusis() throws MidiUnavailableException, IOException, InvalidMidiDataException {

        // Obtains the default Sequencer connected to a default device.
//        Sequencer sequencer = MidiSystem.getSequencer();

        // Opens the device, indicating that it should now acquire any
        // system resources it requires and become operational.
        sequencer.open();

        InputStream is = new ByteArrayInputStream(Resources.toByteArray(Resources.getResource("midi/NowOrNever.mid")));

        // Sets the current sequence on which the sequencer operates.
        // The stream must point to MIDI file data.
        sequencer.setSequence(is);

        // Starts playback of the MIDI data in the currently loaded sequence.
    }

    public void pauseMusic() {
        sequencer.stop();
    }

    public void playMusic() {
        sequencer.start();
    }
}
