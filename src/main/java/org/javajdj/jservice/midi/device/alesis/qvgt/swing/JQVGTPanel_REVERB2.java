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
import org.javajdj.jservice.midi.device.swing.JMidiDeviceParameter;
import org.javajdj.jservice.midi.device.swing.JMidiDeviceParameter_Boolean;
import org.javajdj.jservice.midi.device.swing.JMidiDeviceParameter_Integer_Slider;
import org.javajdj.swing.SwingUtilsJdJ;

/** A {@link JPanel} for monitoring and editing (some) REVERB parameters of an Alesis Quadraverb GT {@link MidiDevice}.
 * 
 * <p>
 * Auxiliary file to {@link JQVGTPanel}.
 * 
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 * <p>
 * Due to space limitations on the GUI, the REVERB parameters are divided among two panels,
 * {@link JQVGTPanel_REVERB1} and {@link JQVGTPanel_REVERB2}.
 * 
 * @see MidiDevice
 * @see MidiDevice_QVGT
 * 
 */
final class JQVGTPanel_REVERB2 extends JPanel
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
  public JQVGTPanel_REVERB2 (final MidiDevice midiDevice)
  {
    super ();
    if (midiDevice == null || ! (midiDevice instanceof MidiDevice_QVGT))
      throw new IllegalArgumentException ();
    this.midiDevice = midiDevice;
    //
    // Configs 1, 2, 6, 7.
    //
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Decay", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_FORWARD_DECAY_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "[Reverse] Time", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_REVERSE_TIME_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Low Decay", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_NON_HALL_LOW_DECAY_NAME, 0, 60));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "High Decay", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_NON_HALL_HIGH_DECAY_NAME, 0, 60));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Low Decay", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_HALL_LOW_DECAY_NAME, 0, 60));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "High Decay", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_HALL_HIGH_DECAY_NAME, 0, 60));
    addMidiDeviceParameter (new JMidiDeviceParameter_Boolean (midiDevice,
      "Gate Enabled", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_PLROCH_GATE_NAME));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Gate Hold Time", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_PLROCH_GATE_HOLD_TIME_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Gate Release Time", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_PLROCH_GATE_REL_TIME_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Gated Level", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_PLROCH_GATE_LEVEL_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Boolean (midiDevice,
      "Gate Enabled", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_HALL_GATE_NAME));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Gate Hold Time", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_HALL_GATE_HOLD_TIME_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Gate Release Time", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_HALL_GATE_REL_TIME_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Gated Level", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_HALL_GATE_LEVEL_NAME, 0, 99));
    //
    // Config 5.
    //
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Decay", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_FORWARD_DECAY_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "[Reverse] Time", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_REVERSE_TIME_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Low Decay", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_NON_HALL_LOW_DECAY_NAME, 0, 60));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "High Decay", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_NON_HALL_HIGH_DECAY_NAME, 0, 60));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Low Decay", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_HALL_LOW_DECAY_NAME, 0, 60));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "High Decay", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_HALL_HIGH_DECAY_NAME, 0, 60));
    addMidiDeviceParameter (new JMidiDeviceParameter_Boolean (midiDevice,
      "Gate Enabled", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_PLROCH_GATE_NAME));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Gate Hold Time", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_PLROCH_GATE_HOLD_TIME_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Gate Release Time", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_PLROCH_GATE_REL_TIME_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Gated Level", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_PLROCH_GATE_LEVEL_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Boolean (midiDevice,
      "Gate Enabled", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_HALL_GATE_NAME));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Gate Hold Time", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_HALL_GATE_HOLD_TIME_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Gate Release Time", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_HALL_GATE_REL_TIME_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Gated Level", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_HALL_GATE_LEVEL_NAME, 0, 99));
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
     || changes.containsKey (MidiDevice_QVGT.EDIT_BUFFER_REVERB_MODE_NAME))
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
        case C2_LES_DL_REV:
        case C6_RING_DL_REV:
        case C7_RESO_DL_REV:
        {
          setLayout (new GridLayout (9, 1, 5, 0));
          final MidiDevice_QVGT.ReverbMode mode =
            (MidiDevice_QVGT.ReverbMode) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_MODE_NAME);
          if (mode != null)
          {
            switch (mode)
            {
              case PLATE:
              case ROOM:
              case CHAMBER:
              case HALL:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_FORWARD_DECAY_NAME));
                break;
              case REVERSE:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_REVERSE_TIME_NAME));
                break;
              default:
                throw new RuntimeException ();
            }
            switch (mode)
            {
              case PLATE:
              case ROOM:
              case CHAMBER:
              case REVERSE:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_NON_HALL_LOW_DECAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_NON_HALL_HIGH_DECAY_NAME));
                break;
              case HALL:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_HALL_LOW_DECAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_HALL_HIGH_DECAY_NAME));
                break;
              default:
                throw new RuntimeException ();
            }
            switch (mode)
            {
              case PLATE:
              case ROOM:
              case CHAMBER:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_PLROCH_GATE_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_PLROCH_GATE_HOLD_TIME_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_PLROCH_GATE_REL_TIME_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_PLROCH_GATE_LEVEL_NAME));
                break;
              case HALL:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_HALL_GATE_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_HALL_GATE_HOLD_TIME_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_HALL_GATE_REL_TIME_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_HALL_GATE_LEVEL_NAME));
                break;
              case REVERSE:
                break;
              default:
                throw new RuntimeException ();
            }
          }
          break;
        }
        case C5_3EQ_REV:
        {
          setLayout (new GridLayout (9, 1, 5, 0));
          final MidiDevice_QVGT.ReverbMode mode =
            (MidiDevice_QVGT.ReverbMode) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_MODE_NAME);
          if (mode != null)
          {
            switch (mode)
            {
              case PLATE:
              case ROOM:
              case CHAMBER:
              case HALL:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_FORWARD_DECAY_NAME));
                break;
              case REVERSE:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_REVERSE_TIME_NAME));
                break;
              default:
                throw new RuntimeException ();
            }
            switch (mode)
            {
              case PLATE:
              case ROOM:
              case CHAMBER:
              case REVERSE:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_NON_HALL_LOW_DECAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_NON_HALL_HIGH_DECAY_NAME));
                break;
              case HALL:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_HALL_LOW_DECAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_HALL_HIGH_DECAY_NAME));
                break;
              default:
                throw new RuntimeException ();
            }
            switch (mode)
            {
              case PLATE:
              case ROOM:
              case CHAMBER:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_PLROCH_GATE_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_PLROCH_GATE_HOLD_TIME_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_PLROCH_GATE_REL_TIME_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_PLROCH_GATE_LEVEL_NAME));
                break;
              case HALL:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_HALL_GATE_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_HALL_GATE_HOLD_TIME_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_HALL_GATE_REL_TIME_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_HALL_GATE_LEVEL_NAME));
                break;
              case REVERSE:
                break;
              default:
                throw new RuntimeException ();
            }
          }
          break;
        }
        case C3_GEQ_DL:
        case C4_5EQ_PCH_DL:
        case C8_SAMPLING:
        {
          setLayout (new GridLayout (1, 1, 5, 0));
          final JLabel label = new JLabel ("Not Available");
          label.setHorizontalAlignment (SwingConstants.CENTER);
          add (label);
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
