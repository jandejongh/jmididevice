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
  // MIDI MESSAGE FORMATTING [SYSEX - ALESIS - QVGT]
  //   - MIDI EDITING
  //   - MIDI DUMP REQUEST
  //   - MIDI DUMP REQUEST EDIT BUFFER
  //   - MIDI DUMP REQUEST ALL PROGRAMS
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private final static byte[] MIDI_EDITING_TEMPLATE = new byte[]
  {
    (byte) 0xF0,                           // System Exclusive Status
    (byte) 0x00, (byte) 0x00, (byte) 0x0E, // Alesis manufacturer id# (Vendor ID)
    (byte) 0x07,                           // Quadraverb-GT id#
    (byte) 0x01,                           // Opcode: 01 - MIDI Editing
    (byte) 0x00,                           // Function#
    (byte) 0x00,                           // Page#
    (byte) 0x00,                           // Encoded data (value) 1
    (byte) 0x00,                           // Encoded data (value) 2
    (byte) 0x00,                           // Encoded data (value) 3
    (byte) 0xF7                            // End-Of-Exclusive
  };

  /** Formats a MIDI-Edit request from given function, page and value for the Alesis Quadraverb GT.
   * 
   * <p>
   * THe MIDI-Edit request is a device-specific 12-byte MIDI System-Exclusive message;
   * it is, apart from MIDI Program and Control Change functions,
   * the only way to change a parameter value on the Alesis QV GT (source: Alesis Quadraverb GT Service Manual, Revision 1.00).
   * 
   * @param function The function, non-{@code null}.
   * @param page     The page, between 0 and 127 inclusive.
   * @param value    The value, between 0 and 65535.
   * 
   * @return The formatted MIDI message.
   * 
   * @throws IllegalArgumentException If the function is {@code null},
   *                                    the page is negative or strictly larger than 127,
   *                                    or the value is beyond the two-byte unsigned-integer encoding range
   *                                    (0 through 65535).
   * 
   */
  public static byte[] createMidiSysExMessage_QVGT_Editing
  (final ParameterDescriptor_QVGT.Function_QVGT function, final int page, final int value)
  {
    if (function == null || page < 0 || page > 127)
      throw new IllegalArgumentException ();
    if (value != (value & 0xffff))
      throw new IllegalArgumentException ();
    final byte[] rawMidiMessage = MidiUtils_QVGT.MIDI_EDITING_TEMPLATE.clone ();
    rawMidiMessage[6] = (byte) function.getFunctionNumber ();
    rawMidiMessage[7] = (byte) page;
    // Note that the Alesis QVGT sends LSB first so we correct it here...
    final byte[] mangledValue = new byte[2];
    mangledValue[0] = (byte) (value & 0x00ff);
    mangledValue[1] = (byte) ((value & 0xff00) >>> 8);
    final byte[] encodedValue = MidiUtils_QVGT.encodeToMidi (mangledValue, 3);
    System.arraycopy (encodedValue, 0, rawMidiMessage, 8, 3);
    // LOG.log (Level.INFO, "Function={0}, page={1}, value={2}: {3}", new Object[]{
    //   function,
    //   Integer.toHexString (page).toUpperCase (),
    //   "0x" + Integer.toHexString (value).toUpperCase (),
    //   HexUtils.bytesToHex (rawMidiMessage)
    // });
    return rawMidiMessage;
  }
  
  private final static byte[] MIDI_DUMP_REQUEST_TEMPLATE = new byte[]
  {
    (byte) 0xF0,                           // System Exclusive Status
    (byte) 0x00, (byte) 0x00, (byte) 0x0E, // Alesis manufacturer id# (Vendor ID)
    (byte) 0x07,                           // Quadraverb-GT id#
    (byte) 0x03,                           // Opcode: 03 - MIDI Dump Request
    (byte) 0x00,                           // Program#
    (byte) 0xF7                            // End-Of-Exclusive
  };

  /** Formats a request for program data from given program number for the Alesis Quadraverb GT.
   * 
   * <p>
   * Program numbers (patches) are numbered 0 through 99, inclusive.
   * The value 100 ({@link MidiDevice_QVGT#EDIT_BUFFER_PROGRAM_NUMBER})
   * is reserved for the Edit Buffer.
   * 
   * @param programNumber The program number, between 0 and 100, inclusive.
   * 
   * @return The formatted MIDI message.
   * 
   * @throws IllegalArgumentException If the program number is negative or strictly larger than 100.
   * 
   * @see MidiDevice_QVGT#EDIT_BUFFER_PROGRAM_NUMBER
   * 
   */
  public static byte[] createMidiSysExMessage_QVGT_DumpRequest (final int programNumber)
  {
    if (programNumber < 0 || programNumber > 100)
      throw new IllegalArgumentException ();
    final byte[] rawMidiMessage = MidiUtils_QVGT.MIDI_DUMP_REQUEST_TEMPLATE.clone ();
    rawMidiMessage[rawMidiMessage.length - 2] = (byte) programNumber;
    return rawMidiMessage;
  }
  
  /** Formats a request for the Edit Buffer program data for the Alesis Quadraverb GT.
   * 
   * <p>
   * Program numbers (patches) are numbered 0 through 99, inclusive.
   * The value 100 ({@link MidiDevice_QVGT#EDIT_BUFFER_PROGRAM_NUMBER})
   * is reserved for the Edit Buffer.
   * 
   * @return The formatted MIDI message.
   * 
   * @see #createMidiSysExMessage_QVGT_DumpRequest(int)
   * @see MidiDevice_QVGT#EDIT_BUFFER_PROGRAM_NUMBER
   * 
   */
  public static byte[] createMidiSysExMessage_QVGT_DumpRequest_EditBuffer ()
  {
    return MidiUtils_QVGT.createMidiSysExMessage_QVGT_DumpRequest (MidiDevice_QVGT.EDIT_BUFFER_PROGRAM_NUMBER);
  }

  /** Formats a request for <i>all</i> program data for the Alesis Quadraverb GT.
   * 
   * <p>
   * Sending this message to an Alesis Quadraverb GT will make it transmit its program data
   * for programs 0 through 99 (i.e., excluding the Edit Buffer).
   * 
   * @return The formatted MIDI message.
   * 
   */
  public static byte[] createMidiSysExMessage_QVGT_DumpRequest_AllPrograms ()
  {
    final byte[] rawMidiMessage = MidiUtils_QVGT.MIDI_DUMP_REQUEST_TEMPLATE.clone ();
    rawMidiMessage[rawMidiMessage.length - 2] = (byte) 101; // Any value < 100 will do.
    return rawMidiMessage;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MIDI MESSAGE ENCODING ON ALESIS QUADRAVERB GT [8 <-> 7 BITS IN A BYTE]
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /** Encodes into (part of a) MIDI message.
   * 
   * <p>
   * The Alesis Quadraverb often uses a compression technique into which the consecutive bits in the bytes of a message
   * are put into the 7 available bits in a MIDI message (because they have to be in data bytes, having most significant bit
   * zero).
   * 
   * <p>
   * This method encodes such a decoded message into, typically, a part of a MIDI message.
   * 
   * @param message   The decoded message, to be encoded into part of a MIDI message.
   * @param dstLength The length of the returned encoded message,
   *                     allowing some bits in (only) the last byte of the input message
   *                     to be discarded.
   * 
   * @return The encoded message.
   * 
   * @throws IllegalArgumentException If {@code message == null} or the {@code dstLength} argument has illegal value.
   * 
   */
  public static byte[] encodeToMidi (final byte[] message, final int dstLength)
  {
    if (message == null)
      throw new IllegalArgumentException ();
    final int nrOfBits = message.length * 8;
    final int dstLengthCalculated = (int) Math.ceil (((double) nrOfBits) / 7);
    if (((message.length * 8) % 7 == 0) && dstLength != dstLengthCalculated)
      throw new IllegalArgumentException ();
    if (((message.length * 8) % 7 != 0) && dstLength != dstLengthCalculated && dstLength != (dstLengthCalculated - 1))
      throw new IllegalArgumentException ();
    final byte[] encodedMessage = new byte[dstLength];
    int dstPosition = 0;
    int dstBitOffset = 1;
    for (final byte messageByte : message)
    {
      encodedMessage[dstPosition] |= (messageByte >>> dstBitOffset);
      if (dstPosition + 1 >= encodedMessage.length)
        break;
      encodedMessage[dstPosition + 1] = (byte) (0x7F & (messageByte << (7 - dstBitOffset)));
      dstPosition++;
      dstBitOffset++;
      if (dstBitOffset == 8)
      {
        dstPosition++;
        if (dstPosition >= encodedMessage.length)
          break;
        dstBitOffset = 1;
      }
    }
    return encodedMessage;    
  }
  
  /** Decodes a (part of a) MIDI message.
   * 
   * <p>
   * The Alesis Quadraverb often uses a compression technique into which the consecutive bits in the bytes of a message
   * are put into the 7 available bits in a MIDI message (because they have to be in data bytes, having most significant bit
   * zero).
   * 
   * <p>
   * This method decodes such an encoded MIDI message, or, typically, a part thereof.
   * 
   * @param rawMessage The raw (part of a) MIDI message, non-{@code null}.
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
    for (final byte rawMessageByte : rawMessage)
    {
      // LOG.log (Level.INFO, "rawIndex = {0}, dstPosition = {1}, dstBitOffset = {2}.",
      //   new Object[]{rawIndex, dstPosition, dstBitOffset});
      rawIndex++;
      if (rawMessageByte < 0)
        throw new IllegalArgumentException ();
      else if (dstBitOffset == 0)
      {
        // LOG.log (Level.INFO, "First");
        decodedMessage[dstPosition] = (byte) (rawMessageByte << 1);
        dstBitOffset = 7;
      }
      else if (dstBitOffset == 1)
      {
        // LOG.log (Level.INFO, "Second");
        decodedMessage[dstPosition] |= rawMessageByte;
        dstPosition++;
        dstBitOffset = 0;
      }
      else
      {
        // LOG.log (Level.INFO, "Third");
        decodedMessage[dstPosition] |= (rawMessageByte >>> (dstBitOffset - 1));
        if (dstPosition + 1 >= decodedMessage.length)
          break;
        decodedMessage[dstPosition + 1] = (byte) (rawMessageByte << (9 - dstBitOffset));
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
