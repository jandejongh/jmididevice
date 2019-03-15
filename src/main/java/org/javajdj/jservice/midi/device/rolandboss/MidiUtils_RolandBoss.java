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
package org.javajdj.jservice.midi.device.rolandboss;

/** MIDI utility class specific to Roland (and Boss) devices.
 *
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 */
public class MidiUtils_RolandBoss
{
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MIDI MESSAGE FORMATTING [SYSEX - ROLAND-BOSS - RQ1]
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private final static byte[] MIDI_DATA_REQ_RQ1_TEMPLATE = new byte[]
  {
    (byte) 0xF0,
    (byte) 0x41,
    (byte) 0x10,
    (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, // Model
    (byte) 0x11, // command ID (RQ1)
    (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, // Address
    (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, // Size
    (byte) 0x00, // Checksum
    (byte) 0xF7
  };
    
  public static byte[] createMidiSysExMessage_RolandBoss_RQ1 (final byte[] address, final byte[] size)
  {
    if (address == null || address.length != 4 || size == null || size.length != 4) /* XXX 4 FOR NOW!! */
      throw new IllegalArgumentException ();
    final byte[] rawMidiMessage = MIDI_DATA_REQ_RQ1_TEMPLATE.clone ();
    System.arraycopy (address, 0, rawMidiMessage, 8, 4);
    System.arraycopy (size, 0, rawMidiMessage, 12, 4);
    rawMidiMessage[rawMidiMessage.length - 2] = MidiUtils_RolandBoss.rolandChecksum (rawMidiMessage);
    return rawMidiMessage;
  }

  public static byte[] createMidiSysExMessage_RolandBoss_DT1 (final byte[] address, final byte[] value)
  {
    if (address == null || address.length != 4 || value == null)
      throw new IllegalArgumentException ();
    final byte[] rawMidiMessage = new byte[14 + value.length];
    rawMidiMessage[0] = (byte) 0xF0; // System Exclusive
    rawMidiMessage[1] = (byte) 0x41; // Roland
    rawMidiMessage[2] = (byte) 0x10; // Device -> Is this relevant, and is 0x10 always a proper value (should be argument)?
    rawMidiMessage[3] = (byte) 0x00; // Model  -> I.D.
    rawMidiMessage[4] = (byte) 0x00;
    rawMidiMessage[5] = (byte) 0x00;
    rawMidiMessage[6] = (byte) 0x01;
    rawMidiMessage[7] = (byte) 0x12; // command ID (DT1)
    System.arraycopy (address, 0, rawMidiMessage, 8, 4);
    System.arraycopy (value, 0, rawMidiMessage, 12, value.length);
    rawMidiMessage[rawMidiMessage.length - 2] = MidiUtils_RolandBoss.rolandChecksum (address, value);
    rawMidiMessage[rawMidiMessage.length - 1] = (byte) 0xF7; // EOX
    return rawMidiMessage;
  }
  
//  public final synchronized void sendMidiDataReq_RQ1 ()
//  {
//    // Return immediately if we are not active.
//    if (getStatus () != Status.ACTIVE)
//      return;
//    final byte[] rawMidiMessage = MIDI_DATA_REQ_RQ1.clone ();
//    rawMidiMessage[rawMidiMessage.length - 2] = MidiUtils_RolandBoss.rolandChecksum (rawMidiMessage);
//    getMidiService ().sendRawMidiMessage (rawMidiMessage);
//  }
  
//  public final synchronized void sendMidiDataReq_RQ1 (final byte[] address, final byte[] size)
//  {
//    // Return immediately if we are not active.
//    if (getStatus () != Status.ACTIVE)
//      return;
//    if (address == null || address.length != 4 || size == null || size.length != 4)
//      throw new IllegalArgumentException ();
//    final byte[] rawMidiMessage = MIDI_DATA_REQ_RQ1.clone ();
//    System.arraycopy (address, 0, rawMidiMessage, 8, 4);
//    System.arraycopy (size, 0, rawMidiMessage, 12, 4);
//    rawMidiMessage[rawMidiMessage.length - 2] = MidiUtils_RolandBoss.rolandChecksum (rawMidiMessage);
//    getMidiService ().sendRawMidiMessage (rawMidiMessage);
//  }

//  public final synchronized void sendMidiDataSet_DT1 (final byte[] address, final byte[] value)
//  {
//    // Return immediately if we are not active.
//    if (getStatus () != Status.ACTIVE)
//      return;
//    if (address == null || address.length != 4 || value == null)
//      throw new IllegalArgumentException ();
//    final byte[] rawMidiMessage = new byte[14 + value.length];
//    rawMidiMessage[0] = (byte) 0xF0; // System Exclusive
//    rawMidiMessage[1] = (byte) 0x41; // Roland
//    rawMidiMessage[2] = (byte) 0x10; // Device
//    rawMidiMessage[3] = (byte) 0x00; // Model
//    rawMidiMessage[4] = (byte) 0x00;
//    rawMidiMessage[5] = (byte) 0x00;
//    rawMidiMessage[6] = (byte) 0x01;
//    rawMidiMessage[7] = (byte) 0x12; // command ID (DT1)
//    System.arraycopy (address, 0, rawMidiMessage, 8, 4);
//    System.arraycopy (value, 0, rawMidiMessage, 12, value.length);
//    rawMidiMessage[rawMidiMessage.length - 2] = MidiUtils_RolandBoss.rolandChecksum (address, value);
//    rawMidiMessage[rawMidiMessage.length - 1] = (byte) 0xF7; // EOX
//    getMidiService ().sendRawMidiMessage (rawMidiMessage);
//  }
//
//  public final synchronized void sendMidiDataReq_RQ1 (final String name)
//  {
//    if (containsKey (name))
//    {
//      // LOG.log (Level.INFO, "Requesting (RQ1): {0}.", name);
//      final ParameterDescriptor_RolandBoss pd = getParameterDescriptor (name);
//      sendMidiDataReq_RQ1 (pd.getAddressAsBytes (), pd.getLengthAsBytes ());
//    }
//    else
//      LOG.log (Level.WARNING, "Unknown parameter name {0}.", name);
//  }
//  
//  public final synchronized void sendMidiDataSet_DT1 (final String name, final byte[] value)
//  {
//    if (containsKey (name))
//    {
//      final ParameterDescriptor_RolandBoss pd = getParameterDescriptor (name);
//      sendMidiDataSet_DT1 (pd.getAddressAsBytes (), value);
//    }
//    else
//      LOG.log (Level.WARNING, "Unknown parameter name {0}.", name);
//  }
//  
    
  public final static byte rolandChecksum (final byte[] rawMessage)
  {
    // Add all address and data (size) bytes.
    int sum = 0;
    for (int i = 8; i < 16; i++)
      sum += rawMessage[i];
    // LOG.log (Level.INFO, "Sum = {0}.", sum);
    // Take remainder from division by 128.
    final int remainder = sum % 128;
    // Substract remainder from 128. but turn 128 result into 0.
    return (remainder == 0) ? ((byte) 0) : (byte) (128 - remainder);
  }
  
  public final static byte rolandChecksum (final byte[] address, final byte[] value)
  {
    // Add all address and data (size) bytes.
    int sum = 0;
    for (int i = 0; i < address.length; i++)
      sum += address[i];
    for (int i = 0; i < value.length; i++)
      sum += value[i];
    // LOG.log (Level.INFO, "Sum = {0}.", sum);
    // Take remainder from division by 128.
    final int remainder = sum % 128;
    // Substract remainder from 128. but turn 128 result into 0.
    return (remainder == 0) ? ((byte) 0) : (byte) (128 - remainder);
  }

}
