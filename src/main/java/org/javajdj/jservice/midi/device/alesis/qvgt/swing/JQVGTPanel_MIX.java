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
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.javajdj.jservice.midi.device.MidiDevice;
import org.javajdj.jservice.midi.device.MidiDeviceListener;
import org.javajdj.jservice.midi.device.alesis.qvgt.MidiDevice_QVGT;
import org.javajdj.jservice.midi.device.alesis.qvgt.Patch_QGVT;
import org.javajdj.jservice.midi.device.swing.JMidiDeviceParameter;
import org.javajdj.jservice.midi.device.swing.JMidiDeviceParameter_Enum;
import org.javajdj.jservice.midi.device.swing.JMidiDeviceParameter_Integer_Slider;
import org.javajdj.swing.SwingUtilsJdJ;

/** A {@link JPanel} for monitoring and editing (non-modulation) MIX parameters of an Alesis Quadraverb GT {@link MidiDevice}.
 * 
 * <p>
 * Auxiliary file to {@link JQVGTPanel}.
 * 
 * <p>
 * The modulation parameters of the mixer are in {@link JQVGTPanel_MIX_MOD}.
 * 
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 * @see MidiDevice
 * @see MidiDevice_QVGT
 * @see JQVGTPanel_MIX_MOD
 * 
 */
