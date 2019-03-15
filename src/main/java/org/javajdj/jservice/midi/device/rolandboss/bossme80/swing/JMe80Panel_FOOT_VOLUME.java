/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.javajdj.jservice.midi.device.rolandboss.bossme80.swing;

import java.awt.GridLayout;
import javax.swing.JPanel;
import org.javajdj.jservice.midi.device.rolandboss.bossme80.MidiDevice_Me80;
import org.javajdj.jservice.midi.device.swing.JMidiDeviceIntParameter_Slider;
import org.javajdj.jservice.midi.device.MidiDevice;

/**
 *
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}.
 * 
 */
public class JMe80Panel_FOOT_VOLUME
  extends JPanel
{
  
  public JMe80Panel_FOOT_VOLUME (final MidiDevice midiDevice)
  {
    super ();
    setLayout (new GridLayout (1, 1, 5, 5));
    add (new JMidiDeviceIntParameter_Slider (midiDevice, "Vol/Exp", MidiDevice_Me80.FOOT_VOLUME_NAME, 0, 127));
  }
  
}
