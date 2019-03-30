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

import java.util.logging.Logger;

/** MIDI utility class specific to the Alesis Quadraverb GT.
 *
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 */
public class MidiUtils_QVGT
{

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // LOGGING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private static final Logger LOG = Logger.getLogger (MidiUtils_QVGT.class.getName ());
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MIDI MESSAGE ENCODING ON ALESIS QUADRAVERB GT [8 <-> 7 BITS IN A BYTE]
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /** Decodes a MIDI message (part).
   * 
   * <p>
   * The Alesis Quadraverb often uses a compression technique into which the consecutive bits in the bytes of a message
   * are put into the 7 available bits in a MIDI message (because they have to be in data bytes, having most significant bit
   * zero).
   * 
   * <p>
   * This method decodes such an encoded MIDI message, or, typically, a part thereof.
   * 
   * @param rawMessage The raw MIDI message (part), non-{@code null}.
   * @param dstLength  The length of the returned decoded message,
   *                     allowing some bits in (only) the last byte of the raw MIDI message
   *                     to be discarded.
   * 
   * @return The decoded message.
   * 
   * @throws IllegalArgumentException If {@code rawMessage == null} or the {@code dstLength} argument has illegal value.
   * 
   */
  public static byte[] decodeFromMidi (final byte[] rawMessage, final int dstLength)
  {
    if (rawMessage == null)
      throw new IllegalArgumentException ();
    final int nrOfBits = rawMessage.length * 7;
    final int dstLengthCalculated = (int) Math.ceil (((double) nrOfBits) / 8);
    if (((rawMessage.length * 7) % 8 == 0) && dstLength != dstLengthCalculated)
      throw new IllegalArgumentException ();
    if (((rawMessage.length * 7) % 8 != 0) && dstLength != dstLengthCalculated && dstLength != (dstLengthCalculated - 1))
      throw new IllegalArgumentException ();
    final byte[] decodedMessage = new byte[dstLength];
    // LOG.log (Level.INFO, "rawMessage.length = {0}, decodedMessage.length = {1}.",
    //  new Object[]{rawMessage.length, decodedMessage.length});
    int dstPosition = 0;
    int dstBitOffset = 0;
    // LOG.log (Level.INFO, "Raw Message: {0}", HexUtils.bytesToHex (rawMessage));
    int rawIndex = 0;
    for (final byte rawMessagebyte : rawMessage)
    {
      // LOG.log (Level.INFO, "rawIndex = {0}, dstPosition = {1}, dstBitOffset = {2}.",
      //   new Object[]{rawIndex, dstPosition, dstBitOffset});
      rawIndex++;
      if (rawMessagebyte < 0)
        throw new IllegalArgumentException ();
      else if (dstBitOffset == 0)
      {
        // LOG.log (Level.INFO, "First");
        decodedMessage[dstPosition] = (byte) (rawMessagebyte << 1);
        dstBitOffset = 7;
      }
      else if (dstBitOffset == 1)
      {
        // LOG.log (Level.INFO, "Second");
        decodedMessage[dstPosition] |= rawMessagebyte;
        dstPosition++;
        dstBitOffset = 0;
      }
      else
      {
        // LOG.log (Level.INFO, "Third");
        decodedMessage[dstPosition] |= (rawMessagebyte >>> (dstBitOffset - 1));
        if (dstPosition + 1 >= decodedMessage.length)
          break;
        decodedMessage[dstPosition + 1] = (byte) (rawMessagebyte << (9 - dstBitOffset));
        dstPosition++;
        dstBitOffset = (dstBitOffset + 7) % 8;
      }
    }
    return decodedMessage;
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
}
