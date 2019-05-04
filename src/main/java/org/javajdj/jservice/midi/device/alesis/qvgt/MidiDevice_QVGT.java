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
package org.javajdj.jservice.midi.device.alesis.qvgt;

import java.time.Duration;
import java.time.Instant;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import org.javajdj.jservice.midi.device.AbstractMidiDevice;
import org.javajdj.jservice.midi.device.MidiDevice;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.javajdj.jservice.Service;
import org.javajdj.util.hex.HexUtils;
import org.javajdj.jservice.midi.MidiService;
import org.javajdj.jservice.midi.device.MidiDeviceListener;

/** Implementation of {@link MidiDevice} for the Alesis Quadraverb GT.
 *
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 *
 */
public class MidiDevice_QVGT
  extends AbstractMidiDevice<ParameterDescriptor_QVGT>
  implements MidiDevice
{

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // LOGGING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private static final Logger LOG = Logger.getLogger (MidiDevice_QVGT.class.getName ());

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTORS / FACTORIES / CLONING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /** Creates the {@link MidiDevice} for the Alesis Quadraverb GT.
   *
   * <p>
   * Registers all parameters as well as a {@link Runnable} doing the main request (for Edit-Buffer contents, a.o.) loop
   * and one as a watchdog.
   * 
   * @param midiService The {@link MidiService} to use.
   *
   */
  public MidiDevice_QVGT (final MidiService midiService)
  {
    super (midiService);
    registerParameters ();
    addRunnable (this.qvgtMainRequestLoop);
    addRunnable (this.qvgtWatchdog);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PARENT [VALUE] VALIDATORS
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private static final class ONLY_CONFIGS
    implements Function<Object, Boolean>
  {
    
    private final EnumSet<Patch_QGVT.Configuration> configurations;

    public ONLY_CONFIGS (final EnumSet<Patch_QGVT.Configuration> configurations)
    {
      if (configurations == null)
        throw new IllegalArgumentException ();
      this.configurations = configurations;
    }

    @Override
    public final Boolean apply (final Object t)
    {
      return (t != null)
        && (t instanceof Patch_QGVT)
        && this.configurations.contains (((Patch_QGVT) t).getConfiguration ());
    }
        
  }
  
  private static final class ONLY_CONFIGS14_WITH_PITCH_MODES
    implements Function<Object, Boolean>
  {
    
    private static final EnumSet<Patch_QGVT.Configuration> CONFIGURATIONS = EnumSet.of (
      Patch_QGVT.Configuration.C1_EQ_PCH_DL_REV,
      Patch_QGVT.Configuration.C4_5EQ_PCH_DL);
    
    private final EnumSet<MidiDevice_QVGT.PitchMode> pitchModes;

    public ONLY_CONFIGS14_WITH_PITCH_MODES (final EnumSet<MidiDevice_QVGT.PitchMode> pitchModes)
    {
      if (pitchModes == null)
        throw new IllegalArgumentException ();
      this.pitchModes = pitchModes;
    }

    @Override
    public final Boolean apply (final Object t)
    {
      return (t != null)
        && (t instanceof Patch_QGVT)
        && CONFIGURATIONS.contains (((Patch_QGVT) t).getConfiguration ())
        && this.pitchModes.contains (((Patch_QGVT) t).getPitchMode_Configs14 ());
    }
        
  }
  
  private static final class ONLY_CONFIG1_WITH_EQ_MODES
    implements Function<Object, Boolean>
  {
    
    private static final EnumSet<Patch_QGVT.Configuration> CONFIGURATIONS = EnumSet.of (Patch_QGVT.Configuration.C1_EQ_PCH_DL_REV);
    
    private final EnumSet<MidiDevice_QVGT.EqModeConfig1> eqModes;

    public ONLY_CONFIG1_WITH_EQ_MODES (final EnumSet<MidiDevice_QVGT.EqModeConfig1> eqModes)
    {
      if (eqModes == null)
        throw new IllegalArgumentException ();
      this.eqModes = eqModes;
    }

    @Override
    public final Boolean apply (final Object t)
    {
      return (t != null)
        && (t instanceof Patch_QGVT)
        && CONFIGURATIONS.contains (((Patch_QGVT) t).getConfiguration ())
        && this.eqModes.contains (((Patch_QGVT) t).getEqMode_Config1 ());
    }
        
  }
  
  private static final class ONLY_CONFIG4_WITH_EQ_MODES
    implements Function<Object, Boolean>
  {
    
    private static final EnumSet<Patch_QGVT.Configuration> CONFIGURATIONS = EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL);
    
    private final EnumSet<MidiDevice_QVGT.EqModeConfig4> eqModes;

    public ONLY_CONFIG4_WITH_EQ_MODES (final EnumSet<MidiDevice_QVGT.EqModeConfig4> eqModes)
    {
      if (eqModes == null)
        throw new IllegalArgumentException ();
      this.eqModes = eqModes;
    }

    @Override
    public final Boolean apply (final Object t)
    {
      return (t != null)
        && (t instanceof Patch_QGVT)
        && CONFIGURATIONS.contains (((Patch_QGVT) t).getConfiguration ())
        && this.eqModes.contains (((Patch_QGVT) t).getEqMode_Config4 ());
    }
        
  }
  
  private static final class ONLY_CONFIGS1267_WITH_REVERB_MODES
    implements Function<Object, Boolean>
  {
    
    private static final EnumSet<Patch_QGVT.Configuration> CONFIGURATIONS = EnumSet.of (
      Patch_QGVT.Configuration.C1_EQ_PCH_DL_REV,
      Patch_QGVT.Configuration.C2_LES_DL_REV,
      Patch_QGVT.Configuration.C6_RING_DL_REV,
      Patch_QGVT.Configuration.C7_RESO_DL_REV);
    
    private final EnumSet<MidiDevice_QVGT.ReverbMode> reverbModes;

    public ONLY_CONFIGS1267_WITH_REVERB_MODES (final EnumSet<MidiDevice_QVGT.ReverbMode> reverbModes)
    {
      if (reverbModes == null)
        throw new IllegalArgumentException ();
      this.reverbModes = reverbModes;
    }

    @Override
    public final Boolean apply (final Object t)
    {
      return (t != null)
        && (t instanceof Patch_QGVT)
        && CONFIGURATIONS.contains (((Patch_QGVT) t).getConfiguration ())
        && this.reverbModes.contains (((Patch_QGVT) t).getReverbMode ());
    }
        
  }
  
  private static final class ONLY_CONFIG5_WITH_REVERB_MODES
    implements Function<Object, Boolean>
  {
    
    private static final EnumSet<Patch_QGVT.Configuration> CONFIGURATIONS = EnumSet.of (Patch_QGVT.Configuration.C5_3EQ_REV);
    
    private final EnumSet<MidiDevice_QVGT.ReverbMode> reverbModes;

    public ONLY_CONFIG5_WITH_REVERB_MODES (final EnumSet<MidiDevice_QVGT.ReverbMode> reverbModes)
    {
      if (reverbModes == null)
        throw new IllegalArgumentException ();
      this.reverbModes = reverbModes;
    }

    @Override
    public final Boolean apply (final Object t)
    {
      return (t != null)
        && (t instanceof Patch_QGVT)
        && CONFIGURATIONS.contains (((Patch_QGVT) t).getConfiguration ())
        && this.reverbModes.contains (((Patch_QGVT) t).getReverbMode ());
    }
        
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PARAMETER REGISTRATION
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private void registerParameters ()
  {
    
    registerParameters_CurrentPatchNumber ();
    registerParameters_EditBuffer ();
    registerParameters_EditBuffer_NameConfig ();
    registerParameters_EditBuffer_Preamp ();
    registerParameters_EditBuffer_Eq ();
    registerParameters_EditBuffer_Pitch ();
    registerParameters_EditBuffer_Delay ();
    registerParameters_EditBuffer_Reverb ();
    registerParameters_EditBuffer_Mix ();
    registerParameters_EditBuffer_Modulation ();

  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PARAMETER REGISTRATION - CURRENT PATCH NUMBER
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public final static String CURRENT_PATCH_NO_NAME = "qvgt.current_patch_no";
  
  private void registerParameters_CurrentPatchNumber ()
  {

    registerParameter (new ParameterDescriptor_QVGT (MidiDevice_QVGT.CURRENT_PATCH_NO_NAME));

  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PARAMETER REGISTRATION - EDIT BUFFER
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public final static String EDIT_BUFFER_NAME = "qvgt.edit_buffer";

  public final static int EDIT_BUFFER_PROGRAM_NUMBER = 100;

  private void registerParameters_EditBuffer ()
  {

    registerParameter (new ParameterDescriptor_QVGT (
      MidiDevice_QVGT.EDIT_BUFFER_NAME, MidiDevice_QVGT.EDIT_BUFFER_PROGRAM_NUMBER));

  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PARAMETER REGISTRATION - EDIT BUFFER - NAME/CONFIG
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public final static String EDIT_BUFFER_NAME_NAME   = EDIT_BUFFER_NAME + ".name";
  public final static String EDIT_BUFFER_CONFIG_NAME = EDIT_BUFFER_NAME + ".config";

  private void registerParameters_EditBuffer_NameConfig ()
  {

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_NAME_NAME,
      String.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.FIXED_US_ASCII_STRING_IN_BYTES,
      null, /* function */
      null, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x6A, /* offset */
      14, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null /* customValueConverter */));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_CONFIG_NAME,
      Patch_QGVT.Configuration.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_CONFIG,
      0, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x44, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null /* customValueConverter */));

  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PARAMETER REGISTRATION - EDIT BUFFER - PREAMP
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public final static String EDIT_BUFFER_PREAMP_NAME = EDIT_BUFFER_NAME + ".preamp";
  
  public final static String EDIT_BUFFER_PREAMP_COMPRESSION_NAME     = EDIT_BUFFER_PREAMP_NAME + ".compression";
  public final static String EDIT_BUFFER_PREAMP_OVERDRIVE_NAME       = EDIT_BUFFER_PREAMP_NAME + ".overdrive";
  public final static String EDIT_BUFFER_PREAMP_DISTORTION_NAME      = EDIT_BUFFER_PREAMP_NAME + ".distortion";
  public final static String EDIT_BUFFER_PREAMP_TONE_NAME            = EDIT_BUFFER_PREAMP_NAME + ".tone";
  public final static String EDIT_BUFFER_PREAMP_BASS_BOOST_NAME      = EDIT_BUFFER_PREAMP_NAME + ".bassBoost";
  public final static String EDIT_BUFFER_PREAMP_CAB_SIMULATOR_NAME   = EDIT_BUFFER_PREAMP_NAME + ".cabSimulator";
  public final static String EDIT_BUFFER_PREAMP_EFFECT_LOOP_NAME     = EDIT_BUFFER_PREAMP_NAME + ".effectLoop";
  public final static String EDIT_BUFFER_PREAMP_NOISE_GATE_RAW_NAME  = EDIT_BUFFER_PREAMP_NAME + ".noiseGateRaw";
  public final static String EDIT_BUFFER_PREAMP_NOISE_GATE_NAME      = EDIT_BUFFER_PREAMP_NAME + ".noiseGate";
  public final static String EDIT_BUFFER_PREAMP_OUTPUT_LEVEL_NAME    = EDIT_BUFFER_PREAMP_NAME + ".outputLevel";

  /** The PREAMP TONE type.
   * 
   * @see #EDIT_BUFFER_PREAMP_TONE_NAME
   * 
   */
  public enum PreampTone
  {
    FLAT,
    PRESENCE,
    BRIGHT;
  }

  /** The PREAMP CAB SIMULATOR type.
   * 
   * @see #EDIT_BUFFER_PREAMP_CAB_SIMULATOR_NAME
   * 
   */  
  public enum CabSimulator
  {
    OFF,
    CAB1_2_10INCH,
    CAB2_4_12INCH;
  }
  
  /** The PREAMP NOISE GATE settings type.
   * 
   * <p>
   * The PREAMP NOISE GATE setting is effectively a 18-valued enum
   * located in 5 consecutive bits at address {@code 0x7E}.
   * But the first value corresponds to the AUTO mode, whereas the
   * rest refers to MANUAL mode.
   * For GUI purposes, a more complex description of the PREAMP NOISE GATE setting
   * is therefore required.
   * 
   * <p>
   * Objects of this class are immutable.
   * 
   * @see #EDIT_BUFFER_PREAMP_NOISE_GATE_RAW_NAME
   * @see #EDIT_BUFFER_PREAMP_NOISE_GATE_NAME
   * 
   */
  public final static class NoiseGate
  {
    
    /** When entering {@link Mode#MANUAL}, this value is chosen as default for the noise-gate setting.
     * 
     * <p>
     * Precisely "half-way".
     * 
     * <p>
     * Note that the value is the same as the one shown on the Alesis Quadraverb GT GUI.
     * 
     */
    public static final int DEFAULT_MANUAL_NOISE_GATE = 8; // GUI value; byte value -> 9!
    
    private final byte b;
    
    private NoiseGate (final byte b)
    {
      if (b < 0 || b > 17)
        throw new IllegalArgumentException ();
      this.b = b;
    }
    
    private NoiseGate (final Mode mode)
    {
      if (mode == null)
        throw new IllegalArgumentException ();
      switch (mode)
      {
        case AUTO:
          this.b = 0;
          break;
        case OFF:
          this.b = 1;
          break;
        case MANUAL:
          this.b = DEFAULT_MANUAL_NOISE_GATE + 1;
          break;
        default:
          throw new IllegalArgumentException ();
      }
    }
    
    private NoiseGate (final int value)
    {
      if (value < 0 || value > 16)
        throw new IllegalArgumentException ();
      this.b = (byte) (value + 1);
    }
    
    private static NoiseGate fromByte (final byte b)
    {
      return new NoiseGate (b);
    }
    
    private byte toByte ()
    {
      if (this.b < 0 || this.b > 17)
        throw new IllegalArgumentException ();
      return this.b;
    }
    
    /** Creates a new NOISE GATE settings object from given noise-gate mode.
     * 
     * <p>
     * In case the mode is set to {@link Mode#MANUAL},
     * the noise-gate (user) value is set to {@link #DEFAULT_MANUAL_NOISE_GATE}.
     * 
     * @param mode The mode, non-{@code null}.
     * 
     * @return The NOISE GATE settings object.
     * 
     * @throws IllegalArgumentException If {@code mode == null}.
     * 
     */
    public static NoiseGate fromMode (final Mode mode)
    {
      return new NoiseGate (mode);
    }
    
    /** Creates a new NOISE GATE settings object from given noise-gate (user) value.
     * 
     * <p>
     * In case the user value is zero,
     * the mode is set to {@link Mode#OFF},
     * and to {@link Mode#MANUAL} otherwise.
     * 
     * @param value The noise-gate (user) value, between 0 and 16 inclusive.
     * 
     * @return The NOISE GATE settings object.
     * 
     * @throws IllegalArgumentException If {@code value < 0} or {@code value > 16}.
     * 
     */
    public final static NoiseGate fromValue (final int value)
    {
      return new NoiseGate (value);
    }
    
    /** The NOISE GATE mode (AUTO, OFF or MANUAL).
     * 
     */
    public enum Mode
    {
      AUTO,
      OFF,
      MANUAL; 
    }
    
    /** Returns the noise-gate mode corresponding to this settings object.
     * 
     * @return The noise-gate mode (AUTO, OFF, or MANUAL).
     * 
     * @see #getValue
     * 
     */
    public final Mode getMode ()
    {
      if (this.b < 0 || this.b > 17)
        throw new RuntimeException ();
      switch (this.b)
      {
        case 0:
          return Mode.AUTO;
        case 1:
          return Mode.OFF;
        default:
          return Mode.MANUAL;
      }
    }
    
    /** Returns the user value of the noise gate.
     * 
     * <p>
     * Note that zero is returned if the mode is {@link Mode#OFF}.
     * 
     * @return The user value of the noise gate between 0 and 16, {@code null} if the mode is {@link Mode#AUTO}.
     * 
     * @see #getMode
     * 
     */
    public final Integer getValue ()
    {
      if (this.b < 0 || this.b > 17)
        throw new RuntimeException ();
      switch (this.b)
      {
        case 0:
          return null;
        default:
          return this.b - 1;
      }      
    }

    @Override
    public final int hashCode ()
    {
      int hash = 7;
      hash = 23 * hash + this.b;
      return hash;
    }

    @Override
    public final boolean equals (Object obj)
    {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass () != obj.getClass ())
        return false;
      final NoiseGate other = (NoiseGate) obj;
      return this.b == other.b;
    }
    
  }
  
  private final class NoiseGateConverter
    implements ParameterDescriptor_QVGT.CustomValueConverter<NoiseGate> 
  {

    @Override
    public final NoiseGate fromDevice (final byte[] bytes)
    {
      if (bytes == null || bytes.length != 1)
        throw new IllegalArgumentException ();
      return NoiseGate.fromByte (bytes[0]);
    }

    @Override
    public final byte[] toDevice (final NoiseGate c)
    {
      if (c == null)
        throw new IllegalArgumentException ();
      return new byte[]{c.toByte ()};
    }
    
  }
  
  private void registerParameters_EditBuffer_Preamp ()
  {

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_PREAMP_COMPRESSION_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_PREAMP,
      0, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x7C, /* offset */
      1, /* size */
      1, /* bitOffset */
      3, /* bitSize */
      EDIT_BUFFER_NAME, /* parentKey */
      null /* customValueConverter */));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_PREAMP_OVERDRIVE_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_PREAMP,
      1, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x7E, /* offset */
      1, /* size */
      0, /* bitOffset */
      3, /* bitSize */
      EDIT_BUFFER_NAME, /* parentKey */
      null /* customValueConverter */));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_PREAMP_DISTORTION_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_PREAMP,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x7C, /* offset */
      1, /* size */
      4, /* bitOffset */
      4, /* bitSize */
      EDIT_BUFFER_NAME, /* parentKey */
      null /* customValueConverter */));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_PREAMP_TONE_NAME,
      PreampTone.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_PREAMP,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x7D, /* offset */
      1, /* size */
      4, /* bitOffset */
      2, /* bitSize */
      EDIT_BUFFER_NAME, /* parentKey */
      null /* customValueConverter */));
        
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_PREAMP_BASS_BOOST_NAME,
      Boolean.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.BOOLEAN_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_PREAMP,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x7D, /* offset */
      1, /* size */
      3, /* bitOffset */
      1, /* bitSize */
      EDIT_BUFFER_NAME, /* parentKey */
      null /* customValueConverter */));
        
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_PREAMP_CAB_SIMULATOR_NAME,
      CabSimulator.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_PREAMP,
      5, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x7D, /* offset */
      1, /* size */
      6, /* bitOffset */
      2, /* bitSize */
      EDIT_BUFFER_NAME, /* parentKey */
      null /* customValueConverter */));
        
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_PREAMP_EFFECT_LOOP_NAME,
      Boolean.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.BOOLEAN_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_PREAMP,
      6, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x7D, /* offset */
      1, /* size */
      2, /* bitOffset */
      1, /* bitSize */
      EDIT_BUFFER_NAME, /* parentKey */
      null /* customValueConverter */));
        
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_PREAMP_NOISE_GATE_RAW_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_PREAMP,
      7, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x7E, /* offset */
      1, /* size */
      3, /* bitOffset */
      5, /* bitSize */
      EDIT_BUFFER_NAME, /* parentKey */
      null /* customValueConverter */));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_PREAMP_NOISE_GATE_NAME,
      NoiseGate.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.CUSTOM,
      ParameterDescriptor_QVGT.Function_QVGT.F_PREAMP,
      7, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x7E, /* offset */
      1, /* size */
      3, /* bitOffset */
      5, /* bitSize */
      EDIT_BUFFER_NAME, /* parentKey */
      new NoiseGateConverter () /* customValueConverter */));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_PREAMP_OUTPUT_LEVEL_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_PREAMP,
      8, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x7F, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.complementOf (EnumSet.of (Patch_QGVT.Configuration.C8_SAMPLING)))));
    
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PARAMETER REGISTRATION - EDIT BUFFER - EQ
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public final static String EDIT_BUFFER_EQ_NAME = EDIT_BUFFER_NAME + ".eq";

  public final static String EDIT_BUFFER_EQ_PRESET_NAME = EDIT_BUFFER_EQ_NAME + ".preset"; // Configs 1, 3, 4, and 5.
  
  public final static String EDIT_BUFFER_EQ_CF1_MODE_NAME         = EDIT_BUFFER_EQ_NAME + ".cf1.mode";
  public final static String EDIT_BUFFER_EQ_CF1_3B_LOW_F_NAME     = EDIT_BUFFER_EQ_NAME + ".cf1.3beq.low.freq";
  public final static String EDIT_BUFFER_EQ_CF1_3B_LOW_AMP_NAME   = EDIT_BUFFER_EQ_NAME + ".cf1.3beq.low.amp";
  public final static String EDIT_BUFFER_EQ_CF1_3B_MID_F_NAME     = EDIT_BUFFER_EQ_NAME + ".cf1.3beq.mid.freq";
  public final static String EDIT_BUFFER_EQ_CF1_3B_MID_BW_NAME    = EDIT_BUFFER_EQ_NAME + ".cf1.3beq.mid.bw";
  public final static String EDIT_BUFFER_EQ_CF1_3B_MID_AMP_NAME   = EDIT_BUFFER_EQ_NAME + ".cf1.3beq.mid.amp";
  public final static String EDIT_BUFFER_EQ_CF1_3B_HIGH_F_NAME    = EDIT_BUFFER_EQ_NAME + ".cf1.3beq.high.freq";
  public final static String EDIT_BUFFER_EQ_CF1_3B_HIGH_AMP_NAME  = EDIT_BUFFER_EQ_NAME + ".cf1.3beq.high.amp";
  public final static String EDIT_BUFFER_EQ_CF1_RESO_1_TUNE_NAME  = EDIT_BUFFER_EQ_NAME + ".cf1.reso.1.tune";
  public final static String EDIT_BUFFER_EQ_CF1_RESO_1_DECAY_NAME = EDIT_BUFFER_EQ_NAME + ".cf1.reso.1.decay";
  public final static String EDIT_BUFFER_EQ_CF1_RESO_1_AMP_NAME   = EDIT_BUFFER_EQ_NAME + ".cf1.reso.1.amp";
  public final static String EDIT_BUFFER_EQ_CF1_RESO_2_TUNE_NAME  = EDIT_BUFFER_EQ_NAME + ".cf1.reso.2.tune";
  public final static String EDIT_BUFFER_EQ_CF1_RESO_2_DECAY_NAME = EDIT_BUFFER_EQ_NAME + ".cf1.reso.2.decay";
  public final static String EDIT_BUFFER_EQ_CF1_RESO_2_AMP_NAME   = EDIT_BUFFER_EQ_NAME + ".cf1.reso.2.amp";
  public final static String EDIT_BUFFER_EQ_CF1_RESO_MID_F_NAME   = EDIT_BUFFER_EQ_NAME + ".cf1.reso.mid.freq";
  public final static String EDIT_BUFFER_EQ_CF1_RESO_MID_BW_NAME  = EDIT_BUFFER_EQ_NAME + ".cf1.reso.mid.bw";
  public final static String EDIT_BUFFER_EQ_CF1_RESO_MID_AMP_NAME = EDIT_BUFFER_EQ_NAME + ".cf1.reso.mid.amp";
  
  public final static String EDIT_BUFFER_EQ_CF2_HIGH_ROTOR_LEVEL_NAME = EDIT_BUFFER_EQ_NAME + ".cf2.highRotorLevel";
  
  public final static String EDIT_BUFFER_EQ_CF3_00016_HZ_NAME = EDIT_BUFFER_EQ_NAME + ".cf3.00016Hz";
  public final static String EDIT_BUFFER_EQ_CF3_00032_HZ_NAME = EDIT_BUFFER_EQ_NAME + ".cf3.00032Hz";
  public final static String EDIT_BUFFER_EQ_CF3_00062_HZ_NAME = EDIT_BUFFER_EQ_NAME + ".cf3.00062Hz";
  public final static String EDIT_BUFFER_EQ_CF3_00126_HZ_NAME = EDIT_BUFFER_EQ_NAME + ".cf3.00126Hz";
  public final static String EDIT_BUFFER_EQ_CF3_00250_HZ_NAME = EDIT_BUFFER_EQ_NAME + ".cf3.00250Hz";
  public final static String EDIT_BUFFER_EQ_CF3_00500_HZ_NAME = EDIT_BUFFER_EQ_NAME + ".cf3.00500Hz";
  public final static String EDIT_BUFFER_EQ_CF3_01000_HZ_NAME = EDIT_BUFFER_EQ_NAME + ".cf3.01000Hz";
  public final static String EDIT_BUFFER_EQ_CF3_02000_HZ_NAME = EDIT_BUFFER_EQ_NAME + ".cf3.02000Hz";
  public final static String EDIT_BUFFER_EQ_CF3_04000_HZ_NAME = EDIT_BUFFER_EQ_NAME + ".cf3.04000Hz";
  public final static String EDIT_BUFFER_EQ_CF3_08000_HZ_NAME = EDIT_BUFFER_EQ_NAME + ".cf3.08000Hz";
  public final static String EDIT_BUFFER_EQ_CF3_16000_HZ_NAME = EDIT_BUFFER_EQ_NAME + ".cf3.16000Hz";
  
  public final static String EDIT_BUFFER_EQ_CF4_MODE_NAME            = EDIT_BUFFER_EQ_NAME + ".cf4.mode";
  public final static String EDIT_BUFFER_EQ_CF4_5B_LOW_F_NAME        = EDIT_BUFFER_EQ_NAME + ".cf4.5beq.low.freq";
  public final static String EDIT_BUFFER_EQ_CF4_5B_LOW_AMP_NAME      = EDIT_BUFFER_EQ_NAME + ".cf4.5beq.low.amp";
  public final static String EDIT_BUFFER_EQ_CF4_5B_LOW_MID_F_NAME    = EDIT_BUFFER_EQ_NAME + ".cf4.5beq.lowMid.freq";
  public final static String EDIT_BUFFER_EQ_CF4_5B_LOW_MID_BW_NAME   = EDIT_BUFFER_EQ_NAME + ".cf4.5beq.lowMid.bw";
  public final static String EDIT_BUFFER_EQ_CF4_5B_LOW_MID_AMP_NAME  = EDIT_BUFFER_EQ_NAME + ".cf4.5beq.lowMid.amp";
  public final static String EDIT_BUFFER_EQ_CF4_5B_MID_F_NAME        = EDIT_BUFFER_EQ_NAME + ".cf4.5beq.mid.freq";
  public final static String EDIT_BUFFER_EQ_CF4_5B_MID_BW_NAME       = EDIT_BUFFER_EQ_NAME + ".cf4.5beq.mid.bw";
  public final static String EDIT_BUFFER_EQ_CF4_5B_MID_AMP_NAME      = EDIT_BUFFER_EQ_NAME + ".cf4.5beq.mid.amp";
  public final static String EDIT_BUFFER_EQ_CF4_5B_HIGH_MID_F_NAME   = EDIT_BUFFER_EQ_NAME + ".cf4.5beq.highMid.freq";
  public final static String EDIT_BUFFER_EQ_CF4_5B_HIGH_MID_BW_NAME  = EDIT_BUFFER_EQ_NAME + ".cf4.5beq.highMid.bw";
  public final static String EDIT_BUFFER_EQ_CF4_5B_HIGH_MID_AMP_NAME = EDIT_BUFFER_EQ_NAME + ".cf4.5beq.highMid.amp";
  public final static String EDIT_BUFFER_EQ_CF4_5B_HIGH_F_NAME       = EDIT_BUFFER_EQ_NAME + ".cf4.5beq.high.freq";
  public final static String EDIT_BUFFER_EQ_CF4_5B_HIGH_AMP_NAME     = EDIT_BUFFER_EQ_NAME + ".cf4.5beq.high.amp";
  public final static String EDIT_BUFFER_EQ_CF4_RESO_NUMBER_NAME     = EDIT_BUFFER_EQ_NAME + ".cf4.reso.number";
  public final static String EDIT_BUFFER_EQ_CF4_RESO_1_TUNE_NAME     = EDIT_BUFFER_EQ_NAME + ".cf4.reso.1.tune";
  public final static String EDIT_BUFFER_EQ_CF4_RESO_1_DECAY_NAME    = EDIT_BUFFER_EQ_NAME + ".cf4.reso.1.decay";
  public final static String EDIT_BUFFER_EQ_CF4_RESO_1_AMP_NAME      = EDIT_BUFFER_EQ_NAME + ".cf4.reso.1.amp";
  public final static String EDIT_BUFFER_EQ_CF4_RESO_2_TUNE_NAME     = EDIT_BUFFER_EQ_NAME + ".cf4.reso.2.tune";
  public final static String EDIT_BUFFER_EQ_CF4_RESO_2_DECAY_NAME    = EDIT_BUFFER_EQ_NAME + ".cf4.reso.2.decay";
  public final static String EDIT_BUFFER_EQ_CF4_RESO_2_AMP_NAME      = EDIT_BUFFER_EQ_NAME + ".cf4.reso.2.amp";
  public final static String EDIT_BUFFER_EQ_CF4_RESO_3_TUNE_NAME     = EDIT_BUFFER_EQ_NAME + ".cf4.reso.3.tune";
  public final static String EDIT_BUFFER_EQ_CF4_RESO_3_DECAY_NAME    = EDIT_BUFFER_EQ_NAME + ".cf4.reso.3.decay";
  public final static String EDIT_BUFFER_EQ_CF4_RESO_3_AMP_NAME      = EDIT_BUFFER_EQ_NAME + ".cf4.reso.3.amp";
  public final static String EDIT_BUFFER_EQ_CF4_RESO_4_TUNE_NAME     = EDIT_BUFFER_EQ_NAME + ".cf4.reso.4.tune";
  public final static String EDIT_BUFFER_EQ_CF4_RESO_4_DECAY_NAME    = EDIT_BUFFER_EQ_NAME + ".cf4.reso.4.decay";
  public final static String EDIT_BUFFER_EQ_CF4_RESO_4_AMP_NAME      = EDIT_BUFFER_EQ_NAME + ".cf4.reso.4.amp";
  public final static String EDIT_BUFFER_EQ_CF4_RESO_5_TUNE_NAME     = EDIT_BUFFER_EQ_NAME + ".cf4.reso.5.tune";
  public final static String EDIT_BUFFER_EQ_CF4_RESO_5_DECAY_NAME    = EDIT_BUFFER_EQ_NAME + ".cf4.reso.5.decay";
  public final static String EDIT_BUFFER_EQ_CF4_RESO_5_AMP_NAME      = EDIT_BUFFER_EQ_NAME + ".cf4.reso.5.amp";
  public final static String EDIT_BUFFER_EQ_CF4_RESO_LOW_F_NAME      = EDIT_BUFFER_EQ_NAME + ".cf4.reso.low.freq";
  public final static String EDIT_BUFFER_EQ_CF4_RESO_LOW_AMP_NAME    = EDIT_BUFFER_EQ_NAME + ".cf4.reso.low.amp";
  public final static String EDIT_BUFFER_EQ_CF4_RESO_MID_F_NAME      = EDIT_BUFFER_EQ_NAME + ".cf4.reso.mid.freq";
  public final static String EDIT_BUFFER_EQ_CF4_RESO_MID_BW_NAME     = EDIT_BUFFER_EQ_NAME + ".cf4.reso.mid.bw";
  public final static String EDIT_BUFFER_EQ_CF4_RESO_MID_AMP_NAME    = EDIT_BUFFER_EQ_NAME + ".cf4.reso.mid.amp";
  public final static String EDIT_BUFFER_EQ_CF4_RESO_HIGH_F_NAME     = EDIT_BUFFER_EQ_NAME + ".cf4.reso.high.freq";
  public final static String EDIT_BUFFER_EQ_CF4_RESO_HIGH_AMP_NAME   = EDIT_BUFFER_EQ_NAME + ".cf4.reso.high.amp";
    
  public final static String EDIT_BUFFER_EQ_CF5_LOW_F_NAME     = EDIT_BUFFER_EQ_NAME + ".cf5.low.freq";
  public final static String EDIT_BUFFER_EQ_CF5_LOW_AMP_NAME   = EDIT_BUFFER_EQ_NAME + ".cf5.low.amp";
  public final static String EDIT_BUFFER_EQ_CF5_MID_F_NAME     = EDIT_BUFFER_EQ_NAME + ".cf5.mid.freq";
  public final static String EDIT_BUFFER_EQ_CF5_MID_BW_NAME    = EDIT_BUFFER_EQ_NAME + ".cf5.mid.bw";
  public final static String EDIT_BUFFER_EQ_CF5_MID_AMP_NAME   = EDIT_BUFFER_EQ_NAME + ".cf5.mid.amp";
  public final static String EDIT_BUFFER_EQ_CF5_HIGH_F_NAME    = EDIT_BUFFER_EQ_NAME + ".cf5.high.freq";
  public final static String EDIT_BUFFER_EQ_CF5_HIGH_AMP_NAME  = EDIT_BUFFER_EQ_NAME + ".cf5.high.amp";
  
  public final static String EDIT_BUFFER_EQ_CF7_RESO_GATE_MODE_NAME = EDIT_BUFFER_EQ_NAME + ".cf7.reso.gateMode";
  public final static String EDIT_BUFFER_EQ_CF7_RESO_DECAY_NAME     = EDIT_BUFFER_EQ_NAME + ".cf7.reso.decay";
  public final static String EDIT_BUFFER_EQ_CF7_RESO_1_TUNE_NAME    = EDIT_BUFFER_EQ_NAME + ".cf7.1.tune";
  public final static String EDIT_BUFFER_EQ_CF7_RESO_2_TUNE_NAME    = EDIT_BUFFER_EQ_NAME + ".cf7.2.tune";
  public final static String EDIT_BUFFER_EQ_CF7_RESO_3_TUNE_NAME    = EDIT_BUFFER_EQ_NAME + ".cf7.3.tune";
  public final static String EDIT_BUFFER_EQ_CF7_RESO_4_TUNE_NAME    = EDIT_BUFFER_EQ_NAME + ".cf7.4.tune";
  public final static String EDIT_BUFFER_EQ_CF7_RESO_5_TUNE_NAME    = EDIT_BUFFER_EQ_NAME + ".cf7.5.tune";
  
  public enum EqPreset
  {
    EQ_USER,
    EQ_1,
    EQ_2,
    EQ_3,
    EQ_4,
    EQ_5,
    EQ_6;
  }
  
  /** The eq mode in configuration 1.
   * 
   * @see Patch_QGVT.Configuration#C1_EQ_PCH_DL_REV
   * 
   */
  public enum EqModeConfig1
  {
    /** Triple-band parametric equalizer.
     * 
     */
    EQ3,
    /** Dual resonator and single-band parametric equalizer.
     * 
     */
    RES2_EQ1;
  }
  
  /** The eq mode in configuration 4.
   * 
   * @see Patch_QGVT.Configuration#C4_5EQ_PCH_DL
   * 
   */
  public enum EqModeConfig4
  {
    /** Quintuple-band parametric equalizer.
     * 
     */
    EQ5,
    /** Quintuple resonator and triple-band parametric equalizer.
     * 
     */
    RES5_EQ3;
  }
  
  /** The (selected) resonator number in Config 4 with the appropriate Eq (quintuple resonators) mode.
   * 
   * <p>
   * Only applicable to {@link Patch_QGVT.Configuration#C4_5EQ_PCH_DL}
   * combined with {@link EqModeConfig4#RES5_EQ3}. In this combination,
   * the Alesis Quadraverb GT has 5 (!) resonators available that can be
   * edited on the device by selecting one of the resonators (Resonator Number, or something equivalent),
   * allowing sub-sequent menu entries to refer to <i>only</i> that resonator
   * (i.e., allowing setting <i>that</i> resonator's parameters.
   * A similar approach is taken towards setting parameters of one of 8 (!) TAPs
   * in {@link DelayModeExtended#MULTI_TAP} configurations, when applicable.
   * Note that for both resonators and taps,
   * all parameter values are simultaneously individually available in the patch data,
   * irrespective of the selected resonator or tap.
   * 
   * <p>
   * The problem we encounter in the implementation of the QVGT MIDI is that both
   * the selected resonator from the EQ (if applicable)
   * as well as the selected tap from the DELAY (if applicable)
   * share <i>the same</i> address in the patch data, viz., {@code 0x69}.
   * Noting that both quintuple-resonator mode and the {@link DelayModeExtended#MULTI_TAP} mode
   * are available in {@link Patch_QGVT.Configuration#C4_5EQ_PCH_DL},
   * we are faced with the problem that the byte value in the patch data may actually exceed the
   * admissible range for the resonators (since we have only five resonators and eight tabs).
   * Hence, defining an {@code Enum} <i>only</i> for the five resonators available leads to runtime errors
   * in case higher-numbered TAPs are selected.
   * 
   * <p>
   * Note that this problem is easy to solve on the QVGT because it can show/edit only either
   * the EQ <i>or</i> the DELAY parameters, so there's ample opportunity to "fix" the problem of a too high-numbered
   * (selected) TAP number interpreted as a (selected) RESONATOR number.
   * In addition, the selected resonator or tap clearly has no effect on the signal-processing requirements arising from
   * the patch data (in other words, the signal processing is most likely to ignore {@code 0x69}).
   * 
   * <p>
   * In our implementation, we circumvent the problem outlined above by simple defining eight instead of five
   * resonators, clearly indicating that the upper three are non-existent,
   * and dealing with these values in the code using the {@link MidiDevice},
   * instead of trying to fix it at the device implementation.
   * 
   * <p>
   * XXX In a future revision of this {@link MidiDevice} implementation,
   * we seek to remove the definitions of {@link EqResonatorConfig4}
   * and {@link DelayTap} altogether,
   * as well as the corresponding parameters.
   * Instead,
   * we plan to simply show <i>all</i> five
   * resonator configurations and <i>all</i>
   * eight tap configurations
   * (whenever applicable) simultaneously in the GUI,
   * obviating the need to use either of them.
   * 
   */
  public enum EqResonatorConfig4
  {
    /** The first resonator.
     * 
     */
    RESONATOR_1,
    /** The second resonator.
     * 
     */
    RESONATOR_2,
    /** The third resonator.
     * 
     */
    RESONATOR_3,
    /** The fourth resonator.
     * 
     */
    RESONATOR_4,
    /** The fifth (highest-numbered) resonator.
     * 
     */
    RESONATOR_5,
    /** A non-existent resonator number.
     * 
     * @see EqResonatorConfig4 for an explanation.
     * 
     */
    NX_RESO_6,
    /** A non-existent resonator number.
     * 
     * @see EqResonatorConfig4 for an explanation.
     * 
     */
    NX_RESO_7,
    /** A non-existent resonator number.
     * 
     * @see EqResonatorConfig4 for an explanation.
     * 
     */
    NX_RESO_8;
  }
  
  public enum EqResonatorGateMode // Config 7.
  {
    CONTINUOUS,
    MIDI_GATED;
  }
  
  private void registerParameters_EditBuffer_Eq ()
  {

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_PRESET_NAME,
      EqPreset.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      0, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x1C, /* offset */
      1, /* size */
      0, /* bitOffset */
      7, /* bitSize */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (
        Patch_QGVT.Configuration.C1_EQ_PCH_DL_REV,
        Patch_QGVT.Configuration.C3_GEQ_DL,
        Patch_QGVT.Configuration.C4_5EQ_PCH_DL,
        Patch_QGVT.Configuration.C5_3EQ_REV))));
    
    //////////////
    // Config 1 //
    //////////////
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF1_MODE_NAME,
      EqModeConfig1.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      1, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x7C, /* offset */
      1, /* size */
      0, /* bitOffset */
      1, /* bitSize */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C1_EQ_PCH_DL_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF1_3B_LOW_F_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x00, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG1_WITH_EQ_MODES (EnumSet.of (EqModeConfig1.EQ3))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF1_3B_LOW_AMP_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x02, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG1_WITH_EQ_MODES (EnumSet.of (EqModeConfig1.EQ3))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF1_3B_MID_F_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x04, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG1_WITH_EQ_MODES (EnumSet.of (EqModeConfig1.EQ3))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF1_3B_MID_BW_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      5, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x06, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG1_WITH_EQ_MODES (EnumSet.of (EqModeConfig1.EQ3))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF1_3B_MID_AMP_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      6, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x07, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG1_WITH_EQ_MODES (EnumSet.of (EqModeConfig1.EQ3))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF1_3B_HIGH_F_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      7, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x09, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG1_WITH_EQ_MODES (EnumSet.of (EqModeConfig1.EQ3))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF1_3B_HIGH_AMP_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      8, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x0B, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG1_WITH_EQ_MODES (EnumSet.of (EqModeConfig1.EQ3))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF1_RESO_1_TUNE_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x00, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG1_WITH_EQ_MODES (EnumSet.of (EqModeConfig1.RES2_EQ1))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF1_RESO_1_DECAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x01, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG1_WITH_EQ_MODES (EnumSet.of (EqModeConfig1.RES2_EQ1))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF1_RESO_1_AMP_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x02, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG1_WITH_EQ_MODES (EnumSet.of (EqModeConfig1.RES2_EQ1))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF1_RESO_2_TUNE_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      5, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x03, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG1_WITH_EQ_MODES (EnumSet.of (EqModeConfig1.RES2_EQ1))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF1_RESO_2_DECAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      6, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x09, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG1_WITH_EQ_MODES (EnumSet.of (EqModeConfig1.RES2_EQ1))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF1_RESO_2_AMP_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      7, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x0A, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG1_WITH_EQ_MODES (EnumSet.of (EqModeConfig1.RES2_EQ1))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF1_RESO_MID_F_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      8, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x04, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG1_WITH_EQ_MODES (EnumSet.of (EqModeConfig1.RES2_EQ1))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF1_RESO_MID_BW_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      9, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x06, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG1_WITH_EQ_MODES (EnumSet.of (EqModeConfig1.RES2_EQ1))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF1_RESO_MID_AMP_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      10, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x07, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG1_WITH_EQ_MODES (EnumSet.of (EqModeConfig1.RES2_EQ1))));
    
    //////////////
    // Config 2 //
    //////////////
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF2_HIGH_ROTOR_LEVEL_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      0, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x0D, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C2_LES_DL_REV))));

    //////////////
    // Config 3 //
    //////////////
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF3_00016_HZ_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      1, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x01, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C3_GEQ_DL))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF3_00032_HZ_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x02, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C3_GEQ_DL))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF3_00062_HZ_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x03, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C3_GEQ_DL))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF3_00126_HZ_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x04, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C3_GEQ_DL))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF3_00250_HZ_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      5, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x05, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C3_GEQ_DL))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF3_00500_HZ_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      6, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x06, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C3_GEQ_DL))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF3_01000_HZ_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      7, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x07, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C3_GEQ_DL))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF3_02000_HZ_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      8, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x08, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C3_GEQ_DL))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF3_04000_HZ_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      9, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x09, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C3_GEQ_DL))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF3_08000_HZ_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      10, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x0A, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C3_GEQ_DL))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF3_16000_HZ_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      11, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x0B, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C3_GEQ_DL))));

    //////////////
    // Config 4 //
    //////////////
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_MODE_NAME,
      EqModeConfig4.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      1, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x7C, /* offset */
      1, /* size */
      0, /* bitOffset */
      1, /* bitSize */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_5B_LOW_F_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x00, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.EQ5))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_5B_LOW_AMP_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x02, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.EQ5))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_5B_LOW_MID_F_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x0E, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.EQ5))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_5B_LOW_MID_BW_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      5, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x10, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.EQ5))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_5B_LOW_MID_AMP_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      6, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x11, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.EQ5))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_5B_MID_F_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      7, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x04, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.EQ5))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_5B_MID_BW_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      8, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x06, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.EQ5))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_5B_MID_AMP_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      9, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x07, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.EQ5))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_5B_HIGH_MID_F_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      10, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x13, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.EQ5))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_5B_HIGH_MID_BW_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      11, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x15, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.EQ5))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_5B_HIGH_MID_AMP_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      12, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x16, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.EQ5))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_5B_HIGH_F_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      13, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x09, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.EQ5))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_5B_HIGH_AMP_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      14, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x0B, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.EQ5))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_RESO_NUMBER_NAME,
      EqResonatorConfig4.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x69, /* offset */ // UNDOCUMENTED BUT APPEARS TO BE SAME ADDRESS AS MULTITAP NUMBER! -> XXX NO CANNOT BE RIGHT [CONFIG 4!!]
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.RES5_EQ3))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_RESO_1_TUNE_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x0E, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.RES5_EQ3))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_RESO_1_DECAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x0F, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.RES5_EQ3))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_RESO_1_AMP_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      5, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x4A, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.RES5_EQ3))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_RESO_2_TUNE_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x10, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.RES5_EQ3))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_RESO_2_DECAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x11, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.RES5_EQ3))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_RESO_2_AMP_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      5, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x78, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.RES5_EQ3))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_RESO_3_TUNE_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x12, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.RES5_EQ3))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_RESO_3_DECAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x13, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.RES5_EQ3))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_RESO_3_AMP_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      5, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x79, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.RES5_EQ3))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_RESO_4_TUNE_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x14, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.RES5_EQ3))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_RESO_4_DECAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x15, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.RES5_EQ3))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_RESO_4_AMP_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      5, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x1B, /* offset */
      1, /* size */
      0, /* bitOffset */
      7, /* bitSize */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.RES5_EQ3))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_RESO_5_TUNE_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x16, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.RES5_EQ3))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_RESO_5_DECAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x17, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.RES5_EQ3))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_RESO_5_AMP_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      5, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x28, /* offset */
      1, /* size */
      0, /* bitOffset */
      7, /* bitSize */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.RES5_EQ3))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_RESO_LOW_F_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      6, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x00, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.RES5_EQ3))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_RESO_LOW_AMP_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      7, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x02, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.RES5_EQ3))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_RESO_MID_F_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      8, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x04, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.RES5_EQ3))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_RESO_MID_BW_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      9, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x06, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.RES5_EQ3))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_RESO_MID_AMP_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      10, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x07, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.RES5_EQ3))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_RESO_HIGH_F_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      11, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x09, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.RES5_EQ3))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF4_RESO_HIGH_AMP_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      12, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x0B, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG4_WITH_EQ_MODES (EnumSet.of (EqModeConfig4.RES5_EQ3))));
    
    //////////////
    // Config 5 //
    //////////////
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF5_LOW_F_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      1, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x00, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C5_3EQ_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF5_LOW_AMP_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x02, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C5_3EQ_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF5_MID_F_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x04, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C5_3EQ_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF5_MID_BW_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x06, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C5_3EQ_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF5_MID_AMP_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      5, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x07, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C5_3EQ_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF5_HIGH_F_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      6, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x09, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C5_3EQ_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF5_HIGH_AMP_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      7, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x0B, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C5_3EQ_REV))));
    
    //////////////
    // Config 7 //
    //////////////
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF7_RESO_GATE_MODE_NAME,
      EqResonatorGateMode.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      0, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x25, /* offset */ // XXX GUESSING HERE!!!
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C7_RESO_DL_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF7_RESO_DECAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x1E, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C7_RESO_DL_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF7_RESO_1_TUNE_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x4B, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C7_RESO_DL_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF7_RESO_2_TUNE_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x4C, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C7_RESO_DL_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF7_RESO_3_TUNE_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x4D, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C7_RESO_DL_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF7_RESO_4_TUNE_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      5, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x4E, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C7_RESO_DL_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_EQ_CF7_RESO_5_TUNE_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_EQ,
      6, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x4F, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C7_RESO_DL_REV))));
    
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PARAMETER REGISTRATION - EDIT BUFFER - PITCH
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public final static String EDIT_BUFFER_PITCH_NAME = EDIT_BUFFER_NAME + ".pitch";

  public final static String EDIT_BUFFER_PITCH_CF14_MODE_NAME             = EDIT_BUFFER_PITCH_NAME + ".cf14.mode";
  public final static String EDIT_BUFFER_PITCH_CF14_INPUT_NAME            = EDIT_BUFFER_PITCH_NAME + ".cf14.input";
  public final static String EDIT_BUFFER_PITCH_CF14_CHORUS_WAVESHAPE_NAME = EDIT_BUFFER_PITCH_NAME + ".cf14.chorus.waveshape";
  public final static String EDIT_BUFFER_PITCH_CF14_CHORUS_SPEED_NAME     = EDIT_BUFFER_PITCH_NAME + ".cf14.chorus.speed";
  public final static String EDIT_BUFFER_PITCH_CF14_CHORUS_DEPTH_NAME     = EDIT_BUFFER_PITCH_NAME + ".cf14.chorus.depth";
  public final static String EDIT_BUFFER_PITCH_CF14_CHORUS_FEEDBACK_NAME  = EDIT_BUFFER_PITCH_NAME + ".cf14.chorus.feedback";
  public final static String EDIT_BUFFER_PITCH_CF14_FLANGE_SPEED_NAME     = EDIT_BUFFER_PITCH_NAME + ".cf14.flange.speed";
  public final static String EDIT_BUFFER_PITCH_CF14_FLANGE_DEPTH_NAME     = EDIT_BUFFER_PITCH_NAME + ".cf14.flange.depth";
  public final static String EDIT_BUFFER_PITCH_CF14_FLANGE_FEEDBACK_NAME  = EDIT_BUFFER_PITCH_NAME + ".cf14.flange.feedback";
  public final static String EDIT_BUFFER_PITCH_CF14_FLANGE_TRIGGER_NAME   = EDIT_BUFFER_PITCH_NAME + ".cf14.flange.trigger";
  public final static String EDIT_BUFFER_PITCH_CF14_DETUNE_TUNE_NAME      = EDIT_BUFFER_PITCH_NAME + ".cf14.detune.tune";
  public final static String EDIT_BUFFER_PITCH_CF14_PHASER_SPEED_NAME     = EDIT_BUFFER_PITCH_NAME + ".cf14.phaser.speed";
  public final static String EDIT_BUFFER_PITCH_CF14_PHASER_DEPTH_NAME     = EDIT_BUFFER_PITCH_NAME + ".cf14.phaser.depth";

  public final static String EDIT_BUFFER_PITCH_CF2_LESLIE_SEPARATION_NAME    = EDIT_BUFFER_PITCH_NAME + ".cf2.leslie.separation";
  public final static String EDIT_BUFFER_PITCH_CF2_LESLIE_MOTOR_CONTROL_NAME = EDIT_BUFFER_PITCH_NAME + ".cf2.leslie.motorControl";
  public final static String EDIT_BUFFER_PITCH_CF2_LESLIE_MOTOR_SPEED_NAME   = EDIT_BUFFER_PITCH_NAME + ".cf2.leslie.motorSpeed";
  
  public final static String EDIT_BUFFER_PITCH_CF5_CHORUS_ENABLE_NAME    = EDIT_BUFFER_PITCH_NAME + ".cf5.chorus.enable";
  public final static String EDIT_BUFFER_PITCH_CF5_CHORUS_WAVESHAPE_NAME = EDIT_BUFFER_PITCH_NAME + ".cf5.chorus.waveshape";
  public final static String EDIT_BUFFER_PITCH_CF5_CHORUS_SPEED_NAME     = EDIT_BUFFER_PITCH_NAME + ".cf5.chorus.speed";
  public final static String EDIT_BUFFER_PITCH_CF5_CHORUS_DEPTH_NAME     = EDIT_BUFFER_PITCH_NAME + ".cf5.chorus.depth";

  // XXX Should be public for proper inclusion in javadoc??
  private final static String EBPN = EDIT_BUFFER_PITCH_NAME;
  
  public final static String EDIT_BUFFER_PITCH_CF6_RINGMOD_SPECTRUM_SHIFT_NAME     = EBPN + ".cf6.ringMod.spectrumShift";
  public final static String EDIT_BUFFER_PITCH_CF6_RINGMOD_OUTPUT_SHIFT_MIX_NAME   = EBPN + ".cf6.ringMod.outputShiftMix";
  public final static String EDIT_BUFFER_PITCH_CF6_RINGMOD_DL_REV_SHIFT_MIX_NAME   = EBPN + ".cf6.ringMod.delayReverbShiftMix";
  
  public enum PitchMode
  {
    MONO_CHORUS,
    STEREO_CHORUS,
    MONO_FLANGE,
    STEREO_FLANGE,
    DETUNE,
    PHASER;
  }
  
  public enum PitchInput
  {
    PREAMP,
    EQ;
  }
  
  public enum LfoWaveshape
  {
    TRIANGLE,
    SQUARE;
  }
  
  public enum LeslieSpeed
  {
    SLOW,
    FAST;
  }
  
  private void registerParameters_EditBuffer_Pitch ()
  {

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_PITCH_CF14_MODE_NAME,
      PitchMode.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_PITCH,
      0, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x1A, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (
        Patch_QGVT.Configuration.C1_EQ_PCH_DL_REV,
        Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
        
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_PITCH_CF14_INPUT_NAME,
      PitchInput.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_PITCH,
      1, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x1B, /* offset */
      1, /* size */
      7, /* bitOffset */
      1, /* bitSize */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS14_WITH_PITCH_MODES (EnumSet.allOf (PitchMode.class))));
        
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_PITCH_CF14_CHORUS_WAVESHAPE_NAME,
      LfoWaveshape.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_PITCH,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x1C, /* offset */
      1, /* size */
      7, /* bitOffset */
      1, /* bitSize */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS14_WITH_PITCH_MODES (EnumSet.of (PitchMode.MONO_CHORUS, PitchMode.STEREO_CHORUS))));
        
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_PITCH_CF14_CHORUS_SPEED_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_PITCH,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x1D, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS14_WITH_PITCH_MODES (EnumSet.of (PitchMode.MONO_CHORUS, PitchMode.STEREO_CHORUS))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_PITCH_CF14_CHORUS_DEPTH_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_PITCH,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x1E, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS14_WITH_PITCH_MODES (EnumSet.of (PitchMode.MONO_CHORUS, PitchMode.STEREO_CHORUS))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_PITCH_CF14_CHORUS_FEEDBACK_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_PITCH,
      5, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x20, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS14_WITH_PITCH_MODES (EnumSet.of (PitchMode.MONO_CHORUS, PitchMode.STEREO_CHORUS))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_PITCH_CF14_FLANGE_SPEED_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_PITCH,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x1D, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS14_WITH_PITCH_MODES (EnumSet.of (PitchMode.MONO_FLANGE, PitchMode.STEREO_FLANGE))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_PITCH_CF14_FLANGE_DEPTH_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_PITCH,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x1E, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS14_WITH_PITCH_MODES (EnumSet.of (PitchMode.MONO_FLANGE, PitchMode.STEREO_FLANGE))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_PITCH_CF14_FLANGE_FEEDBACK_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_PITCH,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x20, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS14_WITH_PITCH_MODES (EnumSet.of (PitchMode.MONO_FLANGE, PitchMode.STEREO_FLANGE))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_PITCH_CF14_FLANGE_TRIGGER_NAME,
      Boolean.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.BOOLEAN_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_PITCH,
      5, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x25, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS14_WITH_PITCH_MODES (EnumSet.of (PitchMode.MONO_FLANGE, PitchMode.STEREO_FLANGE))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_PITCH_CF14_DETUNE_TUNE_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_PITCH,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x21, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS14_WITH_PITCH_MODES (EnumSet.of (PitchMode.DETUNE))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_PITCH_CF14_PHASER_SPEED_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_PITCH,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x1D, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS14_WITH_PITCH_MODES (EnumSet.of (PitchMode.PHASER))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_PITCH_CF14_PHASER_DEPTH_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_PITCH,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x1E, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS14_WITH_PITCH_MODES (EnumSet.of (PitchMode.PHASER))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_PITCH_CF2_LESLIE_SEPARATION_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_PITCH,
      0, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x22, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C2_LES_DL_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_PITCH_CF2_LESLIE_MOTOR_CONTROL_NAME,
      Boolean.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.BOOLEAN_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_PITCH,
      1, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x23, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C2_LES_DL_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_PITCH_CF2_LESLIE_MOTOR_SPEED_NAME,
      LeslieSpeed.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_PITCH,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x24, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null /* customValueConverter */,
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C2_LES_DL_REV))));
        
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_PITCH_CF5_CHORUS_ENABLE_NAME,
      Boolean.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.BOOLEAN_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_PITCH,
      0, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x1A, /* offset */
      1, /* size */
      7, /* bitOffset */
      1, /* bitSize */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C5_3EQ_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_PITCH_CF5_CHORUS_WAVESHAPE_NAME,
      LfoWaveshape.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_PITCH,
      1, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x1C, /* offset */
      1, /* size */
      7, /* bitOffset */
      1, /* bitSize */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C5_3EQ_REV))));
        
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_PITCH_CF5_CHORUS_SPEED_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_PITCH,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x1D, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C5_3EQ_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_PITCH_CF5_CHORUS_DEPTH_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_PITCH,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x1E, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C5_3EQ_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_PITCH_CF6_RINGMOD_SPECTRUM_SHIFT_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_PITCH,
      0, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x42, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C6_RING_DL_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_PITCH_CF6_RINGMOD_OUTPUT_SHIFT_MIX_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_PITCH,
      1, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x78, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C6_RING_DL_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_PITCH_CF6_RINGMOD_DL_REV_SHIFT_MIX_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_PITCH,
      1, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x79, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C6_RING_DL_REV))));
    
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PARAMETER REGISTRATION - EDIT BUFFER - DELAY
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public final static String EDIT_BUFFER_DELAY_NAME = EDIT_BUFFER_NAME + ".delay";
  
  public final static String EDIT_BUFFER_DELAY_MODE_NAME          = EDIT_BUFFER_DELAY_NAME + ".mode";
  public final static String EDIT_BUFFER_DELAY_MODE_EXTENDED_NAME = EDIT_BUFFER_DELAY_NAME + ".mode.extended";
  public final static String EDIT_BUFFER_DELAY_INPUT1_NAME        = EDIT_BUFFER_DELAY_NAME + ".input1";
  public final static String EDIT_BUFFER_DELAY_INPUT_NAME         = EDIT_BUFFER_DELAY_NAME + ".input";
  public final static String EDIT_BUFFER_DELAY_CF14_IN_MIX_NAME   = EDIT_BUFFER_DELAY_NAME + ".cf14.inMix";
  public final static String EDIT_BUFFER_DELAY_CF267_IN_MIX_NAME  = EDIT_BUFFER_DELAY_NAME + ".cf267.inMix";
  
  public final static String EDIT_BUFFER_DELAY_CF1_MONO_DELAY_NAME         = EDIT_BUFFER_DELAY_NAME + ".cf1.mono.delay";
  public final static String EDIT_BUFFER_DELAY_CF1_MONO_FEEDBACK_NAME      = EDIT_BUFFER_DELAY_NAME + ".cf1.mono.feedback";
  public final static String EDIT_BUFFER_DELAY_CF1_STEREO_L_DELAY_NAME     = EDIT_BUFFER_DELAY_NAME + ".cf1.stereo.l.delay";
  public final static String EDIT_BUFFER_DELAY_CF1_STEREO_L_FEEDBACK_NAME  = EDIT_BUFFER_DELAY_NAME + ".cf1.stereo.l.feedback";
  public final static String EDIT_BUFFER_DELAY_CF1_STEREO_R_DELAY_NAME     = EDIT_BUFFER_DELAY_NAME + ".cf1.stereo.r.delay";
  public final static String EDIT_BUFFER_DELAY_CF1_STEREO_R_FEEDBACK_NAME  = EDIT_BUFFER_DELAY_NAME + ".cf1.stereo.r.feedback";
  public final static String EDIT_BUFFER_DELAY_CF1_PING_PONG_DELAY_NAME    = EDIT_BUFFER_DELAY_NAME + ".cf1.ping_pong.delay";
  public final static String EDIT_BUFFER_DELAY_CF1_PING_PONG_FEEDBACK_NAME = EDIT_BUFFER_DELAY_NAME + ".cf1.ping_pong.feedback";
  
  public final static String EDIT_BUFFER_DELAY_CF26_MONO_DELAY_NAME         = EDIT_BUFFER_DELAY_NAME + ".cf26.mono.delay";
  public final static String EDIT_BUFFER_DELAY_CF26_MONO_FEEDBACK_NAME      = EDIT_BUFFER_DELAY_NAME + ".cf26.mono.feedback";
  public final static String EDIT_BUFFER_DELAY_CF26_STEREO_L_DELAY_NAME     = EDIT_BUFFER_DELAY_NAME + ".cf26.stereo.l.delay";
  public final static String EDIT_BUFFER_DELAY_CF26_STEREO_L_FEEDBACK_NAME  = EDIT_BUFFER_DELAY_NAME + ".cf26.stereo.l.feedback";
  public final static String EDIT_BUFFER_DELAY_CF26_STEREO_R_DELAY_NAME     = EDIT_BUFFER_DELAY_NAME + ".cf26.stereo.r.delay";
  public final static String EDIT_BUFFER_DELAY_CF26_STEREO_R_FEEDBACK_NAME  = EDIT_BUFFER_DELAY_NAME + ".cf26.stereo.r.feedback";
  public final static String EDIT_BUFFER_DELAY_CF26_PING_PONG_DELAY_NAME    = EDIT_BUFFER_DELAY_NAME + ".cf26.ping_pong.delay";
  public final static String EDIT_BUFFER_DELAY_CF26_PING_PONG_FEEDBACK_NAME = EDIT_BUFFER_DELAY_NAME + ".cf26.ping_pong.feedback";
  
  public final static String EDIT_BUFFER_DELAY_CF3_MONO_DELAY_NAME         = EDIT_BUFFER_DELAY_NAME + ".cf3.mono.delay";
  public final static String EDIT_BUFFER_DELAY_CF3_MONO_FEEDBACK_NAME      = EDIT_BUFFER_DELAY_NAME + ".cf3.mono.feedback";
  public final static String EDIT_BUFFER_DELAY_CF3_STEREO_L_DELAY_NAME     = EDIT_BUFFER_DELAY_NAME + ".cf3.stereo.l.delay";
  public final static String EDIT_BUFFER_DELAY_CF3_STEREO_L_FEEDBACK_NAME  = EDIT_BUFFER_DELAY_NAME + ".cf3.stereo.l.feedback";
  public final static String EDIT_BUFFER_DELAY_CF3_STEREO_R_DELAY_NAME     = EDIT_BUFFER_DELAY_NAME + ".cf3.stereo.r.delay";
  public final static String EDIT_BUFFER_DELAY_CF3_STEREO_R_FEEDBACK_NAME  = EDIT_BUFFER_DELAY_NAME + ".cf3.stereo.r.feedback";
  public final static String EDIT_BUFFER_DELAY_CF3_PING_PONG_DELAY_NAME    = EDIT_BUFFER_DELAY_NAME + ".cf3.ping_pong.delay";
  public final static String EDIT_BUFFER_DELAY_CF3_PING_PONG_FEEDBACK_NAME = EDIT_BUFFER_DELAY_NAME + ".cf3.ping_pong.feedback";
  
  private final static String EB_DELAY_NAME = EDIT_BUFFER_DELAY_NAME;
  
  public final static String EDIT_BUFFER_DELAY_CF4_MONO_DELAY_NAME               = EB_DELAY_NAME + ".cf4.mono.delay";
  public final static String EDIT_BUFFER_DELAY_CF4_MONO_FEEDBACK_NAME            = EB_DELAY_NAME + ".cf4.mono.feedback";
  public final static String EDIT_BUFFER_DELAY_CF4_STEREO_L_DELAY_NAME           = EB_DELAY_NAME + ".cf4.stereo.l.delay";
  public final static String EDIT_BUFFER_DELAY_CF4_STEREO_L_FEEDBACK_NAME        = EB_DELAY_NAME + ".cf4.stereo.l.feedback";
  public final static String EDIT_BUFFER_DELAY_CF4_STEREO_R_DELAY_NAME           = EB_DELAY_NAME + ".cf4.stereo.r.delay";
  public final static String EDIT_BUFFER_DELAY_CF4_STEREO_R_FEEDBACK_NAME        = EB_DELAY_NAME + ".cf4.stereo.r.feedback";
  public final static String EDIT_BUFFER_DELAY_CF4_PING_PONG_DELAY_NAME          = EB_DELAY_NAME + ".cf4.ping_pong.delay";
  public final static String EDIT_BUFFER_DELAY_CF4_PING_PONG_FEEDBACK_NAME       = EB_DELAY_NAME + ".cf4.ping_pong.feedback";
  public final static String EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_NUMBER_NAME      = EB_DELAY_NAME + ".cf4.multitap.tap.number";
  public final static String EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_1_DELAY_NAME     = EB_DELAY_NAME + ".cf4.multitap.tap.1.delay";
  public final static String EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_1_VOLUME_NAME    = EB_DELAY_NAME + ".cf4.multitap.tap.1.volume";
  public final static String EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_1_PANNING_NAME   = EB_DELAY_NAME + ".cf4.multitap.tap.1.panning";
  public final static String EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_1_FEEDBACK_NAME  = EB_DELAY_NAME + ".cf4.multitap.tap.1.feedback";
  public final static String EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_2_DELAY_NAME     = EB_DELAY_NAME + ".cf4.multitap.tap.2.delay";
  public final static String EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_2_VOLUME_NAME    = EB_DELAY_NAME + ".cf4.multitap.tap.2.volume";
  public final static String EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_2_PANNING_NAME   = EB_DELAY_NAME + ".cf4.multitap.tap.2.panning";
  public final static String EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_2_FEEDBACK_NAME  = EB_DELAY_NAME + ".cf4.multitap.tap.2.feedback";
  public final static String EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_3_DELAY_NAME     = EB_DELAY_NAME + ".cf4.multitap.tap.3.delay";
  public final static String EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_3_VOLUME_NAME    = EB_DELAY_NAME + ".cf4.multitap.tap.3.volume";
  public final static String EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_3_PANNING_NAME   = EB_DELAY_NAME + ".cf4.multitap.tap.3.panning";
  public final static String EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_3_FEEDBACK_NAME  = EB_DELAY_NAME + ".cf4.multitap.tap.3.feedback";
  public final static String EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_4_DELAY_NAME     = EB_DELAY_NAME + ".cf4.multitap.tap.4.delay";
  public final static String EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_4_VOLUME_NAME    = EB_DELAY_NAME + ".cf4.multitap.tap.4.volume";
  public final static String EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_4_PANNING_NAME   = EB_DELAY_NAME + ".cf4.multitap.tap.4.panning";
  public final static String EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_4_FEEDBACK_NAME  = EB_DELAY_NAME + ".cf4.multitap.tap.4.feedback";
  public final static String EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_5_DELAY_NAME     = EB_DELAY_NAME + ".cf4.multitap.tap.5.delay";
  public final static String EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_5_VOLUME_NAME    = EB_DELAY_NAME + ".cf4.multitap.tap.5.volume";
  public final static String EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_5_PANNING_NAME   = EB_DELAY_NAME + ".cf4.multitap.tap.5.panning";
  public final static String EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_5_FEEDBACK_NAME  = EB_DELAY_NAME + ".cf4.multitap.tap.5.feedback";
  public final static String EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_6_DELAY_NAME     = EB_DELAY_NAME + ".cf4.multitap.tap.6.delay";
  public final static String EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_6_VOLUME_NAME    = EB_DELAY_NAME + ".cf4.multitap.tap.6.volume";
  public final static String EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_6_PANNING_NAME   = EB_DELAY_NAME + ".cf4.multitap.tap.6.panning";
  public final static String EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_6_FEEDBACK_NAME  = EB_DELAY_NAME + ".cf4.multitap.tap.6.feedback";
  public final static String EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_7_DELAY_NAME     = EB_DELAY_NAME + ".cf4.multitap.tap.7.delay";
  public final static String EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_7_VOLUME_NAME    = EB_DELAY_NAME + ".cf4.multitap.tap.7.volume";
  public final static String EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_7_PANNING_NAME   = EB_DELAY_NAME + ".cf4.multitap.tap.7.panning";
  public final static String EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_7_FEEDBACK_NAME  = EB_DELAY_NAME + ".cf4.multitap.tap.7.feedback";
  public final static String EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_8_DELAY_NAME     = EB_DELAY_NAME + ".cf4.multitap.tap.8.delay";
  public final static String EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_8_VOLUME_NAME    = EB_DELAY_NAME + ".cf4.multitap.tap.8.volume";
  public final static String EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_8_PANNING_NAME   = EB_DELAY_NAME + ".cf4.multitap.tap.8.panning";
  public final static String EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_8_FEEDBACK_NAME  = EB_DELAY_NAME + ".cf4.multitap.tap.8.feedback";
  public final static String EDIT_BUFFER_DELAY_CF4_MULTITAP_MASTER_FEEDBACK_NAME = EB_DELAY_NAME + ".cf4.multitap.masterFeedback";
  
  public final static String EDIT_BUFFER_DELAY_CF7_MONO_DELAY_NAME         = EDIT_BUFFER_DELAY_NAME + ".cf7.mono.delay";
  public final static String EDIT_BUFFER_DELAY_CF7_MONO_FEEDBACK_NAME      = EDIT_BUFFER_DELAY_NAME + ".cf7.mono.feedback";
  public final static String EDIT_BUFFER_DELAY_CF7_STEREO_L_DELAY_NAME     = EDIT_BUFFER_DELAY_NAME + ".cf7.stereo.l.delay";
  public final static String EDIT_BUFFER_DELAY_CF7_STEREO_L_FEEDBACK_NAME  = EDIT_BUFFER_DELAY_NAME + ".cf7.stereo.l.feedback";
  public final static String EDIT_BUFFER_DELAY_CF7_STEREO_R_DELAY_NAME     = EDIT_BUFFER_DELAY_NAME + ".cf7.stereo.r.delay";
  public final static String EDIT_BUFFER_DELAY_CF7_STEREO_R_FEEDBACK_NAME  = EDIT_BUFFER_DELAY_NAME + ".cf7.stereo.r.feedback";
  public final static String EDIT_BUFFER_DELAY_CF7_PING_PONG_DELAY_NAME    = EDIT_BUFFER_DELAY_NAME + ".cf7.ping_pong.delay";
  public final static String EDIT_BUFFER_DELAY_CF7_PING_PONG_FEEDBACK_NAME = EDIT_BUFFER_DELAY_NAME + ".cf7.ping_pong.feedback";
 
  public final static String EDIT_BUFFER_DELAY_CF8_SAMPLE_PLAYBACK_NAME = EDIT_BUFFER_DELAY_NAME + ".cf8.samplePlayback";
  public final static String EDIT_BUFFER_DELAY_CF8_SAMPLE_START_NAME    = EDIT_BUFFER_DELAY_NAME + ".cf8.sampleStart";
  public final static String EDIT_BUFFER_DELAY_CF8_SAMPLE_LENGTH_NAME   = EDIT_BUFFER_DELAY_NAME + ".cf8.sampleLength";
  public final static String EDIT_BUFFER_DELAY_CF8_AUDIO_TRIGGER_NAME   = EDIT_BUFFER_DELAY_NAME + ".cf8.audioTrigger";
  public final static String EDIT_BUFFER_DELAY_CF8_MIDI_TRIGGER_NAME    = EDIT_BUFFER_DELAY_NAME + ".cf8.midiTrigger";
  public final static String EDIT_BUFFER_DELAY_CF8_MIDI_LOW_LIMIT_NAME  = EDIT_BUFFER_DELAY_NAME + ".cf8.midiLowLimit";
  public final static String EDIT_BUFFER_DELAY_CF8_MIDI_BASE_NOTE_NAME  = EDIT_BUFFER_DELAY_NAME + ".cf8.midiBaseNote";
  public final static String EDIT_BUFFER_DELAY_CF8_MIDI_HIGH_LIMIT_NAME = EDIT_BUFFER_DELAY_NAME + ".cf8.midiHighLimit";
    
  public enum DelayMode
  {
    MONO,
    STEREO,
    PING_PONG;
  }
  
  public enum DelayModeExtended
  {
    MONO,
    STEREO,
    PING_PONG,
    MULTI_TAP;
  }
  
  public enum DelayInput1
  {
    PREAMP,
    EQ;
  }
  
  public enum DelayInput
  {
    PREAMP,
    EQ;
  }
  
  public enum DelayTap
  {
    TAP_1,
    TAP_2,
    TAP_3,
    TAP_4,
    TAP_5,
    TAP_6,
    TAP_7,
    TAP_8;
  }
  
  public enum SamplePlayback
  {
    LOOPING,
    ONE_SHOT,
    AUDIO_TRIGGER;
  }
  
  public enum MidiTrigger
  {
    OFF,
    GATED,
    ONE_SHOT;
  }
  
  private void registerParameters_EditBuffer_Delay ()
  {

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_MODE_NAME,
      DelayMode.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      0, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x27, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (
        EnumSet.complementOf (
          EnumSet.of (
            Patch_QGVT.Configuration.C4_5EQ_PCH_DL,
            Patch_QGVT.Configuration.C5_3EQ_REV,
            Patch_QGVT.Configuration.C8_SAMPLING)))));
        
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_MODE_EXTENDED_NAME,
      DelayModeExtended.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      0, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x27, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_INPUT1_NAME,
      DelayInput1.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      1, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x28, /* offset */
      1, /* size */
      7, /* bitOffset */
      1, /* bitSize */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (
        EnumSet.of (
          Patch_QGVT.Configuration.C1_EQ_PCH_DL_REV,
          Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_INPUT_NAME,
      DelayInput.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      1, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x28, /* offset */
      1, /* size */
      7, /* bitOffset */
      1, /* bitSize */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C3_GEQ_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF14_IN_MIX_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x29, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (
        EnumSet.of (
          Patch_QGVT.Configuration.C1_EQ_PCH_DL_REV,
          Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF267_IN_MIX_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      1, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x29, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (
        EnumSet.of (
          Patch_QGVT.Configuration.C2_LES_DL_REV,
          Patch_QGVT.Configuration.C6_RING_DL_REV,
          Patch_QGVT.Configuration.C7_RESO_DL_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF1_MONO_DELAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2A, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C1_EQ_PCH_DL_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF1_MONO_FEEDBACK_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2C, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C1_EQ_PCH_DL_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF1_STEREO_L_DELAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2A, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C1_EQ_PCH_DL_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF1_STEREO_L_FEEDBACK_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2C, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C1_EQ_PCH_DL_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF1_STEREO_R_DELAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      5, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2D, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C1_EQ_PCH_DL_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF1_STEREO_R_FEEDBACK_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      6, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2F, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C1_EQ_PCH_DL_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF1_PING_PONG_DELAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2A, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C1_EQ_PCH_DL_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF1_PING_PONG_FEEDBACK_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2C, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C1_EQ_PCH_DL_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF26_MONO_DELAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2A, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C2_LES_DL_REV, Patch_QGVT.Configuration.C6_RING_DL_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF26_MONO_FEEDBACK_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2C, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C2_LES_DL_REV, Patch_QGVT.Configuration.C6_RING_DL_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF26_STEREO_L_DELAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2A, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C2_LES_DL_REV, Patch_QGVT.Configuration.C6_RING_DL_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF26_STEREO_L_FEEDBACK_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2C, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C2_LES_DL_REV, Patch_QGVT.Configuration.C6_RING_DL_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF26_STEREO_R_DELAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2D, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C2_LES_DL_REV, Patch_QGVT.Configuration.C6_RING_DL_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF26_STEREO_R_FEEDBACK_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      5, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2F, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C2_LES_DL_REV, Patch_QGVT.Configuration.C6_RING_DL_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF26_PING_PONG_DELAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2A, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C2_LES_DL_REV, Patch_QGVT.Configuration.C6_RING_DL_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF26_PING_PONG_FEEDBACK_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2C, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C2_LES_DL_REV, Patch_QGVT.Configuration.C6_RING_DL_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF3_MONO_DELAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2A, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C3_GEQ_DL))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF3_MONO_FEEDBACK_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2C, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C3_GEQ_DL))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF3_STEREO_L_DELAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2A, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C3_GEQ_DL))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF3_STEREO_L_FEEDBACK_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2C, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C3_GEQ_DL))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF3_STEREO_R_DELAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2D, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C3_GEQ_DL))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF3_STEREO_R_FEEDBACK_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      5, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2F, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C3_GEQ_DL))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF3_PING_PONG_DELAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2A, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C3_GEQ_DL))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF3_PING_PONG_FEEDBACK_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2C, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C3_GEQ_DL))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MONO_DELAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2A, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MONO_FEEDBACK_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2C, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_STEREO_L_DELAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2A, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_STEREO_L_FEEDBACK_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2C, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_STEREO_R_DELAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      5, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2D, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_STEREO_R_FEEDBACK_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      6, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2F, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_PING_PONG_DELAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2A, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_PING_PONG_FEEDBACK_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2C, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_NUMBER_NAME,
      DelayTap.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x69, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_1_DELAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      new int[]{0x0D, 0x18}, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_1_VOLUME_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      5, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x19, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_1_PANNING_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      6, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x1F, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_1_FEEDBACK_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      7, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x22, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_2_DELAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x23, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_2_VOLUME_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      5, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x26, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_2_PANNING_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      6, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2A, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_2_FEEDBACK_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      7, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2B, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_3_DELAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2C, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_3_VOLUME_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      5, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2E, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_3_PANNING_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      6, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2F, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_3_FEEDBACK_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      7, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x30, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_4_DELAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x31, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_4_VOLUME_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      5, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x33, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_4_PANNING_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      6, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x34, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_4_FEEDBACK_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      7, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x35, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_5_DELAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x36, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_5_VOLUME_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      5, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x38, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_5_PANNING_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      6, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x39, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_5_FEEDBACK_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      7, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x3A, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_6_DELAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x3B, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_6_VOLUME_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      5, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x3D, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_6_PANNING_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      6, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x3E, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_6_FEEDBACK_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      7, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x3F, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_7_DELAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x40, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_7_VOLUME_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      5, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x42, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_7_PANNING_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      6, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x43, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_7_FEEDBACK_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      7, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x4A, /* offset */ // UNDOCUMENTED -> FOUND THROUGH MIDI DEBUGGING 20190504.
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_8_DELAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x4B, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_8_VOLUME_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      5, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x4D, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_8_PANNING_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      6, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x4E, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MULTITAP_TAP_8_FEEDBACK_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      7, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x4F, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF4_MULTITAP_MASTER_FEEDBACK_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      8, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x68, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF7_MONO_DELAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2A, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C7_RESO_DL_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF7_MONO_FEEDBACK_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2C, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C7_RESO_DL_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF7_STEREO_L_DELAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2A, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C7_RESO_DL_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF7_STEREO_L_FEEDBACK_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2C, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C7_RESO_DL_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF7_STEREO_R_DELAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2D, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C7_RESO_DL_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF7_STEREO_R_FEEDBACK_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      5, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2F, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C7_RESO_DL_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF7_PING_PONG_DELAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_2BYTES,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2A, /* offset */
      2, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C7_RESO_DL_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF7_PING_PONG_FEEDBACK_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x2C, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C7_RESO_DL_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF8_SAMPLE_PLAYBACK_NAME,
      SamplePlayback.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      0, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x1F, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C8_SAMPLING))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF8_SAMPLE_START_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      1, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x18, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C8_SAMPLING))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF8_SAMPLE_LENGTH_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x19, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C8_SAMPLING))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF8_AUDIO_TRIGGER_NAME,
      Boolean.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.BOOLEAN_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x23, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C8_SAMPLING))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF8_MIDI_TRIGGER_NAME,
      MidiTrigger.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x24, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C8_SAMPLING))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF8_MIDI_LOW_LIMIT_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      5, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x30, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C8_SAMPLING))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF8_MIDI_BASE_NOTE_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      6, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x26, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C8_SAMPLING))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_DELAY_CF8_MIDI_HIGH_LIMIT_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_DELAY,
      7, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x31, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C8_SAMPLING))));
    
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PARAMETER REGISTRATION - EDIT BUFFER - REVERB
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public final static String EDIT_BUFFER_REVERB_NAME = EDIT_BUFFER_NAME + ".reverb";
  private final static String EB_REVERB_NAME = EDIT_BUFFER_REVERB_NAME;
  
  public final static String EDIT_BUFFER_REVERB_MODE_NAME = EB_REVERB_NAME + ".mode";
  
  public final static String EDIT_BUFFER_REVERB_CF1_INPUT1_NAME = EB_REVERB_NAME + ".cf1.input1";
  public final static String EDIT_BUFFER_REVERB_CF2_INPUT1_NAME = EB_REVERB_NAME + ".cf2.input1";
  public final static String EDIT_BUFFER_REVERB_CF5_INPUT_NAME  = EB_REVERB_NAME + ".cf5.input";
  public final static String EDIT_BUFFER_REVERB_CF6_INPUT1_NAME = EB_REVERB_NAME + ".cf6.input1";
  public final static String EDIT_BUFFER_REVERB_CF7_INPUT1_NAME = EB_REVERB_NAME + ".cf7.input1";
  
  public final static String EDIT_BUFFER_REVERB_CF1_INPUT2_NAME = EB_REVERB_NAME + ".cf1.input2";
  public final static String EDIT_BUFFER_REVERB_CF2_INPUT2_NAME = EB_REVERB_NAME + ".cf2.input2";
  public final static String EDIT_BUFFER_REVERB_CF6_INPUT2_NAME = EB_REVERB_NAME + ".cf6.input2";
  public final static String EDIT_BUFFER_REVERB_CF7_INPUT2_NAME = EB_REVERB_NAME + ".cf7.input2";
    
  public final static String EDIT_BUFFER_REVERB_CF1267_IN_MIX_NAME                = EB_REVERB_NAME + ".cf1267.inMix";
  public final static String EDIT_BUFFER_REVERB_CF1267_PRE_DELAY_NAME             = EB_REVERB_NAME + ".cf1267.preDelay";
  public final static String EDIT_BUFFER_REVERB_CF1267_PRE_MIX_NAME               = EB_REVERB_NAME + ".cf1267.preMix";
  public final static String EDIT_BUFFER_REVERB_CF1267_FORWARD_DECAY_NAME         = EB_REVERB_NAME + ".cf1267.forward.decay";
  public final static String EDIT_BUFFER_REVERB_CF1267_REVERSE_TIME_NAME          = EB_REVERB_NAME + ".cf1267.reverse.time";
  public final static String EDIT_BUFFER_REVERB_CF1267_DIFFUSION_NAME             = EB_REVERB_NAME + ".cf1267.diffusion";
  public final static String EDIT_BUFFER_REVERB_CF1267_NON_HALL_DENSITY_NAME      = EB_REVERB_NAME + ".cf1267.nonHall.density";
  public final static String EDIT_BUFFER_REVERB_CF1267_NON_HALL_LOW_DECAY_NAME    = EB_REVERB_NAME + ".cf1267.nonHall.lowDecay";
  public final static String EDIT_BUFFER_REVERB_CF1267_NON_HALL_HIGH_DECAY_NAME   = EB_REVERB_NAME + ".cf1267.nonHall.highDecay";
  public final static String EDIT_BUFFER_REVERB_CF1267_HALL_LOW_DECAY_NAME        = EB_REVERB_NAME + ".cf1267.hall.lowDecay";
  public final static String EDIT_BUFFER_REVERB_CF1267_HALL_HIGH_DECAY_NAME       = EB_REVERB_NAME + ".cf1267.hall.highDecay";
  public final static String EDIT_BUFFER_REVERB_CF1267_PLROCH_GATE_NAME           = EB_REVERB_NAME + ".cf1267.plRoCh.gate";
  public final static String EDIT_BUFFER_REVERB_CF1267_PLROCH_GATE_HOLD_TIME_NAME = EB_REVERB_NAME + ".cf1267.plRoCh.gateHoldTime";
  public final static String EDIT_BUFFER_REVERB_CF1267_PLROCH_GATE_REL_TIME_NAME  = EB_REVERB_NAME + ".cf1267.plRoCh.gateRelTime";
  public final static String EDIT_BUFFER_REVERB_CF1267_PLROCH_GATE_LEVEL_NAME     = EB_REVERB_NAME + ".cf1267.plRoCh.gateLevel";
  public final static String EDIT_BUFFER_REVERB_CF1267_HALL_GATE_NAME             = EB_REVERB_NAME + ".cf1267.hall.gate";
  public final static String EDIT_BUFFER_REVERB_CF1267_HALL_GATE_HOLD_TIME_NAME   = EB_REVERB_NAME + ".cf1267.hall.gateHoldTime";
  public final static String EDIT_BUFFER_REVERB_CF1267_HALL_GATE_REL_TIME_NAME    = EB_REVERB_NAME + ".cf1267.hall.gateRelTime";
  public final static String EDIT_BUFFER_REVERB_CF1267_HALL_GATE_LEVEL_NAME       = EB_REVERB_NAME + ".cf1267.hall.gateLevel";
  
  public final static String EDIT_BUFFER_REVERB_CF5_PRE_DELAY_NAME             = EB_REVERB_NAME + ".cf5.preDelay";
  public final static String EDIT_BUFFER_REVERB_CF5_PRE_MIX_NAME               = EB_REVERB_NAME + ".cf5.preMix";
  public final static String EDIT_BUFFER_REVERB_CF5_FORWARD_DECAY_NAME         = EB_REVERB_NAME + ".cf5.forward.decay";
  public final static String EDIT_BUFFER_REVERB_CF5_REVERSE_TIME_NAME          = EB_REVERB_NAME + ".cf5.reverse.time";
  public final static String EDIT_BUFFER_REVERB_CF5_DIFFUSION_NAME             = EB_REVERB_NAME + ".cf5.diffusion";
  public final static String EDIT_BUFFER_REVERB_CF5_NON_HALL_DENSITY_NAME      = EB_REVERB_NAME + ".cf5.nonHall.density";
  public final static String EDIT_BUFFER_REVERB_CF5_NON_HALL_LOW_DECAY_NAME    = EB_REVERB_NAME + ".cf5.nonHall.lowDecay";
  public final static String EDIT_BUFFER_REVERB_CF5_NON_HALL_HIGH_DECAY_NAME   = EB_REVERB_NAME + ".cf5.nonHall.highDecay";
  public final static String EDIT_BUFFER_REVERB_CF5_HALL_LOW_DECAY_NAME        = EB_REVERB_NAME + ".cf5.hall.lowDecay";
  public final static String EDIT_BUFFER_REVERB_CF5_HALL_HIGH_DECAY_NAME       = EB_REVERB_NAME + ".cf5.hall.highDecay";
  public final static String EDIT_BUFFER_REVERB_CF5_PLROCH_GATE_NAME           = EB_REVERB_NAME + ".cf5.plRoCh.gate";
  public final static String EDIT_BUFFER_REVERB_CF5_PLROCH_GATE_HOLD_TIME_NAME = EB_REVERB_NAME + ".cf5.plRoCh.gateHoldTime";
  public final static String EDIT_BUFFER_REVERB_CF5_PLROCH_GATE_REL_TIME_NAME  = EB_REVERB_NAME + ".cf5.plRoCh.gateRelTime";
  public final static String EDIT_BUFFER_REVERB_CF5_PLROCH_GATE_LEVEL_NAME     = EB_REVERB_NAME + ".cf5.plRoCh.gateLevel";
  public final static String EDIT_BUFFER_REVERB_CF5_HALL_GATE_NAME             = EB_REVERB_NAME + ".cf5.hall.gate";
  public final static String EDIT_BUFFER_REVERB_CF5_HALL_GATE_HOLD_TIME_NAME   = EB_REVERB_NAME + ".cf5.hall.gateHoldTime";
  public final static String EDIT_BUFFER_REVERB_CF5_HALL_GATE_REL_TIME_NAME    = EB_REVERB_NAME + ".cf5.hall.gateRelTime";
  public final static String EDIT_BUFFER_REVERB_CF5_HALL_GATE_LEVEL_NAME       = EB_REVERB_NAME + ".cf5.hall.gateLevel";
  
  /** The REVERB MODE in Configurations 1, 2, 5, 6, 7.
   * 
   */
  public enum ReverbMode
  {
    PLATE,
    ROOM,
    CHAMBER,
    HALL,
    REVERSE;
  }
  
  /** The REVERB INPUT1 in Configuration 1.
   * 
   * <p>
   * Verified against QVGT 20190503.
   * 
   * @see #EDIT_BUFFER_REVERB_CF1_INPUT1_NAME
   * 
   */
  public enum ReverbInput1Config1
  {
    PREAMP_OUT,
    EQ_OUT,
    PITCH_OUT,
    DELAY_INPUT_MIX;
  }
  
  /** The REVERB INPUT1 in Configuration 2.
   * 
   * <p>
   * Verified against QVGT 20190503.
   * 
   * @see #EDIT_BUFFER_REVERB_CF2_INPUT1_NAME
   * 
   */
  public enum ReverbInput1Config2
  {
    /** Non-existent setting in Configuration 2.
     * 
     * <p>
     * The QVGT resets the value to {@link ReverbInput1Config2#PREAMP_OUT}.
     * 
     */
    NX_PITCH_OUT,
    PREAMP_OUT,
    LESLIE_OUT,
    DELAY_INPUT_MIX;
  }
  
  public enum ReverbInputConfig5
  {
    PREAMP_OUT,
    EQ_OUT;
  }
  
  public enum ReverbInput1Config6
  {
    PREAMP_OUT,
    RING_MOD_OUT,
    DELAY_INPUT_MIX;
  }
  
  public enum ReverbInput1Config7
  {
    PREAMP_OUT,
    RESO_OUT,
    DELAY_INPUT_MIX;
  }
  
  public enum ReverbInput2Config1
  {
    DELAY_OUT,
    PITCH_OUT;
  }
  
  public enum ReverbInput2Config2
  {
    DELAY_OUT,
    LESLIE_OUT;
  }
  
  public enum ReverbInput2Config6
  {
    DELAY_OUT,
    RING_MOD_OUT;
  }
  
  public enum ReverbInput2Config7
  {
    DELAY_OUT,
    RESO_OUT;
  }
  
  private void registerParameters_EditBuffer_Reverb ()
  {

    //////////////////////////////////
    // Mode - Configs 1, 2, 5, 6, 7 //
    //////////////////////////////////
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_MODE_NAME,
      ReverbMode.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      0, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x32, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null /* customValueConverter */,
      new ONLY_CONFIGS (
        EnumSet.complementOf (
          EnumSet.of (
            Patch_QGVT.Configuration.C3_GEQ_DL,
            Patch_QGVT.Configuration.C4_5EQ_PCH_DL,
            Patch_QGVT.Configuration.C8_SAMPLING)))));

    ////////////////////////////////////
    // Input1 - Configs 1, 2, 5, 6, 7 //
    ////////////////////////////////////
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF1_INPUT1_NAME,
      ReverbInput1Config1.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      1, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x34, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null /* customValueConverter */,
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C1_EQ_PCH_DL_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF2_INPUT1_NAME,
      ReverbInput1Config2.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      1, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x34, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null /* customValueConverter */,
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C2_LES_DL_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF5_INPUT_NAME,
      ReverbInputConfig5.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      1, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x34, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null /* customValueConverter */,
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C5_3EQ_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF6_INPUT1_NAME,
      ReverbInput1Config6.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      1, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x34, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null /* customValueConverter */,
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C6_RING_DL_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF7_INPUT1_NAME,
      ReverbInput1Config7.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      1, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x34, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null /* customValueConverter */,
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C7_RESO_DL_REV))));

    /////////////////////////////////
    // Input2 - Configs 1, 2, 6, 7 //
    /////////////////////////////////
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF1_INPUT2_NAME,
      ReverbInput2Config1.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x35, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null /* customValueConverter */,
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C1_EQ_PCH_DL_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF2_INPUT2_NAME,
      ReverbInput2Config2.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x35, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null /* customValueConverter */,
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C2_LES_DL_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF6_INPUT2_NAME,
      ReverbInput2Config6.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x35, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null /* customValueConverter */,
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C6_RING_DL_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF7_INPUT2_NAME,
      ReverbInput2Config7.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x35, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null /* customValueConverter */,
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C7_RESO_DL_REV))));

    //////////////////////////////////////////////////////
    // Remaining REVERB parameters - Configs 1, 2, 6, 7 //
    //////////////////////////////////////////////////////
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF1267_IN_MIX_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x36, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (
        Patch_QGVT.Configuration.C1_EQ_PCH_DL_REV,
        Patch_QGVT.Configuration.C2_LES_DL_REV,
        Patch_QGVT.Configuration.C6_RING_DL_REV,
        Patch_QGVT.Configuration.C7_RESO_DL_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF1267_PRE_DELAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x37, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (
        Patch_QGVT.Configuration.C1_EQ_PCH_DL_REV,
        Patch_QGVT.Configuration.C2_LES_DL_REV,
        Patch_QGVT.Configuration.C6_RING_DL_REV,
        Patch_QGVT.Configuration.C7_RESO_DL_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF1267_PRE_MIX_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      5, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x38, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (
        Patch_QGVT.Configuration.C1_EQ_PCH_DL_REV,
        Patch_QGVT.Configuration.C2_LES_DL_REV,
        Patch_QGVT.Configuration.C6_RING_DL_REV,
        Patch_QGVT.Configuration.C7_RESO_DL_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF1267_FORWARD_DECAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      6, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x39, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS1267_WITH_REVERB_MODES (EnumSet.complementOf (EnumSet.of (ReverbMode.REVERSE)))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF1267_REVERSE_TIME_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      6, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x39, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS1267_WITH_REVERB_MODES (EnumSet.of (ReverbMode.REVERSE))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF1267_DIFFUSION_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      7, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x3A, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (
        Patch_QGVT.Configuration.C1_EQ_PCH_DL_REV,
        Patch_QGVT.Configuration.C2_LES_DL_REV,
        Patch_QGVT.Configuration.C6_RING_DL_REV,
        Patch_QGVT.Configuration.C7_RESO_DL_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF1267_NON_HALL_DENSITY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      8, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x3D, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS1267_WITH_REVERB_MODES (EnumSet.complementOf (EnumSet.of (ReverbMode.HALL)))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF1267_NON_HALL_LOW_DECAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      9, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x3B, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS1267_WITH_REVERB_MODES (EnumSet.complementOf (EnumSet.of (ReverbMode.HALL)))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF1267_NON_HALL_HIGH_DECAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      10, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x3C, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS1267_WITH_REVERB_MODES (EnumSet.complementOf (EnumSet.of (ReverbMode.HALL)))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF1267_HALL_LOW_DECAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      8, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x3B, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS1267_WITH_REVERB_MODES (EnumSet.of (ReverbMode.HALL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF1267_HALL_HIGH_DECAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      9, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x3C, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS1267_WITH_REVERB_MODES (EnumSet.of (ReverbMode.HALL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF1267_PLROCH_GATE_NAME,
      Boolean.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.BOOLEAN_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      11, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x3E, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS1267_WITH_REVERB_MODES (EnumSet.of (
        ReverbMode.PLATE,
        ReverbMode.ROOM,
        ReverbMode.CHAMBER))));
        
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF1267_PLROCH_GATE_HOLD_TIME_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      12, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x3F, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS1267_WITH_REVERB_MODES (EnumSet.of (
        ReverbMode.PLATE,
        ReverbMode.ROOM,
        ReverbMode.CHAMBER))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF1267_PLROCH_GATE_REL_TIME_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      13, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x40, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS1267_WITH_REVERB_MODES (EnumSet.of (
        ReverbMode.PLATE,
        ReverbMode.ROOM,
        ReverbMode.CHAMBER))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF1267_PLROCH_GATE_LEVEL_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      14, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x41, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS1267_WITH_REVERB_MODES (EnumSet.of (
        ReverbMode.PLATE,
        ReverbMode.ROOM,
        ReverbMode.CHAMBER))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF1267_HALL_GATE_NAME,
      Boolean.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.BOOLEAN_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      10, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x3E, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS1267_WITH_REVERB_MODES (EnumSet.of (ReverbMode.HALL))));
        
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF1267_HALL_GATE_HOLD_TIME_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      11, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x3F, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS1267_WITH_REVERB_MODES (EnumSet.of (ReverbMode.HALL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF1267_HALL_GATE_REL_TIME_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      12, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x40, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS1267_WITH_REVERB_MODES (EnumSet.of (ReverbMode.HALL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF1267_HALL_GATE_LEVEL_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      13, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x41, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS1267_WITH_REVERB_MODES (EnumSet.of (ReverbMode.HALL))));
    
    ////////////////////////////////////////////
    // Remaining REVERB parameters - Config 5 //
    ////////////////////////////////////////////
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF5_PRE_DELAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x37, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C5_3EQ_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF5_PRE_MIX_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x38, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C5_3EQ_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF5_FORWARD_DECAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x39, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG5_WITH_REVERB_MODES (EnumSet.complementOf (EnumSet.of (ReverbMode.REVERSE)))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF5_REVERSE_TIME_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x39, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG5_WITH_REVERB_MODES (EnumSet.of (ReverbMode.REVERSE))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF5_DIFFUSION_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      5, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x3A, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C5_3EQ_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF5_NON_HALL_DENSITY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      6, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x3D, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG5_WITH_REVERB_MODES (EnumSet.complementOf (EnumSet.of (ReverbMode.HALL)))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF5_NON_HALL_LOW_DECAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      7, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x3B, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG5_WITH_REVERB_MODES (EnumSet.complementOf (EnumSet.of (ReverbMode.HALL)))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF5_NON_HALL_HIGH_DECAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      8, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x3C, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG5_WITH_REVERB_MODES (EnumSet.complementOf (EnumSet.of (ReverbMode.HALL)))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF5_HALL_LOW_DECAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      6, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x3B, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG5_WITH_REVERB_MODES (EnumSet.of (ReverbMode.HALL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF5_HALL_HIGH_DECAY_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      7, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x3C, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG5_WITH_REVERB_MODES (EnumSet.of (ReverbMode.HALL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF5_PLROCH_GATE_NAME,
      Boolean.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.BOOLEAN_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      9, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x3E, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG5_WITH_REVERB_MODES (EnumSet.of (
        ReverbMode.PLATE,
        ReverbMode.ROOM,
        ReverbMode.CHAMBER))));
        
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF5_PLROCH_GATE_HOLD_TIME_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      10, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x3F, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG5_WITH_REVERB_MODES (EnumSet.of (
        ReverbMode.PLATE,
        ReverbMode.ROOM,
        ReverbMode.CHAMBER))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF5_PLROCH_GATE_REL_TIME_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      11, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x40, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG5_WITH_REVERB_MODES (EnumSet.of (
        ReverbMode.PLATE,
        ReverbMode.ROOM,
        ReverbMode.CHAMBER))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF5_PLROCH_GATE_LEVEL_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      12, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x41, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG5_WITH_REVERB_MODES (EnumSet.of (
        ReverbMode.PLATE,
        ReverbMode.ROOM,
        ReverbMode.CHAMBER))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF5_HALL_GATE_NAME,
      Boolean.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.BOOLEAN_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      8, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x3E, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG5_WITH_REVERB_MODES (EnumSet.of (ReverbMode.HALL))));
        
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF5_HALL_GATE_HOLD_TIME_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      9, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x3F, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG5_WITH_REVERB_MODES (EnumSet.of (ReverbMode.HALL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF5_HALL_GATE_REL_TIME_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      10, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x40, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG5_WITH_REVERB_MODES (EnumSet.of (ReverbMode.HALL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_REVERB_CF5_HALL_GATE_LEVEL_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_REVERB,
      11, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x41, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIG5_WITH_REVERB_MODES (EnumSet.of (ReverbMode.HALL))));
    
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PARAMETER REGISTRATION - EDIT BUFFER - MIX
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public final static String EDIT_BUFFER_MIX_NAME = EDIT_BUFFER_NAME + ".mix";
  
  public final static String EDIT_BUFFER_MIX_DIRECT_LEVEL_NAME       = EDIT_BUFFER_MIX_NAME + ".directLevel";
  public final static String EDIT_BUFFER_MIX_MASTER_FX_LEVEL_NAME    = EDIT_BUFFER_MIX_NAME + ".masterFxLevel";
  
  public final static String EDIT_BUFFER_MIX_CF1_PREAMP_SIGNAL_NAME      = EDIT_BUFFER_MIX_NAME + ".cf1.preampSignal";
  public final static String EDIT_BUFFER_MIX_CF1_PREAMP_OR_EQ_LEVEL_NAME = EDIT_BUFFER_MIX_NAME + ".cf1.preampOrEqLevel";
  public final static String EDIT_BUFFER_MIX_CF1_PITCH_LEVEL_NAME        = EDIT_BUFFER_MIX_NAME + ".cf1.pitchLevel";
  public final static String EDIT_BUFFER_MIX_CF1_DELAY_LEVEL_NAME        = EDIT_BUFFER_MIX_NAME + ".cf1.delayLevel";
  public final static String EDIT_BUFFER_MIX_CF1_REVERB_LEVEL_NAME       = EDIT_BUFFER_MIX_NAME + ".cf1.reverbLevel";
  public final static String EDIT_BUFFER_MIX_MODULATION_CF1_TYPE_NAME    = EDIT_BUFFER_MIX_NAME + ".modulation.cf1.type";
  public final static String EDIT_BUFFER_MIX_MODULATION_CF1_DEPTH_NAME   = EDIT_BUFFER_MIX_NAME + ".modulation.cf1.depth";
  public final static String EDIT_BUFFER_MIX_MODULATION_CF1_SPEED_NAME   = EDIT_BUFFER_MIX_NAME + ".modulation.cf1.speed";

  public final static String EDIT_BUFFER_MIX_CF2_LESLIE_LEVEL_NAME = EDIT_BUFFER_MIX_NAME + ".cf2.leslieLevel";
  public final static String EDIT_BUFFER_MIX_CF2_DELAY_LEVEL_NAME  = EDIT_BUFFER_MIX_NAME + ".cf2.delayLevel";
  public final static String EDIT_BUFFER_MIX_CF2_REVERB_LEVEL_NAME = EDIT_BUFFER_MIX_NAME + ".cf2.reverbLevel";
  
  public final static String EDIT_BUFFER_MIX_CF3_EQ_LEVEL_NAME    = EDIT_BUFFER_MIX_NAME + ".cf3.eqLevel";
  public final static String EDIT_BUFFER_MIX_CF3_DELAY_LEVEL_NAME = EDIT_BUFFER_MIX_NAME + ".cf3.delayLevel";
  
  public final static String EDIT_BUFFER_MIX_CF4_PREAMP_SIGNAL_NAME      = EDIT_BUFFER_MIX_NAME + ".cf4.preampSignal";
  public final static String EDIT_BUFFER_MIX_CF4_PREAMP_OR_EQ_LEVEL_NAME = EDIT_BUFFER_MIX_NAME + ".cf4.preampOrEqLevel";
  public final static String EDIT_BUFFER_MIX_CF4_PITCH_LEVEL_NAME        = EDIT_BUFFER_MIX_NAME + ".cf4.pitchLevel";
  public final static String EDIT_BUFFER_MIX_CF4_DELAY_LEVEL_NAME        = EDIT_BUFFER_MIX_NAME + ".cf4.delayLevel";
  public final static String EDIT_BUFFER_MIX_MODULATION_CF4_TYPE_NAME    = EDIT_BUFFER_MIX_NAME + ".modulation.cf4.type";
  public final static String EDIT_BUFFER_MIX_MODULATION_CF4_DEPTH_NAME   = EDIT_BUFFER_MIX_NAME + ".modulation.cf4.depth";
  public final static String EDIT_BUFFER_MIX_MODULATION_CF4_SPEED_NAME   = EDIT_BUFFER_MIX_NAME + ".modulation.cf4.speed";

  public final static String EDIT_BUFFER_MIX_CF5_PREAMP_SIGNAL_NAME      = EDIT_BUFFER_MIX_NAME + ".cf5.preampSignal";
  public final static String EDIT_BUFFER_MIX_CF5_PREAMP_OR_EQ_LEVEL_NAME = EDIT_BUFFER_MIX_NAME + ".cf5.preampOrEqLevel";
  public final static String EDIT_BUFFER_MIX_CF5_REVERB_LEVEL_NAME       = EDIT_BUFFER_MIX_NAME + ".cf5.reverbLevel";
  
  public final static String EDIT_BUFFER_MIX_CF6_PREAMP_LEVEL_NAME  = EDIT_BUFFER_MIX_NAME + ".cf6.preampLevel";
  public final static String EDIT_BUFFER_MIX_CF6_RINGMOD_LEVEL_NAME = EDIT_BUFFER_MIX_NAME + ".cf6.ringModLevel";
  public final static String EDIT_BUFFER_MIX_CF6_DELAY_LEVEL_NAME   = EDIT_BUFFER_MIX_NAME + ".cf6.delayLevel";
  public final static String EDIT_BUFFER_MIX_CF6_REVERB_LEVEL_NAME  = EDIT_BUFFER_MIX_NAME + ".cf6.reverbLevel";
  
  public final static String EDIT_BUFFER_MIX_CF7_PREAMP_LEVEL_NAME    = EDIT_BUFFER_MIX_NAME + ".cf7.preampLevel";
  public final static String EDIT_BUFFER_MIX_CF7_RESONATOR_LEVEL_NAME = EDIT_BUFFER_MIX_NAME + ".cf7.resonatorLevel";
  public final static String EDIT_BUFFER_MIX_CF7_DELAY_LEVEL_NAME     = EDIT_BUFFER_MIX_NAME + ".cf7.delayLevel";
  public final static String EDIT_BUFFER_MIX_CF7_REVERB_LEVEL_NAME    = EDIT_BUFFER_MIX_NAME + ".cf7.reverbLevel";
  
  public final static String EDIT_BUFFER_MIX_CF8_PREAMP_LEVEL_NAME   = EDIT_BUFFER_MIX_NAME + ".cf8.preampLevel";
  public final static String EDIT_BUFFER_MIX_CF8_PLAYBACK_LEVEL_NAME = EDIT_BUFFER_MIX_NAME + ".cf8.playbackLevel";
  
  public enum PreampSignal
  {
    PRE_EQ,
    POST_EQ;
  }
  
  public enum MixModulation
  {
    OFF,
    TREMOLO,
    PANNING;
  }
    
  private void registerParameters_EditBuffer_Mix ()
  {

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_DIRECT_LEVEL_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      0, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x45, /* offset */
      1, /* size */
      0, /* bitOffset */
      7, /* bitSize */
      EDIT_BUFFER_NAME, /* parentKey */
      null /* customValueConverter */));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_MASTER_FX_LEVEL_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      1, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x47, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.complementOf (EnumSet.of (Patch_QGVT.Configuration.C8_SAMPLING)))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_CF1_PREAMP_SIGNAL_NAME,
      PreampSignal.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x45, /* offset */
      1, /* size */
      7, /* bitOffset */
      1, /* bitSize */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C1_EQ_PCH_DL_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_CF1_PREAMP_OR_EQ_LEVEL_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x46, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C1_EQ_PCH_DL_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_CF1_PITCH_LEVEL_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x48, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C1_EQ_PCH_DL_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_CF1_DELAY_LEVEL_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      5, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x49, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C1_EQ_PCH_DL_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_CF1_REVERB_LEVEL_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      6, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x4A, /* offset */ // Shared with some resonator setting...
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C1_EQ_PCH_DL_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_MODULATION_CF1_TYPE_NAME,
      MixModulation.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      7, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x7D, /* offset */
      1, /* size */
      0, /* bitOffset */
      2, /* bitSize */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C1_EQ_PCH_DL_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_MODULATION_CF1_DEPTH_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      8, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x7B, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C1_EQ_PCH_DL_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_MODULATION_CF1_SPEED_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      9, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x7A, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C1_EQ_PCH_DL_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_CF2_LESLIE_LEVEL_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x48, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C2_LES_DL_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_CF2_DELAY_LEVEL_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x49, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C2_LES_DL_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_CF2_REVERB_LEVEL_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x4A, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C2_LES_DL_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_CF3_EQ_LEVEL_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x46, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C3_GEQ_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_CF3_DELAY_LEVEL_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x49, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C3_GEQ_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_CF4_PREAMP_SIGNAL_NAME,
      PreampSignal.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x45, /* offset */
      1, /* size */
      7, /* bitOffset */
      1, /* bitSize */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_CF4_PREAMP_OR_EQ_LEVEL_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x46, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_CF4_PITCH_LEVEL_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x48, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_CF4_DELAY_LEVEL_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      5, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x49, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_MODULATION_CF4_TYPE_NAME,
      MixModulation.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      6, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x7D, /* offset */
      1, /* size */
      0, /* bitOffset */
      2, /* bitSize */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_MODULATION_CF4_DEPTH_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      7, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x7B, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_MODULATION_CF4_SPEED_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      8, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x7A, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C4_5EQ_PCH_DL))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_CF5_PREAMP_SIGNAL_NAME,
      PreampSignal.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.ENUM_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x45, /* offset */
      1, /* size */
      7, /* bitOffset */
      1, /* bitSize */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C5_3EQ_REV))));

    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_CF5_PREAMP_OR_EQ_LEVEL_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x46, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C5_3EQ_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_CF5_REVERB_LEVEL_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x4A, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C5_3EQ_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_CF6_PREAMP_LEVEL_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x46, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C6_RING_DL_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_CF6_RINGMOD_LEVEL_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x48, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C6_RING_DL_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_CF6_DELAY_LEVEL_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x49, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C6_RING_DL_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_CF6_REVERB_LEVEL_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      5, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x4A, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C6_RING_DL_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_CF7_PREAMP_LEVEL_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x46, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C7_RESO_DL_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_CF7_RESONATOR_LEVEL_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      3, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x48, /* offset */ // XXX IS THIS CORRECT?? XXX UNDOCUMENTED PARAMETER ADDRESS IN PROGRAM 
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C7_RESO_DL_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_CF7_DELAY_LEVEL_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      4, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x49, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C7_RESO_DL_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_CF7_REVERB_LEVEL_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      5, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x4A, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C7_RESO_DL_REV))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_CF8_PREAMP_LEVEL_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      1, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x46, /* offset */
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C8_SAMPLING))));
    
    registerParameter (new ParameterDescriptor_QVGT (
      EDIT_BUFFER_MIX_CF8_PLAYBACK_LEVEL_NAME,
      Integer.class,
      ParameterDescriptor_QVGT.ParameterConversion_QVGT.INT_IN_BYTE,
      ParameterDescriptor_QVGT.Function_QVGT.F_MIX,
      2, /* page */
      EDIT_BUFFER_PROGRAM_NUMBER, /* program */
      0x48, /* offset */ // XXX IS THIS CORRECT?? XXX UNDOCUMENTED PARAMETER ADDRESS IN PROGRAM -> 0x47 ALSO CANDIDATE!!
      1, /* size */
      EDIT_BUFFER_NAME, /* parentKey */
      null, /* customValueConverter */
      new ONLY_CONFIGS (EnumSet.of (Patch_QGVT.Configuration.C8_SAMPLING))));
    
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PARAMETER REGISTRATION - EDIT BUFFER - MIX - [MIDI] MODULATION
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public final static String EDIT_BUFFER_MODULATION_NAME = EDIT_BUFFER_NAME + ".modulation";
  
  public final static String EDIT_BUFFER_MODULATION_1_SOURCE_NAME    = EDIT_BUFFER_MODULATION_NAME + ".1.source";
  public final static String EDIT_BUFFER_MODULATION_1_TARGET_NAME    = EDIT_BUFFER_MODULATION_NAME + ".1.target";
  public final static String EDIT_BUFFER_MODULATION_1_AMPLITUDE_NAME = EDIT_BUFFER_MODULATION_NAME + ".1.amplitude";
  
  public final static String EDIT_BUFFER_MODULATION_2_SOURCE_NAME    = EDIT_BUFFER_MODULATION_NAME + ".2.source";
  public final static String EDIT_BUFFER_MODULATION_2_TARGET_NAME    = EDIT_BUFFER_MODULATION_NAME + ".2.target";
  public final static String EDIT_BUFFER_MODULATION_2_AMPLITUDE_NAME = EDIT_BUFFER_MODULATION_NAME + ".2.amplitude";
  
  public final static String EDIT_BUFFER_MODULATION_3_SOURCE_NAME    = EDIT_BUFFER_MODULATION_NAME + ".3.source";
  public final static String EDIT_BUFFER_MODULATION_3_TARGET_NAME    = EDIT_BUFFER_MODULATION_NAME + ".3.target";
  public final static String EDIT_BUFFER_MODULATION_3_AMPLITUDE_NAME = EDIT_BUFFER_MODULATION_NAME + ".3.amplitude";
  
  public final static String EDIT_BUFFER_MODULATION_4_SOURCE_NAME    = EDIT_BUFFER_MODULATION_NAME + ".4.source";
  public final static String EDIT_BUFFER_MODULATION_4_TARGET_NAME    = EDIT_BUFFER_MODULATION_NAME + ".4.target";
  public final static String EDIT_BUFFER_MODULATION_4_AMPLITUDE_NAME = EDIT_BUFFER_MODULATION_NAME + ".4.amplitude";
  
  public final static String EDIT_BUFFER_MODULATION_5_SOURCE_NAME    = EDIT_BUFFER_MODULATION_NAME + ".5.source";
  public final static String EDIT_BUFFER_MODULATION_5_TARGET_NAME    = EDIT_BUFFER_MODULATION_NAME + ".5.target";
  public final static String EDIT_BUFFER_MODULATION_5_AMPLITUDE_NAME = EDIT_BUFFER_MODULATION_NAME + ".5.amplitude";
  
  public final static String EDIT_BUFFER_MODULATION_6_SOURCE_NAME    = EDIT_BUFFER_MODULATION_NAME + ".6.source";
  public final static String EDIT_BUFFER_MODULATION_6_TARGET_NAME    = EDIT_BUFFER_MODULATION_NAME + ".6.target";
  public final static String EDIT_BUFFER_MODULATION_6_AMPLITUDE_NAME = EDIT_BUFFER_MODULATION_NAME + ".6.amplitude";
  
  public final static String EDIT_BUFFER_MODULATION_7_SOURCE_NAME    = EDIT_BUFFER_MODULATION_NAME + ".7.source";
  public final static String EDIT_BUFFER_MODULATION_7_TARGET_NAME    = EDIT_BUFFER_MODULATION_NAME + ".7.target";
  public final static String EDIT_BUFFER_MODULATION_7_AMPLITUDE_NAME = EDIT_BUFFER_MODULATION_NAME + ".7.amplitude";
  
  public final static String EDIT_BUFFER_MODULATION_8_SOURCE_NAME    = EDIT_BUFFER_MODULATION_NAME + ".8.source";
  public final static String EDIT_BUFFER_MODULATION_8_TARGET_NAME    = EDIT_BUFFER_MODULATION_NAME + ".8.target";
  public final static String EDIT_BUFFER_MODULATION_8_AMPLITUDE_NAME = EDIT_BUFFER_MODULATION_NAME + ".8.amplitude";
  
  private void registerParameters_EditBuffer_Modulation ()
  {
    
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MIDI DEVICE PARAMETER MAP
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private final Map<Integer, Set<ParameterDescriptor_QVGT>> controlChangeParameters = new TreeMap<> ();

  private final Set<ParameterDescriptor_QVGT> programChangeParameters = new LinkedHashSet<> ();

  private final Map<Integer, Set<ParameterDescriptor_QVGT>> dataDumpParameters = new TreeMap<> ();

  private final Map<String, Set<ParameterDescriptor_QVGT>> subParameters = new LinkedHashMap<> ();

  @Override
  protected final void registerParameter (final ParameterDescriptor_QVGT parameterDescriptor)
  {
    super.registerParameter (parameterDescriptor);
    switch (parameterDescriptor.getParameterType_QVGT ())
    {
      case MidiControlChange:
      {
        final int controller = parameterDescriptor.getController ();
        if (! this.controlChangeParameters.containsKey (controller))
          this.controlChangeParameters.put (controller, new LinkedHashSet<> ());
        // XXX Weird test: Should test on values of p-d map?
        if (this.controlChangeParameters.get (controller).contains (parameterDescriptor))
          throw new IllegalArgumentException ();
        this.controlChangeParameters.get (controller).add (parameterDescriptor);
        break;
      }
      case MidiProgramChange:
      {
        if (this.programChangeParameters.contains (parameterDescriptor))
          throw new IllegalArgumentException ();
        this.programChangeParameters.add (parameterDescriptor);
        break;
      }
      case MidiSysEx_QVGT_Editing:
      {
        final String parentKey = parameterDescriptor.getParentKey ();
        if (parentKey == null || ! keySet ().contains (parentKey))
          throw new IllegalArgumentException ();
        if (! this.subParameters.containsKey (parentKey))
          this.subParameters.put (parentKey, new LinkedHashSet<> ());
        else if (this.subParameters.get (parentKey).contains (parameterDescriptor))
          throw new IllegalArgumentException ();
        this.subParameters.get (parentKey).add (parameterDescriptor);
        break;
      }
      case MidiSysEx_QVGT_DataDump:
      {
        final int program = parameterDescriptor.getProgram ();
        if (! this.dataDumpParameters.containsKey (program))
          this.dataDumpParameters.put (program, new LinkedHashSet<> ());
        final Set<ParameterDescriptor_QVGT> programDataDumpDescriptors = this.dataDumpParameters.get (program);
        if (programDataDumpDescriptors.contains (parameterDescriptor))
          throw new IllegalArgumentException ();
        programDataDumpDescriptors.add (parameterDescriptor);
        break;
      }
      default:
        throw new RuntimeException ();
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // QVGT MAIN REQUEST LOOP
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private final long QVGT_MAIN_REQUEST_LOOP_PERIOD_MS = 1000L;

  private final Runnable qvgtMainRequestLoop = () ->
  {
    LOG.log (Level.INFO, "Starting Main Request Loop on Alesis Quadraverb GT.");
    try
    {
      while (! Thread.interrupted ())
      {
        // The Alesis Quadraverb GT does not support Midi Id Request...
        // MidiDevice_QVGT.this.sendMidiIdReq ();
        MidiDevice_QVGT.this.sendMidiSysExMessage_QGVT_DumpRequest_EditBuffer ();
        Thread.sleep (MidiDevice_QVGT.this.QVGT_MAIN_REQUEST_LOOP_PERIOD_MS);
      }
    }
    catch (InterruptedException ie)
    {
    }
    LOG.log (Level.INFO, "Terminated Main Request Loop on Alesis Quadraverb GT.");
  };

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // QVGT WATCHDOG
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private volatile Instant lastUpdate = Instant.MIN;

  private volatile boolean watchdogStatus = false;

  private final Runnable qvgtWatchdog = () ->
  {
    try
    {
      LOG.log (Level.INFO, "Starting Watchdog on Alesis Quadraverb GT.");
      fireWatchdogStart ();
      while (! Thread.interrupted ())
      {
        boolean newWatchdogStatus;
        try
        {
          newWatchdogStatus = Duration.between (MidiDevice_QVGT.this.lastUpdate, Instant.now ()).toMillis () <= 2000L;
        }
        catch (ArithmeticException ae)
        {
          newWatchdogStatus = false;
        }
        if (newWatchdogStatus != MidiDevice_QVGT.this.watchdogStatus)
        {
          MidiDevice_QVGT.this.watchdogStatus = newWatchdogStatus;
          if (newWatchdogStatus)
            MidiDevice_QVGT.this.fireWatchdogSuccess ();
          else
            MidiDevice_QVGT.this.fireWatchdogFail ();
        }
        Thread.sleep (1000L);
      }
    }
    catch (InterruptedException ie)
    {
    }
    MidiDevice_QVGT.this.watchdogStatus = false;
    fireWatchdogStop ();
    LOG.log (Level.INFO, "Terminated Watchdog on Alesis Quadraverb GT.");
  };

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // QVGT WATCHDOG LISTENERS
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public static interface MidiDeviceWatchdogListener
    extends MidiDeviceListener
  {
    void watchdogStart ();
    void watchdogStop ();
    void watchdogFail ();
    void watchdogSuccess ();
  }

  protected void fireWatchdogStart ()
  {
    final Set<MidiDeviceListener> midiDeviceListenersCopy = getMidiDeviceListenersCopy ();
    for (final MidiDeviceListener l : midiDeviceListenersCopy)
      if (l instanceof MidiDeviceWatchdogListener)
        ((MidiDeviceWatchdogListener) l).watchdogStart ();
  }

  protected void fireWatchdogStop ()
  {
    final Set<MidiDeviceListener> midiDeviceListenersCopy = getMidiDeviceListenersCopy ();
    for (final MidiDeviceListener l : midiDeviceListenersCopy)
      if (l instanceof MidiDeviceWatchdogListener)
        ((MidiDeviceWatchdogListener) l).watchdogStop ();
  }

  protected void fireWatchdogFail ()
  {
    final Set<MidiDeviceListener> midiDeviceListenersCopy = getMidiDeviceListenersCopy ();
    for (final MidiDeviceListener l : midiDeviceListenersCopy)
      if (l instanceof MidiDeviceWatchdogListener)
        ((MidiDeviceWatchdogListener) l).watchdogFail ();
  }

  protected void fireWatchdogSuccess ()
  {
    final Set<MidiDeviceListener> midiDeviceListenersCopy = getMidiDeviceListenersCopy ();
    for (final MidiDeviceListener l : midiDeviceListenersCopy)
      if (l instanceof MidiDeviceWatchdogListener)
        ((MidiDeviceWatchdogListener) l).watchdogSuccess ();
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MIDI SERVICE [AbstractMidiDevice]
  // TX HANDLING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private void sendMidiSysExMessage_QVGT_Editing
  (final ParameterDescriptor_QVGT.Function_QVGT function, final int page, final int value)
  {
    final byte[] rawMidiMessage = MidiUtils_QVGT.createMidiSysExMessage_QVGT_Editing (function, page, value);
    // XXX The following should work as well!! XXX
    //sendMidiSysEx ((byte) 0x03 /* XXX MUST BE DONE DIFFERENTLY IN SUPER CLASS!! XXX */, rawMidiMessage);
    getMidiService ().sendRawMidiMessage (rawMidiMessage);    
  }

  private void sendMidiSysExMessage_QGVT_DataDump (final Patch_QGVT patch, final int programNumber)
  {
    if (patch == null)
      throw new IllegalArgumentException ();
    final byte[] rawMidiMessage = MidiUtils_QVGT.createMidiSysExMessage_QVGT_DataDump (patch.getEncodedBytes (), programNumber);
    // XXX The following should work as well!! XXX
    //sendMidiSysEx ((byte) 0x03 /* XXX MUST BE DONE DIFFERENTLY IN SUPER CLASS!! XXX */, rawMidiMessage);
    getMidiService ().sendRawMidiMessage (rawMidiMessage);
  }

  private void sendMidiSysExMessage_QGVT_DumpRequest (final int programNumber)
  {
    final byte[] rawMidiMessage = MidiUtils_QVGT.createMidiSysExMessage_QVGT_DumpRequest (programNumber);
    // XXX The following should work as well!! XXX
    //sendMidiSysEx ((byte) 0x03 /* XXX MUST BE DONE DIFFERENTLY IN SUPER CLASS!! XXX */, rawMidiMessage);
    getMidiService ().sendRawMidiMessage (rawMidiMessage);
  }

  private void sendMidiSysExMessage_QGVT_DumpRequest_EditBuffer ()
  {
    final byte[] rawMidiMessage = MidiUtils_QVGT.createMidiSysExMessage_QVGT_DumpRequest_EditBuffer ();
    // XXX The following should work as well!! XXX
    //sendMidiSysEx ((byte) 0x03 /* XXX MUST BE DONE DIFFERENTLY IN SUPER CLASS!! XXX */, rawMidiMessage);
    getMidiService ().sendRawMidiMessage (rawMidiMessage);
  }
  
  private void sendMidiSysExMessage_QGVT_DumpRequest_AllPrograms ()
  {
    final byte[] rawMidiMessage = MidiUtils_QVGT.createMidiSysExMessage_QVGT_DumpRequest_AllPrograms ();
    getMidiService ().sendRawMidiMessage (rawMidiMessage);
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MIDI SERVICE [AbstractMidiDevice]
  // RX HANDLING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  @Override
  protected void onMidiRxProgramChange (final int midiChannel, final int patch)
  {
    super.onMidiRxProgramChange (midiChannel, patch);
    for (final ParameterDescriptor_QVGT parameterDescriptor_QVGT : this.programChangeParameters)
      onParameterReadFromDevice (parameterDescriptor_QVGT.getParameterName (), new byte[]{(byte) patch});
  }

//  @Override
//  protected void onMidiRxControlChange (final int midiChannel, final int controller, final int value)
//  {
//    super.onMidiRxControlChange (midiChannel, controller, value);
//    if (controller < 0 || controller > 127)
//      throw new RuntimeException ();
//    if (value < 0 || value > 127)
//      throw new RuntimeException ();
//    final Set<ParameterDescriptor_RolandBoss> parameters = this.controlChangeParameters.get ((byte) controller);
//    if (parameters != null)
//      for (final ParameterDescriptor_RolandBoss parameter : parameters)
//        onParameterReadFromDevice (parameter.getParameterName (), new byte[]{(byte) value});
//  }

  @Override
  protected void onMidiRxSysEx (final byte vendorId, final byte[] rawMidiMessage)
  {
    super.onMidiRxSysEx (vendorId, rawMidiMessage);
    // LOG.log (Level.INFO, "onMidiRxSysEx, message={0}", HexUtils.bytesToHex (rawMidiMessage));
    if (rawMidiMessage.length < 7                                   // Allow for OpCode; mandatory
      || (rawMidiMessage[0] & 0xFF) != 0xF0                         // System Exclusive Status
      || (rawMidiMessage[1] & 0xFF) != 0x00                         // 0x00000E: Alesis vendor ID
      || (rawMidiMessage[2] & 0xFF) != 0x00                         // 0x00000E: Alesis vendor ID
      || (rawMidiMessage[3] & 0xFF) != 0x0E                         // 0x00000E: Alesis vendor ID
      || (rawMidiMessage[4] & 0xFF) != 0x07                         // Quadraverb GT device ID
      || (rawMidiMessage[rawMidiMessage.length - 1] &0xFF) != 0xF7) // End-Of-Exclusive
    {
      LOG.log (Level.WARNING, "Dropped unknown SysEx message: {0}.", HexUtils.bytesToHex (rawMidiMessage));
      return;
    }
    if (rawMidiMessage.length == 155
      && (rawMidiMessage[5] & 0xFF) == 0x02 // OpCode:
      && (rawMidiMessage[6] >= 0 && rawMidiMessage[6] <= 100)) // Program Number
    {
      final int programNumber = rawMidiMessage[6];
      final byte[] encodedProgramBytes = new byte[Patch_QGVT.ENCODED_PATCH_SIZE];
      System.arraycopy (rawMidiMessage, 7, encodedProgramBytes, 0, Patch_QGVT.ENCODED_PATCH_SIZE);
      this.lastUpdate = Instant.now (); // To please our watchdog...
      onMidiDataDumpFromDevice (programNumber, encodedProgramBytes);
    }
    else
      LOG.log (Level.WARNING, "Dropped unknown SysEx message with length {0}: {1}.",
        new Object[]{rawMidiMessage.length, HexUtils.bytesToHex (rawMidiMessage)});
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MIDI SERVICE [AbstractMidiDevice]
  // RX HANDLING
  //
  // MIDI DATA DUMP FROM DEVICE
  // PARAMETER READ FROM DEVICE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  protected final void onMidiDataDumpFromDeviceEncoded (final int program, final byte[] encodedProgramBytes)
  {
    
  }
  
  protected final void onMidiDataDumpFromDevice (final int program, final byte[] programBytes)
  {
    // LOG.log (Level.INFO, "program={0}.", new Object[]{program});
    if (this.dataDumpParameters.containsKey (program))
      for (final ParameterDescriptor_QVGT pd : this.dataDumpParameters.get (program))
        onParameterReadFromDevice (pd.getParameterName (), programBytes);
    else
      LOG.log (Level.WARNING, "Dropping Midi Data Dump for (unregistered) program {0}.", program);
  }

  protected final void onParameterReadFromDevice (final String key, final byte[] value)
  {
    // LOG.log (Level.INFO, "key={0}, value={1}.", new Object[]{key, HexUtils.bytesToHex (value)});
    if (key == null || ! keySet ().contains (key))
      throw new IllegalArgumentException ();
    final ParameterDescriptor_QVGT parameterDescriptor_qvgt = getParameterDescriptor (key);
    if (parameterDescriptor_qvgt == null)
      throw new IllegalArgumentException ();
    if (value == null)
    {
      //
      // Special treatment of a null value obtained from the device
      // (e.g., because the parameter is not applicable in the given configuration).
      // Invalidate (set to null value) ourselves and all our sub-parameters.
      //
      invalidateParameterFromDevice (key);
      if (this.subParameters.containsKey (key))
        for (final ParameterDescriptor_QVGT subPD : this.subParameters.get (key))
          invalidateParameterFromDevice (subPD.getParameterName ());
    }
    else if (value.length != parameterDescriptor_qvgt.getSize ())
    {
      LOG.log (Level.SEVERE, "key={0}, value={1}, value.length={2}, size={3}",
        new Object[]{key, HexUtils.bytesToHex (value), value.length, parameterDescriptor_qvgt.getSize ()});
      throw new IllegalArgumentException ();
    }
    else
    {
      //
      // We have a non-null value of proper size.
      //
      final Object oValue = parameterDescriptor_qvgt.convertFromDevice (value);
      if (oValue == null)
        throw new RuntimeException ();
      // Small debug section for logging changes (if non-null values) from the device's EDIT BUFFER.
      // if (key.equals (EDIT_BUFFER_NAME))
      // {
      //   final Patch_QGVT oldPatch = (Patch_QGVT) get (EDIT_BUFFER_NAME);
      //   final Patch_QGVT newPatch = (Patch_QGVT) oValue;
      //   if (oldPatch != null)
      //   {
      //     final byte[] oBytes = oldPatch.getDecodedBytes ();
      //     final byte[] nBytes = newPatch.getDecodedBytes ();
      //     for (int i = 0; i < Patch_QGVT.DECODED_PATCH_SIZE; i++)
      //       if (oBytes[i] != nBytes[i])
      //         LOG.log (Level.INFO, "Change in patch detected @0x{0}.", Integer.toHexString (i));
      //   }
      // }
      updateParameterFromDevice (key, oValue);
      if (this.subParameters.containsKey (key))
      {
        final byte[] decodedValue;
        if (oValue instanceof Patch_QGVT)
          decodedValue = ((Patch_QGVT) oValue).getDecodedBytes ();
        else
          decodedValue = value;
        for (final ParameterDescriptor_QVGT subPD : this.subParameters.get (key))
        {
          final byte[] subValue;
          final Function<Object, Boolean> parentValidator = subPD.getParentValidator ();
          if (parentValidator != null && ! parentValidator.apply (oValue))
            subValue = null;
          else
          {
            final int[] offsets = subPD.getOffsets ();
            final int size = subPD.getSize ();
            switch (offsets.length)
            {
              case 0:
                throw new RuntimeException ();
              case 1:
                final int offset = offsets[0];
                if (offset < 0 || size <= 0 || offset + size > decodedValue.length)
                  throw new RuntimeException ();
                subValue = new byte[size];
                System.arraycopy (decodedValue, offset, subValue, 0, size);
                break;
              default:
                if (offsets.length != size)
                  throw new RuntimeException ();
                subValue = new byte[size];
                for (int i = 0; i < size; i++)
                  if (offsets[i] < 0 && offsets[i] >= decodedValue.length)
                    throw new RuntimeException ();
                  else
                    subValue[i] = decodedValue[offsets[i]];
                break;
            }
            if (subPD.getBitOffset () != null)
            {
              if (subValue.length != 1)
                throw new RuntimeException ();
              final int bitOffset = subPD.getBitOffset ();
              final int bitSize = subPD.getBitSize ();
              // First; zero everything left of us in the byte.
              subValue[0] = (byte) ((subValue[0] << bitOffset) & 0xFF);
              // Value now in first bitSize bits of subValue[0]; move it to the "last" bitSize bits in the byte.
              subValue[0] = (byte) ((subValue[0] & 0xFF) >>> (8 - bitSize));
            }
          }
          onParameterReadFromDevice (subPD.getParameterName (), subValue);
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // Map.put IMPLEMENTATION
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  @Override
  protected Object putImpl (final String key, final Object value)
  {
    // LOG.log (Level.INFO, "Put: {0}: {1} -> {2}.", new Object[]{key, super.get (key), value});
    // XXX Shouldn't we check the old value and ignore the request when appropriate?
    if (key == null || ! keySet ().contains (key))
      throw new IllegalArgumentException ();
    final ParameterDescriptor_QVGT parameterDescriptor_QVGT = getParameterDescriptor (key);
    if (parameterDescriptor_QVGT == null)
      throw new RuntimeException ();
    synchronized (this)
    {
      final Object oldValue = super.get (key);
      if (getStatus () != Status.STOPPED && getMidiService () != null)
      {
        switch (parameterDescriptor_QVGT.getParameterType_QVGT ())
        {
          case MidiControlChange:
          {
            final byte[] convertedValue = parameterDescriptor_QVGT.convertToDevice (value);
            if (convertedValue == null)
              throw new IllegalArgumentException ();
            sendMidiControlChange (getMidiChannel (), parameterDescriptor_QVGT.getController (), (int) convertedValue[0]);
            return oldValue;
          }
          case MidiProgramChange:
          {
            final byte[] convertedValue = parameterDescriptor_QVGT.convertToDevice (value);
            if (convertedValue == null || convertedValue.length != 1)
              throw new IllegalArgumentException ();
            sendMidiProgramChange (getMidiChannel (), (int) convertedValue[0]);
            return oldValue;
          }
          case MidiSysEx_QVGT_Editing:
          {
            final byte[] convertedValue = parameterDescriptor_QVGT.convertToDevice (value);
            // LOG.log (Level.INFO, "value={0} -> convertedValue={1}.", new Object[]{value, HexUtils.bytesToHex (convertedValue)});
            if (convertedValue == null)
              throw new IllegalArgumentException ();
            if (parameterDescriptor_QVGT.getFunction () == null)
            {
              // XXX
              throw new UnsupportedOperationException ();
            }
            else
            {
              final ParameterDescriptor_QVGT.Function_QVGT function = parameterDescriptor_QVGT.getFunction ();
              final int page = parameterDescriptor_QVGT.getPage ();
              final int editingValue;
              switch (convertedValue.length)
              {
                case 1:
                  editingValue = convertedValue[0] & 0xFF;
                  break;
                case 2:
                  editingValue = (((convertedValue[0] & 0xFF)) << 8)
                                 + (convertedValue[1] & 0xFF);
                  break;
                default:
                  throw new RuntimeException ();
              }
              sendMidiSysExMessage_QVGT_Editing (function, page, editingValue);
            }
            return oldValue;
          }
          case MidiSysEx_QVGT_DataDump:
          {
            if (value == null || ! (value instanceof Patch_QGVT))
              throw new IllegalArgumentException ();
            final int programNumber = parameterDescriptor_QVGT.getProgram ();
            sendMidiSysExMessage_QGVT_DataDump ((Patch_QGVT) value, programNumber);
            return oldValue;
          }
          default:
            throw new RuntimeException ();
        }
      }
      return oldValue;
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // WRITE PATCH TO DEVICE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /** Writes a patch to a specific patch slot on the Alesis Quadraverb GT.
   * 
   * <p>
   * The request is ignored if the device (as a {@link Service}) is not active.
   * 
   * @param patch             The patch, non-{@code null} and of proper size.
   * @param targetPatchNumber The target patch (program) number on the Alesis Quadraverb GT.
   * 
   * @throws IllegalArgumentException If the patch is {@code null}.
   *                                  or the target patch number is out of range (0 through 99 inclusive).
   *
   * @see MidiDevice_QVGT#EDIT_BUFFER_PROGRAM_NUMBER
   * @see Service#getStatus
   * @see Status#ACTIVE
   * 
   */
  public final void writePatchToDevice
  ( final Patch_QGVT patch,
    final int targetPatchNumber)
  {
    if (patch == null || targetPatchNumber < 0 || targetPatchNumber >= MidiDevice_QVGT.EDIT_BUFFER_PROGRAM_NUMBER)
      throw new IllegalArgumentException ();
    if (getStatus () != Status.ACTIVE)
      return;
    // LOG.log (Level.INFO, "Writing {0} to {1}.", new Object[]{patch, targetPatchNumber});
    sendMidiSysExMessage_QGVT_DataDump (patch, targetPatchNumber);
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
