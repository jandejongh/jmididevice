/* 
 * Copyright 2019 Jan de Jongh <jfcmdejongh@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.javajdj.jservice.midi.device.swing;

import org.javajdj.jservice.midi.device.alesis.qvgt.swing.*;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import org.javajdj.jservice.midi.MidiService;
import org.javajdj.jservice.midi.device.MidiDevice;
import org.javajdj.jservice.midi.device.alesis.qvgt.MidiDevice_QVGT;
import org.javajdj.jservice.midi.device.rolandboss.bossme80.MidiDevice_Me80;
import org.javajdj.jservice.midi.device.rolandboss.bossme80.swing.JMe80Panel;
import org.javajdj.jservice.midi.swing.JMidiService;
import org.javajdj.swing.SwingUtilsJdJ;

/** A {@link JFrame} for MIDI instruments.
 * 
 * <p>
 * Version showing the known MIDI Devices.
 * 
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 * @see MidiDevice
 * @see MidiDevice_Me80
 * @see MidiDevice_QVGT
 * 
 */
public class JMidiDevice
  extends JFrame
{
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // LOG
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private static final Logger LOG = Logger.getLogger (JMidiDevice.class.getName ());

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // RELEASE STRING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public final static String RELEASE_STRING = "0.3.0";

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTOR(S) / CLONING / FACTORY
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public JMidiDevice ()
  {
    super ();
    setTitle ("JMidiDevice - Release " + RELEASE_STRING + " - (C) Jan de Jongh 2019");
    setMinimumSize (new Dimension (600, 300));
    setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
    //
    final JMenuBar jMenuBar = new JMenuBar ();
    //
    final JMenu fileMenu = new JMenu ("File");
    final JMenuItem exitMenuItem = new JMenuItem ("Exit");
    exitMenuItem.addActionListener ((ActionEvent) -> { System.exit (0); });
    fileMenu.add (exitMenuItem);
    jMenuBar.add (fileMenu);
    //
    final JMenu deviceMenu = new JMenu ("Device");
    final JMenu deviceNewMenu = new JMenu ("New");
    final JMenuItem deviceNewMe80MenuItem = new JMenuItem ("BOSS ME-80");
    deviceNewMe80MenuItem.addActionListener ((ActionEvent) ->
    {
      SwingUtilsJdJ.invokeOnSwingEDT (() ->
      {
        final MidiService midiService = new JMidiService ();
        final MidiDevice_Me80 midiDevice_Me80 = new MidiDevice_Me80 (midiService);
        final JMe80Panel jMe80 = new JMe80Panel (midiService, midiDevice_Me80);
        JMidiDevice.this.tabbedPane.add ("Instrument: BOSS ME-80", jMe80);
        LOG.log (Level.INFO, "Created MIDI Device {0}.", jMe80.getName ());    
      });
    });
    deviceNewMenu.add (deviceNewMe80MenuItem);
    final JMenuItem deviceNewQVGTMenuItem = new JMenuItem ("Alesis Quadraverb GT");
    deviceNewQVGTMenuItem.addActionListener ((ActionEvent) ->
    {
      SwingUtilsJdJ.invokeOnSwingEDT (() ->
      {
        final MidiService midiService = new JMidiService ();
        final MidiDevice_QVGT midiDevice_QVGT = new MidiDevice_QVGT (midiService);
        final JQVGTPanel jQVGT = new JQVGTPanel (midiService, midiDevice_QVGT);
        JMidiDevice.this.tabbedPane.add ("Instrument: Alesis QVGT", jQVGT);
        LOG.log (Level.INFO, "Created MIDI Device {0}.", jQVGT.getName ());    
      });
    });
    deviceNewMenu.add (deviceNewQVGTMenuItem);
    deviceMenu.add (deviceNewMenu);
    jMenuBar.add (deviceMenu);
    //
    final JMenu helpMenu = new JMenu ("Help");
    final JMenuItem helpAboutItem = new JMenuItem ("About");
    helpAboutItem.addActionListener ((ActionEvent) ->
    {
      JOptionPane.showMessageDialog (JMidiDevice.this,
        "JMidiDevice was designed and realized by \n\n" +
        "Jan de Jongh\n\n"
        + "Copyright (c) 2019 Jan de Jongh");
    });
    helpMenu.add (helpAboutItem);
    jMenuBar.add (helpMenu);
    setJMenuBar (jMenuBar);
    this.tabbedPane = new JTabbedPane ();
    setContentPane (this.tabbedPane);
    pack ();
    setLocationRelativeTo (null);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MAIN
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public final static void main (final String[] args)
  {
    SwingUtilities.invokeLater (() ->
    {
      try
      {
        final JMidiDevice frame = new JMidiDevice ();
        frame.setExtendedState (JFrame.MAXIMIZED_BOTH);
        frame.setVisible (true);
      }
      catch (HeadlessException he)
      {
        LOG.log (Level.WARNING, "No GUI; bailing out!");
      }
    });
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // TABBED PANE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final JTabbedPane tabbedPane;
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // LISTENERS
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final Set<PropertyChangeListener> listeners = new LinkedHashSet<> ();
  
  public final synchronized void addListener (final PropertyChangeListener l)
  {
    if (l != null)
      this.listeners.add (l);
  }
  
  public final synchronized void removeListener (final PropertyChangeListener l)
  {
      this.listeners.remove (l);
  }
  
  protected final synchronized void fire (final PropertyChangeEvent pce)
  {
    if (pce != null)
      for (final PropertyChangeListener l : this.listeners)
        try
        {
          l.propertyChange (pce);
        }
        catch (UnsupportedOperationException uoe)
        {
          LOG.log (Level.WARNING, "Unsupported operation in Property Change Handler!");
        }
  }
  
  protected final synchronized void fireChanged (final String propertyName, final Object oldValue, final Object newValue)
  {
    final PropertyChangeEvent pce = new PropertyChangeEvent (this, propertyName, oldValue, newValue);
    fire (pce);
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
}
