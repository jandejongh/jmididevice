package org.javajdj.jservice.midi.device.swing;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;
import org.javajdj.jservice.midi.device.MidiDevice;

public class JMidiDeviceEnumParameter<E extends Enum<E>>
  extends JMidiDeviceParameter<E>
{

  protected final JComboBox<E> getComboBox ()
  {
    return (JComboBox<E>) getValueComponent ();
  }
  
  public JMidiDeviceEnumParameter (final MidiDevice midiDevice,
                                   final String displayName,
                                   final String key,
                                   final Class<E> clazz)
  {
    super (midiDevice, displayName, key, new JComboBox<> (clazz.getEnumConstants ()));
    getComboBox ().addItemListener (new ValueItemListener ());
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
        JMidiDeviceEnumParameter.this.setDataValue (item);
      }
    }
      
  }
  
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
    }
  }
  
}
