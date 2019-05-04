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
import org.javajdj.jservice.midi.device.swing.JMidiDeviceParameter_Enum;
import org.javajdj.jservice.midi.device.swing.JMidiDeviceParameter_Integer_Slider;
import org.javajdj.swing.SwingUtilsJdJ;

/** A {@link JPanel} for monitoring and editing (most) DELAY parameters of an Alesis Quadraverb GT {@link MidiDevice}.
 * 
 * <p>
 * Auxiliary file to {@link JQVGTPanel}.
 * 
 * <p>
 * Due to space limitations on the GUI, the DELAY parameters are divided among two panels,
 * {@link JQVGTPanel_DELAY1} and {@link JQVGTPanel_DELAY2}.
 * 
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 * @see MidiDevice
 * @see MidiDevice_QVGT
 * 
 */
final class JQVGTPanel_DELAY1 extends JPanel
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
  public JQVGTPanel_DELAY1 (final MidiDevice midiDevice)
  {
    super ();
    if (midiDevice == null || ! (midiDevice instanceof MidiDevice_QVGT))
      throw new IllegalArgumentException ();
    this.midiDevice = midiDevice;
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "Mode", MidiDevice_QVGT.EDIT_BUFFER_DELAY_MODE_NAME, MidiDevice_QVGT.DelayMode.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "Mode", MidiDevice_QVGT.EDIT_BUFFER_DELAY_MODE_EXTENDED_NAME, MidiDevice_QVGT.DelayModeExtended.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "Input 1", MidiDevice_QVGT.EDIT_BUFFER_DELAY_INPUT1_NAME, MidiDevice_QVGT.DelayInput1.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "Input", MidiDevice_QVGT.EDIT_BUFFER_DELAY_INPUT_NAME, MidiDevice_QVGT.DelayInput.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Input Mix", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF14_IN_MIX_NAME, 0, 198, -99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Input Mix", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF267_IN_MIX_NAME, 0, 198, -99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Delay [ms]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF1_MONO_DELAY_NAME, 1, 775));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Feedback [%]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF1_MONO_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "L Delay [ms]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF1_STEREO_L_DELAY_NAME, 1, 375));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "L Feedback [%]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF1_STEREO_L_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "R Delay [ms]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF1_STEREO_R_DELAY_NAME, 1, 375));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "R Feedback [%]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF1_STEREO_R_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Delay [ms]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF1_PING_PONG_DELAY_NAME, 1, 375));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Feedback [%]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF1_PING_PONG_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Delay [ms]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF26_MONO_DELAY_NAME, 1, 800));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Feedback [%]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF26_MONO_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "L Delay [ms]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF26_STEREO_L_DELAY_NAME, 1, 400));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "L Feedback [%]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF26_STEREO_L_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "R Delay [ms]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF26_STEREO_R_DELAY_NAME, 1, 400));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "R Feedback [%]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF26_STEREO_R_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Delay [ms]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF26_PING_PONG_DELAY_NAME, 1, 400));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Feedback [%]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF26_PING_PONG_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Delay [ms]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF3_MONO_DELAY_NAME, 1, 1500));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Feedback [%]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF3_MONO_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "L Delay [ms]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF3_STEREO_L_DELAY_NAME, 1, 750));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "L Feedback [%]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF3_STEREO_L_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "R Delay [ms]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF3_STEREO_R_DELAY_NAME, 1, 750));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "R Feedback [%]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF3_STEREO_R_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Delay [ms]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF3_PING_PONG_DELAY_NAME, 1, 750));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Feedback [%]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF3_PING_PONG_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Delay [ms]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MONO_DELAY_NAME, 1, 1470));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Feedback [%]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MONO_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "L Delay [ms]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_STEREO_L_DELAY_NAME, 1, 705));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "L Feedback [%]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_STEREO_L_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "R Delay [ms]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_STEREO_R_DELAY_NAME, 1, 705));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "R Feedback [%]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_STEREO_R_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Delay [ms]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_PING_PONG_DELAY_NAME, 1, 705));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Feedback [%]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_PING_PONG_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Master Feedback [%]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_MASTER_FEEDBACK_NAME, 0, 99));
    final JMidiDeviceParameter<Integer> multiTapTotalDelayParameter = new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Total Tap Delay [ms]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TOTAL_DELAY_NAME, 8, 1470);
    multiTapTotalDelayParameter.setReadOnly (true);
    addMidiDeviceParameter (multiTapTotalDelayParameter);
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Delay [ms]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF7_MONO_DELAY_NAME, 1, 720));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Feedback [%]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF7_MONO_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "L Delay [ms]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF7_STEREO_L_DELAY_NAME, 1, 320));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "L Feedback [%]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF7_STEREO_L_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "R Delay [ms]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF7_STEREO_R_DELAY_NAME, 1, 320));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "R Feedback [%]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF7_STEREO_R_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Delay [ms]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF7_PING_PONG_DELAY_NAME, 1, 320));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Feedback [%]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF7_PING_PONG_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "Sample Playback", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF8_SAMPLE_PLAYBACK_NAME, MidiDevice_QVGT.SamplePlayback.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Sample Start [s]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF8_SAMPLE_START_NAME, 0, 150, 0, 0.01, "%3.2f"));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Sample Length [s]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF8_SAMPLE_LENGTH_NAME, 5, 155, 0, 0.01, "%3.2f"));
    addMidiDeviceParameter (new JMidiDeviceParameter_Boolean (midiDevice,
      "Audio Trigger", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF8_AUDIO_TRIGGER_NAME));
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "MIDI Trigger", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF8_MIDI_TRIGGER_NAME, MidiDevice_QVGT.MidiTrigger.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "MIDI Low Note", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF8_MIDI_LOW_LIMIT_NAME, 0, 127));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "MIDI Base Note", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF8_MIDI_BASE_NOTE_NAME, 0, 127));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "MIDI High Note", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF8_MIDI_HIGH_LIMIT_NAME, 0, 127));
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
     || changes.containsKey (MidiDevice_QVGT.EDIT_BUFFER_DELAY_MODE_NAME)
     || changes.containsKey (MidiDevice_QVGT.EDIT_BUFFER_DELAY_MODE_EXTENDED_NAME))
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
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_MODE_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_INPUT1_NAME));
          final JPanel input2Panel = new JPanel ();
          input2Panel.setLayout (new GridLayout (1, 2, 5, 5));
          input2Panel.add (new JLabel ("Input 2"));
          input2Panel.add (new JLabel ("PITCH"));
          add (input2Panel);
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF14_IN_MIX_NAME));
          add (new JLabel ());
          final MidiDevice_QVGT.DelayMode mode =
            (MidiDevice_QVGT.DelayMode) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_MODE_NAME);
          if (mode != null)
            switch (mode)
            {
              case MONO:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF1_MONO_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF1_MONO_FEEDBACK_NAME));
                break;
              case STEREO:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF1_STEREO_L_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF1_STEREO_L_FEEDBACK_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF1_STEREO_R_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF1_STEREO_R_FEEDBACK_NAME));
                break;
              case PING_PONG:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF1_PING_PONG_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF1_PING_PONG_FEEDBACK_NAME));
                break;
              default:
                throw new RuntimeException ();
            }
          break;
        }
        case C2_LES_DL_REV:
        case C6_RING_DL_REV:
        {
          setLayout (new GridLayout (9, 1, 5, 5));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_MODE_NAME));
          final JPanel input1Panel = new JPanel ();
          input1Panel.setLayout (new GridLayout (1, 2, 5, 5));
          input1Panel.add (new JLabel ("Input 1"));
          input1Panel.add (new JLabel ("PREAMP"));
          add (input1Panel);
          final JPanel input2Panel = new JPanel ();
          input2Panel.setLayout (new GridLayout (1, 2, 5, 5));
          input2Panel.add (new JLabel ("Input 2"));
          switch (configuration)
          {
            case C2_LES_DL_REV:
              input2Panel.add (new JLabel ("LESLIE"));
              break;
            case C6_RING_DL_REV:
              input2Panel.add (new JLabel ("RING MODULATOR"));
              break;
            default:
              throw new RuntimeException ();
          }
          add (input2Panel);
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF267_IN_MIX_NAME));
          add (new JLabel ());
          final MidiDevice_QVGT.DelayMode mode =
            (MidiDevice_QVGT.DelayMode) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_MODE_NAME);
          if (mode != null)
            switch (mode)
            {
              case MONO:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF26_MONO_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF26_MONO_FEEDBACK_NAME));
                break;
              case STEREO:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF26_STEREO_L_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF26_STEREO_L_FEEDBACK_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF26_STEREO_R_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF26_STEREO_R_FEEDBACK_NAME));
                break;
              case PING_PONG:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF26_PING_PONG_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF26_PING_PONG_FEEDBACK_NAME));
                break;
              default:
                throw new RuntimeException ();
            }
          break;
        }
        case C3_GEQ_DL:
        {
          setLayout (new GridLayout (9, 1, 5, 5));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_MODE_NAME));
          add (new JLabel ());
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_INPUT_NAME));
          add (new JLabel ());
          final MidiDevice_QVGT.DelayMode mode =
            (MidiDevice_QVGT.DelayMode) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_MODE_NAME);
          if (mode != null)
            switch (mode)
            {
              case MONO:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF3_MONO_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF3_MONO_FEEDBACK_NAME));
                break;
              case STEREO:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF3_STEREO_L_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF3_STEREO_L_FEEDBACK_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF3_STEREO_R_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF3_STEREO_R_FEEDBACK_NAME));
                break;
              case PING_PONG:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF3_PING_PONG_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF3_PING_PONG_FEEDBACK_NAME));
                break;
              default:
                throw new RuntimeException ();
            }
          break;
        }
        case C4_5EQ_PCH_DL:
        {
          setLayout (new GridLayout (9, 1, 5, 5));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_MODE_EXTENDED_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_INPUT1_NAME));
          final JPanel input2Panel = new JPanel ();
          input2Panel.setLayout (new GridLayout (1, 2, 5, 5));
          input2Panel.add (new JLabel ("Input 2"));
          input2Panel.add (new JLabel ("PITCH"));
          add (input2Panel);
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF14_IN_MIX_NAME));
          add (new JLabel ());
          final MidiDevice_QVGT.DelayModeExtended mode =
            (MidiDevice_QVGT.DelayModeExtended) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_MODE_EXTENDED_NAME);
          if (mode != null)
            switch (mode)
            {
              case MONO:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MONO_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MONO_FEEDBACK_NAME));
                break;
              case STEREO:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_STEREO_L_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_STEREO_L_FEEDBACK_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_STEREO_R_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_STEREO_R_FEEDBACK_NAME));
                break;
              case PING_PONG:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_PING_PONG_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_PING_PONG_FEEDBACK_NAME));
                break;
              case MULTI_TAP:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_MASTER_FEEDBACK_NAME));
                add (new JLabel ());
                final JLabel label = new JLabel ("Total TAP Delay cannot exceed 1470 ms!");
                label.setHorizontalAlignment (SwingConstants.CENTER);
                add (label);
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TOTAL_DELAY_NAME));
                break;
              default:
                throw new RuntimeException ();
            }
          break;
        }
        case C5_3EQ_REV:
        {
          setLayout (new GridLayout (1, 1, 5, 5));
          final JLabel label = new JLabel ("Not Available");
          label.setHorizontalAlignment (SwingConstants.CENTER);
          add (label);
          break;
        }
        case C7_RESO_DL_REV:
        {


          setLayout (new GridLayout (9, 1, 5, 5));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_MODE_NAME));
          final JPanel input1Panel = new JPanel ();
          input1Panel.setLayout (new GridLayout (1, 2, 5, 5));
          input1Panel.add (new JLabel ("Input 1"));
          input1Panel.add (new JLabel ("PREAMP"));
          add (input1Panel);
          final JPanel input2Panel = new JPanel ();
          input2Panel.setLayout (new GridLayout (1, 2, 5, 5));
          input2Panel.add (new JLabel ("Input 2"));
          input2Panel.add (new JLabel ("RESONATORS"));
          add (input2Panel);
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF267_IN_MIX_NAME));
          add (new JLabel ());
          final MidiDevice_QVGT.DelayMode mode =
            (MidiDevice_QVGT.DelayMode) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_MODE_NAME);
          if (mode != null)
            switch (mode)
            {
              case MONO:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF7_MONO_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF7_MONO_FEEDBACK_NAME));
                break;
              case STEREO:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF7_STEREO_L_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF7_STEREO_L_FEEDBACK_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF7_STEREO_R_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF7_STEREO_R_FEEDBACK_NAME));
                break;
              case PING_PONG:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF7_PING_PONG_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF7_PING_PONG_FEEDBACK_NAME));
                break;
              default:
                throw new RuntimeException ();
            }
          break;
        }
        case C8_SAMPLING:
        {
          setLayout (new GridLayout (8, 1, 5, 5));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF8_SAMPLE_PLAYBACK_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF8_SAMPLE_START_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF8_SAMPLE_LENGTH_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF8_AUDIO_TRIGGER_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF8_MIDI_TRIGGER_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF8_MIDI_LOW_LIMIT_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF8_MIDI_BASE_NOTE_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF8_MIDI_HIGH_LIMIT_NAME));
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
