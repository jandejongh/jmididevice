/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.javajdj.jservice.midi.device.rolandboss.bossme80.swing;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.javajdj.jservice.midi.device.rolandboss.bossme80.MidiDevice_Me80;
import org.javajdj.jservice.midi.device.swing.JMidiDeviceBooleanParameter;
import org.javajdj.jservice.midi.device.swing.JMidiDeviceEnumParameter;
import org.javajdj.jservice.midi.device.swing.JMidiDeviceIntParameter_Slider;
import org.javajdj.jservice.midi.device.MidiDevice;

/**
 *
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}.
 * 
 */
public class JMe80Panel_AMP
  extends JPanel
{
  
  public JMe80Panel_AMP (final MidiDevice midiDevice)
  {
    super ();
    setLayout (new GridLayout (8, 1, 5, 0));
    add (new JMidiDeviceBooleanParameter (midiDevice,
      "Active", MidiDevice_Me80.TP_AMP_SW_NAME));
    add (new JMidiDeviceEnumParameter (midiDevice,
      "Type", MidiDevice_Me80.TP_AMP_TYPE_NAME, MidiDevice_Me80.AmpEffectType.class));
    add (new JLabel ());
    add (new JMidiDeviceIntParameter_Slider (midiDevice,
      "Gain", MidiDevice_Me80.TP_AMP_1_NAME, 0, 99));
    add (new JMidiDeviceIntParameter_Slider (midiDevice,
      "Bass", MidiDevice_Me80.TP_AMP_2_NAME, 0, 99));
    add (new JMidiDeviceIntParameter_Slider (midiDevice,
      "Middle", MidiDevice_Me80.TP_AMP_3_NAME, 0, 99));
    add (new JMidiDeviceIntParameter_Slider (midiDevice,
      "Treble", MidiDevice_Me80.TP_AMP_4_NAME, 0, 99));
    add (new JMidiDeviceIntParameter_Slider (midiDevice,
      "Level", MidiDevice_Me80.TP_AMP_5_NAME, 0, 99));
  }
  
}
