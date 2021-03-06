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
import org.javajdj.jservice.midi.device.swing.parameter.JMidiDeviceParameter_Enum;
import org.javajdj.jservice.midi.device.swing.parameter.JMidiDeviceParameter_Integer_Slider;
import org.javajdj.swing.SwingUtilsJdJ;

/** A {@link JPanel} for monitoring and editing (some) REVERB parameters of an Alesis Quadraverb GT {@link MidiDevice}.
 * 
 * <p>
 * Auxiliary file to {@link JQVGTPanel}.
 * 
 * <p>
 * Due to space limitations on the GUI, the REVERB parameters are divided among two panels,
 * {@link JQVGTPanel_REVERB1} and {@link JQVGTPanel_REVERB2}.
 * 
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 * @see MidiDevice
 * @see MidiDevice_QVGT
 * 
 */
final class JQVGTPanel_REVERB1 extends JPanel
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
  public JQVGTPanel_REVERB1 (final MidiDevice midiDevice)
  {
    super ();
    if (midiDevice == null || ! (midiDevice instanceof MidiDevice_QVGT))
      throw new IllegalArgumentException ();
    this.midiDevice = midiDevice;
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "Mode", MidiDevice_QVGT.EDIT_BUFFER_REVERB_MODE_NAME, MidiDevice_QVGT.ReverbMode.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "Input 1", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1_INPUT1_NAME, MidiDevice_QVGT.ReverbInput1Config1.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "Input 1", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF2_INPUT1_NAME, MidiDevice_QVGT.ReverbInput1Config2.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "Input", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_INPUT_NAME, MidiDevice_QVGT.ReverbInputConfig5.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "Input 1", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF6_INPUT1_NAME, MidiDevice_QVGT.ReverbInput1Config6.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "Input 1", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF7_INPUT1_NAME, MidiDevice_QVGT.ReverbInput1Config7.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "Input 2", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1_INPUT2_NAME, MidiDevice_QVGT.ReverbInput2Config1.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "Input 2", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF2_INPUT2_NAME, MidiDevice_QVGT.ReverbInput2Config2.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "Input 2", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF6_INPUT2_NAME, MidiDevice_QVGT.ReverbInput2Config6.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "Input 2", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF7_INPUT2_NAME, MidiDevice_QVGT.ReverbInput2Config7.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Input Mix", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_IN_MIX_NAME, 0, 198, -99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Pre Delay [ms]", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_PRE_DELAY_NAME, 1, 140));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "PreDly Mix [Pre/Post]", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_PRE_MIX_NAME, 0 /* XXX Service Manual: 1 */, 198, -99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Diffusion", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_DIFFUSION_NAME, 0, 8, 1));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Density", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_NON_HALL_DENSITY_NAME, 0, 8, 1));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Pre Delay [ms]", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_PRE_DELAY_NAME, 1, 140));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "PreDly Mix [Pre/Post]", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_PRE_MIX_NAME, 0 /* XXX Service Manual: 1 */, 198, -99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Diffusion", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_DIFFUSION_NAME, 0, 8, 1));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Density", MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_NON_HALL_DENSITY_NAME, 0, 8, 1));
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
        {
          setLayout (new GridLayout (9, 1, 5, 5));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_MODE_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1_INPUT1_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1_INPUT2_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_IN_MIX_NAME));
          add (new JLabel ());
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_PRE_DELAY_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_PRE_MIX_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_DIFFUSION_NAME));
          final MidiDevice_QVGT.ReverbMode mode =
            (MidiDevice_QVGT.ReverbMode) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_MODE_NAME);
          if (mode != null && mode != MidiDevice_QVGT.ReverbMode.HALL)
            add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_NON_HALL_DENSITY_NAME));
          break;
        }
        case C2_LES_DL_REV:
        {
          setLayout (new GridLayout (9, 1, 5, 5));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_MODE_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF2_INPUT1_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF2_INPUT2_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_IN_MIX_NAME));
          add (new JLabel ());
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_PRE_DELAY_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_PRE_MIX_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_DIFFUSION_NAME));
          final MidiDevice_QVGT.ReverbMode mode =
            (MidiDevice_QVGT.ReverbMode) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_MODE_NAME);
          if (mode != null && mode != MidiDevice_QVGT.ReverbMode.HALL)
            add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_NON_HALL_DENSITY_NAME));
          break;
        }
        case C5_3EQ_REV:
        {
          setLayout (new GridLayout (9, 1, 5, 5));    
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_MODE_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_INPUT_NAME));
          add (new JLabel ());
          add (new JLabel ());
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_PRE_DELAY_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_PRE_MIX_NAME));
          add (new JLabel ());
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_DIFFUSION_NAME));
          final MidiDevice_QVGT.ReverbMode mode =
            (MidiDevice_QVGT.ReverbMode) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_MODE_NAME);
          if (mode != null && mode != MidiDevice_QVGT.ReverbMode.HALL)
            add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF5_NON_HALL_DENSITY_NAME));
          break;
        }
        case C6_RING_DL_REV:
        {
          setLayout (new GridLayout (9, 1, 5, 5));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_MODE_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF6_INPUT1_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF6_INPUT2_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_IN_MIX_NAME));
          add (new JLabel ());
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_PRE_DELAY_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_PRE_MIX_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_DIFFUSION_NAME));
          final MidiDevice_QVGT.ReverbMode mode =
            (MidiDevice_QVGT.ReverbMode) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_MODE_NAME);
          if (mode != null && mode != MidiDevice_QVGT.ReverbMode.HALL)
            add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_NON_HALL_DENSITY_NAME));
          break;
        }
        case C7_RESO_DL_REV:
        {
          setLayout (new GridLayout (9, 1, 5, 5));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_MODE_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF7_INPUT1_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF7_INPUT2_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_IN_MIX_NAME));
          add (new JLabel ());
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_PRE_DELAY_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_PRE_MIX_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_DIFFUSION_NAME));
          final MidiDevice_QVGT.ReverbMode mode =
            (MidiDevice_QVGT.ReverbMode) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_MODE_NAME);
          if (mode != null && mode != MidiDevice_QVGT.ReverbMode.HALL)
            add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_REVERB_CF1267_NON_HALL_DENSITY_NAME));
          break;
        }
        case C3_GEQ_DL:
        case C4_5EQ_PCH_DL:
        case C8_SAMPLING:
        {
          setLayout (new GridLayout (1, 1, 5, 5));
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
