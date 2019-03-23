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
package org.javajdj.jservice.midi.device.rolandboss.bossme80;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;
import org.javajdj.jservice.midi.device.rolandboss.ParameterDescriptor_RolandBoss;
import org.javajdj.jservice.midi.MidiService;
import org.javajdj.jservice.midi.device.MidiDevice;
import org.javajdj.jservice.midi.device.rolandboss.bossme80.swing.JMe80Panel_PatchSelector;

/** A {@link MidiDevice} implementation for the Roland-Boss ME-80.
 * 
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 */
public final class MidiDevice_Me80
  extends MidiDevice_Me80_Base
{

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // LOGGING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private static final Logger LOG = Logger.getLogger (MidiDevice_Me80.class.getName ());

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTORS / FACTORIES / CLONING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public MidiDevice_Me80 (final MidiService midiService)
  {
    super (midiService);
    registerParameters_Me80 ();
  }

  public MidiDevice_Me80 ()
  {
    super (null);
  }
  
  private void registerParameters_Me80 ()
  {
    registerParameters_Me80_CurrentPatchNo ();
    registerParameters_Me80_System ();
    registerParameters_Me80_TemporaryPatch ();
    registerParameters_Me80_FootVolume ();
    registerParameters_Me80_Patches ();
    registerParameters_Me80_Commands ();
    registerParameters_Me80_TODO ();
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CURRENT PATCH NO PARAMETER
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public final static String CURRENT_PATCH_NO_NAME = "current_patch_no";
  
  private void registerParameters_Me80_CurrentPatchNo ()
  {
        
    registerParameter (new ParameterDescriptor_RolandBoss (CURRENT_PATCH_NO_NAME, Integer.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.INT_IN_BYTE,
      new byte[]{0x00, 0x00, 0x00, 0x00}, new byte[]{0x00, 0x00, 0x00, 0x01}, CURRENT_PATCH_NO_RAW_NAME));
    
  }
  
  private final static String[] CURRENT_PATCH_NO_RAW_SUB_KEYS = new String[]
  {
    CURRENT_PATCH_NO_NAME
  };
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // SYSTEM PARAMETERS
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public final static String SY_KNOB_MODE_NAME          = "system.knob_mode";
  public final static String SY_AUTO_OFF_SWITCH_NAME    = "system.auto_off_switch";
  public final static String SY_TUNER_MUTE_NAME         = "system.tuner_mute";
  public final static String SY_PATCH_CHANGE_MODE_NAME  = "system.patch_change_mode";
  public final static String SY_MANUAL_CHANGE_MODE_NAME = "system.manual_change_mode";
  public final static String SY_USB_OUTPUT_LEVEL_NAME   = "system.usb_output_level";
  public final static String SY_USB_LOOPBACK_NAME       = "system.usb_loopback";
  public final static String SY_USB_DRY_REC_NAME        = "system.usb_dry_rec";
  public final static String SY_USB_MIDI_CH_NAME        = "system.usb_midi_ch";
  public final static String SY_TUNER_PITCH_NAME        = "system.tuner_pitch";

  private final static String[] SYSTEM_SUB_KEYS = new String[]
  {
    SY_KNOB_MODE_NAME,
    SY_AUTO_OFF_SWITCH_NAME,
    SY_TUNER_MUTE_NAME,
    SY_PATCH_CHANGE_MODE_NAME,
    SY_MANUAL_CHANGE_MODE_NAME,
    SY_USB_OUTPUT_LEVEL_NAME,
    SY_USB_LOOPBACK_NAME,
    SY_USB_DRY_REC_NAME,
    SY_USB_MIDI_CH_NAME,
    SY_TUNER_PITCH_NAME
  };
  
  public enum KnobMode
  {
    IMMEDIATE,
    WHEN_REACHED;
  }
  
  public enum BankChangeMode
  {
    WAIT_FOR_NR,
    IMMEDIATE;
  }
  
  public enum ManualControlOps
  {
    ENABLED,
    DISABLED;
  }
  
  public enum MidiChannel
  {
    CH_01,
    CH_02,
    CH_03,
    CH_04,
    CH_05,
    CH_06,
    CH_07,
    CH_08,
    CH_09,
    CH_10,
    CH_11,
    CH_12,
    CH_13,
    CH_14,
    CH_15,
    CH_16;
  }
  
  public enum TunerPitch
  {
    TUN_435_Hz,
    TUN_436_Hz,
    TUN_437_Hz,
    TUN_438_Hz,
    TUN_439_Hz,
    TUN_440_Hz,
    TUN_441_Hz,
    TUN_442_Hz,
    TUN_443_Hz,
    TUN_444_Hz,
    TUN_445_Hz;
  }
  
  private void registerParameters_Me80_System ()
  {
        
    registerParameter (new ParameterDescriptor_RolandBoss (SY_KNOB_MODE_NAME, KnobMode.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.ENUM_IN_BYTE,
      new byte[]{0x10, 0x00, 0x00, 0x00}, new byte[]{0x00, 0x00, 0x00, 0x01}, SYSTEM_NAME));
    
    registerParameter (new ParameterDescriptor_RolandBoss (SY_AUTO_OFF_SWITCH_NAME, Boolean.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.BOOLEAN_IN_BYTE,
      new byte[]{0x10, 0x00, 0x00, 0x01}, new byte[]{0x00, 0x00, 0x00, 0x01}, SYSTEM_NAME));
    
    registerParameter (new ParameterDescriptor_RolandBoss (SY_TUNER_MUTE_NAME, Boolean.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.BOOLEAN_IN_BYTE,
      new byte[]{0x10, 0x00, 0x00, 0x02}, new byte[]{0x00, 0x00, 0x00, 0x01}, SYSTEM_NAME));

    registerParameter (new ParameterDescriptor_RolandBoss (SY_PATCH_CHANGE_MODE_NAME, BankChangeMode.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.ENUM_IN_BYTE,
      new byte[]{0x10, 0x00, 0x00, 0x03}, new byte[]{0x00, 0x00, 0x00, 0x01}, SYSTEM_NAME));

    registerParameter (new ParameterDescriptor_RolandBoss (SY_MANUAL_CHANGE_MODE_NAME, ManualControlOps.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.ENUM_IN_BYTE,
      new byte[]{0x10, 0x00, 0x00, 0x04}, new byte[]{0x00, 0x00, 0x00, 0x01}, SYSTEM_NAME));

    registerParameter (new ParameterDescriptor_RolandBoss (SY_USB_OUTPUT_LEVEL_NAME, Integer.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.INT_IN_BYTE,
      new byte[]{0x10, 0x00, 0x00, 0x05}, new byte[]{0x00, 0x00, 0x00, 0x01}, SYSTEM_NAME));

    registerParameter (new ParameterDescriptor_RolandBoss (SY_USB_LOOPBACK_NAME, Boolean.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.BOOLEAN_IN_BYTE,
      new byte[]{0x10, 0x00, 0x00, 0x06}, new byte[]{0x00, 0x00, 0x00, 0x01}, SYSTEM_NAME));

    registerParameter (new ParameterDescriptor_RolandBoss (SY_USB_DRY_REC_NAME, Boolean.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.BOOLEAN_IN_BYTE,
      new byte[]{0x10, 0x00, 0x00, 0x07}, new byte[]{0x00, 0x00, 0x00, 0x01}, SYSTEM_NAME));

    registerParameter (new ParameterDescriptor_RolandBoss (SY_USB_MIDI_CH_NAME, MidiChannel.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.ENUM_IN_BYTE,
      new byte[]{0x10, 0x00, 0x00, 0x08}, new byte[]{0x00, 0x00, 0x00, 0x01}, SYSTEM_NAME));

    registerParameter (new ParameterDescriptor_RolandBoss (SY_TUNER_PITCH_NAME, TunerPitch.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.ENUM_IN_BYTE,
      new byte[]{0x10, 0x00, 0x00, 0x09}, new byte[]{0x00, 0x00, 0x00, 0x01}, SYSTEM_NAME));
    
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // TEMPORARY-PATCH PARAMETERS
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final static String[] TEMPORARY_PATCH_SUB_KEYS = new String[]
  {
    
    // NAME
    MidiDevice_Me80.TP_NAME_NAME,
    
    // COMP
    MidiDevice_Me80.TP_COMP_SW_NAME,
    MidiDevice_Me80.TP_COMP_TYPE_NAME,
    MidiDevice_Me80.TP_COMP_1_NAME,
    MidiDevice_Me80.TP_COMP_2_NAME,
    MidiDevice_Me80.TP_COMP_3_NAME,
    
    // OD/DS
    MidiDevice_Me80.TP_OD_DS_SW_NAME,
    MidiDevice_Me80.TP_OD_DS_TYPE_NAME,
    MidiDevice_Me80.TP_OD_DS_1_NAME,
    MidiDevice_Me80.TP_OD_DS_2_NAME,
    MidiDevice_Me80.TP_OD_DS_3_NAME,
    
    // AMP
    MidiDevice_Me80.TP_AMP_SW_NAME,
    MidiDevice_Me80.TP_AMP_TYPE_NAME,
    MidiDevice_Me80.TP_AMP_1_NAME,
    MidiDevice_Me80.TP_AMP_2_NAME,
    MidiDevice_Me80.TP_AMP_3_NAME,
    MidiDevice_Me80.TP_AMP_4_NAME,
    MidiDevice_Me80.TP_AMP_5_NAME,
   
    // MOD
    MidiDevice_Me80.TP_MOD_SW_NAME,
    MidiDevice_Me80.TP_MOD_TYPE_NAME,
    MidiDevice_Me80.TP_MOD_1_NAME,
    MidiDevice_Me80.TP_MOD_2_NAME,
    MidiDevice_Me80.TP_MOD_3_NAME,
    
    // EQ/FX2
    MidiDevice_Me80.TP_EQ_FX2_SW_NAME,
    MidiDevice_Me80.TP_EQ_FX2_TYPE_NAME,
    MidiDevice_Me80.TP_EQ_FX2_1_NAME,
    MidiDevice_Me80.TP_EQ_FX2_2_NAME,
    MidiDevice_Me80.TP_EQ_FX2_3_NAME,
    MidiDevice_Me80.TP_EQ_FX2_4_NAME,
    
    // DELAY
    MidiDevice_Me80.TP_DELAY_SW_NAME,
    MidiDevice_Me80.TP_DELAY_TYPE_NAME,
    MidiDevice_Me80.TP_DELAY_1_NAME,
    MidiDevice_Me80.TP_DELAY_2_NAME,
    MidiDevice_Me80.TP_DELAY_3_NAME,
    
    // REVERB
    MidiDevice_Me80.TP_REVERB_SW_NAME,
    MidiDevice_Me80.TP_REVERB_TYPE_NAME,
    MidiDevice_Me80.TP_REVERB_REV_NAME,
    
    // CTL
    MidiDevice_Me80.TP_CTL_TARGET_NAME,
    MidiDevice_Me80.TP_CTL_KNOB_VALUE_NAME,
    MidiDevice_Me80.TP_CTL_MODE_NAME,
    MidiDevice_Me80.TP_CTL_TARGET_CUSTOM_NAME,
    
    // PEDAL/FX
    MidiDevice_Me80.TP_PEDAL_FX_SW_NAME,    
    MidiDevice_Me80.TP_PEDAL_FX_TYPE_NAME,
    
    // NS
    MidiDevice_Me80.TP_NS_THRESH_NAME
    
  };
  
  private void registerParameters_Me80_TemporaryPatch ()
  {
    registerParameters_Me80_TemporaryPatch_Name ();
    registerParameters_Me80_TemporaryPatch_Comp ();
    registerParameters_Me80_TemporaryPatch_OdDs ();
    registerParameters_Me80_TemporaryPatch_Amp ();
    registerParameters_Me80_TemporaryPatch_Mod ();
    registerParameters_Me80_TemporaryPatch_Eq_Fx2 ();
    registerParameters_Me80_TemporaryPatch_Delay ();
    registerParameters_Me80_TemporaryPatch_Reverb ();
    registerParameters_Me80_TemporaryPatch_CTL ();
    registerParameters_Me80_TemporaryPatch_PedalFx ();
    registerParameters_Me80_TemporaryPatch_Ns ();
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // TEMPORARY-PATCH PARAMETERS - NAME
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public final static String TP_NAME_NAME = "temporary_patch.name";
  
  private void registerParameters_Me80_TemporaryPatch_Name ()
  {
    registerParameter (new ParameterDescriptor_RolandBoss (TP_NAME_NAME, String.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.FIXED_US_ASCII_STRING_IN_BYTES,
      new byte[]{0x20, 0x00, 0x00, 0x00}, new byte[]{0x00, 0x00, 0x00, 0x10}, TEMPORARY_PATCH_NAME)); 
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // TEMPORARY-PATCH PARAMETERS - COMP
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public final static String TP_COMP_SW_NAME   = "temporary_patch.comp.sw";
  public final static String TP_COMP_TYPE_NAME = "temporary_patch.comp.type";
  public final static String TP_COMP_1_NAME    = "temporary_patch.comp.1";
  public final static String TP_COMP_2_NAME    = "temporary_patch.comp.2";
  public final static String TP_COMP_3_NAME    = "temporary_patch.comp.3";
  
  public enum CompEffectType
  {
    COMP,
    T_WAH_UP,
    T_WAH_DOWN,
    OCTAVE,
    SLOW_GEAR,
    DEFRETTER,
    RING_MOD,
    AC_SIM,
    SINGLE_2_HUM,
    HUM_2_SINGLE,
    SOLO;
  }
  
  private void registerParameters_Me80_TemporaryPatch_Comp ()
  {
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_COMP_SW_NAME, Boolean.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.BOOLEAN_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x10}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_COMP_TYPE_NAME, CompEffectType.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.ENUM_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x18}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_COMP_1_NAME, Integer.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.INT_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x20}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_COMP_2_NAME, Integer.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.INT_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x21}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_COMP_3_NAME, Integer.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.INT_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x22}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));

  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // TEMPORARY-PATCH PARAMETERS - OD/DS
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public final static String TP_OD_DS_SW_NAME   = "temporary_patch.od_ds.sw";
  public final static String TP_OD_DS_TYPE_NAME = "temporary_patch.od_ds.type";
  public final static String TP_OD_DS_1_NAME    = "temporary_patch.od_ds.1";
  public final static String TP_OD_DS_2_NAME    = "temporary_patch.od_ds.2";
  public final static String TP_OD_DS_3_NAME    = "temporary_patch.od_ds.3";
  
  public enum OdDsEffectType
  {
    BOOST,
    OVERDRIVE,
    T_SCREAM,
    BLUES,
    TURBO_OD,
    DISTORTION,
    TURBO_DS,
    METAL_DS,
    CORE,
    FUZZ,
    OCT_FUZZ;
  }
  
  private void registerParameters_Me80_TemporaryPatch_OdDs ()
  {
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_OD_DS_SW_NAME, Boolean.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.BOOLEAN_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x11}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_OD_DS_TYPE_NAME, OdDsEffectType.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.ENUM_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x19}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_OD_DS_1_NAME, Integer.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.INT_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x23}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_OD_DS_2_NAME, Integer.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.INT_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x24}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_OD_DS_3_NAME, Integer.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.INT_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x25}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));

  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // TEMPORARY-PATCH PARAMETERS - AMP
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public final static String TP_AMP_SW_NAME   = "temporary_patch.amp.sw";
  public final static String TP_AMP_TYPE_NAME = "temporary_patch.amp.type";
  public final static String TP_AMP_1_NAME    = "temporary_patch.amp.1";
  public final static String TP_AMP_2_NAME    = "temporary_patch.amp.2";
  public final static String TP_AMP_3_NAME    = "temporary_patch.amp.3";
  public final static String TP_AMP_4_NAME    = "temporary_patch.amp.4";
  public final static String TP_AMP_5_NAME    = "temporary_patch.amp.5";
  
  public enum AmpEffectType
  {
    AC,
    CLEAN,
    TWEED,
    CRUNCH,
    COMBO,
    LEAD,
    DRIVE,
    STACK,
    METAL;
  }
    
  private void registerParameters_Me80_TemporaryPatch_Amp ()
  {
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_AMP_SW_NAME, Boolean.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.BOOLEAN_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x14}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_AMP_TYPE_NAME, AmpEffectType.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.ENUM_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x1C}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_AMP_1_NAME, Integer.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.INT_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x2C}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_AMP_2_NAME, Integer.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.INT_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x2D}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_AMP_3_NAME, Integer.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.INT_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x2E}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));

    registerParameter (new ParameterDescriptor_RolandBoss (TP_AMP_4_NAME, Integer.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.INT_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x2F}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_AMP_5_NAME, Integer.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.INT_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x30}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));
    
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // TEMPORARY-PATCH PARAMETERS - MOD
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public final static String TP_MOD_SW_NAME   = "temporary_patch.mod.sw";
  public final static String TP_MOD_TYPE_NAME = "temporary_patch.mod.type";
  public final static String TP_MOD_1_NAME    = "temporary_patch.mod.1";
  public final static String TP_MOD_2_NAME    = "temporary_patch.mod.2";
  public final static String TP_MOD_3_NAME    = "temporary_patch.mod.3";
  
  public enum ModEffectType
  {
    PHASER,
    FLANGER,
    TREMOLO,
    CHORUS,
    VIBRATO,
    PITCH_SHIFT,
    HARMONIST,
    ROTARY,
    UNI_V,
    DELAY,
    OVERTONE;
  }
  
  private void registerParameters_Me80_TemporaryPatch_Mod ()
  {
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_MOD_SW_NAME, Boolean.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.BOOLEAN_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x12}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_MOD_TYPE_NAME, ModEffectType.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.ENUM_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x1A}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_MOD_1_NAME, Integer.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.INT_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x26}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_MOD_2_NAME, Integer.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.INT_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x27}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_MOD_3_NAME, Integer.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.INT_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x28}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));

  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // TEMPORARY-PATCH PARAMETERS - EQ/FX2
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public final static String TP_EQ_FX2_SW_NAME   = "temporary_patch.eq_fx2.sw";
  public final static String TP_EQ_FX2_TYPE_NAME = "temporary_patch.eq_fx2.type";
  public final static String TP_EQ_FX2_1_NAME    = "temporary_patch.eq_fx2.1";
  public final static String TP_EQ_FX2_2_NAME    = "temporary_patch.eq_fx2.2";
  public final static String TP_EQ_FX2_3_NAME    = "temporary_patch.eq_fx2.3";
  public final static String TP_EQ_FX2_4_NAME    = "temporary_patch.eq_fx2.4";
  
  public enum EqFx2Type
  {
    PHASER,
    TREMOLO,
    BOOST,
    DELAY,
    CHORUS,
    EQ;
  }
  
  private void registerParameters_Me80_TemporaryPatch_Eq_Fx2 ()
  {
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_EQ_FX2_SW_NAME, Boolean.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.BOOLEAN_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x15}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_EQ_FX2_TYPE_NAME, EqFx2Type.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.ENUM_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x1D}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_EQ_FX2_1_NAME, Integer.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.INT_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x31}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_EQ_FX2_2_NAME, Integer.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.INT_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x32}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_EQ_FX2_3_NAME, Integer.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.INT_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x33}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));

    registerParameter (new ParameterDescriptor_RolandBoss (TP_EQ_FX2_4_NAME, Integer.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.INT_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x34}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));
    
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // TEMPORARY-PATCH PARAMETERS - DELAY
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public final static String TP_DELAY_SW_NAME   = "temporary_patch.delay.sw";
  public final static String TP_DELAY_TYPE_NAME = "temporary_patch.delay.type";
  public final static String TP_DELAY_1_NAME    = "temporary_patch.delay.1";
  public final static String TP_DELAY_2_NAME    = "temporary_patch.delay.2";
  public final static String TP_DELAY_3_NAME    = "temporary_patch.delay.3";
  
  public enum DelayEffectType
  {
    DELAY_1_99_MS,
    DELAY_100_600_MS,
    DELAY_500_6000_MS,
    ANALOG,
    TAPE,
    MODULATE,
    REVERSE,
    ECHO_DELAY,
    TEMPO,
    TERA_ECHO,
    PHRASE_LOOP;
  }
  
  private void registerParameters_Me80_TemporaryPatch_Delay ()
  {
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_DELAY_SW_NAME, Boolean.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.BOOLEAN_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x13}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_DELAY_TYPE_NAME, DelayEffectType.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.ENUM_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x1B}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_DELAY_1_NAME, Integer.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.INT_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x29}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_DELAY_2_NAME, Integer.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.INT_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x2A}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_DELAY_3_NAME, Integer.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.INT_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x2B}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));

  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // TEMPORARY-PATCH PARAMETERS - REVERB
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public final static String TP_REVERB_SW_NAME   = "temporary_patch.reverb.sw";
  public final static String TP_REVERB_TYPE_NAME = "temporary_patch.reverb.type";
  public final static String TP_REVERB_REV_NAME  = "temporary_patch.reverb.rev";
  
  public enum ReverbEffectType
  {
    ROOM,
    HALL,
    SPRING;
  }
    
  private void registerParameters_Me80_TemporaryPatch_Reverb ()
  {
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_REVERB_SW_NAME, Boolean.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.BOOLEAN_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x16}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_REVERB_TYPE_NAME, ReverbEffectType.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.ENUM_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x1F}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_REVERB_REV_NAME, Integer.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.INT_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x3A}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));

  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // TEMPORARY-PATCH PARAMETERS - CTL
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public final static String TP_CTL_TARGET_NAME            = "temporary_patch.ctl.target";
  public final static String TP_CTL_KNOB_VALUE_NAME        = "temporary_patch.ctl.knob_value";
  public final static String TP_CTL_MODE_NAME              = "temporary_patch.ctl.mode";
  public final static String TP_CTL_TARGET_CUSTOM_NAME     = "temporary_patch.ctl.target_custom";
  
  public static enum CtlTargetCustom
  {
    
    EFFECTS,
    COMP_1,
    COMP_2,
    COMP_3,
    OD_DS_DRIVE,
    OD_DS_TONE,
    OD_DS_LEVEL,
    MOD_1,
    MOD_2,
    MOD_3,
    DELAY_TIME,
    DELAY_FEEDBACK,
    DELAY_E_LEVEL,
    AMP_GAIN,
    AMP_BASS,
    AMP_MIDDLE,
    AMP_TREBLE,
    AMP_LEVEL,
    EQ_FX2_1,
    EQ_FX2_2,
    EQ_FX2_3,
    EQ_FX2_4;
    
    private static CtlTargetCustom fromDevice (final byte[] bytes)
    {
      if (bytes == null || bytes.length != 3)
        throw new IllegalArgumentException ();
      if (bytes[2] == 0x64)
        return EFFECTS;
      if (bytes[0] == 0x00 && bytes[1] == 0x00)
        return COMP_1;
      if (bytes[0] == 0x00 && bytes[1] == 0x01)
        return COMP_2;
      if (bytes[0] == 0x00 && bytes[1] == 0x02)
        return COMP_3;
      if (bytes[0] == 0x00 && bytes[1] == 0x03)
        return OD_DS_DRIVE;
      if (bytes[0] == 0x00 && bytes[1] == 0x04)
        return OD_DS_TONE;
      if (bytes[0] == 0x00 && bytes[1] == 0x05)
        return OD_DS_LEVEL;
      if (bytes[0] == 0x00 && bytes[1] == 0x06)
        return MOD_1;
      if (bytes[0] == 0x00 && bytes[1] == 0x07)
        return MOD_2;
      if (bytes[0] == 0x00 && bytes[1] == 0x08)
        return MOD_3;
      if (bytes[0] == 0x00 && bytes[1] == 0x09)
        return DELAY_TIME;
      if (bytes[0] == 0x00 && bytes[1] == 0x0A)
        return DELAY_FEEDBACK;
      if (bytes[0] == 0x00 && bytes[1] == 0x0B)
        return DELAY_E_LEVEL;
      if (bytes[0] == 0x00 && bytes[1] == 0x0C)
        return AMP_GAIN;
      if (bytes[0] == 0x00 && bytes[1] == 0x0D)
        return AMP_BASS;
      if (bytes[0] == 0x00 && bytes[1] == 0x0E)
        return AMP_MIDDLE;
      if (bytes[0] == 0x00 && bytes[1] == 0x0F)
        return AMP_TREBLE;
      if (bytes[0] == 0x01 && bytes[1] == 0x00)
        return AMP_LEVEL;
      if (bytes[0] == 0x01 && bytes[1] == 0x01)
        return EQ_FX2_1;
      if (bytes[0] == 0x01 && bytes[1] == 0x02)
        return EQ_FX2_2;
      if (bytes[0] == 0x01 && bytes[1] == 0x03)
        return EQ_FX2_3;
      if (bytes[0] == 0x01 && bytes[1] == 0x04)
        return EQ_FX2_4;
      throw new IllegalArgumentException ();
    }
    
  }

  public static enum CtlEffectsCustom
  {
    
    COMP,
    OD_DS,
    MOD,
    DELAY,
    AMP,
    EQ_FX2,
    REVERB;
    
    private static Set<CtlEffectsCustom> fromDevice (final byte[] bytes)
    {
      if (bytes == null || bytes.length != 3)
        throw new IllegalArgumentException ();
      if (bytes[2] == 0x64)
      {
        final Set<CtlEffectsCustom> ctlEffectsCustoms = EnumSet.noneOf (CtlEffectsCustom.class);
        if ((bytes[1] & 0x01) != 0)
          ctlEffectsCustoms.add (COMP);
        if ((bytes[1] & 0x02) != 0)
          ctlEffectsCustoms.add (OD_DS);
        if ((bytes[1] & 0x04) != 0)
          ctlEffectsCustoms.add (MOD);
        if ((bytes[1] & 0x08) != 0)
          ctlEffectsCustoms.add (DELAY);
        if ((bytes[0] & 0x01) != 0)
          ctlEffectsCustoms.add (AMP);
        if ((bytes[0] & 0x02) != 0)
          ctlEffectsCustoms.add (EQ_FX2);
        if ((bytes[0] & 0x04) != 0)
          ctlEffectsCustoms.add (REVERB);
        return ctlEffectsCustoms;
      }
      else
        return null;
    }
    
    private static byte[] mergeEffectsToDevice (final byte[] bytes, final Set<CtlEffectsCustom> c_set)
    {
      if (bytes == null || bytes.length != 3 || c_set == null)
        throw new IllegalArgumentException ();
      for (final CtlEffectsCustom c : c_set)
        switch (c)
        {
          case COMP:
            bytes[1] |= 0x01;
            break;
          case OD_DS:
            bytes[1] |= 0x02;
            break;
          case MOD:
            bytes[1] |= 0x04;
            break;
          case DELAY:
            bytes[1] |= 0x08;
            break;
          case AMP:
            bytes[0] |= 0x01;
            break;
          case EQ_FX2:
            bytes[0] |= 0x02;
            break;
          case REVERB:
            bytes[0] |= 0x04;
            break;
          default:
            throw new RuntimeException ();
        }
      return bytes;
    }
    
  }

  public static class CtlTargetAndKnobValueCustom
  {

    public CtlTargetAndKnobValueCustom
    ( final CtlTargetCustom ctlTargetCustom,
      final Integer ctlKnobValueCustom,
      final Set<CtlEffectsCustom> ctlEffectsCustom)
    {
      if (ctlTargetCustom == null)
        throw new IllegalArgumentException ();
      if (ctlTargetCustom == CtlTargetCustom.EFFECTS)
      {
        if (ctlKnobValueCustom != null && ctlKnobValueCustom != 0x64)
          throw new IllegalArgumentException ();
        if (ctlEffectsCustom == null)
          throw new IllegalArgumentException ();
      }
      else
      {
        if (ctlKnobValueCustom == null || ctlKnobValueCustom < 0 || ctlKnobValueCustom >= 0x64)
          throw new IllegalArgumentException ();
        if (ctlEffectsCustom != null)
          throw new IllegalArgumentException ();
      }
      this.ctlTargetCustom = ctlTargetCustom;
      this.ctlKnobValueCustom = (ctlKnobValueCustom != null && ctlKnobValueCustom != 0x64) ? ctlKnobValueCustom : null;
      this.ctlEffectsCustom = (ctlEffectsCustom != null ? EnumSet.copyOf (ctlEffectsCustom) : null);
    }
    
    public static CtlTargetAndKnobValueCustom fromDevice (final byte[] bytes)
    {
      if (bytes == null || bytes.length != 3)
        throw new IllegalArgumentException ();
      return new CtlTargetAndKnobValueCustom (CtlTargetCustom.fromDevice (bytes),
                                              (int) bytes[2],
                                              CtlEffectsCustom.fromDevice (bytes));
    }
    
    public static byte[] toDevice (final CtlTargetAndKnobValueCustom c)
    {
      if (c == null)
        throw new IllegalArgumentException ();
      if (c.getTarget () == null)
        throw new RuntimeException ();
      final byte[] bytes = new byte[3];
      switch (c.getTarget ())
      {
        case EFFECTS:
          bytes[2] = 0x64;
          CtlEffectsCustom.mergeEffectsToDevice (bytes, c.getCtlEffectsCustom ()); // in-situ
          break;
        case COMP_1:
        case COMP_2:
        case COMP_3:
        case OD_DS_DRIVE:
        case OD_DS_TONE:
        case OD_DS_LEVEL:
        case MOD_1:
        case MOD_2:
        case MOD_3:
        case DELAY_TIME:
        case DELAY_FEEDBACK:
        case DELAY_E_LEVEL:
        case AMP_GAIN:
        case AMP_BASS:
        case AMP_MIDDLE:
        case AMP_TREBLE:
          bytes[0] = 0x00;
          bytes[1] = (byte) (c.getTarget ().ordinal () - CtlTargetCustom.COMP_1.ordinal ());
          if (c.getKnobValue () == null || c.getKnobValue () < 0 || c.getKnobValue () >= 0x64)
            throw new IllegalArgumentException ();
          bytes[2] = c.getKnobValue ().byteValue ();
          break;
        case AMP_LEVEL:
        case EQ_FX2_1:
        case EQ_FX2_2:
        case EQ_FX2_3:
        case EQ_FX2_4:
          bytes[0] = 0x01;
          bytes[1] = (byte) (c.getTarget ().ordinal () - CtlTargetCustom.AMP_LEVEL.ordinal ());
          if (c.getKnobValue () == null || c.getKnobValue () < 0 || c.getKnobValue () >= 0x64)
            throw new IllegalArgumentException ();
          bytes[2] = c.getKnobValue ().byteValue ();
          break;        
        default:
          throw new RuntimeException ();
      }
      return bytes;
    }
    
    private final CtlTargetCustom ctlTargetCustom;
    
    public final CtlTargetCustom getTarget ()
    {
      return this.ctlTargetCustom;
    }
    
    public final CtlTargetAndKnobValueCustom withTarget (final CtlTargetCustom ctlTargetCustom)
    {
      if (ctlTargetCustom == null)
        throw new IllegalArgumentException ();
      if (this.ctlTargetCustom == ctlTargetCustom)
        return new CtlTargetAndKnobValueCustom (this.ctlTargetCustom, this.ctlKnobValueCustom, this.ctlEffectsCustom);
      else if (ctlTargetCustom == CtlTargetCustom.EFFECTS)
        return new CtlTargetAndKnobValueCustom (CtlTargetCustom.EFFECTS, null, Collections.EMPTY_SET);
      else if (this.ctlTargetCustom == CtlTargetCustom.EFFECTS)
        return new CtlTargetAndKnobValueCustom (ctlTargetCustom, 0x32, null);
      else
        return new CtlTargetAndKnobValueCustom (ctlTargetCustom, getKnobValue (), null);        
    }
    
    private final Integer ctlKnobValueCustom;
    
    public final Integer getKnobValue ()
    {
      return this.ctlKnobValueCustom;
    }
    
    private final EnumSet<CtlEffectsCustom> ctlEffectsCustom;
    
    public final Set<CtlEffectsCustom> getCtlEffectsCustom ()
    {
      return Collections.unmodifiableSet (this.ctlEffectsCustom);
    }

    @Override
    public int hashCode ()
    {
      int hash = 7;
      hash = 67 * hash + Objects.hashCode (this.ctlTargetCustom);
      hash = 67 * hash + Objects.hashCode (this.ctlKnobValueCustom);
      hash = 67 * hash + Objects.hashCode (this.ctlEffectsCustom);
      return hash;
    }

    @Override
    public boolean equals (Object obj)
    {
      if (this == obj)
      {
        return true;
      }
      if (obj == null)
      {
        return false;
      }
      if (getClass () != obj.getClass ())
      {
        return false;
      }
      final CtlTargetAndKnobValueCustom other = (CtlTargetAndKnobValueCustom) obj;
      if (this.ctlTargetCustom != other.ctlTargetCustom)
      {
        return false;
      }
      if (! Objects.equals (this.ctlKnobValueCustom, other.ctlKnobValueCustom))
      {
        return false;
      }
      return Objects.equals (this.ctlEffectsCustom, other.ctlEffectsCustom);
    }
    
  }
  
  private static class CtlTargetAndKnobValueCustomConverter
    implements ParameterDescriptor_RolandBoss.CustomValueConverter<CtlTargetAndKnobValueCustom>
  {

    @Override
    public final CtlTargetAndKnobValueCustom fromDevice (final byte[] bytes)
    {
      if (bytes == null || bytes.length != 3)
        throw new IllegalArgumentException ();
      return CtlTargetAndKnobValueCustom.fromDevice (bytes);
    }

    @Override
    public final byte[] toDevice (final CtlTargetAndKnobValueCustom c)
    {
      if (c == null)
        throw new IllegalArgumentException ();
      return CtlTargetAndKnobValueCustom.toDevice (c);
    }
    
  }
  
  public enum CtlMode
  {
    MOMENTARY,
    TOGGLE;
  }
  
  private void registerParameters_Me80_TemporaryPatch_CTL ()
  {
    
    // The ctl_target parameter is defined in the data\addressmap.json in the (a/my) Boss Tone Studio distribution
    // as having length 1, but that seems to be an error.
    // The ME-80 does not provide any value with a unit-length request at 0x20000035; it simple igores the request.
    // Setting the length to 2 seems to solve the problem; that value also makes a lot more sense
    // since the next parameter defined ("ctrl_knob_value") starts at 0x37, i.e., +2, and NOT at 0x36 (+1). 
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_CTL_TARGET_NAME, byte[].class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.NONE,
      new byte[]{0x20, 0x00, 0x00, 0x35}, new byte[]{0x00, 0x00, 0x00, /* 0x01 */ 0x02}, TEMPORARY_PATCH_NAME));
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_CTL_KNOB_VALUE_NAME, Integer.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.INT_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x37}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_CTL_MODE_NAME, CtlMode.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.ENUM_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x38}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));

    // The following parameter definition is non-standard (well, not defined as such in the docs with Boss Tone Studio.
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_CTL_TARGET_CUSTOM_NAME, CtlTargetAndKnobValueCustom.class,
      new byte[]{0x20, 0x00, 0x00, 0x35}, new byte[]{0x00, 0x00, 0x00, 0x03}, TEMPORARY_PATCH_NAME,
      new CtlTargetAndKnobValueCustomConverter ()));
    
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // TEMPORARY-PATCH PARAMETERS - PEDAL/FX
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public final static String TP_PEDAL_FX_SW_NAME   = "temporary_patch.pdlfx.sw";
  public final static String TP_PEDAL_FX_TYPE_NAME = "temporary_patch.pdlfx.type";
  
  public enum PedalFxType
  {
    WAH,
    VOICE,
    PLUS_1_OCT,
    PLUS_2_OCT,
    MINUS_1_OCT,
    FREEZE,
    OSC_DELAY,
    OD_DS,
    MOD_RATE,
    DELAY_LEV;
  }
  
    
  private void registerParameters_Me80_TemporaryPatch_PedalFx ()
  {
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_PEDAL_FX_SW_NAME, Boolean.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.BOOLEAN_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x17}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_PEDAL_FX_TYPE_NAME, PedalFxType.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.ENUM_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x1E}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));
    
  }
    
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // TEMPORARY-PATCH PARAMETERS - NS
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public final static String TP_NS_THRESH_NAME   = "temporary_patch.ns.ns_thresh";
  
  private void registerParameters_Me80_TemporaryPatch_Ns ()
  {
    
    registerParameter (new ParameterDescriptor_RolandBoss (TP_NS_THRESH_NAME, Integer.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.INT_IN_BYTE,
      new byte[]{0x20, 0x00, 0x00, 0x39}, new byte[]{0x00, 0x00, 0x00, 0x01}, TEMPORARY_PATCH_NAME));

  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PATCH PARAMETERS - FOOT VOLUME
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public final static String FOOT_VOLUME_NAME   = "foot_volume";
  
  public final static byte BOSS_ME80_VOL_EXPR_CONTROLLER = (byte) 0x10;
  
  private void registerParameters_Me80_FootVolume ()
  {
    
    registerParameter (new ParameterDescriptor_RolandBoss (FOOT_VOLUME_NAME, BOSS_ME80_VOL_EXPR_CONTROLLER));

  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PARAMETERS - [USER/PRESET] PATCHES
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private void registerParameters_Me80_Patches ()
  {   
    byte patchCounter = 1; // Start with 1, hence with 0x20010000 for U1.1; note that 0x20000000 is the temporary patch.
    for (final JMe80Panel_PatchSelector.ME80_BANK me80_bank : JMe80Panel_PatchSelector.ME80_BANK.values ())
      for (final JMe80Panel_PatchSelector.ME80_PATCH_IN_BANK me80_patch_in_bank :
        JMe80Panel_PatchSelector.ME80_PATCH_IN_BANK.values ())
      {
        registerParameter (new ParameterDescriptor_RolandBoss (
          toParameterName (me80_bank, me80_patch_in_bank),
          byte[].class,
          new byte[]{0x20, patchCounter++, 0x00, 0x00},
          new byte[]{0x00, 0x00, 0x00, PATCH_SIZE},
          null));
      }
  }
  
  /** Returns the parameter name of a specific patch in the ME'80's patch bank.
   * 
   * @param me80_bank          The bank, non-{@code null}.
   * @param me80_patch_in_bank The patch in the specified bank, non-{@code null}.
   * 
   * @return The parameter name (in the {@link #keySet} of this device) of the specified patch in the ME-80's patch bank.
   * 
   */
  public static String toParameterName
  (final JMe80Panel_PatchSelector.ME80_BANK me80_bank,
   final JMe80Panel_PatchSelector.ME80_PATCH_IN_BANK me80_patch_in_bank)
  {
    return "patch." + me80_bank.name ().trim ().toLowerCase () + "." + me80_patch_in_bank.name ().trim ().toLowerCase ();
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PARAMETERS - COMMANDS
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public final static String COMMAND_PATCH_WRITE_NAME     = "command.patch_write";
  public final static String COMMAND_USB_DIRECT_MON_NAME  = "command.usb_direct_mon";
  public final static String COMMAND_USB_IN_OUT_MODE_NAME = "command.usb_in_out_mode";
  
  private void registerParameters_Me80_Commands ()
  {
    
    // I do not think we really need this 'command' since we can directly write the patch banks...
    registerParameter (new ParameterDescriptor_RolandBoss (COMMAND_PATCH_WRITE_NAME, byte[].class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.NONE,
      new byte[]{0x7F, 0x00, 0x01, 0x04}, new byte[]{0x00, 0x00, 0x00, 0x02}, null));
    registerParameter (new ParameterDescriptor_RolandBoss (COMMAND_USB_DIRECT_MON_NAME, Boolean.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.BOOLEAN_IN_BYTE,
      new byte[]{0x7F, 0x00, 0x06, 0x00}, new byte[]{0x00, 0x00, 0x00, 0x01}, null));
    addRQ1Request (COMMAND_USB_DIRECT_MON_NAME);
    // I do not know what this parameter does... But i'd like to try...
    registerParameter (new ParameterDescriptor_RolandBoss (COMMAND_USB_IN_OUT_MODE_NAME, Integer.class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.INT_IN_BYTE,
      new byte[]{0x7F, 0x00, 0x06, 0x01}, new byte[]{0x00, 0x00, 0x00, 0x01}, null));
    addRQ1Request (COMMAND_USB_IN_OUT_MODE_NAME);
    
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PARAMETERS - TODO
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private void registerParameters_Me80_TODO ()
  {
        
//    
//    // See comment above with ctl_target: size is two instead of unity on both modulation_bpm and delay_bpm.
//    registerParameter (new ParameterDescriptor_RolandBoss ("modulation_bpm",
//      new byte[]{0x20, 0x00, 0x00, 0x3B}, new byte[]{0x00, 0x00, 0x00, /* 0x01 */ 0x02}));
//    registerParameter (new ParameterDescriptor_RolandBoss ("delay_bpm",
//      new byte[]{0x20, 0x00, 0x00, 0x3D}, new byte[]{0x00, 0x00, 0x00, /* 0x01 */ 0x02}));
//    
//    registerParameter (new ParameterDescriptor_RolandBoss ("value_dummy_1",
//      new byte[]{0x20, 0x00, 0x00, 0x3F}, new byte[]{0x00, 0x00, 0x00, /* 0x01 */ 0x02}));
//    registerParameter (new ParameterDescriptor_RolandBoss ("value_dummy_2",
//      new byte[]{0x20, 0x00, 0x00, 0x41}, new byte[]{0x00, 0x00, 0x00, /* 0x01 */ 0x02}));
//    registerParameter (new ParameterDescriptor_RolandBoss ("value_dummy_3",
//      new byte[]{0x20, 0x00, 0x00, 0x43}, new byte[]{0x00, 0x00, 0x00, /* 0x01 */ 0x02}));
//    registerParameter (new ParameterDescriptor_RolandBoss ("value_dummy_4",
//      new byte[]{0x20, 0x00, 0x00, 0x45}, new byte[]{0x00, 0x00, 0x00, /* 0x01 */ 0x02}));
//    
////  "command": [
////    {
////      "name": "editor_comunication_level",
////      "address": "7F,00,00,00",
////      "size": "00,00,00,01"
////    },
////    {
////      "name": "editor_comunication_mode",
////      "address": "7F,00,00,01",
////      "size": "00,00,00,01"
////    },
////    {
////      "name": "running_mode",
////      "address": "7F,00,00,02",
////      "size": "00,00,00,01"
////    },
////    {
////      "name": "patch_copy",
////      "address": "7F,00,01,00",
////      "size": "00,00,00,04"
////    },
////    {
////      "name": "patch_write",
////      "address": "7F,00,01,04",
////      "size": "00,00,00,02"
////    },
////    {
////      "name": "patch_initialize",
////      "address": "7F,00,01,06",
////      "size": "00,00,00,01"
////    },
////    {
////      "name": "tuner_single_mode_pitch",
////      "address": "7F,00,05,00",
////      "size": "00,00,00,02"
////    },
////    {
////      "name": "usb_direct_mon",
////      "address": "7F,00,06,00",
////      "size": "00,00,00,01"
////    },
////    {
////      "name": "usb_in_out_mode",
////      "address": "7F,00,06,01",
////      "size": "00,00,00,01"
////    },
////    {
////      "name": "update_patch_map",
////      "address": "7F,71,00,00",
////      "size": "00,00,00,01"
////    },
//
//    registerParameter (new ParameterDescriptor_RolandBoss ("command_7f0000xx", /* XXX DOES NOT WORK XXX */
//      new byte[]{0x7F, 0x00, 0x00, 0x00}, new byte[]{0x00, 0x00, 0x00, 0x03}));
//    registerParameter (new ParameterDescriptor_RolandBoss ("editor_comunication_level",
//      new byte[]{0x7F, 0x00, 0x00, 0x00}, new byte[]{0x00, 0x00, 0x00, 0x01}));
//    registerParameter (new ParameterDescriptor_RolandBoss ("editor_comunication_mode", /* XXX DOES NOT WORK XXX */
//      new byte[]{0x7F, 0x00, 0x00, 0x01}, new byte[]{0x00, 0x00, 0x00, 0x01}));
//    registerParameter (new ParameterDescriptor_RolandBoss ("running_mode", /* XXX DOES NOT WORK XXX */
//      new byte[]{0x7F, 0x00, 0x00, 0x02}, new byte[]{0x00, 0x00, 0x00, 0x01}));
//    
//    registerParameter (new ParameterDescriptor_RolandBoss ("command_7f0001xx", /* XXX DOES NOT WORK XXX */
//      new byte[]{0x7F, 0x00, 0x01, 0x00}, new byte[]{0x00, 0x00, 0x00, 0x07}));
//    registerParameter (new ParameterDescriptor_RolandBoss ("patch_copy",
//      new byte[]{0x7F, 0x00, 0x01, 0x00}, new byte[]{0x00, 0x00, 0x00, 0x04}));
//    registerParameter (new ParameterDescriptor_RolandBoss ("patch_write",
//      new byte[]{0x7F, 0x00, 0x01, 0x04}, new byte[]{0x00, 0x00, 0x00, 0x02}));
//    registerParameter (new ParameterDescriptor_RolandBoss ("patch_initialize",
//      new byte[]{0x7F, 0x00, 0x01, 0x06}, new byte[]{0x00, 0x00, 0x00, 0x01}));
//    
//    registerParameter (new ParameterDescriptor_RolandBoss ("command_7f0005xx", /* XXX DOES NOT WORK XXX */
//      new byte[]{0x7F, 0x00, 0x05, 0x00}, new byte[]{0x00, 0x00, 0x00, 0x02}));
//    registerParameter (new ParameterDescriptor_RolandBoss ("tuner_single_mode_pitch", /* XXX DOES NOT WORK XXX */
//      new byte[]{0x7F, 0x00, 0x05, 0x00}, new byte[]{0x00, 0x00, 0x00, 0x02}));
//    
//    registerParameter (new ParameterDescriptor_RolandBoss ("command_7f0006xx", /* XXX DOES NOT WORK XXX */
//      new byte[]{0x7F, 0x00, 0x06, 0x00}, new byte[]{0x00, 0x00, 0x00, 0x02}));
//    registerParameter (new ParameterDescriptor_RolandBoss ("usb_direct_mon",
//      new byte[]{0x7F, 0x00, 0x06, 0x00}, new byte[]{0x00, 0x00, 0x00, 0x01}));
//    registerParameter (new ParameterDescriptor_RolandBoss ("usb_in_out_mode",
//      new byte[]{0x7F, 0x00, 0x06, 0x01}, new byte[]{0x00, 0x00, 0x00, 0x01}));
//    
//    registerParameter (new ParameterDescriptor_RolandBoss ("command_7f7100xx", /* XXX DOES NOT WORK XXX */
//      new byte[]{0x7F, 0x71, 0x00, 0x00}, new byte[]{0x00, 0x00, 0x00, 0x01}));
//    registerParameter (new ParameterDescriptor_RolandBoss ("update_patch_map", /* XXX DOES NOT WORK XXX */
//      new byte[]{0x7F, 0x71, 0x00, 0x00}, new byte[]{0x00, 0x00, 0x00, 0x01}));
//        
//    registerParameter (new ParameterDescriptor_RolandBoss ("patch_map", /* XXX DOES NOT WORK XXX */
//      new byte[]{0x7F, 0x70, 0x00, 0x00}, new byte[]{0x00, 0x00, 0x00, 0x48}));
//    registerParameter (new ParameterDescriptor_RolandBoss ("patch_map_u1_1", /* XXX DOES NOT WORK XXX */
//      new byte[]{0x7F, 0x70, 0x00, 0x00}, new byte[]{0x00, 0x00, 0x00, 0x02}));
//
    
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // UPDATE FROM [MIDI] DEVICE
  // FROM SUPER CLASS
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  @Override
  protected void updatePatchNoFromDevice (final byte[] value)
  {
    // LOG.log (Level.INFO, "New patch no: {0}.", HexUtils.bytesToHex (value));
    synchronized (this)
    {
      super.updatePatchNoFromDevice (value);
      for (final String key : CURRENT_PATCH_NO_RAW_SUB_KEYS)
      {
        final ParameterDescriptor_RolandBoss pd = getParameterDescriptor (key);
        final ParameterDescriptor_RolandBoss pd_parent = getParameterDescriptor (pd.getParentKey ());
        final byte[] newValue = new byte[pd.getLength ()];
        final int offset = pd.getAddress () - pd_parent.getAddress ();
        System.arraycopy (value, offset, newValue, 0, pd.getLength ());
        onParameterReadFromDevice (key, newValue);
      }
    }
  }

  @Override
  protected void updateSystemSettingsFromDevice (final byte[] value)
  {
    // LOG.log (Level.INFO, "New system settings: {0}.", HexUtils.bytesToHex (value));
    synchronized (this)
    {
      super.updateSystemSettingsFromDevice (value);
      for (final String key : SYSTEM_SUB_KEYS)
      {
        final ParameterDescriptor_RolandBoss pd = getParameterDescriptor (key);
        final ParameterDescriptor_RolandBoss pd_parent = getParameterDescriptor (pd.getParentKey ());
        final byte[] newValue = new byte[pd.getLength ()];
        final int offset = pd.getAddress () - pd_parent.getAddress ();
        System.arraycopy (value, offset, newValue, 0, pd.getLength ());
        onParameterReadFromDevice (key, newValue);
      }
    }
  }

  @Override
  protected void updateTemporaryPatchFromDevice (final byte[] value)
  {
    // LOG.log (Level.INFO, "New temporary patch: {0}.", HexUtils.bytesToHex (value));
    synchronized (this)
    {
      super.updateTemporaryPatchFromDevice (value);
      for (final String key : TEMPORARY_PATCH_SUB_KEYS)
      {
        final ParameterDescriptor_RolandBoss pd = getParameterDescriptor (key);
        final ParameterDescriptor_RolandBoss pd_parent = getParameterDescriptor (pd.getParentKey ());
        final byte[] newValue = new byte[pd.getLength ()];
        final int offset = pd.getAddress () - pd_parent.getAddress ();
        System.arraycopy (value, offset, newValue, 0, pd.getLength ());
        onParameterReadFromDevice (key, newValue);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // WRITE PATCH TO DEVICE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /** Writes a patch to a specific user patch bank on the ME-80.
   * 
   * @param patch             The patch, non-{@code null} and of proper size.
   * @param targetBank        The bank to which to write, must be non-{@code null} and a user bank (U1 through U9).
   * @param targetPatchInBank The patch to write (in the specific bank), non-{@code null}.
   * 
   * @throws IllegalArgumentException If any argument is {@code null}.
   *                                  the byte array is not of the right size,
   *                                  or a preset bank has been supplied.
   * 
   * @see MidiDevice_Me80_Base#PATCH_SIZE
   * 
   */
  public final void writePatchToDevice
  ( final byte[] patch,
    final JMe80Panel_PatchSelector.ME80_BANK targetBank,
    final JMe80Panel_PatchSelector.ME80_PATCH_IN_BANK targetPatchInBank)
  {
    if (patch == null || patch.length != MidiDevice_Me80_Base.PATCH_SIZE)
      throw new IllegalArgumentException ();
    if (targetBank == null || ! targetBank.isUserBank ())
      throw new IllegalArgumentException ();
    if (targetPatchInBank == null)
      throw new IllegalArgumentException ();
    // LOG.log (Level.INFO, "Writing {0} to {1}.{2}.", new Object[]{HexUtils.bytesToHex (patch), targetBank, targetPatchInBank});
    put (toParameterName (targetBank, targetPatchInBank), patch);
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
