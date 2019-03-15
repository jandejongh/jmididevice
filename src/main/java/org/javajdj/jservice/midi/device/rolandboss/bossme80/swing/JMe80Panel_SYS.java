package org.javajdj.jservice.midi.device.rolandboss.bossme80.swing;

import java.awt.GridLayout;
import javax.swing.JPanel;
import org.javajdj.jservice.midi.device.rolandboss.bossme80.MidiDevice_Me80;
import org.javajdj.jservice.midi.device.swing.JMidiDeviceBooleanParameter;
import org.javajdj.jservice.midi.device.swing.JMidiDeviceEnumParameter;
import org.javajdj.jservice.midi.device.swing.JMidiDeviceIntParameter_Slider;
import org.javajdj.jservice.midi.device.MidiDevice;

/**
 *
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 */
public class JMe80Panel_SYS
  extends JPanel
{
  
  public JMe80Panel_SYS (final MidiDevice midiDevice)
  {
    super ();
    setLayout (new GridLayout (10, 1, 2, 2));
    add (new JMidiDeviceEnumParameter (midiDevice,
      "Knob Mode", MidiDevice_Me80.SY_KNOB_MODE_NAME, MidiDevice_Me80.KnobMode.class));
    add (new JMidiDeviceBooleanParameter (midiDevice,
      "Auto Off", MidiDevice_Me80.SY_AUTO_OFF_SWITCH_NAME));
    add (new JMidiDeviceBooleanParameter (midiDevice,
      "Output While Tuning", MidiDevice_Me80.SY_TUNER_MUTE_NAME));
    add (new JMidiDeviceEnumParameter (midiDevice,
      "Bank Change Mode", MidiDevice_Me80.SY_PATCH_CHANGE_MODE_NAME, MidiDevice_Me80.BankChangeMode.class));    
    add (new JMidiDeviceEnumParameter (midiDevice,
      "Manual Control Ops", MidiDevice_Me80.SY_MANUAL_CHANGE_MODE_NAME, MidiDevice_Me80.ManualControlOps.class));
    final JMidiDeviceIntParameter_Slider jUsbOutputLevel =
      new JMidiDeviceIntParameter_Slider (midiDevice,
        "USB Output Level", MidiDevice_Me80.SY_USB_OUTPUT_LEVEL_NAME, 0, 9);
    jUsbOutputLevel.getSlider ().setPaintTicks (false);
    jUsbOutputLevel.getSlider ().setPaintLabels (false);
    add (jUsbOutputLevel);
    add (new JMidiDeviceBooleanParameter (midiDevice,
      "USB Loopback", MidiDevice_Me80.SY_USB_LOOPBACK_NAME));
    add (new JMidiDeviceBooleanParameter (midiDevice,
      "USB Dry Rec",  MidiDevice_Me80.SY_USB_DRY_REC_NAME));
    add (new JMidiDeviceEnumParameter (midiDevice,
      "MIDI Channel", MidiDevice_Me80.SY_USB_MIDI_CH_NAME, MidiDevice_Me80.MidiChannel.class));
    add (new JMidiDeviceEnumParameter (midiDevice,
      "Tuner Pitch", MidiDevice_Me80.SY_TUNER_PITCH_NAME, MidiDevice_Me80.TunerPitch.class));
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
}
