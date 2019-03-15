package org.javajdj.jservice.midi.device.swing;

import java.awt.GridLayout;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.javajdj.jservice.midi.device.MidiDevice;
import org.javajdj.jservice.midi.device.MidiDeviceListener;

public class JMidiDeviceParameter<C>
  extends JPanel
{
  
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

  private final MidiDevice midiDevice;
  
  private final String displayName;
  
  private final String key;
  
  private final JComponent jValueComponent;
  
  public final MidiDevice getMidiDevice ()
  {
    return this.midiDevice;
  }

  public final String getDisplayName ()
  {
    return this.displayName;
  }

  public final String getKey ()
  {
    return this.key;
  }

  protected final JComponent getValueComponent ()
  {
    return this.jValueComponent;
  }
  
  protected void dataValueChanged (C newDataValue)
  {
  }
  
  protected void setDataValue (final C newDataValue)
  {
    getMidiDevice ().put (getKey (), newDataValue);
  }
  
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
  
}
