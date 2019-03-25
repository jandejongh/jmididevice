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

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.javajdj.util.hex.HexUtils;

/** A patch (aka program) on the Boss ME-80.
 *
 * <p>
 * Actually, this object just holds the patch <i>data</i> without
 * any reference to the ME-80 (like bank number).
 * 
 * <p>
 * A patch is an immutable object.
 * Its intended use is for saving patches to the ME-80, to the file system, or to a patch library.
 * 
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 */
public final class Patch_Me80
  implements Cloneable
{
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // LOGGING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private static final Logger LOG = Logger.getLogger (Patch_Me80.class.getName ());
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTORS / FACTORIES / CLONING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /** Forbids parameter-less instantiation.
   * 
   * @throws UnsupportedOperationException Always.
   * 
   */
  private Patch_Me80 ()
  {
    throw new UnsupportedOperationException ();
  }
  
  /** Instantiation from a byte array.
   * 
   * <p>
   * Reserved for {@link Patch_Me80} and sub-class use.
   * 
   * @param bytes The patch bytes.
   * 
   * @throws IllegalArgumentException If the array is {@code null} or of inappropriate size.
   * 
   * @see MidiDevice_Me80_Base#PATCH_SIZE
   * 
   */
  protected Patch_Me80 (final byte[] bytes)
  {
    if (bytes == null || bytes.length != MidiDevice_Me80_Base.PATCH_SIZE)
      throw new IllegalArgumentException ();
    this.bytes = bytes.clone ();
  }
  
  /** Generates a ME-80 patch from (raw) bytes.
   * 
   * @param bytes The patch bytes.
   * 
   * @return The patch.
   * 
   */
  public static Patch_Me80 fromBytes (final byte[] bytes)
  {
    return new Patch_Me80 (bytes);
  }
  
  /** Returns a copy of this patch with another name.
   * 
   * @param name The patch name of the copy.
   * 
   * @return A copy of this patch with another name, {@code null} if the name is illegal
   *         (containing non-{@code US-ASCII} characters).
   * 
   * @throws IllegalArgumentException If the name is {@code null}, empty, whitespace-only, or longer than 16 bytes.
   * 
   */
  public Patch_Me80 withName (final String name)
  {
    if (name == null || name.trim ().length () == 0 || name.trim ().length () > 16)
      throw new IllegalArgumentException ();
    final byte[] newNameBytesTrimmed;
    try
    {
      newNameBytesTrimmed = name.getBytes ("US-ASCII");
    }
    catch (UnsupportedEncodingException uee)
    {
      LOG.log (Level.WARNING, "Encoding Error for US-ASCII: {0}.", name);
      throw new IllegalArgumentException ();
    }
    final byte[] newNameBytes;
    if (newNameBytesTrimmed.length == 16)
      newNameBytes = newNameBytesTrimmed;
    else
    {
      newNameBytes = new byte[16];
      System.arraycopy (newNameBytesTrimmed, 0, newNameBytes, 0, newNameBytesTrimmed.length);
      Arrays.fill (newNameBytes, newNameBytesTrimmed.length, 16, (byte) 0x20 /* US-ASCII SPACE */);
    }
    final byte[] newBytes = this.bytes.clone ();
    System.arraycopy (newNameBytes, 0, newBytes, 0, 16);
    return new Patch_Me80 (newBytes);
  }
  
  /** Returns a new patch with data supplied by a {@link Map} from (e.g.) TLS/JSON parsing.
   * 
   * @param jsonMap The map, non-{@code null}.
   * 
   * @return The patch.
   *
   * @throws IllegalArgumentException If {@code jsonMap == null} or has invalid structure (in JSON or TLS sense).
   * 
   */
  public static Patch_Me80 fromTlsJsonMap (final Map<String, Object> jsonMap)
  {
    if (jsonMap == null)
      throw new IllegalArgumentException ();
    final byte[] bytes = new byte[MidiDevice_Me80_Base.PATCH_SIZE];
    // Name
    final String patchName = (String) jsonMap.get ("patchname");
    if (patchName == null)
      throw new IllegalArgumentException ();
    // COMP
    bytes[0x10] = (byte) Integer.parseInt ((String) jsonMap.get ("comp_sw"));
    bytes[0x18] = (byte) Integer.parseInt ((String) jsonMap.get ("comp_type"));
    bytes[0x20] = (byte) Integer.parseInt ((String) jsonMap.get ("comp1"));
    bytes[0x21] = (byte) Integer.parseInt ((String) jsonMap.get ("comp2"));
    bytes[0x22] = (byte) Integer.parseInt ((String) jsonMap.get ("comp3"));
    // OD/DS
    bytes[0x11] = (byte) Integer.parseInt ((String) jsonMap.get ("odds_sw"));
    bytes[0x19] = (byte) Integer.parseInt ((String) jsonMap.get ("odds_type"));
    bytes[0x23] = (byte) Integer.parseInt ((String) jsonMap.get ("odds1"));
    bytes[0x24] = (byte) Integer.parseInt ((String) jsonMap.get ("odds2"));
    bytes[0x25] = (byte) Integer.parseInt ((String) jsonMap.get ("odds3"));
    // MOD
    bytes[0x12] = (byte) Integer.parseInt ((String) jsonMap.get ("mod_sw"));
    bytes[0x1A] = (byte) Integer.parseInt ((String) jsonMap.get ("mod_type"));
    bytes[0x26] = (byte) Integer.parseInt ((String) jsonMap.get ("mod1"));
    bytes[0x27] = (byte) Integer.parseInt ((String) jsonMap.get ("mod2"));
    bytes[0x28] = (byte) Integer.parseInt ((String) jsonMap.get ("mod3"));
    // DELAY
    bytes[0x13] = (byte) Integer.parseInt ((String) jsonMap.get ("dly_sw"));
    bytes[0x1B] = (byte) Integer.parseInt ((String) jsonMap.get ("dly_type"));
    bytes[0x29] = (byte) Integer.parseInt ((String) jsonMap.get ("dly1"));
    bytes[0x2A] = (byte) Integer.parseInt ((String) jsonMap.get ("dly2"));
    bytes[0x2B] = (byte) Integer.parseInt ((String) jsonMap.get ("dly3"));
    // AMP
    bytes[0x14] = (byte) Integer.parseInt ((String) jsonMap.get ("amp_sw"));
    bytes[0x1C] = (byte) Integer.parseInt ((String) jsonMap.get ("amp_type"));
    bytes[0x2C] = (byte) Integer.parseInt ((String) jsonMap.get ("amp1"));
    bytes[0x2D] = (byte) Integer.parseInt ((String) jsonMap.get ("amp2"));
    bytes[0x2E] = (byte) Integer.parseInt ((String) jsonMap.get ("amp3"));
    bytes[0x2F] = (byte) Integer.parseInt ((String) jsonMap.get ("amp4"));
    bytes[0x30] = (byte) Integer.parseInt ((String) jsonMap.get ("amp5"));
    // EQ/FX2
    bytes[0x15] = (byte) Integer.parseInt ((String) jsonMap.get ("fx2_sw"));
    bytes[0x1D] = (byte) Integer.parseInt ((String) jsonMap.get ("fx2_type"));
    bytes[0x31] = (byte) Integer.parseInt ((String) jsonMap.get ("fx2_1"));
    bytes[0x32] = (byte) Integer.parseInt ((String) jsonMap.get ("fx2_2"));
    bytes[0x33] = (byte) Integer.parseInt ((String) jsonMap.get ("fx2_3"));
    bytes[0x34] = (byte) Integer.parseInt ((String) jsonMap.get ("fx2_4"));
    // REVERB
    bytes[0x16] = (byte) Integer.parseInt ((String) jsonMap.get ("rev_sw"));
    bytes[0x1F] = (byte) Integer.parseInt ((String) jsonMap.get ("rev_type"));
    bytes[0x3A] = (byte) Integer.parseInt ((String) jsonMap.get ("rev"));
    // PEDAL/FX
    bytes[0x17] = (byte) Integer.parseInt ((String) jsonMap.get ("pdlfx_sw"));
    bytes[0x1E] = (byte) Integer.parseInt ((String) jsonMap.get ("pdlfx_type"));
    // NS
    bytes[0x39] = (byte) Integer.parseInt ((String) jsonMap.get ("ns_thresh"));
    // CTL
    bytes[0x35] = (byte) Integer.parseInt ((String) jsonMap.get ("ctl_target_h"));
    bytes[0x36] = (byte) Integer.parseInt ((String) jsonMap.get ("ctl_target_l"));
    bytes[0x37] = (byte) Integer.parseInt ((String) jsonMap.get ("ctrl_knob_value"));
    bytes[0x38] = (byte) Integer.parseInt ((String) jsonMap.get ("ctl_mode"));
    // BPM
    final int modulationBpm = Integer.parseInt ((String) jsonMap.get ("modulation_bpm"));
    bytes[0x3B] = (byte) ((modulationBpm & 0xff00) >>> 8);
    bytes[0x3C] = (byte) (modulationBpm & 0xff);
    final int delayBpm = Integer.parseInt ((String) jsonMap.get ("delay_bpm"));
    bytes[0x3D] = (byte) ((delayBpm & 0xff00) >>> 8);
    bytes[0x3E] = (byte) (delayBpm & 0xff);
    // DUMMY
    // bytes[0x3F] value_dummy_1[h] value_dummy_1_h
    // bytes[0x40] value_dummy_1[l] value_dummy_1_l
    // bytes[0x41] value_dummy_2[h] value_dummy_2_h
    // bytes[0x42] value_dummy_2[l] value_dummy_2_l
    // bytes[0x43] value_dummy_3[h] value_dummy_3_h
    // bytes[0x44] value_dummy_3[l] value_dummy_3_l
    // bytes[0x45] value_dummy_4[h] value_dummy_4_h
    // bytes[0x46] value_dummy_4[l] value_dummy_4_l
    return new Patch_Me80 (bytes).withName (patchName);
  }

  /** Returns a map (for further JSON/TSL-format processing) describing this patch.
   * 
   * <p>
   * The keys and values chosen in the {@link Map} conform
   * with the (Boss) TSL format for (multi-) patch files.
   * 
   * @return A (new) map describing this patch.
   * 
   */
  public final Map<String, Object> toTlsJsonMap ()
  {
    final Map<String, Object> map = new HashMap<> ();
    // Name
    for (int i = 1; i <= 16; i++)
      map.put ("name" + i, this.bytes[i - 1]);
    map.put ("patchname", getName ()); // We do not have to align this to 16 characters; checked on example .tsl files.
    // COMP
    map.put ("comp_sw",   Integer.toString (this.bytes[0x10]));
    map.put ("comp_type", Integer.toString (this.bytes[0x18]));
    map.put ("comp1",     Integer.toString (this.bytes[0x20]));
    map.put ("comp2",     Integer.toString (this.bytes[0x21]));
    map.put ("comp3",     Integer.toString (this.bytes[0x22]));
    // OD/DS
    map.put ("odds_sw",   Integer.toString (this.bytes[0x11]));
    map.put ("odds_type", Integer.toString (this.bytes[0x19]));
    map.put ("odds1",     Integer.toString (this.bytes[0x23]));
    map.put ("odds2",     Integer.toString (this.bytes[0x24]));
    map.put ("odds3",     Integer.toString (this.bytes[0x25]));
    // MOD
    map.put ("mod_sw",   Integer.toString (this.bytes[0x12]));
    map.put ("mod_type", Integer.toString (this.bytes[0x1A]));
    map.put ("mod1",     Integer.toString (this.bytes[0x26]));
    map.put ("mod2",     Integer.toString (this.bytes[0x27]));
    map.put ("mod3",     Integer.toString (this.bytes[0x28]));
    // DELAY
    map.put ("dly_sw",   Integer.toString (this.bytes[0x13]));
    map.put ("dly_type", Integer.toString (this.bytes[0x1B]));
    map.put ("dly1",     Integer.toString (this.bytes[0x29]));
    map.put ("dly2",     Integer.toString (this.bytes[0x2A]));
    map.put ("dly3",     Integer.toString (this.bytes[0x2B]));
    // AMP
    map.put ("amp_sw",   Integer.toString (this.bytes[0x14]));
    map.put ("amp_type", Integer.toString (this.bytes[0x1C]));
    map.put ("amp1",     Integer.toString (this.bytes[0x2C]));
    map.put ("amp2",     Integer.toString (this.bytes[0x2D]));
    map.put ("amp3",     Integer.toString (this.bytes[0x2E]));
    map.put ("amp4",     Integer.toString (this.bytes[0x2F]));
    map.put ("amp5",     Integer.toString (this.bytes[0x30]));
    // EQ/FX2
    map.put ("fx2_sw",   Integer.toString (this.bytes[0x15]));
    map.put ("fx2_type", Integer.toString (this.bytes[0x1D]));
    map.put ("fx2_1",    Integer.toString (this.bytes[0x31]));
    map.put ("fx2_2",    Integer.toString (this.bytes[0x32]));
    map.put ("fx2_3",    Integer.toString (this.bytes[0x33]));
    map.put ("fx2_4",    Integer.toString (this.bytes[0x34]));
    // REVERB
    map.put ("rev_sw",   Integer.toString (this.bytes[0x16]));
    map.put ("rev_type", Integer.toString (this.bytes[0x1F]));
    map.put ("rev",      Integer.toString (this.bytes[0x3A]));
    // PEDAL/FX
    map.put ("pdlfx_sw",   Integer.toString (this.bytes[0x17]));
    map.put ("pdlfx_type", Integer.toString (this.bytes[0x1E]));
    // NS
    map.put ("ns_thresh", Integer.toString (this.bytes[0x39]));
    // CTL
    map.put ("ctl_target_h",    Integer.toString (this.bytes[0x35]));
    map.put ("ctl_target_l",    Integer.toString (this.bytes[0x36]));
    map.put ("ctl_target",      Integer.toString ((this.bytes[0x35] << 8) + this.bytes[0x46]));
    map.put ("ctrl_knob_value", Integer.toString (this.bytes[0x37]));
    map.put ("ctl_mode",        Integer.toString (this.bytes[0x38]));
    // BPM
    map.put ("modulation_bpm_h", Integer.toString (this.bytes[0x3B]));
    map.put ("modulation_bpm_l", Integer.toString (this.bytes[0x3C]));
    map.put ("modulation_bpm",   Integer.toString ((this.bytes[0x3B] << 8) + this.bytes[0x3C]));
    map.put ("delay_bpm_h",      Integer.toString (this.bytes[0x3D]));
    map.put ("delay_bpm_l",      Integer.toString (this.bytes[0x3E]));
    map.put ("delay_bpm",        Integer.toString ((this.bytes[0x3D] << 8) + this.bytes[0x3E]));
    // DUMMY - UNUSED BY US
    map.put ("value_dummy_1_h", Integer.toString (this.bytes[0x3F])); 
    map.put ("value_dummy_1_l", Integer.toString (this.bytes[0x40])); 
    map.put ("value_dummy_1",   Integer.toString ((this.bytes[0x3F] << 8) + this.bytes[0x40]));
    map.put ("value_dummy_2_h", Integer.toString (this.bytes[0x41]));
    map.put ("value_dummy_2_l", Integer.toString (this.bytes[0x42]));
    map.put ("value_dummy_2",   Integer.toString ((this.bytes[0x41] << 8) + this.bytes[0x42]));
    map.put ("value_dummy_3_h", Integer.toString (this.bytes[0x43]));
    map.put ("value_dummy_3_l", Integer.toString (this.bytes[0x44]));
    map.put ("value_dummy_3",   Integer.toString ((this.bytes[0x43] << 8) + this.bytes[0x44]));
    map.put ("value_dummy_4_h", Integer.toString (this.bytes[0x45]));
    map.put ("value_dummy_4_l", Integer.toString (this.bytes[0x46]));
    map.put ("value_dummy_4",   Integer.toString ((this.bytes[0x45] << 8) + this.bytes[0x46]));
    // OTHERS - UNUSED BY US
    map.put ("currentPatchNo",     null); 
    map.put ("prevCurrentPatchNo", null); 
    //
    return map;
  }
  
  /** Returns a clone (copy) of this patch (data).
   * 
   * @return A clone (copy) of this patch (data).
   * 
   * @throws CloneNotSupportedException Never.
   * 
   */
  @Override
  public final Patch_Me80 clone () throws CloneNotSupportedException
  {
    return (Patch_Me80) super.clone ();
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // BYTES
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final byte[] bytes;
  
  /** Returns the (raw) bytes of this patch.
   * 
   * @return The (raw) bytes of this patch, non-{@code null} and of size {@link MidiDevice_Me80_Base#PATCH_SIZE}.
   * 
   */
  public final byte[] getBytes ()
  {
    return this.bytes.clone ();
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
    final byte[] nameBytes = new byte[16];
    System.arraycopy (this.bytes, 0, nameBytes, 0, 16);
    try
    {
      return new String (nameBytes, "US-ASCII").trim ();          
    }
    catch (UnsupportedEncodingException uee)
    {
      LOG.log (Level.WARNING, "Encoding Error for US-ASCII: {0}.", HexUtils.bytesToHex (nameBytes));
      return null;
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
}
