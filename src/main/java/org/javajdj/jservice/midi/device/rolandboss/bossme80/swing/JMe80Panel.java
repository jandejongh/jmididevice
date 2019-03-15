package org.javajdj.jservice.midi.device.rolandboss.bossme80.swing;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;
//import nl.jdj.jmidiswing.device.rolandboss.bossme80.swing.fxs.JMe80Panel_AMP;
//import nl.jdj.jmidiswing.device.rolandboss.bossme80.swing.fxs.JMe80Panel_COMP;
//import nl.jdj.jmidiswing.device.rolandboss.bossme80.swing.fxs.JMe80Panel_CTL;
//import nl.jdj.jmidiswing.device.rolandboss.bossme80.swing.fxs.JMe80Panel_DELAY;
//import nl.jdj.jmidiswing.device.rolandboss.bossme80.swing.fxs.JMe80Panel_EQ_FX2;
//import nl.jdj.jmidiswing.device.rolandboss.bossme80.swing.fxs.JMe80Panel_FOOT_VOLUME;
//import nl.jdj.jmidiswing.device.rolandboss.bossme80.swing.fxs.JMe80Panel_MOD;
//import nl.jdj.jmidiswing.device.rolandboss.bossme80.swing.fxs.JMe80Panel_NS;
//import nl.jdj.jmidiswing.device.rolandboss.bossme80.swing.fxs.JMe80Panel_OD_DS;
//import nl.jdj.jmidiswing.device.rolandboss.bossme80.swing.fxs.JMe80Panel_PEDAL_FX;
//import nl.jdj.jmidiswing.device.rolandboss.bossme80.swing.fxs.JMe80Panel_REVERB;
//import nl.jdj.jmidiswing.device.rolandboss.bossme80.swing.sys.JMe80Panel_SYS;
//import nl.jdj.jmidiswing.device.swing.JMidiDevicePanel;
import org.javajdj.jservice.midi.MidiService;
import org.javajdj.jservice.midi.device.MidiDevice;
import org.javajdj.jservice.midi.swing.JMidiService;
import org.javajdj.jservice.midi.swing.JRawMidiService;

/** A {@link JPanel} for controlling and monitoring a {@link VirtualITSStation}.
 *
 * @author Jan de Jongh, TNO
 * 
 */
