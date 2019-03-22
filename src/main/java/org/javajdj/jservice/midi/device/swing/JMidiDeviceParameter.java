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
package org.javajdj.jservice.midi.device.swing;

import java.awt.GridLayout;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.javajdj.jservice.midi.device.MidiDevice;
import org.javajdj.jservice.midi.device.MidiDeviceListener;
import org.javajdj.swing.SwingUtilsJdJ;

/** A {@link JPanel} holding the name and value of a (simple) MIDI-Device parameter.
 * 
 * <p>
 * Both name and value component are optional.
 * This allows sub-classes to merely use the basic MIDI-device registry
 * functions of this component.
 * 
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 * @param <C> The value (generic) type.
 * 
 */
public class JMidiDeviceParameter<C>
  extends JPanel
{
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTORS / FACTORIES / CLONING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public JMidiDeviceParameter
    (final MidiDevice midiDevice,
     final String displayName,
     final String key,
     final JComponent jValueComponent)
  {
    super ();
    if (midiDevice == null || key == null || ! midiDevice.containsKey (key))
      throw new IllegalArgumentException ();
    this.midiDevice = midiDevice;
    this.displayName = displayName;
    this.jDisplayNameComponent = (this.displayName != null ? new JLabel (this.displayName) : null);
    this.key = key;
    this.jValueComponent = jValueComponent;
    getMidiDevice ().addMidiDeviceListener (this.midiDeviceListener);
    if (this.jDisplayNameComponent != null || this.jValueComponent != null)
    {
      final int nrOfComponents = (this.jDisplayNameComponent != null && this.jValueComponent != null ? 2 : 1);
      setLayout (new GridLayout (1, nrOfComponents, 5, 5));
      if (this.jDisplayNameComponent != null)
        add (this.jDisplayNameComponent);
      if (this.jValueComponent != null)
        add (this.jValueComponent);
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MIDI DEVICE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final MidiDevice midiDevice;
  
  public final MidiDevice getMidiDevice ()
  {
    return this.midiDevice;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MIDI DEVICE LISTENER
  //
  // SET DATA VALUE
  // DATA VALUE CHANGED
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final MidiDeviceListener midiDeviceListener = (final Map<String, Object> changes) ->
  {
    if (changes == null || changes.isEmpty ())
      throw new RuntimeException ();
    if (! changes.containsKey (getKey ()))
      return;
    final C value = (C) changes.get (getKey ());
    dataValueChanged (value);
    SwingUtilsJdJ.invokeOnSwingEDT (()->
    {
      SwingUtilsJdJ.enableComponentAndDescendants (this, (! JMidiDeviceParameter.this.readOnly) && value != null);
    });
  };
  
  protected void setDataValue (final C newDataValue)
  {
    getMidiDevice ().put (getKey (), newDataValue);
  }
  
  protected void dataValueChanged (final C newDataValue)
  {
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // DISPLAY NAME
  // DISPLAY NAME COMPONENT
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private volatile String displayName;
  
  private final JLabel jDisplayNameComponent;
  
  public final String getDisplayName ()
  {
    return this.displayName;
  }
  
  public final void setDisplayName (final String displayName)
  {
    if (displayName == null || displayName.trim ().isEmpty ())
      throw new IllegalArgumentException ();
    if (this.displayName == null)
      throw new IllegalArgumentException ();
    if (this.displayName == null || displayName.equals (this.displayName))
      return;
    this.displayName = displayName;
    SwingUtilsJdJ.invokeOnSwingEDT (() -> this.jDisplayNameComponent.setText (displayName));
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // KEY
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final String key;
  
  public final String getKey ()
  {
    return this.key;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // VALUE JCOMPONENT
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final JComponent jValueComponent;
  
  protected final JComponent getValueComponent ()
  {
    return this.jValueComponent;
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // READ ONLY
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private volatile boolean readOnly = false;
  
  public final boolean isReadOnly ()
  {
    return this.readOnly;
  }
  
  public synchronized void setReadOnly (final boolean readOnly)
  {
    if (readOnly != this.readOnly)
    {
      this.readOnly = readOnly;
      SwingUtilsJdJ.invokeOnSwingEDT (() -> setEnabled (! readOnly));
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
}
