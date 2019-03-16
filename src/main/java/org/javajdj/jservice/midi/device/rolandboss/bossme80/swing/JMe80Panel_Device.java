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
package org.javajdj.jservice.midi.device.rolandboss.bossme80.swing;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.javajdj.util.hex.HexUtils;
import org.javajdj.jservice.midi.device.DefaultMidiDeviceListener;
import org.javajdj.jservice.midi.device.rolandboss.bossme80.MidiDevice_Me80;
import org.javajdj.swing.JColorCheckBox;
import org.javajdj.jservice.Service;
import org.javajdj.jservice.midi.device.MidiDevice;
import org.javajdj.swing.DefaultMouseListener;

/**A {@link JPanel} for monitoring and controlling a {@link MidiDevice}.
 *
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 */
public class JMe80Panel_Device
  extends JPanel
{

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // LOGGING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private static final Logger LOG = Logger.getLogger (JMe80Panel_Device.class.getName ());
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTORS / FACTORIES / CLONING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public JMe80Panel_Device (final MidiDevice midiDevice)
  {
    super ();
    this.midiDevice = midiDevice;
    setLayout (new GridLayout (6, 2, 5, 5));
    add (new JLabel ("Enabled"));
    final Map<Boolean, Color> enableColorMap = new HashMap<> ();
    enableColorMap.put (false, null);
    enableColorMap.put (true, Color.red);
    this.enabledCheckBox = new JColorCheckBox.JBoolean (enableColorMap);
    this.enabledCheckBox.setDisplayedValue (this.midiDevice != null
                                           && this.midiDevice.getStatus () == Service.Status.ACTIVE);
    this.enabledCheckBox.addMouseListener (new JEnabledMouseListener ());
    add (this.enabledCheckBox);
    add (new JLabel ("Watchdog"));
    final Map<Boolean, Color> watchDogColorMap = new HashMap<> ();
    watchDogColorMap.put (false, Color.orange);
    watchDogColorMap.put (true, Color.green);
    this.watchdogCheckBox = new JColorCheckBox.JBoolean (watchDogColorMap);
    this.watchdogCheckBox.setDisplayedValue (false);
    add (this.watchdogCheckBox);
    add (new JLabel ("Device ID: "));
    add (this.jDeviceId);
    add (new JLabel ("Device Family Code: "));
    add (this.jDeviceFamilyCode);
    add (new JLabel ("Device Family Number: "));
    add (this.jDeviceFamilyNumber);
    add (new JLabel ("Software Revision Level: "));
    add (this.jSoftwareRevisionLevel);
    if (this.midiDevice != null)
    {
      this.midiDevice.addStatusListener (this.midiDeviceStatusListener);
      this.midiDevice.addMidiDeviceListener (this.midiDeviceListener);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MIDI DEVICE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  final MidiDevice midiDevice;

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MIDI DEVICE STATUS LISTENER
  //
  // ENABLED CHECKBOX
  // ENABLED MOUSE LISTENER
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final Service.StatusListener midiDeviceStatusListener = new Service.StatusListener ()
  {
    @Override
    public void onStatusChange (Service service, Service.Status oldStatus, Service.Status newStatus)
    {
      if (! SwingUtilities.isEventDispatchThread ())
      {
        SwingUtilities.invokeLater (() -> onStatusChange (service, oldStatus, newStatus));
        return;
      }
      // Now on Swing EDT.
      final boolean newStatusBoolean = newStatus == Service.Status.ACTIVE;
      JMe80Panel_Device.this.enabledCheckBox.setDisplayedValue (newStatusBoolean);
      if (! newStatusBoolean)
        JMe80Panel_Device.this.setGuiValues (false);
    }
  };

  private final JColorCheckBox.JBoolean enabledCheckBox;
  
  private class JEnabledMouseListener
    extends DefaultMouseListener
  {
    @Override
    public final void mouseClicked (MouseEvent e)
    {
      final MidiDevice midiDevice = JMe80Panel_Device.this.midiDevice;
      if (midiDevice != null)
        midiDevice.toggleService ();
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MIDI DEVICE [WATCHDOG] LISTENER
  //
  // WATCHDOG CHECKBOX
  //
  // DEVICE ID
  // DEVICE FAMILY CODE
  // DEVICE FAMILY NUMBER
  // SOFTWARE REVISION LEVEL
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final class MidiDeviceListener_Me80Watchdog
    extends DefaultMidiDeviceListener
    implements MidiDevice_Me80.MidiDeviceWatchdogListener
  {
    
    @Override
    public void watchdogStart ()
    {
      JMe80Panel_Device.this.setGuiValues (false);      
    }

    @Override
    public void watchdogStop ()
    {
      JMe80Panel_Device.this.setGuiValues (false);      
    }

    @Override
    public void watchdogFail ()
    {
      JMe80Panel_Device.this.setGuiValues (false);
    }

    @Override
    public void watchdogSuccess
      (final byte deviceId, final byte[] deviceFamilyCode, final byte[] deviceFamilyNumber, final byte[] softwareRevisionLevel)
    {
      JMe80Panel_Device.this.setGuiValues (deviceId, deviceFamilyCode, deviceFamilyNumber, softwareRevisionLevel);
    }
    
  };
  
  private final MidiDeviceListener_Me80Watchdog midiDeviceListener = new MidiDeviceListener_Me80Watchdog ();
  
  private final JColorCheckBox.JBoolean watchdogCheckBox;
  
  private final JLabel jDeviceId = new JLabel ("unknown");
  private final JLabel jDeviceFamilyCode = new JLabel ("unknown");
  private final JLabel jDeviceFamilyNumber = new JLabel ("unknown");
  private final JLabel jSoftwareRevisionLevel = new JLabel ("unknown");
  

  private void setGuiValues (final boolean newWatchdogValue)
  {
    if (! SwingUtilities.isEventDispatchThread ())
      SwingUtilities.invokeLater (() -> setGuiValues (newWatchdogValue));
    else
    {
      this.watchdogCheckBox.setDisplayedValue (newWatchdogValue);
      if (! newWatchdogValue)
      {
        JMe80Panel_Device.this.jDeviceId.setText ("unknown");
        JMe80Panel_Device.this.jDeviceFamilyCode.setText ("unknown");
        JMe80Panel_Device.this.jDeviceFamilyNumber.setText ("unknown");
        JMe80Panel_Device.this.jSoftwareRevisionLevel.setText ("unknown");        
      }
    }
  }
  
  private void setGuiValues
  (final byte deviceId, final byte[] deviceFamilyCode, final byte[] deviceFamilyNumber, final byte[] softwareRevisionLevel)
  {
    if (! SwingUtilities.isEventDispatchThread ())
      SwingUtilities.invokeLater (() -> setGuiValues (deviceId, deviceFamilyCode, deviceFamilyNumber, softwareRevisionLevel));
    else
    {
      this.watchdogCheckBox.setDisplayedValue (true);
      this.jDeviceId.setText ("0x" + HexUtils.bytesToHex (new byte[] {deviceId}));
      this.jDeviceFamilyCode.setText ("0x" + HexUtils.bytesToHex (deviceFamilyCode));
      this.jDeviceFamilyNumber.setText ("0x" + HexUtils.bytesToHex (deviceFamilyNumber));
      this.jSoftwareRevisionLevel.setText ("0x" + HexUtils.bytesToHex (softwareRevisionLevel));
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
