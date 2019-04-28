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

import java.util.Objects;
import java.util.logging.Logger;

/** A patch memory slot on the Boss ME-80 ({@code U1.1} through {@code P9.4}, or the Manual patch).
 * 
 * <p>
 * A patch memory slot is an index into one of the ME-80 patch memories.
 * The patch memories are the {@code U1.1} through {@code U9.4}
 * and {@code P1.1} through {@code P9.4} slots,
 * <i>and</i> the patch slot corresponding to Manual Mode.
 * 
 * <p>
 * Beware that objects of this class merely <i>refer</i> to a memory location on the ME-80
 * capable of holding patch data, but that the actual patch data is <i>not</i> part
 * of the objects. (See {@link Patch_Me80}.)
 * 
 * <p>
 * Objects of this class are immutable and {@link Cloneable}.
 * 
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 */
public final class PatchSlot_Me80
  implements Cloneable
{
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // LOGGING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private static final Logger LOG = Logger.getLogger (PatchSlot_Me80.class.getName ());
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTORS / FACTORIES / CLONING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /** Constructs the patch memory slot from given bank and patch.
   * 
   * <p>
   * If both arguments are {@code null}, the Manual patch slot is constructed.
   * 
   * @param bank        The patch bank on the ME-80.
   * @param patchInBank The patch within the patch bank on the ME-80.
   * 
   * @throws IllegalArgumentException If one of the arguments is {@code null} while the other is not.
   * 
   * @see #MANUAL_SLOT
   * @see #manualSlot
   * 
   */
  public PatchSlot_Me80 (final ME80_BANK bank, final ME80_PATCH_IN_BANK patchInBank)
  {
    if ((bank == null) != (patchInBank == null))
      throw new IllegalArgumentException ();
    this.bank = bank;
    this.patchInBank = patchInBank;
  }
    
  @Override
  public final PatchSlot_Me80 clone ()
    throws CloneNotSupportedException
  {
    return (PatchSlot_Me80) super.clone ();
  }
      
  /** Constructs the patch memory slot from ME-80 encoding in a byte.
   * 
   * <p>
   * The ME-80 encodes its 72 patch slots as {@code 0x00} through
   * {@code 0x47} inclusive, and the Manual patch as {@code 0x48}.
   * 
   * @param patchSlotByte The byte.
   * 
   * @return The patch slot object created.
   * 
   * @throws IllegalArgumentException If the byte is illegal and does not correspond to a valid ME-80 patch slot.
   * 
   * @see #toByte
   * 
   */
  public static PatchSlot_Me80 fromByte (final byte patchSlotByte)
  {
    if (patchSlotByte < 0 || patchSlotByte > 0x48)
      throw new IllegalArgumentException ();
    if (patchSlotByte == 0x48)
      return new PatchSlot_Me80 (null, null);
    else
      return new PatchSlot_Me80 (ME80_BANK.values ()[patchSlotByte / 4], ME80_PATCH_IN_BANK.values ()[patchSlotByte % 4]);
  }

  /** Encodes this patch slot into a byte compliant with ME-80.
   * 
   * @return The encoded patch slot in a byte.
   * 
   * @see #toByte
   * 
   */
  public final byte toByte ()
  {
    if ((this.bank == null) != (this.patchInBank == null))
      throw new RuntimeException ();
    if (this.bank == null)
      return (byte) 0x48;
    else
      return (byte) (4 * this.bank.ordinal () + this.patchInBank.ordinal ());
  }
  
  /** Object representing the Manual patch slot.
   * 
   */
  public final static PatchSlot_Me80 MANUAL_SLOT = new PatchSlot_Me80 (null, null);
  
  /** Returns an object representing the Manual patch slot.
   * 
   * @return The Manual patch slot (representation).
   * 
   */
  public static final PatchSlot_Me80 manualSlot ()
  {
    return PatchSlot_Me80.MANUAL_SLOT;
  }
    
  /** Returns a version of this patch slot with another bank.
   * 
   * @param newBank The new bank; when {@code null}, the {@link #MANUAL_SLOT} is returned.
   * 
   * @return The patch slot with new bank (or {@link #MANUAL_SLOT}).
   *         If this patch slot is the Manual slot, the returned object has {@link ME80_PATCH_IN_BANK#PIB_1} as patch in the bank.
   * 
   * @see #withPatchInBank
   * @see #withBankAndPatch
   * 
   */
  public final PatchSlot_Me80 withBank (final ME80_BANK newBank)
  {
    if ((this.bank == null) != (this.patchInBank == null))
      throw new RuntimeException ();
    if (newBank == this.bank)
      return this;
    if (newBank == null)
      return PatchSlot_Me80.MANUAL_SLOT;
    else
      return new PatchSlot_Me80 (newBank, this.patchInBank != null ? this.patchInBank : ME80_PATCH_IN_BANK.PIB_1);
  }
    
  /** Returns a version of this patch slot with another patch in the bank.
   * 
   * @param newPatchInBank The new patch in the bank; when {@code null}, the {@link #MANUAL_SLOT} is returned.
   * 
   * @return The patch slot with new patch in the bank (or {@link #MANUAL_SLOT}).
   *         If this patch slot is the Manual slot, the returned object has {@link ME80_BANK#U1} as bank.
   * 
   * @see #withBank
   * @see #withBankAndPatch
   * 
   */
  public final PatchSlot_Me80 withPatchInBank (final ME80_PATCH_IN_BANK newPatchInBank)
  {
    if ((this.bank == null) != (this.patchInBank == null))
      throw new RuntimeException ();
    if (newPatchInBank == this.patchInBank)
      return this;
    if (newPatchInBank == null)
      return new PatchSlot_Me80 (null, null);
    else
      return new PatchSlot_Me80 (this.bank != null ? this.bank : ME80_BANK.U1, newPatchInBank);
  }
  
  /** Returns a version of this patch slot with another bank and patch in the bank.
   * 
   * @param newBank        The new bank; when {@code null}, the {@link #MANUAL_SLOT} is returned.
   * @param newPatchInBank The new patch in the bank; when {@code null}, the {@link #MANUAL_SLOT} is returned.
   * 
   * @return The patch slot with new patch in the bank (or {@link #MANUAL_SLOT}).
   *         If this patch slot is the Manual slot, the returned object has {@link ME80_BANK#U1} as bank.
   * 
   * @throws IllegalArgumentException If one of the arguments is {@code null} while the other is not.
   * 
   * @see #withBank
   * @see #withPatchInBank
   * @see PatchSlot_Me80#PatchSlot_Me80
   * 
   */
  public final PatchSlot_Me80 withBankAndPatch (final ME80_BANK newBank, final ME80_PATCH_IN_BANK newPatchInBank)
  {
    if ((this.bank == null) != (this.patchInBank == null))
      throw new RuntimeException ();
    if (this.bank != newBank || this.patchInBank != newPatchInBank)
      return new PatchSlot_Me80 (newBank, newPatchInBank);
    else
      return this;
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // BANK
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  /** A (Memory) patch bank {@code U1} through {@code P9} on the Boss ME-80.
   * 
   */
  public enum ME80_BANK
  {
    
    U1 (true),
    U2 (true),
    U3 (true),
    U4 (true),
    U5 (true),
    U6 (true),
    U7 (true),
    U8 (true),
    U9 (true),
    P1 (false),
    P2 (false),
    P3 (false),
    P4 (false),
    P5 (false),
    P6 (false),
    P7 (false),
    P8 (false),
    P9 (false);
    
    private ME80_BANK (final boolean isUserBank)
    {
      this.isUserBank = isUserBank;
    }
    
    private final boolean isUserBank;
    
    /** Returns whether or not this is a user-bank ({@code U1} through {@code U9}).
     * 
     * @return Whether or not this is a user-bank.
     * 
     */
    public final boolean isUserBank ()
    {
      return this.isUserBank;
    }
    
  }

  private final ME80_BANK bank;
    
  /** Returns the patch bank.
   * 
   * @return The patch bank; if {@code null}, this object refers to the Manual patch slot
   *                                          (and the patch within the bank is also {@code null}).
   * 
   * @see #getPatchInBank
   * 
   */
  public final ME80_BANK getBank ()
  {
    return this.bank;
  }
    
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PATCH IN BANK
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /** A patch within a bank on the ME-80.
   * 
   * On the ME-80, each patch bank contains four patches.
   * 
   */
  public enum ME80_PATCH_IN_BANK
  {
    
    PIB_1 ("1"),
    PIB_2 ("2"),
    PIB_3 ("3"),
    PIB_4 ("4");

    private final String string;
    
    private ME80_PATCH_IN_BANK (final String string)
    {
      this.string = string;
    }

    @Override
    public String toString ()
    {
      return this.string;
    }
    
  }
  
  private final ME80_PATCH_IN_BANK patchInBank;
    
  /** Returns the patch number within the selected bank.
   * 
   * @return The patch within the bank; if {@code null}, this object refers to the Manual patch slot
   *                                                     (and the the bank is also {@code null}).
   * 
   * @see #getBank
   * 
   */
  public final ME80_PATCH_IN_BANK getPatchInBank ()
  {
    return this.patchInBank;
  }
    
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // INQUIRIES
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /** Returns whether this is the Manual patch slot.
   * 
   * @return The value {@code true} if this is the Manual patch slot.
   * 
   */
  public final boolean isManualPatchSlot ()
  {
    return this.bank == null && this.patchInBank == null;
  }
    
  /** Returns whether this is a "user" patch that can be modified.
   * 
   * <p>
   * The {@code U1.1} through {@code U9.4} patch slots can be modified,
   * as well as the Manual patch.
   * 
   * @return Whether this is a user patch that can be modified.
   * 
   */
  public final boolean isUserMemoryPatch ()
  {
    return isManualPatchSlot () || this.bank.isUserBank ();
  }
    
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // EQUALS
  // HASH CODE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  @Override
  public final int hashCode ()
  {
    int hash = 3;
    hash = 17 * hash + Objects.hashCode (this.bank);
    hash = 17 * hash + Objects.hashCode (this.patchInBank);
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
    final PatchSlot_Me80 other = (PatchSlot_Me80) obj;
    if (this.bank != other.bank)
      return false;
    return this.patchInBank == other.patchInBank;
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
}
