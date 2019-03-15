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
public class JMe80Panel_COMP
  extends JPanel
{
  
  public JMe80Panel_COMP (final MidiDevice midiDevice)
  {
    super ();
    setLayout (new GridLayout (6, 1, 5, 5));
    add (new JMidiDeviceBooleanParameter (midiDevice,
      "Active", MidiDevice_Me80.TP_COMP_SW_NAME));
    add (new JMidiDeviceEnumParameter (midiDevice,
      "Type", MidiDevice_Me80.TP_COMP_TYPE_NAME, MidiDevice_Me80.CompEffectType.class));
    add (new JLabel ());
    add (new JMidiDeviceIntParameter_Slider (midiDevice,
      "Sus/Sens/-1Oct/Freq/Low", MidiDevice_Me80.TP_COMP_1_NAME, 0, 99));
    add (new JMidiDeviceIntParameter_Slider (midiDevice,
      "Att/Tone/-2Oct/[D.]Lvl/High", MidiDevice_Me80.TP_COMP_2_NAME, 0, 99));
    add (new JMidiDeviceIntParameter_Slider (midiDevice,
      "Lvl/Peak/Dir/[E.]Lvl", MidiDevice_Me80.TP_COMP_3_NAME, 0, 99));
  }
  
}
