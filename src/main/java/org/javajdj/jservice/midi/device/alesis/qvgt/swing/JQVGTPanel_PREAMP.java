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
import javax.swing.JPanel;
import org.javajdj.jservice.midi.device.MidiDevice;
import org.javajdj.jservice.midi.device.MidiDeviceListener;
import org.javajdj.jservice.midi.device.alesis.qvgt.MidiDevice_QVGT;
import org.javajdj.jservice.midi.device.alesis.qvgt.Patch_QGVT;
import org.javajdj.jservice.midi.device.swing.JMidiDeviceParameter;
import org.javajdj.jservice.midi.device.swing.JMidiDeviceParameter_Boolean;
import org.javajdj.jservice.midi.device.swing.JMidiDeviceParameter_Enum;
import org.javajdj.jservice.midi.device.swing.JMidiDeviceParameter_Integer_Slider;
import org.javajdj.swing.SwingUtilsJdJ;

/** A {@link JPanel} for monitoring and editing PREAMP parameters of an Alesis Quadraverb GT {@link MidiDevice}.
 * 
 * <p>
 * Auxiliary file to {@link JQVGTPanel}.
 * 
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 * @see MidiDevice
 * @see MidiDevice_QVGT
 * 
 */
final class JQVGTPanel_PREAMP extends JPanel
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
  public JQVGTPanel_PREAMP (final MidiDevice midiDevice)
  {
    super ();
    if (midiDevice == null || ! (midiDevice instanceof MidiDevice_QVGT))
      throw new IllegalArgumentException ();
    this.midiDevice = midiDevice;
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (
      midiDevice, "Compression", MidiDevice_QVGT.EDIT_BUFFER_PREAMP_COMPRESSION_NAME, 0, 7));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (
      midiDevice, "Overdrive", MidiDevice_QVGT.EDIT_BUFFER_PREAMP_OVERDRIVE_NAME, 0, 7));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (
      midiDevice, "Distortion", MidiDevice_QVGT.EDIT_BUFFER_PREAMP_DISTORTION_NAME, 0, 8));
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (
      midiDevice, "Tone", MidiDevice_QVGT.EDIT_BUFFER_PREAMP_TONE_NAME, MidiDevice_QVGT.PreampTone.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Boolean (
      midiDevice, "Bass Boost", MidiDevice_QVGT.EDIT_BUFFER_PREAMP_BASS_BOOST_NAME));
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (
      midiDevice, "Cab Simulator", MidiDevice_QVGT.EDIT_BUFFER_PREAMP_CAB_SIMULATOR_NAME, MidiDevice_QVGT.CabSimulator.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Boolean (
      midiDevice, "Effect Loop", MidiDevice_QVGT.EDIT_BUFFER_PREAMP_EFFECT_LOOP_NAME));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (
      midiDevice, "Noise Gate", MidiDevice_QVGT.EDIT_BUFFER_PREAMP_NOISE_GATE_RAW_NAME, 0, 17));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (
      midiDevice, "Output Level", MidiDevice_QVGT.EDIT_BUFFER_PREAMP_OUTPUT_LEVEL_NAME, 0, 99));
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
      switch (configuration)
      {
        case C1_EQ_PCH_DL_REV:
        case C2_LES_DL_REV:
        case C3_GEQ_DL:
        case C4_5EQ_PCH_DL:
        case C5_3EQ_REV:
        case C6_RING_DL_REV:
        case C7_RESO_DL_REV:
        case C8_SAMPLING:
        {
          setLayout (new GridLayout (9, 1, 0, 4));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_PREAMP_COMPRESSION_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_PREAMP_OVERDRIVE_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_PREAMP_DISTORTION_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_PREAMP_TONE_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_PREAMP_BASS_BOOST_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_PREAMP_CAB_SIMULATOR_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_PREAMP_EFFECT_LOOP_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_PREAMP_NOISE_GATE_RAW_NAME));
          if (configuration != Patch_QGVT.Configuration.C8_SAMPLING)
            add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_PREAMP_OUTPUT_LEVEL_NAME));
          break;
        }
        default:
          throw new RuntimeException ();
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
