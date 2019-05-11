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
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.javajdj.jservice.midi.device.MidiDevice;
import org.javajdj.jservice.midi.device.MidiDeviceListener;
import org.javajdj.jservice.midi.device.alesis.qvgt.MidiDevice_QVGT;
import org.javajdj.jservice.midi.device.alesis.qvgt.Patch_QGVT;
import org.javajdj.jservice.midi.device.alesis.qvgt.swing.chart.JQVGT_EqChart_Config3;
import org.javajdj.swing.SwingUtilsJdJ;

/** A {@link JPanel} for monitoring and editing EQ parameters of an Alesis Quadraverb GT {@link MidiDevice}
 *  through (if supported for given configuration) {@code JFreeChart} components.
 * 
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 * @see MidiDevice
 * @see MidiDevice_QVGT
 * 
 * @since 0.4.0
 * 
 */
final class JQVGTPanel_EQ_Chart
  extends JPanel
{

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // LOGGING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private static final Logger LOG = Logger.getLogger (JQVGTPanel_EQ_Chart.class.getName ());
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTORS / FACTORIES / CLONING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public JQVGTPanel_EQ_Chart (final MidiDevice midiDevice)
  {
    super ();
    if (midiDevice == null || ! (midiDevice instanceof MidiDevice_QVGT))
      throw new IllegalArgumentException ();
    this.midiDevice = midiDevice;
    this.jEq3 = new JQVGT_EqChart_Config3 (midiDevice);
    midiDevice.addMidiDeviceListener (this.midiDeviceListener);
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PLOT X BOUNDARIES
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private final JPanel jEq3;
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MIDI DEVICE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final MidiDevice midiDevice;
  
  private MidiDevice getMidiDevice ()
  {
    return this.midiDevice;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MIDI DEVICE LISTENER
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final MidiDeviceListener midiDeviceListener = (final Map<String, Object> changes) ->
  {
    if (changes == null)
      return;
    if (changes.containsKey (MidiDevice_QVGT.EDIT_BUFFER_CONFIG_NAME))
    {
      SwingUtilsJdJ.invokeOnSwingEDT (
        () -> setGuiParameters ((Patch_QGVT.Configuration) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_CONFIG_NAME)));
    }
  };
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // SET GUI PARAMETERS
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private void setGuiParameters (final Patch_QGVT.Configuration configuration)
  {
    removeAll ();
    if (configuration != null)
    {
      switch (configuration)
      {
        case C1_EQ_PCH_DL_REV:
        case C2_LES_DL_REV:
        case C4_5EQ_PCH_DL:
        case C5_3EQ_REV:
        case C6_RING_DL_REV:
        case C7_RESO_DL_REV:
        case C8_SAMPLING:
        {
          setLayout (new GridLayout (1, 1, 5, 5));
          final JLabel label = new JLabel ("Not Implemented");
          label.setHorizontalAlignment (SwingConstants.CENTER);
          add (label);
          break;
        }
        case C3_GEQ_DL:
        {
          setLayout (new GridLayout (1, 1, 0, 0));
          add (this.jEq3);
          break;
        }
        default:
          throw new RuntimeException ();
      }
    }
    // We need to validate (); but this was found through trial-and-error.
    validate ();
    repaint ();
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
}
