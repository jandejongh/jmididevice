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

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.javajdj.jservice.midi.device.MidiDeviceListener;
import org.javajdj.jservice.midi.device.rolandboss.AbstractMidiDevice_RolandBoss;
import org.javajdj.jservice.midi.device.rolandboss.ParameterDescriptor_RolandBoss;
import org.javajdj.jservice.midi.MidiService;
import org.javajdj.jservice.midi.device.MidiDevice;

/** A {@link MidiDevice} implementation (base part) for the Roland-Boss ME-80.
 * 
 * <p>
 * The base part registers only three parameters:
 * The current patch number,
 * the system setting, and
 * the temporary patch.
 * These three parameters are periodically requested from the device
 * through RQ1 messages.
 * 
 * <p>
 * The three parameters are subdivided into more
 * detailed (sub-)parameters in
 * {@link MidiDevice_Me80}.
 * 
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 */
public class MidiDevice_Me80_Base
  extends AbstractMidiDevice_RolandBoss<ParameterDescriptor_RolandBoss>
{

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // LOGGING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private static final Logger LOG = Logger.getLogger (MidiDevice_Me80_Base.class.getName ());

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTORS / FACTORIES / CLONING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public MidiDevice_Me80_Base (final MidiService midiService)
  {
    super (midiService);
    registerParameters_Me80_Base ();
    addRunnable (this.me80MainRequestLoop);
    addRunnable (this.me80Watchdog);
  }

  public final static String CURRENT_PATCH_NO_RAW_NAME = "current_patch_no_raw"; // Likely renamed and re-typed in sub-class...
  public final static String SYSTEM_NAME               = "system";
  public final static String TEMPORARY_PATCH_NAME      = "temporary_patch";
  
  public final static byte TEMPORARY_PATCH_SIZE = (byte) 0x47;
  
  protected final Set<String> ME80_BASE_PARAMETERS = new LinkedHashSet<> (Arrays.asList (new String[]
  {
    CURRENT_PATCH_NO_RAW_NAME,
    SYSTEM_NAME,
    TEMPORARY_PATCH_NAME
  }));
  
  private void registerParameters_Me80_Base ()
  {
    
    registerParameter (new ParameterDescriptor_RolandBoss (CURRENT_PATCH_NO_RAW_NAME, byte[].class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.NONE,
      new byte[]{0x00, 0x00, 0x00, 0x00}, new byte[]{0x00, 0x00, 0x00, 0x01}, null));
    
    registerParameter (new ParameterDescriptor_RolandBoss (SYSTEM_NAME, byte[].class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.NONE,
      new byte[]{0x10, 0x00, 0x00, 0x00}, new byte[]{0x00, 0x00, 0x00, 0x0A}, null));
        
    registerParameter (new ParameterDescriptor_RolandBoss (TEMPORARY_PATCH_NAME, byte[].class,
      ParameterDescriptor_RolandBoss.ParameterConversion_RolandBoss.NONE,
      new byte[]{0x20, 0x00, 0x00, 0x00}, new byte[]{0x00, 0x00, 0x00, TEMPORARY_PATCH_SIZE}, null));
    
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // ME80 MAIN REQUEST LOOP
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final long ME80_MAIN_REQUEST_LOOP_PERIOD_MS = 500L;
  
  private final Runnable me80MainRequestLoop = () ->
  {
    LOG.log (Level.INFO, "Starting Main Request Loop on BOSS ME-80.");
    try
    {
      while (! Thread.interrupted ())
      {
        MidiDevice_Me80_Base.this.sendMidiIdReq ();
        Thread.sleep (50L);
        MidiDevice_Me80_Base.this.sendMidiSysExMessage_RolandBoss_RQ1 (MidiDevice_Me80_Base.CURRENT_PATCH_NO_RAW_NAME);
        Thread.sleep (50L);
        MidiDevice_Me80_Base.this.sendMidiSysExMessage_RolandBoss_RQ1 (MidiDevice_Me80_Base.SYSTEM_NAME);
        Thread.sleep (100L);
        MidiDevice_Me80_Base.this.sendMidiSysExMessage_RolandBoss_RQ1 (MidiDevice_Me80_Base.TEMPORARY_PATCH_NAME);
        Thread.sleep (MidiDevice_Me80_Base.this.ME80_MAIN_REQUEST_LOOP_PERIOD_MS - 200L);
      }
    }
    catch (InterruptedException ie)
    {      
    }
    LOG.log (Level.INFO, "Terminated Main Request Loop on BOSS ME-80.");
  };
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // ME80 WATCHDOG LISTENERS
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public static interface MidiDeviceWatchdogListener
    extends MidiDeviceListener
  {
    void watchdogStart ();
    void watchdogStop ();
    void watchdogFail ();
    void watchdogSuccess
      (final byte deviceId, final byte[] deviceFamilyCode, final byte[] deviceFamilyNumber, final byte[] softwareRevisionLevel);
  }
  
  protected void fireWatchdogStart ()
  {
    final Set<MidiDeviceListener> midiDeviceListenersCopy = getMidiDeviceListenersCopy ();
    for (final MidiDeviceListener l : midiDeviceListenersCopy)
      if (l instanceof MidiDeviceWatchdogListener)
        ((MidiDeviceWatchdogListener) l).watchdogStart ();
  }
  
  protected void fireWatchdogStop ()
  {
    final Set<MidiDeviceListener> midiDeviceListenersCopy = getMidiDeviceListenersCopy ();
    for (final MidiDeviceListener l : midiDeviceListenersCopy)
      if (l instanceof MidiDeviceWatchdogListener)
        ((MidiDeviceWatchdogListener) l).watchdogStop ();    
  }
  
  protected void fireWatchdogFail ()
  {
    final Set<MidiDeviceListener> midiDeviceListenersCopy = getMidiDeviceListenersCopy ();
    for (final MidiDeviceListener l : midiDeviceListenersCopy)
      if (l instanceof MidiDeviceWatchdogListener)
        ((MidiDeviceWatchdogListener) l).watchdogFail ();
  }
  
  protected void fireWatchdogSuccess
    (final byte deviceId, final byte[] deviceFamilyCode, final byte[] deviceFamilyNumber, final byte[] softwareRevisionLevel)
  {
    final Set<MidiDeviceListener> midiDeviceListenersCopy = getMidiDeviceListenersCopy ();
    for (final MidiDeviceListener l : midiDeviceListenersCopy)
      if (l instanceof MidiDeviceWatchdogListener)
        ((MidiDeviceWatchdogListener) l).watchdogSuccess (deviceId, deviceFamilyCode, deviceFamilyNumber, softwareRevisionLevel);
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // ME80 WATCHDOG
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private volatile Instant lastUpdate = Instant.MIN;
  
  // XXX Concurrency issues on this field?
  private volatile boolean watchdogStatus = false;

  @Override
  protected void onIdReply
  (final byte deviceId, final byte[] deviceFamilyCode, final byte[] deviceFamilyNumber, final byte[] softwareRevisionLevel)
  {
    super.onIdReply (deviceId, deviceFamilyCode, deviceFamilyNumber, softwareRevisionLevel);
    this.lastUpdate = Instant.now ();
    this.watchdogStatus = true;
    fireWatchdogSuccess (deviceId, deviceFamilyCode, deviceFamilyNumber, softwareRevisionLevel);
  }
  
  private final Runnable me80Watchdog = () ->
  {
    try
    {
      LOG.log (Level.INFO, "Starting Watchdog on BOSS ME-80.");
      fireWatchdogStart ();
      while (! Thread.interrupted ())
      {
        boolean newWatchdogStatus;
        try
        {
          newWatchdogStatus = Duration.between (MidiDevice_Me80_Base.this.lastUpdate, Instant.now ()).toMillis () <= 2000L;
        }
        catch (ArithmeticException ae)
        {
          newWatchdogStatus = false;
        }
        if (newWatchdogStatus != MidiDevice_Me80_Base.this.watchdogStatus)
        {
          MidiDevice_Me80_Base.this.watchdogStatus = newWatchdogStatus;
          if (! newWatchdogStatus)
            MidiDevice_Me80_Base.this.fireWatchdogFail ();
        }
        Thread.sleep (1000L);
      }
    }
    catch (InterruptedException ie)
    {
    }
    MidiDevice_Me80_Base.this.watchdogStatus = false;
    fireWatchdogStop ();
    LOG.log (Level.INFO, "Terminated Watchdog on BOSS ME-80.");
  };

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MIDI SERVICE [AbstractMidiDevice]
  // RX HANDLING
  //
  // PARAMETER READ FROM DEVICE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  @Override
  protected void onParameterReadFromDevice (final String key, final byte[] value)
  {
    super.onParameterReadFromDevice (key, value);
    if (value == null)
      throw new RuntimeException ();
    switch (key)
    {
      case CURRENT_PATCH_NO_RAW_NAME:
      case SYSTEM_NAME:
      case TEMPORARY_PATCH_NAME:
        // LOG.log (Level.INFO, "From device: {0}: {1}.", new Object[]{key, HexUtils.bytesToHex (value)});
        break;
      default:
        return;
    }
    switch (key)
    {
      case CURRENT_PATCH_NO_RAW_NAME:
        updatePatchNoFromDevice (value);
        break;
      case SYSTEM_NAME:
        updateSystemSettingsFromDevice (value);
        break;
      case TEMPORARY_PATCH_NAME:
        updateTemporaryPatchFromDevice (value);
        break;
      default:
        throw new RuntimeException ();
    }
  }
  
  protected void updatePatchNoFromDevice (final byte[] value)
  {
  }
  
  protected void updateSystemSettingsFromDevice (final byte[] value)
  {
  }
  
  protected void updateTemporaryPatchFromDevice (final byte[] value)
  {
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
