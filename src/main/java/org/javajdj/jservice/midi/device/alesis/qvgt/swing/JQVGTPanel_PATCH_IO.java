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
package org.javajdj.jservice.midi.device.alesis.qvgt.swing;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.Function;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;
import org.javajdj.jservice.midi.device.MidiDevice;
import org.javajdj.jservice.midi.device.alesis.qvgt.MidiDevice_QVGT;
import org.javajdj.jservice.midi.device.alesis.qvgt.Patch_QGVT;
import org.javajdj.jservice.midi.device.swing.parameter.JMidiDeviceParameter;
import org.javajdj.swing.DefaultMouseListener;
import org.javajdj.swing.JColorCheckBox;

/** A {@link JPanel} for patch (Edit Buffer) I/O with an Alesis Quadraverb GT {@link MidiDevice}.
 * 
 * <p>
 * Auxiliary file to {@link JQVGTPanel}.
 * 
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 */
final class JQVGTPanel_PATCH_IO
  extends JMidiDeviceParameter<Patch_QGVT>
{

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // LOGGING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private static final Logger LOG = Logger.getLogger (JQVGTPanel_PATCH_IO.class.getName ());
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTORS / FACTORIES / CLONING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
  private final Function<Object, Color> BUTTON_COLOR_FUNCTION = (final Object t) -> Color.blue.darker ();
      
  public JQVGTPanel_PATCH_IO (final MidiDevice midiDevice)
  {
      
    super (midiDevice, null, MidiDevice_QVGT.EDIT_BUFFER_NAME, null);
      
    setLayout (new GridLayout (10, 2, 2, 2));
      
    add (new JLabel ("Write Patch > QVGT"));
    final JComponent jWrite = new JColorCheckBox (BUTTON_COLOR_FUNCTION);
    jWrite.addMouseListener (new DefaultMouseListener ()
    {
      @Override
      public void mouseClicked (final MouseEvent me)
      {
        final Patch_QGVT patch = (Patch_QGVT) getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_NAME);
        if (patch == null)
        {
          JOptionPane.showMessageDialog (null,
            "No patch data!",
            "Problem",
            JOptionPane.ERROR_MESSAGE);
            return;
        }
        final String initialName = patch.getName ();
        final JTargetPatchSelectorDialog_QVGT dialog = new JTargetPatchSelectorDialog_QVGT (null, initialName, null);
        dialog.setLocationRelativeTo (null);
        dialog.setVisible (true);
        if (dialog.isOk ())
        {
          final String patchName = dialog.getPatchName ();
          final int patchNumber = dialog.getPatchNumber ();
          ((MidiDevice_QVGT) getMidiDevice ()).writePatchToDevice (patch.withName (patchName), patchNumber);
          JOptionPane.showMessageDialog (null,
            "Saved Patch to Alesis Quadraverb GT::" + patchNumber + "!",
            "Message",
            JOptionPane.INFORMATION_MESSAGE);
        }
      }
    });
    add (jWrite);
    
    add (new JLabel ());
    add (new JLabel ());
      
    add (new JLabel ("Load Patch"));
    final JComponent jLoadPatch = new JColorCheckBox (BUTTON_COLOR_FUNCTION);
    jLoadPatch.addMouseListener (new DefaultMouseListener ()
    {
      @Override
      public void mouseClicked (final MouseEvent me)
      {
        final JFileChooser jfc = new JFileChooser (FileSystemView.getFileSystemView ().getHomeDirectory ());
        jfc.setDialogTitle ("Load Patch");
        final int returnValue = jfc.showOpenDialog (null);
        if (returnValue == JFileChooser.APPROVE_OPTION)
        {
          final File selectedFile = jfc.getSelectedFile ();
          try (final FileInputStream file = new FileInputStream (selectedFile))
          {
            final byte[] patchBytes = new byte[Patch_QGVT.DECODED_PATCH_SIZE];
            final int bytesRead = file.read (patchBytes, 0, Patch_QGVT.DECODED_PATCH_SIZE);
            // LOG.log (Level.INFO, "Bytes read: {0}", bytesRead);
            if (bytesRead != Patch_QGVT.DECODED_PATCH_SIZE)
            {
              JOptionPane.showMessageDialog (null,
                "Parse Error [not a patch file for Alesis Quadraverb GT (wrong size)?]: " + selectedFile.getAbsolutePath (),
                "Problem",
                JOptionPane.ERROR_MESSAGE);
              return;
            }
            final Patch_QGVT newPatch;
            try
            {
              newPatch = Patch_QGVT.fromBytes (patchBytes);
            }
            catch (IllegalArgumentException iae)
            {
              JOptionPane.showMessageDialog (null,
                "Parse Error [not a patch file for Alesis Quadraverb GT (illegal patch)?]: " + selectedFile.getAbsolutePath (),
                "Problem",
                JOptionPane.ERROR_MESSAGE);
              return;
            }
            getMidiDevice ().put (MidiDevice_QVGT.EDIT_BUFFER_NAME, newPatch);
            JQVGTPanel_PATCH_IO.this.jPatchFile.setText (selectedFile.getName ());
            JOptionPane.showMessageDialog (null,
              "Loaded Patch from " + selectedFile.getAbsolutePath (),
              "Message",
              JOptionPane.INFORMATION_MESSAGE);            
          }
          catch (FileNotFoundException fnfe)
          {
            JOptionPane.showMessageDialog (null,
              "File Not Found: " + selectedFile.getAbsolutePath (),
              "Problem",
              JOptionPane.ERROR_MESSAGE);
          }
          catch (IOException ie)
          {
            JOptionPane.showMessageDialog (null,
              "I/O Error: " + selectedFile.getAbsolutePath (),
              "Problem",
              JOptionPane.ERROR_MESSAGE);
          }
        }
      }
    });
    add (jLoadPatch);
      
    add (new JLabel ("Save Patch"));
    final JComponent jSavePatch = new JColorCheckBox (BUTTON_COLOR_FUNCTION);
    jSavePatch.addMouseListener (new DefaultMouseListener ()
    {
      @Override
      public void mouseClicked (final MouseEvent me)
      {
        final Patch_QGVT patch = (Patch_QGVT) JQVGTPanel_PATCH_IO.this.getMidiDevice ().get (MidiDevice_QVGT.EDIT_BUFFER_NAME);
        if (patch == null)
        {
          JOptionPane.showMessageDialog (null,
            "No patch data!",
            "Problem",
            JOptionPane.ERROR_MESSAGE);
            return;
        }
        final JFileChooser jfc = new JFileChooser (FileSystemView.getFileSystemView ().getHomeDirectory ());
        jfc.setDialogTitle ("Save Patch");
        final int returnValue = jfc.showOpenDialog (null);
        if (returnValue == JFileChooser.APPROVE_OPTION)
        {
          final File selectedFile = jfc.getSelectedFile ();
          try (final FileOutputStream file = new FileOutputStream (selectedFile))
          {
            file.write (patch.getDecodedBytes ());
            JQVGTPanel_PATCH_IO.this.jPatchFile.setText (selectedFile.getName ());
            JOptionPane.showMessageDialog (null,
              "Saved Patch in " + selectedFile.getAbsolutePath (),
              "Message",
              JOptionPane.INFORMATION_MESSAGE);            
          }
          catch (IOException ie)
          {
            JOptionPane.showMessageDialog (null,
              "I/O Error: " + selectedFile.getAbsolutePath (),
              "Problem",
              JOptionPane.ERROR_MESSAGE);
          }
        }
      }
    });
    add (jSavePatch);
      
    add (new JLabel ("Patch File"));
    this.jPatchFile = new JTextField ();
    this.jPatchFile.setOpaque (false);
    this.jPatchFile.setEditable (false);
    add (this.jPatchFile);
      
    add (new JLabel ());
    add (new JLabel ());
      
    add (new JLabel ());
    add (new JLabel ());
      
    add (new JLabel ());
    add (new JLabel ());
      
    add (new JLabel ());
    add (new JLabel ());
      
    add (new JLabel ());
    add (new JLabel ());
      
    // XXX
    // What was this all about?? Perhaps check ME-80 implementation...
    // setDataValue ((byte[]) getMidiDevice ().get (MidiDevice_Me80_Base.TEMPORARY_PATCH_NAME));
      
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PATCH FILE [NAME] COMPONENT
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final JTextField jPatchFile;
    
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
}
