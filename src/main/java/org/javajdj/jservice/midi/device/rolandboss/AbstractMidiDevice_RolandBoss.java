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

import org.javajdj.jservice.midi.device.AbstractMidiDevice;
import org.javajdj.jservice.midi.device.MidiDevice;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.javajdj.util.hex.HexUtils;
import org.javajdj.jservice.midi.MidiService;
import org.javajdj.util.hex.ByteUtils;

/** Partial implementation of {@link MidiDevice} for Roland-Boss devices.
 * 
 * @param <D> The type used to describe parameters.
 * 
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 */
public abstract class AbstractMidiDevice_RolandBoss<D extends ParameterDescriptor_RolandBoss>
  extends AbstractMidiDevice<D>
  implements MidiDevice
{
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // LOGGING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private static final Logger LOG = Logger.getLogger (AbstractMidiDevice_RolandBoss.class.getName ());
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTORS / FACTORIES / CLONING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  /** Creates a partial {@link MidiDevice} for a specific (sub-class defined) Roland-Boss device.
   * 
   * @param midiService The {@link MidiService} to use.
   * 
   */
  public AbstractMidiDevice_RolandBoss (final MidiService midiService)
  {
    super (midiService);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MIDI DEVICE PARAMETER MAP
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private final Set<ParameterDescriptor_RolandBoss> patchParameters = new LinkedHashSet<> ();
  
  private final Map<Byte, Set<ParameterDescriptor_RolandBoss>> controllers = new TreeMap<> ();
  
  private final Map<Long, Set<ParameterDescriptor_RolandBoss>> addresses = new TreeMap<> ();
  
  @Override
  protected void registerParameter (final D parameterDescriptor)
  {
    super.registerParameter (parameterDescriptor);
    switch (parameterDescriptor.getParameterType_RolandBoss ())
    {
      case MidiProgramChange:
      {
        if (this.patchParameters.contains (parameterDescriptor))
          throw new IllegalArgumentException ();
        this.patchParameters.add (parameterDescriptor);
        break;
      }
      case MidiControlChange:
      {
        final byte controller = parameterDescriptor.getController ();
        if (! this.controllers.containsKey (controller))
          this.controllers.put (controller, new LinkedHashSet<> ());
        if (this.controllers.get (controller).contains (parameterDescriptor)) // XXX Weird test: Should test on values of p-d map?
          throw new IllegalArgumentException ();
        this.controllers.get (controller).add (parameterDescriptor);
        break;
      }
      case MidiSysExRolandBoss_RQ1_DT1:
      {
        final Long addressAsLong = ByteUtils.bytes4ToLong (parameterDescriptor.getAddressAsBytes (), 0);
        if (! this.addresses.containsKey (addressAsLong))
          this.addresses.put (addressAsLong, new LinkedHashSet<> ());
        final Set<ParameterDescriptor_RolandBoss> addressDescriptors = this.addresses.get (addressAsLong);
        if (addressDescriptors.contains (parameterDescriptor))
          throw new IllegalArgumentException ();
        addressDescriptors.add (parameterDescriptor);
        break;
      }
      default:
      {
        throw new RuntimeException ();
      }
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MIDI SERVICE [AbstractMidiDevice]
  // TX HANDLING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  protected void sendMidiSysExMessage_RolandBoss_RQ1 (final String key)
  {
    if (key == null || ! keySet ().contains (key))
      throw new IllegalArgumentException ();
    final ParameterDescriptor_RolandBoss parameterDescriptor_RolandBoss = getParameterDescriptor (key);
    if (parameterDescriptor_RolandBoss == null)
      throw new RuntimeException ();
    if (parameterDescriptor_RolandBoss.getParameterType_RolandBoss ()
      != ParameterDescriptor_RolandBoss.ParameterType_RolandBoss.MidiSysExRolandBoss_RQ1_DT1)
      throw new IllegalArgumentException ();
    final byte[] address = parameterDescriptor_RolandBoss.getAddressAsBytes ();
    final byte[] size = parameterDescriptor_RolandBoss.getLengthAsBytes ();
    if (address == null || size == null)
      throw new RuntimeException ();
    synchronized (this)
    {
      if (getStatus () != Status.STOPPED && getMidiService () != null)
      {
        final byte[] midiMessage = MidiUtils_RolandBoss.createMidiSysExMessage_RolandBoss_RQ1 (address, size);
        getMidiService ().sendRawMidiMessage (midiMessage);
      }
    }
  }
  
  protected void sendMidiSysExMessage_RolandBoss_DT1 (final String key, final byte[] value)
  {
    if (key == null || ! keySet ().contains (key))
      throw new IllegalArgumentException ();
    final ParameterDescriptor_RolandBoss parameterDescriptor_RolandBoss = getParameterDescriptor (key);
    if (parameterDescriptor_RolandBoss == null)
      throw new RuntimeException ();
    if (parameterDescriptor_RolandBoss.getParameterType_RolandBoss ()
      != ParameterDescriptor_RolandBoss.ParameterType_RolandBoss.MidiSysExRolandBoss_RQ1_DT1)
      throw new IllegalArgumentException ();
    final byte[] address = parameterDescriptor_RolandBoss.getAddressAsBytes ();
    synchronized (this)
    {
      if (getStatus () != Status.STOPPED && getMidiService () != null)
      {
        final byte[] midiMessage = MidiUtils_RolandBoss.createMidiSysExMessage_RolandBoss_DT1 (address, value);
        getMidiService ().sendRawMidiMessage (midiMessage);
      }
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MIDI SERVICE [AbstractMidiDevice]
  // RX HANDLING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  @Override  
  protected void onMidiRxNoteOff (final int midiChannel, final int note, final int velocity)
  {
    super.onMidiRxNoteOff (midiChannel, note, velocity);
  }

  @Override
  protected void onMidiRxNoteOn (final int midiChannel, final int note, final int velocity)
  {
    super.onMidiRxNoteOn (midiChannel, note, velocity);
  }

  @Override
  protected void onMidiRxProgramChange (final int midiChannel, final int patch)
  {
    super.onMidiRxProgramChange (midiChannel, patch);
    for (final ParameterDescriptor_RolandBoss parameterDescriptor_RolandBoss : this.patchParameters)
      // XXX NOT GOOD... THIS BYPASSES ANY VALUE CONVERSION AND ASSUMES THE VALUE IS ALWAYS A SINGLE BYTE... XXX
      // SUGGESTION: AbstractMidiDevice_RolandBoss.this.onParameterReadFromDevice
      //  (parameterDescriptor_RolandBoss.getParameterName (), new byte[]{(byte) patch});
      super.updateParameterFromDevice (parameterDescriptor_RolandBoss.getParameterName (), Byte.valueOf ((byte) patch));
  }

  @Override
  protected void onMidiRxControlChange (final int midiChannel, final int controller, final int value)
  {
    super.onMidiRxControlChange (midiChannel, controller, value);
    if (controller < 0 || controller > 127)
      throw new RuntimeException ();
    if (value < 0 || value > 127)
      throw new RuntimeException ();
    final Set<ParameterDescriptor_RolandBoss> parameters = this.controllers.get ((byte) controller);
    if (parameters != null)
      for (final ParameterDescriptor_RolandBoss parameter : parameters)
        onParameterReadFromDevice (parameter.getParameterName (), new byte[]{(byte) value});        
  }
  
  @Override
  protected void onMidiRxSysEx (final byte vendorId, final byte[] rawMidiMessage)
  {
    super.onMidiRxSysEx (vendorId, rawMidiMessage);
    // LOG.log (Level.INFO, "onMidiRxSysEx, message={0}", HexUtils.bytesToHex (rawMidiMessage));
    if (rawMidiMessage.length == 15
      && (rawMidiMessage[0] & 0xFF) == 0xF0
      && (rawMidiMessage[1] & 0xFF) == 0x7E
      // rawMidiMessage[2] == deviceId
      && (rawMidiMessage[3] & 0xFF) == 0x06 // General Information (sub ID #1)
      && (rawMidiMessage[4] & 0xFF) == 0x02 // Identity Request (sub ID #2)
      && (rawMidiMessage[5] & 0xFF) == 0x41 // Manufacturer ID (Roland)
      && (rawMidiMessage[14] & 0xFF) == 0xF7)
    {
      final byte deviceId = rawMidiMessage[2];
      final byte[] deviceFamilyCode = new byte[]{ rawMidiMessage[6], rawMidiMessage[7] };
      final byte[] deviceFamilyNumber = new byte[]{ rawMidiMessage[8], rawMidiMessage[9], rawMidiMessage[10], rawMidiMessage[11] };
      final byte[] softwareRevisionLevel = new byte[]{ rawMidiMessage[12], rawMidiMessage[13] };
      onIdReply (deviceId, deviceFamilyCode, deviceFamilyNumber, softwareRevisionLevel);
    }
    else if (rawMidiMessage.length > 14
      && (rawMidiMessage[0] & 0xFF) == 0xF0
      && (rawMidiMessage[1] & 0xFF) == 0x41
      && (rawMidiMessage[2] & 0xFF) == 0x10 // Roland; 3-6: Model
      && (rawMidiMessage[7] & 0xFF) == 0x12 // DT1
      && (rawMidiMessage[rawMidiMessage.length - 1] & 0xFF) == 0xF7)
    {
      final long address = ByteUtils.bytes4ToLong (rawMidiMessage, 8);
      final int length = rawMidiMessage.length - 14;
      //LOG.log (Level.INFO, "Found address: {0}.", address);
      if (! this.addresses.containsKey (address))
      {
        LOG.log (Level.WARNING, "Dropped SysEx DT1 message with unknown address: {0}.", HexUtils.bytesToHex (rawMidiMessage));
        return;
      }
      for (final ParameterDescriptor_RolandBoss pd : this.addresses.get (address))
      {
        if (length == pd.getLength ())
        {
          final byte[] data = new byte[length];
          System.arraycopy (rawMidiMessage, 12, data, 0, length);
          // LOG.log (Level.INFO, "onMidiRxSysEx, key={0}", pd.getParameterName ());
          AbstractMidiDevice_RolandBoss.this.onParameterReadFromDevice (pd.getParameterName (), data);
        }
      }
    }
    else
      LOG.log (Level.WARNING, "Dropped unknown SysEx message: {0}.", HexUtils.bytesToHex (rawMidiMessage));
  }
  
  protected void onIdReply (final byte deviceId,
                            final byte[] deviceFamilyCode,
                            final byte[] deviceFamilyNumber,
                            final byte[] softwareRevisionLevel)
  {
//    LOG.log (Level.INFO, "deviceId = {0}, deviceFamilyCode = {1}, deviceFamilyNumber = {2}, softwareRevisionLevel = {3}.",
//      new Object[]{ HexUtils.bytesToHex (new byte[]{deviceId}),
//                    HexUtils.bytesToHex (deviceFamilyCode),
//                    HexUtils.bytesToHex (deviceFamilyNumber),
//                    HexUtils.bytesToHex (softwareRevisionLevel)});
// XXX TODO MUST BE DONE DIFFERENTLY    
    // fireMidiDeviceSysExIdReply (deviceId, deviceFamilyCode, deviceFamilyNumber, softwareRevisionLevel);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MIDI SERVICE [AbstractMidiDevice]
  // RX HANDLING
  //
  // PARAMETER READ FROM DEVICE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  protected void onParameterReadFromDevice (final String key, final byte[] value)
  {
    
    if (key == null || ! keySet ().contains (key))
      throw new IllegalArgumentException ();
    final ParameterDescriptor_RolandBoss parameterDescriptor_RolandBoss = getParameterDescriptor (key);
    if (parameterDescriptor_RolandBoss == null)
      throw new IllegalArgumentException ();
    if (value == null || value.length != parameterDescriptor_RolandBoss.getLength ())
      throw new IllegalArgumentException ();
    
    final Object oValue = parameterDescriptor_RolandBoss.convertFromDevice (value);
    if (oValue == null)
      throw new RuntimeException ();
    
    updateParameterFromDevice (key, oValue);
    
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // Map.put IMPLEMENTATION
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  @Override
  protected Object putImpl (final String key, final Object value)
  {
    if (key == null || ! keySet ().contains (key))
      throw new IllegalArgumentException ();
    final ParameterDescriptor_RolandBoss parameterDescriptor_RolandBoss = getParameterDescriptor (key);
    if (parameterDescriptor_RolandBoss == null)
      throw new RuntimeException ();
    synchronized (this)
    {
      final Object oldValue = super.get (key);
      if (getStatus () != Status.STOPPED && getMidiService () != null)
      {
        switch (parameterDescriptor_RolandBoss.getParameterType_RolandBoss ())
        {
          case MidiProgramChange:
          {
            throw new UnsupportedOperationException ();
            // break;
          }
          case MidiControlChange:
          {
            final byte[] convertedValue = parameterDescriptor_RolandBoss.convertToDevice (value);
            if (convertedValue == null)
              throw new IllegalArgumentException ();
            AbstractMidiDevice_RolandBoss.this.sendMidiControlChange
              (getMidiChannel (), (byte) parameterDescriptor_RolandBoss.getController (), (int) convertedValue[0]);
            return oldValue;
          }
          case MidiSysExRolandBoss_RQ1_DT1:
          {
            final byte[] convertedValue = parameterDescriptor_RolandBoss.convertToDevice (value);
            if (convertedValue == null)
              throw new IllegalArgumentException ();
            AbstractMidiDevice_RolandBoss.this.sendMidiSysExMessage_RolandBoss_DT1 (key, convertedValue);
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
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
}
