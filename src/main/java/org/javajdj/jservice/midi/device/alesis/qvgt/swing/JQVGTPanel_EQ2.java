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
 * @see JQVGTPanel_EQ1
 * 
 */
final class JQVGTPanel_EQ2 extends JPanel
{

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // LOGGING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private static final Logger LOG = Logger.getLogger (JQVGTPanel_EQ2.class.getName ());
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTORS / FACTORIES / CLONING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public JQVGTPanel_EQ2 (final MidiDevice midiDevice)
  {
    super ();
    if (midiDevice == null || ! (midiDevice instanceof MidiDevice_QVGT))
      throw new IllegalArgumentException ();
    this.midiDevice = midiDevice;
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Reso 1 Tune", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_RESO_1_TUNE_NAME, 0, 60));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Reso 1 Decay", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_RESO_1_DECAY_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Reso 1 Amp", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_RESO_1_AMP_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Reso 2 Tune", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_RESO_2_TUNE_NAME, 0, 60));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Reso 2 Decay", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_RESO_2_DECAY_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Reso 2 Amp", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_RESO_2_AMP_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Mid Frequency", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_5B_MID_F_NAME, 200, 9999));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Mid Bandwidth", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_5B_MID_BW_NAME, 20, 255));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Mid Amplitude", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_5B_MID_AMP_NAME, 0, 560));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Mid High Frequency", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_5B_HIGH_MID_F_NAME, 2000, 18000));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Mid High Bandwidth", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_5B_HIGH_MID_BW_NAME, 20, 255));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Mid High Amplitude", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_5B_HIGH_MID_AMP_NAME, 0, 560));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "High Frequency", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_5B_HIGH_F_NAME, 2000, 18000));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "High Amplitude", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_5B_HIGH_AMP_NAME, 0, 560));
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "Reso Number", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_NUMBER_NAME, MidiDevice_QVGT.EqResonatorConfig4.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Reso 1 Tune", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_1_TUNE_NAME, 0, 60));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Reso 1 Decay", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_1_DECAY_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Reso 1 Amp", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_1_AMP_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Reso 2 Tune", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_2_TUNE_NAME, 0, 60));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Reso 2 Decay", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_2_DECAY_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Reso 2 Amp", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_2_AMP_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Reso 3 Tune", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_3_TUNE_NAME, 0, 60));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Reso 3 Decay", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_3_DECAY_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Reso 3 Amp", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_3_AMP_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Reso 4 Tune", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_4_TUNE_NAME, 0, 60));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Reso 4 Decay", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_4_DECAY_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Reso 4 Amp", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_4_AMP_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Reso 5 Tune", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_5_TUNE_NAME, 0, 60));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Reso 5 Decay", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_5_DECAY_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Reso 5 Amp", MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_5_AMP_NAME, 0, 99));
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
     || changes.containsKey (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_MODE_NAME)
     || changes.containsKey (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_NUMBER_NAME))
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
          final MidiDevice_QVGT.EqModeConfig1 mode =
            (MidiDevice_QVGT.EqModeConfig1) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_MODE_NAME);
          if (mode != null)
            switch (mode)
            {
              case EQ3:
              {
                setLayout (new GridLayout (1, 1, 5, 0));
                final JLabel label = new JLabel ("Not Available");
                label.setHorizontalAlignment (SwingConstants.CENTER);
                add (label);
                break;
              }
              case RES2_EQ1:
              {
                setLayout (new GridLayout (7, 1, 5, 0));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_RESO_1_TUNE_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_RESO_1_DECAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_RESO_1_AMP_NAME));
                add (new JLabel ());
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_RESO_2_TUNE_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_RESO_2_DECAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF1_RESO_2_AMP_NAME));
                break;
              }
              default:
                throw new RuntimeException ();
            }
          break;
        }
        case C2_LES_DL_REV:
        {
          setLayout (new GridLayout (1, 1, 5, 0));
          final JLabel label = new JLabel ("Not Available");
          label.setHorizontalAlignment (SwingConstants.CENTER);
          add (label);
          break;
        }
        case C3_GEQ_DL:
        {
          setLayout (new GridLayout (1, 1, 5, 0));
          final JLabel label = new JLabel ("Not Available");
          label.setHorizontalAlignment (SwingConstants.CENTER);
          add (label);
          break;
        }
        case C4_5EQ_PCH_DL:
        {
          final MidiDevice_QVGT.EqModeConfig4 mode =
            (MidiDevice_QVGT.EqModeConfig4) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_MODE_NAME);
          if (mode != null)
            switch (mode)
            {
              case EQ5:
              {
                setLayout (new GridLayout (10, 1, 5, 0));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_5B_MID_F_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_5B_MID_BW_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_5B_MID_AMP_NAME));
                add (new JLabel ());
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_5B_HIGH_MID_F_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_5B_HIGH_MID_BW_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_5B_HIGH_MID_AMP_NAME));
                add (new JLabel ());
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_5B_HIGH_F_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_5B_HIGH_AMP_NAME));
                break;
              }
              case RES5_EQ3:
              {
                setLayout (new GridLayout (5, 1, 5, 0));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_NUMBER_NAME));
                add (new JLabel ());
                final MidiDevice_QVGT.EqResonatorConfig4 resonator =
                  (MidiDevice_QVGT.EqResonatorConfig4) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_NUMBER_NAME);
                if (resonator != null)
                  switch (resonator)
                  {
                    case RESONATOR_1:
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_1_TUNE_NAME));
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_1_DECAY_NAME));
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_1_AMP_NAME));
                      break;
                    case RESONATOR_2:
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_2_TUNE_NAME));
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_2_DECAY_NAME));
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_2_AMP_NAME));
                      break;
                    case RESONATOR_3:
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_3_TUNE_NAME));
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_3_DECAY_NAME));
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_3_AMP_NAME));
                      break;
                    case RESONATOR_4:
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_4_TUNE_NAME));
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_4_DECAY_NAME));
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_4_AMP_NAME));
                      break;
                    case RESONATOR_5:
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_5_TUNE_NAME));
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_5_DECAY_NAME));
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_EQ_CF4_RESO_5_AMP_NAME));
                      break;
                    default:
                      add (new JLabel ("Error - Wrong resonator value: " + resonator));
                      break;
                  }
                else
                  add (new JLabel ("Error - Null resonator number!"));
                break;
              }
              default:
                throw new RuntimeException ();
            }
          break;
        }
        case C5_3EQ_REV:
        {
          setLayout (new GridLayout (1, 1, 5, 0));
          final JLabel label = new JLabel ("Not Available");
          label.setHorizontalAlignment (SwingConstants.CENTER);
          add (label);
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
          setLayout (new GridLayout (1, 1, 5, 0));
          final JLabel label = new JLabel ("Not Available");
          label.setHorizontalAlignment (SwingConstants.CENTER);
          add (label);
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
