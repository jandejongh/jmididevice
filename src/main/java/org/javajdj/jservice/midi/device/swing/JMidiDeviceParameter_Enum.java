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
import java.util.Collections;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import org.javajdj.jservice.midi.device.MidiDevice;
import org.javajdj.swing.SwingUtilsJdJ;

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
  
  /** Constructs the component.
   * 
   * @param midiDevice      The MIDI device, non-{@code null}.
   * @param displayName     The name to put into a {@link JLabel}; if {@code null}, no parameter label is created.
   * @param key             The parameter key (must exist on the {@code midiDevice}).
   * @param clazz           The {@link Class} of the parameter value.
   * 
   * @throws IllegalArgumentException If the device is {@code null}, or the key is unregistered at the MIDI device.
   * 
   * @see JMidiDeviceParameter
   * 
   */
  public JMidiDeviceParameter_Enum (final MidiDevice midiDevice,
                                   final String displayName,
                                   final String key,
                                   final Class<E> clazz)
  {
    super (midiDevice, displayName, key, new JComboBox<> (clazz.getEnumConstants ()));
    getComboBox ().setEditable (false); // User is not allowed to modify the entries.
    getComboBox ().addItemListener (new ValueItemListener ());
    dataValueChanged (Collections.singletonMap (getKey (), getDataValue ()));
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // VALUE COMPONENT
  // VALUE COMPONENT LISTENER
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  /** Returns the value component, a {@link JComboBox}.
   * 
   * @return The value component, non-{@code null}.
   * 
   * @see #getValueComponent
   * 
   */
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
      if (isReadOnly ())
        return;
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
  
  /** Sets the new value on the value component.
   * 
   * @param newDataValue The new value; may be {@code null}.
   * 
   * @see #getValueComponent
   * @see #getComboBox
   * 
   */
  @Override
  protected final void dataValueChanged (final E newDataValue)
  {
    super.dataValueChanged (newDataValue);
    SwingUtilsJdJ.invokeOnSwingEDT (() -> getComboBox ().setSelectedItem (newDataValue));
  }
    
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
}
