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
package org.javajdj.jservice.midi.device.swing.parameter;

import java.util.Collections;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.javajdj.jservice.midi.device.MidiDevice;
import org.javajdj.swing.JTextFieldListener;
import org.javajdj.swing.SwingUtilsJdJ;

/** A {@link JMidiDeviceParameter} for an {@link String}-valued parameter.
 * 
 * <p>
 * The value is shown in a {@link JTextField}.
 * 
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 */
public class JMidiDeviceParameter_String
  extends JMidiDeviceParameter<String>
{

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // LOGGING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private static final Logger LOG = Logger.getLogger (JMidiDeviceParameter_String.class.getName ());
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTORS / FACTORIES / CLONING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  /** Constructs the component.
   * 
   * @param midiDevice  The MIDI device, non-{@code null}.
   * @param displayName The name to put into a {@link JLabel}; if {@code null}, no parameter label is created.
   * @param key         The parameter key (must exist on the {@code midiDevice}).
   * @param columns     The optional number of columns to use to lay out the {@link JTextField}
   *                      value component, ignored if {@code null}, see {@link JTextField#JTextField(int)}.
   * 
   * @throws IllegalArgumentException If the device is {@code null}, or the key is unregistered at the MIDI device.
   * 
   * @see JMidiDeviceParameter
   * 
   */
  public JMidiDeviceParameter_String (final MidiDevice midiDevice,
                                     final String displayName,
                                     final String key,
                                     final Integer columns)
  {
    super (midiDevice, displayName, key, columns != null ? new JTextField (columns) : new JTextField ());
    getTextField ().setOpaque (false);
    JTextFieldListener.addJTextFieldListener (getTextField (), this.valueComponentListener);
    dataValueChanged (Collections.singletonMap (getKey (), getDataValue ()));
  }
  
  /** Constructs the component.
   * 
   * @param midiDevice  The MIDI device, non-{@code null}.
   * @param displayName The name to put into a {@link JLabel}; if {@code null}, no parameter label is created.
   * @param key         The parameter key (must exist on the {@code midiDevice}).
   * 
   * @throws IllegalArgumentException If the device is {@code null}, or the key is unregistered at the MIDI device.
   * 
   * @see JMidiDeviceParameter
   * 
   */
  public JMidiDeviceParameter_String (final MidiDevice midiDevice,
                                     final String displayName,
                                     final String key)
  {
    this (midiDevice, displayName, key, null);
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // VALUE COMPONENT
  // VALUE COMPONENT LISTENER
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  /** Returns the value component, a {@link JTextField}.
   * 
   * @return The value component, non-{@code null}.
   * 
   * @see #getValueComponent
   * 
   */
  public final JTextField getTextField ()
  {
    return (JTextField) getValueComponent ();
  }
  
  private final JTextFieldListener valueComponentListener = new JTextFieldListener ()
  {
    @Override
    public void actionPerformed ()
    {
      if (isReadOnly ())
        return;
      final String newValue = JMidiDeviceParameter_String.this.getTextField ().getText ();
      JMidiDeviceParameter_String.this.setDataValue (newValue);
    }
  };
  
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
   * @see #getTextField
   * 
   */
  @Override
  protected final void dataValueChanged (final String newDataValue)
  {
    super.dataValueChanged (newDataValue);
    SwingUtilsJdJ.invokeOnSwingEDT (() -> getTextField ().setText (newDataValue));
  }
    
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
}
