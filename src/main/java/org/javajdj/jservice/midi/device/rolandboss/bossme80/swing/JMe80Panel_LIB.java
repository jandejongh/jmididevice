/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.javajdj.jservice.midi.device.rolandboss.bossme80.swing;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.javajdj.jservice.midi.device.MidiDevice;

/**
 *
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}.
 * 
 */
public class JMe80Panel_LIB
  extends JPanel
{
  
  public JMe80Panel_LIB (final MidiDevice midiDevice)
  {
    super ();
    setLayout (new GridLayout (8, 1, 5, 5));
    add (new JLabel ());
    add (new JLabel ());
    add (new JLabel ());
    add (new JLabel ("     File I/O and Patch Operations"));
    add (new JLabel ("     are planned for Release 2"));
    add (new JLabel ());
    add (new JLabel ());
    add (new JLabel ());
  }
  
}