final class JQVGTPanel_MIX
  extends JPanel
{

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTORS / FACTORIES / CLONING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  /** Constructs the panel.
   * 
   * @param midiDevice The MIDI device, must be non-{@code null} and, at the present time, a {@link MidiDevice_QVGT}.
   * 
   * @throws IllegalArgumentException If the MIDI device is {@code null} or of illegal type.
   * 
   */
  public JQVGTPanel_MIX (final MidiDevice midiDevice)
  {
    super ();
    if (midiDevice == null || ! (midiDevice instanceof MidiDevice_QVGT))
      throw new IllegalArgumentException ();
    this.midiDevice = midiDevice;
    setLayout (new GridLayout (9, 1, 5, 5));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Direct Level", MidiDevice_QVGT.EDIT_BUFFER_MIX_DIRECT_LEVEL_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Master Fx Level", MidiDevice_QVGT.EDIT_BUFFER_MIX_MASTER_FX_LEVEL_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "    Preamp Signal", MidiDevice_QVGT.EDIT_BUFFER_MIX_CF1_PREAMP_SIGNAL_NAME, MidiDevice_QVGT.PreampSignal.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "    Preamp/Eq Level", MidiDevice_QVGT.EDIT_BUFFER_MIX_CF1_PREAMP_OR_EQ_LEVEL_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "    Pitch Level", MidiDevice_QVGT.EDIT_BUFFER_MIX_CF1_PITCH_LEVEL_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "    Delay Level", MidiDevice_QVGT.EDIT_BUFFER_MIX_CF1_DELAY_LEVEL_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "    Reverb Level", MidiDevice_QVGT.EDIT_BUFFER_MIX_CF1_REVERB_LEVEL_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "    Leslie Level", MidiDevice_QVGT.EDIT_BUFFER_MIX_CF2_LESLIE_LEVEL_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "    Delay Level", MidiDevice_QVGT.EDIT_BUFFER_MIX_CF2_DELAY_LEVEL_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "    Reverb Level", MidiDevice_QVGT.EDIT_BUFFER_MIX_CF2_REVERB_LEVEL_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "    Eq Level", MidiDevice_QVGT.EDIT_BUFFER_MIX_CF3_EQ_LEVEL_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "    Delay Level", MidiDevice_QVGT.EDIT_BUFFER_MIX_CF3_DELAY_LEVEL_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "    Preamp Signal", MidiDevice_QVGT.EDIT_BUFFER_MIX_CF4_PREAMP_SIGNAL_NAME, MidiDevice_QVGT.PreampSignal.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "    Preamp/Eq Level", MidiDevice_QVGT.EDIT_BUFFER_MIX_CF4_PREAMP_OR_EQ_LEVEL_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "    Pitch Level", MidiDevice_QVGT.EDIT_BUFFER_MIX_CF4_PITCH_LEVEL_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "    Delay Level", MidiDevice_QVGT.EDIT_BUFFER_MIX_CF4_DELAY_LEVEL_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "    Preamp Signal", MidiDevice_QVGT.EDIT_BUFFER_MIX_CF5_PREAMP_SIGNAL_NAME, MidiDevice_QVGT.PreampSignal.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "    Preamp/Eq Level", MidiDevice_QVGT.EDIT_BUFFER_MIX_CF5_PREAMP_OR_EQ_LEVEL_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "    Reverb Level", MidiDevice_QVGT.EDIT_BUFFER_MIX_CF5_REVERB_LEVEL_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "    Preamp Level", MidiDevice_QVGT.EDIT_BUFFER_MIX_CF6_PREAMP_LEVEL_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "    Ring Mod Level", MidiDevice_QVGT.EDIT_BUFFER_MIX_CF6_RINGMOD_LEVEL_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "    Delay Level", MidiDevice_QVGT.EDIT_BUFFER_MIX_CF6_DELAY_LEVEL_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "    Reverb Level", MidiDevice_QVGT.EDIT_BUFFER_MIX_CF6_REVERB_LEVEL_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "    Preamp Level", MidiDevice_QVGT.EDIT_BUFFER_MIX_CF7_PREAMP_LEVEL_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "    Resonator Level", MidiDevice_QVGT.EDIT_BUFFER_MIX_CF7_RESONATOR_LEVEL_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "    Delay Level", MidiDevice_QVGT.EDIT_BUFFER_MIX_CF7_DELAY_LEVEL_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "    Reverb Level", MidiDevice_QVGT.EDIT_BUFFER_MIX_CF7_REVERB_LEVEL_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Preamp Level", MidiDevice_QVGT.EDIT_BUFFER_MIX_CF8_PREAMP_LEVEL_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Playback Level", MidiDevice_QVGT.EDIT_BUFFER_MIX_CF8_PLAYBACK_LEVEL_NAME, 0, 99));
    setGuiParameters ((Patch_QGVT.Configuration) midiDevice.get (MidiDevice_QVGT.EDIT_BUFFER_CONFIG_NAME));
    midiDevice.addMidiDeviceListener (this.midiDeviceListener);
  }
  
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
      // XXX The commented-out version should work as well; apparently our device does not provide the new values properly [?].
      // () -> setGuiParameters ((Patch_QGVT.Configuration) changes.get (MidiDevice_QVGT.EDIT_BUFFER_CONFIG_NAME)));
      () -> setGuiParameters ((Patch_QGVT.Configuration) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_CONFIG_NAME)));
    }
  };
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PARAMETER MAP
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final Map<String, JMidiDeviceParameter> parameterMap = new HashMap<> ();

  private void addMidiDeviceParameter (final JMidiDeviceParameter midiDeviceParameter)
  {
    if (midiDeviceParameter == null || this.parameterMap.containsKey (midiDeviceParameter.getKey ()))
      throw new IllegalArgumentException ();
    this.parameterMap.put (midiDeviceParameter.getKey (), midiDeviceParameter);
  }

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
        {
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_DIRECT_LEVEL_NAME));
          add (new JLabel ());
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_MASTER_FX_LEVEL_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_CF1_PREAMP_SIGNAL_NAME));
          add (new JLabel ());
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_CF1_PREAMP_OR_EQ_LEVEL_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_CF1_PITCH_LEVEL_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_CF1_DELAY_LEVEL_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_CF1_REVERB_LEVEL_NAME));
          break;
        }
        case C2_LES_DL_REV:
        {
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_DIRECT_LEVEL_NAME));
          add (new JLabel ());
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_MASTER_FX_LEVEL_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_CF2_LESLIE_LEVEL_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_CF2_DELAY_LEVEL_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_CF2_REVERB_LEVEL_NAME));
          break;
        }
        case C3_GEQ_DL:
        {
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_DIRECT_LEVEL_NAME));
          add (new JLabel ());
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_MASTER_FX_LEVEL_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_CF3_EQ_LEVEL_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_CF3_DELAY_LEVEL_NAME));
          break;
        }
        case C4_5EQ_PCH_DL:
        {
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_DIRECT_LEVEL_NAME));
          add (new JLabel ());
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_MASTER_FX_LEVEL_NAME));
          add (new JLabel ());
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_CF4_PREAMP_SIGNAL_NAME));
          add (new JLabel ());
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_CF4_PREAMP_OR_EQ_LEVEL_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_CF4_PITCH_LEVEL_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_CF4_DELAY_LEVEL_NAME));
          break;
        }
        case C5_3EQ_REV:
        {
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_DIRECT_LEVEL_NAME));
          add (new JLabel ());
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_MASTER_FX_LEVEL_NAME));
          add (new JLabel ());
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_CF5_PREAMP_SIGNAL_NAME));
          add (new JLabel ());
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_CF5_PREAMP_OR_EQ_LEVEL_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_CF5_REVERB_LEVEL_NAME));
          break;
        }        
        case C6_RING_DL_REV:
        {
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_DIRECT_LEVEL_NAME));
          add (new JLabel ());
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_MASTER_FX_LEVEL_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_CF6_PREAMP_LEVEL_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_CF6_RINGMOD_LEVEL_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_CF6_DELAY_LEVEL_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_CF6_REVERB_LEVEL_NAME));
          break;
        }
        case C7_RESO_DL_REV:
        {
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_DIRECT_LEVEL_NAME));
          add (new JLabel ());
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_MASTER_FX_LEVEL_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_CF7_PREAMP_LEVEL_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_CF7_RESONATOR_LEVEL_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_CF7_DELAY_LEVEL_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_CF7_REVERB_LEVEL_NAME));
          break;
        }
        case C8_SAMPLING:
        {
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_DIRECT_LEVEL_NAME));
          add (new JLabel ());
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_CF8_PREAMP_LEVEL_NAME));
          add (new JLabel ());
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_MIX_CF8_PLAYBACK_LEVEL_NAME));
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