public class JMe80Panel
  extends JPanel
{

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // SERIALIZATION
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private static final long serialVersionUID = -2670414054463543149L;

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // LOGGING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private static final Logger LOG = Logger.getLogger (JMe80Panel.class.getName ());

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTORS / FACTORIES / CLONING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public JMe80Panel (final MidiService midiService, final MidiDevice midiDevice)
  {
    
    // super (midiDevice);
    super ();
    
    if (midiDevice == null)
      throw new IllegalArgumentException ();
    this.midiDevice = midiDevice;
    setLayout (new GridLayout (3, 5, 6, 6));
    final JPanel midiServicePanel;
    if (midiService == null)
      midiServicePanel = new JMidiService ();
    else if (midiService instanceof JMidiService)
      midiServicePanel = (JPanel) midiService;
    else if (midiService instanceof JRawMidiService)
      midiServicePanel = (JPanel) midiService;
    else
      midiServicePanel = new JRawMidiService (midiService);
    addBorderSYSGroup (midiServicePanel, "MIDI");
    add (midiServicePanel);

    final JPanel jMe80JPanel = new JMe80Panel_Device (midiDevice);
    addBorderSYSGroup (jMe80JPanel, "ME-80 [Device]");
    add (jMe80JPanel);

    final JPanel jMe80JPanel_SYS = new JMe80Panel_SYS (midiDevice);
    addBorderSYSGroup (jMe80JPanel_SYS, "ME-80 [System]");
    add (jMe80JPanel_SYS);

    final JPanel jMe80PatchPanel = new JMe80Panel_PatchSelector (midiDevice);
    addBorderSYSGroup (jMe80PatchPanel, "ME-80 [Patch]");
    add (jMe80PatchPanel);

    final JPanel jMe80LibrarianPanel = new JMe80Panel_LIB (midiDevice);
    addBorderLIBGroup (jMe80LibrarianPanel, "ME-80 [Lib]");
    add (jMe80LibrarianPanel);
    
    final JPanel jMe80JPanel_COMP = new JMe80Panel_COMP (midiDevice);
    addBorderFXGroup (jMe80JPanel_COMP, "COMP");
    add (jMe80JPanel_COMP);

    final JPanel jMe80JPanel_OD_DS = new JMe80Panel_OD_DS (midiDevice);
    addBorderFXGroup (jMe80JPanel_OD_DS, "OD/DS");
    add (jMe80JPanel_OD_DS);
    
    final JPanel jMe80JPanel_AMP = new JMe80Panel_AMP (midiDevice);
    addBorderFXGroup (jMe80JPanel_AMP, "AMP");
    add (jMe80JPanel_AMP);
    
    final JPanel jMe80JPanel_MISC = new JPanel ();
    jMe80JPanel_MISC.setLayout (new GridLayout (3, 1, 5, 5));
    {
      
      final JPanel jMe80JPanel_PEDAL_FX = new JMe80Panel_PEDAL_FX (midiDevice);
      addBorderFXGroup (jMe80JPanel_PEDAL_FX, "PEDAL FX");
      jMe80JPanel_MISC.add (jMe80JPanel_PEDAL_FX);
    
      final JPanel jMe80JPanel_NS = new JMe80Panel_NS (midiDevice);
      addBorderFXGroup (jMe80JPanel_NS, "NS");
      jMe80JPanel_MISC.add (jMe80JPanel_NS);

      final JPanel jMe80JPanel_FOOT_VOLUME = new JMe80Panel_FOOT_VOLUME (midiDevice);
      addBorderFXGroup (jMe80JPanel_FOOT_VOLUME, "FOOT VOLUME");
      jMe80JPanel_MISC.add (jMe80JPanel_FOOT_VOLUME);

    }
    // addBorderFXGroup (jMe80JPanel_MISC, "MISC");
    add (jMe80JPanel_MISC);
    
    add (new JPanel ());
    
    final JPanel jMe80JPanel_MOD = new JMe80Panel_MOD (midiDevice);
    addBorderFXGroup (jMe80JPanel_MOD, "MOD");
    add (jMe80JPanel_MOD);

    final JPanel jMe80JPanel_EQ_FX2 = new JMe80Panel_EQ_FX2 (midiDevice);
    addBorderFXGroup (jMe80JPanel_EQ_FX2, "EQ/FX2");
    add (jMe80JPanel_EQ_FX2);

    final JPanel jMe80JPanel_DELAY = new JMe80Panel_DELAY (midiDevice);
    addBorderFXGroup (jMe80JPanel_DELAY, "DELAY");
    add (jMe80JPanel_DELAY);

    final JPanel jMe80JPanel_REVERB = new JMe80Panel_REVERB (midiDevice);
    addBorderFXGroup (jMe80JPanel_REVERB, "REVERB");
    add (jMe80JPanel_REVERB);
    
    final JPanel jMe80JPanel_CTL = new JMe80Panel_CTL (midiDevice);
    addBorderFXGroup (jMe80JPanel_CTL, "CTL");
    add (jMe80JPanel_CTL);
    
  }
  
  private final static Color SYS_GROUP_BORDER_COLOR = Color.BLACK;
  
  private void addBorderSYSGroup (final JPanel sysGroupPanel, final String title)
  {
    if (sysGroupPanel == null || title == null)
      throw new IllegalArgumentException ();
    final Border lineBorder = BorderFactory.createLineBorder (SYS_GROUP_BORDER_COLOR, 2, true);
    final Border border = BorderFactory.createTitledBorder (lineBorder, title);
    sysGroupPanel.setBorder (border);
  }
  
  private final static Color LIB_GROUP_BORDER_COLOR = Color.YELLOW.darker ();
  
  private void addBorderLIBGroup (final JPanel libGroupPanel, final String title)
  {
    if (libGroupPanel == null || title == null)
      throw new IllegalArgumentException ();
    final Border lineBorder = BorderFactory.createLineBorder (LIB_GROUP_BORDER_COLOR, 2, true);
    final Border border = BorderFactory.createTitledBorder (lineBorder, title);
    libGroupPanel.setBorder (border);
  }
  
  private final static Color FX_GROUP_BORDER_COLOR = Color.BLUE;
  
  private void addBorderFXGroup (final JPanel fxGroupPanel, final String title)
  {
    if (fxGroupPanel == null || title == null)
      throw new IllegalArgumentException ();
    final Border lineBorder = BorderFactory.createLineBorder (FX_GROUP_BORDER_COLOR, 2, true);
    final Border border = BorderFactory.createTitledBorder (lineBorder, title);
    fxGroupPanel.setBorder (border);
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
  
}
