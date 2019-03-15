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
public class JMe80Panel_EQ_FX2
  extends JPanel
{
  
  public JMe80Panel_EQ_FX2 (final MidiDevice midiDevice)
  {
    super ();
    setLayout (new GridLayout (7, 1, 5, 5));
    add (new JMidiDeviceBooleanParameter (midiDevice,
      "Active", MidiDevice_Me80.TP_EQ_FX2_SW_NAME));
    add (new JMidiDeviceEnumParameter (midiDevice,
      "Type", MidiDevice_Me80.TP_EQ_FX2_TYPE_NAME, MidiDevice_Me80.EqFx2Type.class));
    add (new JLabel ());
    add (new JMidiDeviceIntParameter_Slider (midiDevice,
      "Bass", MidiDevice_Me80.TP_EQ_FX2_1_NAME, 0, 99));
    add (new JMidiDeviceIntParameter_Slider (midiDevice,
      "Rate/Drive/Time/Middle", MidiDevice_Me80.TP_EQ_FX2_2_NAME, 0, 99));
    add (new JMidiDeviceIntParameter_Slider (midiDevice,
      "Depth/Tone/FB/Treble", MidiDevice_Me80.TP_EQ_FX2_3_NAME, 0, 99));
    add (new JMidiDeviceIntParameter_Slider (midiDevice,
      "Level", MidiDevice_Me80.TP_EQ_FX2_4_NAME, 0, 99));
  }
  
}
