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

import java.util.logging.Logger;
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
  
  public JMidiDeviceParameter_String (final MidiDevice midiDevice,
                                     final String displayName,
                                     final String key,
                                     final Integer columns)
  {
    super (midiDevice, displayName, key, columns != null ? new JTextField (columns) : new JTextField ());
    getTextField ().setOpaque (false);
    JTextFieldListener.addJTextFieldListener (getTextField (), this.valueComponentListener);
    getTextField ().setText ((String) midiDevice.get (key));
  }
  
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
  
  public final JTextField getTextField ()
  {
    return (JTextField) getValueComponent ();
  }
  
  private final JTextFieldListener valueComponentListener = new JTextFieldListener ()
  {
    @Override
    public void actionPerformed ()
    {
      final String newValue = JMidiDeviceParameter_String.this.getTextField ().getText ();
      // LOG.log (Level.INFO, "New value: {0}.", newValue);
      JMidiDeviceParameter_String.this.getMidiDevice ().put (JMidiDeviceParameter_String.this.getKey (), newValue);
    }
  };
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // DATA VALUE CHANGED [SUPER-CLASS: MIDI DEVICE LISTENER]
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  @Override
  protected void dataValueChanged (final String newDataValue)
  {
    super.dataValueChanged (newDataValue);
    SwingUtilsJdJ.invokeOnSwingEDT (() ->
    {
      getTextField ().setText (newDataValue);
      if (isReadOnly ())
      {
        getTextField ().setEnabled (false);
        getTextField ().setEditable (false);
      }
    });
  }
    
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // READ ONLY
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  @Override
  public synchronized void setReadOnly (final boolean readOnly)
  {
    super.setReadOnly (readOnly);
    if (! isReadOnly ())
      SwingUtilsJdJ.invokeOnSwingEDT (() ->
      {
        setEnabled (false);
        getTextField ().setEnabled (false);
        getTextField ().setEditable (false);
      });
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
}
