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
package org.javajdj.jservice.midi.device.alesis.qvgt.swing;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.javajdj.jservice.midi.device.DefaultMidiDeviceListener;
import org.javajdj.swing.JColorCheckBox;
import org.javajdj.jservice.midi.device.MidiDevice;
import org.javajdj.jservice.midi.device.alesis.qvgt.MidiDevice_QVGT;
import org.javajdj.jservice.midi.swing.JRawMidiService;
import org.javajdj.jservice.swing.JServiceControl;

/**A {@link JPanel} for monitoring and controlling a {@link MidiDevice}.
 *
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 */
public class JQVGTPanel_Device
  extends JPanel
{

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // LOGGING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private static final Logger LOG = Logger.getLogger (JQVGTPanel_Device.class.getName ());
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTORS / FACTORIES / CLONING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  /** Constructs the panel.
   * 
   * If a {@code null} MIDI device is passed, the device will be shown disabled without the option to enable it.
   * In other words, not a very useful feature.
   * 
   * @param midiDevice The MIDI device, may be {@code null}.
   * 
   */
  public JQVGTPanel_Device (final MidiDevice midiDevice)
  {
    super ();
    this.midiDevice = midiDevice;
    setLayout (new GridLayout (6, 2, 5, 5));
    add (new JLabel ("Enabled"));
    final Map<Boolean, Color> enableColorMap = new HashMap<> ();
    enableColorMap.put (false, null);
    enableColorMap.put (true, Color.red);
    this.enabledCheckBox = new JServiceControl (midiDevice, JRawMidiService.DEFAULT_STATUS_COLOR_FUNCTION);
    add (this.enabledCheckBox);
    add (new JLabel ("Watchdog"));
    final Map<Boolean, Color> watchDogColorMap = new HashMap<> ();
    watchDogColorMap.put (false, Color.orange);
    watchDogColorMap.put (true, Color.green);
    this.watchdogCheckBox = new JColorCheckBox.JBoolean (watchDogColorMap);
    this.watchdogCheckBox.setDisplayedValue (false);
    add (this.watchdogCheckBox);
    add (new JPanel ());
    add (new JPanel ());
    add (new JPanel ());
    add (new JPanel ());
    add (new JPanel ());
    add (new JPanel ());
    add (new JPanel ());
    add (new JPanel ());
    if (this.midiDevice != null)
      this.midiDevice.addMidiDeviceListener (this.midiDeviceListener);
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
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final JServiceControl enabledCheckBox;
  
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
  
  private final class MidiDeviceListener_QVGTWatchdog
    extends DefaultMidiDeviceListener
    implements MidiDevice_QVGT.MidiDeviceWatchdogListener
  {
    
    @Override
    public void watchdogStart ()
    {
      JQVGTPanel_Device.this.setGuiValues (false);      
    }

    @Override
    public void watchdogStop ()
    {
      JQVGTPanel_Device.this.setGuiValues (false);      
    }

    @Override
    public void watchdogFail ()
    {
      JQVGTPanel_Device.this.setGuiValues (false);
    }

    @Override
    public void watchdogSuccess ()
    {
      JQVGTPanel_Device.this.setGuiValues (true);
    }
    
  };
  
  private final MidiDeviceListener_QVGTWatchdog midiDeviceListener = new MidiDeviceListener_QVGTWatchdog ();
  
  private final JColorCheckBox.JBoolean watchdogCheckBox;
  
  private void setGuiValues (final boolean newWatchdogValue)
  {
    if (! SwingUtilities.isEventDispatchThread ())
      SwingUtilities.invokeLater (() -> setGuiValues (newWatchdogValue));
    else
    {
      // LOG.log (Level.INFO, "Setting displayed value on watchdow to {0}.", newWatchdogValue);
      this.watchdogCheckBox.setDisplayedValue (newWatchdogValue);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
