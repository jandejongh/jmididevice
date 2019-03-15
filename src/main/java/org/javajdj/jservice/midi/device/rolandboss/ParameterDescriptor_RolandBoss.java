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

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.javajdj.jservice.midi.device.MidiDevice;
import org.javajdj.jservice.midi.device.ParameterDescriptor;
import org.javajdj.util.hex.HexUtils;

/** Description of a parameter (key, value pair) in a {@link MidiDevice},
 *  in particular for Roland-Boss devices.
 *
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 */
public class ParameterDescriptor_RolandBoss<E>
  implements ParameterDescriptor
{
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // LOGGING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private static final Logger LOG = Logger.getLogger (ParameterDescriptor_RolandBoss.class.getName ());
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTORS / FACTORIES / CLONING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  protected ParameterDescriptor_RolandBoss
  ( final ParameterType_RolandBoss parameterType_RolandBoss,
    final String parameterName,
    final Class<E> parameterValueClass,
    final ParameterConversion_RolandBoss parameterConversion_RolandBoss,
    final byte controller,
    final byte[] address,
    final byte[] length,
    final String parentKey,
    final CustomValueConverter<E> customValueConverter)
  {
    if (parameterType_RolandBoss == null)
      throw new IllegalArgumentException ();
    this.parameterType_RolandBoss = parameterType_RolandBoss;
    if (parameterName == null || parameterName.trim ().length () == 0)
      throw new IllegalArgumentException ();
    this.parameterName = parameterName;
    this.parameterValueClass = parameterValueClass;
    this.parameterConversion_RolandBoss =
      (parameterConversion_RolandBoss != null ? parameterConversion_RolandBoss : ParameterConversion_RolandBoss.NONE);
    switch (this.parameterType_RolandBoss)
    {
      case MidiProgramChange:
      {
        this.controller = (byte) -1;
        this.address = null;
        this.length = null;
        this.parentKey = null;
        break;
      }
      case MidiControlChange:
      {
        if (controller < 0)
          throw new IllegalArgumentException ();
        this.controller = controller;
        this.address = null;
        this.length = null;
        this.parentKey = null;
        break;
      }
      case MidiSysExRolandBoss_RQ1_DT1:
      {
        this.controller = (byte) -1;
        if (address == null || address.length != 4 /* XXX FOR NOW XXX */)
          throw new IllegalArgumentException ();
        this.address = address;
        if (length == null || length.length != 4 /* XXX FOR NOW XXX */)
          throw new IllegalArgumentException ();
        this.length = length;
        this.parentKey = parentKey;
        break;
      }
      default:
      {
        throw new RuntimeException ();
      }
    }
    if (parameterConversion_RolandBoss == ParameterConversion_RolandBoss.CUSTOM && customValueConverter == null)
      throw new IllegalArgumentException ();
    this.customValueConverter = customValueConverter;
  }
    
  public ParameterDescriptor_RolandBoss
  ( final String parameterName,
    final Class<E> parameterValueClass,
    final ParameterConversion_RolandBoss parameterConversion_RolandBoss,
    final byte[] address,
    final byte[] length,
    final String parentKey)
  {
    this (ParameterType_RolandBoss.MidiSysExRolandBoss_RQ1_DT1,
          parameterName,
          parameterValueClass,
          parameterConversion_RolandBoss,
          (byte) -1,
          address,
          length,
          parentKey,
          null);
  }
    
  public ParameterDescriptor_RolandBoss
  ( final String parameterName,
    final Class<E> parameterValueClass,
    final byte[] address,
    final byte[] length,
    final String parentKey)
  {
    this (ParameterType_RolandBoss.MidiSysExRolandBoss_RQ1_DT1,
          parameterName,
          parameterValueClass,
          null,
          (byte) -1,
          address,
          length,
          parentKey,
          null);
  }
  
  public ParameterDescriptor_RolandBoss
  ( final String parameterName,
    final Class<E> parameterValueClass,
    final byte[] address,
    final byte[] length,
    final String parentKey,
    final CustomValueConverter<E> customValueConverter)
  {
    this (ParameterType_RolandBoss.MidiSysExRolandBoss_RQ1_DT1,
          parameterName,
          parameterValueClass,
          ParameterConversion_RolandBoss.CUSTOM,
          (byte) -1,
          address,
          length,
          parentKey,
          customValueConverter);
  }
  
  public ParameterDescriptor_RolandBoss
  ( final String parameterName,
    final byte controller)
  {
    this (ParameterType_RolandBoss.MidiControlChange,
      parameterName,
      (Class<E>) Integer.class,
      ParameterConversion_RolandBoss.INT_IN_BYTE,
      controller,
      null,
      null,
      null,
      null);
  }
    
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PARAMETER TYPE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
  public enum ParameterType_RolandBoss
  {
    MidiProgramChange,
    MidiControlChange,
    MidiSysExRolandBoss_RQ1_DT1;
  }
  
  private final ParameterType_RolandBoss parameterType_RolandBoss;
  
  public final ParameterType_RolandBoss getParameterType_RolandBoss ()
  {
    return this.parameterType_RolandBoss;
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PARAMETER NAME
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
  private final String parameterName;
    
  @Override
  public final String getParameterName ()
  {
    return this.parameterName;
  }
    
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PARAMETER VALUE CLASS
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
  private final Class<E> parameterValueClass;
  
  public final Class<E> getParameterValueClass ()
  {
    return this.parameterValueClass;
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // TYPE CONVERSION
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public enum ParameterConversion_RolandBoss
  {
    /** No conversion; the byte array is passed as value.
     * 
     */
    NONE,
    /** A boolean value is held in a single byte.
     * 
     */
    BOOLEAN_IN_BYTE,
    /** An enum value is held in a single byte.
     * 
     */
    ENUM_IN_BYTE,
    /** An integer value is held in a single byte.
     * 
     */
    INT_IN_BYTE,
    /** A fixed-size String encoded in US-ASCII.
     * 
     */
    FIXED_US_ASCII_STRING_IN_BYTES,
    /** An unknown (fixed-size) type with user-supplied conversion.
     * 
     */
    CUSTOM;
  }
  
  private final ParameterConversion_RolandBoss parameterConversion_RolandBoss;
  
  public final Object convertFromDevice (final byte[] value)
  {
    switch (this.parameterConversion_RolandBoss)
    {
      case NONE:
        return value;
      case BOOLEAN_IN_BYTE:
      {
        if (value == null)
          return null;
        else if (value.length != 1)
          throw new IllegalArgumentException ();
        else if (value[0] != 0 && value[0] != 1)
          throw new IllegalArgumentException ();
        else
          return (value[0] == 1);
      }
      case ENUM_IN_BYTE:
      {
        if (value == null)
          return null;
        else if (value.length != 1)
          throw new IllegalArgumentException ();
        else if (value[0] < 0 || value[0] >= getParameterValueClass ().getEnumConstants ().length)
          throw new IllegalArgumentException ();
        else
          return (getParameterValueClass ().getEnumConstants ()[value[0]]);
      }
      case INT_IN_BYTE:
      {
        if (value == null)
          return null;
        else if (value.length != 1)
          throw new IllegalArgumentException ();
        else if (value[0] < 0)
          throw new IllegalArgumentException ();
        else
          return ((int) value[0]);
      }
      case FIXED_US_ASCII_STRING_IN_BYTES:
      {
        if (value == null)
          return null;
        else if (value.length != getLength ())
          throw new IllegalArgumentException ();
        try
        {
          return new String (value, "US-ASCII");          
        }
        catch (UnsupportedEncodingException uee)
        {
          LOG.log (Level.WARNING, "Encoding Error for US-ASCII: {0}.", HexUtils.bytesToHex (value));
          return null;
        }        
      }
      case CUSTOM:
      {
        if (this.customValueConverter == null)
          throw new RuntimeException ();
        if (value == null)
          return null;
        else if (value.length != getLength ())
          throw new IllegalArgumentException ();
        else
        {
          final E e = this.customValueConverter.fromDevice (value);
          if (e == null)
            throw new RuntimeException ();
          return e;
        }
      }
      default:
        throw new RuntimeException ();
    }
  }

  public final byte[] convertToDevice (final Object value)
  {
    if (value == null)
      throw new IllegalArgumentException ();
    switch (this.parameterConversion_RolandBoss)
    {
      case NONE:
      {
        if (! (value instanceof byte[]))
          throw new IllegalArgumentException ();
        return (byte[]) value;
      }
      case BOOLEAN_IN_BYTE:
      {
        if (! (value instanceof Boolean))
          throw new IllegalArgumentException ();
        return new byte[]{((Boolean) value) ? (byte) 0x01 : (byte) 0x00};
      }
      case ENUM_IN_BYTE:
      {
        if (! getParameterValueClass ().isInstance (value))
          throw new IllegalArgumentException ();
        final int index = Arrays.asList (getParameterValueClass ().getEnumConstants ()).indexOf (value);
        if (index < 0)
          throw new IllegalArgumentException ();
        return new byte[]{(byte) index};
      }
      case INT_IN_BYTE:
      {
        if (! (value instanceof Integer))
          throw new IllegalArgumentException ();
        final int intValue = (int) value;
        if (intValue < 0 || intValue > 127)
          throw new IllegalArgumentException ();
        return new byte[]{(byte) intValue};        
      }
      case FIXED_US_ASCII_STRING_IN_BYTES:
      {
        if (! (value instanceof String))
          throw new IllegalArgumentException ();
        final byte[] stringValue;
        try
        {
          stringValue = ((String) value).getBytes ("US_ASCII");
        }
        catch (UnsupportedEncodingException uee)
        {
          LOG.log (Level.WARNING, "Encoding Error for US-ASCII: {0}.", value);
          return null;
        }
        if (stringValue.length != getLength ())
        {
          final byte[] trimmedStringValue = new byte[getLength ()];
          System.arraycopy (stringValue, 0, trimmedStringValue, 0, Math.min (stringValue.length, trimmedStringValue.length));
          if (trimmedStringValue.length > stringValue.length)
            Arrays.fill (trimmedStringValue, stringValue.length, trimmedStringValue.length, (byte) 0x20 /* US-ASCII SPACE */);
          return trimmedStringValue;
        }
        else
          return stringValue;
      }
      case CUSTOM:
      {
        if (this.customValueConverter == null)
          throw new RuntimeException ();
        else
        {
          final byte[] bytes = this.customValueConverter.toDevice ((E) value);
          if (bytes == null || bytes.length != getLength ())
            throw new RuntimeException ();
          return bytes;
        }
      }
      default:
        throw new RuntimeException ();
    }
  }
  
  public interface CustomValueConverter<C>
  {
    C fromDevice (byte[] bytes);
    byte[] toDevice (C c);
  }
  
  private final CustomValueConverter<E> customValueConverter;
  
  public final CustomValueConverter<E> getCustomValueConverter ()
  {
    return this.customValueConverter;
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // [ParameterType_RolandBoss.MidiControlChange]
  // CONTROLLER
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
  private byte controller;
  
  public final byte getController ()
  {
    if (this.parameterType_RolandBoss == ParameterType_RolandBoss.MidiControlChange)
      return this.controller;
    else
      return (byte) -1;
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // [ParameterType_RolandBoss.MidiSysExRolandBoss_RQ1_DT1]
  // ADDRESS
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
  private final byte[] address;
  
  /** Gets the address in the device's address space at which the parameter starts.
   * 
   * @return The address (clone thereof) in the device's address space at which the parameter starts;
   *           {@code null} if this parameter is not obtained through {@link ParameterType_RolandBoss#MidiSysExRolandBoss_RQ1_DT1}.
   * 
   */
  public final byte[] getAddressAsBytes ()
  {
    if (this.parameterType_RolandBoss == ParameterType_RolandBoss.MidiSysExRolandBoss_RQ1_DT1)
      return this.address.clone ();
    else
      return null;
  }
  
  public final int getAddress ()
  {
    if (this.parameterType_RolandBoss == ParameterType_RolandBoss.MidiSysExRolandBoss_RQ1_DT1)
    {
      final int address = ParameterDescriptor_RolandBoss.lengthToInt (this.address);
      // XXX Always correct with first bit??
      if (address < 0)
        // Better be safe than sorry.
        throw new UnsupportedOperationException ();
      return address;
    }
    else
      return 0;    
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // [ParameterType_RolandBoss.MidiSysExRolandBoss_RQ1_DT1]
  // LENGTH
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
  private final byte[] length;

  public final int getLength ()
  {
    if (this.parameterType_RolandBoss == ParameterType_RolandBoss.MidiSysExRolandBoss_RQ1_DT1)
      return ParameterDescriptor_RolandBoss.lengthToInt (this.length);
    else
      return 0;
  }
    
  public final byte[] getLengthAsBytes ()
  {
    if (this.parameterType_RolandBoss == ParameterType_RolandBoss.MidiSysExRolandBoss_RQ1_DT1)
      return this.length.clone ();
    else
      return null;
  }
    
  private static int lengthToInt (final byte[] lengthBytes)
  {
    if (lengthBytes == null || lengthBytes.length != 4 /* XXX FOR NOW XXX */)
      throw new IllegalArgumentException ();
    if (lengthBytes[0] < 0)
      throw new IllegalArgumentException ();
    return ((((int) lengthBytes[0]) & 0xFF) << 24)
         + ((((int) lengthBytes[1]) & 0xFF) << 16)
         + ((((int) lengthBytes[2]) & 0xFF) <<  8)
         + ((((int) lengthBytes[3]) & 0xFF));
  }
  
  private final String parentKey;
  
  public final String getParentKey ()
  {
    return this.parentKey;
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
      
}
