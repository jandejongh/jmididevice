/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.javajdj.jservice.midi.device.rolandboss.bossme80.swing;

import java.awt.GridLayout;
import javax.swing.JPanel;
import org.javajdj.jservice.midi.device.rolandboss.bossme80.MidiDevice_Me80;
import org.javajdj.jservice.midi.device.swing.JMidiDeviceBooleanParameter;
import org.javajdj.jservice.midi.device.swing.JMidiDeviceEnumParameter;
import org.javajdj.jservice.midi.device.MidiDevice;

/**
 *
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}.
 * 
 */
public class JMe80Panel_PEDAL_FX
  extends JPanel
{
  
  public JMe80Panel_PEDAL_FX (final MidiDevice midiDevice)
  {
    super ();
    setLayout (new GridLayout (2, 1, 5, 5));
    add (new JMidiDeviceBooleanParameter (midiDevice,
      "Active", MidiDevice_Me80.TP_PEDAL_FX_SW_NAME));
    add (new JMidiDeviceEnumParameter (midiDevice,
      "Type", MidiDevice_Me80.TP_PEDAL_FX_TYPE_NAME, MidiDevice_Me80.PedalFxType.class));
  }
  
}
