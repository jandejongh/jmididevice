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

/** A {@link JPanel} for monitoring and editing DISPLAY parameters of an Alesis Quadraverb GT {@link MidiDevice}.
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
final class JQVGTPanel_DELAY extends JPanel
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
  public JQVGTPanel_DELAY (final MidiDevice midiDevice)
  {
    super ();
    if (midiDevice == null || ! (midiDevice instanceof MidiDevice_QVGT))
      throw new IllegalArgumentException ();
    this.midiDevice = midiDevice;
    setLayout (new GridLayout (9, 1, 5, 0));
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "Mode", MidiDevice_QVGT.EDIT_BUFFER_DELAY_MODE_NAME, MidiDevice_QVGT.DelayMode.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "Mode", MidiDevice_QVGT.EDIT_BUFFER_DELAY_MODE_EXTENDED_NAME, MidiDevice_QVGT.DelayModeExtended.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "Input 1", MidiDevice_QVGT.EDIT_BUFFER_DELAY_INPUT1_NAME, MidiDevice_QVGT.DelayInput1.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "Input", MidiDevice_QVGT.EDIT_BUFFER_DELAY_INPUT_NAME, MidiDevice_QVGT.DelayInput.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Input Mix", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF14_IN_MIX_NAME, 0, 198));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Input Mix", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF267_IN_MIX_NAME, 0, 198));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Delay", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF1_MONO_DELAY_NAME, 1, 775));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Feedback", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF1_MONO_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "L Delay", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF1_STEREO_L_DELAY_NAME, 1, 375));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "L Feedback", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF1_STEREO_L_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "R Delay", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF1_STEREO_R_DELAY_NAME, 1, 375));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "R Feedback", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF1_STEREO_R_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Delay", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF1_PING_PONG_DELAY_NAME, 1, 375));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Feedback", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF1_PING_PONG_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Delay", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF26_MONO_DELAY_NAME, 1, 800));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Feedback", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF26_MONO_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "L Delay", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF26_STEREO_L_DELAY_NAME, 1, 400));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "L Feedback", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF26_STEREO_L_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "R Delay", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF26_STEREO_R_DELAY_NAME, 1, 400));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "R Feedback", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF26_STEREO_R_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Delay", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF26_PING_PONG_DELAY_NAME, 1, 400));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Feedback", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF26_PING_PONG_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Delay", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF3_MONO_DELAY_NAME, 1, 1500));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Feedback", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF3_MONO_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "L Delay", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF3_STEREO_L_DELAY_NAME, 1, 750));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "L Feedback", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF3_STEREO_L_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "R Delay", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF3_STEREO_R_DELAY_NAME, 1, 750));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "R Feedback", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF3_STEREO_R_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Delay", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF3_PING_PONG_DELAY_NAME, 1, 750));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Feedback", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF3_PING_PONG_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Delay", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MONO_DELAY_NAME, 1, 1470));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Feedback", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MONO_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "L Delay", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_STEREO_L_DELAY_NAME, 1, 705));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "L Feedback", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_STEREO_L_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "R Delay", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_STEREO_R_DELAY_NAME, 1, 705));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "R Feedback", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_STEREO_R_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Delay", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_PING_PONG_DELAY_NAME, 1, 705));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Feedback", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_PING_PONG_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "Tap", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_NUMBER_NAME, MidiDevice_QVGT.DelayTap.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Delay[1]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_1_DELAY_NAME, 0, 1470));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Volume[1]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_1_VOLUME_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Panning[1]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_1_PANNING_NAME, 0, 198));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Feedback[1]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_1_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Delay[2]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_2_DELAY_NAME, 0, 1470));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Volume[2]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_2_VOLUME_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Panning[2]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_2_PANNING_NAME, 0, 198));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Feedback[2]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_2_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Delay[3]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_3_DELAY_NAME, 0, 1470));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Volume[3]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_3_VOLUME_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Panning[3]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_3_PANNING_NAME, 0, 198));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Feedback[3]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_3_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Delay[4]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_4_DELAY_NAME, 0, 1470));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Volume[4]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_4_VOLUME_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Panning[4]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_4_PANNING_NAME, 0, 198));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Feedback[4]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_4_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Delay[5]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_5_DELAY_NAME, 0, 1470));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Volume[5]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_5_VOLUME_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Panning[5]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_5_PANNING_NAME, 0, 198));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Feedback[5]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_5_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Delay[6]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_6_DELAY_NAME, 0, 1470));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Volume[6]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_6_VOLUME_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Panning[6]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_6_PANNING_NAME, 0, 198));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Feedback[6]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_6_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Delay[7]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_7_DELAY_NAME, 0, 1470));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Volume[7]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_7_VOLUME_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Panning[7]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_7_PANNING_NAME, 0, 198));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Feedback[7]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_7_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Delay[8]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_8_DELAY_NAME, 0, 1470));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Volume[8]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_8_VOLUME_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Panning[8]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_8_PANNING_NAME, 0, 198));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Feedback[8]", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_8_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Master Feedback", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_MASTER_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Delay", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF7_MONO_DELAY_NAME, 1, 720));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Feedback", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF7_MONO_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "L Delay", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF7_STEREO_L_DELAY_NAME, 1, 320));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "L Feedback", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF7_STEREO_L_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "R Delay", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF7_STEREO_R_DELAY_NAME, 1, 320));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "R Feedback", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF7_STEREO_R_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Delay", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF7_PING_PONG_DELAY_NAME, 1, 320));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Feedback", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF7_PING_PONG_FEEDBACK_NAME, 0, 99));
    addMidiDeviceParameter (new JMidiDeviceParameter_Enum (midiDevice,
      "Sample Playback", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF8_SAMPLE_PLAYBACK_NAME, MidiDevice_QVGT.SamplePlayback.class));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Sample Start", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF8_SAMPLE_START_NAME, 0, 150));
    addMidiDeviceParameter (new JMidiDeviceParameter_Integer_Slider (midiDevice,
      "Sample Length", MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF8_SAMPLE_LENGTH_NAME, 5, 155));
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
     || changes.containsKey (MidiDevice_QVGT.EDIT_BUFFER_DELAY_MODE_EXTENDED_NAME)
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
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_MODE_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_INPUT1_NAME));
          add (new JLabel ());
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF14_IN_MIX_NAME));
          final MidiDevice_QVGT.DelayMode mode =
            (MidiDevice_QVGT.DelayMode) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_MODE_NAME);
          if (mode != null)
            switch (mode)
            {
              case MONO:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF1_MONO_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF1_MONO_FEEDBACK_NAME));
                add (new JLabel ());
                add (new JLabel ());
                add (new JLabel ());
                break;
              case STEREO:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF1_STEREO_L_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF1_STEREO_L_FEEDBACK_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF1_STEREO_R_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF1_STEREO_R_FEEDBACK_NAME));
                add (new JLabel ());
                break;
              case PING_PONG:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF1_PING_PONG_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF1_PING_PONG_FEEDBACK_NAME));
                add (new JLabel ());
                add (new JLabel ());
                add (new JLabel ());
                break;
              default:
                throw new RuntimeException ();
            }
          break;
        }
        case C2_LES_DL_REV:
        case C6_RING_DL_REV:
        {
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_MODE_NAME));
          add (new JLabel ());
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF267_IN_MIX_NAME));
          final MidiDevice_QVGT.DelayMode mode =
            (MidiDevice_QVGT.DelayMode) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_MODE_NAME);
          if (mode != null)
            switch (mode)
            {
              case MONO:
                add (new JLabel ());
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF26_MONO_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF26_MONO_FEEDBACK_NAME));
                add (new JLabel ());
                add (new JLabel ());
                add (new JLabel ());
                break;
              case STEREO:
                add (new JLabel ());
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF26_STEREO_L_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF26_STEREO_L_FEEDBACK_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF26_STEREO_R_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF26_STEREO_R_FEEDBACK_NAME));
                add (new JLabel ());
                break;
              case PING_PONG:
                add (new JLabel ());
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF26_PING_PONG_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF26_PING_PONG_FEEDBACK_NAME));
                add (new JLabel ());
                add (new JLabel ());
                add (new JLabel ());
                break;
              default:
                throw new RuntimeException ();
            }
          break;
        }
        case C3_GEQ_DL:
        {
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_MODE_NAME));
          add (new JLabel ());
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_INPUT_NAME));
          final MidiDevice_QVGT.DelayMode mode =
            (MidiDevice_QVGT.DelayMode) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_MODE_NAME);
          if (mode != null)
            switch (mode)
            {
              case MONO:
                add (new JLabel ());
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF3_MONO_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF3_MONO_FEEDBACK_NAME));
                add (new JLabel ());
                add (new JLabel ());
                add (new JLabel ());
                break;
              case STEREO:
                add (new JLabel ());
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF3_STEREO_L_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF3_STEREO_L_FEEDBACK_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF3_STEREO_R_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF3_STEREO_R_FEEDBACK_NAME));
                add (new JLabel ());
                break;
              case PING_PONG:
                add (new JLabel ());
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF3_PING_PONG_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF3_PING_PONG_FEEDBACK_NAME));
                add (new JLabel ());
                add (new JLabel ());
                add (new JLabel ());
                break;
              default:
                throw new RuntimeException ();
            }
          break;
        }
        case C4_5EQ_PCH_DL:
        {
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_MODE_EXTENDED_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_INPUT1_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF14_IN_MIX_NAME));
          final MidiDevice_QVGT.DelayModeExtended mode =
            (MidiDevice_QVGT.DelayModeExtended) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_MODE_EXTENDED_NAME);
          if (mode != null)
            switch (mode)
            {
              case MONO:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MONO_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MONO_FEEDBACK_NAME));
                add (new JLabel ());
                add (new JLabel ());
                add (new JLabel ());
                break;
              case STEREO:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_STEREO_L_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_STEREO_L_FEEDBACK_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_STEREO_R_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_STEREO_R_FEEDBACK_NAME));
                add (new JLabel ());
                break;
              case PING_PONG:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_PING_PONG_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_PING_PONG_FEEDBACK_NAME));
                add (new JLabel ());
                add (new JLabel ());
                add (new JLabel ());
                break;
              case MULTI_TAP:
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_NUMBER_NAME));
                final MidiDevice_QVGT.DelayTap delayTap =
                  (MidiDevice_QVGT.DelayTap) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_NUMBER_NAME);
                if (delayTap != null)
                  switch (delayTap)
                  {
                    case TAP_1:
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_1_DELAY_NAME));
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_1_VOLUME_NAME));
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_1_PANNING_NAME));
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_1_FEEDBACK_NAME));
                      break;
                    case TAP_2:
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_2_DELAY_NAME));
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_2_VOLUME_NAME));
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_2_PANNING_NAME));
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_2_FEEDBACK_NAME));
                      break;
                    case TAP_3:
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_3_DELAY_NAME));
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_3_VOLUME_NAME));
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_3_PANNING_NAME));
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_3_FEEDBACK_NAME));
                      break;
                    case TAP_4:
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_4_DELAY_NAME));
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_4_VOLUME_NAME));
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_4_PANNING_NAME));
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_4_FEEDBACK_NAME));
                      break;
                    case TAP_5:
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_5_DELAY_NAME));
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_5_VOLUME_NAME));
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_5_PANNING_NAME));
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_5_FEEDBACK_NAME));
                      break;
                    case TAP_6:
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_6_DELAY_NAME));
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_6_VOLUME_NAME));
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_6_PANNING_NAME));
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_6_FEEDBACK_NAME));
                      break;
                    case TAP_7:
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_7_DELAY_NAME));
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_7_VOLUME_NAME));
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_7_PANNING_NAME));
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_7_FEEDBACK_NAME));
                      break;
                    case TAP_8:
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_8_DELAY_NAME));
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_8_VOLUME_NAME));
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_8_PANNING_NAME));
                      add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_8_FEEDBACK_NAME));
                      break;
                    default:
                      throw new RuntimeException ();
                  }
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF4_MULTITAP_MASTER_FEEDBACK_NAME));
                break;
              default:
                throw new RuntimeException ();
            }
          break;
        }
        case C5_3EQ_REV:
        {
          add (new JLabel ());
          add (new JLabel ());
          add (new JLabel ());
          add (new JLabel ());
          final JLabel label = new JLabel ("Not Available");
          label.setHorizontalAlignment (SwingConstants.CENTER);
          add (label);
          add (new JLabel ());
          add (new JLabel ());
          add (new JLabel ());
          add (new JLabel ());
          add (new JLabel ());
          break;
        }
        case C7_RESO_DL_REV:
        {
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_MODE_NAME));
          add (new JLabel ());
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF267_IN_MIX_NAME));
          final MidiDevice_QVGT.DelayMode mode =
            (MidiDevice_QVGT.DelayMode) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_MODE_NAME);
          if (mode != null)
            switch (mode)
            {
              case MONO:
                add (new JLabel ());
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF7_MONO_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF7_MONO_FEEDBACK_NAME));
                add (new JLabel ());
                add (new JLabel ());
                add (new JLabel ());
                break;
              case STEREO:
                add (new JLabel ());
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF7_STEREO_L_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF7_STEREO_L_FEEDBACK_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF7_STEREO_R_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF7_STEREO_R_FEEDBACK_NAME));
                add (new JLabel ());
                break;
              case PING_PONG:
                add (new JLabel ());
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF7_PING_PONG_DELAY_NAME));
                add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF7_PING_PONG_FEEDBACK_NAME));
                add (new JLabel ());
                add (new JLabel ());
                add (new JLabel ());
                break;
              default:
                throw new RuntimeException ();
            }
          break;
        }
        case C8_SAMPLING:
        {
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF8_SAMPLE_PLAYBACK_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF8_SAMPLE_START_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF8_SAMPLE_LENGTH_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF8_AUDIO_TRIGGER_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF8_MIDI_TRIGGER_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF8_MIDI_LOW_LIMIT_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF8_MIDI_BASE_NOTE_NAME));
          add (this.parameterMap.get (MidiDevice_QVGT.EDIT_BUFFER_DELAY_CF8_MIDI_HIGH_LIMIT_NAME));
          add (new JLabel ());
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
