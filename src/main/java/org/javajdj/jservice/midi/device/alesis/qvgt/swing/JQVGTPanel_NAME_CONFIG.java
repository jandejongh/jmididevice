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

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.javajdj.jservice.midi.device.MidiDevice;
import org.javajdj.jservice.midi.device.alesis.qvgt.MidiDevice_QVGT;
import org.javajdj.jservice.midi.device.alesis.qvgt.Patch_QGVT;
import org.javajdj.jservice.midi.device.swing.JMidiDeviceParameter_Enum;
import org.javajdj.jservice.midi.device.swing.JMidiDeviceParameter_String;

/** A {@link JPanel} the NAME and CONFIGURATION of the current patch (Edit Buffer)
 *  of an Alesis Quadraverb GT {@link MidiDevice}.
 * 
 * <p>
 * Auxiliary file to {@link JQVGTPanel}.
 * 
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 */
final class JQVGTPanel_NAME_CONFIG
  extends JPanel
{
  
  /** Constructs the panel.
   * 
   * @param midiDevice The MIDI device, must be non-{@code null} and, at the present time, a {@link MidiDevice_QVGT}.
   * 
   * @throws IllegalArgumentException If the MIDI device is {@code null} or of illegal type.
   * 
   */
  public JQVGTPanel_NAME_CONFIG (final MidiDevice midiDevice)
  {
    super ();
    setLayout (new GridLayout (6, 1, 5, 5));
    add (new JMidiDeviceParameter_String (midiDevice,
      "Name", MidiDevice_QVGT.EDIT_BUFFER_NAME_NAME));
    add (new JLabel ());
    add (new JMidiDeviceParameter_Enum (midiDevice,
      "Config", MidiDevice_QVGT.EDIT_BUFFER_CONFIG_NAME, Patch_QGVT.Configuration.class));
    add (new JLabel ());
    add (new JLabel ());
    add (new JLabel ());
  }
    
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
}
