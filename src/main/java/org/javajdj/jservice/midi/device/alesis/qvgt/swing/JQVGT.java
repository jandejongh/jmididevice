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

import java.awt.HeadlessException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/** A patch editor for the Alesis Quadraverb GT.
 *
 * @author Jan de Jongh {@literal <jfcmdejongh@gmail.com>}
 * 
 */
public final class JQVGT
{

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // LOGGING
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  private static final Logger LOG = Logger.getLogger (JQVGT.class.getName ());
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTORS / CLONING / FACTORIES
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  /** Forbids construction.
   * 
   */
  private JQVGT ()
  {
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // MAIN
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  public final static void main (final String[] args)
  {
    SwingUtilities.invokeLater (() ->
    {
      try
      {
        final JQVGTFrame frame = new JQVGTFrame ();
        frame.setExtendedState (JFrame.MAXIMIZED_BOTH);
        frame.setVisible (true);
      }
      catch (HeadlessException he)
      {
        LOG.log (Level.WARNING, "No GUI; bailing out!");
      }
    });
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // END OF FILE
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
}
