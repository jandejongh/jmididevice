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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;
import org.javajdj.jservice.midi.device.MidiDevice;

/** A {@link JMidiDeviceParameter} for an {@link Enum}-valued parameter.
 * 
 * <p>
 * The value is shown in a {@link JComboBox}.
 * 
 * @param <E> The enum (generic) type.
 * 
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 */
public class JMidiDeviceParameter_Enum<E extends Enum<E>>
  extends JMidiDeviceParameter<E>
{

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTORS / FACTORIES / CLONING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public JMidiDeviceParameter_Enum (final MidiDevice midiDevice,
                                   final String displayName,
                                   final String key,
                                   final Class<E> clazz)
  {
    super (midiDevice, displayName, key, new JComboBox<> (clazz.getEnumConstants ()));
    getComboBox ().addItemListener (new ValueItemListener ());
    setValueOnGui ((E) midiDevice.get (key));
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // VALUE COMPONENT
  // VALUE COMPONENT LISTENER
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  protected final JComboBox<E> getComboBox ()
  {
    return (JComboBox<E>) getValueComponent ();
  }
  
  private class ValueItemListener
    implements ItemListener
  {

    @Override
    public void itemStateChanged (ItemEvent ie)
    {
      if (ie.getStateChange () == ItemEvent.SELECTED)
      {
        E item = (E) ie.getItem ();
        if (item == null)
          // This should be fixed, should the exception be thrown :-).
          throw new IllegalStateException ();
        JMidiDeviceParameter_Enum.this.setDataValue (item);
      }
    }
      
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // DATA VALUE CHANGED [SUPER-CLASS: MIDI DEVICE LISTENER]
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  @Override
  protected void dataValueChanged (E newDataValue)
  {
    super.dataValueChanged (newDataValue);
    setValueOnGui (newDataValue);
  }
    
  private void setValueOnGui (final E newDataValue)
  {
    if (! SwingUtilities.isEventDispatchThread ())
    {
      SwingUtilities.invokeLater (() -> setValueOnGui (newDataValue));
    }
    else
    {
      getComboBox ().setSelectedItem (newDataValue);
      getComboBox ().setEnabled (newDataValue != null);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
}
