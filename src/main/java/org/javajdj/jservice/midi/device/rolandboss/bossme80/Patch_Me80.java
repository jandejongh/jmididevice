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
