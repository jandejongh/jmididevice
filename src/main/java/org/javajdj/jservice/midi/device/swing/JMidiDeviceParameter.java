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

/** A {@link JPanel} holding the name and value of a (simple) MIDI-Device parameter.
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
    if (midiDevice == null)
      throw new IllegalArgumentException ();
    if (jValueComponent == null)
      throw new IllegalArgumentException ();
    this.midiDevice = midiDevice;
    this.displayName = displayName;
    this.key = key;
    this.jValueComponent = jValueComponent;
    // this.jValueComponent.addItemListener (new ValueItemListener ());
    getMidiDevice ().addMidiDeviceListener (new SettingsListener ());
    setLayout (new GridLayout (1, 2, 5, 5));
    add (new JLabel (getDisplayName ()));
    add (this.jValueComponent);
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
  
  private class SettingsListener
    implements MidiDeviceListener
  {

    @Override
    public void notifyParameterChanged (final Map<String, Object> changes)
    {
      if (changes == null || changes.isEmpty ())
        throw new RuntimeException ();
      for (final Map.Entry<String, Object> entry : changes.entrySet ())
      {
        final String name = entry.getKey ();
        final C value = (C) entry.getValue ();
        if (name.equals (getKey ()))
          dataValueChanged (value);
      }
    }

  }
  
  protected void setDataValue (final C newDataValue)
  {
    getMidiDevice ().put (getKey (), newDataValue);
  }
  
  protected void dataValueChanged (C newDataValue)
  {
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // DISPLAY NAME
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final String displayName;
  
  public final String getDisplayName ()
  {
    return this.displayName;
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
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
}
