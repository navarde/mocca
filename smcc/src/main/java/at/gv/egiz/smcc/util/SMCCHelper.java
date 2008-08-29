/*
* Copyright 2008 Federal Chancellery Austria and
* Graz University of Technology
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package at.gv.egiz.smcc.util;

import java.util.Locale;
import java.util.Map;

import javax.smartcardio.ATR;
import javax.smartcardio.Card;
import javax.smartcardio.CardTerminal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.gv.egiz.smcc.CardNotSupportedException;
import at.gv.egiz.smcc.SignatureCard;
import at.gv.egiz.smcc.SignatureCardFactory;

public class SMCCHelper {

  public final static int NO_CARD = 0;
  public final static int PC_SC_NOT_SUPPORTED = 1;
  public final static int TERMINAL_NOT_PRESENT = 2;
  public final static int CARD_NOT_SUPPORTED = 3;
  public final static int CARD_FOUND = 4;

  private final static Log log = LogFactory.getLog(SMCCHelper.class);

  protected SmartCardIO smartCardIO = new SmartCardIO();
  protected int resultCode = NO_CARD;
  protected SignatureCard signatureCard = null;
  protected static boolean useSWCard = false;

  public SMCCHelper() {
    update();
  }

  public void update() {
    update(-1);
  }

  public void update(int sleep) {
    SignatureCardFactory factory = SignatureCardFactory.getInstance();
    if (useSWCard) {
      try {
        signatureCard = factory.createSignatureCard(null);
        resultCode = CARD_FOUND;
      } catch (CardNotSupportedException e) {
        resultCode = CARD_NOT_SUPPORTED;
        signatureCard = null;
      }
      return;
    }
    signatureCard = null;
    resultCode = NO_CARD;
    // find pcsc support
    if (smartCardIO.isPCSCSupported()) {
      // find supported card
      if (smartCardIO.isTerminalPresent()) {
        Map<CardTerminal, Card> newCards = null;
        if (sleep > 0) {
          smartCardIO.waitForInserted(sleep);

        }
        newCards = smartCardIO.getCards();
        for (CardTerminal cardTerminal : newCards.keySet()) {
          try {
            Card c = newCards.get(cardTerminal);
            if (c == null) {
              throw new CardNotSupportedException();
            }
            signatureCard = factory.createSignatureCard(c);
            ATR atr = newCards.get(cardTerminal).getATR();
            log.trace("Found supported card (" + signatureCard.toString() + ") "
                + "in terminal '" + cardTerminal.getName() + "', ATR = "
                + toString(atr.getHistoricalBytes()) + ".");
            resultCode = CARD_FOUND;
            break;

          } catch (CardNotSupportedException e) {
            Card c = newCards.get(cardTerminal);
            if (c != null) {
              ATR atr = c.getATR();
              log.info("Found unsupported card" + " in terminal '"
                  + cardTerminal.getName() + "', ATR = "
                  + toString(atr.getHistoricalBytes()) + ".");
            } else {
              log.info("Found unsupported card in terminal '"
                  + cardTerminal.getName() + "' without ATR");
            }
            resultCode = CARD_NOT_SUPPORTED;
          }
        }
      } else {
        resultCode = TERMINAL_NOT_PRESENT;
      }
    } else {
      resultCode = PC_SC_NOT_SUPPORTED;
    }
  }

  public SignatureCard getSignatureCard(Locale locale) {
    if (signatureCard != null) {
      signatureCard.setLocale(locale);
    }
    return signatureCard;
  }

  public int getResultCode() {
    return resultCode;
  }

  public static String toString(byte[] b) {
    StringBuffer sb = new StringBuffer();
    sb.append('[');
    if (b != null && b.length > 0) {
      sb.append(Integer.toHexString((b[0] & 240) >> 4));
      sb.append(Integer.toHexString(b[0] & 15));
      for (int i = 1; i < b.length; i++) {
        sb.append((i % 32 == 0) ? '\n' : ':');
        sb.append(Integer.toHexString((b[i] & 240) >> 4));
        sb.append(Integer.toHexString(b[i] & 15));
      }
    }
    sb.append(']');
    return sb.toString();
  }

  public static boolean isUseSWCard() {
    return useSWCard;
  }

  public static void setUseSWCard(boolean useSWCard) {
    SMCCHelper.useSWCard = useSWCard;
  }
}
