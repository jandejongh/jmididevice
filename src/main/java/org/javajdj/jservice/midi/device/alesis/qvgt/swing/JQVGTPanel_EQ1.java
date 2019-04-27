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
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.javajdj.jservice.midi.device.MidiDevice;
import org.javajdj.jservice.midi.device.MidiDeviceListener;
import org.javajdj.jservice.midi.device.alesis.qvgt.MidiDevice_QVGT;
import org.javajdj.jservice.midi.device.alesis.qvgt.Patch_QGVT;
import org.javajdj.jservice.midi.device.swing.JMidiDeviceParameter;
import org.javajdj.jservice.midi.device.swing.JMidiDeviceParameter_Enum;
import org.javajdj.jservice.midi.device.swing.JMidiDeviceParameter_Integer_Slider;
import org.javajdj.swing.SwingUtilsJdJ;

/** A {@link JPanel} for monitoring and editing (some) EQ parameters of an Alesis Quadraverb GT {@link MidiDevice}.
 * 
 * <p>
 * Auxiliary file to {@link JQVGTPanel}.
 * 
 * <p>
 * Due to space limitations on the GUI, the EQ parameters are divided among two panels,
 * {@link JQVGTPanel_EQ1} and {@link JQVGTPanel_EQ2}.
 * 
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 * @see MidiDevice
 * @see MidiDevice_QVGT
 * @see JQVGTPanel_EQ2
 * 
 */
