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
import javax.swing.SwingConstants;
import org.javajdj.jservice.midi.device.MidiDevice;
import org.javajdj.jservice.midi.device.MidiDeviceListener;
import org.javajdj.jservice.midi.device.alesis.qvgt.MidiDevice_QVGT;
import org.javajdj.jservice.midi.device.alesis.qvgt.Patch_QGVT;
import org.javajdj.jservice.midi.device.swing.parameter.JMidiDeviceParameter;
import org.javajdj.jservice.midi.device.swing.parameter.JMidiDeviceParameter_Boolean;
import org.javajdj.jservice.midi.device.swing.parameter.JMidiDeviceParameter_Enum;
import org.javajdj.jservice.midi.device.swing.parameter.JMidiDeviceParameter_Integer_Slider;
import org.javajdj.swing.SwingUtilsJdJ;

/** A {@link JPanel} for monitoring and editing PITCH parameters of an Alesis Quadraverb GT {@link MidiDevice}.
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
final class JQVGTPanel_PITCH extends JPanel
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
  public JQVGTPanel_PITCH (final MidiDevice midiDevice)
  {
    super ();
    if (midiDevice == null || ! (midiDevice instanceof MidiDevice_QVGT))
      throw new IllegalArgumentException ();
    this.midiDevice = midiDevice;
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "Mode", MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF14_MODE_NAME, MidiDevice_QVGT.PitchMode.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "Input", MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF14_INPUT_NAME, MidiDevice_QVGT.PitchInput.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "Wave", MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF14_CHORUS_WAVESHAPE_NAME, MidiDevice_QVGT.LfoWaveshape.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Speed", MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF14_CHORUS_SPEED_NAME, 0, 98, 1));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Depth", MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF14_CHORUS_DEPTH_NAME, 0, 98, 1));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Feedback [%]", MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF14_CHORUS_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Speed", MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF14_FLANGE_SPEED_NAME, 0, 98, 1));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Depth", MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF14_FLANGE_DEPTH_NAME, 0, 98, 1));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Feedback [%]", MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF14_FLANGE_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Boolean (midiDevice,
      "Trigger", MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF14_FLANGE_TRIGGER_NAME));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Detune [cents]", MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF14_DETUNE_TUNE_NAME, 0, 198, -99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Speed", MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF14_PHASER_SPEED_NAME, 0, 98, 1));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Depth", MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF14_PHASER_DEPTH_NAME, 0, 98, 1));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Stero Separation", MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF2_LESLIE_SEPARATION_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Boolean (midiDevice,
      "Motor Control", MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF2_LESLIE_MOTOR_CONTROL_NAME));
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "Speed", MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF2_LESLIE_MOTOR_SPEED_NAME, MidiDevice_QVGT.LeslieSpeed.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Boolean (midiDevice,
      "Chorus Enable", MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF5_CHORUS_ENABLE_NAME));
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "Wave", MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF5_CHORUS_WAVESHAPE_NAME, MidiDevice_QVGT.LfoWaveshape.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Speed", MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF5_CHORUS_SPEED_NAME, 0, 98, 1));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Depth", MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF5_CHORUS_DEPTH_NAME, 0, 98, 1));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Spectrum Shift [Hz]", MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF6_RINGMOD_SPECTRUM_SHIFT_NAME, 1, 300));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Output Shift Mix", MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF6_RINGMOD_OUTPUT_SHIFT_MIX_NAME, 0, 198, -99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "DL/REV Shift Mix", MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF6_RINGMOD_DL_REV_SHIFT_MIX_NAME, 0, 198, -99));
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
    if (changes.containsKey (MidiDevice_QVGT.EDIT_BUFFER_CONFIG_NAME)
     || changes.containsKey (MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF14_MODE_NAME))
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
        case C4_5EQ_PCH_DL:
        {
          setLayout (new GridLayout (7, 1, 5, 5));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF14_MODE_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF14_INPUT_NAME));
          final MidiDevice_QVGT.PitchMode mode =
            (MidiDevice_QVGT.PitchMode) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF14_MODE_NAME);
          if (mode != null)
            switch (mode)
            {
              case MONO_CHORUS:
              case STEREO_CHORUS:
              {
                add (new JLabel ());
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF14_CHORUS_WAVESHAPE_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF14_CHORUS_SPEED_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF14_CHORUS_DEPTH_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF14_CHORUS_FEEDBACK_NAME));
                break;
              }
              case MONO_FLANGE:
              case STEREO_FLANGE:
                add (new JLabel ());
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF14_FLANGE_SPEED_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF14_FLANGE_DEPTH_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF14_FLANGE_FEEDBACK_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF14_FLANGE_TRIGGER_NAME));
                break;
              case DETUNE:
                add (new JLabel ());
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF14_DETUNE_TUNE_NAME));
                break;
              case PHASER:
                add (new JLabel ());
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF14_PHASER_SPEED_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF14_PHASER_DEPTH_NAME));
                break;
              default:
                throw new RuntimeException ();
            }
          break;
        }
        case C2_LES_DL_REV:
        {
          setLayout (new GridLayout (7, 1, 5, 5));
          add (new JLabel ());
          final JLabel label = new JLabel ("Leslie");
          label.setHorizontalAlignment (SwingConstants.CENTER);
          add (label);
          add (new JLabel ());
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF2_LESLIE_SEPARATION_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF2_LESLIE_MOTOR_CONTROL_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF2_LESLIE_MOTOR_SPEED_NAME));
          break;
        }
        case C5_3EQ_REV:
        {
          setLayout (new GridLayout (7, 1, 5, 5));
          final JLabel label = new JLabel ("Reverb Chorus");
          label.setHorizontalAlignment (SwingConstants.CENTER);
          add (label);
          add (new JLabel ());
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF5_CHORUS_ENABLE_NAME));
          add (new JLabel ());
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF5_CHORUS_WAVESHAPE_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF5_CHORUS_SPEED_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF5_CHORUS_DEPTH_NAME));
          break;
        }
        case C6_RING_DL_REV:
        {
          setLayout (new GridLayout (7, 1, 5, 5));
          final JLabel label = new JLabel ("Ring Modulator");
          label.setHorizontalAlignment (SwingConstants.CENTER);
          add (label);
          add (new JLabel ());
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF6_RINGMOD_SPECTRUM_SHIFT_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF6_RINGMOD_OUTPUT_SHIFT_MIX_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_PITCH_CF6_RINGMOD_DL_REV_SHIFT_MIX_NAME));
          break;
        }
        case C3_GEQ_DL:
        case C7_RESO_DL_REV:
        case C8_SAMPLING:
        {
          setLayout (new GridLayout (7, 1, 5, 5));
          add (new JLabel ());
          add (new JLabel ());
          add (new JLabel ());
          final JLabel label = new JLabel ("Not Available");
          label.setHorizontalAlignment (SwingConstants.CENTER);
          add (label);
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
