package org.javajdj.jservice.midi.device.swing;

import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.javajdj.jservice.midi.device.MidiDevice;

public class JMidiDeviceIntParameter_Slider
  extends JMidiDeviceParameter<Integer>
{

  private static JSlider createValueComponent (final int minValue, final int maxValue)
  {
    return new JSlider (minValue, maxValue);
  }

  public final JSlider getSlider ()
  {
    return (JSlider) getValueComponent ();
  }
  
  public JMidiDeviceIntParameter_Slider (final MidiDevice midiDevice,
                                         final String displayName,
                                         final String key,
                                         final int minValue,
                                         final int maxValue)
  {
    super (midiDevice, displayName, key, createValueComponent (minValue, maxValue));
    getSlider ().setMajorTickSpacing (maxValue - minValue);
    // getSlider ().setPaintTicks (true);
    getSlider ().setPaintLabels (true);
    getSlider ().addChangeListener (new ValueChangeListener ());
  }
  
  private class ValueChangeListener
    implements ChangeListener
  {

    @Override
    public void stateChanged (ChangeEvent ce)
    {
      if (ce.getSource () == JMidiDeviceIntParameter_Slider.this.getSlider ())
      {
        if (! JMidiDeviceIntParameter_Slider.this.getSlider ().getValueIsAdjusting ())
        {
          final int newValue = JMidiDeviceIntParameter_Slider.this.getSlider ().getValue ();
          JMidiDeviceIntParameter_Slider.this.getSlider ().setToolTipText (Integer.toString (newValue));
          JMidiDeviceIntParameter_Slider.this.setDataValue (newValue);
        }
      }
    }
  }
  
  @Override
  protected void dataValueChanged (final Integer newDataValue)
  {
    super.dataValueChanged (newDataValue);
    setValueOnGui (newDataValue);
  }
    
  private void setValueOnGui (final int value)
  {
    if (! SwingUtilities.isEventDispatchThread ())
    {
      SwingUtilities.invokeLater (() -> setValueOnGui (value));
    }
    else
    {
      if (value >= getSlider ().getMinimum () && value <= getSlider ().getMaximum ())
      {
        getSlider ().setEnabled (true);
        getSlider ().setValue (value);
      }
      else
      {
        getSlider ().setEnabled (false);
      }
    }
  }
  
}
