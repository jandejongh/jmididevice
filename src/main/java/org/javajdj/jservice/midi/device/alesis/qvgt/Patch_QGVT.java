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

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.javajdj.jservice.midi.MidiUtils;
import org.javajdj.util.hex.HexUtils;

/** A patch (aka program) on the Alesis Quadraverb GT.
 *
 * <p>
 * Actually, this object just holds the patch <i>data</i> without
 * any reference to the Quadraverb GT (like program number).
 * 
 * <p>
 * A patch is an immutable object.
 * Its intended use is for saving patches to the Quadraverb GT, to the file system, or to a patch library.
 * 
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 */
public final class Patch_QGVT
  implements Cloneable
{
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // LOGGING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private static final Logger LOG = Logger.getLogger (Patch_QGVT.class.getName ());
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTORS / FACTORIES / CLONING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /** The size of the decoded patch data in bytes.
   * 
   */
  public final static int DECODED_PATCH_SIZE = 128;
  
  /** The size of the encoded patch data in bytes.
   * 
   */
  public final static int ENCODED_PATCH_SIZE = 147;
  
  /** Forbids parameter-less instantiation.
   * 
   * @throws UnsupportedOperationException Always.
   * 
   */
  private Patch_QGVT ()
  {
    throw new UnsupportedOperationException ();
  }
  
  /** Instantiation from a byte array.
   * 
   * <p>
   * This constructor accepts both <i>decoded</i> patch data
   * (array with size {@link Patch_QGVT#DECODED_PATCH_SIZE}
   * containing arbitrary values)
   * and <i>encoded</i> patch data
   * (array with size {@link Patch_QGVT#ENCODED_PATCH_SIZE}
   * containing MIDI Data Bytes only).
   * 
   * <p>
   * Reserved for {@link Patch_QGVT} and sub-class use.
   * 
   * @param bytes The patch bytes.
   * 
   * @throws IllegalArgumentException If the array is {@code null}, of inappropriate size,
   *                                    has unexpected MIDI Control Bytes (encoded use case),
   *                                    or contains otherwise illegal values for a Quadraverb GT patch.
   * 
   * @see Patch_QGVT#DECODED_PATCH_SIZE
   * @see Patch_QGVT#ENCODED_PATCH_SIZE
   * 
   */
  protected Patch_QGVT (final byte[] bytes)
  {
    if (bytes == null)
      throw new IllegalArgumentException ();
    switch (bytes.length)
    {
      case DECODED_PATCH_SIZE:
        this.decodedBytes = MidiUtils.ensureAllMidiDataBytes (bytes).clone ();
        this.encodedBytes = MidiUtils_QVGT.encodeToMidi (this.decodedBytes, Patch_QGVT.ENCODED_PATCH_SIZE);
        break;
      case ENCODED_PATCH_SIZE:
        this.decodedBytes= MidiUtils_QVGT.decodeFromMidi (bytes, Patch_QGVT.DECODED_PATCH_SIZE);
        this.encodedBytes = bytes.clone ();
        break;
      default:
        throw new IllegalArgumentException ();
    }
    // Check additional values indirectly through getName () and getConfiguration ().
    try
    {
      getName ();
      getConfiguration ();
    }
    catch (RuntimeException re)
    {
      throw new IllegalArgumentException ();
    }
  }
  
  /** Generates a ME-80 patch from (raw) decodedBytes.
   * 
   * <p>
   * This method accepts both <i>decoded</i> patch data
   * (array with size {@link Patch_QGVT#DECODED_PATCH_SIZE}
   * containing arbitrary values)
   * and <i>encoded</i> patch data
   * (array with size {@link Patch_QGVT#ENCODED_PATCH_SIZE}
   * containing MIDI Data Bytes only).
   * 
   * @param bytes The patch bytes.
   * 
   * @return The patch.
   * 
   * @throws IllegalArgumentException If the array is {@code null}, of inappropriate size,
   *                                    has unexpected MIDI Control Bytes (encoded use case),
   *                                    or contains otherwise illegal values for a Quadraverb GT patch.
   * 
   * @see Patch_QGVT#DECODED_PATCH_SIZE
   * @see Patch_QGVT#ENCODED_PATCH_SIZE
   * 
   */
  public static Patch_QGVT fromBytes (final byte[] bytes)
  {
    return new Patch_QGVT (bytes);
  }
  
  /** Returns a copy of this patch with another name.
   * 
   * @param name The patch name of the copy.
   * 
   * @return A copy of this patch with another name.
   * 
   * @throws IllegalArgumentException If the name is {@code null}, empty, whitespace-only, or longer than 14 characters.
   * 
   */
  public final Patch_QGVT withName (final String name)
  {
    if (name == null || name.trim ().length () == 0 || name.trim ().length () > 14)
      throw new IllegalArgumentException ();
    final byte[] newNameBytesTrimmed;
    try
    {
      newNameBytesTrimmed = name.trim ().getBytes ("US-ASCII");
    }
    catch (UnsupportedEncodingException uee)
    {
      LOG.log (Level.WARNING, "Encoding Error for US-ASCII: {0}.", name);
      throw new IllegalArgumentException ();
    }
    final byte[] newNameBytes;
    if (newNameBytesTrimmed.length == 14)
      newNameBytes = newNameBytesTrimmed;
    else
    {
      newNameBytes = new byte[14];
      System.arraycopy (newNameBytesTrimmed, 0, newNameBytes, 0, newNameBytesTrimmed.length);
      Arrays.fill (newNameBytes, newNameBytesTrimmed.length, 14, (byte) 0x20 /* US-ASCII SPACE */);
    }
    final byte[] newBytes = this.decodedBytes.clone ();
    System.arraycopy (newNameBytes, 0, newBytes, 0x6A, 14);
    return new Patch_QGVT (newBytes);
  }
  
  /** Returns a clone (copy) of this patch (data).
   * 
   * @return A clone (copy) of this patch (data).
   * 
   * @throws CloneNotSupportedException Never.
   * 
   */
  @Override
  public final Patch_QGVT clone () throws CloneNotSupportedException
  {
    return (Patch_QGVT) super.clone ();
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // DECODED/ENCODED BYTES
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final byte[] decodedBytes;
  
  private final byte[] encodedBytes;
  
  /** Returns the (raw) decoded bytes of this (decoded) patch.
   * 
   * @return The (raw) decoded bytes of this (decoded) patch, non-{@code null} and of size {@link Patch_QGVT#DECODED_PATCH_SIZE}.
   * 
   */
  public final byte[] getDecodedBytes ()
  {
    return this.decodedBytes.clone ();
  }
  
  /** Returns the (raw) encoded bytes of this patch.
   * 
   * @return The (raw) encoded bytes of this patch, non-{@code null} and of size {@link Patch_QGVT#ENCODED_PATCH_SIZE}.
   * 
   */
  public final byte[] getEncodedBytes ()
  {
    return this.encodedBytes.clone ();
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // NAME
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  /** Returns the name of this patch (as encoded in the patch data).
   * 
   * @return The name of this patch (as encoded in the patch data),
   *         {@code null} if the name contains non-{@code US-ASCII} characters.
   * 
   */
  public final String getName ()
  {
    final byte[] nameBytes = new byte[14];
    System.arraycopy (this.decodedBytes, 0x6A, nameBytes, 0, 14);
    try
    {
      return new String (nameBytes, "US-ASCII").trim ();          
    }
    catch (UnsupportedEncodingException uee)
    {
      LOG.log (Level.WARNING, "Encoding Error for US-ASCII: {0}.", HexUtils.bytesToHex (nameBytes));
      throw new RuntimeException ();
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONFIGURATION
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  /** The offset in the decoded Alesis Quadraverb GT patch/program data of the patch configuration.
   * 
   * @see Configuration
   * 
   */
  public final static int OFFSET_CONFIGURATION = 0x44;
  
  /** The configuration of a Alesis Quadraverb GT patch.
   * 
   * <p>
   * Configuration numbering follows the Reference Manual convention, i.e.,
   * numbering start with unity.
   * (Not the Service Manual convention.)
   * 
   */
  public enum Configuration
  {
    
    C1_EQ_PCH_DL_REV,
    C2_LES_DL_REV,
    C3_GEQ_DL,
    C4_5EQ_PCH_DL,
    C5_3EQ_REV,
    C6_RING_DL_REV,
    C7_RESO_DL_REV,
    C8_SAMPLING;
    
  }
  
  /** Returns the Alesis Quadraverb GT configuration of this patch (as found in the patch data).
   * 
   * <p>
   * The Alesis Quadraverb GT configuration is held in byte {@code 0x44} in the patch data.
   * Its allowed values are 0 through 7 inclusive.
   * 
   * @return The Alesis Quadraverb GT configuration of this patch (as found in the patch data).
   * 
   * @throws RuntimeException If the patch data is invalid with respect to the patch configuration.
   * 
   */
  public final Configuration getConfiguration ()
  {
    final int configurationInt = this.decodedBytes[Patch_QGVT.OFFSET_CONFIGURATION];
    if (configurationInt < 0 || configurationInt > 7)
      throw new RuntimeException ();
    return Configuration.values ()[configurationInt];
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // EQ MODE [CONFIGURATIONS 1 OR 4 ONLY]
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /** The offset in the decoded Alesis Quadraverb GT patch/program data of the eq mode.
   * 
   * <p>
   * The eq mode has a unique address in the patch data; it is the first (most significant) bit in {@code 0x7C},
   * but is is only valid in {@link Configuration#C1_EQ_PCH_DL_REV} and {@link Configuration#C4_5EQ_PCH_DL},
   * in which case it represents a {@link MidiDevice_QVGT.EqModeConfig1} or {@link MidiDevice_QVGT.EqModeConfig4},
   * respectively.
   * 
   * @see MidiDevice_QVGT.EqModeConfig1
   * @see MidiDevice_QVGT.EqModeConfig4
   * @see Configuration
   * @see Configuration#C1_EQ_PCH_DL_REV
   * @see Configuration#C4_5EQ_PCH_DL
   * 
   */
  public final static int OFFSET_EQ_MODE = 0x7C;
  
  /** Returns the eq mode of this program, provided it is in configuration 1.
   * 
   * <p>
   * A {@link RuntimeException} is thrown if this program is in a configuration other than
   * {@link Configuration#C1_EQ_PCH_DL_REV},
   * or if the patch data is otherwise found to be invalid.
   * 
   * @return The eq mode of this program (if in configuration 1).
   * 
   * @see #getConfiguration
   * @see MidiDevice_QVGT.EqModeConfig1
   * @see Configuration#C1_EQ_PCH_DL_REV
   * 
   */
  public final MidiDevice_QVGT.EqModeConfig1 getEqMode_Config1 ()
  {
    switch (getConfiguration ())
    {
      case C1_EQ_PCH_DL_REV:
        final boolean bitValue = (this.decodedBytes[Patch_QGVT.OFFSET_EQ_MODE] & 0x80) != 0;
        return MidiDevice_QVGT.EqModeConfig1.values ()[bitValue ? 1 : 0];
      default:
        throw new RuntimeException ();
    }
  }
  
  /** Returns the eq mode of this program, provided it is in configuration 4.
   * 
   * <p>
   * A {@link RuntimeException} is thrown if this program is in a configuration other than
   * {@link Configuration#C4_5EQ_PCH_DL},
   * or if the patch data is otherwise found to be invalid.
   * 
   * @return The eq mode of this program (if in configuration 4).
   * 
   * @see #getConfiguration
   * @see MidiDevice_QVGT.EqModeConfig4
   * @see Configuration#C4_5EQ_PCH_DL
   * 
   */
  public final MidiDevice_QVGT.EqModeConfig4 getEqMode_Config4 ()
  {
    switch (getConfiguration ())
    {
      case C4_5EQ_PCH_DL:
        final boolean bitValue = (this.decodedBytes[Patch_QGVT.OFFSET_EQ_MODE] & 0x80) != 0;
        return MidiDevice_QVGT.EqModeConfig4.values ()[bitValue ? 1 : 0];
      default:
        throw new RuntimeException ();
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PITCH MODE [CONFIGURATIONS 1 AND 4 ONLY]
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  /** The offset in the decoded Alesis Quadraverb GT patch/program data of the pitch mode.
   * 
   * <p>
   * The pitch mode has a unique address in the patch data, but is is only valid in
   * {@link Configuration#C1_EQ_PCH_DL_REV} and {@link Configuration#C4_5EQ_PCH_DL},
   * in which case it represents a {@link MidiDevice_QVGT.PitchMode},
   * or in {@link Configuration#C5_3EQ_REV},
   * in which case it represents a {@link Boolean} indicating the status of the chorus effect.
   * 
   * @see MidiDevice_QVGT.PitchMode
   * @see Configuration
   * @see Configuration#C1_EQ_PCH_DL_REV
   * @see Configuration#C4_5EQ_PCH_DL
   * @see Configuration#C5_3EQ_REV
   * 
   */
  public final static int OFFSET_PITCH_MODE = 0x1A;
  
  /** Returns the pitch mode of this program, provided it is in configurations 1 or 4.
   * 
   * <p>
   * A {@link RuntimeException} is thrown if this program is in a configuration other than
   * {@link Configuration#C1_EQ_PCH_DL_REV} or {@link Configuration#C4_5EQ_PCH_DL},
   * or if the patch data is otherwise found to be invalid.
   * 
   * @return The pitch mode of this program (if in configuration 1 or 4).
   * 
   * @see #getConfiguration
   * @see MidiDevice_QVGT.PitchMode
   * @see Configuration#C1_EQ_PCH_DL_REV
   * @see Configuration#C4_5EQ_PCH_DL
   * 
   */
  public final MidiDevice_QVGT.PitchMode getPitchMode_Configs14 ()
  {
    switch (getConfiguration ())
    {
      case C1_EQ_PCH_DL_REV:
      case C4_5EQ_PCH_DL:
        final int byteValue = this.decodedBytes[Patch_QGVT.OFFSET_PITCH_MODE];
        if (byteValue < 0)
          throw new RuntimeException ();
        final MidiDevice_QVGT.PitchMode[] enumValues = MidiDevice_QVGT.PitchMode.values ();
        if (byteValue >= enumValues.length)
          throw new RuntimeException ();
        return enumValues[byteValue];
      default:
        throw new RuntimeException ();
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // REVERB MODE [CONFIGURATIONS 1, 2, 5, 6, 7 ONLY]
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  /** The offset in the decoded Alesis Quadraverb GT patch/program data of the reverb mode.
   * 
   * <p>
   * The reverb mode has a unique address in the patch data, but is is only valid in
   * {@link Configuration#C1_EQ_PCH_DL_REV},
   * {@link Configuration#C2_LES_DL_REV},
   * {@link Configuration#C5_3EQ_REV},
   * {@link Configuration#C6_RING_DL_REV},
   * and {@link Configuration#C7_RESO_DL_REV},
   * in which case it represents a {@link MidiDevice_QVGT.ReverbMode}.
   * 
   * @see MidiDevice_QVGT.ReverbMode
   * @see Configuration
   * @see Configuration#C1_EQ_PCH_DL_REV
   * @see Configuration#C2_LES_DL_REV
   * @see Configuration#C5_3EQ_REV
   * @see Configuration#C6_RING_DL_REV
   * @see Configuration#C7_RESO_DL_REV
   * 
   */
  public final static int OFFSET_REVERB_MODE = 0x32;
  
  /** Returns the reverb mode of this program, provided it is in configurations 1, 2, 5, 6, or 7.
   * 
   * <p>
   * A {@link RuntimeException} is thrown if this program is in a configuration other than
   * {@link Configuration#C1_EQ_PCH_DL_REV},
   * {@link Configuration#C2_LES_DL_REV},
   * {@link Configuration#C5_3EQ_REV},
   * {@link Configuration#C6_RING_DL_REV},
   * or {@link Configuration#C7_RESO_DL_REV},
   * or if the patch data is otherwise found to be invalid.
   * 
   * @return The reverb mode of this program (if in configuration 1, 2, 5, 6, or 7).
   * 
   * @see #getConfiguration
   * @see MidiDevice_QVGT.ReverbMode
   * @see Configuration#C1_EQ_PCH_DL_REV
   * @see Configuration#C2_LES_DL_REV
   * @see Configuration#C5_3EQ_REV
   * @see Configuration#C6_RING_DL_REV
   * @see Configuration#C7_RESO_DL_REV
   * 
   */
  public final MidiDevice_QVGT.ReverbMode getReverbMode ()
  {
    switch (getConfiguration ())
    {
      case C1_EQ_PCH_DL_REV:
      case C2_LES_DL_REV:
      case C5_3EQ_REV:
      case C6_RING_DL_REV:
      case C7_RESO_DL_REV:
        final int byteValue = this.decodedBytes[Patch_QGVT.OFFSET_REVERB_MODE];
        if (byteValue < 0)
          throw new RuntimeException ();
        final MidiDevice_QVGT.ReverbMode[] enumValues = MidiDevice_QVGT.ReverbMode.values ();
        if (byteValue >= enumValues.length)
          throw new RuntimeException ();
        return enumValues[byteValue];
      default:
        throw new RuntimeException ();
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // EQUALS / HASHCODE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  @Override
  public final int hashCode ()
  {
    int hash = 3;
    hash = 23 * hash + Arrays.hashCode (this.decodedBytes);
    return hash;
  }

  @Override
  public final boolean equals (final Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass () != obj.getClass ())
      return false;
    final Patch_QGVT other = (Patch_QGVT) obj;
    return Arrays.equals (this.decodedBytes, other.decodedBytes);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
