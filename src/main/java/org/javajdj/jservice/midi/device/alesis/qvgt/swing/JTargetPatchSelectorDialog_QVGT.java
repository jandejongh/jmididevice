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
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import org.javajdj.jservice.midi.device.alesis.qvgt.MidiDevice_QVGT;
import org.javajdj.swing.JTextFieldListener;

/** A {@link JDialog} requesting a target patch slot on an Alesis Quadraverb GT, as well as a patch name.
 *
 * <p>
 * Typically required for saving a patch on the Alesis Quadraverb GT.
 * Only saving patches to slots 0 through 99 is supported (i.e., not to the Edit Buffer).
 * 
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 */
public class JTargetPatchSelectorDialog_QVGT
  extends JDialog
{

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTORS / FACTORIES / CLONING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  /** Creates the dialog.
   * 
   * @param frame              The frame in which the dialog resides, see {@link JDialog#JDialog(java.awt.Frame)},
   *                             may be {@code null}.
   * @param initialName        The initial name of the patch, may be {@code null}.
   * @param initialPatchNumber The initial patch number to show as pre-selection (may be {@code null}).
   *
   * @throws IllegalArgumentException If the initial patch number is out of range
   *                                  (when non-{@code null},
   *                                  must be between zero {@link MidiDevice_QVGT#EDIT_BUFFER_PROGRAM_NUMBER} - 1 inclusive).
   * 
   */
  public JTargetPatchSelectorDialog_QVGT
  ( final Frame frame,
    final String initialName,
    final Integer initialPatchNumber)
  {
    
    super (frame, "Select Target Patch Slot on Alesis Quadraverb GT", true);
    
    if (initialPatchNumber != null
      && (initialPatchNumber < 0 || initialPatchNumber >= MidiDevice_QVGT.EDIT_BUFFER_PROGRAM_NUMBER))
      throw new IllegalArgumentException ();
    
    getContentPane ().setLayout (new BoxLayout (getContentPane (), BoxLayout.Y_AXIS));
    
    this.jName = new JTextField ();
    this.jName.setOpaque (false);
    if (initialName != null)
      this.jName.setText (initialName);
    final Border nameLineBorder = BorderFactory.createLineBorder (Color.orange, 2, true);
    final Border nameBorder = BorderFactory.createCompoundBorder (
      BorderFactory.createEmptyBorder (20, 20, 20, 20),
      BorderFactory.createTitledBorder (nameLineBorder, "Patch Name"));
    this.jName.setBorder (nameBorder);
    getContentPane ().add (this.jName);
    
    final JTextField jPatchNumber = new JTextField ();
    jPatchNumber.setOpaque (false);
    if (initialPatchNumber != null)
      jPatchNumber.setText (Integer.toString (initialPatchNumber));
    JTextFieldListener.addJTextFieldListener (jPatchNumber, new JTextFieldListener ()
    {
      @Override
      public final void actionPerformed ()
      {
        final String newStringValue = jPatchNumber.getText ();
        final int newPatchNumber;
        try
        {
          newPatchNumber = Integer.parseInt (newStringValue);
        }
        catch (NumberFormatException nfe)
        {
          JOptionPane.showMessageDialog (null,
            "Invalid number: " + newStringValue + "!",
            "Problem",
            JOptionPane.ERROR_MESSAGE);
          return;
        }
        if (newPatchNumber < 0 || newPatchNumber >= MidiDevice_QVGT.EDIT_BUFFER_PROGRAM_NUMBER)
        {
          JOptionPane.showMessageDialog (null,
            "Patch Slot Number "
              + newPatchNumber
              + " out of range (must be between 0 and "
              + (MidiDevice_QVGT.EDIT_BUFFER_PROGRAM_NUMBER -1)
              + " inclusive!",
            "Problem",
            JOptionPane.ERROR_MESSAGE);
          return;
        }
        JTargetPatchSelectorDialog_QVGT.this.patchNumber = newPatchNumber;
      }
    });
    final Border patchNumberLineBorder = BorderFactory.createLineBorder (Color.orange, 2, true);
    final Border patchNumberBorder = BorderFactory.createCompoundBorder (
      BorderFactory.createEmptyBorder (20, 20, 20, 20),
      BorderFactory.createTitledBorder (patchNumberLineBorder, "Patch Number"));
    jPatchNumber.setBorder (patchNumberBorder);
    getContentPane ().add (jPatchNumber);
    
    final JPanel okCancelPanel = new JPanel ();
    okCancelPanel.setLayout (new GridLayout (1, 5, 10, 10));
    okCancelPanel.add (new JLabel ());
    final JButton okButton = new JButton ("OK");
    okButton.addActionListener ((final ActionEvent ae) ->
    {
      if (JTargetPatchSelectorDialog_QVGT.this.patchNumber == null)
        JOptionPane.showMessageDialog (null,
          "No (valid) patch number entered!",
          "Problem",
          JOptionPane.ERROR_MESSAGE);
      else
      {
        JTargetPatchSelectorDialog_QVGT.this.ok = true;
        JTargetPatchSelectorDialog_QVGT.this.dispose ();
      }
    });
    okCancelPanel.add (okButton);
    okCancelPanel.add (new JLabel ());
    final JButton cancelButton = new JButton ("Cancel");
    cancelButton.addActionListener ((final ActionEvent ae) ->
    {
      JTargetPatchSelectorDialog_QVGT.this.ok = false;
      JTargetPatchSelectorDialog_QVGT.this.dispose ();
    });
    okCancelPanel.add (cancelButton);
    okCancelPanel.add (new JLabel ());
    final Border okCancelBorder = BorderFactory.createEmptyBorder (20, 20, 20, 20);
    okCancelPanel.setBorder (okCancelBorder);
    getContentPane ().add (okCancelPanel);
    
    pack ();
    
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // OK
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private boolean ok = false;
  
  /** Returns true if the dialog was closed and disposed of through the OK button, and
   *  parameters entered were valid.
   * 
   * @return True if the user dismissed the dialog with the "OK" button with valid parameters entered.
   * 
   */
  public final boolean isOk ()
  {
    return this.ok;
  }
    
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PATCH NAME
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private final JTextField jName;
  
  /** Returns the patch name.
   * 
   * <p>
   * Null is returned if the dialog was somehow canceled.
   * 
   * @return The patch name entered, {@code null} if the dialog was canceled.
   * 
   * @see #isOk
   * 
   */
  public final String getPatchName ()
  {
    if (this.ok)
      return this.jName.getText ();
    else
      return null;
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // [SELECTED] PATCH NUMBER
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private Integer patchNumber;
  
  /** Returns the selected patch number.
   * 
   * <p>
   * Null is returned if the dialog was somehow canceled.
   * 
   * @return The selected patch number, {@code null} if the dialog was canceled.
   * 
   * @see #isOk
   * 
   */
  public final Integer getPatchNumber ()
  {
    if (this.ok)
      return this.patchNumber;
    else
      return null;
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
}
