package org.javajdj.jservice.midi.device.swing;

import org.javajdj.swing.DefaultMouseListener;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.SwingUtilities;
import org.javajdj.jservice.midi.device.MidiDevice;
import org.javajdj.swing.JColorCheckBox;

public class JMidiDeviceBooleanParameter
  extends JMidiDeviceParameter<Boolean>
{

  private static JColorCheckBox.JBoolean createValueComponent ()
  {
    final Map<Boolean, Color> colorMap = new HashMap<> ();
    colorMap.put (false, null);
    colorMap.put (true, Color.red);
    return new JColorCheckBox.JBoolean (colorMap);
  }

  protected final JColorCheckBox.JBoolean getCheckBox ()
  {
    return (JColorCheckBox.JBoolean) getValueComponent ();
  }
  
  public JMidiDeviceBooleanParameter (final MidiDevice midiDevice,
                                      final String displayName,
                                      final String key)
  {
    super (midiDevice, displayName, key, createValueComponent ());
    getCheckBox ().addMouseListener (new ValueMouseListener ());
  }
  
  private class ValueMouseListener
    extends DefaultMouseListener
  {
    
    @Override
    public final void mouseClicked (MouseEvent e)
    {
      final Boolean currentValue = JMidiDeviceBooleanParameter.this.getCheckBox ().getDisplayedValue ();
      final boolean newValue = currentValue != null ? (! currentValue) : true;
      JMidiDeviceBooleanParameter.this.setDataValue (newValue);
    }

  }

  @Override
  protected void dataValueChanged (final Boolean newDataValue)
  {
    super.dataValueChanged (newDataValue);
    setValueOnGui (newDataValue);
  }
    
  private void setValueOnGui (final boolean active)
  {
    if (! SwingUtilities.isEventDispatchThread ())
      SwingUtilities.invokeLater (() -> setValueOnGui (active));
    else
      getCheckBox ().setDisplayedValue (active);
  }
  
}
