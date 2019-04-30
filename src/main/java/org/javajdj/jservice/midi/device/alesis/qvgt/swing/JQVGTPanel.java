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
package org.javajdj.jservice.midi.device.alesis.qvgt.swing;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;
import org.javajdj.jservice.midi.MidiService;
import org.javajdj.jservice.midi.device.MidiDevice;
import org.javajdj.jservice.midi.swing.JMidiService;
import org.javajdj.jservice.midi.swing.JRawMidiService;

/** A {@link JPanel} for controlling and monitoring an Alesis Quadraverb GT.
 *
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 */
public class JQVGTPanel
  extends JPanel
{

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // LOGGING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private static final Logger LOG = Logger.getLogger (JQVGTPanel.class.getName ());

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTORS / FACTORIES / CLONING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  /** Constructs the panel.
   * 
   * @param midiService The MIDI Service, non-{@code null}.
   * @param midiDevice  The MIDI device, must be non-{@code null} and, at the present time, a {@link MidiDevice_QVGT}.
   *
   * @throws IllegalArgumentException If the service or device is {@code null}, or if the device is of illegal type.
   * 
   */
  public JQVGTPanel (final MidiService midiService, final MidiDevice midiDevice)
  {
    
    super (); 
    
    if (midiDevice == null)
      throw new IllegalArgumentException ();
    this.midiDevice = midiDevice;
    
    setLayout (new GridLayout (3, 5, 6, 6));
    
    final JPanel midiServicePanel;
    if (midiService == null)
      midiServicePanel = new JMidiService ();
    else if (midiService instanceof JMidiService)
      midiServicePanel = (JPanel) midiService;
    else if (midiService instanceof JRawMidiService)
      midiServicePanel = (JPanel) midiService;
    else
      midiServicePanel = new JRawMidiService (midiService);
    addBorderSYSGroup (midiServicePanel, "MIDI");
    add (midiServicePanel);

    final JPanel jQgvtJPanel = new JQVGTPanel_Device (midiDevice);
    addBorderSYSGroup (jQgvtJPanel, "Quadraverb GT [Device]");
    add (jQgvtJPanel);

    final JPanel jPatchIO = new JQVGTPanel_PATCH_IO (midiDevice);
    addBorderLIBGroup (jPatchIO, "Quadraverb GT [Patch I/O]");
    add (jPatchIO);
    
    final JPanel jPatchSelector = new JQVGTPanel_PATCH_SELECTOR (midiDevice);
    addBorderSYSGroup (jPatchSelector, "Quadraverb GT [Patch Selector]");
    add (jPatchSelector);

    final JPanel jNameConfig = new JQVGTPanel_NAME_CONFIG (midiDevice);
    addBorderFXGroup (jNameConfig, "Name/Config");
    add (jNameConfig);
    
    final JPanel jPreampJPanel = new JQVGTPanel_PREAMP (midiDevice);
    addBorderFXGroup (jPreampJPanel, "PREAMP");
    add (jPreampJPanel);
    
    final JPanel jEq1JPanel = new JQVGTPanel_EQ1 (midiDevice);
    addBorderFXGroup (jEq1JPanel, "EQ [1/2]");
    add (jEq1JPanel);
    
    final JPanel jEq2JPanel = new JQVGTPanel_EQ2 (midiDevice);
    addBorderFXGroup (jEq2JPanel, "EQ [2/2]");
    add (jEq2JPanel);
    
    final JPanel jPitchJPanel = new JQVGTPanel_PITCH (midiDevice);
    addBorderFXGroup (jPitchJPanel, "PITCH");
    add (jPitchJPanel);
    
    final JPanel jDelayJPanel = new JQVGTPanel_DELAY (midiDevice);
    addBorderFXGroup (jDelayJPanel, "DELAY");
    add (jDelayJPanel);
    
    final JPanel jReverb1JPanel = new JQVGTPanel_REVERB1 (midiDevice);
    addBorderFXGroup (jReverb1JPanel, "REVERB [1/2]");
    add (jReverb1JPanel);
    
    final JPanel jReverb2JPanel = new JQVGTPanel_REVERB2 (midiDevice);
    addBorderFXGroup (jReverb2JPanel, "REVERB [2/2]");
    add (jReverb2JPanel);
    
    final JPanel jMixJPanel = new JQVGTPanel_MIX (midiDevice);
    addBorderFXGroup (jMixJPanel, "MIX [LEVELS/ROUTING]");
    add (jMixJPanel);
    
    final JPanel jMixModJPanel = new JQVGTPanel_MIX_MOD (midiDevice);
    addBorderFXGroup (jMixModJPanel, "MIX [MODULATION]");
    add (jMixModJPanel);
    
    final JPanel jModulationJPanel = new JQVGTPanel_MODULATION (midiDevice);
    addBorderFXGroup (jModulationJPanel, "MODULATION");
    add (jModulationJPanel);
    
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MIDI DEVICE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final MidiDevice midiDevice;
  
  public final MidiDevice getMidiDevice ()
  {
    return this.midiDevice;
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // BORDERS
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final static Color SYS_GROUP_BORDER_COLOR = Color.BLACK;
  
  private void addBorderSYSGroup (final JPanel sysGroupPanel, final String title)
  {
    if (sysGroupPanel == null || title == null)
      throw new IllegalArgumentException ();
    final Border lineBorder = BorderFactory.createLineBorder (SYS_GROUP_BORDER_COLOR, 2, true);
    final Border border = BorderFactory.createTitledBorder (lineBorder, title);
    sysGroupPanel.setBorder (border);
  }
  
  private final static Color LIB_GROUP_BORDER_COLOR = Color.YELLOW.darker ();
  
  private void addBorderLIBGroup (final JPanel libGroupPanel, final String title)
  {
    if (libGroupPanel == null || title == null)
      throw new IllegalArgumentException ();
    final Border lineBorder = BorderFactory.createLineBorder (LIB_GROUP_BORDER_COLOR, 2, true);
    final Border border = BorderFactory.createTitledBorder (lineBorder, title);
    libGroupPanel.setBorder (border);
  }
  
  private final static Color FX_GROUP_BORDER_COLOR = Color.BLUE;
  
  private void addBorderFXGroup (final JPanel fxGroupPanel, final String title)
  {
    if (fxGroupPanel == null || title == null)
      throw new IllegalArgumentException ();
    final Border lineBorder = BorderFactory.createLineBorder (FX_GROUP_BORDER_COLOR, 2, true);
    final Border border = BorderFactory.createTitledBorder (lineBorder, title);
    fxGroupPanel.setBorder (border);
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
}