final class JQVGTPanel_EQ1 extends JPanel
{

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // LOGGING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private static final Logger LOG = Logger.getLogger (JQVGTPanel_EQ1.class.getName ());
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTORS / FACTORIES / CLONING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public JQVGTPanel_EQ1 (final MidiDevice midiDevice)
  {
    super ();
    if (midiDevice == null || ! (midiDevice instanceof MidiDevice_QVGT))
      throw new IllegalArgumentException ();
    this.midiDevice = midiDevice;
    setLayout (new GridLayout (9, 1, 5, 0));
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "Preset", MidiDevice_QVGT.EDIT_BUFFER_EQ_PRESET_NAME, MidiDevice_QVGT.EqPreset.class));
    
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "Mode", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_MODE_NAME, MidiDevice_QVGT.EqModeConfig1.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Low Frequency", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_3B_LOW_F_NAME, 20, 999));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Low Amplitude", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_3B_LOW_AMP_NAME, 0, 560));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Mid Frequency", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_3B_MID_F_NAME, 200, 9999));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Mid Frequency", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_3B_MID_BW_NAME, 20, 255));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Mid Amplitude", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_3B_MID_AMP_NAME, 0, 560));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "High Frequency", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_3B_HIGH_F_NAME, 2000, 18000));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "High Amplitude", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_3B_HIGH_AMP_NAME, 0, 560));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Mid Frequency", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_RESO_MID_F_NAME, 200, 9999));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Mid Frequency", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_RESO_MID_BW_NAME, 20, 255));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Mid Amplitude", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_RESO_MID_AMP_NAME, 0, 560));
    
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "High Rotor Level", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF2_HIGH_ROTOR_LEVEL_NAME, 0, 26));
    
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "16   Hz", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_00016_HZ_NAME, 0, 28, -14));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "32   Hz", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_00032_HZ_NAME, 0, 28, -14));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "62   Hz", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_00062_HZ_NAME, 0, 28, -14));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "126 Hz", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_00126_HZ_NAME, 0, 28, -14));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "250 Hz", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_00250_HZ_NAME, 0, 28, -14));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "500 Hz", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_00500_HZ_NAME, 0, 28, -14));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "1   kHz", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_01000_HZ_NAME, 0, 28, -14));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "2   kHz", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_02000_HZ_NAME, 0, 28, -14));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "4   kHz", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_04000_HZ_NAME, 0, 28, -14));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "8   kHz", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_08000_HZ_NAME, 0, 28, -14));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "16  kHz", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_16000_HZ_NAME, 0, 28, -14));
    
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "Mode", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_MODE_NAME, MidiDevice_QVGT.EqModeConfig4.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Low Frequency", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_5B_LOW_F_NAME, 20, 999));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Low Amplitude", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_5B_LOW_AMP_NAME, 0, 560));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Mid Low Frequency", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_5B_LOW_MID_F_NAME, 20, 500));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Mid Low Bandwidth", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_5B_LOW_MID_BW_NAME, 20, 255));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Mid Low Amplitude", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_5B_LOW_MID_AMP_NAME, 0, 560));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Low Frequency", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_LOW_F_NAME, 20, 999));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Low Amplitude", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_LOW_AMP_NAME, 0, 560));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Mid Frequency", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_MID_F_NAME, 200, 9999));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Mid Frequency", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_MID_BW_NAME, 20, 255));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Mid Amplitude", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_MID_AMP_NAME, 0, 560));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "High Frequency", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_HIGH_F_NAME, 2000, 18000));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "High Amplitude", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_HIGH_AMP_NAME, 0, 560));
    
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Low Frequency", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF5_LOW_F_NAME, 20, 999));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Low Amplitude", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF5_LOW_AMP_NAME, 0, 560));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Mid Frequency", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF5_MID_F_NAME, 200, 9999));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Mid Frequency", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF5_MID_BW_NAME, 20, 255));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Mid Amplitude", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF5_MID_AMP_NAME, 0, 560));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "High Frequency", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF5_HIGH_F_NAME, 2000, 18000));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "High Amplitude", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF5_HIGH_AMP_NAME, 0, 560));
    
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "Reso Gate Mode", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF7_RESO_GATE_MODE_NAME, MidiDevice_QVGT.EqResonatorGateMode.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Reso Decay", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF7_RESO_DECAY_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Reso 1 Tune", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF7_RESO_1_TUNE_NAME, 0, 60));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Reso 2 Tune", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF7_RESO_2_TUNE_NAME, 0, 60));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Reso 3 Tune", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF7_RESO_3_TUNE_NAME, 0, 60));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Reso 4 Tune", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF7_RESO_4_TUNE_NAME, 0, 60));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Reso 5 Tune", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF7_RESO_5_TUNE_NAME, 0, 60));
    
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
     || changes.containsKey (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_MODE_NAME)
     || changes.containsKey (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_MODE_NAME))
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
          setLayout (new GridLayout (9, 1, 5, 0));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_PRESET_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_MODE_NAME));
          final MidiDevice_QVGT.EqModeConfig1 mode =
            (MidiDevice_QVGT.EqModeConfig1) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_MODE_NAME);
          if (mode != null)
            switch (mode)
            {
              case EQ3:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_3B_LOW_F_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_3B_LOW_AMP_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_3B_MID_F_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_3B_MID_BW_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_3B_MID_AMP_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_3B_HIGH_F_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_3B_HIGH_AMP_NAME));
                break;
              case RES2_EQ1:
                setLayout (new GridLayout (7, 1, 5, 0));
                add (new JLabel ());
                add (new JLabel ());
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_RESO_MID_F_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_RESO_MID_BW_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_RESO_MID_AMP_NAME));
                break;
              default:
                throw new RuntimeException ();
            }
          break;
        }
        case C2_LES_DL_REV:
        {
          setLayout (new GridLayout (8, 1, 5, 0));
          add (new JLabel ());
          final JLabel label = new JLabel ("Leslie");
          label.setHorizontalAlignment (SwingConstants.CENTER);
          add (label);
          add (new JLabel ());
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF2_HIGH_ROTOR_LEVEL_NAME));
          break;
        }
        case C3_GEQ_DL:
        {
          setLayout (new GridLayout (12, 1, 5, 0));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_PRESET_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_00016_HZ_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_00032_HZ_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_00062_HZ_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_00126_HZ_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_00250_HZ_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_00500_HZ_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_01000_HZ_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_02000_HZ_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_04000_HZ_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_08000_HZ_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF3_16000_HZ_NAME));
          break;
        }
        case C4_5EQ_PCH_DL:
        {
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_PRESET_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_MODE_NAME));
          final MidiDevice_QVGT.EqModeConfig4 mode =
            (MidiDevice_QVGT.EqModeConfig4) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_MODE_NAME);
          if (mode != null)
            switch (mode)
            {
              case EQ5:
                setLayout (new GridLayout (9, 1, 5, 0));
                add (new JLabel ());
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_5B_LOW_F_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_5B_LOW_AMP_NAME));
                add (new JLabel ());
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_5B_LOW_MID_F_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_5B_LOW_MID_BW_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_5B_LOW_MID_AMP_NAME));
                break;
              case RES5_EQ3:
                setLayout (new GridLayout (12, 1, 5, 0));
                add (new JPanel ());
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_LOW_F_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_LOW_AMP_NAME));
                add (new JPanel ());
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_MID_F_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_MID_BW_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_MID_AMP_NAME));
                add (new JPanel ());
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_HIGH_F_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_HIGH_AMP_NAME));
                break;
              default:
                throw new RuntimeException ();
            }
          break;
        }
        case C5_3EQ_REV:
        {
          setLayout (new GridLayout (8, 1, 5, 0));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_PRESET_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF5_LOW_F_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF5_LOW_AMP_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF5_MID_F_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF5_MID_BW_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF5_MID_AMP_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF5_HIGH_F_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF5_HIGH_AMP_NAME));
          break;
        }
        case C6_RING_DL_REV:
        {
          setLayout (new GridLayout (1, 1, 5, 0));
          final JLabel label = new JLabel ("Not Available");
          label.setHorizontalAlignment (SwingConstants.CENTER);
          add (label);
          break;
        }
        case C7_RESO_DL_REV:
        {
          setLayout (new GridLayout (9, 1, 5, 0));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF7_RESO_GATE_MODE_NAME));
          add (new JLabel ());
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF7_RESO_DECAY_NAME));
          add (new JLabel ());
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF7_RESO_1_TUNE_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF7_RESO_2_TUNE_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF7_RESO_3_TUNE_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF7_RESO_4_TUNE_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF7_RESO_5_TUNE_NAME));
          break;
        }
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
