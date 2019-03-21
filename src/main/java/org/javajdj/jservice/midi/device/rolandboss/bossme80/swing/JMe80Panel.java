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
package org.javajdj.jservice.midi.device.rolandboss.bossme80.swing;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import org.javajdj.jservice.midi.MidiService;
import org.javajdj.jservice.midi.device.MidiDevice;
import org.javajdj.jservice.midi.device.rolandboss.bossme80.MidiDevice_Me80;
import org.javajdj.jservice.midi.device.swing.JMidiDeviceParameter_Boolean;
import org.javajdj.jservice.midi.device.swing.JMidiDeviceParameter_Enum;
import org.javajdj.jservice.midi.device.swing.JMidiDeviceParameter_Integer_Slider;
import org.javajdj.jservice.midi.swing.JMidiService;
import org.javajdj.jservice.midi.swing.JRawMidiService;

/** A {@link JPanel} for controlling and monitoring a Boss ME-80.
 *
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 */
public class JMe80Panel
  extends JPanel
{

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // SERIALIZATION
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private static final long serialVersionUID = -2670414054463543149L;

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // LOGGING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private static final Logger LOG = Logger.getLogger (JMe80Panel.class.getName ());

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTORS / FACTORIES / CLONING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public JMe80Panel (final MidiService midiService, final MidiDevice midiDevice)
  {
    
    super (); 
    
    if (midiDevice == null)
      throw new IllegalArgumentException ();
    this.midiDevice = midiDevice;
    
    setLayout (new GridLayout (3, 5, 6, 6));
    
    final JPanel midiServicePanel;
    if (midiService == null)
      midiServicePanel = new JMidiService ();
    else if (midiService instanceof JMidiService)
      midiServicePanel = (JPanel) midiService;
    else if (midiService instanceof JRawMidiService)
      midiServicePanel = (JPanel) midiService;
    else
      midiServicePanel = new JRawMidiService (midiService);
    addBorderSYSGroup (midiServicePanel, "MIDI");
    add (midiServicePanel);

    final JPanel jMe80JPanel = new JMe80Panel_Device (midiDevice);
    addBorderSYSGroup (jMe80JPanel, "ME-80 [Device]");
    add (jMe80JPanel);

    final JPanel jMe80JPanel_SYS = new JMe80Panel_SYS (midiDevice);
    addBorderSYSGroup (jMe80JPanel_SYS, "ME-80 [System]");
    add (jMe80JPanel_SYS);

    final JPanel jMe80LibrarianPanel = new JMe80Panel_LIB (midiDevice);
    addBorderLIBGroup (jMe80LibrarianPanel, "ME-80 [Lib]");
    add (jMe80LibrarianPanel);
    
    final JPanel jMe80SparePanel = new JMe80Panel_SPARE (midiDevice);
    addBorderLIBGroup (jMe80SparePanel, "ME-80 [Spare]");
    add (jMe80SparePanel);
    
    final JPanel jMe80PatchPanel = new JMe80Panel_PatchSelector (midiDevice);
    addBorderSYSGroup (jMe80PatchPanel, "ME-80 [Patch]");
    add (jMe80PatchPanel);

    final JPanel jMe80JPanel_COMP = new JMe80Panel_COMP (midiDevice);
    addBorderFXGroup (jMe80JPanel_COMP, "COMP");
    add (jMe80JPanel_COMP);

    final JPanel jMe80JPanel_OD_DS = new JMe80Panel_OD_DS (midiDevice);
    addBorderFXGroup (jMe80JPanel_OD_DS, "OD/DS");
    add (jMe80JPanel_OD_DS);
    
    final JPanel jMe80JPanel_AMP = new JMe80Panel_AMP (midiDevice);
    addBorderFXGroup (jMe80JPanel_AMP, "AMP");
    add (jMe80JPanel_AMP);
    
    final JPanel jMe80JPanel_MISC = new JPanel ();
    // jMe80JPanel_MISC.setLayout (new GridLayout (3, 1, 5, 5));
    jMe80JPanel_MISC.setLayout (new BoxLayout (jMe80JPanel_MISC, BoxLayout.Y_AXIS));
    {
      
      final JPanel jMe80JPanel_PEDAL_FX = new JMe80Panel_PEDAL_FX (midiDevice);
      addBorderFXGroup (jMe80JPanel_PEDAL_FX, "PEDAL FX");
      jMe80JPanel_MISC.add (jMe80JPanel_PEDAL_FX);
    
      final JPanel jMe80JPanel_NS = new JMe80Panel_NS (midiDevice);
      addBorderFXGroup (jMe80JPanel_NS, "NS");
      jMe80JPanel_MISC.add (jMe80JPanel_NS);

      final JPanel jMe80JPanel_FOOT_VOLUME = new JMe80Panel_FOOT_VOLUME (midiDevice);
      addBorderFXGroup (jMe80JPanel_FOOT_VOLUME, "FOOT VOLUME");
      jMe80JPanel_MISC.add (jMe80JPanel_FOOT_VOLUME);

    }
    // addBorderFXGroup (jMe80JPanel_MISC, "MISC");
    add (jMe80JPanel_MISC);
    
    final JPanel jMe80JPanel_MOD = new JMe80Panel_MOD (midiDevice);
    addBorderFXGroup (jMe80JPanel_MOD, "MOD");
    add (jMe80JPanel_MOD);

    final JPanel jMe80JPanel_EQ_FX2 = new JMe80Panel_EQ_FX2 (midiDevice);
    addBorderFXGroup (jMe80JPanel_EQ_FX2, "EQ/FX2");
    add (jMe80JPanel_EQ_FX2);

    final JPanel jMe80JPanel_DELAY = new JMe80Panel_DELAY (midiDevice);
    addBorderFXGroup (jMe80JPanel_DELAY, "DELAY");
    add (jMe80JPanel_DELAY);

    final JPanel jMe80JPanel_REVERB = new JMe80Panel_REVERB (midiDevice);
    addBorderFXGroup (jMe80JPanel_REVERB, "REVERB");
    add (jMe80JPanel_REVERB);
    
    final JPanel jMe80JPanel_CTL = new JMe80Panel_CTL (midiDevice);
    addBorderFXGroup (jMe80JPanel_CTL, "CTL");
    add (jMe80JPanel_CTL);
    
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MIDI DEVICE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final MidiDevice midiDevice;
  
  public final MidiDevice getMidiDevice ()
  {
    return this.midiDevice;
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // BORDERS
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final static Color SYS_GROUP_BORDER_COLOR = Color.BLACK;
  
  private void addBorderSYSGroup (final JPanel sysGroupPanel, final String title)
  {
    if (sysGroupPanel == null || title == null)
      throw new IllegalArgumentException ();
    final Border lineBorder = BorderFactory.createLineBorder (SYS_GROUP_BORDER_COLOR, 2, true);
    final Border border = BorderFactory.createTitledBorder (lineBorder, title);
    sysGroupPanel.setBorder (border);
  }
  
  private final static Color LIB_GROUP_BORDER_COLOR = Color.YELLOW.darker ();
  
  private void addBorderLIBGroup (final JPanel libGroupPanel, final String title)
  {
    if (libGroupPanel == null || title == null)
      throw new IllegalArgumentException ();
    final Border lineBorder = BorderFactory.createLineBorder (LIB_GROUP_BORDER_COLOR, 2, true);
    final Border border = BorderFactory.createTitledBorder (lineBorder, title);
    libGroupPanel.setBorder (border);
  }
  
  private final static Color FX_GROUP_BORDER_COLOR = Color.BLUE;
  
  private void addBorderFXGroup (final JPanel fxGroupPanel, final String title)
  {
    if (fxGroupPanel == null || title == null)
      throw new IllegalArgumentException ();
    final Border lineBorder = BorderFactory.createLineBorder (FX_GROUP_BORDER_COLOR, 2, true);
    final Border border = BorderFactory.createTitledBorder (lineBorder, title);
    fxGroupPanel.setBorder (border);
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // SYS
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  protected class JMe80Panel_SYS
    extends JPanel
  {

    public JMe80Panel_SYS (final MidiDevice midiDevice)
    {
      super ();
      setLayout (new GridLayout (10, 1, 2, 2));
      add (new JMidiDeviceParameter_Enum (midiDevice,
        "Knob Mode", MidiDevice_Me80.SY_KNOB_MODE_NAME, MidiDevice_Me80.KnobMode.class));
      add (new JMidiDeviceParameter_Boolean (midiDevice,
        "Auto Off", MidiDevice_Me80.SY_AUTO_OFF_SWITCH_NAME));
      add (new JMidiDeviceParameter_Boolean (midiDevice,
        "Output While Tuning", MidiDevice_Me80.SY_TUNER_MUTE_NAME));
      add (new JMidiDeviceParameter_Enum (midiDevice,
        "Bank Change Mode", MidiDevice_Me80.SY_PATCH_CHANGE_MODE_NAME, MidiDevice_Me80.BankChangeMode.class));
      add (new JMidiDeviceParameter_Enum (midiDevice,
        "Manual Control Ops", MidiDevice_Me80.SY_MANUAL_CHANGE_MODE_NAME, MidiDevice_Me80.ManualControlOps.class));
      final JMidiDeviceParameter_Integer_Slider jUsbOutputLevel
        = new JMidiDeviceParameter_Integer_Slider (midiDevice,
          "USB Output Level", MidiDevice_Me80.SY_USB_OUTPUT_LEVEL_NAME, 0, 9);
      jUsbOutputLevel.getSlider ().setPaintTicks (false);
      jUsbOutputLevel.getSlider ().setPaintLabels (false);
      add (jUsbOutputLevel);
      add (new JMidiDeviceParameter_Boolean (midiDevice,
        "USB Loopback", MidiDevice_Me80.SY_USB_LOOPBACK_NAME));
      add (new JMidiDeviceParameter_Boolean (midiDevice,
        "USB Dry Rec", MidiDevice_Me80.SY_USB_DRY_REC_NAME));
      add (new JMidiDeviceParameter_Enum (midiDevice,
        "MIDI Channel", MidiDevice_Me80.SY_USB_MIDI_CH_NAME, MidiDevice_Me80.MidiChannel.class));
      add (new JMidiDeviceParameter_Enum (midiDevice,
        "Tuner Pitch", MidiDevice_Me80.SY_TUNER_PITCH_NAME, MidiDevice_Me80.TunerPitch.class));
    }

  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // LIB
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  protected class JMe80Panel_LIB
    extends JPanel
  {

    public JMe80Panel_LIB (final MidiDevice midiDevice)
    {
      super ();
      setLayout (new GridLayout (8, 1, 5, 5));
      add (new JLabel ());
      add (new JLabel ());
      add (new JLabel ());
      add (new JLabel ("     File I/O and Patch Operations"));
      add (new JLabel ("     are planned for Release 2"));
      add (new JLabel ());
      add (new JLabel ());
      add (new JLabel ());
    }

  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // SPARE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  protected class JMe80Panel_SPARE
    extends JPanel
  {

    public JMe80Panel_SPARE (final MidiDevice midiDevice)
    {
      super ();
      setLayout (new GridLayout (8, 1, 5, 5));
      add (new JLabel ());
      add (new JLabel ());
      add (new JLabel ());
      add (new JLabel ("     Spare Panel"));
      add (new JLabel ());
      add (new JLabel ());
      add (new JLabel ());
      add (new JLabel ());
    }

  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PATCH SELECTOR
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // COMP
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  protected class JMe80Panel_COMP
    extends JPanel
  {

    public JMe80Panel_COMP (final MidiDevice midiDevice)
    {
      super ();
      setLayout (new GridLayout (6, 1, 5, 5));
      add (new JMidiDeviceParameter_Boolean (midiDevice,
        "Active", MidiDevice_Me80.TP_COMP_SW_NAME));
      add (new JMidiDeviceParameter_Enum (midiDevice,
        "Type", MidiDevice_Me80.TP_COMP_TYPE_NAME, MidiDevice_Me80.CompEffectType.class));
      add (new JLabel ());
      add (new JMidiDeviceParameter_Integer_Slider (midiDevice,
        "Sus/Sens/-1Oct/Freq/Low", MidiDevice_Me80.TP_COMP_1_NAME, 0, 99));
      add (new JMidiDeviceParameter_Integer_Slider (midiDevice,
        "Att/Tone/-2Oct/[D.]Lvl/High", MidiDevice_Me80.TP_COMP_2_NAME, 0, 99));
      add (new JMidiDeviceParameter_Integer_Slider (midiDevice,
        "Lvl/Peak/Dir/[E.]Lvl", MidiDevice_Me80.TP_COMP_3_NAME, 0, 99));
    }

  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // OD/DS
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  protected class JMe80Panel_OD_DS
    extends JPanel
  {

    public JMe80Panel_OD_DS (final MidiDevice midiDevice)
    {
      super ();
      setLayout (new GridLayout (6, 1, 5, 5));
      add (new JMidiDeviceParameter_Boolean (midiDevice,
        "Active", MidiDevice_Me80.TP_OD_DS_SW_NAME));
      add (new JMidiDeviceParameter_Enum (midiDevice,
        "Type", MidiDevice_Me80.TP_OD_DS_TYPE_NAME, MidiDevice_Me80.OdDsEffectType.class));
      add (new JLabel ());
      add (new JMidiDeviceParameter_Integer_Slider (midiDevice,
        "Drive", MidiDevice_Me80.TP_OD_DS_1_NAME, 0, 99));
      add (new JMidiDeviceParameter_Integer_Slider (midiDevice,
        "Tone", MidiDevice_Me80.TP_OD_DS_2_NAME, 0, 99));
      add (new JMidiDeviceParameter_Integer_Slider (midiDevice,
        "Level", MidiDevice_Me80.TP_OD_DS_3_NAME, 0, 99));
    }

  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // AMP
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  protected class JMe80Panel_AMP
    extends JPanel
  {

    public JMe80Panel_AMP (final MidiDevice midiDevice)
    {
      super ();
      setLayout (new GridLayout (8, 1, 5, 0));
      add (new JMidiDeviceParameter_Boolean (midiDevice, "Active", MidiDevice_Me80.TP_AMP_SW_NAME));
      add (new JMidiDeviceParameter_Enum (midiDevice,
        "Type", MidiDevice_Me80.TP_AMP_TYPE_NAME, MidiDevice_Me80.AmpEffectType.class));
      add (new JLabel ());
      add (new JMidiDeviceParameter_Integer_Slider (midiDevice, "Gain",   MidiDevice_Me80.TP_AMP_1_NAME, 0, 99));
      add (new JMidiDeviceParameter_Integer_Slider (midiDevice, "Bass",   MidiDevice_Me80.TP_AMP_2_NAME, 0, 99));
      add (new JMidiDeviceParameter_Integer_Slider (midiDevice, "Middle", MidiDevice_Me80.TP_AMP_3_NAME, 0, 99));
      add (new JMidiDeviceParameter_Integer_Slider (midiDevice, "Treble", MidiDevice_Me80.TP_AMP_4_NAME, 0, 99));
      add (new JMidiDeviceParameter_Integer_Slider (midiDevice, "Level",  MidiDevice_Me80.TP_AMP_5_NAME, 0, 99));
    }
    
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PEDAL/FX
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  protected class JMe80Panel_PEDAL_FX
    extends JPanel
  {

    public JMe80Panel_PEDAL_FX (final MidiDevice midiDevice)
    {
      super ();
      setLayout (new GridLayout (2, 1, 5, 5));
      add (new JMidiDeviceParameter_Boolean (midiDevice,
        "Active", MidiDevice_Me80.TP_PEDAL_FX_SW_NAME));
      add (new JMidiDeviceParameter_Enum (midiDevice,
        "Type", MidiDevice_Me80.TP_PEDAL_FX_TYPE_NAME, MidiDevice_Me80.PedalFxType.class));
    }

  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // NS
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  protected class JMe80Panel_NS
    extends JPanel
  {

    public JMe80Panel_NS (final MidiDevice midiDevice)
    {
      super ();
      setLayout (new GridLayout (1, 1, 5, 5));
      add (new JMidiDeviceParameter_Integer_Slider (midiDevice,
        "Threshold", MidiDevice_Me80.TP_NS_THRESH_NAME, 0, 35));
    }

  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // FOOT VOLUME / EXPRESSION
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  protected class JMe80Panel_FOOT_VOLUME
    extends JPanel
  {

    public JMe80Panel_FOOT_VOLUME (final MidiDevice midiDevice)
    {
      super ();
      setLayout (new GridLayout (1, 1, 5, 5));
      add (new JMidiDeviceParameter_Integer_Slider (midiDevice, "Vol/Exp", MidiDevice_Me80.FOOT_VOLUME_NAME, 0, 127));
    }

  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MOD
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  protected class JMe80Panel_MOD
    extends JPanel
  {

    public JMe80Panel_MOD (final MidiDevice midiDevice)
    {
      super ();
      setLayout (new GridLayout (6, 1, 5, 5));
      add (new JMidiDeviceParameter_Boolean (midiDevice,
        "Active", MidiDevice_Me80.TP_MOD_SW_NAME));
      add (new JMidiDeviceParameter_Enum (midiDevice,
        "Type", MidiDevice_Me80.TP_MOD_TYPE_NAME, MidiDevice_Me80.ModEffectType.class));
      add (new JLabel ());
      add (new JMidiDeviceParameter_Integer_Slider (midiDevice,
        "Rate/Pitch/Key/Time/Upr", MidiDevice_Me80.TP_MOD_1_NAME, 0, 99));
      add (new JMidiDeviceParameter_Integer_Slider (midiDevice,
        "Depth/D.Lvl/Harm/FB/Lwr", MidiDevice_Me80.TP_MOD_2_NAME, 0, 99));
      add (new JMidiDeviceParameter_Integer_Slider (midiDevice,
        "Reso/E.Level/D.Level", MidiDevice_Me80.TP_MOD_3_NAME, 0, 99));
    }

  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // EQ/FX2
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  protected class JMe80Panel_EQ_FX2
    extends JPanel
  {

    public JMe80Panel_EQ_FX2 (final MidiDevice midiDevice)
    {
      super ();
      setLayout (new GridLayout (7, 1, 5, 5));
      add (new JMidiDeviceParameter_Boolean (midiDevice,
        "Active", MidiDevice_Me80.TP_EQ_FX2_SW_NAME));
      add (new JMidiDeviceParameter_Enum (midiDevice,
        "Type", MidiDevice_Me80.TP_EQ_FX2_TYPE_NAME, MidiDevice_Me80.EqFx2Type.class));
      add (new JLabel ());
      add (new JMidiDeviceParameter_Integer_Slider (midiDevice,
        "Bass", MidiDevice_Me80.TP_EQ_FX2_1_NAME, 0, 99));
      add (new JMidiDeviceParameter_Integer_Slider (midiDevice,
        "Rate/Drive/Time/Middle", MidiDevice_Me80.TP_EQ_FX2_2_NAME, 0, 99));
      add (new JMidiDeviceParameter_Integer_Slider (midiDevice,
        "Depth/Tone/FB/Treble", MidiDevice_Me80.TP_EQ_FX2_3_NAME, 0, 99));
      add (new JMidiDeviceParameter_Integer_Slider (midiDevice,
        "Level", MidiDevice_Me80.TP_EQ_FX2_4_NAME, 0, 99));
    }

  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // DELAY
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  protected class JMe80Panel_DELAY
    extends JPanel
  {

    public JMe80Panel_DELAY (final MidiDevice midiDevice)
    {
      super ();
      setLayout (new GridLayout (6, 1, 5, 5));
      add (new JMidiDeviceParameter_Boolean (midiDevice,
        "Active", MidiDevice_Me80.TP_DELAY_SW_NAME));
      add (new JMidiDeviceParameter_Enum (midiDevice,
        "Type", MidiDevice_Me80.TP_DELAY_TYPE_NAME, MidiDevice_Me80.DelayEffectType.class));
      add (new JLabel ());
      add (new JMidiDeviceParameter_Integer_Slider (midiDevice,
        "Time/Tempo", MidiDevice_Me80.TP_DELAY_1_NAME, 0, 99));
      add (new JMidiDeviceParameter_Integer_Slider (midiDevice,
        "Feedback", MidiDevice_Me80.TP_DELAY_2_NAME, 0, 99));
      add (new JMidiDeviceParameter_Integer_Slider (midiDevice,
        "E.Level", MidiDevice_Me80.TP_DELAY_3_NAME, 0, 99));
    }

  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // REVERB
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  protected class JMe80Panel_REVERB
    extends JPanel
  {

    public JMe80Panel_REVERB (final MidiDevice midiDevice)
    {
      super ();
      setLayout (new GridLayout (6, 1, 5, 5));
      add (new JMidiDeviceParameter_Boolean (midiDevice,
        "Active", MidiDevice_Me80.TP_REVERB_SW_NAME));
      add (new JMidiDeviceParameter_Enum (midiDevice,
        "Type", MidiDevice_Me80.TP_REVERB_TYPE_NAME, MidiDevice_Me80.ReverbEffectType.class));
      add (new JLabel ());
      add (new JMidiDeviceParameter_Integer_Slider (midiDevice,
        "Reverb", MidiDevice_Me80.TP_REVERB_REV_NAME, 0, 49));
    }

  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
}
