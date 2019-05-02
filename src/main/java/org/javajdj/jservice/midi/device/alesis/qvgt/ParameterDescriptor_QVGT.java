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
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.javajdj.jservice.midi.device.MidiDevice;
import org.javajdj.jservice.midi.device.ParameterDescriptor;
import org.javajdj.util.hex.HexUtils;

/** Description of a parameter (key, value pair) for an Alesis Quadraverb GT.
 * 
 * <p>
 * Setting the parameter on the device is supported though
 * MIDI Program Change Message, MIDI Control Change Message,
 * and through a few Alesis Quadraverb-GT specific System Exclusive messages
 * (MIDI Editing, MIDI Data Dump, MIDI Dump Request).
 *
 * <p>
 * This class is considered an implementation class to {@link MidiUtils_QVGT},
 * and is therefore (and most of its methods)
 * package-private, with the exception of certain parameter-type definitions.
 * 
 * @param <E> The type of the parameter value (in {@link MidiDevice}).
 * 
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 * @see MidiDevice_QVGT
 * 
 */
final class ParameterDescriptor_QVGT<E>
  implements ParameterDescriptor
{
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // LOGGING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private static final Logger LOG = Logger.getLogger (ParameterDescriptor_QVGT.class.getName ());
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTORS / FACTORIES / CLONING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  ParameterDescriptor_QVGT
  ( final ParameterType_QVGT parameterType_QVGT,
    final String parameterName,
    final Class<E> parameterValueClass,
    final ParameterConversion_QVGT parameterConversion_QVGT,
    final Integer controller,
    final Function_QVGT function,
    final Integer page,
    final Integer program,
    final int[] offsets,
    final Integer size,
    final Integer bitOffset,
    final Integer bitSize,
    final String parentKey,
    final CustomValueConverter<E> customValueConverter,
    final Function<Object, Boolean> parentValidator)
  {
    if (parameterType_QVGT == null)
      throw new IllegalArgumentException ();
    this.parameterType_QVGT = parameterType_QVGT;
    if (parameterName == null || parameterName.trim ().length () == 0)
      throw new IllegalArgumentException ();
    this.parameterName = parameterName;
    this.parameterValueClass = parameterValueClass;
    this.parameterConversion_QVGT =
      (parameterConversion_QVGT != null ? parameterConversion_QVGT : ParameterConversion_QVGT.NONE);
    switch (this.parameterType_QVGT)
    {
      case MidiControlChange:
      {
        if ( controller == null
          || controller < 0
          || controller > 127
          || function != null
          || page != null
          || program != null
          || offsets != null
          || size == null || size != 2
          || bitOffset != null
          || bitSize != null
          || parentKey != null
          || parentValidator != null)
          throw new IllegalArgumentException (parameterName);
        break;
      }
      case MidiProgramChange:
      {
        if ( controller != null
          || function != null
          || page != null
          || program != null
          || offsets != null
          || size == null || size != 1
          || bitOffset != null
          || bitSize != null
          || parentKey != null
          || parentValidator != null)
          throw new IllegalArgumentException (parameterName);
        break;
      }
      case MidiSysEx_QVGT_Editing:
      {
        if ( controller != null
          || (page != null && (page < 0))
          || program == null
          || offsets == null || offsets.length == 0
          || size == null || (offsets.length > 1 && size != offsets.length)
          || ((bitOffset == null) != (bitSize == null))
          || ((bitOffset != null) && (size != 1))
          || ((bitOffset != null) && (bitOffset < 0 || bitOffset > 7))
          || ((bitSize != null) && (bitSize <= 0 || bitOffset + bitSize > 8))
          || parentKey == null)
          throw new IllegalArgumentException (parameterName);
        break;
      }
      case MidiSysEx_QVGT_DataDump:
      {
        if ( controller != null
          || function != null
          || page != null
          || program == null
          || offsets != null
          || size == null || size != Patch_QGVT.ENCODED_PATCH_SIZE
          || bitOffset != null
          || bitSize != null
          || parentKey != null
          || parentValidator != null)
          throw new IllegalArgumentException (parameterName);
        break;
      }
      default:
      {
        throw new RuntimeException (parameterName);
      }
    }
    this.controller = controller;
    this.function = function;
    this.page = page;
    this.program = program;
    this.offsets = (offsets != null ? offsets.clone () : null);
    this.size = size;
    this.bitOffset = bitOffset;
    this.bitSize = bitSize;
    this.parentKey = parentKey;
    if (parameterConversion_QVGT == ParameterConversion_QVGT.CUSTOM && customValueConverter == null)
      throw new IllegalArgumentException ();
    this.customValueConverter = customValueConverter;
    this.parentValidator = parentValidator;
  }
  
  ParameterDescriptor_QVGT
  ( final ParameterType_QVGT parameterType_QVGT,
    final String parameterName,
    final Class<E> parameterValueClass,
    final ParameterConversion_QVGT parameterConversion_QVGT,
    final Integer controller,
    final Function_QVGT function,
    final Integer page,
    final Integer program,
    final Integer offset,
    final Integer size,
    final Integer bitOffset,
    final Integer bitSize,
    final String parentKey,
    final CustomValueConverter<E> customValueConverter,
    final Function<Object, Boolean> parentValidator)
  {
    this (
      parameterType_QVGT,
      parameterName,
      parameterValueClass,
      parameterConversion_QVGT,
      controller,
      function,
      page,
      program,
      offset != null ? new int[]{offset} : null,
      size,
      bitOffset,
      bitSize,
      parentKey,
      customValueConverter,
      parentValidator);
  }
  
  ParameterDescriptor_QVGT (final String parameterName)
  {
    this (
      ParameterType_QVGT.MidiProgramChange,
      parameterName,
      (Class<E>) Integer.class,
      ParameterConversion_QVGT.INT_IN_BYTE, /* parameterConversion_QVGT */
      null, /* controller */
      null, /* function */
      null, /* page */
      null, /* program */
      (Integer) null, /* offset */
      1, /* size */
      null, /* bitOffset */
      null, /* bitSize */
      null, /* parentKey */
      null, /* customValueConverter */
      null /* parentValidator */);
  }
  
  ParameterDescriptor_QVGT
  ( final String parameterName,
    final int program)
  {
    this (
      ParameterType_QVGT.MidiSysEx_QVGT_DataDump,
      parameterName,
      (Class<E>) Patch_QGVT.class,
      ParameterConversion_QVGT.CUSTOM,
      null, /* controller */
      null, /* function */
      null, /* page */
      program,
      (int[]) null, /* offset */
      Patch_QGVT.ENCODED_PATCH_SIZE, /* size */
      null, /* bitOffset */
      null, /* bitSize */
      null, /* parentKey */
      (CustomValueConverter<E>) ParameterDescriptor_QVGT.PATCH_VALUE_CONVERTER,
      null /* parentValidator */);
  }
  
  ParameterDescriptor_QVGT
  ( final String parameterName,
    final Class<E> parameterValueClass,
    final ParameterConversion_QVGT parameterConversion_QVGT,
    final Function_QVGT function,
    final Integer page,
    final Integer program,
    final Integer offset,
    final Integer size,
    final String parentKey,
    final CustomValueConverter<E> customValueConverter)
  {
    this (
      ParameterType_QVGT.MidiSysEx_QVGT_Editing,
      parameterName,
      parameterValueClass,
      parameterConversion_QVGT,
      null, /* controller */
      function,
      page,
      program,
      offset,
      size,
      null, /* bitOffset */
      null, /* bitSize */
      parentKey,
      customValueConverter,
      null /* parentValidator */);
  }
  
  ParameterDescriptor_QVGT
  ( final String parameterName,
    final Class<E> parameterValueClass,
    final ParameterConversion_QVGT parameterConversion_QVGT,
    final Function_QVGT function,
    final Integer page,
    final Integer program,
    final Integer offset,
    final Integer size,
    final Integer bitOffset,
    final Integer bitSize,
    final String parentKey,
    final CustomValueConverter<E> customValueConverter)
  {
    this (
      ParameterType_QVGT.MidiSysEx_QVGT_Editing,
      parameterName,
      parameterValueClass,
      parameterConversion_QVGT,
      null, /* controller */
      function,
      page,
      program,
      offset,
      size,
      bitOffset,
      bitSize,
      parentKey,
      customValueConverter,
      null /* parentValidator */);
  }
  
  ParameterDescriptor_QVGT
  ( final String parameterName,
    final Class<E> parameterValueClass,
    final ParameterConversion_QVGT parameterConversion_QVGT,
    final Function_QVGT function,
    final Integer page,
    final Integer program,
    final Integer offset,
    final Integer size,
    final String parentKey,
    final CustomValueConverter<E> customValueConverter,
    final Function<Object, Boolean> parentValidator)
  {
    this (
      ParameterType_QVGT.MidiSysEx_QVGT_Editing,
      parameterName,
      parameterValueClass,
      parameterConversion_QVGT,
      null, /* controller */
      function,
      page,
      program,
      offset,
      size,
      null, /* bitOffset */
      null, /* bitSize */
      parentKey,
      customValueConverter,
      parentValidator);
  }
  
  ParameterDescriptor_QVGT
  ( final String parameterName,
    final Class<E> parameterValueClass,
    final ParameterConversion_QVGT parameterConversion_QVGT,
    final Function_QVGT function,
    final Integer page,
    final Integer program,
    final Integer offset,
    final Integer size,
    final Integer bitOffset,
    final Integer bitSize,
    final String parentKey,
    final CustomValueConverter<E> customValueConverter,
    final Function<Object, Boolean> parentValidator)
  {
    this (
      ParameterType_QVGT.MidiSysEx_QVGT_Editing,
      parameterName,
      parameterValueClass,
      parameterConversion_QVGT,
      null, /* controller */
      function,
      page,
      program,
      offset,
      size,
      bitOffset,
      bitSize,
      parentKey,
      customValueConverter,
      parentValidator);
  }
  
  ParameterDescriptor_QVGT
  ( final String parameterName,
    final Class<E> parameterValueClass,
    final ParameterConversion_QVGT parameterConversion_QVGT,
    final Function_QVGT function,
    final Integer page,
    final Integer program,
    final int[] offsets,
    final Integer size,
    final String parentKey,
    final CustomValueConverter<E> customValueConverter,
    final Function<Object, Boolean> parentValidator)
  {
    this (
      ParameterType_QVGT.MidiSysEx_QVGT_Editing,
      parameterName,
      parameterValueClass,
      parameterConversion_QVGT,
      null, /* controller */
      function,
      page,
      program,
      offsets,
      size,
      null, /* bitOffset */
      null, /* bitSize */
      parentKey,
      customValueConverter,
      parentValidator);
  }
  
  private final static CustomValueConverter<Patch_QGVT> PATCH_VALUE_CONVERTER = new CustomValueConverter<Patch_QGVT> ()
  {
    
    @Override
    public final Patch_QGVT fromDevice (final byte[] bytes)
    {
      return Patch_QGVT.fromBytes (bytes);
    }

    @Override
    public final byte[] toDevice (final Patch_QGVT c)
    {
      if (c == null)
        throw new IllegalArgumentException ();
      return c.getDecodedBytes ();
    }
    
  };
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PARAMETER TYPE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
  enum ParameterType_QVGT
  {
    MidiControlChange,
    MidiProgramChange,
    MidiSysEx_QVGT_Editing,
    MidiSysEx_QVGT_DataDump;
  }
  
  private final ParameterType_QVGT parameterType_QVGT;
  
  final ParameterType_QVGT getParameterType_QVGT ()
  {
    return this.parameterType_QVGT;
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
  
  final Class<E> getParameterValueClass ()
  {
    return this.parameterValueClass;
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PARAMETER CONVERSION
  // CUSTOM VALUE CONVERTER
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  enum ParameterConversion_QVGT
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
    /** An integer value is held in two bytes.
     * 
     */
    INT_IN_2BYTES,
    /** A fixed-size String encoded in US-ASCII.
     * 
     */
    FIXED_US_ASCII_STRING_IN_BYTES,
    /** An unknown (fixed-size) type with user-supplied conversion.
     * 
     */
    CUSTOM;
  }
  
  private final ParameterConversion_QVGT parameterConversion_QVGT;
  
  final Object convertFromDevice (final byte[] value)
  {
    switch (this.parameterConversion_QVGT)
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
        {
          LOG.log (Level.SEVERE, "ENUM_IN_BYTE[{0}]: value ({1}) out of range [0, {2}]!",
            new Object[]{getParameterName (), value[0], getParameterValueClass ().getEnumConstants ().length - 1});
          throw new IllegalArgumentException ();
        }
        else
          return (getParameterValueClass ().getEnumConstants ()[value[0]]);
      }
      case INT_IN_BYTE:
      {
        if (value == null)
          return null;
        else if (value.length != 1)
          throw new IllegalArgumentException ();
//        // XXX Is this check necessary? Or even plain wrong?
//        else if (value[0] < 0)
//          throw new IllegalArgumentException ();
//        else
//          return ((int) value[0]);
        return ((int) value[0]) & 0xFF;
      }
      case INT_IN_2BYTES:
      {
        if (value == null)
          return null;
        else if (value.length != 2)
          throw new IllegalArgumentException ();
        return (0x100 * (value[0] & 0xFF)) + (value[1] & 0xFF);
      }
      case FIXED_US_ASCII_STRING_IN_BYTES:
      {
        if (value == null)
          return null;
        else if (value.length != getSize ())
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
        else if (value.length != getSize ())
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

  final byte[] convertToDevice (final Object value)
  {
    if (value == null)
      throw new IllegalArgumentException ();
    switch (this.parameterConversion_QVGT)
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
        if (intValue < 0 || intValue > /* XXX 127 */ 255)
          throw new IllegalArgumentException ();
        return new byte[]{(byte) intValue};        
      }
      case INT_IN_2BYTES:
      {
        if (! (value instanceof Integer))
          throw new IllegalArgumentException ();
        final int intValue = (int) value;
        if (intValue < 0 || intValue > 65535)
          throw new IllegalArgumentException ();
        return new byte[]{(byte) ((intValue & 0xFF00) >>> 8), (byte) (intValue & 0xFF)};        
      }
      case FIXED_US_ASCII_STRING_IN_BYTES:
      {
        if (! (value instanceof String))
          throw new IllegalArgumentException ();
        final byte[] stringValue;
        try
        {
          stringValue = ((String) value).getBytes ("US-ASCII");
        }
        catch (UnsupportedEncodingException uee)
        {
          LOG.log (Level.WARNING, "Encoding Error for US-ASCII: {0}.", value);
          return null;
        }
        if (stringValue.length != getSize ())
        {
          final byte[] trimmedStringValue = new byte[getSize ()];
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
          if (bytes == null || bytes.length != getSize ())
            throw new RuntimeException ();
          return bytes;
        }
      }
      default:
        throw new RuntimeException ();
    }
  }
  
  interface CustomValueConverter<C>
  {
    C fromDevice (byte[] bytes);
    byte[] toDevice (C c);
  }
  
  private final CustomValueConverter<E> customValueConverter;
  
  final CustomValueConverter<E> getCustomValueConverter ()
  {
    return this.customValueConverter;
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONTROLLER
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
  private Integer controller;
  
  final int getController ()
  {
    if (this.controller != null)
      return this.controller;
    else
      throw new RuntimeException ();
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // FUNCTION
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   
  enum Function_QVGT
  {
    
    F_REVERB  ( 1),
    F_DELAY   ( 2),
    F_PITCH   ( 3),
    F_EQ      ( 4),
    F_CONFIG  ( 7),
    F_MIX     ( 8),
    F_NAM_MOD ( 9),
    F_PREAMP  (10);

    private Function_QVGT (final int functionNumber)
    {
      this.functionNumber = functionNumber;
    }
    
    private final int functionNumber;
    
    public final int getFunctionNumber ()
    {
      return this.functionNumber;
    }
    
  }
  
  private final Function_QVGT function;

  final Function_QVGT getFunction ()
  {
    return this.function;
  }
  
  final int getFunctionNumber ()
  {
    if (this.function != null)
      return this.function.getFunctionNumber ();
    else
      throw new RuntimeException ();
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PAGE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
  private final Integer page;
  
  final int getPage ()
  {
    if (this.page != null)
      return this.page;
    else
      throw new RuntimeException ();
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PROGRAM
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
  private final Integer program;
  
  final int getProgram ()
  {
    if (this.program != null)
      return this.program;
    else
      throw new RuntimeException ();
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // OFFSET[S]
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
  private final int[] offsets;
  
  final int getOffset ()
  {
    if (this.offsets != null && this.offsets.length == 1)
      return this.offsets[0];
    else
      throw new RuntimeException ();
  }
  
  final int[] getOffsets ()
  {
    if (this.offsets != null)
      return this.offsets;
    else
      throw new RuntimeException ();
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // SIZE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
  private final Integer size;
  
  final int getSize ()
  {
    if (this.size != null)
      return this.size;
    else
      throw new RuntimeException ();
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // BIT OFFSET
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
  private final Integer bitOffset;
  
  final Integer getBitOffset ()
  {
    if (this.parameterType_QVGT == ParameterType_QVGT.MidiSysEx_QVGT_Editing)
      return this.bitOffset;
    else
      throw new RuntimeException ();
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // BIT SIZE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
  private final Integer bitSize;
  
  final int getBitSize ()
  {
    if (this.bitSize != null)
      return this.bitSize;
    else
      throw new RuntimeException ();
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PARENT KEY
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
      
  private final String parentKey;
  
  final String getParentKey ()
  {
    return this.parentKey;
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PARENT VALIDATOR
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
      
  private final Function<Object, Boolean> parentValidator;
  
  final Function<Object, Boolean> getParentValidator ()
  {
    return this.parentValidator;
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
      
}
